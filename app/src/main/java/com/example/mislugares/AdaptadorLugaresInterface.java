package com.example.mislugares;

import android.view.View;

public interface AdaptadorLugaresInterface {
    public String getKey(int pos);

    public Lugar getItem(int pos);

    public void setOnItemClickListener(View.OnClickListener onClick);

    public void startListening();

    public void stopListening();
}