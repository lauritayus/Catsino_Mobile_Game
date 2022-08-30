package com.example.katsino_bd.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.katsino_bd.Services.App;
import com.example.katsino_bd.Services.BackgroundSoundService;
import com.example.katsino_bd.HomeActivity;
import com.example.katsino_bd.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    Button btn_entrar, btn_registrar;
    EditText txt_nombre_log, txt_pass_log;
    Intent in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setTitle(" ");

        btn_entrar = findViewById(R.id.btn_entrar);
        btn_registrar = findViewById(R.id.btn_registrar);

        ImageView check_name = findViewById(R.id.check_name);
        ImageView check_pass = findViewById(R.id.check_pass);
        setup();

        mAuth = FirebaseAuth.getInstance();

        String action = "PLAY";
        Intent myService = new Intent(MainActivity.this, BackgroundSoundService.class);
        myService.setAction(action);
        startService(myService);



        txt_nombre_log = (EditText) findViewById(R.id.txt_nombre_log);
        txt_nombre_log.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                if (txt_nombre_log.getText().toString().contains("@gmail.com")) {
                    check_name.setVisibility(View.VISIBLE);
                } else {
                    check_name.setVisibility(View.INVISIBLE);
                }
            }
        });

        txt_pass_log = (EditText) findViewById(R.id.txt_pass_log);
        txt_pass_log.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                if (txt_pass_log.getText().toString().length() > 0) {
                    check_pass.setVisibility(View.VISIBLE);
                } else {
                    check_pass.setVisibility(View.INVISIBLE);
                }
            }
        });
        
    }

    private void setup() {
        //title = "Autentificacion";
        btn_entrar.setOnClickListener(v -> {
            if (!txt_nombre_log.getText().toString().isEmpty() && !txt_pass_log.getText().toString().isEmpty()) {
                signIn(txt_nombre_log.getText().toString(), txt_pass_log.getText().toString());
            }
        });

        btn_registrar.setOnClickListener(v -> {
            in = new Intent(MainActivity.this, CreateUserActivity.class);
            startActivity(in);
        });
    }

    private void signIn(String email, String password) {
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            in = new Intent(MainActivity.this, HomeActivity.class);
                            startActivity(in);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this, task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });

    }

    private void updateUI(FirebaseUser user) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(App.instance().settings.getBoolean("lastState_playingMusic",false))
        {
            String action = "PLAY_AND_SEEK";
            Intent myService = new Intent(MainActivity.this, BackgroundSoundService.class);
            myService.setAction(action);
            startService(myService);
        }
    }


}