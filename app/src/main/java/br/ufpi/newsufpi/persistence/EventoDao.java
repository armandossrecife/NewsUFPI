package br.ufpi.newsufpi.persistence;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import br.ufpi.newsufpi.model.Evento;

/**
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
     * @param context
     */
    public EventoDao(Context context) {
        super(context);
    }

    public void insertEvents(List<Evento> eventos) throws ParseException {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        for (Evento evento : eventos) {
            if (hasEvent(evento.getId()) == null) {
                ContentValues values = new ContentValues();
                values.put(COL_ID_EVENT, evento.getId());
                values.put(COL_TITLE_EVENT, evento.getTitle());
                values.put(COL_CONTENT_EVENT, evento.getContent());
                values.put(COL_LOCAL_EVENT, evento.getLocal());
                values.put(COL_DATE_EVENT, String.valueOf(evento.getDate()));
                // values.put(COL_IMAGE_EVENT, event.getImages());
                insertImages(evento, sqLiteDatabase);
                sqLiteDatabase.insert(TABLE_NAME_EVENT, null, values);
            }
        }
        sqLiteDatabase.close();
    }

    public void insertImages(Evento event, SQLiteDatabase sqLiteDatabase) {
        for (String image : event.getImages()) {
            ContentValues values = new ContentValues();
            values.put(COL_ID_EVENT, event.getId());
            values.put(COL_PATH_IMAGE, image);
            values.put(COL_CATEGORY_IMAGE, "event");
            sqLiteDatabase.insert(TABLE_NAME_IMAGES, null, values);
        }
    }

    /**
     * @return retorna todos eventos
     * @throws ParseException
     */
    @SuppressLint("SimpleDateFormat")
    public List<Evento> listAllEvents() throws ParseException {
        List<Evento> events = new ArrayList<Evento>();
        String[] aux = { COL_ID_EVENT, COL_TITLE_EVENT, COL_CONTENT_EVENT,
                COL_LOCAL_EVENT, COL_DATE_EVENT };
        Cursor cursor = getWritableDatabase().query(TABLE_NAME_EVENT, aux,
                null, null, null, null, COL_DATE_EVENT + " DESC");
        SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy");
        while (cursor.moveToNext()) {

            Evento event = new Evento(cursor.getInt(0), cursor.getString(1),
                    cursor.getString(2), cursor.getString(3),
                    formater.parse(cursor.getString(4)));
            List<String> images = listImages(event);
            event.setImages(images);
            events.add(event);
        }
        cursor.close();
        return events;
    }

    /**
     * @param eventTitle
     * @return retorna os eventos, onde será buscado no titulo e na descrição do
     *         evento
     * @throws ParseException
     */
    public List<Evento> findEvent(String eventTitle) throws ParseException {
        Evento event;
        List<Evento> events = new ArrayList<Evento>();
        Cursor cursor = getWritableDatabase().rawQuery(
                "SELECT * FROM " + TABLE_NAME_EVENT + " WHERE "
                        + COL_TITLE_EVENT + " LIKE '%" + eventTitle + "%' OR "
                        + COL_CONTENT_EVENT + " LIKE '%" + eventTitle
                        + "%' order by " + COL_DATE_EVENT + " DESC ;", null);
        SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy");
        while (cursor.moveToNext()) {
            event = new Evento(cursor.getInt(0), cursor.getString(1),
                    cursor.getString(2), cursor.getString(3),
                    formater.parse(cursor.getString(4)));
            List<String> images = listImages(event);
            event.setImages(images);
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
                    formater.parse(cursor.getString(4))
            );
            return event;
        }
        return null;
    }

    /**
     * @param eventId
     *            Apaga um evento passando seu id
     */
    public void deleteEvent(Integer eventId) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM" + TABLE_NAME_EVENT + "WHERE"
                + COL_ID_EVENT + "= '" + eventId + "';");
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
        return images;
    }
}
