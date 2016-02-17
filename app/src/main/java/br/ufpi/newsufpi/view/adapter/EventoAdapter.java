package br.ufpi.newsufpi.view.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.ufpi.newsufpi.R;
import br.ufpi.newsufpi.model.Evento;

/**
 * Created by franciscowender on 29/01/2016.
 */
public class EventoAdapter extends RecyclerView.Adapter<EventoAdapter.EventosViewHolder> {
    protected static final String TAG = "newsufpi";
    private final List<Evento> eventos;
    private final Context context;
    private EventoOnClickListener eventoOnClickListner;

    public EventoAdapter(Context context, List<Evento> eventos, EventoOnClickListener eventoOnClickListener) {
        this.context = context;
        this.eventos = eventos;
        this.eventoOnClickListner = eventoOnClickListener;

    }

    @Override
    public int getItemCount() {
        return this.eventos != null ? this.eventos.size() : 0;
    }

    @Override
    public EventoAdapter.EventosViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Infla a view do layout1
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_noticia, viewGroup, false);

        CardView cardView = (CardView) view.findViewById(R.id.card_view);

        // Cria o ViewHolder
        EventosViewHolder holder = new EventosViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final EventosViewHolder holder, final int position) {
        // Atualiza a view
        Evento e = eventos.get(position);
        holder.tNome.setText(e.getTitle());
//        holder.tdateIntervalo.setText(e.getDataInicioString()+ " - " + e.getDataFimString());

        // Click
        if (eventoOnClickListner != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    eventoOnClickListner.onClickEvento(holder.itemView, position); // A variável position é final
                }
            });
        }
    }

    public interface EventoOnClickListener {
        public void onClickEvento(View view, int idx);
    }

    // ViewHolder com as views
    public static class EventosViewHolder extends RecyclerView.ViewHolder {
        public TextView tNome;
        public TextView tdateIntervalo;

        public EventosViewHolder(View view) {
            super(view);
            // Cria as views para salvar no ViewHolder
            tNome = (TextView) view.findViewById(R.id.text);
            tdateIntervalo = (TextView) view.findViewById(R.id.intervalo);
        }
    }
}
