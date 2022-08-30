package com.example.katsino_bd.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.katsino_bd.Entity.Players;
import com.example.katsino_bd.R;

import java.util.ArrayList;

public class RankingAdapter extends RecyclerView.Adapter<RankingAdapter.PersonasViewHolder> {

    ArrayList<Players> listaUsuario;
    Context con;
    String fotito;

    public RankingAdapter(ArrayList<Players> listaUsuario, Context con) {
        this.listaUsuario = listaUsuario;
        this.con = con;
    }

    @Override
    public PersonasViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_personas,null,false);

        return new PersonasViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PersonasViewHolder holder, int position) {
        holder.nombre.setText(listaUsuario.get(position).getNombre());
        holder.cantidad.setText(listaUsuario.get(position).getCantidad());
        fotito = (listaUsuario.get(position).getFoto());

        if(fotito.equals("profile01")) {
            holder.rv_image.setImageResource(R.drawable.profile01);
        } else if (fotito.equals("profile02")) {
            holder.rv_image.setImageResource(R.drawable.profile02);
        } else if (fotito.equals("profile03")) {
            holder.rv_image.setImageResource(R.drawable.profile03);
        } else if (fotito.equals("profile04")) {
            holder.rv_image.setImageResource(R.drawable.profile04);
        } else if (fotito.equals("profile05")) {
            holder.rv_image.setImageResource(R.drawable.profile05);
        } else if (fotito.equals("profile06")) {
            holder.rv_image.setImageResource(R.drawable.profile06);
        } else if (fotito.equals("profile07")) {
            holder.rv_image.setImageResource(R.drawable.profile07);
        } else if (fotito.equals("profile08")) {
            holder.rv_image.setImageResource(R.drawable.profile08);
        } else {
            holder.rv_image.setImageResource(R.drawable.profile01);
        }

    }

    @Override
    public int getItemCount() {
        return listaUsuario.size();
    }

    public class PersonasViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView nombre, cantidad;
        CardView card;
        ImageView rv_image;


        public PersonasViewHolder(View itemView) {
            super(itemView);
            nombre = (TextView) itemView.findViewById(R.id.textNombre);
            cantidad = (TextView) itemView.findViewById(R.id.textCantidad);
            rv_image = (ImageView) itemView.findViewById(R.id.rv_image);
            card = (CardView) itemView.findViewById(R.id.card_view_one);
        }

        // Cuando cliquemos una se abrira un nuevo activity
        @Override
        public void onClick(View view) {

        }

    }
}
