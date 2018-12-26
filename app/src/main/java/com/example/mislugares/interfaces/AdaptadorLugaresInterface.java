package com.example.mislugares.interfaces;

import android.view.View;

import com.example.mislugares.entidades.Lugar;

public interface AdaptadorLugaresInterface {
    public String getKey(int pos);

    public Lugar getItem(int pos);

    public void setOnItemClickListener(View.OnClickListener onClick);

    public void startListening();

    public void stopListening();
}