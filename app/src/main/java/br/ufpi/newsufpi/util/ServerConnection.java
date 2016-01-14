package br.ufpi.newsufpi.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.net.NoRouteToHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import br.ufpi.newsufpi.controller.EventoController;
import br.ufpi.newsufpi.controller.NoticiaController;
import br.ufpi.newsufpi.model.Evento;
import br.ufpi.newsufpi.model.Noticia;

import static com.android.volley.Request.Method.GET;

/**
 * Created by thasciano on 23/12/15.
 */
public class ServerConnection {

    private static List<Noticia> listaDeNoticias;
    private List<Evento> listaDeEventos;
    private static final String TAG = "ServerConnection";
    private static Context context;
    private static RequestQueue mRequestQueue;


    public List<Noticia> getListaDeNoticias() {
        try {
            return downloadNoticias();
        } catch (Exception e) {
            // TODO explicar exception
            Log.e(TAG, "Erro ao ler as noticias: " + e.getMessage(), e);
            return null;
        }
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public ServerConnection(Context context) {
        this.context = context;
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context);
        }
    }



    public List<Noticia> downloadNoticias() throws Exception {
        String url = "http://www.json-generator.com/api/json/get/bUCqJnfXJu?indent=2";
        listaDeNoticias = new ArrayList<Noticia>();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {
                            int resultCount = response.optInt("resultCount");
                            if (resultCount > 0) {
                                //Gson gson = new Gson();
                                // formatando a data
                                GsonBuilder builder = new GsonBuilder();
                                builder.setDateFormat("dd/MM/yyyy");

                                // Criando uma lista de noticias a partir do json
                                Gson gson = builder.create();
                                JSONArray jsonArray = response.optJSONArray("results");
                                if (jsonArray != null) {
                                    Noticia[] noticias = gson.fromJson(jsonArray.toString(), Noticia[].class);
                                    if (noticias != null && noticias.length > 0) {
                                        for (Noticia not : noticias) {
                                            listaDeNoticias.add(not);
                                            Log.i("LOG", not.getTitle());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("LOG", error.toString());
                    }
                });
        addToRequestQueue(jsonObjectRequest);

        return listaDeNoticias;

    }

}
