package com.example.proyestoandroid;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.proyestoandroid.abcc.AbccActivity;

import java.util.ArrayList;

import model.Model;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder>{

    private ArrayList<Model> localDataset;
    AbccActivity source;
    public CustomAdapter(ArrayList<Model> datos, AbccActivity source){
        localDataset = datos;
        this.source = source;
    }
    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.textview_recyclerview, parent, false);

        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        Model model = localDataset.get(position);
        Log.i("CUSTOMHOLDER", ""+position);
        holder.bindModel(model, source);
    }
    @Override
    public int getItemCount() {
        return localDataset.size();
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public Button btnEliminar;
        public Button btnEditar;
        public Button btnDetalles;
        public Model m;
        public CustomViewHolder(View v){
            super(v);
            textView = v.findViewById(R.id.textview_result);
            btnEliminar = v.findViewById(R.id.textview_result_btnEliminar);
            btnEditar = v.findViewById(R.id.textview_result_btnEditar);
            btnDetalles = v.findViewById(R.id.textview_result_btnDetalles);
        }
        public TextView getTextView() {
            return textView;
        }
        public void bindModel(Model m, AbccActivity source){
            this.m = m;
            getTextView().setText(m.toString());
            Model proto = m.getProto();
            String primaria = m.read(proto.proto_getCampoPrimario()).toString();

            ///bindear botones a callbacks
            btnDetalles.setOnClickListener(v -> {
                source.detallesIntent(m);
            });
            btnEditar.setOnClickListener(v -> {
                source.editarIntent(primaria, m);
            });
            btnEliminar.setOnClickListener(v -> {
                source.eliminar(primaria);
            });
        }
    }
}
