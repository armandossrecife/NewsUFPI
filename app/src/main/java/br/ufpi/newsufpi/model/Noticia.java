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
public class Noticia implements Serializable {
    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
    private Integer id;
    private String title;
    private String content;
    private Date date;
    private List<String> images;

    public Noticia() {
        images =  new ArrayList<String>();
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

    public void setDate(String date) {
        try {
            this.date = (Date)format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
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
