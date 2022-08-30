package com.example.katsino_bd.Games;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.katsino_bd.SQLiteHelper.ConexionSQLiteHelper;
import com.example.katsino_bd.Contract.Contract;
import com.example.katsino_bd.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import pl.droidsonroids.gif.GifImageView;

public class GameRoulleteActivity  extends AppCompatActivity {

    GifImageView gif_roullette, gif_coins;
    ImageView img_roullette_stop;
    TextView actual_money_text;
    int money_int;
    FirebaseUser user;
    String user_money, user_email;

    /**
     * Método onCreate con la lógica del juego de la ruleta.
     */
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.setTitle(" ");
        setContentView(R.layout.game_roullete);
        Intent in = getIntent();
        user = FirebaseAuth.getInstance().getCurrentUser();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        gif_roullette = findViewById(R.id.ruletagirando);
        gif_coins = findViewById(R.id.coinsgif);
        actual_money_text = findViewById(R.id.dineroactual);

        checkCurrentUser();
        money_int = new Integer(actual_money_text.getText().toString());

        img_roullette_stop = findViewById(R.id.ruletaquieta);
        img_roullette_stop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (img_roullette_stop.getVisibility() == View.VISIBLE && (money_int >= 50)) {

                    gif_coins.setVisibility(v.VISIBLE);
                    gif_coins.setImageResource(R.drawable.coinsgif);

                    img_roullette_stop.setVisibility(v.INVISIBLE);
                    MediaPlayer song = MediaPlayer.create(GameRoulleteActivity.this, R.raw.roullete_sound);
                    song.start();
                    money_int = money_int - 50;
                    actual_money_text.setText(money_int + "");

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            img_roullette_stop.setVisibility(v.VISIBLE);

                            TypedArray images = getResources().obtainTypedArray(R.array.loading_images_roullete);
                            int choice = (int) (Math.random() * images.length());
                            img_roullette_stop.setImageResource(images.getResourceId(choice, R.drawable.ruleta01_12));
                            images.recycle();

                            switch (choice) {
                                case 0:
                                    Toast.makeText(getApplicationContext(), "TIME!!! You get 12 points", Toast.LENGTH_SHORT).show();
                                    money_int += 12;
                                    break;
                                case 1:
                                    Toast.makeText(getApplicationContext(), "TIME!!! You get 2 points", Toast.LENGTH_SHORT).show();
                                    money_int += 2;
                                    break;
                                case 2:
                                    Toast.makeText(getApplicationContext(), "TIME!!! You get 34 points", Toast.LENGTH_SHORT).show();
                                    money_int += 34;
                                    break;
                                case 3:
                                    Toast.makeText(getApplicationContext(), "TIME!!! You get 67 points", Toast.LENGTH_SHORT).show();
                                    money_int += 67;
                                    break;
                                case 4:
                                    Toast.makeText(getApplicationContext(), "TIME!!! You get 345 points", Toast.LENGTH_SHORT).show();
                                    money_int += 345;
                                    break;
                                case 5:
                                    Toast.makeText(getApplicationContext(), "TIME!!! You get 234 points", Toast.LENGTH_SHORT).show();
                                    money_int += 234;
                                    break;
                                case 6:
                                    Toast.makeText(getApplicationContext(), "TIME!!! You get 12 points", Toast.LENGTH_SHORT).show();
                                    money_int += 12;
                                    break;
                                case 7:
                                    Toast.makeText(getApplicationContext(), "TIME!!! You get 34 points", Toast.LENGTH_SHORT).show();
                                    money_int += 34;
                                    break;
                                case 8:
                                    Toast.makeText(getApplicationContext(), "TIME!!! You get 23 points", Toast.LENGTH_SHORT).show();
                                    money_int += 23;
                                    break;
                                case 9:
                                    Toast.makeText(getApplicationContext(), "TIME!!! You get 12 points", Toast.LENGTH_SHORT).show();
                                    money_int += 12;
                                    break;
                                case 10:
                                    Toast.makeText(getApplicationContext(), "TIME!!! You get 8 points", Toast.LENGTH_SHORT).show();
                                    money_int += 8;
                                    break;
                                case 11:
                                    Toast.makeText(getApplicationContext(), "TIME!!! You get 12 points", Toast.LENGTH_SHORT).show();
                                    money_int += 12;
                                    break;
                                case 12:
                                    Toast.makeText(getApplicationContext(), "TIME!!! You get 45 points", Toast.LENGTH_SHORT).show();
                                    money_int += 45;
                                    break;
                                case 13:
                                    Toast.makeText(getApplicationContext(), "TIME!!! You get 45 points", Toast.LENGTH_SHORT).show();
                                    money_int += 45;
                                    break;
                                case 14:
                                    Toast.makeText(getApplicationContext(), "TIME!!! You get 23 points", Toast.LENGTH_SHORT).show();
                                    money_int += 23;
                                    break;

                            }
                            actual_money_text.setText(money_int + "");
                            updatemoney();
                            song.stop();

                        }
                    }, 3500);
                } else {
                    //Toast.makeText(getApplicationContext(), "You don't have enough money", Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder alert = new AlertDialog.Builder(GameRoulleteActivity.this);
                    alert.setTitle("You don't have enough money");

                    alert.setPositiveButton("Ok", null);
                    alert.create();
                    alert.show();
                }
            }
        });


    }

    /**
     * Comprobar usuario registrado desde Firebase.
     */
    public void checkCurrentUser() {
        // [START check_current_user]
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            getUserProfile();
        } else {
            // No user is signed in
        }
        // [END check_current_user]
    }

    /**
     * Recoger la información del usuario registrado de la base de datos.
     * Colocar monedas en la pantala del juego.
     */
    public void getUserProfile() {
        // [START get_user_profile]
        ConexionSQLiteHelper conn=new ConexionSQLiteHelper(this,"bd_usuarios",null,1);
        SQLiteDatabase db=conn.getWritableDatabase();
        Cursor cursor=db.rawQuery("SELECT "+ Contract.CAMPO_CANTIDAD + " FROM " + Contract.TABLA_USUARIO + " WHERE " + Contract.CAMPO_CORREO + " = '" + user.getEmail() +"' ", null);
        while (cursor.moveToNext()) {
            user_money = cursor.getString(0);
        }

        if (user != null) {
            // Name, email address, and profile photo Url
            actual_money_text.setText(user_money);
            user_email = user.getEmail();
        }
        // [END get_user_profile]
    }

    /**
     * Sentencia SQL para actualizar las monedas del jugador.
     */
    public void updatemoney() {
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this,"bd_usuarios",null,1);
        SQLiteDatabase db=conn.getWritableDatabase();
        String[] parametros={user_email};
        ContentValues values=new ContentValues();
        values.put(Contract.CAMPO_CANTIDAD, money_int);

        db.update(Contract.TABLA_USUARIO,values,Contract.CAMPO_CORREO+"=?",parametros);
        //Toast.makeText(this,"Item updated successfully",Toast.LENGTH_LONG).show();
        db.close();
    }

    /**
     * Metodo para mostrar un menu de ayuda en la barra de estado.
     */
    //#region public void Menu()
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.info:
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(GameRoulleteActivity.this);
                builder.setTitle("Instructions");
                builder.setMessage("Each game costs 50 pawcoins.\n" +
                        "\n" +
                        "The objective of the game is to spin the wheel and wait for the result. At the end, the result of the winning box is added to your pawcoins.\n" +
                        "\n" +
                        "The probability is 7% to get each roulette box.");

                builder.setPositiveButton("Got it!", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

                builder.create();
                builder.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //#endregion

}
