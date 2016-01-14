package br.ufpi.newsufpi.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import br.ufpi.newsufpi.R;
import br.ufpi.newsufpi.controller.NoticiaController;
import br.ufpi.newsufpi.model.Noticia;
import br.ufpi.newsufpi.view.activity.NoticiaActivity;
import br.ufpi.newsufpi.view.adapter.NoticiaAdapter;
import livroandroid.lib.fragment.BaseFragment;
import livroandroid.lib.utils.AndroidUtils;

/**
 * Created by thasciano on 24/12/15.
 */
public class NoticiasFragment extends BaseFragment {
    protected RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;

    List<Noticia> noticias;
    private String tipo;
    private SwipeRefreshLayout swipeLayout;
    Snackbar snackbar;
    private View view;
    private NoticiaController noticiaController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.tipo = getArguments().getString("tipo");
        }
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

        return view;
    }


    /**
     * Atualiza ao fazer o gesto Swipe To Refresh
     * @return
     */
    private SwipeRefreshLayout.OnRefreshListener OnRefreshListener() {
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (AndroidUtils.isNetworkAvailable(getContext())) {
                    taskCarros(true);
                } else {
                    snackbar = Snackbar.make(view, R.string.error_conexao_indisponivel, Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        };
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Por padrão busca os noticias do banco de dados.
        taskCarros(false);
    }

    private void taskCarros(boolean pullToRefresh) {
        startTask("noticias", new GetNoticiasTask(pullToRefresh), pullToRefresh ? R.id.swipeToRefresh : R.id.progress);
    }


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
     * Classe para gerenciar o refresh de noticias.
     * @author thasciano
     */
    private class GetNoticiasTask implements TaskListener<List<Noticia>> {
        private boolean refresh;

        public GetNoticiasTask(boolean refresh) {
            this.refresh = refresh;
        }

        /**
         * Busca os noticias em background (Thread)
         * @return
         * @throws Exception
         */
        @Override
        public List<Noticia> execute() throws Exception {
            Thread.sleep(500);
            noticiaController = new NoticiaController(getActivity());
            return noticiaController.listAllNotices(getActivity());
        }

        @Override
        public void updateView(List<Noticia> noticias) {
            if (noticias != null) {
                NoticiasFragment.this.noticias = noticias;
                // Atualiza a view na UI Thread
                recyclerView.setAdapter(new NoticiaAdapter(getContext(), noticias, onClickNoticia()));

                snackbar = Snackbar.make(view, "update " + noticias.size() + " noticias.", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }

        @Override
        public void onError(Exception e) {
            snackbar = Snackbar.make(view, "Ocorreu algum erro ao buscar os dados.", Snackbar.LENGTH_LONG);
            snackbar.show();
        }

        @Override
        public void onCancelled(String s) {
        }
    }
}