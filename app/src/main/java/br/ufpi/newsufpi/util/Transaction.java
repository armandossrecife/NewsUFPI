package br.ufpi.newsufpi.util;

/**
 * Created by thasciano on 27/01/16.
 */
public interface Transaction {
    public void doBefore();
    public void doAfter(String answer);
    public RequestData getRequestData();
}
