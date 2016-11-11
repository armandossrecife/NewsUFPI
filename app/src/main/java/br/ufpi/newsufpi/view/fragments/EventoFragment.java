package br.ufpi.newsufpi.view.fragments;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.ufpi.newsufpi.R;
import br.ufpi.newsufpi.model.Evento;

/**
 * Created by franciscowender on 29/01/2016.
 */
public class EventoFragment extends livroandroid.lib.fragment.BaseFragment {
    protected Evento noticia;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_evento, container, false);
        TextView textView = (TextView) view.findViewById(R.id.desc_e);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        return view;
    }

    // Método público chamado pela activity para atualizar os dados do carro
    public void setEvento(Evento evento) {

        if(evento != null) {
            this.noticia = noticia;
            setTextString(R.id.title_e, evento.getTitle());
            setTextString(R.id.date_intervalo, evento.getDateBeginString() + " - " + evento.getDateEndString());
            ((TextView)getActivity().findViewById(R.id.desc_e)).setText(Html.fromHtml(evento.getContent()));
        }
    }
}