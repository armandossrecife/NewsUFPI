package br.ufpi.newsufpi.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import br.ufpi.newsufpi.R;
import br.ufpi.newsufpi.model.Noticia;
import livroandroid.lib.fragment.BaseFragment;

/**
 * Created by thasciano on 24/12/15.
 */
public class NoticiaFragment extends BaseFragment {
    protected Noticia noticia;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_noticia, container, false);

        return view;
    }

    // Método público chamado pela activity para atualizar os dados do carro
    public void setNoticia(Noticia noticia) {

        if(noticia != null) {
            this.noticia = noticia;
            //setTextString(R.id.tNome,carro.nome);
            setTextString(R.id.title_n, noticia.getTitle());
            setTextString(R.id.date_n, noticia.getDateString());
            setTextString(R.id.desc_n, noticia.getContent());
            if(noticia.getImages().size()>0) {
                final ImageView imgView = (ImageView) getView().findViewById(R.id.img_noticia);
                imgView.setVisibility(ImageView.VISIBLE);
                Picasso.with(getContext()).load(noticia.getImages().get(0)).fit().into(imgView);
            }
        }
    }
}