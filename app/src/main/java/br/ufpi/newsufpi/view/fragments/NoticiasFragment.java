package br.ufpi.newsufpi.view.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import br.ufpi.newsufpi.R;
import br.ufpi.newsufpi.controller.NoticiaController;
import br.ufpi.newsufpi.model.Noticia;
import br.ufpi.newsufpi.util.RequestData;
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
    int loaded = 0;
    int scroll = 0;
    boolean loading = false;

    List<Noticia> noticias;
    protected SwipeRefreshLayout swipeLayout;
    Snackbar snackbar;
    protected View view;
    protected NoticiaController noticiaController;
    ProgressBar progressBar;
    ShareActionProvider provider;
    boolean fav = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fav = getArguments().getBoolean("favorites");
        view = inflater.inflate(R.layout.fragment_noticias, container, false);
        progressBar = (ProgressBar) view.findViewById(R.id.progress);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        provider = new ShareActionProvider(this.getContext());

        // Swipe to Refresh
        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeToRefresh);
        swipeLayout.setOnRefreshListener(OnRefreshListener());
        swipeLayout.setColorSchemeResources(
                R.color.refresh_progress_1,
                R.color.refresh_progress_2,
                R.color.refresh_progress_3);

        noticiaController = new NoticiaController(getContext());
        progressBar.setVisibility(View.VISIBLE);
        OnRefreshListener().onRefresh();
        recyclerView.addOnScrollListener(onScrollListener());
        return view;
    }

    private RecyclerView.OnScrollListener onScrollListener() {
        RecyclerView.OnScrollListener listener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (manager.findLastCompletelyVisibleItemPosition() == recyclerView.getAdapter().getItemCount() - 1) {
                    loaded += 10;
                    scroll = recyclerView.getScrollY();
                    executeTask(Noticia.NOTICIA_URL + "?start=" + loaded);
                }
            }
        };
        return listener;
    }

    /**
     * Espera o toque para poder abrir o detalhamento da noticia.
     *
     * @return
     */
    private NoticiaAdapter.NoticiaOnClickListener onClickNoticia() {
        return new NoticiaAdapter.NoticiaOnClickListener() {
            @Override
            public void onClickNoticia(View view, int idx, int op) {
                Noticia n = noticias.get(idx);
                Intent intent;
                switch (op) {
                    case 0:
                        intent = new Intent(getContext(), NoticiaActivity.class);
                        intent.putExtra("noticia", n);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_TEXT, n.getUrl());
                        startActivity(Intent.createChooser(intent, "Compartilhar"));
                        break;
                    case 2:
                        n.setFavorito((n.getFavorito() == 1 ? 0 : 1));
                        if (noticiaController.favoriteNotice(n)) {
                            ImageView favoritar = (ImageView) view.findViewById(R.id.favorite);
                            favoritar.setImageResource(n.getFavorito() == 1 ? R.drawable.ic_star_black_24dp : R.drawable.ic_star_border_black_24dp);
                        }
                }
            }
        };
    }

    /**
     * Atualiza ao fazer o gesto Swipe To Refresh.
     * Faz a conexão com o servidor.
     *
     * @return
     */
    private SwipeRefreshLayout.OnRefreshListener OnRefreshListener() {
        return new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (AndroidUtils.isNetworkAvailable(getContext()) && !fav) {

                    int buscar = getArguments().getInt("buscar");
                    String query = getArguments().getString("query");
                    if (buscar == 1) {
                        try {
                            noticias = noticiaController.findNotices(query);
                            updateView(noticias, noticias.size());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    } else {
                        if (!loading)
                            executeTask(Noticia.NOTICIA_URL + "?start=" + loaded);
                        if (loaded == 0) loaded = 100;
//                        (new ServerConnection(getContext(), NoticiasFragment.this)).execute();
                    }

                } else {
                    updateView(noticiaController.listAllNotices(), -1);
                    swipeLayout.setRefreshing(false);
                }
            }
        };
    }

    /**
     * Atualiza o adapter com as noticias.
     *
     * @param noticias
     * @param count
     */
    public void updateView(List<Noticia> noticias, int count) {
        progressBar.setVisibility(View.GONE);
        if (noticias != null) {

            if (fav) {
                NoticiasFragment.this.noticias = new ArrayList<>();
                for (Noticia n : noticias) {
                    if (n.getFavorito() == 1) {
                        NoticiasFragment.this.noticias.add(n);
                    }
                }
            } else {
                NoticiasFragment.this.noticias = noticias;
            }
            recyclerView.setAdapter(new NoticiaAdapter(getContext(), NoticiasFragment.this.noticias, onClickNoticia()));
            if (scroll > 0) {
                recyclerView.scrollTo(0, scroll);
                scroll = 0;
            }
            swipeLayout.setRefreshing(false);
            if ((getArguments().getInt("buscar")) == 1) {
                snackbar = Snackbar.make(view, noticias.size() + " noticias foram encontradas.", Snackbar.LENGTH_LONG);
                getArguments().putSerializable("buscar", 0);
            } else {
                if (count > 0) {
                    snackbar = Snackbar.make(view, noticias.size() + " novas noticias foram baixadas.", Snackbar.LENGTH_LONG);
                } else if (count == -1) {
                    snackbar = Snackbar.make(view, R.string.error_conexao_indisponivel, Snackbar.LENGTH_LONG);
                } else {
                    snackbar = Snackbar.make(view, " Não há novas noticias.", Snackbar.LENGTH_LONG);
                }
            }
            if (!fav)
                snackbar.show();
        }
        loaded = recyclerView.getAdapter().getItemCount();
        loading = false;
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
     *
     * @param answer
     */
    @Override
    public void doAfter(String answer) {

        Log.d("doAFTER", answer);

        /*
        Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
        Noticia[] fromJson = gson.fromJson(answer, Noticia[].class);
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

        } else {
            snackbar = Snackbar.make(view, "Ocorreu algum erro ao buscar os dados.", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
        */
    }

    public Noticia docToNoticia(Document doc) {
        Element titleEl = doc.select("[class=documentFirstHeading").first();
        String url = titleEl.children().first().absUrl("href");
        String t = url.substring(url.lastIndexOf("/") + 1);
        t = t.substring(0, t.indexOf("-"));
        int id = Integer.parseInt(t);
        String title = titleEl.text();
        String dateEl = doc.select("[class=documentPublished").first().text();
        String date = dateEl.split(" ")[2].replace("-", "/");
        StringBuilder content = new StringBuilder();
        ArrayList<String> images = new ArrayList<>();
        for (Element e : doc.select("p")) {
            if (e == null) continue;
            Element e1 = (e.children().isEmpty()) ? null : e.children().first();
            if (e1 != null) {

                if (e1.tag().toString().equalsIgnoreCase("img")) {
                    String st = e1.attr("src");
                    try {
                        st = st.substring(st.lastIndexOf('/') + 1);
                        images.add("http://ufpi.br/images/" + URLEncoder.encode(st, "UTF-8"));
                        continue;
                    } catch (UnsupportedEncodingException e2) {
//                    e2.printStackTrace();
                    }
                } else if (e1.tag().toString().equalsIgnoreCase("a")) {
                    content.append(e.text()).append("(").append(e1.absUrl("href")).append(" )").append('\n');
                    continue;
                }
            }
            content.append(e.text()).append('\n');
        }
        try {
            return new Noticia(id, title, content.toString(), new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(date), url, 0, images);
        } catch (ParseException e1) {
            return new Noticia(id, title, content.toString(), new Date(), url, 0, images);
        }
    }

    private void executeTask(String url) {
        loading = true;
        new AsyncTask<Object, Void, SparseArray>() {

            @Override
            protected SparseArray doInBackground(Object... params) {
                String url = params[0].toString();
                if (url == null) {
                    url = "http://ufpi.br/ultimas-noticias-ufpi";
                }
                try {
                    Document doc = Jsoup.connect(url).timeout(20000).data("limit", "10").post();
                    Elements news = doc.select("[class=tileHeadline");
                    SparseArray<String> s = new SparseArray<>();
                    String temp;
                    for (Element e : news) {
                        url = e.children().first().absUrl("href");
                        temp = url.substring(url.lastIndexOf("/") + 1);
                        temp = temp.substring(0, temp.indexOf("-"));
                        int key = Integer.valueOf(temp);
                        s.put(key, url);
                    }
                    return s;
                } catch (IOException e) {
                    if (getActivity() != null)
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                                swipeLayout.setRefreshing(false);
                                updateView(noticiaController.listAllNotices(), -1);
                            }
                        });

                    snackbar = Snackbar.make(view, "Ocorreu algum erro ao buscar os dados.", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    return new SparseArray<>();
                }
            }

            @Override
            protected void onPostExecute(SparseArray list) {
                if (list.size() > 0) {
                    noticiasTask(list);
                }
            }
        }.execute(url);
    }

    private void noticiasTask(SparseArray list) {
        new AsyncTask<SparseArray, Void, List<Noticia>>() {
            @Override
            protected List<Noticia> doInBackground(SparseArray... params) {
                SparseArray<String> sparseArray = params[0];
                String url;
                int id;
                ArrayList<Noticia> noticias = new ArrayList<>();
                for (int i = 0; i < sparseArray.size(); i++) {
                    id = sparseArray.keyAt(i);
                    url = sparseArray.valueAt(i);
                    try {
                        if (!noticiaController.hasNotice(id)) {
                            Connection conn = Jsoup.connect(url).timeout(20000);
                            Document doc = conn.get();
                            Noticia n = docToNoticia(doc);
                            Log.d("Noticia", n.getTitle());
                            if (n != null)
                                noticias.add(n);
                            //Log.d("Baixou a noticia", n.getTitle());
                        }
                    } catch (ParseException | IOException ignored) {
                        ignored.printStackTrace();
                    }
                }
                return noticias;
            }

            @Override
            protected void onPostExecute(List<Noticia> noticias) {
                if (noticias.size() > 0) {
                    int count = 0;
                    try {
                        count = noticiaController.insertNotices(noticias);
                    } catch (ParseException e) {
                        progressBar.setVisibility(View.GONE);
                        swipeLayout.setRefreshing(false);
                        snackbar = Snackbar.make(view, "Ocorreu algum erro ao salvar os dados.", Snackbar.LENGTH_LONG);
                        snackbar.show();
                        e.printStackTrace();
                    }

                    updateView(noticiaController.listAllNotices(), count);

                } else {
                    updateView(noticiaController.listAllNotices(), 0);
                    progressBar.setVisibility(View.GONE);
                    swipeLayout.setRefreshing(false);
                    loaded = Math.max(0, loaded - 10);
                }
            }
        }.execute(list);
    }

    /**
     * Requisita a url para download.
     *
     * @return
     */
    @Override
    public RequestData getRequestData() {
        Log.d("getRequestData", "Requesting");
        return (new RequestData(Noticia.NOTICIA_URL, "an-get-notice", Noticia.class));
    }

}