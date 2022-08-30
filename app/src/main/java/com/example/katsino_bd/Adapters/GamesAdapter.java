package com.example.katsino_bd.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.katsino_bd.R;

import java.util.ArrayList;

public class GamesAdapter extends RecyclerView.Adapter<GamesAdapter.GamesAdapterHolder> {
    ArrayList<Integer> imagenes = new ArrayList<Integer>();
    ArrayList<String> nombres = new ArrayList<String>();
    ArrayList<Integer> estrellas = new ArrayList<Integer>();

    public GamesAdapter(ArrayList<Integer> imagenes, ArrayList<String> nombres, ArrayList<Integer> estrellas) {
        this.imagenes=imagenes;
        this.nombres=nombres;
        this.estrellas=estrellas;

    }

    GamesInterface m;
    public void setInterface(GamesInterface m){
        this.m=m;
    }

    @NonNull
    @Override
    public GamesAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater v=LayoutInflater.from(parent.getContext());
        GamesAdapterHolder m= new GamesAdapterHolder(v.inflate(R.layout.lista, parent, false));
        return m;
    }

    // Método que escribe los datos en cada item de la lista
    @Override
    public void onBindViewHolder(@NonNull GamesAdapterHolder holder, int position) {
        holder.imprimir(position);
    }

    //Devuelve el número de elementos de la lista
    @Override
    public int getItemCount() {
        return nombres.size();
    }

    public class GamesAdapterHolder extends RecyclerView.ViewHolder  {
        //referencio los objetos del layout que he creado para los elementos de la lista
        TextView tv1;
        ImageView iv, iv2;
        //constructor lo crea el asistente.
        public GamesAdapterHolder(@NonNull View itemView) {
            super(itemView);
            tv1=itemView.findViewById(R.id.game_one_text);
            iv=itemView.findViewById(R.id.game_one);
            iv2=itemView.findViewById(R.id.game_one_stars);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    m.miMetodo((int)getLayoutPosition());
                }
            });
        }
        //Asigno los datos al MiAdaptadorHolder de la posición position
        public void imprimir(int position) {
            tv1.setText(nombres.get(position));
            iv2.setImageResource(estrellas.get(position));
            iv.setImageResource(imagenes.get(position));
        }
    }
}
