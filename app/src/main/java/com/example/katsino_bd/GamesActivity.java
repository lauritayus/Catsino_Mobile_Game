package com.example.katsino_bd;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.katsino_bd.Adapters.GamesAdapter;
import com.example.katsino_bd.Adapters.GamesInterface;
import com.example.katsino_bd.Games.GameBubblesActivity;
import com.example.katsino_bd.Games.GameDicesActivity;
import com.example.katsino_bd.Games.GameRoulleteActivity;
import com.example.katsino_bd.Games.GameSlotsActivity;

import java.util.ArrayList;

public class GamesActivity extends AppCompatActivity implements View.OnClickListener{

    ImageButton search, home, game, settings;
    Context myContex;
    RecyclerView rv1;
    ArrayList<String> nombres = new ArrayList<String>();
    ArrayList<Integer> estrellas = new ArrayList<Integer>();
    ArrayList<Integer> imagenes = new ArrayList<Integer>();
    GamesAdapter miAdaptador;

    /**
     * Método onCreate con la lógica del adaptador del Recycler View.
     */
    protected void onCreate(Bundle savedInstanceState) {
        fillArrays();
        super.onCreate(savedInstanceState);
        this.setTitle(" ");
        setContentView(R.layout.activity_game);
        Intent in = getIntent();
        myContex = this.getApplicationContext();

        search = findViewById(R.id.btn_menu_search);
        home =  findViewById(R.id.btn_menu_home);
        game = findViewById(R.id.btn_menu_game);
        settings =  findViewById(R.id.btn_menu_settings);

        search.setOnClickListener(this);
        home.setOnClickListener(this);
        game.setOnClickListener(this);
        settings.setOnClickListener(this);

        rv1 = (RecyclerView) findViewById(R.id.rv1);
        rv1.setLayoutManager(new GridLayoutManager(this, 2));//Se lo asigno a mi Recycler
        miAdaptador = new GamesAdapter(imagenes, nombres, estrellas);
        rv1.setAdapter(miAdaptador);
        miAdaptador.setInterface(new GamesInterface() {
            @Override
            public void miMetodo(int n) {
                //Toast.makeText(getApplicationContext(), String.valueOf(n), Toast.LENGTH_SHORT).show();
                Intent in;
                switch (n) {
                    case 0:
                        in = new Intent(myContex, GameSlotsActivity.class);
                        startActivity(in);
                        break;
                    case 1:
                        in = new Intent(myContex, GameRoulleteActivity.class);
                        startActivity(in);
                        break;
                    case 2:
                        in = new Intent(myContex, GameBubblesActivity.class);
                        startActivity(in);
                        break;
                    case 3:
                        in = new Intent(myContex, GameDicesActivity.class);
                        startActivity(in);
                        break;
                }
            }
        });
    }

    /**
     * Método de llenado de arrays con imagenes y titulos de los juegos.
     */
    private void fillArrays() {
        imagenes = new ArrayList<Integer>();
        imagenes.add(R.drawable.png_game_slot);
        imagenes.add(R.drawable.png_game_roullette);
        imagenes.add(R.drawable.png_game_bubbles);
        imagenes.add(R.drawable.png_game_dices);
        nombres = new ArrayList<String>();
        nombres.add("Game Slots");
        nombres.add("Game Roulette");
        nombres.add("Game Bubbles");
        nombres.add("Game Dices");
        estrellas = new ArrayList<Integer>();
        estrellas.add(R.drawable.five_stars);
        estrellas.add(R.drawable.five_stars);
        estrellas.add(R.drawable.five_stars);
        estrellas.add(R.drawable.five_stars);
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
                break;
            case R.id.btn_menu_settings:
                in = new Intent( this, SettingsActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(in);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " +
                        v.getId());
        }
    }
}

