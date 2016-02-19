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
import br.ufpi.newsufpi.model.Lembrete;

/**
 * Created by katia cibele on 12/02/2016.
 */
public class LembreteDao extends FacadeDao {

    private static LembreteDao lembreteDao;

    public static LembreteDao getInstance(Context context) {
        if (lembreteDao == null) {
            lembreteDao = new LembreteDao(context);
        }
        return lembreteDao;
    }

    /**
     * @param context
     */
    public LembreteDao(Context context) {
        super(context);
    }

    public void insertLembrete(List<Lembrete> lembretes) throws ParseException {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        for (Lembrete lembrete : lembretes) {
            if (hasLembrete(lembrete.getId()) == null) {
                ContentValues values = new ContentValues();
                values.put(COL_ID_LEMBRETE, lembrete.getId());
                values.put(COL_TITLE_LEMBRETE, lembrete.getTitle_lembrete());
                 values.put(COL_DATE_LEMBRETE, String.valueOf(lembrete.getDateLembrete()));
                values.put(COL_ID_EVENT, lembrete.getIdEvento());
                sqLiteDatabase.insert(TABLE_NAME_LEMBRETE, null, values);
            }
        }
        sqLiteDatabase.close();
    }



    /**
     * @return retorna todos eventos
     * @throws ParseException
     */
    @SuppressLint("SimpleDateFormat")
    public List<Lembrete> listAllLembrete() throws ParseException {
        List<Lembrete> lembretes = new ArrayList<Lembrete>();
        String[] aux = { COL_ID_LEMBRETE, COL_TITLE_EVENT, COL_DATE_LEMBRETE,
                COL_ID_EVENT};
        Cursor cursor = getWritableDatabase().query(TABLE_NAME_LEMBRETE, aux,
                null, null, null, null,COL_DATE_LEMBRETE + "DESC");
        SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy");
        while (cursor.moveToNext()) {

            Lembrete lembrete = new Lembrete(

                    cursor.getInt(0),
                    cursor.getString(1),
                    formater.parse(cursor.getString(2)),
                    cursor.getInt(3)

            );

            lembretes.add(lembrete);
        }
        cursor.close();
        return lembretes;
    }

    /**
     * Este metodo serve pra verificar se o lembrete pode ser executado
     *
     * @return retorna um lembrete para criar o alerta, verificando com a data
     * @throws ParseException
     */
    public Lembrete hasLembrete(int idLembrete) throws ParseException {
        Lembrete lembrete;
        SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy");

       Cursor cursor = getWritableDatabase().rawQuery(
                "SELECT * FROM " + TABLE_NAME_EVENT + " WHERE " + COL_ID_EVENT
                        + "= '" + idLembrete+ "';", null);

        if (cursor.moveToFirst()) {
            lembrete = new Lembrete( cursor.getInt(0),
                    cursor.getString(1),
                    formater.parse(cursor.getString(2)),
                    cursor.getInt(3)

            );
            return lembrete;
        }
        return null;
    }

    /**
     * @param lembreteId
     *            Apaga um evento passando seu id
     */
    public void deleteLembrete(Integer lembreteId) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM" + TABLE_NAME_LEMBRETE + "WHERE"
                + COL_ID_LEMBRETE+ "= '" + lembreteId + "';");
    }
    /**
     * Apaga todos os Eventos
     */
    public void deleteAllEvents() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM" + TABLE_NAME_LEMBRETE + ";");
    }



}
