package com.example.proyestoandroid;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import model.Model;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder>{

    private ArrayList<Model> localDataset;

    public CustomAdapter(ArrayList<Model> datos){
        localDataset = datos;
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
        Model model = (Model)localDataset.get(position);
        Model proto = model.getProto();
        Log.i("CUSTOMHOLDER", ""+position);
        holder.getTextView().setText(model.read(proto.proto_getCampoPrimario()).toString());
    }

    @Override
    public int getItemCount() {
        return localDataset.size();
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public CustomViewHolder(View v){
            super(v);
            textView = (TextView) v.findViewById(R.id.textview_result);
        }
        public TextView getTextView() {
            return textView;
        }
    }
}
