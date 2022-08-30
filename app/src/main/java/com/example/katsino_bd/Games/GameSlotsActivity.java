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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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

public class GameSlotsActivity  extends AppCompatActivity {

    GifImageView gif_slot, gif_coins;
    ImageView img_slot_stop, btn_spin;
    TextView actual_money_text;
    EditText actual_bet_text;
    int actual_money_num, actual_bet_num;
    FirebaseUser user;
    String user_money, user_email;

    /**
     * Método onCreate con la lógica del juego de los dados.
     */
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.setTitle(" ");
        setContentView(R.layout.game_slots);
        Intent in = getIntent();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        user = FirebaseAuth.getInstance().getCurrentUser();

        gif_slot = findViewById(R.id.slotgirando);
        gif_coins = findViewById(R.id.coinsgif);
        btn_spin = findViewById(R.id.btn_spin);
        img_slot_stop = findViewById(R.id.img_slot);

        actual_bet_text = findViewById(R.id.apuestaactual);
        actual_bet_num = Integer.parseInt(actual_bet_text.getText().toString());

        actual_money_text = findViewById(R.id.dineroactual);
        actual_bet_text.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                if (Integer.parseInt(actual_bet_text.getText().toString()) <= 0) {
                    actual_bet_text.setText("10");
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                actual_bet_num = Integer.parseInt(actual_bet_text.getText().toString());
            }
        });

        checkCurrentUser();

        actual_money_num = new Integer(actual_money_text.getText().toString());

        btn_spin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (img_slot_stop.getVisibility() == View.VISIBLE && (actual_money_num >= actual_bet_num)) {

                    gif_coins.setVisibility(v.VISIBLE);
                    gif_coins.setImageResource(R.drawable.coinsgif);

                    img_slot_stop.setVisibility(v.INVISIBLE);
                    MediaPlayer song = MediaPlayer.create(GameSlotsActivity.this, R.raw.slot_sound);
                    song.start();
                    actual_money_num = actual_money_num - actual_bet_num;
                    actual_money_text.setText(actual_money_num + "");

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            img_slot_stop.setVisibility(v.VISIBLE);

                            TypedArray images = getResources().obtainTypedArray(R.array.loading_images_slot);
                            int choice = (int) (Math.random() * images.length());
                            img_slot_stop.setImageResource(images.getResourceId(choice, R.drawable.game_slot_1));
                            images.recycle();

                            switch (choice) {
                                case 0:
                                    Toast.makeText(getApplicationContext(), "CONGRATS!!! You win "+ (300+ actual_bet_num) +"points", Toast.LENGTH_SHORT).show();
                                    actual_money_num += 300 + actual_bet_num;
                                    break;
                                case 4:
                                    Toast.makeText(getApplicationContext(), "CONGRATS!!! You win "+ (100+ actual_bet_num) +" points", Toast.LENGTH_SHORT).show();
                                    actual_money_num += 100 + actual_bet_num;
                                    break;
                                case 10:
                                    Toast.makeText(getApplicationContext(), "CONGRATS!!! You win "+ (50+ actual_bet_num) +" points", Toast.LENGTH_SHORT).show();
                                    actual_money_num += 50 + actual_bet_num;
                                    break;
                                default:
                                    Toast.makeText(getApplicationContext(), "Better luck next time!!!", Toast.LENGTH_SHORT).show();
                                    break;

                            }
                            actual_money_text.setText(actual_money_num + "");
                            updateMoney();
                            song.stop();

                        }
                    }, 3500);
                } else {
                    //Toast.makeText(getApplicationContext(), "You don't have enough money", Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder alert = new AlertDialog.Builder(GameSlotsActivity.this);
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
    public void updateMoney() {
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this,"bd_usuarios",null,1);
        SQLiteDatabase db=conn.getWritableDatabase();
        String[] parametros={user_email};
        ContentValues values=new ContentValues();
        values.put(Contract.CAMPO_CANTIDAD, actual_money_num);

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
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(GameSlotsActivity.this);
                builder.setTitle("Instructions");
                builder.setMessage("Each game costs the pawcoins bet (top right).\n" +
                        "\n" +
                        "The object of the game is to spin the slot machine and wait for the result. If you win, it adds to your pawcoins.\n" +
                        "\n" +
                        "The probability of the prizes are:\n" +
                        "\t \t 5% to get diamonds (300 pawcoins + bet).\n" +
                        "\t \t 5% to get sevens (100 pawcoins + bet).\n" +
                        "\t \t 5% to get pawcoins (50 pawcoins + bet).\n" +
                        "\t \t 85% chance of losing.");

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
