package br.ufpi.newsufpi.view.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import br.ufpi.newsufpi.R;
import br.ufpi.newsufpi.controller.EventoController;
import br.ufpi.newsufpi.model.Evento;
import br.ufpi.newsufpi.util.RequestData;
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
    private boolean loading = false;
    ProgressBar progressBar;
    private int loaded = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_eventos, container, false);
        progressBar = (ProgressBar) view.findViewById(R.id.progress);
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
        progressBar.setVisibility(View.VISIBLE);
        OnRefreshListener().onRefresh();

        return view;
    }

    /**
     * Espera o toque para poder abrir o detalhamento da noticia.
     *
     * @return
     */
    private EventoAdapter.EventoOnClickListener onClickEvento() {
        return new EventoAdapter.EventoOnClickListener() {
            @Override
            public void onClickEvento(View view, int idx, int op) {
                Evento e = eventos.get(idx);
                Intent intent;
                switch (op) {
                    case 0: //Seleciona um evento e abre na janela principal
                        intent = new Intent(getContext(), EventoActivity.class);
                        intent.putExtra("evento", e);
                        startActivity(intent);
                        break;
                    case 1: //Compartilha a URL do Evento
                        intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_TEXT, e.getUrl());
                        startActivity(Intent.createChooser(intent, "Compartilhar"));
                        break;/*
                    case 2:
                        e.setFavorito((e.getFavorito() == 1 ? 0 : 1));
                        if (eventoControler.favoriteEvent(e)) {
                            ImageView favoritar = (ImageView) view.findViewById(R.id.favorite);
                            favoritar.setImageResource(e.getFavorito() == 1 ? R.drawable.ic_star_black_24dp : R.drawable.ic_star_border_black_24dp);
                        }*/
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
                if (AndroidUtils.isNetworkAvailable(getContext())) {
                    if (!loading)
                        executeTask(Evento.EVENTO_URL + "?start=" + loaded);
//                    (new ServerConnection(getContext(), EventosFragment.this)).execute();
                } else {
                    updateView(eventoControler.listAllEvents(), -1);
                    swipeLayout.setRefreshing(false);
                }
            }
        };
    }

    /**
     * Atualiza o adapter com as eventos.
     *
     * @param eventos
     * @param count
     */
    public void updateView(List<Evento> eventos, int count) {
        progressBar.setVisibility(View.GONE);
        if (eventos != null) {
            EventosFragment.this.eventos = eventos;

            recyclerView.setAdapter(new EventoAdapter(getContext(), eventos, onClickEvento()));
            swipeLayout.setRefreshing(false);
            if (count > 0) {
                snackbar = Snackbar.make(view, eventos.size() + " novos eventos foram baixados.", Snackbar.LENGTH_LONG);
            } else if (count == -1) {
                snackbar = Snackbar.make(view, R.string.error_conexao_indisponivel, Snackbar.LENGTH_LONG);
            } else {
                snackbar = Snackbar.make(view, " Não há novos eventos.", Snackbar.LENGTH_LONG);
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
     *
     * @param answer
     */
    @Override
    public void doAfter(String answer) {

        /*Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
        Evento[] fromJson = gson.fromJson(answer, Evento[].class);
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

        } else {
            snackbar = Snackbar.make(view, "Ocorreu algum erro ao buscar os dados.", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
*/
    }

    public Evento docToEvento(Document doc) {
        String title = doc.getElementsByClass("componentheading").first().text();
        String url = doc.baseUri();
        String t = url.substring(url.lastIndexOf("/") + 1);
        t = t.substring(0, t.indexOf("-"));
        int id = Integer.parseInt(t);
        String description;
        //= doc.getElementsByAttributeValueEnding("class", "event_desc").first().text();
        String startDate, endDate;
        startDate = doc.select("[itemprop=startDate]").first().attr("content");
        endDate = doc.select("[itemprop=endDate]").first().attr("content");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Elements elements = doc.select("p");
        StringBuilder stringBuilder = new StringBuilder();
//        Elements aux = new Elements();
        for (Element e : elements) {
            if (e == null) continue;
            if( e.select("img").size() > 0) continue;
            if(e.select("script")== null || e.select("script").size() == 0){
                Element x = e.select("a").first();
                String a = "";
                if(x != null){
                    a = "["+x.absUrl("href")+"]";
                }
                stringBuilder.append("<p>").append(e.html()).append(a).append("</p>");
//                stringBuilder.append(e.html()).append("\n");
            }
        }
        description = stringBuilder.toString();
        try {
            return new Evento(id, title, description, "", "", dateFormat.parse(startDate), dateFormat.parse(endDate), url, 0);
        } catch (ParseException e) {
            return new Evento(id, title, description, "", "", new Date(), new Date(), url, 0);
        }
    }

    private void executeTask(String url) {
        loading = true;
        new AsyncTask<Object, Void, SparseArray>() {

            @Override
            protected SparseArray doInBackground(Object... params) {
                String url = params[0].toString();
                if (url == null) {
                    url = "http://ufpi.br/agenda-ufpi";
                }
                try {
                    Document doc = Jsoup.connect(url).timeout(20000).data("limit", "10").post();
                    Element tableEvent = doc.select("[class=eventtable").first();
                    SparseArray<EventInfo> s = new SparseArray<>();
                    String temp;
                    for (Element e : tableEvent.select("[itemtype=http://schema.org/Event]")) {
                        Log.d("EVENTO", e.toString());
                        EventInfo aux = new EventInfo();
                        Element element = e.select("[headers=jem_title]").first();
                        url = element.child(0).absUrl("href");
                        temp = url.substring(url.lastIndexOf("/") + 1);
                        temp = temp.substring(0, temp.indexOf("-"));
                        int key = Integer.valueOf(temp);
                        aux.url = url;
                        element = e.select("[headers=jem_category]").first();
                        aux.categoria = element.text();
                        StringBuilder stringBuilder = new StringBuilder();
                        element = e.select("[headers=jem_location]").first();
                        if (!element.text().equals("-")) {
                            stringBuilder.append(element.text()).append(" - ");
                        }
                        element = e.select("[headers=jem_city]").first();
                        if (!element.text().equals("-")) {
                            stringBuilder.append(element.text()).append(" - ");
                        }
                        element = e.select("[headers=jem_state]").first();
                        if (!element.text().equals("-")) {
                            stringBuilder.append(element.text());
                        }
                        aux.local = stringBuilder.toString();
                        s.put(key, aux);
                    }
                    return s;
                } catch (IOException e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                            swipeLayout.setRefreshing(false);
                            updateView(eventoControler.listAllEvents(), -1);
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
        new AsyncTask<SparseArray, Void, List<Evento>>() {
            @Override
            protected List<Evento> doInBackground(SparseArray... params) {
                SparseArray<EventInfo> sparseArray = params[0];
                List e = eventoControler.listAllEvents();
                EventInfo info;
                int id;
                ArrayList<Evento> eventos = new ArrayList<>();
                for (int i = 0; i < sparseArray.size(); i++) {
                    id = sparseArray.keyAt(i);
                    info = sparseArray.valueAt(i);
                    try {
                        if (eventoControler.hasEvent(id) == null) {
                            Connection conn = Jsoup.connect(info.url).timeout(20000);
                            Document doc = conn.get();
                            Evento ev = docToEvento(doc);
                            if (ev != null) {
                                ev.setCategoria(info.categoria);
                                ev.setLocal(info.local);
                                eventos.add(ev);
                            }
                            //Log.d("Baixou a noticia", n.getTitle());
                        }
                    } catch (ParseException | IOException ignored) {
                        ignored.printStackTrace();
                    }
                }
                return eventos;
            }

            @Override
            protected void onPostExecute(List<Evento> eventos) {
                if (eventos.size() > 0) {
                    int count = 0;
                    try {
                        count = eventoControler.insertEvents(eventos);
                    } catch (ParseException e) {
                        progressBar.setVisibility(View.GONE);
                        swipeLayout.setRefreshing(false);
                        snackbar = Snackbar.make(view, "Ocorreu algum erro ao salvar os dados.", Snackbar.LENGTH_LONG);
                        snackbar.show();
                        e.printStackTrace();
                    }

                    updateView(eventoControler.listAllEvents(), count);

                } else {
                    updateView(eventoControler.listAllEvents(), 0);
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
        return (new RequestData(Evento.EVENTO_URL, "an-get-event", Evento.class));
    }

    private class EventInfo {
        public String url;
        public String categoria;
        public String local;
    }

}