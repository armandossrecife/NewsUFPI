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

import com.android.volley.toolbox.ImageLoader;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import br.ufpi.newsufpi.R;
import br.ufpi.newsufpi.model.Noticia;

/**
 * Created by thasciano on 24/12/15.
 */
public class NoticiaAdapter extends RecyclerView.Adapter<NoticiaAdapter.NoticiasViewHolder> {
    protected static final String TAG = "newsufpi";
    private final List<Noticia> noticias;
    private final Context context;
    private ImageLoader imageLoader;
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
    public void onBindViewHolder(final NoticiasViewHolder holder, int position) {
        // Atualiza a view
        //
        holder.adapterPosition = holder.getAdapterPosition();
        Noticia n = noticias.get(position);

        holder.tNome.setText(n.getTitle());
        holder.tdate.setText((CharSequence) n.getDateString());

        if (n.getImages().size() > 0) {
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

        } else {
            holder.img.setVisibility(View.GONE);
            holder.progress.setVisibility(View.GONE
            );
        }

        // Click
        if (noticiaOnClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    noticiaOnClickListener.onClickNoticia(holder.itemView, holder.adapterPosition, 0); // A variável position é final
                }
            });
            holder.share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    noticiaOnClickListener.onClickNoticia(holder.itemView, holder.adapterPosition, 1);
                }
            });
            holder.favoritar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    noticiaOnClickListener.onClickNoticia(holder.itemView, holder.adapterPosition, 2);
                }
            });
        }
        holder.favoritar.setImageResource(n.getFavorito()==1?R.drawable.ic_star_black_24dp:R.drawable.ic_star_border_black_24dp);
    }

    public interface NoticiaOnClickListener {
        public void onClickNoticia(View view, int idx, int op);
    }

    // ViewHolder com as views
    public static class NoticiasViewHolder extends RecyclerView.ViewHolder {
        int adapterPosition;
        public TextView tNome;
        public TextView tdate;
        ImageView img;
        ProgressBar progress;
        ImageView share, favoritar;

        public NoticiasViewHolder(View view) {
            super(view);
            // Cria as views para salvar no ViewHolder
            tNome = (TextView) view.findViewById(R.id.text);
            tdate = (TextView) view.findViewById(R.id.date);
            img = (ImageView) view.findViewById(R.id.img);
            progress = (ProgressBar) view.findViewById(R.id.progressImg);
            share = (ImageView) view.findViewById(R.id.share);
            favoritar = (ImageView) view.findViewById(R.id.favorite);
        }
    }
}
