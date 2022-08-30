package com.example.katsino_bd;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.katsino_bd.SQLiteHelper.ConexionSQLiteHelper;
import com.example.katsino_bd.Contract.Contract;
import com.example.katsino_bd.Services.TimerService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    public TextView txt_name, txt_dinero, txt_timer, tvTimer;
    public ImageView ruletaquieta, img_user;
    public ImageButton ranking, home, game, settings;
    public String s_nombre, s_cantidad, s_correo, s_foto;
    public int s_id, money_int, money_earn;
    private FirebaseAuth mAuth;
    public FirebaseUser user;

    /**
     * Método onCreate con la lógica de la ruleta diaria.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        this.setTitle("CATSINO MOBILE GAME");
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        txt_name = findViewById(R.id.txt_name);
        txt_dinero = findViewById(R.id.txt_dinero);
        img_user = findViewById(R.id.img_user);
        txt_timer = findViewById(R.id.text_timer);

        ranking = findViewById(R.id.btn_ranking);
        home = findViewById(R.id.btn_menu_home);
        game = findViewById(R.id.btn_menu_game);
        settings = findViewById(R.id.btn_menu_settings);

        ranking.setOnClickListener(this);
        home.setOnClickListener(this);
        game.setOnClickListener(this);
        settings.setOnClickListener(this);

        checkCurrentUser();

        money_int = new Integer(txt_dinero.getText().toString());
        ruletaquieta = findViewById(R.id.ruletaquieta);
        ruletaquieta.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (ruletaquieta.getVisibility() == View.VISIBLE) {
                    ruletaquieta.setVisibility(v.INVISIBLE);
                    MediaPlayer song = MediaPlayer.create(HomeActivity.this, R.raw.roullete_sound);
                    song.start();
                    txt_dinero.setText(money_int + "");
                    money_earn = money_int;

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //Para poner una imagen aleatoria.
                            ruletaquieta.setVisibility(v.VISIBLE);
                            TypedArray images = getResources().obtainTypedArray(R.array.loading_images_daily);
                            int choice = (int) (Math.random() * images.length());
                            ruletaquieta.setImageResource(images.getResourceId(choice, R.drawable.ruletadailypng));
                            images.recycle();

                            //Añadir el premio al text_view.
                            switch (choice) {
                                case 0:
                                    money_int += 122;
                                    break;
                                case 1:
                                    money_int += 288;
                                    break;
                                case 2:
                                    money_int += 341;
                                    break;
                                case 3:
                                    money_int += 674;
                                    break;
                                case 4:
                                    money_int += 345;
                                    break;
                                case 5:
                                    money_int += 234;
                                    break;
                                case 6:
                                    money_int += 121;
                                    break;
                                case 7:
                                    money_int += 345;
                                    break;
                                case 8:
                                    money_int += 232;
                                    break;
                                case 9:
                                    money_int += 121;
                                    break;
                                case 10:
                                    money_int += 843;
                                    break;
                                case 11:
                                    money_int += 123;
                                    break;
                                case 12:
                                    money_int += 452;
                                    break;
                                case 13:
                                    money_int += 457;
                                    break;
                                case 14:
                                    money_int += 233;
                                    break;

                            }
                            txt_dinero.setText(money_int + "");
                            updatemoney();
                            song.stop();

                            //Dialogo de premio
                            AlertDialog.Builder alert = new AlertDialog.Builder(HomeActivity.this);
                            alert.setTitle("¡Come back tomorrow for more!");
                            alert.setMessage("You win " + (money_int - money_earn) + " points in your daily roulette");
                            alert.create();
                            alert.show();
                        }
                    }, 3500);

                    handleStartTimer();

                } else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(HomeActivity.this);
                    alert.setTitle("An error has ocurred");

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
     * Colocar nombre y monedas en la pantala de Home.
     */
    public void getUserProfile() {
        // [START get_user_profile]
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this, "bd_usuarios", null, 1);
        SQLiteDatabase db = conn.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Contract.TABLA_USUARIO + " WHERE " + Contract.CAMPO_CORREO + " = '" + user.getEmail() + "' ", null);
        while (cursor.moveToNext()) {
            s_id = cursor.getInt(0);
            s_nombre = cursor.getString(1);
            s_cantidad = cursor.getString(2);
            s_correo = cursor.getString(3);
            s_foto = cursor.getString(4);
        }

        if (user != null) {
            // Name, email address, and profile photo Url
            String email = user.getEmail();

            txt_name.setText(s_nombre);
            txt_dinero.setText(s_cantidad);
            colocarfoto();
            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();
        }
        // [END get_user_profile]
    }

    /**
     * Colocar avatar de usuario en la pantalla de Home.
     */
    public void colocarfoto() {
        if (s_foto.equals("profile01")) {
            img_user.setImageResource(R.drawable.profile01);
        } else if (s_foto.equals("profile02")) {
            img_user.setImageResource(R.drawable.profile02);
        } else if (s_foto.equals("profile03")) {
            img_user.setImageResource(R.drawable.profile03);
        } else if (s_foto.equals("profile04")) {
            img_user.setImageResource(R.drawable.profile04);
        } else if (s_foto.equals("profile05")) {
            img_user.setImageResource(R.drawable.profile05);
        } else if (s_foto.equals("profile06")) {
            img_user.setImageResource(R.drawable.profile06);
        } else if (s_foto.equals("profile07")) {
            img_user.setImageResource(R.drawable.profile07);
        } else if (s_foto.equals("profile08")) {
            img_user.setImageResource(R.drawable.profile08);
        } else {
            img_user.setImageResource(R.drawable.profile01);
        }
    }

    /**
     * Evento onClick para botones del menu.
     */
    public void onClick(View v) {
        Intent in;
        switch (v.getId()) {
            case R.id.btn_ranking:
                in = new Intent(this, RankingActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(in);
                break;
            case R.id.btn_menu_home:
                break;
            case R.id.btn_menu_game:
                in = new Intent(this, GamesActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(in);
                break;
            case R.id.btn_menu_settings:
                in = new Intent(this, SettingsActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(in);
                break;
            //default:
            //    throw new IllegalStateException("Unexpected value: " +
            //            v.getId());
        }
    }

    /**
     * Sentencia SQL para actualizar las monedas del jugador.
     */
    public void updatemoney() {
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this, "bd_usuarios", null, 1);
        SQLiteDatabase db = conn.getWritableDatabase();
        String[] parametros = {s_correo};
        ContentValues values = new ContentValues();
        values.put(Contract.CAMPO_CANTIDAD, money_int);

        db.update(Contract.TABLA_USUARIO, values, Contract.CAMPO_CORREO + "=?", parametros);
        //Toast.makeText(this, "Item updated successfully", Toast.LENGTH_LONG).show();
        db.close();
    }

    /**
     * Región temporizador regresivo, lanza mensaje cuando termina.
     * Se activa cuando inicias la ruleta, dura 24h.
     */
    //#region public void TEMPORIZADOR
    public void handleStartTimer() {
        Intent intent = new Intent(this, TimerService.class);
        intent.putExtra("inputExtra", "");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ContextCompat.startForegroundService(this, intent);
        } else {
            this.startService(intent);
        }
    }

    final private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                updateGUI(intent);
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter(TimerService.COUNTDOWN_BR));
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onStop() {
        try {
            unregisterReceiver(broadcastReceiver);
        } catch (Exception e) {
            // Receiver was probably already stopped in onPause()
        }
        super.onStop();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateGUI(Intent intent) {
        if (intent.getExtras() != null) {
            long millisUntilFinished = intent.getLongExtra("countdown", 0);
            long seconds = (millisUntilFinished / 1000) % 60;
            long minutes = (millisUntilFinished / (1000 * 60)) % 60;
            long hours = (millisUntilFinished / (1000 * 60 * 60)) % 60;
            String time = (hours + " : " + minutes + " : " + seconds);
            tvTimer = findViewById(R.id.text_timer);
            tvTimer.setText("DAILY ROULETTE - " + time + " left");
            ruletaquieta.setEnabled(false);


            boolean countdownTimerFinished = intent.getBooleanExtra("countdownTimerFinished", false);
            if (countdownTimerFinished) {
                tvTimer.setText("DAILY ROULETTE - READY!");
                ruletaquieta.setEnabled(true);

                //Notification thing
                String id ="channel_1";
                String description = "123";
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel channel = new NotificationChannel(id, "123", importance);

                //Adding attributes to channel
                channel.enableVibration(true); //vibration
                //channel.enableLights(true); //prompt light
                NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                manager.createNotificationChannel(channel);//Add channel
                Notification notification = new Notification.Builder(HomeActivity.this,id)
                        //Note that there is an additional parameter id, which refers to the ID of the configured Notification Channel.
                        //You can try it on your own and then configure the generation above this line of code after you run it once.
                        //Code comment can also run successfully if the parameter id is changed directly to "channel_1"
                        //But we can't change it to something else like "channel_2".
                        .setCategory(Notification.CATEGORY_MESSAGE)
                        .setSmallIcon(R.drawable.logo_catsino)
                        .setContentTitle("DAILY ROULLETE IS READY")
                        .setContentText("Play now the daily roullete for free!")
                        .setAutoCancel(true)
                        .build();
                manager.notify(1,notification);
            }
        }
    }

    //#endregion
}
