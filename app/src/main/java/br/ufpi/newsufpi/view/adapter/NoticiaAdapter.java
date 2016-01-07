package br.ufpi.newsufpi.view.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import br.ufpi.newsufpi.R;
import br.ufpi.newsufpi.model.Noticia;

/**
 * Created by thasciano on 24/12/15.
 */
public class NoticiaAdapter extends RecyclerView.Adapter<NoticiaAdapter.NoticiasViewHolder> {
    protected static final String TAG = "livroandroid";
    private final List<Noticia> noticias;
    private final Context context;

    private NoticiaOnClickListener noticiaOnClickListener;

    public NoticiaAdapter(Context context, List<Noticia> noticias, NoticiaOnClickListener noticiaOnClickListener) {
        this.context = context;
        this.noticias = noticias;
        this.noticiaOnClickListener = noticiaOnClickListener;
    }

    @Override
    public int getItemCount() {
        return this.noticias != null ? this.noticias.size() : 0;
    }

    @Override
    public NoticiasViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Infla a view do layout1
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_noticia, viewGroup, false);

        CardView cardView = (CardView) view.findViewById(R.id.card_view);

        // Cria o ViewHolder
        NoticiasViewHolder holder = new NoticiasViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final NoticiasViewHolder holder, final int position) {
        // Atualiza a view
        Noticia n = noticias.get(position);

        holder.tNome.setText(n.getTitle());
        if(n.getImages().size()>0) {
            holder.progress.setVisibility(View.VISIBLE);
            holder.img.setVisibility(View.VISIBLE);


            Picasso.with(context).load(n.getImages().get(0)).fit().into(holder.img, new Callback() {
                @Override
                public void onSuccess() {
                    holder.progress.setVisibility(View.GONE);
                }

                @Override
                public void onError() {
                    holder.progress.setVisibility(View.GONE);
                }
            });
        }

        // Click
        if (noticiaOnClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    noticiaOnClickListener.onClickNoticia(holder.itemView, position); // A variável position é final
                }
            });
        }
    }

    public interface NoticiaOnClickListener {
        public void onClickNoticia(View view, int idx);
    }

    // ViewHolder com as views
    public static class NoticiasViewHolder extends RecyclerView.ViewHolder {
        public TextView tNome;
        ImageView img;
        ProgressBar progress;

        public NoticiasViewHolder(View view) {
            super(view);
            // Cria as views para salvar no ViewHolder
            tNome = (TextView) view.findViewById(R.id.text);
            img = (ImageView) view.findViewById(R.id.img);
            progress = (ProgressBar) view.findViewById(R.id.progressImg);
        }
    }
}
