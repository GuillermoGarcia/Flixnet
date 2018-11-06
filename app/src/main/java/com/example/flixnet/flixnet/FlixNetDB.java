package com.example.flixnet.flixnet;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.flixnet.flixnet.Modelos.Lista;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;
import java.util.function.BiConsumer;

public class FlixNetDB extends SQLiteOpenHelper {

  //
  private static int dbVersion = 1 ;

  // Instancia del objeto y contexto
  private static FlixNetDB instance = null ;
  private static SQLiteDatabase database;
  private Context dbContext ;

  //
  private String dbName ;

  private FlixNetDB(@Nullable Context context, @Nullable String name,
                    @Nullable SQLiteDatabase.CursorFactory factory, int version) {
    super(context, name, factory, version);

    // Guardamos el contexto
    dbName    = name ;
    dbContext = context ;
  }

  /**
   * Utilizamos el patrón de diseño Singleton para crear una única
   * instancia del objeto FlixNetDB, teniendo también de esta manera
   * un único punto de entrada a la base de datos.
   *
   * @param dbName
   * @param context
   * @return
   */
  public static synchronized FlixNetDB getInstance(String dbName, Context context) {

    // Si aún no hemos creado la instancia, lo hacemos y llamamos al
    // constructor con los datos apropiados.
    if (instance==null)
      instance = new FlixNetDB(context, dbName, null, dbVersion) ;

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

    // Si la base de datos no existe, creamos aquellos elementos que
    // necesitaremos para nuestra aplicación y obtenemos información
    // de la API de The Movie DB.
    //
    // Firebase: almacenará películas, géneros, comentarios, listas
    // y cines. Esto es, básicamente toda la información de nuestra
    // aplicación.
    //
    // Localmente: en nuestro dispositivo guardaremos nuestras listas
    // e información temporal de las películas contenidas en las
    // mismas.

    // creamos base de datos local utilizando las instrucciones alma-
    // cenadas en el archivo correspondiente.
    try {

      InputStream is = dbContext.getResources().openRawResource(R.raw.flixnet_local);

      // Creamos un buffer de lectura
      BufferedReader buf = new BufferedReader(new InputStreamReader(is));

      // Leemos el contenido y lanzamos las instrucciones
      String line = buf.readLine();

      while (line != null) {
        sqliteDB.execSQL(line) ;
        line = buf.readLine();
      }

      // Cerramos el buffer de lectura
      buf.close();

      // Cerramos el recurso
      is.close();

    } catch (IOException e) {
      e.printStackTrace();
    }
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
    Log.i("FLIXNETDB", "Actualizando....") ;
  }


  /**
   * Función para poblar un tabla con los datos obtenidos desde Firebase
   * @param tabla
   * @param ref
   */
  public void setData(DatabaseReference ref){
    ref.orderByKey().addChildEventListener(new ChildEventListener() {
      @Override
      public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        Lista lista = dataSnapshot.getValue(Lista.class);
        String query = "INSERT OR REPLACE INTO lista (idUsuario, idLista, nombre) VALUES (\"";
        query += dataSnapshot.getKey() + "\", \"";
        query += lista.getIdLista() + "\", \"";
        query += lista.getNombre() + "\")";

        database.execSQL(query);

        Map<String, String> pelis = lista.getPeliculas();

        for (Iterator it = pelis.keySet().iterator(); it.hasNext();){
          query = "INSERT OR REPLACE INTO lista_pelicula (idLista, idPelicula) VALUES (\"";
          query += lista.getIdLista() + "\", \"";
          query += it.next() + "\")";
          Log.d("QUERY:", query);
          database.execSQL(query);
        }

      }

      @Override
      public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

      }

      @Override
      public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

      }

      @Override
      public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {

      }
    });

  }

}
