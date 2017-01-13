package br.ufpi.newsufpi.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.ufpi.newsufpi.model.Noticia;

/**
 * Classe responsavel por fazer as operações no banco de dados de noticias.
 * <p>
 * Created by thasciano on 23/12/15.
 */
public class NoticiaDao extends FacadeDao {

    private static NoticiaDao noticiaDao;

    /**
     * @param context
     * @return
     */
    public static NoticiaDao getInstance(Context context) {
        if (noticiaDao == null) {
            noticiaDao = new NoticiaDao(context);
        }
        return noticiaDao;
    }

    /**
     * @param context
     */
    private NoticiaDao(Context context) {
        super(context);
    }

    /**
     * Lista todas as notícias
     * @return
     */
    public List<Noticia> listAllNotices() {
        List<Noticia> notices = new ArrayList<Noticia>();
        String[] cols = {COL_ID_NOTICE, COL_TITLE_NOTICE, COL_CONTENT_NOTICE,
                COL_DATE_NOTICE, COL_IMAGE_NOTICE, URL, FAVORITE};
        Cursor cursor = getWritableDatabase().query(TABLE_NAME_NOTICE, cols,
                null, null, null, null, COL_DATE_NOTICE + " DESC");
        while (cursor.moveToNext()) {
            Noticia noticia = new Noticia(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    new Date(cursor.getLong(3)),
                    cursor.getString(5),
                    cursor.getInt(6));
            List<String> imagens = this.listImages(noticia);
            noticia.setImages(imagens);
            notices.add(noticia);

        }
        cursor.close();
        return notices;
    }

    /**
     * Lista as notícias de acordo com o texto buscado.
     * @param text - O texto que está buscando.
     * @return
     * @throws ParseException
     */
    public List<Noticia> findNotices(String text) throws ParseException {
        List<Noticia> noticias = new ArrayList<Noticia>();
        Cursor cursor = getWritableDatabase().rawQuery(
                "SELECT * FROM " + TABLE_NAME_NOTICE + " WHERE "
                        + COL_TITLE_NOTICE + " LIKE '%" + text + "%' OR "
                        + COL_CONTENT_NOTICE + " LIKE '%" + text
                        + "%' ORDER BY " + COL_DATE_NOTICE + " DESC;", null);

        while (cursor.moveToNext()) {
            Noticia noticia = new Noticia(cursor.getInt(0),
                    cursor.getString(1), cursor.getString(2),
                    new Date(cursor.getLong(3)), cursor.getString(4), cursor.getInt(5));
            List<String> imagens = this.listImages(noticia);
            noticia.setImages(imagens);
            noticias.add(noticia);
        }
        cursor.close();
        return noticias;
    }

    /**
     * Salva uma lista de notícias
     * @param noticias
     * @return
     * @throws ParseException
     */
    public int insertNotices(List<Noticia> noticias) throws ParseException {
        int count = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        for (Noticia notice : noticias) {
            if ((notice.getId() != null) && (hasNotice(notice.getId()) == null)) {
                ContentValues values = new ContentValues();
                values.put(COL_ID_NOTICE, notice.getId());
                values.put(COL_TITLE_NOTICE, notice.getTitle());
                values.put(COL_CONTENT_NOTICE, notice.getContent());
                values.put(COL_DATE_NOTICE, notice.getDate().getTime());
                values.put(URL, notice.getUrl());
                insertImages(notice, db);
                db.insert(TABLE_NAME_NOTICE, null, values);
                Log.i("Save", notice.getTitle());
                count++;
            }
        }
//        db.close();
        return count;
    }

    /**
     * Método que insere o path das imagens na tabela de imagens
     *
     * @param notice
     * @param sqLiteDatabase
     */
    private void insertImages(Noticia notice, SQLiteDatabase sqLiteDatabase) {
        for (String image : notice.getImages()) {
            ContentValues values = new ContentValues();
            values.put(COL_ID_NOTICE, notice.getId());
            values.put(COL_PATH_IMAGE, image);
            values.put(COL_CATEGORY_IMAGE, "notice");
            sqLiteDatabase.insert(TABLE_NAME_IMAGES, null, values);
        }
    }

    /**
     * Método que retorna o path das imagens referentes a uma determinada
     * Notícia.
     *
     * @param noticia
     * @return
     */
    private List<String> listImages(Noticia noticia) {
        List<String> images = new ArrayList<String>();
        Cursor cursor = getWritableDatabase().rawQuery(
                "SELECT * FROM " + TABLE_NAME_IMAGES + " WHERE "
                        + COL_ID_NOTICE + " =" + noticia.getId() + " AND "
                        + COL_CATEGORY_IMAGE + " LIKE '%notice%' ;", null);
        while (cursor.moveToNext()) {
            String path = cursor.getString(1);
            images.add(path);
        }
        cursor.close();
        return images;
    }

    /**
     * Método que busca uma notícia pelo id.
     *
     * @param noticiaId - O id da noticia
     * @return Uma noticia se existir ou nulo senão.
     * @throws ParseException
     */

    public Noticia hasNotice(Integer noticiaId) throws ParseException {
        Noticia noticia;

        Cursor cursor = getWritableDatabase().rawQuery(
                "SELECT * FROM " + TABLE_NAME_NOTICE + " WHERE "
                        + COL_ID_NOTICE + "= '" + noticiaId + "';", null);

        if (cursor.moveToFirst()) {
            noticia = new Noticia(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    new Date(cursor.getLong(3)),
                    "url",
                    cursor.getInt(5)
            );
            cursor.close();
            return noticia;
        }
        cursor.close();
        return null;
    }

    public boolean favoriteNotice(Noticia noticia) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        try {
            sqLiteDatabase.execSQL("UPDATE " + TABLE_NAME_NOTICE
                    + " SET " + FAVORITE + " = '" + noticia.getFavorito() + "'"
                    + " WHERE " + COL_ID_NOTICE + " = '" + noticia.getId() + "';");
            return true;
        } catch (SQLException e){
            return false;
        }
    }
}
