package com.example.katsino_bd;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.katsino_bd.SQLiteHelper.ConexionSQLiteHelper;
import com.example.katsino_bd.Contract.Contract;
import com.example.katsino_bd.Login.MainActivity;
import com.example.katsino_bd.Services.App;
import com.example.katsino_bd.Services.BackgroundSoundService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import pl.droidsonroids.gif.GifImageView;


public class SettingsActivity extends AppCompatActivity implements View.OnClickListener{
    ImageButton search, home, game, settings;
    TextView delete, changename, changepass, switch_sound, terms, donate;
    GifImageView coinsgif;
    Button logout;
    Boolean playing = false;
    String s_correo;
    FirebaseAuth firebaseuth;
    int counter = 10;

    /**
     * Método onCreate con la lógica de cada botón para la configuración.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        this.setTitle(" ");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this,"bd_usuarios",null,1);
        s_correo = user.getEmail();

        search = findViewById(R.id.btn_menu_search);
        home =  findViewById(R.id.btn_menu_home);
        game = findViewById(R.id.btn_menu_game);
        settings =  findViewById(R.id.btn_menu_settings);
        coinsgif = findViewById(R.id.coinsgif);
        firebaseuth = FirebaseAuth.getInstance();

        search.setOnClickListener(this);
        home.setOnClickListener(this);
        game.setOnClickListener(this);
        settings.setOnClickListener(this);

        delete = findViewById(R.id.txt_delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder2 = new AlertDialog.Builder(SettingsActivity.this);
                builder2.setTitle("Delete account");
                builder2.setMessage("Are you sure you want to delete your account?");
                builder2.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        // Get auth credentials from the user for re-authentication. The example below shows
                        // email and password credentials but there are multiple possible providers,
                        // such as GoogleAuthProvider or FacebookAuthProvider.
                        AuthCredential credential = EmailAuthProvider
                                .getCredential("user@example.com", "password1234");

                        // Prompt the user to re-provide their sign-in credentials
                        user.reauthenticate(credential)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        user.delete()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {

                                                            SQLiteDatabase db=conn.getWritableDatabase();
                                                            db.execSQL("delete from "+ Contract.TABLA_USUARIO + " where "+ Contract.CAMPO_CORREO + " = " + s_correo );
                                                            db.close();

                                                            Intent in = new Intent(SettingsActivity.this, MainActivity.class);
                                                            startActivity(in);
                                                        }
                                                    }
                                                });

                                    }
                                });
                    }
                });
                builder2.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                builder2.create();
                builder2.show();

            }
        });

        changename = findViewById(R.id.txt_change_name);
        changename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                final EditText TITULONUEVO = new EditText(SettingsActivity.this);
                builder.setTitle("Change username");
                builder.setMessage("Please enter the your new username.");
                builder.setView(TITULONUEVO);
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String newusername = "";
                        newusername = TITULONUEVO.getText().toString();

                        if (newusername.isEmpty()) {
                            newusername = "default name";
                        }
                        updateName(newusername);
                    }
                });

                builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                builder.create();
                builder.show();

            }
        });

        changepass = findViewById(R.id.txt_change_pass);
        changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                builder.setTitle("Change password");
                builder.setMessage("We'll send you an email to change your password");
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        firebaseuth.sendPasswordResetEmail(user.getEmail()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(SettingsActivity.this, "Password send to your email", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(SettingsActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    }
                });

                builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                builder.create();
                builder.show();

            }
        });

        switch_sound = findViewById(R.id.txt_sound);
        switch_sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playing) {
                    playing = false;
                    String action = "PLAY";
                    Intent myService = new Intent(SettingsActivity.this, BackgroundSoundService.class);
                    myService.setAction(action);
                    startService(myService);
                } else {
                    playing = true;
                    String action = "STOP";
                    Intent myService = new Intent(SettingsActivity.this, BackgroundSoundService.class);
                    myService.setAction(action);
                    startService(myService);
                }
            }
        });

        logout = findViewById(R.id.txt_log_out);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder2 = new AlertDialog.Builder(SettingsActivity.this);
                builder2.setTitle("Log out");
                builder2.setMessage("Are you sure you want to log out?");
                builder2.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        FirebaseAuth.getInstance().signOut();
                        Intent in;
                        in = new Intent(SettingsActivity.this, MainActivity.class);
                        in.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(in);
                    }
                });
                builder2.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                builder2.create();
                builder2.show();

            }
        });

        terms = findViewById(R.id.txt_terms);
        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder2 = new AlertDialog.Builder(SettingsActivity.this);
                builder2.setTitle("Terms and conditions");
                builder2.setMessage("Are you sure you accept the terms and conditions of this game?");
                builder2.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                builder2.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                builder2.create();
                builder2.show();

            }
        });

        donate = findViewById(R.id.txt_donate);
        donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                coinsgif.setVisibility(v.VISIBLE);
                coinsgif.setImageResource(R.drawable.coinsgif);

                updateMoney(String.valueOf(counter));
                if (counter <= 20) {
                    Toast.makeText(getApplicationContext(), "Thanks for donating " + counter + " $ <3", Toast.LENGTH_SHORT).show();
                } else if (counter <= 50) {
                    Toast.makeText(getApplicationContext(), "Hey buddy, stop that, you've already donate " + counter + " $", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Okay, you're rich, I get it, thanks for the  " + counter + " $", Toast.LENGTH_SHORT).show();
                }
                counter += 10;
            }
        });
    }

    /**
     * Evento onClick para botones del menu.
     */
    public void onClick(View v) {
        Intent in;
        switch (v.getId()) {
            case R.id.btn_menu_search:
                in = new Intent( this, RankingActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(in);
                break;
            case R.id.btn_menu_home:
                in = new Intent( this, HomeActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(in);
                break;
            case R.id.btn_menu_game:
                in = new Intent( this, GamesActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(in);
                break;
            case R.id.btn_menu_settings:
                break;
            default:
                throw new IllegalStateException("Unexpected value: " +
                        v.getId());
        }
    }

    /**
     * Evento onResume para la música de fondo.
     */
    @Override
    protected void onResume() {
        super.onResume();
        if(App.instance().settings.getBoolean("lastState_playingMusic",false))
        {
            String action = "PLAY_AND_SEEK";
            Intent myService = new Intent(SettingsActivity.this, BackgroundSoundService.class);
            myService.setAction(action);
            startService(myService);
        }
    }

    /**
     * Evento para actualizar el nombre en la base de datos.
     */
    public void updateName(String name) {
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this, "bd_usuarios", null, 1);
        SQLiteDatabase db = conn.getWritableDatabase();
        String[] parametros = {s_correo};
        ContentValues values = new ContentValues();
        values.put(Contract.CAMPO_NOMBRE, name);

        db.update(Contract.TABLA_USUARIO, values, Contract.CAMPO_CORREO + "=?", parametros);
        db.close();
    }

    /**
     * Evento para restar y actualizar las monedas en la base de datos.
     */
    public void updateMoney(String moneyRest) {
        //Llamar a la bd
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this, "bd_usuarios", null, 1);
        SQLiteDatabase db = conn.getWritableDatabase();

        //Obtener las monedas actuales
        Cursor cursor = db.rawQuery("SELECT " + Contract.CAMPO_CANTIDAD + " FROM " + Contract.TABLA_USUARIO + " WHERE " + Contract.CAMPO_CORREO + " = '" + s_correo + "' ", null);

        String money = "";
        while (cursor.moveToNext()) {
            money = cursor.getString(0);
        }

        //Restarlo
        money = (Integer.parseInt(money) - Integer.parseInt(moneyRest)) + "";

        //Actualizar bd con el dinero nuevo
        String[] parametros = {s_correo};
        ContentValues values = new ContentValues();
        values.put(Contract.CAMPO_CANTIDAD, money);

        db.update(Contract.TABLA_USUARIO, values, Contract.CAMPO_CORREO + "=?", parametros);
        db.close();
    }

}

