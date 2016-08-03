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
    private String categoria;
    private Date dataInicio;
    private Date dataFim;
    private Integer favorito = 0;

    private String url;

    /**
     * Construtor.
     *
     * @param id
     * @param title
     * @param content
     * @param local
     * @param categoria
     * @param dataInicio
     * @param dataFim
     * @param favorito
     */
    public Evento(Integer id, String title, String content, String local, String categoria, Date dataInicio, Date dataFim, String url, Integer favorito) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.local = local;
        this.categoria = categoria;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.url = url;
        this.favorito = favorito;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(String dataInicio) {
        try {
            this.dataInicio = (Date) format.parse(dataInicio);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Date getDataFim() {
        return dataFim;
    }

    public void setDataFim(String dataFim) {
        try {
            this.dataFim = (Date) format.parse(dataFim);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getDataInicioString() {
        return format.format(getDataInicio());
    }

    public String getDataFimString() {
        return format.format(getDataFim());
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
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

    public Integer getFavorito() {
        return favorito;
    }

    public void setFavorito(Integer favorito) {
        this.favorito = favorito;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
