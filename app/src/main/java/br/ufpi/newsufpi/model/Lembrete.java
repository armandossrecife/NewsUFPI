package br.ufpi.newsufpi.model;

import java.util.Date;

/**
 * Created by katia cibele on 12/02/2016.
 */
public class Lembrete {

    private int id;
    private String content;
    private Date dateLembrete;
    private int  idEvento;

    public Lembrete(int id, String content, Date dateLembrete, int idEvento) {
        this.id = id;
        this.content = content;
        this.dateLembrete = dateLembrete;
        this.idEvento = idEvento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(int idEvento) {
        this.idEvento = idEvento;
    }

    public Date getDateLembrete() {
        return dateLembrete;
    }

    public void setDateLembrete(Date dateLembrete) {
        this.dateLembrete = dateLembrete;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
