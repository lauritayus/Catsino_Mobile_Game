package com.example.katsino_bd.Games;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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

public class GameBubblesActivity extends AppCompatActivity implements View.OnClickListener {

    GifImageView gif_coins;
    TextView actual_money_text;
    int money_int, cont_game = 3, cont_money = 0;
    FirebaseUser user;
    String user_money, user_email;
    ImageView cs1, cs2, cs3, cs4, cs5, cs6, cs7, cs8, cs9, cs10,
            cs11, cs12, cs13, cs14, cs15, cs16, cs17, cs18, cs19, cs20;

    /**
     * Método onCreate con la lógica del juego de las burbujas.
     */
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.setTitle(" ");
        setContentView(R.layout.game_bubbles);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        user = FirebaseAuth.getInstance().getCurrentUser();
        actual_money_text = findViewById(R.id.dineroactual);
        gif_coins = findViewById(R.id.coinsgif);

        cs1 = findViewById(R.id.cs1);cs2 = findViewById(R.id.cs2);cs3 = findViewById(R.id.cs3);cs4 = findViewById(R.id.cs4);cs5 = findViewById(R.id.cs5);
        cs6 = findViewById(R.id.cs6);cs7 = findViewById(R.id.cs7);cs8 = findViewById(R.id.cs8);cs9 = findViewById(R.id.cs9);cs10 = findViewById(R.id.cs10);
        cs11 = findViewById(R.id.cs11);cs12 = findViewById(R.id.cs12);cs13 = findViewById(R.id.cs13);cs14 = findViewById(R.id.cs14);cs15 = findViewById(R.id.cs15);
        cs16 = findViewById(R.id.cs16);cs17 = findViewById(R.id.cs17);cs18 = findViewById(R.id.cs18);cs19 = findViewById(R.id.cs19);cs20 = findViewById(R.id.cs20);


        cs1.setOnClickListener(this);cs2.setOnClickListener(this);cs3.setOnClickListener(this);cs4.setOnClickListener(this);cs5.setOnClickListener(this);
        cs6.setOnClickListener(this);cs7.setOnClickListener(this);cs8.setOnClickListener(this);cs9.setOnClickListener(this);cs10.setOnClickListener(this);
        cs11.setOnClickListener(this);cs12.setOnClickListener(this);cs13.setOnClickListener(this);cs14.setOnClickListener(this);cs15.setOnClickListener(this);
        cs16.setOnClickListener(this);cs17.setOnClickListener(this);cs18.setOnClickListener(this);cs19.setOnClickListener(this);cs20.setOnClickListener(this);

