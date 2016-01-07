package br.ufpi.newsufpi.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.util.List;

import br.ufpi.newsufpi.model.Evento;
import br.ufpi.newsufpi.model.Noticia;

/**
 * Created by thasciano on 23/12/15.
 */
public class FacadeDao extends SQLiteOpenHelper {

    private static FacadeDao facadeDao;
    private static NoticiaDao noticiaDao;
    private static EventoDao eventoDao;

    protected static final Integer DATABASE_VERSION = 1;

    protected static final String TABLE_NAME_NOTICE = "NOTICE";
    protected static final String COL_ID_NOTICE = "ID";
    protected static final String COL_TITLE_NOTICE = "TITLE";
    protected static final String COL_CONTENT_NOTICE = "CONTENT";
    protected static final String COL_DATE_NOTICE = "DATE";
    protected static final String COL_IMAGE_NOTICE = "IMAGE";

    protected static final String TABLE_NAME_EVENT = "EVENT";
    protected static final String COL_ID_EVENT = "ID";
    protected static final String COL_TITLE_EVENT = "TITLE";
    protected static final String COL_CONTENT_EVENT = "CONTENT";
    protected static final String COL_LOCAL_EVENT = "LOCAL";
    protected static final String COL_DATE_EVENT = "DATE";
    protected static final String COL_IMAGE_EVENT = "IMAGE";

    protected static final String TABLE_NAME_IMAGES = "IMAGES";
    protected static final String COL_PATH_IMAGE = "PATH";
    protected static final String COL_CATEGORY_IMAGE = "CATEGORY";

    private static final String CREATE_TABLE_NOTICE = "CREATE TABLE "
            + TABLE_NAME_NOTICE + "( " + COL_ID_NOTICE
            + " INTEGER PRIMARY KEY, " + COL_TITLE_NOTICE + " TEXT , "
            + COL_CONTENT_NOTICE + " TEXT , " + COL_DATE_NOTICE + " DATETIME, "
            + COL_IMAGE_NOTICE + " TEXT );";

    private static final String CREATE_TABLE_EVENT = "CREATE TABLE "
            + TABLE_NAME_EVENT + "( " + COL_ID_EVENT + " INTEGER PRIMARY KEY, "
            + COL_TITLE_EVENT + " TEXT , " + COL_CONTENT_EVENT + " TEXT , "
            + COL_LOCAL_EVENT + " TEXT , " + COL_DATE_EVENT + " TEXT);";

    private static final String CREATE_TABLE_IMAGES = "CREATE TABLE "
            + TABLE_NAME_IMAGES + "( " + COL_ID_EVENT + " INTEGER, "
            + COL_PATH_IMAGE + " TEXT, "
            + COL_CATEGORY_IMAGE + " TEXT,"
            +" PRIMARY KEY(" + COL_ID_EVENT + ","+COL_PATH_IMAGE+"));";

    public FacadeDao(Context context) {
        super(context, "newsufpi_database", null, DATABASE_VERSION);
    }

    public static FacadeDao getInstance(Context context) {
        if (facadeDao == null) {
            facadeDao = new FacadeDao(context);
        }
        noticiaDao = NoticiaDao.getInstance(context);
        eventoDao = EventoDao.getInstance(context);
        return facadeDao;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_NOTICE);
        db.execSQL(CREATE_TABLE_EVENT);
        db.execSQL(CREATE_TABLE_IMAGES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_NAME_NOTICE);
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_NAME_EVENT);
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_NAME_IMAGES);
        onCreate(db);
    }

    /**
     * Método que lista todas as noticias.
     * @return Uma lista de notpicias.
     */
    public List<Noticia> listAllNotices() {
        return noticiaDao.listAllNotices();
    }

    /**
     * Método que insere as notícias no banco de dados.
     * @param noticias
     * @throws ParseException
     */
    public void insertNotices(List<Noticia> noticias) throws ParseException {
        noticiaDao.insertNotices(noticias);
    }

    /**
     * Método que busca as noticias no banco por determinado texto.
     * @param text - O texto que está buscando.
     * @return
     * @throws ParseException
     */
    public List<Noticia> findNotices(String text) throws ParseException {
        return noticiaDao.findNotices(text);
    }

    /**
     * @return
     * @throws ParseException
     */
    public List<Evento> listAllEvents() throws ParseException {
        return eventoDao.listAllEvents();
    }

    /**
     * @param eventos
     * @throws ParseException
     */
    public void insertEvents(List<Evento> eventos) throws ParseException {
        eventoDao.insertEvents(eventos);
    }

    /**
     * @param eventTitle
     * @return
     * @throws ParseException
     */
    public List<Evento> findEvent(String eventTitle) throws ParseException {
        return eventoDao.findEvent(eventTitle);
    }

    /**
     * @param eventId
     */
    public void deleteEvent(Integer eventId) {
        eventoDao.deleteEvent(eventId);
    }

    /**
     *
     */
    public void deleteAllEvents() {
        eventoDao.deleteAllEvents();
    }

}
