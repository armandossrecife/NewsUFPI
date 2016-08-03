package br.ufpi.newsufpi.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.ufpi.newsufpi.model.Evento;

/**
 * Classe responsavel por fazer as operações no banco de dados de Eventos.
 * <p>
 * Created by thasciano on 23/12/15.
 */
public class EventoDao extends FacadeDao {

    private static EventoDao eventoDao;

    public static EventoDao getInstance(Context context) {
        if (eventoDao == null) {
            eventoDao = new EventoDao(context);
        }
        return eventoDao;
    }

    /**
     * Contrutor.
     *
     * @param context
     */
    public EventoDao(Context context) {
        super(context);
    }

    public int insertEvents(List<Evento> events) throws ParseException {
        int count = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        for (Evento event : events) {
            if ((event.getId() != null) && (hasEvent(event.getId()) == null)) {
                ContentValues values = new ContentValues();
                values.put(COL_DATAINICIO_EVENT, event.getDataInicio().getTime());
                values.put(COL_ID_EVENT, event.getId());
                values.put(COL_LOCAL_EVENT, event.getLocal());
                values.put(COL_TITLE_EVENT, event.getTitle());
                values.put(COL_CONTENT_EVENT, event.getContent());

                values.put(COL_CATEGORIA_EVENT, event.getCategoria());
                values.put(COL_DATAFIM_EVENT, event.getDataFim().getTime());
                values.put(URL, event.getUrl());
                db.insert(TABLE_NAME_EVENT, null, values);
                Log.i("Save", event.getTitle());
                count++;
            }
        }
        return count;
    }

    /**
     * @return retorna todos eventos
     * @throws ParseException
     */
    public List<Evento> listAllEvents() {
        List<Evento> events = new ArrayList<Evento>();
        String[] aux = {COL_ID_EVENT, COL_TITLE_EVENT, COL_CONTENT_EVENT,
                COL_LOCAL_EVENT, COL_CATEGORIA_EVENT, COL_DATAINICIO_EVENT, COL_DATAFIM_EVENT, URL, FAVORITE};
        Cursor cursor = getWritableDatabase().query(TABLE_NAME_EVENT, aux,
                null, null, null, null, COL_DATAINICIO_EVENT + " DESC");
        while (cursor.moveToNext()) {

            Evento event = new Evento(cursor.getInt(0), cursor.getString(1),
                    cursor.getString(2), cursor.getString(3), cursor.getString(4),
                    new Date(cursor.getLong(5)), new Date(cursor.getLong(6)), cursor.getString(7), cursor.getInt(8)
            );
            events.add(event);
        }
        cursor.close();
        return events;
    }

    /**
     * @param eventTitle
     * @return retorna os eventos, onde será buscado no titulo e na descrição do
     * evento
     * @throws ParseException
     */
    public List<Evento> findEvent(String eventTitle) throws ParseException {
        Evento event;
        List<Evento> events = new ArrayList<Evento>();
        Cursor cursor = getWritableDatabase().rawQuery(
                "SELECT * FROM " + TABLE_NAME_EVENT + " WHERE "
                        + COL_TITLE_EVENT + " LIKE '%" + eventTitle + "%' OR "
                        + COL_CONTENT_EVENT + " LIKE '%" + eventTitle
                        + "%' order by " + COL_DATAINICIO_EVENT + " DESC ;", null);
        SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy");
        while (cursor.moveToNext()) {
            event = new Evento(cursor.getInt(0), cursor.getString(1),
                    cursor.getString(2), cursor.getString(3), cursor.getString(4),
                    formater.parse(cursor.getString(5)), formater.parse(cursor.getString(6)), cursor.getString(7), cursor.getInt(8));
            events.add(event);
        }
        cursor.close();
        return events;
    }

    public List<Evento> findEventId(int eventId) throws ParseException {
        Evento event;
        List<Evento> events = new ArrayList<Evento>();
        Cursor cursor = getWritableDatabase().rawQuery(
                "SELECT * FROM " + TABLE_NAME_EVENT + " WHERE "
                        + COL_ID_EVENT + "= '%" + eventId + "%' order by " + COL_DATAINICIO_EVENT + " DESC ;", null);
        SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy");
        while (cursor.moveToNext()) {
            event = new Evento(cursor.getInt(0), cursor.getString(1),
                    cursor.getString(2), cursor.getString(3), cursor.getString(4),
                    formater.parse(cursor.getString(5)), formater.parse(cursor.getString(6)), cursor.getString(7), cursor.getInt(8));
            events.add(event);
        }
        cursor.close();
        return events;
    }

    /**
     * Este metodo serve pra verificar se o evento já existe no banco.
     *
     * @return retorna um evento, procurando-o pelo seu id.
     * @throws ParseException
     */
    public Evento hasEvent(Integer eventId) throws ParseException {
        Evento event;
        SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy");
        Cursor cursor = getWritableDatabase().rawQuery(
                "SELECT * FROM " + TABLE_NAME_EVENT + " WHERE " + COL_ID_EVENT
                        + "= '" + eventId + "';", null);
        System.out.println(cursor.moveToFirst());
        if (cursor.moveToFirst()) {
            event = new Evento(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    new Date(cursor.getLong(5)),
                    new Date(cursor.getLong(6)),
                    cursor.getString(7),
                    cursor.getInt(8));
            cursor.close();
            return event;
        }
        cursor.close();
        return null;
    }

    /**
     * @param eventId Apaga um evento passando seu id
     */
    public void deleteEvent(Integer eventId) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM " + TABLE_NAME_EVENT + " WHERE "
                + COL_ID_EVENT + "= '" + eventId + "';");
    }

    /**
     * @param event Evento a ser favoritado
     */
    public boolean favoriteEvent(Evento event) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        try {
            sqLiteDatabase.execSQL("UPDATE " + TABLE_NAME_NOTICE
                    + " SET " + FAVORITE + " = '" + event.getFavorito() + "'"
                    + " WHERE " + COL_ID_EVENT + " = '" + event.getId() + "';");
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Apaga todos os Eventos
     */
    public void deleteAllEvents() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM" + TABLE_NAME_EVENT + ";");
    }

    private List<String> listImages(Evento event) {
        List<String> images = new ArrayList<String>();
        Cursor cursor = getWritableDatabase().rawQuery(
                "SELECT * FROM " + TABLE_NAME_IMAGES + " WHERE " + COL_ID_EVENT
                        + " =" + event.getId() + " AND " + COL_CATEGORY_IMAGE
                        + " LIKE '%event%' ;", null);
        while (cursor.moveToNext()) {
            String path = cursor.getString(1);
            images.add(path);
        }
        cursor.close();
        return images;
    }
}