        checkCurrentUser();
        money_int = new Integer(actual_money_text.getText().toString());

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
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this, "bd_usuarios", null, 1);
        SQLiteDatabase db = conn.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + Contract.CAMPO_CANTIDAD + " FROM " + Contract.TABLA_USUARIO + " WHERE " + Contract.CAMPO_CORREO + " = '" + user.getEmail() + "' ", null);
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
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this, "bd_usuarios", null, 1);
        SQLiteDatabase db = conn.getWritableDatabase();
        String[] parametros = {user_email};
        ContentValues values = new ContentValues();
        values.put(Contract.CAMPO_CANTIDAD, money_int);

        db.update(Contract.TABLA_USUARIO, values, Contract.CAMPO_CORREO + "=?", parametros);
        //Toast.makeText(this, "Item updated successfully", Toast.LENGTH_LONG).show();
        db.close();
    }

    /**
     * Evento del juego de las burbujas.
     * Maneja 3 clicks en burbujas y luego resetea.
     */
    public void clickOnBubble(ImageView bubble) {
        if (money_int >= 50) {

            if (cont_game > 0) {
                if(cont_game == 3 ){
                    money_int = money_int - 50;
                    actual_money_text.setText(money_int + "");
                    gif_coins.setVisibility(View.VISIBLE);
                    gif_coins.setImageResource(R.drawable.coinsgif);
                };

                TypedArray images = getResources().obtainTypedArray(R.array.loading_images_bubbles);
                int choice = (int) (Math.random() * images.length());
                bubble.setImageResource(images.getResourceId(choice, R.drawable.ruleta01_12));
                images.recycle();

                switch (choice) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                        cont_money += 1;
                        break;
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                    case 10:
                        cont_money += 5;
                        break;
                    case 11:
                    case 12:
                    case 13:
                    case 14:
                        cont_money += 10;
                        break;
                    case 15:
                    case 16:
                    case 17:
                        cont_money += 25;
                        break;
                    case 18:
                    case 19:
                        cont_money += 50;
                        break;
                    case 20:
                    case 21:
                        cont_money += 100;
                        break;
                    case 22:
                        cont_money += 500;
                        break;
                    case 23:
                        cont_money += 1000;
                        break;
                    case 24:
                        cont_money += 2022;
                        break;

                }

                cont_game--;

                if(cont_game == 0) {
                    money_int += cont_money;
                    actual_money_text.setText(money_int + "");
                    updateMoney();
                    Toast.makeText(this, "You win " + cont_money + " coins!!", Toast.LENGTH_LONG).show();
                }
            } else {
                reset();
            }
        }else {
            AlertDialog.Builder alert = new AlertDialog.Builder(GameBubblesActivity.this);
            alert.setTitle("You don't have enough money");

            alert.setPositiveButton("Ok", null);
            alert.create();
            alert.show();
        }
    }

    /**
     * Evento para resetear las imagenes cada juego nuevo.
     */
    public void reset() {
        cs1.setImageResource(R.drawable.bubble);
        cs2.setImageResource(R.drawable.bubble);
        cs3.setImageResource(R.drawable.bubble);
        cs4.setImageResource(R.drawable.bubble);
        cs5.setImageResource(R.drawable.bubble);
        cs6.setImageResource(R.drawable.bubble);
        cs7.setImageResource(R.drawable.bubble);
        cs8.setImageResource(R.drawable.bubble);
        cs9.setImageResource(R.drawable.bubble);
        cs10.setImageResource(R.drawable.bubble);
        cs11.setImageResource(R.drawable.bubble);
        cs12.setImageResource(R.drawable.bubble);
        cs13.setImageResource(R.drawable.bubble);
        cs14.setImageResource(R.drawable.bubble);
        cs15.setImageResource(R.drawable.bubble);
        cs16.setImageResource(R.drawable.bubble);
        cs17.setImageResource(R.drawable.bubble);
        cs18.setImageResource(R.drawable.bubble);
        cs19.setImageResource(R.drawable.bubble);
        cs20.setImageResource(R.drawable.bubble);
        cont_money = 0;
        cont_game = 3;
    }

    /**
     * Evento onClick para cada casilla.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cs1:
                clickOnBubble(cs1);
                break;
            case R.id.cs2:
                clickOnBubble(cs2);
                break;
            case R.id.cs3:
                clickOnBubble(cs3);
                break;
            case R.id.cs4:
                clickOnBubble(cs4);
                break;
            case R.id.cs5:
                clickOnBubble(cs5);
                break;
            case R.id.cs6:
                clickOnBubble(cs6);
                break;
            case R.id.cs7:
                clickOnBubble(cs7);
                break;
            case R.id.cs8:
                clickOnBubble(cs8);
                break;
            case R.id.cs9:
                clickOnBubble(cs9);
                break;
            case R.id.cs10:
                clickOnBubble(cs10);
                break;
            case R.id.cs11:
                clickOnBubble(cs11);
                break;
            case R.id.cs12:
                clickOnBubble(cs12);
                break;
            case R.id.cs13:
                clickOnBubble(cs13);
                break;
            case R.id.cs14:
                clickOnBubble(cs14);
                break;
            case R.id.cs15:
                clickOnBubble(cs15);
                break;
            case R.id.cs16:
                clickOnBubble(cs16);
                break;
            case R.id.cs17:
                clickOnBubble(cs17);
                break;
            case R.id.cs18:
                clickOnBubble(cs18);
                break;
            case R.id.cs19:
                clickOnBubble(cs19);
                break;
            case R.id.cs20:
                clickOnBubble(cs20);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " +
                        v.getId());
        }
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
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(GameBubblesActivity.this);
                builder.setTitle("Instructions");
                builder.setMessage("Each game costs 50 pawcoins.\n" +
                        "\n" +
                        "The goal of the game is to pop 3 bubbles and see what's underneath. At the end, the sum of the prizes are added to your pawcoins.\n" +
                        "\n" +
                        "The probability of the prizes are:\n" +
                        "\t \t 24% chance of getting a 1.\n" +
                        "\t \t 20% to get a 5.\n" +
                        "\t \t 18% to get a 10.\n" +
                        "\t \t 12% to get a 25.\n" +
                        "\t \t 8% to get a 50.\n" +
                        "\t \t 8% to get a 100.\n" +
                        "\t \t 4% to get a 500.\n" +
                        "\t \t 4% to get a 1000.\n" +
                        "\t \t 2% to get a 2022.");

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
