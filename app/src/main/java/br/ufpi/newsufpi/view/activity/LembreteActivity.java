package br.ufpi.newsufpi.view.activity;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import br.ufpi.newsufpi.R;
import android.view.View;

/**
 * Created by katia cibele on 03/02/2016.
 */
public class LembreteActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        nm.cancel(R.drawable.icone_principal);
    }
}

