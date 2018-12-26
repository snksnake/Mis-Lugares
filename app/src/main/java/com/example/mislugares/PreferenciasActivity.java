package com.example.mislugares;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class PreferenciasActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new PreferenciasFragment())
                .commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Preferencias pref = Preferencias.getInstance();
        if (pref.usarFirestore()) {
            MainActivity.lugares = new LugaresFirestore();
        } else {
            MainActivity.lugares = new LugaresFirebase();
        }
        SelectorFragment.ponerAdaptador();
    }
}