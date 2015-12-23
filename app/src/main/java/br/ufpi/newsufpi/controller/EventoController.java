package br.ufpi.newsufpi.controller;

import android.content.Context;

import java.text.ParseException;
import java.util.List;

import br.ufpi.newsufpi.model.Evento;
import br.ufpi.newsufpi.persistence.FacadeDao;

/**
 * Created by thasciano on 23/12/15.
 */
public class EventoController {
    private FacadeDao facadeDao;

    public EventoController(Context context) {
        this.facadeDao = FacadeDao.getInstance(context);
    }

    public void insertEvents(List<Evento> eventos) throws ParseException{
        facadeDao.insertEvents(eventos);
    }

    public List<Evento> listAllEvents() throws ParseException {
        return facadeDao.listAllEvents();
    }

    public List<Evento> findEvent(String text) throws ParseException{
        return facadeDao.findEvent(text);
    }

    public void deleteEvent(Integer eventId){
        facadeDao.deleteEvent(eventId);
    }

    public void deleteAllEvents(){
        facadeDao.deleteAllEvents();
    }
}
