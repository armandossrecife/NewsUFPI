package br.ufpi.newsufpi.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.ufpi.newsufpi.model.Evento;
import br.ufpi.newsufpi.model.Noticia;

/**
 * Created by thasciano on 23/12/15.
 */
public class ServerConnection {

    private static final String TAG = ServerConnection.class.getSimpleName();
    private static Context context;
    private static RequestQueue mRequestQueue;
    private static ServerConnection mInstance;
    private ImageLoader mImageLoader;

    private static List<Noticia> listaDeNoticias;
    private List<Evento> listaDeEventos;

    public ServerConnection(Context context) {
        this.context = context;
    }

    public static synchronized ServerConnection getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new ServerConnection(context);
        }
        return mInstance;
    }

    public List<Noticia> getListaDeNoticias() {
        try {
            return jsonObjectRequest();
        } catch (Exception e) {
            // TODO explicar exception
            Log.e(TAG, "Erro ao ler as noticias: " + e.getMessage(), e);
            return null;
        }
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context);
        }
        return mRequestQueue;
    }

    /*public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }*/

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

    public List<Noticia> jsonObjectRequest() throws Exception {
        String url = "http://www.json-generator.com/api/json/get/bOfpGSCtOW?indent=2";
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
