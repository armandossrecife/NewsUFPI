package br.ufpi.newsufpi.view.activity;

import android.os.Bundle;

import br.ufpi.newsufpi.R;
import br.ufpi.newsufpi.model.Evento;
import br.ufpi.newsufpi.view.fragments.EventoFragment;

/**
 * Created by franciscowender on 29/01/2016.
 */
public class EventoActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_evento);
        // Configura a Toolbar como a action bar
        setUpToolbar();

        // Atualiza a evento no fragment
        EventoFragment ef = (EventoFragment) getSupportFragmentManager().findFragmentById(R.id.EventoDetalhadoFragment);
        Evento e = (Evento) getIntent().getSerializableExtra("evento");
        ef.setEvento(e);

        // Título da toolbar e botão up navigation
        getSupportActionBar().setTitle(e.getTitle());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
