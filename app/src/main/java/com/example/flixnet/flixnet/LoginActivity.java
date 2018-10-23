package com.example.flixnet.flixnet;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    private final String DUMMY_LOGIN = "Bruce";
    private final String DUMMY_PASSWORD = "iambatman";

    private Button btnLogin, btnRegister;
    private EditText txtUser, txtPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
                String usr = txtUser.getText().toString().trim();
                String pas = txtPass.getText().toString().trim();

                Log.d("FLIXNET_LOGIN", usr + " - " + pas);

                // Comprobamos que hay datos

                if (usr.isEmpty() || pas.isEmpty()){
                    Snackbar.make(v, R.string.login_vacio_login, Snackbar.LENGTH_LONG).show();
                } else if (!usr.equals(DUMMY_LOGIN) || !pas.equals(DUMMY_PASSWORD)) {
                    Snackbar.make(v, R.string.login_error_login, Snackbar.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(LoginActivity.this , ListActivity.class);
                    intent.putExtra("usuario", usr);
                    startActivity(intent);
                }

                /* Intent intent = new Intent(LoginActivity.this, AccessActivity.class);
                intent.putExtras();
                startActivity(intent); */
            }
        });
    }
}
