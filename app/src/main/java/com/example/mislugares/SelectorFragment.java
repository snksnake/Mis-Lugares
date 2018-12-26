package com.example.mislugares;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import adapters.AdaptadorLugaresFirebase;
import adapters.AdaptadorLugaresFirebaseUI;
import adapters.AdaptadorLugaresFirestore;
import adapters.AdaptadorLugaresFirestoreUI;

import static com.example.mislugares.Preferencias.SELECCION_MIOS;
import static com.example.mislugares.Preferencias.SELECCION_TIPO;

/**
 * Created by Jesús Tomás on 19/04/2017.
 */

public class SelectorFragment extends Fragment {
    private static RecyclerView recyclerView;
    //public static AdaptadorLugaresBD adaptador;
    //public static AdaptadorLugaresFirebaseUI adaptador;
    //public static AdaptadorLugaresFirebase adaptador;
    public static AdaptadorLugaresFirestoreUI adaptador2;
    //public static AdaptadorLugaresFirestore adaptador3;
    public static Context context;
    public static RecyclerView.Adapter adaptador3;

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
        context = getContext();
        ponerAdaptador();

    }

    @Override
    public void onStart() {
        super.onStart();
        getAdaptador().startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        getAdaptador().stopListening();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getAdaptador().stopListening();
    }

    public static void ponerAdaptador() {
        Preferencias pref = Preferencias.getInstance();
        pref.inicializa(context);
// Poner el código aquí }
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

        //Query query = FirebaseFirestore.getInstance().collection("lugares").limit(50);

        /*Query query = FirebaseFirestore.getInstance().collection("lugares").orderBy(pref.criterioOrdenacion()).limit(pref.maximoMostrar());
        switch (pref.criterioSeleccion()) {
            case SELECCION_MIOS:
                query = query.whereEqualTo("creador", FirebaseAuth.getInstance().getCurrentUser().getUid());
                break;
            case SELECCION_TIPO:
                query = query.whereEqualTo("tipo", pref.tipoSeleccion());
                break;
        }*/

        /*com.google.firebase.database.Query query = FirebaseDatabase.getInstance().getReference().child("lugares").limitToLast(pref.maximoMostrar());
        switch (pref.criterioSeleccion()) {
            case SELECCION_MIOS:
                query = query.orderByChild("creador").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
                break;
            case SELECCION_TIPO:
                query = query.orderByChild("tipo").equalTo(pref.tipoSeleccion());
                break;
            default:
                query = query.orderByChild(pref.criterioOrdenacion());
                break;
        }*/

        //FirestoreRecyclerOptions<Lugar> opciones = new FirestoreRecyclerOptions.Builder<Lugar>().setQuery(query, Lugar.class).build();
        //adaptador2 = new AdaptadorLugaresFirestoreUI(opciones);
        //adaptador3 = new AdaptadorLugaresFirestore(getContext(), FirebaseFirestore.getInstance().collection("lugares"));
        //recyclerView.setAdapter(adaptador2);

        if (pref.usarFirestore()) {
            com.google.firebase.firestore.Query query = FirebaseFirestore.getInstance().collection("lugares").orderBy(pref.criterioOrdenacion()).limit(pref.maximoMostrar());
            switch (pref.criterioSeleccion()) {
                case SELECCION_MIOS:
                    query = query.whereEqualTo("creador", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    break;
                case SELECCION_TIPO:
                    query = query.whereEqualTo("tipo", pref.tipoSeleccion());
                    break;
            }
            if (pref.usarFirebaseUI()) {
                FirestoreRecyclerOptions<Lugar> options = new FirestoreRecyclerOptions.Builder<Lugar>().setQuery(query, Lugar.class).build();
                adaptador3 = new AdaptadorLugaresFirestoreUI(options);
            } else {
                adaptador3 = new AdaptadorLugaresFirestore(context, query);
            }
        } else {
            if (pref.usarFirebaseUI()) {
                com.google.firebase.database.Query query = FirebaseDatabase.getInstance().getReference().child("lugares").limitToLast(pref.maximoMostrar());
                switch (pref.criterioSeleccion()) {
                    case SELECCION_MIOS:
                        query = query.orderByChild("creador").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        break;
                    case SELECCION_TIPO:
                        query = query.orderByChild("tipo").equalTo(pref.tipoSeleccion());
                        break;
                    default:
                        query = query.orderByChild(pref.criterioOrdenacion());
                        break;
                }
                FirebaseRecyclerOptions<Lugar> options = new FirebaseRecyclerOptions.Builder<Lugar>().setQuery(query, Lugar.class).build();
                adaptador3 = new AdaptadorLugaresFirebaseUI(options);
            } else {
                adaptador3 = new AdaptadorLugaresFirebase(context, FirebaseDatabase.getInstance().getReference("lugares"));
            }
        }
        getAdaptador().setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) context).muestraLugar(recyclerView.getChildAdapterPosition(v));
            }
        });
        getAdaptador().startListening();
        recyclerView.setAdapter(adaptador3);

        /*getAdaptador().setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).muestraLugar(
                        recyclerView.getChildAdapterPosition(v));
                /*Intent i = new Intent(getContext(), VistaLugarActivity.class);
                i.putExtra("id", (long)
                        recyclerView.getChildAdapterPosition(v));
                startActivity(i);*/
            /*}
        });

        recyclerView.setAdapter(adaptador3);*/

        //recyclerView.setAdapter(adaptador);

        //getAdaptador().startListening();
        //adaptador2.startListening();
        //adaptador.startListening();
    }

    public static AdaptadorLugaresInterface getAdaptador() {
        return (AdaptadorLugaresInterface) adaptador3;
    }
}