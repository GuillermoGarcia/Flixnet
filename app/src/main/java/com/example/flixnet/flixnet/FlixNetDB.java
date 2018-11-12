package com.example.flixnet.flixnet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.example.flixnet.flixnet.Modelos.Pelicula;
import com.google.android.gms.common.data.DataBufferSafeParcelable;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Programación multimedia y dispositivos móviles
 * @author Antonio J.Sánchez
 * @year 2018/19
 *
 */
public class FlixNetDB extends SQLiteOpenHelper {

  // Instancia del objeto y contexto
  private static FlixNetDB instance = null ;
  private static SQLiteDatabase database;
  private static String uid ;

  //
  private Context dbContext ;
  //private List<Lista> listas = null ;

  //
  private FirebaseDatabase fbdb ;

  /**
   * @param context
   * @param name
   * @param factory
   * @param version
   */
  private FlixNetDB(@Nullable Context context, @Nullable String name,
                    @Nullable SQLiteDatabase.CursorFactory factory, int version) {
    super(context, name, factory, version);

    dbContext = context ; // Guardamos el contexto
  }

  /**
   * Utilizamos el patrón de diseño Singleton para crear una única instancia del
   * objeto FlixNetDB, teniendo también de esta manera un único punto de entrada
   * a la base de datos. NOTA: hacemos que el método seá además synchronized
   * para evitar que puedan crearse diferentes instancias al mismo tiempo.
   *
   * @param context
   * @param userID
   * @return
   */
  public static synchronized FlixNetDB getInstance(Context context, String userID) {

    // Si aún no hemos creado la instancia, lo hacemos y llamamos al
    // constructor con los datos apropiados.
    if (instance==null)
      instance = new FlixNetDB(context, "flixnet", null, 2) ;

    // Guardamos la información sobre el identificador de usuario
    uid = userID ;

    // Creamos la base de datos
    database = instance.getWritableDatabase() ;

    // Devolvemos la instancia de la base de datos
    return instance ;
  }

  /**
   * Se lanzará cuando se cree el objeto FlixNetDB. Hemos de tener en cuenta
   * que este método se lanzará automáticamente si la base de datos no existe.
   * Sin embargo, si la base de datos ya existe y las versiones coinciden, se
   * establecerá únicamente una conexión.
   *
   * @param sqliteDB
   */
  @Override
  public void onCreate(SQLiteDatabase sqliteDB) {

    crearDB(sqliteDB) ;     // Creamos la base de datos
    poblarDB(sqliteDB) ;    // Poblamos la base de datos


  }

  /**
   * Se lanzará automáticamente si la base de datos existe y las versiones
   * no coinciden.
   * @param sqLiteDatabase
   * @param i
   * @param i1
   */
  @Override
  public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    // Destruimos las tablas y recreamos la base de datos.
    sqLiteDatabase.execSQL("DROP TABLE lista_pelicula ;");
    sqLiteDatabase.execSQL("DROP TABLE pelicula ;");
    sqLiteDatabase.execSQL("DROP TABLE lista ;");

