package com.example.flixnet.flixnet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.flixnet.flixnet.Modelos.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.HashMap;
import java.util.Map;

public class ListActivity extends AppCompatActivity {

  private FirebaseAuth mAuth;
  private FirebaseDatabase db;

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

    FlixNetDB fndb = FlixNetDB.getInstance("flixnet",this );


  }
}
