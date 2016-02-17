package br.ufpi.newsufpi.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by thasciano on 23/12/15.
 */
public class Evento implements Serializable {

    private Integer id;
    private String title;
    private String content;
    private String local;
    private Date date;
    private List<String> images;

    public Evento(Integer id, String title, String content, String local, Date date) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.local = local;
        this.date = date;
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
