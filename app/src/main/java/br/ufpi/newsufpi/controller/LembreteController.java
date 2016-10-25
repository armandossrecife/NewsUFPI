package br.ufpi.newsufpi.controller;

import android.content.Context;

import java.text.ParseException;
import java.util.List;

import br.ufpi.newsufpi.model.Lembrete;
import br.ufpi.newsufpi.persistence.FacadeDao;

/**
 *Classe responsável pela regra de negócio do lembrete.
 *
 * Created by katia cibele on 12/02/2016.
 */
@SuppressWarnings("ALL")
public class LembreteController {
    private FacadeDao facadeDao;

    /**
     * Contrutor.
     *
     * @param context
     */
    public LembreteController(Context context) {
        this.facadeDao = FacadeDao.getInstance(context);
    }

    /**
     * Insere uma lista de lembretes.
     *
     * @param lembretes
     * @throws ParseException
     */
    public void insertLembrete(List<Lembrete> lembretes) throws ParseException {
        facadeDao.insertLembrete(lembretes);
    }

    /**
     * Lista todos os lembretes cadastrados.
     *
     * @return lista de lembretes cadastrados no banco
     * @throws ParseException
     */
    public List<Lembrete> listAllLembrete() throws ParseException {
        return facadeDao.listAllLembrete();
    }

    /**
     * Deleta um lembrete com base no id.
     * @param lembreteId
     */
    public void deleteLembrete(Integer lembreteId){
        facadeDao.deleteLembrete(lembreteId);
    }

    /**
     * Deleta todos os lembretes.
     */
    public void deleteAllLembretes(){
        facadeDao.deleteAllLembretes();
    }


}
