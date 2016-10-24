package br.ufpi.newsufpi.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Entidade de Evento.
 * <p>
 * Created by thasciano on 23/12/15.
 */
@SuppressWarnings("ALL")
public class Evento implements Serializable {
    public static final String EVENTO_URL = "http://ufpi.br/agenda-ufpi";

    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
    /**
     *
     */
    private Integer id;
    private String title;
    private String content;
    private String local;
    private String category;
    private Date dateBegin;
    private Date dateEnd;
    private Integer favorite = 0;

    private String url;

    /**
     * Construtor.
     *
     * @param id
     * @param title
     * @param content
     * @param local
     * @param category
     * @param dateBegin
     * @param dateEnd
     * @param favorite
     */
    public Evento(Integer id, String title, String content, String local, String category, Date dateBegin, Date dateEnd, String url, Integer favorite) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.local = local;
        this.category = category;
        this.dateBegin = dateBegin;
        this.dateEnd = dateEnd;
        this.url = url;
        this.favorite = favorite;
    }

    public Date getDateBegin() {
        return dateBegin;
    }

    public void setDateBegin(String dateBegin) {
        try {
            this.dateBegin = (Date) format.parse(dateBegin);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        try {
            this.dateEnd = (Date) format.parse(dateEnd);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getDateBeginString() {
        return format.format(getDateBegin());
    }

    public String getDateEndString() {
        return format.format(getDateEnd());
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public Integer getFavorite() {
        return favorite;
    }

    public void setFavorite(Integer favorite) {
        this.favorite = favorite;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
