package com.example.katsino_bd.Contract;

public class Contract {

    //Constantes campos tabla usuario
    public static final String TABLA_USUARIO="usuario";
    public static final String CAMPO_ID="id";
    public static final String CAMPO_NOMBRE="nombre";
    public static final String CAMPO_CANTIDAD="cantidad";
    public static final String CAMPO_CORREO="correo";
    public static final String CAMPO_FOTO="foto";


    public static final String CREAR_TABLA_USUARIO="CREATE TABLE " +
            ""+TABLA_USUARIO+" ("+CAMPO_ID+" " +
            "INTEGER PRIMARY KEY AUTOINCREMENT, "+CAMPO_NOMBRE+" TEXT,"+CAMPO_CANTIDAD+" TEXT, "
            + CAMPO_CORREO+ " TEXT,"+ CAMPO_FOTO+ " TEXT)";


}