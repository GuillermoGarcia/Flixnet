package com.example.flixnet.flixnet.Adaptadores;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class filmAdapter extends RecyclerView.Adapter<filmAdapter.filmHolder> {

    @NonNull
    @Override
    public filmHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull filmHolder filmHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class filmHolder extends RecyclerView.ViewHolder {

        public filmHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
