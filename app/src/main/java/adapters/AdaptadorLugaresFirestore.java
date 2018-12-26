package adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mislugares.AdaptadorLugares;
import com.example.mislugares.Lugar;
import com.example.mislugares.R;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static com.example.mislugares.AdaptadorLugares.personalizaVista;
import static java.lang.Math.min;
import static java.lang.StrictMath.abs;

public class AdaptadorLugaresFirestore extends RecyclerView.Adapter<AdaptadorLugares.ViewHolder> implements EventListener<QuerySnapshot> {
    public static final String TAG = "Mislugares";
    private Query query;
    private List<DocumentSnapshot> items;
    private ListenerRegistration registration;
    private LayoutInflater inflador;
    private View.OnClickListener onClickListener;

    public AdaptadorLugaresFirestore(Context contexto, Query query) {
        items = new ArrayList<DocumentSnapshot>();
        //reference = ref;
        this.query = query;
        inflador = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public AdaptadorLugares.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflador.inflate(R.layout.elemento_lista, null);
        v.setOnClickListener(onClickListener);
        return new AdaptadorLugares.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AdaptadorLugares.ViewHolder holder, int posicion) {
        Lugar lugar = getItem(posicion);
        personalizaVista(holder, lugar);
    }

    public Lugar getItem(int pos) {
        return items.get(pos).toObject(Lugar.class);
    }

    public String getKey(int pos) {
        return items.get(pos).getId();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setOnItemClickListener(View.OnClickListener onClick) {
        onClickListener = onClick;
    }

    public void startListening() {
        items = new ArrayList<DocumentSnapshot>();
        registration = query.addSnapshotListener(this);
    }

    public void stopListening() {
        registration.remove();
    }

    @Override
    public void onEvent(QuerySnapshot snapshots, FirebaseFirestoreException e) {
        if (e != null) {
            Log.w(TAG, "error al recibir evento", e);
            return;
        }
        for (DocumentChange dc : snapshots.getDocumentChanges()) {
            int pos = dc.getNewIndex();
            int oldPos = dc.getOldIndex();
            switch (dc.getType()) {
                case ADDED:
                    items.add(pos, dc.getDocument());
                    notifyItemInserted(pos);
                    break;
                case REMOVED:
                    items.remove(oldPos);
                    notifyItemRemoved(oldPos);
                    break;
                case MODIFIED:
                    items.remove(oldPos);
                    items.add(pos, dc.getDocument());
                    notifyItemRangeChanged(min(pos, oldPos), abs(pos - oldPos) + 1);
                    break;
                default:
                    Log.w(TAG, "Tipo de cambio desconocido", e);
            }
        }
    }
}
