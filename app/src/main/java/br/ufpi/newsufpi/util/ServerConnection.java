package br.ufpi.newsufpi.util;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.ufpi.newsufpi.controller.NoticiaController;
import br.ufpi.newsufpi.model.Evento;
import br.ufpi.newsufpi.model.Noticia;

/**
 * Created by thasciano on 23/12/15.
 */
public class ServerConnection {
    private static NoticiaController noticiaController;
    String url = "http://www.json-generator.com/api/json/get/bQxZigdczm?indent=2";
    private static final String TAG = ServerConnection.class.getSimpleName();
    private static Context context;
    private static RequestQueue mRequestQueue;
    private static ServerConnection mInstance;
    private ImageLoader mImageLoader;
    Noticia[] noticias;
    private static List<Noticia> listaDeNoticias;
    private List<Evento> listaDeEventos;

    public ServerConnection(Context context) {
        this.context = context;
    }

    public static synchronized ServerConnection getInstance(Context context) {
        noticiaController = new NoticiaController(context);
        if (mInstance == null) {
            mInstance = new ServerConnection(context);
        }
        return mInstance;
    }

    public void getListaDeNoticias(ServerCallbackNoticia callbackNoticia) {
        try {
//            callbackNoticia.onSuccess(downloadNoticias());
            stringRequestNoticias(callbackNoticia);
//            jsonObjectRequest(callbackNoticia);
        } catch (Exception e) {
            // TODO explicar exception
            Log.e(TAG, "Erro ao ler as noticias: " + e.getMessage(), e);
        }
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context);
        }
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

    public void jsonObjectRequest(final ServerCallbackNoticia serverCallbackNoticia) throws Exception {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, new Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {
                            Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();

                            JSONArray jsonArray = response.optJSONArray("results");
                            if (jsonArray != null) {
                                Noticia[] noticias = gson.fromJson(jsonArray.toString(), Noticia[].class);
                                if (noticias != null && noticias.length > 0) {
                                    serverCallbackNoticia.onSuccess(Arrays.asList(noticias));
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
    }

    public void downloadImagens(final List<Noticia> listaDeNoticias) throws Exception {


        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    // diretorio onde vamos salvar
                    File root = Environment.getExternalStorageDirectory();
                    File dir = new File(root.getAbsolutePath()
                            + "/newsUfpi/img");
                    // cria a pasta se ela não existir
                    dir.mkdirs();

                    // Realizando o download das imagens das noticias e salvando
                    // no cartao
                    // de memoria
                    for (Noticia noticia : listaDeNoticias) {

                        List<String> path = new ArrayList<String>();
                        for (int i = 0; i < noticia.getImages().size(); i++) {

                            // url da imagem
                            URL url = new URL(noticia.getImages().get(i));
                            // abrindo conexão com a url
                            HttpURLConnection c = (HttpURLConnection) url
                                    .openConnection();
                            c.setRequestMethod("GET");
                            c.setDoOutput(true);

                            // recebendo o arquivo referente a url
                            InputStream in = c.getInputStream();
                            //FileOutputStream fileOutputStream = new FileOutputStream(new File(dir, "img_ntc_" + noticia.getId() + i + ".jpg"));
                            FileOutputStream fileOutputStream = new FileOutputStream(new File(dir, "img_ntc_" + noticia.getId()+ "_" + i + ".png"));

                            // salvando a imagem no cartao de memoria
                            byte[] buffer = new byte[1024];
                            int len1 = 0;
                            while ((len1 = in.read(buffer)) > 0) {
                                fileOutputStream.write(buffer, 0, len1);
                            }
                            fileOutputStream.close();
                            // alterando o path da imagem da noticia que será salvo na tabela noticias da aplicação
                            path.add(dir.getAbsolutePath() + "/img_ntc_" + noticia.getId() + "_" + i + ".png");
                        }
                        noticia.setImages(path);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }


    private void stringRequestNoticias(final ServerCallbackNoticia serverCallbackNoticia) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
                        noticias =  gson.fromJson(response, Noticia[].class);
                        if (noticias != null && noticias.length > 0) {
                            serverCallbackNoticia.onSuccess(Arrays.asList(noticias));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(MainActivity.this, "Verifique sua conexão!", Toast.LENGTH_LONG).show();
                    }
                });

        addToRequestQueue(stringRequest);
    }

}
