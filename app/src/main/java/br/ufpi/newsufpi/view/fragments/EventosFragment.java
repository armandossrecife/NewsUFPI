package br.ufpi.newsufpi.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

import br.ufpi.newsufpi.R;
import br.ufpi.newsufpi.controller.EventoController;
import br.ufpi.newsufpi.model.Evento;
import br.ufpi.newsufpi.util.RequestData;
import br.ufpi.newsufpi.util.ServerConnection;
import br.ufpi.newsufpi.util.Transaction;
import br.ufpi.newsufpi.view.activity.EventoActivity;
import br.ufpi.newsufpi.view.adapter.EventoAdapter;
import livroandroid.lib.utils.AndroidUtils;

/**
 * Created by franciscowender on 29/01/2016.
 */
public class EventosFragment extends livroandroid.lib.fragment.BaseFragment implements Transaction {
    protected RecyclerView recyclerView;
    protected LinearLayoutManager mLayoutManager;

    List<Evento> eventos;
    protected SwipeRefreshLayout swipeLayout;
    Snackbar snackbar;
    protected View view;
    protected EventoController eventoControler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_eventos, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        // Swipe to Refresh
        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeToRefresh);
        swipeLayout.setOnRefreshListener(OnRefreshListener());
        swipeLayout.setColorSchemeResources(
                R.color.refresh_progress_1,
                R.color.refresh_progress_2,
                R.color.refresh_progress_3);

        eventoControler = new EventoController(getContext());
        OnRefreshListener().onRefresh();

        return view;
    }

    /**
     * Espera o toque para poder abrir o detalhamento da noticia.
     * @return
     */
    private EventoAdapter.EventoOnClickListener onClickEvento() {
        return new EventoAdapter.EventoOnClickListener(){
            @Override
            public void onClickEvento(View view, int idx) {
                Evento e = eventos.get(idx);
                Intent intent = new Intent(getContext(), EventoActivity.class);
                intent.putExtra("evento", e);
                startActivity(intent);
            }
        };
    }

    /**
     * Atualiza ao fazer o gesto Swipe To Refresh.
     * Faz a conexão com o servidor.
     * @return
     */
    private SwipeRefreshLayout.OnRefreshListener OnRefreshListener() {
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (AndroidUtils.isNetworkAvailable(getContext())) {
                    (new ServerConnection(getContext(), EventosFragment.this)).execute();
                } else {
                    updateView(eventoControler.listAllEvents(), -1);
                    swipeLayout.setRefreshing(false);
                }
            }
        };
    }

    /**
     * Atualiza o adapter com as eventos.
     * @param eventos
     * @param count
     */
    public void updateView(List<Evento> eventos, int count) {
        if (eventos != null) {
            EventosFragment.this.eventos = eventos;

            recyclerView.setAdapter(new EventoAdapter(getContext(), eventos, onClickEvento()));
            swipeLayout.setRefreshing(false);
            if(count > 0) {
                snackbar = Snackbar.make(view, eventos.size() + " novas eventos foram baixadas.", Snackbar.LENGTH_LONG);
            }else if(count == -1) {
                snackbar = Snackbar.make(view, R.string.error_conexao_indisponivel, Snackbar.LENGTH_LONG);
            }else{
                snackbar = Snackbar.make(view, " Não há novas eventos.", Snackbar.LENGTH_LONG);
            }
            snackbar.show();

        }
    }

    /**
     * Ativa a animação do swipe
     */
    @Override
    public void doBefore() {
        swipeLayout.setRefreshing(true);
    }

    /**
     * Recebe a resposta do servidor e trata a resposta para ser salva no banco.
     * @param answer
     */
    @Override
    public void doAfter(String answer) {

        Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
        Evento[] fromJson =  gson.fromJson(answer, Evento[].class);
        eventos = Arrays.asList(fromJson);
        if (eventos.size() > 0) {
            int count = 0;
            try {
                count = eventoControler.insertEvents(eventos);
            } catch (ParseException e) {
                snackbar = Snackbar.make(view, "Ocorreu algum erro ao salvar os dados.", Snackbar.LENGTH_LONG);
                snackbar.show();
                e.printStackTrace();
            }

            updateView(eventoControler.listAllEvents(), count);

        }else{
            snackbar = Snackbar.make(view, "Ocorreu algum erro ao buscar os dados.", Snackbar.LENGTH_LONG);
            snackbar.show();
        }

    }

    /**
     * Requisita a url para download.
     * @return
     */
    @Override
    public RequestData getRequestData() {
        return (new RequestData(Evento.EVENTO_URL, "an-get-event", Evento.class) );
    }

}