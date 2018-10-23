package com.example.flixnet.flixnet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Recogemos información de la anterior Actividad
        Bundle bund = getIntent().getExtras();

        Toast.makeText(this, bund.getString("mensaje"), Toast.LENGTH_LONG).show();

    }
}
