package com.example.mislugares;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    Button btnSign, btnRegister, btnSignUi, tryregister;
    EditText password, username;
    private static final int RC_SIGN_IN = 123;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseApp.initializeApp(this);

        mAuth = FirebaseAuth.getInstance();

        btnSign = findViewById(R.id.signin);
        btnRegister = findViewById(R.id.register);
        btnSignUi = findViewById(R.id.signinui);
        password = findViewById(R.id.password);
        username = findViewById(R.id.username);

        btnSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
                //verificarSiUsuarioRegistrado();

            }
        });
    }

    private void login(){
        try {
            FirebaseUser usuario = mAuth.getCurrentUser();

            if (usuario != null) {
                Toast.makeText(this, getText(R.string.session_message), Toast.LENGTH_LONG).show();

                Uri photo = usuario.getPhotoUrl();
                String mail = usuario.getEmail();
                String provider = usuario.getProviderId();
                String username = usuario.getDisplayName();
                String uuid = usuario.getUid();

                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(
                        Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                Intent.FLAG_ACTIVITY_NEW_TASK |
                                Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("username", username);
                intent.putExtra("mail", mail);
                intent.putExtra("provider", provider);
                intent.putExtra("uuid", uuid);
                intent.putExtra("photo", photo);

                startActivity(intent);
            } else {
                startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(Arrays.asList(
                        new AuthUI.IdpConfig.Builder(EmailAuthProvider.PROVIDER_ID).build(),
                        new AuthUI.IdpConfig.Builder(PhoneAuthProvider.PROVIDER_ID).build(),
                        new AuthUI.IdpConfig.Builder(GoogleAuthProvider.PROVIDER_ID).build())).setIsSmartLockEnabled(false).build(), RC_SIGN_IN);
            }
        }catch (Exception ex){
            Log.d("Error", ex.getMessage());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == ResultCodes.OK) {
                login();
                finish();
            } else {
                IdpResponse response = IdpResponse.fromResultIntent(data);
                if (response == null) {
                    Toast.makeText(this, "Cancelado", Toast.LENGTH_LONG).show();
                    return;
                } else if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(this, "No estas conectado", Toast.LENGTH_LONG).show();
                    return;
                } else if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Toast.makeText(this, "Error desconocido", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }
    }

    private void verificarSiUsuarioRegistrado(){
        FirebaseUser usuario = mAuth.getCurrentUser();
        if(usuario !=null){
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }
}
