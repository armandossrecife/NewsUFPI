package br.ufpi.newsufpi.view.fragments;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;

import android.content.Context;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.graphics.BitmapFactory;
import android.media.Ringtone;

import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

import java.util.List;


import br.ufpi.newsufpi.R;

import br.ufpi.newsufpi.model.Evento;

import br.ufpi.newsufpi.persistence.FacadeDao;
import br.ufpi.newsufpi.view.activity.LembreteActivity;
import br.ufpi.newsufpi.view.activity.MainActivity;


/**
 * Created by katia cibele on 03/02/2016.
 */
public class LembreteFragment extends BaseFragment {
    private  Evento e ;
    private Spinner spinner;
    private ListView listView;
    List<Evento> eventos;
    private ArrayList<String> op ;

    private SpinnerAdapter spinnerAdapter ;
    private FacadeDao dadosBB = new FacadeDao(getContext());



    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.lembrete_fragment, container, false);


        //tratando button
        ImageButton b1 = (ImageButton) view.findViewById(R.id.buttonConfirmaLembrete);
        ImageButton b2 = (ImageButton) view.findViewById(R.id.buttonCancelaLembrete);

        b1.setOnClickListener(new View.OnClickListener()
                              {
                                  // caso comfirmação para insercao  de dados
                                  public void onClick(View v) {
                                      replaceFragment(new FormLembreteFragment());
                                  }
                              }
        );
        b2.setOnClickListener(new View.OnClickListener() {
                                  @Override
                                  public void onClick(View v) {
                                      Intent intent = new Intent(getActivity(), MainActivity.class);
                                      startActivity(intent);
                                  }
                              }
        );


        //preparando listView

        ArrayList image_details = getListDadosListView();

        listView = (ListView) view.findViewById(R.id.listViewMenuLembretes);
        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_multiple_choice,image_details);
        listView.setAdapter(new CustomListAdapter(getContext(), image_details));


        return view;
    }

    private ArrayList getListDadosListView() {

        ArrayList<NewsItem> results = new ArrayList<NewsItem>();
        NewsItem newsData = new NewsItem();
        List<Evento> e = dadosBB.listAllEvents();
        newsData.setTitulo(e.get(0).getTitle());
        newsData.setLocal(e.get(0).getLocal());
        newsData.setCategoria("Notificar uma hora antes");
        newsData.setDate(e.get(0).getDataInicioString());
        results.add(newsData);

        // Add some more dummy data for testing
        return results;
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
        mn.notify(R.drawable.icone_principal, n);

        //setando toque
        try{

            Uri som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone toque = RingtoneManager.getRingtone(getContext(),som);


        }catch(Exception e){

        }

    }

    private void replaceFragment(Fragment frag) {
       this.getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_drawer_container, frag, "TAG").commit();
    }

    public  View onCreateViewInseriLembrete (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){



        //chamando formulario
        final View view = inflater.inflate(R.layout.form_lembrete_fragment, container, false);




        return view;


    }


    //classe que seta os itens

    class NewsItem {
        private String titulo;
        private String local;
        private String categoria;
        private String date;

        public String getTitulo() {
            return titulo;
        }

        public void setTitulo(String titulo) {
            this.titulo = titulo;
        }

        public String getLocal() {
            return local;
        }

        public void setLocal(String local) {
            this.local = local;
        }

        public String getCategoria() {
            return categoria;
        }

        public void setCategoria(String categoria) {
            this.categoria = categoria;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }
    //classe usada para manipulas ListAdapter
    public class CustomListAdapter extends BaseAdapter {
        private ArrayList<NewsItem> listData;
        private LayoutInflater layoutInflater;

        public CustomListAdapter(Context aContext, ArrayList<NewsItem> listData) {
            this.listData = listData;
            layoutInflater = LayoutInflater.from(aContext);
        }

        @Override
        public int getCount() {
            return listData.size();
        }

        @Override
        public Object getItem(int position) {
            return listData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.list_row_lembrete, null);
                holder = new ViewHolder();
                holder.tituloView = (TextView) convertView.findViewById(R.id.titleSubItemLembrete);
                holder.localView = (TextView) convertView.findViewById(R.id.local);
                holder.categoriaView = (TextView) convertView.findViewById(R.id.categoria);
                holder.reportedDateView = (TextView) convertView.findViewById(R.id.dataSubItemLembrete);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.tituloView.setText(listData.get(position).getTitulo());
            holder.localView.setText("Local: " + listData.get(position).getLocal());
            holder.localView.setText(" " + listData.get(position).getCategoria());
            holder.reportedDateView.setText(listData.get(position).getDate());
            return convertView;
        }

        class ViewHolder {
            TextView tituloView;
            TextView localView;
            TextView categoriaView;
            TextView reportedDateView;
        }
    }

    public Evento findEvento (int idEvento){
        List<Evento> evt = dadosBB.listAllEvents();
        Evento e = new Evento(0,null,null,null,null,null,null);
        for (int i=0;i<evt.size();i++) {
            if(evt.get(i).getId() ==  idEvento){
                e = evt.get(i);
                return e;
            }

        }

        return  null;
    }



}


