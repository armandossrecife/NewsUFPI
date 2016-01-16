package br.ufpi.newsufpi.util;

import java.util.List;

import br.ufpi.newsufpi.model.Noticia;

/**
 * Created by thasciano on 27/01/16.
 */
public interface ServerCallbackNoticia {
    void onSuccess(List<Noticia> result);
}
