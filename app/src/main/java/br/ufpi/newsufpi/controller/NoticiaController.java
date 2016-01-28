package br.ufpi.newsufpi.controller;

import android.content.Context;

import java.text.ParseException;
import java.util.List;

import br.ufpi.newsufpi.model.Noticia;
import br.ufpi.newsufpi.persistence.FacadeDao;

/**
 * Created by thasciano on 23/12/15.
 */
public class NoticiaController {
    private FacadeDao facadeDao;

    public NoticiaController(Context context) {
        this.facadeDao = FacadeDao.getInstance(context);
    }

    public List<Noticia> listAllNotices(){
        return facadeDao.listAllNotices();
    }

    /**
     * Método que busca as noticias por palavra encontrada no título ou no conteudo.
     * @param text O texto que está buscando
     * @return Uma lista de notícias que contém o texto buscado.
     * @throws ParseException
     */
    public List<Noticia> findNotices(String text) throws ParseException{
        return facadeDao.findNotices(text);
    }

    /**
     * Método que insere as notícias no banco
     * @param notices - A lista de notícias que deseja inserir
     * @throws ParseException
     */
    public int insertNotices(List<Noticia> notices) throws ParseException{
        return facadeDao.insertNotices(notices);
    }

}