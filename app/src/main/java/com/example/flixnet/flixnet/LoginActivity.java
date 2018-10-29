package com.example.flixnet.flixnet;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.flixnet.flixnet.Modelos.Respuesta;
import com.example.flixnet.flixnet.Modelos.Usuario;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

  private final String DUMMY_LOGIN = "Bruce";
  private final String DUMMY_PASSWORD = "iambatman";

  private Button btnLogin, btnRegister;
  private EditText txtUser, txtPass;

  private RequestQueue queue;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    // Creamos una cola de peticiones
    queue = Volley.newRequestQueue(this);

    // Instanciamos elementos del Layout
    txtUser = findViewById(R.id.input_usuario);
    txtPass = findViewById(R.id.input_contrasena);
    btnLogin = findViewById(R.id.boton_login);
    btnRegister = findViewById(R.id.boton_register);

    // Funcionalidad de botones
    btnRegister.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // Cambiamos a la Actividad de Registro
        Intent intent = new Intent(LoginActivity.this , RegisterActivity.class);
        intent.putExtra("mensaje", "Bienvenido/a a Flixnet");
        startActivity(intent);
      }
    });

    btnLogin.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        final String usr = txtUser.getText().toString().trim();
        final String pas = txtPass.getText().toString().trim();
        final String API_KEY = "cb12f2411587de50f97c6aee5a1c3220";

        Log.d("FLIXNET_LOGIN", usr + " - " + pas);

        // Comprobamos que hay datos

        if (usr.isEmpty() || pas.isEmpty()){
          Snackbar.make(v, R.string.login_vacio_login, Snackbar.LENGTH_LONG).show();
        } else {
          // Login contra Servidor RESTful
          LoginApi(v, usr, pas, API_KEY);

          // Login contra Firebase
          LoginFirebase(v, usr, pas, API_KEY);

        }

        /* Intent intent = new Intent(LoginActivity.this, AccessActivity.class);
        intent.putExtras();
        startActivity(intent); */
      }
    });
  }

  private void LoginApi(View v, final String usr, final String pas, final String API_KEY){
    // Definimos la URL de acceso a la API. Recuerda que debes utilizar la IP de tu
    // máquina (no vale 127.0.0.1 o localhost)
    String ruta = "http://169.254.150.235/api/api.php";

    // Creamos una solicitud de acceso a la URL anterior.
    // Necesitamos especificar: método (GET|POST), ruta y dos listeners.
    StringRequest jsonreq = new StringRequest(
            Request.Method.POST,
            ruta,
            new Response.Listener<String>() {
              @Override
              public void onResponse(String response) {

                // Si la solicitud se ha realizado con éxito, procesamos de manera apropiada,
                // la información obtenida en RESPONSE.
                Toast.makeText(LoginActivity.this,response, Toast.LENGTH_LONG).show();

                // Creamos un objeto de la clase GsonBuilder
                GsonBuilder builder = new GsonBuilder();

                //A partir del constructor anterior, creamos un objeto GSon
                Gson gson = builder.create();

                //Deserializamos la cadena response al objeto Respuesta

                Respuesta respuesta = gson.fromJson(response, Respuesta.class);

                if (respuesta.isError()){
                  Toast.makeText(LoginActivity.this,response, Toast.LENGTH_LONG).show();
                } else {
                  Usuario usuario = respuesta.getData();
                  Toast.makeText(LoginActivity.this,"Bienvenido, " + usuario.getNombre(), Toast.LENGTH_LONG).show();
                }

              }
            },

            new Response.ErrorListener() {
              @Override
              public void onErrorResponse(VolleyError error) {

                // Se la solicitud no se ha completado con éxito, procesamos el error de la
                // manera que estimemos oportuna, según la información obtenida a través del
                // objeto ERROR de tipo VOLLEYERROR.
                //Log.e("FLIXNET_LOGIN", error.getMessage());
              }
            }
    )
    {

      // El método getParams de la clase Request<T>, nos permite definir aquellos parámetros
      // que necesitemos enviar a través de una petición POST.
      @Override
      protected Map<String, String> getParams() throws AuthFailureError {

        Map<String, String> param = new HashMap<String, String>();
        param.put("token",API_KEY);
        param.put("codigo","1");
        param.put("usr",usr);
        param.put("pwd",pas);

        return param;
      }

      // El método getHeaders de la clase Request<T>, nos permite modificar la cabecera del
      // mensaje HTTP que enviamos al servidor. En este caso, nos bastará con establecer el
      // valor del parámetro Content-Type, indicando que los parámetros enviados deberán
      // ser codificados en tuplas clave, valor.
      @Override
      public Map<String, String> getHeaders() throws AuthFailureError {

        Map<String, String> param = new HashMap<String,String>();
        param.put("Content-Type","application/x-www-form-urlencoded");

        return param;
      }
    };

    // Finalmente, añadimos la petición a la cola de Volley.
    queue.add(jsonreq);
    if (!usr.equals(DUMMY_LOGIN) || !pas.equals(DUMMY_PASSWORD)) {
      Snackbar.make(v, R.string.login_error_login, Snackbar.LENGTH_LONG).show();
    } else {
      Intent intent = new Intent(LoginActivity.this , ListActivity.class);
      intent.putExtra("usuario", usr);
      startActivity(intent);
    }

  }

  private void LoginFirebase(View v, final String usr, final String pas, final String API_KEY){

  }
}
