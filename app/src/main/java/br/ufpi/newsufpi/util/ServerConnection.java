package br.ufpi.newsufpi.util;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * Classe que faz a requisição de dados do servidor.
 *
 * Created by thasciano on 23/12/15.
 */
public class ServerConnection {
    private Transaction transaction;
    private RequestQueue requestQueue;

    public ServerConnection(Context c, Transaction t) {
        transaction = t;
        requestQueue = Volley.newRequestQueue(c);
    }

    public void execute(){
        transaction.doBefore();
        stringRequest(transaction.getRequestData());
    }

    private void stringRequest(final RequestData requestData) {

        StringRequest request = new StringRequest(Request.Method.GET,
                requestData.getUrl(),
                new Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        transaction.doAfter(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        transaction.doAfter(null);
                    }
                });

        request.setTag("conn");
        requestQueue.add(request);
    }

}
