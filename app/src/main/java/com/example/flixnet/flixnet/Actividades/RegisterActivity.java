package com.example.flixnet.flixnet.Actividades;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.flixnet.flixnet.Modelos.Usuario;
import com.example.flixnet.flixnet.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {


  private Button btnSignUp;
  private EditText nom, ape, ali, tel, ema, pwd1, pwd2; //usu

  private FirebaseAuth mAuth;
  private FirebaseDatabase db;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_register);

    // Instanciamos elementos del Layout
    nom = findViewById(R.id.register_firstName);
    ape = findViewById(R.id.register_lastName);
    //usu = findViewById(R.id.register_userName);
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
          Toast.LENGTH_LONG).show();
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
                Toast.LENGTH_LONG).show();
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
                Toast.LENGTH_LONG).show();
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
    //usu.setFilters(new InputFilter[] { alfanumerico });
    //usu.setFilters(new InputFilter[] { longitudMax15 });
    tel.setFilters(new InputFilter[] { new InputFilter.LengthFilter(9) });
    pwd1.setFilters(new InputFilter[] { longitudMax15 });



    // Funcionalidad de botones
    btnSignUp.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

      //Obtenemos los valores introducidos por el usuario
      String usuario = ema.getText().toString().trim();
      String clave = pwd1.getText().toString().trim();

      // Obtenemos una instancia del objeto FirebaseAuth
      mAuth = FirebaseAuth.getInstance();
      mAuth.createUserWithEmailAndPassword(usuario, clave)
        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
           @Override
           public void onComplete(@NonNull Task<AuthResult> task) {
             if (task.isSuccessful()) {
               // Si, nuestro usuario tiene más información asociada,
               // tendremos que guardarla en la base de datos.

               // Obtenemos una instancia de la base de datos
               db = FirebaseDatabase.getInstance();

               // Obtenemos una referencia al documento (tabla) que contendrá
               // la información. Si el documento no existe, Firebase lo crea.
               DatabaseReference ref = db.getReference("usuario");

               // Obtener los datos proporcionados por Firebase sobre el usuario
               // registrado.
               FirebaseUser fbUser = mAuth.getCurrentUser();

               // Preguntamos por el UID
               String uid = fbUser.getUid();

               // Creamos nuestro objeto usuario con los datos proporcionados
               // a través del formulario.
               Usuario miUsuario = new Usuario(uid, nom.getText().toString(), ape.getText().toString(),
                   ema.getText().toString(), tel.getText().toString());

               // Guardamos la información en la base de datos de Firebase,
               // asociados al UID.
               ref.child(uid).setValue(miUsuario);

               Toast.makeText(RegisterActivity.this, "Se ha creado con éxito.", Toast.LENGTH_LONG).show();
             } else {
               Toast.makeText(RegisterActivity.this, "Se ha producido un error en el registro.", Toast.LENGTH_LONG).show();
             }
           }
        });
      }
    });

    // Recogemos información de la anterior Actividad
    Bundle bund = getIntent().getExtras();

    Toast.makeText(this, bund.getString("mensaje"), Toast.LENGTH_LONG).show();

  }
}
