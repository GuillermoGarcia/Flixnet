package com.example.flixnet.flixnet;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.HashMap;
import java.util.Map;

public class LoadMDB {

  public LoadMDB(Context context) {

    //Acceso a la BD de TheMovieDB a traves de su api

    // Creamos una cola a la que añadiremos la petición
    RequestQueue queue = Volley.newRequestQueue(context);

    // La ruta de la API de themoviedb para nuestra petición
    String ruta = "https://api.themoviedb.org/3/movie/now_playing?api_key=5f775a993e0d9b14b2d0424b78e5cfc4&language=es-ES&page=1";

    // Preparamos la solicitud que se enviará
    StringRequest jsonreq = new StringRequest(
            Request.Method.GET,
            ruta,
            new Response.Listener<String>() {
              @Override
              public void onResponse(String response) {

                // Creamos un objeto de tipo GsonBuilder
                GsonBuilder builder = new GsonBuilder();

                // A partir del constructor anterior, instanciamos un objeto de
                // tipo Gson. Éste será el encargado de parsear la cadena y
                // deserializarla, permitiéndonos obtener el objeto correspon-
                // diente.
                Gson gson = builder.create();

                // Ya que no vamos a usar una clase, hay que parsear el json recibido en
                // un Array del que obtendremos los datos, para ello tenemos que pasar a
                // traves de objeto JsonObject y este provendra de un JsonParser

                JsonArray jsonar = new JsonParser().parse(response).getAsJsonObject()
                        .get("results").getAsJsonArray();

                // Iteramos sobre el array para recorrer todos los elementos, de cada
                // elemento nos quedamos con la información que queramos.

                Map<String, String> data = new HashMap<String, String>();

                for (int i = 0; i < jsonar.size(); i++) {
                  data.clear();
                  JsonObject item = (JsonObject) jsonar.get(i);
                  //Log.i("TITULO", getItem(item,"title"));
                  data.put("estreno", getItem(item, "release_date"));
                  data.put("nota", getItem(item, "vote_average"));
                  data.put("poster", getItem(item, "poster_path"));
                  data.put("sinopsis", getItem(item, "overview"));
                  data.put("titulo", getItem(item, "title"));
                  saveOnFirebase(item.get("id").toString(), data);
                }
              }
            },

            new Response.ErrorListener() {
              @Override
              public void onErrorResponse(VolleyError error) {
                Log.d("FLIXNET_LOGIN", error.getMessage());
              }
            }
    );
        /*{
            /**
             * @return
             * @throws AuthFailureError
             */
        /*    @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> param = new HashMap<String,String>();
                param.put("api_key","5f775a993e0d9b14b2d0424b78e5cfc4");
                param.put("language","es-ES");
                param.put("page","1");

                return param ;
            }

            /**
             * @return
             * @throws AuthFailureError
             */
         /*   @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> param = new HashMap<String,String>();
                param.put("Content-Type","application/x-www-form-urlencoded");

                return param;
            }
        };*/

    queue.add(jsonreq);
  }



  protected void saveOnFirebase(String id, Map<String, String> data){


    // Obtenemos una referencia al documento (tabla) que contendrá
    // la información. Si el documento no existe, Firebase lo crea.
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("pelicula");

    // Guardamos la información de la película en la base de datos de Firebase,
    // asociado al ID de la película.
    ref.child(id).setValue(data);

    //Toast.makeText(RegisterActivity.this, "Se ha creado con éxito.", Toast.LENGTH_LONG).show();

  }

  protected String getItem(JsonObject item, String key){
    return item.get(key).toString().replace('"',' ').trim();
  }
}
