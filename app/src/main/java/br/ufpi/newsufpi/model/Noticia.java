package br.ufpi.newsufpi.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by thasciano on 23/12/15.
 */
public class Noticia implements Serializable {

    private Integer id;
    private String title;
    private String content;
    private Date date;
    private List<String> images;

    public Noticia() {
    }

    public Noticia(Integer id, String title,
                   String content, Date date) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.date = date;
    }

    public Noticia(Integer id, String title, Date date,
                   List<String> images, String content) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.images = images;
        this.content = content;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
