package com.example.katsino_bd;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.katsino_bd.Adapters.RankingAdapter;
import com.example.katsino_bd.SQLiteHelper.ConexionSQLiteHelper;
import com.example.katsino_bd.Contract.Contract;
import com.example.katsino_bd.Entity.Players;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class RankingActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this,"bd_usuarios",null,1);
    RecyclerView recyclerViewUsuarios;
    RankingAdapter adapter;
    ArrayList<Players> listaUsuario;
    Button sort, sort_name;
    ImageButton home, game, settings;
    Intent in;

    /**
     * Método onCreate con la lógica de mostrar el RecyclerView con la lista de usuarios.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        mAuth = FirebaseAuth.getInstance();
        this.setTitle(" ");
        listaUsuario=new ArrayList<>();
        recyclerViewUsuarios= (RecyclerView) findViewById(R.id.recyclerPersonas);
        recyclerViewUsuarios.setLayoutManager(new LinearLayoutManager(this));

        getUserList();

        Collections.sort(listaUsuario, new Comparator<Players>() {
            @Override
            public int compare(Players o1, Players o2) {
                return Integer.parseInt(o1.getCantidad()) > Integer.parseInt(o2.getCantidad()) ? -1
                        : (Integer.parseInt(o1.getCantidad()) < Integer.parseInt(o2.getCantidad()) ) ? 1 : 0;
            }
        });

        adapter = new RankingAdapter(listaUsuario, this);
        recyclerViewUsuarios.setAdapter(adapter);

        sort = findViewById(R.id.btn_sort);
        sort.setOnClickListener(this);

        sort_name = findViewById(R.id.btn_sort_name);
        sort_name.setOnClickListener(this);

        home =  findViewById(R.id.btn_menu_home);
        game = findViewById(R.id.btn_menu_game);
        settings =  findViewById(R.id.btn_menu_settings);

        home.setOnClickListener(this);
        game.setOnClickListener(this);
        settings.setOnClickListener(this);

    }

    /**
     * Método que recoge y crea una lista de usuarios.
     */
    private void getUserList() {
        SQLiteDatabase db=conn.getReadableDatabase();
        Players usuario=null;
        Cursor cursor=db.rawQuery("SELECT * FROM "+ Contract.TABLA_USUARIO + " ORDER BY " +Contract.CAMPO_CANTIDAD + " DESC ",null);

        while (cursor.moveToNext()){
            usuario=new Players();
            usuario.setId(cursor.getInt(0));
            usuario.setNombre(cursor.getString(1));
            usuario.setCantidad(cursor.getString(2));
            usuario.setCorreo(cursor.getString(3));
            usuario.setFoto(cursor.getString(4));
            listaUsuario.add(usuario);
        }

    }

    /**
     * Evento onClick para los botones que ordenan alfabeticaente o por numero, o los del menu.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sort:
                if (sort.getText().toString().equals(">")) {
                    sort.setText("<");
                    Collections.sort(listaUsuario, new Comparator<Players>() {
                        @Override
                        public int compare(Players o1, Players o2) {
                            return Integer.parseInt(o1.getCantidad()) > Integer.parseInt(o2.getCantidad()) ? -1 : (Integer.parseInt(o1.getCantidad()) < Integer.parseInt(o2.getCantidad()) ) ? 1 : 0;
                        }
                    });
                } else {
                    sort.setText(">");
                    Collections.sort(listaUsuario, new Comparator<Players>() {
                        @Override
                        public int compare(Players o1, Players o2) {
                            return Integer.parseInt(o2.getCantidad()) > Integer.parseInt(o1.getCantidad()) ? -1 : (Integer.parseInt(o2.getCantidad()) < Integer.parseInt(o1.getCantidad()) ) ? 1 : 0;
                        }
                    });
                }
                Collections.reverse(listaUsuario);
                adapter.notifyDataSetChanged();
                break;
            case R.id.btn_sort_name:
                if (sort_name.getText().toString().equals(">")) {
                    sort_name.setText("<");
                    Collections.sort(listaUsuario, new Comparator<Players>() {
                        @Override
                        public int compare(Players o3, Players o4) {
                            return o3.getNombre().compareToIgnoreCase(o4.getNombre());
                        }
                    });
                } else {
                    sort_name.setText(">");
                    Collections.sort(listaUsuario, new Comparator<Players>() {
                        @Override
                        public int compare(Players o3, Players o4) {
                            return o4.getNombre().compareToIgnoreCase(o3.getNombre());
                        }
                    });
                }
                adapter.notifyDataSetChanged();
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
                in = new Intent( this, SettingsActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(in);
                break;
        }
    }
}
