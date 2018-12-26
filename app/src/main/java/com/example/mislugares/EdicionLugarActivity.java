package com.example.mislugares;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;

public class EdicionLugarActivity extends AppCompatActivity {
    private long id;
    private String _id;
    private Lugar lugar;
    private EditText nombre;
    private Spinner tipo;
    private EditText direccion;
    private EditText telefono;
    private EditText url;
    private EditText comentario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edicion_lugar);
        Bundle extras = getIntent().getExtras();
        id = extras.getLong("id", -1);
        /*_id = extras.getLong("_id", -1);
        if (_id!=-1) {
            lugar = MainActivity.lugares.elemento((int) _id);
        } else {
            lugar = SelectorFragment.adaptador.lugarPosicion((int) id);
        }*/
        _id = extras.getString("_id", null);
        if (_id != null) {
            lugar = new Lugar();
        } else {
            lugar = SelectorFragment.adaptador3.getItem((int) id);
            _id = SelectorFragment.adaptador3.getKey((int) id);
        }
        nombre = (EditText) findViewById(R.id.nombre);
        nombre.setText(lugar.getNombre());
        direccion = (EditText) findViewById(R.id.direccion);
        direccion.setText(lugar.getDireccion());
        telefono = (EditText) findViewById(R.id.telefono);
        telefono.setText(Integer.toString(lugar.getTelefono()));
        url = (EditText) findViewById(R.id.url);
        url.setText(lugar.getUrl());
        comentario = (EditText) findViewById(R.id.comentario);
        comentario.setText(lugar.getComentario());
        tipo = (Spinner) findViewById(R.id.tipo);
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, TipoLugar.getNombres());
        adaptador.setDropDownViewResource(android.R.layout.
                simple_spinner_dropdown_item);
        tipo.setAdapter(adaptador);
        tipo.setSelection(lugar.getTipo().ordinal());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edicion_lugar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.accion_cancelar:
                /*if (getIntent().getExtras().getBoolean("nuevo", false)) {
                    MainActivity.lugares.borrar((int) id);
                }*/
                if (getIntent().getExtras().getBoolean("nuevo", false)) {
                    MainActivity.lugares.borrar(_id);
                }
                finish();
                return true;
            case R.id.accion_guardar:
                lugar.setNombre(nombre.getText().toString());
                lugar.setTipo(TipoLugar.values()[tipo.getSelectedItemPosition()]);
                lugar.setDireccion(direccion.getText().toString());
                lugar.setTelefono(Integer.parseInt(telefono.getText().toString()));
                lugar.setUrl(url.getText().toString());
                lugar.setComentario(comentario.getText().toString());
                /*if (_id == -1) {
                    _id = SelectorFragment.adaptador.idPosicion((int) id);
                }
                MainActivity.lugares.actualiza((int) _id, lugar);
                SelectorFragment.adaptador.setCursor(MainActivity.lugares.extraeCursor());
                if (id != -1) {
                    SelectorFragment.adaptador.notifyItemChanged((int) id);
                } else {
                    SelectorFragment.adaptador.notifyDataSetChanged();
                }*/
                MainActivity.lugares.actualiza(_id, lugar);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
