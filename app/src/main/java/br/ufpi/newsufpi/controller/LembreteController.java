package br.ufpi.newsufpi.controller;

import android.content.Context;
import android.content.Intent;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import br.ufpi.newsufpi.model.Lembrete;
import br.ufpi.newsufpi.persistence.FacadeDao;

/**
 * Created by katia cibele on 12/02/2016.
 */
public class LembreteController {
    private FacadeDao facadeDao;


    public LembreteController(Context context) {
        this.facadeDao = FacadeDao.getInstance(context);
    }

    public void insertLembrete(List<Lembrete> lembretes) throws ParseException {
        facadeDao.insertLembrete(lembretes);
    }

    public List<Lembrete> listAllLembrete() throws ParseException {
        return facadeDao.listAllLembrete();
    }

    public void deleteLembrete(Integer lembreteId){
        facadeDao.deleteLembrete(lembreteId);
    }

    public void deleteAllLembretes(){
        facadeDao.deleteAllLembretes();
    }


}
