package com.example.mislugares;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.example.mislugares.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UsuarioFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflador, ViewGroup contenedor, Bundle savedInstanceState) {
        View vista = inflador.inflate(R.layout.fragment_usuario, contenedor, false);
        FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
        TextView nombre = vista.findViewById(R.id.nombre);
        TextView email = vista.findViewById(R.id.email);
        TextView providers = vista.findViewById(R.id.providers);
        TextView phoneNumber = vista.findViewById(R.id.phone_number);
        TextView uid = vista.findViewById(R.id.uid);

        nombre.setText(usuario.getDisplayName());
        email.setText(usuario.getEmail());
        providers.setText(usuario.getProviders().toString());
        phoneNumber.setText(usuario.getPhoneNumber());
        uid.setText(usuario.getUid());

        Button cerrarSesion = vista.findViewById(R.id.btn_cerrar_sesion);
        cerrarSesion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AuthUI.getInstance().signOut(getActivity()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent i = new Intent(getActivity(), LoginActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        getActivity().finish();
                    }
                });
            }
        });

        RequestQueue colaPeticiones = Volley.newRequestQueue(getActivity().getApplicationContext());
        ImageLoader lectorImagenes = new ImageLoader(colaPeticiones, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(10);

            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }

            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }
        });

        Uri urlImagen = usuario.getPhotoUrl();
        if (urlImagen != null) {
            NetworkImageView fotoUsuario = vista.findViewById(R.id.imagen);
            fotoUsuario.setImageUrl(urlImagen.toString(), lectorImagenes);
        }

        return vista;
    }
}