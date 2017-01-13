package br.ufpi.newsufpi.view.activity;

import android.support.v7.widget.Toolbar;

import br.ufpi.newsufpi.R;

/**
 * Classe Mãe para todas as activities do projeto.
 * Created by thasciano on 24/12/15.
 */
public class BaseActivity extends livroandroid.lib.activity.BaseActivity {
    /**
     * Ativa a Toolbar no codigo. pois essa tarefa precisa ser feita em todas as activities.
     */
    protected void setUpToolbar() {
        // fazer import android.support.v7.widget.Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.i_toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
    }

}
