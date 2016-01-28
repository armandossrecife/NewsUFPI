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
import br.ufpi.newsufpi.controller.NoticiaController;
import br.ufpi.newsufpi.model.Noticia;
import br.ufpi.newsufpi.util.RequestData;
import br.ufpi.newsufpi.util.ServerConnection;
import br.ufpi.newsufpi.util.Transaction;
import br.ufpi.newsufpi.view.activity.NoticiaActivity;
import br.ufpi.newsufpi.view.adapter.NoticiaAdapter;
import livroandroid.lib.fragment.BaseFragment;
import livroandroid.lib.utils.AndroidUtils;

/**
 * Created by thasciano on 24/12/15.
 */
public class NoticiasFragment extends BaseFragment implements Transaction {
    protected RecyclerView recyclerView;
    protected LinearLayoutManager mLayoutManager;

    List<Noticia> noticias;
    protected SwipeRefreshLayout swipeLayout;
    Snackbar snackbar;
    protected View view;
    protected NoticiaController noticiaController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_noticias, container, false);

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

        noticiaController = new NoticiaController(getContext());
        OnRefreshListener().onRefresh();

        return view;
    }

    /**
     * Espera o toque para poder abrir o detalhamento da noticia.
     * @return
     */
    private NoticiaAdapter.NoticiaOnClickListener onClickNoticia() {
        return new NoticiaAdapter.NoticiaOnClickListener(){
            @Override
            public void onClickNoticia(View view, int idx) {
                Noticia n = noticias.get(idx);
                Intent intent = new Intent(getContext(), NoticiaActivity.class);
                intent.putExtra("noticia", n);
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
                    (new ServerConnection(getContext(), NoticiasFragment.this)).execute();
                } else {
                    updateView(noticiaController.listAllNotices(), -1);
                    swipeLayout.setRefreshing(false);
                }
            }
        };
    }

    /**
     * Atualiza o adapter com as noticias.
     * @param noticias
     * @param count
     */
    public void updateView(List<Noticia> noticias, int count) {
        if (noticias != null) {
            NoticiasFragment.this.noticias = noticias;

            recyclerView.setAdapter(new NoticiaAdapter(getContext(), noticias, onClickNoticia()));
            swipeLayout.setRefreshing(false);
            if(count > 0) {
                snackbar = Snackbar.make(view, noticias.size() + " novas noticias foram baixadas.", Snackbar.LENGTH_LONG);
            }else if(count == -1) {
                snackbar = Snackbar.make(view, R.string.error_conexao_indisponivel, Snackbar.LENGTH_LONG);
            }else{
                snackbar = Snackbar.make(view, " Não há novas noticias.", Snackbar.LENGTH_LONG);
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
        Noticia[] fromJson =  gson.fromJson(answer, Noticia[].class);
        noticias = Arrays.asList(fromJson);
        if (noticias.size() > 0) {
            int count = 0;
            try {
                count = noticiaController.insertNotices(noticias);
            } catch (ParseException e) {
                snackbar = Snackbar.make(view, "Ocorreu algum erro ao salvar os dados.", Snackbar.LENGTH_LONG);
                snackbar.show();
                e.printStackTrace();
            }

            updateView(noticiaController.listAllNotices(), count);

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
        return (new RequestData(Noticia.NOTICIA_URL, "an-get-notice", Noticia.class) );
    }

}