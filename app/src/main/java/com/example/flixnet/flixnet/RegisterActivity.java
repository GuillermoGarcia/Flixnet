package com.example.flixnet.flixnet;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {


    private Button btnSignUp;
    private EditText nom, ape, usu, ali, tel, ema, pwd1, pwd2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Instanciamos elementos del Layout
        nom = findViewById(R.id.register_firstName);
        ape = findViewById(R.id.register_lastName);
        usu = findViewById(R.id.register_userName);
        ali = findViewById(R.id.register_alias);
        tel = findViewById(R.id.register_phoneNumber);
        ema = findViewById(R.id.register_email);
        pwd1 = findViewById(R.id.register_password);
        pwd2 = findViewById(R.id.register_confirm_password);

        btnSignUp = findViewById(R.id.register_btn_send);

        InputFilter alfabeticos = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {

                for (int i=start; i<end; i++){
                    if (!Character.isLetter(source.charAt(i))) {
                        Toast.makeText(RegisterActivity.this,
                                       R.string.register_error_alphabetic,
                                       Toast.LENGTH_LONG);
                        return "";
                    }
                }
                return null;
            }
        };

        InputFilter alfanumerico = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                for (int i=start; i<end; i++){
                    if(!Character.isLetterOrDigit(source.charAt(i))) {
                        Toast.makeText(RegisterActivity.this,
                                R.string.register_error_alphanumeric,
                                Toast.LENGTH_LONG);
                        return "";
                    }
                }
                return null;
            }
        };

        InputFilter longitudMax9 = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                for (int i=start; i<end; i++){
                    if ((end-start) > 9) {
                        Toast.makeText(RegisterActivity.this,
                                R.string.register_error_max9,
                                Toast.LENGTH_LONG);
                        return "";
                    }
                }
                return null;
            }
        };

        InputFilter longitudMax15 = new InputFilter.LengthFilter(15);


        /* Forma de Hacerlo si no se conoce LengthFilter
        InputFilter longitudMax15 = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                for (int i=start; i<end; i++){
                    if ((end-start) > 15) {
                        Toast.makeText(RegisterActivity.this,
                                R.string.register_error_max15,
                                Toast.LENGTH_LONG);
                        return "";
                    }
                }
                return null;
            }
        }; */

        nom.setFilters(new InputFilter[] { alfabeticos });
        usu.setFilters(new InputFilter[] { alfanumerico });
        usu.setFilters(new InputFilter[] { longitudMax15 });
        tel.setFilters(new InputFilter[] { new InputFilter.LengthFilter(9) });
        pwd1.setFilters(new InputFilter[] { longitudMax15 });



        // Funcionalidad de botones
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usr = usu.getText().toString().trim();
                String pas = pwd1.getText().toString().trim();

                Log.d("FLIXNET_LOGIN", usr + " - " + pas);

                // Comprobamos que hay datos



                /* Intent intent = new Intent(LoginActivity.this, AccessActivity.class);
                intent.putExtras();
                startActivity(intent); */
            }
        });

        // Recogemos información de la anterior Actividad
        Bundle bund = getIntent().getExtras();

        Toast.makeText(this, bund.getString("mensaje"), Toast.LENGTH_LONG).show();

    }
}
