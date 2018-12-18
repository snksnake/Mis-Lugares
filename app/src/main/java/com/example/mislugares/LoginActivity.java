package com.example.mislugares;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Arrays;

public class LoginActivity extends Activity {

    Button btnSign, btnRegister, btnSignUi;
    EditText etPassword, etUsername;
    private static final int RC_SIGN_IN = 123;
    private GoogleApiClient googleApiClient;
    private FirebaseAuth mAuth;
    private String correo, contraseña;
    private TextInputLayout tilCorreo, tilContraseña;
    private boolean unificar;

    private ViewGroup contenedor;
    private ProgressDialog dialogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseApp.initializeApp(this);

        mAuth = FirebaseAuth.getInstance();

        btnSign = findViewById(R.id.inicio_sesión);
        btnRegister = findViewById(R.id.registro);
        btnSignUi = findViewById(R.id.firebase_ui);
        etPassword = findViewById(R.id.contraseña);
        etUsername = findViewById(R.id.correo);
        dialogo = new ProgressDialog(this);
        dialogo.setTitle("Verificando usuario");
        dialogo.setMessage("Por favor espere...");
        tilCorreo = (TextInputLayout) findViewById(R.id.til_correo);
        tilContraseña = (TextInputLayout) findViewById(R.id.til_contraseña);

        unificar = getIntent().getBooleanExtra("unificar", false);

        verificarSiUsuarioRegistrado();

        btnSignUi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    private void login() {
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
                startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setLogo(R.mipmap.ic_launcher).setTheme(R.style.FirebaseUITema).setAvailableProviders(Arrays.asList(
                        new AuthUI.IdpConfig.Builder(EmailAuthProvider.PROVIDER_ID).build(),
                        new AuthUI.IdpConfig.Builder(PhoneAuthProvider.PROVIDER_ID).build(),
                        new AuthUI.IdpConfig.Builder(GoogleAuthProvider.PROVIDER_ID).build())).setIsSmartLockEnabled(false).build(), RC_SIGN_IN);
            }
        } catch (Exception ex) {
            Log.d("Error", ex.getMessage());
        }
    }


    public void reestablecerContrasena(View v) {
        correo = etUsername.getText().toString();
        tilCorreo.setError("");
        if (correo.isEmpty()) {
            tilCorreo.setError("Introduce un correo");
        } else if (!correo.matches(".+@.+[.].+")) {
            tilCorreo.setError("Correo no válido");
        } else {
            dialogo.show();
            mAuth.sendPasswordResetEmail(correo)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            dialogo.dismiss();
                            if (task.isSuccessful()) {
                                mensaje("Verifica tu correo para cambiar contraseña.");
                            } else {
                                mensaje("ERROR al mandar correo para cambiar contraseña");
                            }
                        }
                    });
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

    private void verificarSiUsuarioRegistrado() {
        FirebaseUser usuario = mAuth.getCurrentUser();
        if (!unificar && usuario != null) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP |
                            Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }

    private void verificaSiUsuarioValidado() {
        if (mAuth.getCurrentUser() != null) {
            Intent i = new Intent(this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        }
    }

    public void inicioSesionCorreo(View v) {
        if (verificaCampos()) {
            dialogo.show();
            mAuth.signInWithEmailAndPassword(etUsername.getText().toString(), etPassword.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                verificaSiUsuarioValidado();
                            } else {
                                dialogo.dismiss();
                                mensaje(task.getException().getLocalizedMessage());
                            }
                        }
                    });
        }
    }

    public void autentificacionAnónima(View v) {
        dialogo.show();
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override public void onComplete(@NonNull Task<AuthResult> task){
                        if (task.isSuccessful()) {
                            verificaSiUsuarioValidado();
                        } else {
                            dialogo.dismiss();
                            Log.w("MisLugares", "Error en signInAnonymously",
                                    task.getException());
                            mensaje( "ERROR al intentarentrar de forma anónima");
                        }
                    }
                });
    }

    public void registroCorreo(View v) {
        if (verificaCampos()) {
            dialogo.show();
            mAuth.createUserWithEmailAndPassword(etUsername.getText().toString(), etPassword.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                verificaSiUsuarioValidado();
                            } else {
                                dialogo.dismiss();
                                mensaje(task.getException().getLocalizedMessage());
                            }
                        }
                    });
        }
    }

    private void mensaje(String mensaje) {
        //Snackbar.make(contenedor, mensaje, Snackbar.LENGTH_LONG).show();
    }

    private boolean verificaCampos() {
        correo = etUsername.getText().toString();
        contraseña = etPassword.getText().toString();
        tilCorreo.setError("");
        tilContraseña.setError("");
        if (correo.isEmpty()) {
            tilCorreo.setError("Introduce un correo");
        } else if (!correo.matches(".+@.+[.].+")) {
            tilCorreo.setError("Correo no válido");
        } else if (contraseña.isEmpty()) {
            tilContraseña.setError("Introduce una contraseña");
        } else if (contraseña.length() < 6) {
            tilContraseña.setError("Ha de contener al menos 6 caracteres");
        } else if (!contraseña.matches(".*[0-9].*")) {
            tilContraseña.setError("Ha de contener un número");
        } else if (!contraseña.matches(".*[A-Z].*")) {
            tilContraseña.setError("Ha de contener una letra mayúscula");
        } else {
            return true;
        }
        return false;
    }

    public void firebaseUI(View v) {
        startActivity(new Intent(this, LoginActivity.class));
    }
}
