package com.example.mislugares;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import adapters.AdaptadorLugaresFirebase;
import adapters.AdaptadorLugaresFirebaseUI;
import adapters.AdaptadorLugaresFirestore;
import adapters.AdaptadorLugaresFirestoreUI;

/**
 * Created by Jesús Tomás on 19/04/2017.
 */

public class SelectorFragment extends Fragment {
    private RecyclerView recyclerView;
    //public static AdaptadorLugaresBD adaptador;
    //public static AdaptadorLugaresFirebaseUI adaptador;
    //public static AdaptadorLugaresFirebase adaptador;
    public static AdaptadorLugaresFirestoreUI adaptador2;
    public static AdaptadorLugaresFirestore adaptador3;

    @Override
    public View onCreateView(LayoutInflater inflador, ViewGroup contenedor,
                             Bundle savedInstanceState) {
        View vista = inflador.inflate(R.layout.fragment_selector,
                contenedor, false);
        recyclerView = (RecyclerView) vista.findViewById(R.id.recycler_view);
        return vista;
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        layoutManager.setAutoMeasureEnabled(true); //Quitar si da problemas

        //adaptador = new AdaptadorLugaresBD(getContext(),
        //        MainActivity.lugares,  MainActivity.lugares.extraeCursor());
        /*Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("lugares")
                .limitToLast(50);*/

        //FirebaseRecyclerOptions<Lugar> opciones = new FirebaseRecyclerOptions
        //        .Builder<Lugar>().setQuery(query, Lugar.class).build();
        //adaptador = new AdaptadorLugaresFirebaseUI(opciones);
        //adaptador = new AdaptadorLugaresFirebase(getContext(), FirebaseDatabase.getInstance().getReference().getRef().getRef().child("lugares"));

        //adaptador.setOnItemClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
        //        ((MainActivity) getActivity()).muestraLugar(
        //                recyclerView.getChildAdapterPosition(v));
                /*Intent i = new Intent(getContext(), VistaLugarActivity.class);
                i.putExtra("id", (long)
                        recyclerView.getChildAdapterPosition(v));
                startActivity(i);*/
         //   }
        //});

        Query query = FirebaseFirestore.getInstance().collection("lugares").limit(50);
        FirestoreRecyclerOptions<Lugar> opciones = new FirestoreRecyclerOptions.Builder<Lugar>().setQuery(query, Lugar.class).build();
        //adaptador2 = new AdaptadorLugaresFirestoreUI(opciones);
        adaptador3 = new AdaptadorLugaresFirestore(getContext(), FirebaseFirestore.getInstance().collection("lugares"));
        //recyclerView.setAdapter(adaptador2);
        adaptador3.setOnItemClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) getActivity()).muestraLugar(
                            recyclerView.getChildAdapterPosition(v));
                /*Intent i = new Intent(getContext(), VistaLugarActivity.class);
                i.putExtra("id", (long)
                        recyclerView.getChildAdapterPosition(v));
                startActivity(i);*/
               }
            });

        recyclerView.setAdapter(adaptador3);

        //recyclerView.setAdapter(adaptador);

        adaptador3.startListening();
        //adaptador2.startListening();
        //adaptador.startListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        adaptador3.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adaptador3.stopListening();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        adaptador3.stopListening();
    }
}