    // Recreamos la base de datos
    onCreate(sqLiteDatabase) ;
  }

  /**
   * Crea la base de datos local, a partir del script SQL que hemos añadido como
   * recurso a nuestro proyecto.
   * @param db
   */
  private void crearDB(SQLiteDatabase db) {

    try {

      InputStream is = dbContext.getResources().openRawResource(R.raw.flixnet_local);

      // Creamos un buffer de lectura
      BufferedReader buf = new BufferedReader(new InputStreamReader(is));

      // Leemos el contenido y lanzamos las instrucciones
      String line = buf.readLine();

      while (line != null) {
        db.execSQL(line) ;
        line = buf.readLine();
      }

      // Cerramos el buffer de lectura
      buf.close();

      // Cerramos el recurso
      is.close();

    } catch (IOException e) {
      Log.e("FLIXNETDB", "** Se ha producido un error en la creación de la base de datos.") ;
      e.printStackTrace();
    }
  }

  /**
   * Firebase: almacenará películas, géneros, comentarios, listas y cines. Esto es,
   * básicamente toda la información de nuestra aplicación. Si la base de datos no
   * existe, obtenemos los datos la API de TMDB y generamos los documentos necesa-
   * rios.
   *
   * Localmente: guardaremos en el dispositivo nuestras listas e información tempo-
   * ral de las películas contenidas en las mismas.
   *
   * @param db
   */
  private void poblarDB(SQLiteDatabase db) {

    // Obtenemos una instancia de la base de datos de Firebase Realtime Database
    fbdb = FirebaseDatabase.getInstance() ;

    // Obtener una referencia a las listas del usuario logueado
    fbdb.getReference("usuarios/" + uid + "/listas")
        .addListenerForSingleValueEvent(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot ds) {

            // Recorrer el resultado obteniendo los id de cada lista
            for(DataSnapshot item : ds.getChildren()) {
              addLista(item.getValue().toString()) ;// Localizar y obtener información sobre la lista
            }
          }

          @Override
          public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e("FLIXNET:DB", "Se ha producido un error en la comunicación con la base de datos [" + databaseError.getCode() + "]") ;
          }
        });

  }

  private void addLista(final String idLista) {

    fbdb.getReference("listas/" + idLista)
        .addListenerForSingleValueEvent(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot ds) {

            // Guardar en la tabla LISTA
            ContentValues values = new ContentValues() ;
            values.put("idLista",   idLista);
            values.put("idUsuario", uid);
            values.put("nombre",    ds.child("nombre").getValue().toString()) ;

            database.insertOrThrow("lista", null, values) ;

            // Obtenemos las películas
            DataSnapshot films = ds.child("peliculas") ;

            // Iteramos sobre las películas y obtenemos información
            for (DataSnapshot item : films.getChildren()) {

              String idPelicula = item.getValue().toString() ;

              // Obtener información y guardar la película en
              // la tabla PELICULA
              addPelicula(idPelicula) ;

              // Relacionar la película con la lista en la tabla
              // LISTA_PELICULA
              values.clear() ;
              values.put("idLista",    idLista) ;
              values.put("idPelicula", idPelicula) ;
              database.insertOrThrow("lista_pelicula", null, values) ;
            }
          }

          @Override
          public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e("FLIXNET:DB", "Se ha producido un error en la comunicación con la base de datos [" + databaseError.getCode() + "]") ;
          }
        });
  }

  private void addPelicula(final String idPelicula) {

    fbdb.getReference("peliculas/" + idPelicula)
        .addListenerForSingleValueEvent(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot ds) {

            // Guardar en la tabla PELICULA
            ContentValues values = new ContentValues() ;
            values.put("idMDB",   idPelicula);
            values.put("titulo",  ds.child("titulo").getValue().toString());
            values.put("poster",  ds.child("poster").getValue().toString()) ;
            values.put("estreno", ds.child("estreno").getValue().toString()) ;
            values.put("sinopsis",ds.child("sinopsis").getValue().toString()) ;
            values.put("nota",    ds.child("nota").getValue().toString()) ;

            try {
              database.insertOrThrow("pelicula", null, values);
            } catch (SQLiteException e) {
              //
            }
          }

          @Override
          public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e("FLIXNET:DB", "Se ha producido un error en la comunicación con la base de datos [" + databaseError.getCode() + "]") ;
          }
        });
  }

  public List<Pelicula> getPeliculas(){
    List<Pelicula> peliculas = new ArrayList<Pelicula>();
    String[] campos = new String[] {"idMDB", "titulo", "poster", "sinopsis", "estreno", "nota"};
    Cursor c = database.query("pelicula",campos,null, null, null, null, null);
    c.moveToFirst();
    Log.d("Count Select", ""+c.getCount());
    do{
      peliculas.add(new Pelicula(c.getString(0), c.getString(1),
          c.getString(2), c.getString(3), c.getString(4),
          c.getString(5)));
    } while (c.moveToNext());

    return peliculas;
  }

    /*private void poblarDB(SQLiteDatabase db) {

        // FLUJO A SEGUIR
        // Obtenemos información de FIREBASE
        // NO HAY ---- poblamos FIREBASE
        // HAY ------- poblamos LOCAL

        Log.i("FLIXNETDB", "Poblando....") ;

        // Obtenemos una instancia de la base de datos de Firebase Realtime Database
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        fbdb = FirebaseDatabase.getInstance() ;

        //


        // Obtenemos referencia al usuario
        fbdb.getReference("usuarios/" + uid + "/listas")
            .addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    // Recorremos los resultados y obtenemos los identificadores de las listas
                    for(DataSnapshot item : dataSnapshot.getChildren())
                        addLista(item.getValue().toString(), uid) ;

                    // Hacemos una parada para comprobar el contenido de la lista
                    Log.i("FLIXNETDB", "Esperando contenido...") ;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("FLIXNET:DB", "Se ha producido un error en la comunicación con la base de datos [" + databaseError.getCode() + "]") ;
                }
            });

    }

    private void addLista(final String idLista, final String idUsuario) {

        // Creamos la lista
        final Lista lst = new Lista(Integer.valueOf(idLista)) ;

        fbdb.getReference("listas/" + idLista)
            .addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    // Guardamos la lista en la base de datos
                    ContentValues lista = new ContentValues() ;
                    lista.put("idLista",   idLista) ;
                    lista.put("idUsuario", idUsuario) ;
                    lista.put("nombre",    dataSnapshot.child("nombre").getValue().toString()) ;

                    database.insertOrThrow("lista", null, lista) ;


                    // Por cada película, buscamos información y la añadimos
                    for (DataSnapshot item : dataSnapshot.child("peliculas").getChildren()) {

                        String idPelicula = item.getValue().toString() ;

                        // Añadimos la película
                        addPelicula(idPelicula) ;

                        // Añadimos la relación en la base de datos
                        ContentValues lista_pelicula = new ContentValues() ;
                        lista_pelicula.put("idLista", idLista) ;
                        lista_pelicula.put("idPelicula", idPelicula) ;

                        database.insertOrThrow("lista_pelicula", null, lista_pelicula) ;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("FLIXNET:DB", "Se ha producido un error en la comunicación con la base de datos [" + databaseError.getCode() + "]") ;
                }
            });

    }

    private void addPelicula(final String idFilm) {

        // Buscamos en la base de datos información sobre la película
        fbdb.getReference("peliculas/" + idFilm)
            .addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dta) {

                    ContentValues pelicula = new ContentValues() ;
                    pelicula.put("idMDB",    idFilm) ;
                    pelicula.put("titulo",   dta.child("titulo").getValue().toString()) ;
                    pelicula.put("poster",   dta.child("poster").getValue().toString()) ;
                    pelicula.put("sinopsis", dta.child("sinopsis").getValue().toString()) ;
                    pelicula.put("estreno",  dta.child("estreno").getValue().toString()) ;
                    pelicula.put("nota",     dta.child("nota").getValue().toString()) ;

                    try {
                        database.insertOrThrow("pelicula",null, pelicula) ;
                    } catch(SQLiteException e) {
                        e.printStackTrace() ;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("FLIXNET:DB", "Se ha producido un error en la comunicación con la base de datos [" + databaseError.getCode() + "]") ;
                }
            }) ;

    }

    public String getPelicula() {

        Cursor c = database.rawQuery("SELECT * FROM pelicula ;", null) ;
        c.moveToFirst() ;
        return c.getString(1) ;
    }*/



}
