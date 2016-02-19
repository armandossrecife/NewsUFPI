package br.ufpi.newsufpi.model;

import java.util.Date;

/**
 * Created by katia cibele on 12/02/2016.
 */
public class Lembrete {

    private int id;
    private String title_lembrete;
    private Date dateLembrete;
    private int  idEvento;

    public Lembrete(int id, String title_lembrete, Date dateLembrete, int idEvento) {
        this.id = id;
        this.title_lembrete = title_lembrete;

        this.dateLembrete = dateLembrete;
        this.idEvento = idEvento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle_lembrete() {
        return title_lembrete;
    }

    public void setTitle_lembrete(String title_lembrete) {
        this.title_lembrete = title_lembrete;
    }

    public Date getDateLembrete() {
        return dateLembrete;
    }

    public void setDateLembrete(Date dateLembrete) {
        this.dateLembrete = dateLembrete;
    }

    public int getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(int idEvento) {
        this.idEvento = idEvento;
    }
}
