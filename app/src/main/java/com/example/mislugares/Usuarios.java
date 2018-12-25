package com.example.mislugares;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class Usuarios {
    static void guardarUsuario(final FirebaseUser user) {
        /*Usuario usuario = new Usuario(
                user.getDisplayName(), user.getEmail());
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference("usuarios/"+user.getUid()).setValue(usuario);
        */
        Usuario usuario = new Usuario(user.getDisplayName(), user.getEmail());
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("usuarios").document(user.getUid()).set(usuario);
    }
}
