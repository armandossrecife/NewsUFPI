package br.ufpi.newsufpi.broadcastReceiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;

import java.text.ParseException;
import java.util.HashMap;

import br.ufpi.newsufpi.R;
import br.ufpi.newsufpi.controller.EventoController;
import br.ufpi.newsufpi.model.Evento;
import br.ufpi.newsufpi.view.activity.EventoActivity;

/**
 * Created by FelipeFsc on 02/08/2016.
 */

public class NotificationService extends BroadcastReceiver {
    public static HashMap<String, PendingIntent> alarms = new HashMap<>();

    public void onReceive(Context context, Intent intent) {

        String title = intent.getStringExtra("title");
        int i = intent.getIntExtra("message", 0);
        String aux = intent.getStringExtra("id");
        EventoController controller = new EventoController(context);
        Evento e = null;
        try {
            e = controller.hasEvent(Integer.valueOf(aux));
        } catch (ParseException e1) {
//            e1.printStackTrace();
        }
        if( e == null){
            return;
        }
        alarms.remove(aux);
        String st;
        switch (i) {
            default:
                st = e.getTitle() + "\n" + "Começa em 1 hora";
                break;
            case 1:
                st = e.getTitle() + "\n" + "Começa em 1 dia";
                break;
        }
        //Constroi a mensagem que será exibida ao usuário
        String msg = context.getString(R.string.app_name) + "\n" + title;
        //Cria uma nova intent para lançar a activity ao clicar na notificação
        Intent notIntent = new Intent(context, EventoActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notIntent, 0);
        Bitmap localBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icone_principal);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentIntent(pendingIntent)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setContentTitle("News UFPI")
                .setLargeIcon(localBitmap)
                .setContentText(st)
                .setWhen(System.currentTimeMillis())
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setAutoCancel(true)
                .setVibrate(new long[]{20L, 500L, 20L, 500L});
        ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).notify(1, builder.build());

    }
}
