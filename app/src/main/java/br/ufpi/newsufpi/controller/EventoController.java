package br.ufpi.newsufpi.controller;

import android.content.Context;

import java.text.ParseException;
import java.util.List;

import br.ufpi.newsufpi.model.Evento;
import br.ufpi.newsufpi.persistence.FacadeDao;

/**
 * Classe responsável pela regra de negócio de evento.
 *
 * Created by thasciano on 23/12/15.
 */
@SuppressWarnings("ALL")
public class EventoController {
    private FacadeDao facadeDao;

    /**
     * Construtor
     *
     * @param context
     *
     */
    public EventoController(Context context) {
        this.facadeDao = FacadeDao.getInstance(context);
    }

    /**
     * Insere uma lista de eventos.
     *
     * @param eventos
     * @return quantidade de eventos salvos com sucesso.
     * @throws ParseException
     */
    public int insertEvents(List<Evento> eventos) throws ParseException{
        return facadeDao.insertEvents(eventos);
    }

    /**
     * Lista todos os enventos cadastrados.
     *
     * @return lista de eventos cadastrados no banco
     */
    public List<Evento> listAllEvents() {
        return facadeDao.listAllEvents();
    }

    /**
     * Busca um evento que tem em alguma parte do titulo ou do texto a string informada.
     *
     * @param text
     * @return a lista de eventos
     * @throws ParseException
     */
    public List<Evento> findEvent(String text) throws ParseException{
        return facadeDao.findEvent(text);
    }

    /**
     * Deleta um evento.
     *
     * @param eventId
     */
    public void deleteEvent(Integer eventId){
        facadeDao.deleteEvent(eventId);
    }

    /**
     * Deleta todos os eventos.
     */
    public void deleteAllEvents(){
        facadeDao.deleteAllEvents();
    }

    public boolean favoriteEvent(Evento e) {
        return facadeDao.favoriteEvent(e);
    }

    public Evento hasEvent(int id) throws ParseException {
        return facadeDao.hasEvent(id);
    }
}
