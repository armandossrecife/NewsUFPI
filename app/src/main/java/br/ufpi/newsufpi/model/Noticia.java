package br.ufpi.newsufpi.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by thasciano on 23/12/15.
 */
@SuppressWarnings("ALL")
public class Noticia implements Serializable {
    //gerador de json: http://www.json-generator.com/
    public static final String NOTICIA_URL = "http://ufpi.br/ultimas-noticias-ufpi";

    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
    private Integer id;
    private String title;
    private String content;
    private Date date;
    private List<String> images;
    private String url;

    private Integer favorito = 0;

    public Noticia() {
        images = new ArrayList<String>();
    }

    public Noticia(Integer id, String title,
                   String content, Date date, String url, Integer favorito) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.date = date;
        this.url = url;
        this.favorito = favorito;
    }

    public Noticia(Integer id, String title, String content, Date date,
                   String url, Integer favorito, List<String> images) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.images = images;
        this.content = content;
        this.url = url;
        this.favorito = favorito;
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


    public String getDateString() {
        return format.format(getDate());
    }

    public Date getDate() {
        return date;
    }

    public void setDate(String date) {
        try {
            this.date = (Date) format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getFavorito() {
        return favorito;
    }

    public void setFavorito(Integer favorito) {
        this.favorito = favorito;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImagens(String images) {
        this.images.add(images);
//        this.images = images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
