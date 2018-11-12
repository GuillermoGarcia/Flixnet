package com.example.flixnet.flixnet.Actividades;

import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.flixnet.flixnet.Adaptadores.filmAdapter;
import com.example.flixnet.flixnet.FlixNetDB;
import com.example.flixnet.flixnet.Modelos.Pelicula;
import com.example.flixnet.flixnet.Modelos.Usuario;
import com.example.flixnet.flixnet.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListActivity extends AppCompatActivity {

  private FirebaseAuth mAuth;
  private FirebaseDatabase db;

  //
  private RecyclerView recycler ;
  private filmAdapter adapter ;


  @Override
    protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_list);

    // Debemos obtener la información que contiene el Bundle asociado
    // a la intención que ha lanzado esta actividad.
    Bundle bundle = getIntent().getExtras();

    // Capturamos el objeto usuario almacenado en el bundle
    Usuario usuario = (Usuario) bundle.getSerializable("usuario");

    //
    Toast.makeText(this, "Bienvenido/a, " + usuario.getNombre(), Toast.LENGTH_LONG).show();

    FlixNetDB fndb = FlixNetDB.getInstance(getApplicationContext(),usuario.getIdUsuario());

    List<Pelicula> peliculas = fndb.getPeliculas();

    // Instanciamos nuestro recyclerView
    recycler = findViewById(R.id.listActivityList) ;

    // Creamos un LayoutManager, encargado de gestionar, junto con el adaptador, la manera
    // en que visualizaremos los elementos en pantalla. Básicamente tenemos dos posibilida-
    // des:
    // - LinearLayoutManager
    //   renderizará los elementos de manera lineal, uno sobre otro, en forma de lista.
    // - GridLayoutManaer
    //   dibujará los elementos en pantalla en forma de rejilla o tabla.

    // Necesitaremos proporcionarle el contexto y el número de columnas que deseamos
    GridLayoutManager manager = new GridLayoutManager(this, 2) ;

    // Definido el gestor de layouts, lo asociamos al recyclerView
    recycler.setLayoutManager(manager) ;

    // Creamos nuestro adaptador y proporcionamos al constructor la información que
    // necesita. Igualmente, definimos un listener OnItemClickListener (la interfaz
    // que definimos, ¿recuerdas?), que implementará el método onItemClick (también
    // definido por nosotros y que será llamado cuando el usuario haga click sobre
    // un determinado ítem).
    adapter = new filmAdapter(this, R.layout.list_film_item,
        peliculas, new filmAdapter.OnItemClickListener() {
      @Override
      public void onItemClick(Pelicula film, int position) {

        // Aquí haremos lo que deseemos cuando el usuario haya
        // seleccionado una película. Temporalmente, mostramos
        // un mensaje con información sobre dicho film.
        Toast.makeText(getApplicationContext(),
            film.toString(), Toast.LENGTH_LONG).show() ;
      }
    }) ;



    // Asociamos el adaptador al recyclerView y... ¡listo!
    recycler.setAdapter(adapter);


  }
}
