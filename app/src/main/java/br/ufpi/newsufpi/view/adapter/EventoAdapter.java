package br.ufpi.newsufpi.view.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_evento, viewGroup, false);

        CardView cardView = (CardView) view.findViewById(R.id.card_view);

        // Cria o ViewHolder
        EventosViewHolder holder = new EventosViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final EventosViewHolder holder, int position) {
        // Atualiza a view

        position = holder.getAdapterPosition();
        Evento e = eventos.get(position);
        holder.tNome.setText(e.getTitle());
        String str = e.getDataInicioString() + " - " + e.getDataFimString();
        holder.tdateIntervalo.setText(str);

        // Click
        if (eventoOnClickListner != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    eventoOnClickListner.onClickEvento(holder.itemView, holder.getAdapterPosition(), 0);
                }
            });
            holder.share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    eventoOnClickListner.onClickEvento(holder.itemView, holder.getAdapterPosition(), 1);
                }
            });
        }

    }

    public interface EventoOnClickListener {
        void onClickEvento(View view, int idx, int op);
    }

    // ViewHolder com as views
    public static class EventosViewHolder extends RecyclerView.ViewHolder {
        public TextView tNome;
        public TextView tdateIntervalo;
        ImageView share;

        public EventosViewHolder(View view) {
            super(view);
            // Cria as views para salvar no ViewHolder
            tNome = (TextView) view.findViewById(R.id.text);
            tdateIntervalo = (TextView) view.findViewById(R.id.intervalo);
            share = (ImageView) view.findViewById(R.id.share);

        }
    }
}
