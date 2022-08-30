package com.example.katsino_bd.Login;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.katsino_bd.SQLiteHelper.ConexionSQLiteHelper;
import com.example.katsino_bd.Contract.Contract;
import com.example.katsino_bd.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CreateUserActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private FirebaseAuth mAuth;
    Button btn_crear;
    TextView txt_username, txt_nombre, txt_pass;
    String string_foto;
    ImageView profile;
    Intent in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createuser);
        this.setTitle("REGISTER NEW");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btn_crear = findViewById(R.id.btn_crear);
        txt_username = findViewById(R.id.txt_username);
        txt_nombre = findViewById(R.id.txt_nombre);
        txt_pass = findViewById(R.id.txt_pass);
        profile = findViewById(R.id.profile);


        mAuth = FirebaseAuth.getInstance();

        btn_crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!txt_nombre.getText().toString().isEmpty() && !txt_pass.getText().toString().isEmpty()) {
                    createAccount(txt_nombre.getText().toString(), txt_pass.getText().toString());
                }
            }
        });

        Spinner spinner = (Spinner) findViewById(R.id.spinner_fotos);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.fotos, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(this);

    }



    private void createAccount(String email, String password) {
        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            registrarUsuarios();
                            in = new Intent(CreateUserActivity.this, MainActivity.class);
                            startActivity(in);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(CreateUserActivity.this, task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
        // [END create_user_with_email]
    }

    private void updateUI(FirebaseUser user) { }

    private void registrarUsuarios() {
        ConexionSQLiteHelper conn=new ConexionSQLiteHelper(this,"bd_usuarios",null,1);

        SQLiteDatabase db=conn.getWritableDatabase();

        ContentValues values=new ContentValues();

        values.put(Contract.CAMPO_NOMBRE, txt_username.getText().toString());
        values.put(Contract.CAMPO_CANTIDAD, "100000" );
        values.put(Contract.CAMPO_CORREO, txt_nombre.getText().toString());
        values.put(Contract.CAMPO_FOTO, string_foto);


        Long idResultante=db.insert(Contract.TABLA_USUARIO,Contract.CAMPO_ID,values);
        Toast.makeText(getApplicationContext(),"Id Registro: "+idResultante,Toast.LENGTH_SHORT).show();
        db.close();
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        parent.getItemAtPosition(pos);
        if(pos == 0) {
            profile.setImageResource(R.drawable.profile01);
            string_foto = "profile01";
        } else if (pos == 1) {
            profile.setImageResource(R.drawable.profile02);
            string_foto = "profile02";
        } else if (pos == 2) {
            profile.setImageResource(R.drawable.profile03);
            string_foto = "profile03";
        } else if (pos == 3) {
            profile.setImageResource(R.drawable.profile04);
            string_foto = "profile04";
        } else if (pos == 4) {
            profile.setImageResource(R.drawable.profile05);
            string_foto = "profile05";
        } else if (pos == 5) {
            profile.setImageResource(R.drawable.profile06);
            string_foto = "profile06";
        } else if (pos == 6) {
            profile.setImageResource(R.drawable.profile07);
            string_foto = "profile07";
        } else if (pos == 7) {
            profile.setImageResource(R.drawable.profile08);
            string_foto = "profile08";
        }
    }

    public void onNothingSelected(AdapterView<?> parent) { // Another interface callback
    }



}
