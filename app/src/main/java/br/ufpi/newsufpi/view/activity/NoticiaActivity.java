package br.ufpi.newsufpi.view.activity;

import android.os.Bundle;

import br.ufpi.newsufpi.R;
import br.ufpi.newsufpi.model.Noticia;
import br.ufpi.newsufpi.view.fragments.NoticiaFragment;

/**
 * Created by thasciano on 24/12/15.
 */
public class NoticiaActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_noticia);

        // Configura a Toolbar como a action bar
        setUpToolbar();

        // Atualiza a noticia no fragment
        NoticiaFragment nf = (NoticiaFragment) getSupportFragmentManager().findFragmentById(R.id.NoticiaDetalhadaFragment);
        Noticia n = (Noticia) getIntent().getSerializableExtra("noticia");
        nf.setNoticia(n);

        // Título da toolbar e botão up navigation
        //getSupportActionBar().setTitle(n.getTitle());
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
