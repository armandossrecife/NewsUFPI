package br.ufpi.newsufpi.view.fragments;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;

import android.content.Context;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.Ringtone;

import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.net.URI;
import java.text.DateFormat;
import java.util.Date;

import br.ufpi.newsufpi.R;
import br.ufpi.newsufpi.model.Evento;
import br.ufpi.newsufpi.view.activity.LembreteActivity;
import br.ufpi.newsufpi.view.activity.MainActivity;


/**
 * Created by katia cibele on 03/02/2016.
 */
public class LembreteFragment extends BaseFragment {
    private  Evento e ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lembrete_fragment, container, false);

         e = new Evento (0,"I Simpósio Internacional Estado, Sociedade e Políticas Públicas",
               "O Simpósio é uma concepção e realização do Programa de Pós-graduação em Políticas Públicas da Universidade Federal do Piauí. Tem o apoio institucional das Pró – Reitorias de Ensino de Pós-graduação e de Pesquisa; Centro de Ciências Humanas e Letras, Programa de Pós-graduação em Artes, Patrimônio e Museologia e do Núcleo de Pesquisa e Extensão sobre Serviço Social e Questão Social da Universidade Federal do Piauí.",
                "Ufpi",
                new Date());





        Button btnSalvaNotificacao = (Button) view.findViewById(R.id.btn_salvar_notificacao);
        btnSalvaNotificacao.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

               gerarNotificacao(arg0);
               Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);

            }

        });



        return view;
    }
    public void  gerarNotificacao(View view){
       NotificationManager mn =  (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent intent = PendingIntent.getActivity(getContext(),0,new Intent(getContext(),LembreteActivity.class),0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext());
        //texto que ira aparecer na notifica~ção
        builder.setTicker(e.getTitle());
        builder.setContentTitle(e.getLocal());
        builder.setContentText(e.getTitle());
        builder.setSmallIcon(R.drawable.ic_notification);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.icone_principal));

        Notification n = builder.build();

        //setando a vibraçãp
        n.vibrate = new long[]  {150 ,300, 150 ,600};

        //enviando a notificacao
        mn.notify(R.drawable.icone_principal,n);

        //setando toque
        try{

           Uri som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone toque = RingtoneManager.getRingtone(getContext(),som);


        }catch(Exception e){

        }

    }
}
