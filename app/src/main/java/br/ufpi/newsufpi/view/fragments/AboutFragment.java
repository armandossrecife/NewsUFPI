package br.ufpi.newsufpi.view.fragments;




import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.ufpi.newsufpi.R;
import br.ufpi.newsufpi.util.ExpandableListAdapter;
import livroandroid.lib.fragment.BaseFragment;


/**
 * Created by katia cibele on 16/02/2016.
 */
public class AboutFragment  extends BaseFragment{

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDadosHeader;
    HashMap<String, List<String>> listDadosChild;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //  View view = inflater.inflate(R.layout.fragment_sobre, container, false);
        View view = inflater.inflate(R.layout.sobre_fragment, container, false);


        expListView = (ExpandableListView) view.findViewById(R.id.expandableListView);
        //setando o indicador
       int x,y;
        DisplayMetrics metrics = new DisplayMetrics();
        this.getActivity().getWindow().getWindowManager().getDefaultDisplay().getMetrics(metrics);





        y = metrics.heightPixels;

        x = metrics.widthPixels;


        expListView.setIndicatorBounds(x-30, x);

        

        preparaListaComDados();
        listAdapter = new ExpandableListAdapter(view.getContext(), listDadosHeader, listDadosChild);
        expListView.setAdapter(listAdapter);


        return view;
    }
    /*
    * cria a lista
    */



    private void preparaListaComDados() {
        listDadosHeader = new ArrayList<String>();
        listDadosChild = new HashMap<String, List<String>>();

        // Adding child data
        listDadosHeader.add("Colaboradores");
        listDadosHeader.add("Comunicar erros");
        listDadosHeader.add("Compartilhe com um amigo");
        listDadosHeader.add("Ajuda");

        // Adding child data
        List<String> colaboradores = new ArrayList<String>();
        colaboradores.add ( "Armando Sousa \n" + " Gerente");
        colaboradores.add ("Felipe Saraiva\n"+" Desenvolvedor");
        colaboradores.add ("Francisco Wender\n"+" Desenvolvedor");
        colaboradores.add("Kátia Cibele\n"+" Desenvolvedor");
        colaboradores.add("Luís Guilherme Teixeira\n"+" Desenvolvedor");
        colaboradores.add ("Rodolfo Murilo\n"+" Desenvolvedor");
        colaboradores.add("Ronnie Santos Cardoso\n"+" Desenvolvedor");
        colaboradores.add("Ronyerison Dantas Braga\n"+" Desenvolvedor");
        colaboradores.add ("Thasciano Lima Carvalho\n"+" Desenvolvedor");
        colaboradores.add ("Wermeson Reis\n"+" Desenvolvedor");

        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                switch (groupPosition){
                    case 0: parent.expandGroup(0);
                        return true;
                    case 2:
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_TEXT, "Baixe já o aplicativo de notícias da ufpi.\nhttps://play.google.com/store/apps/details?id=com.google.android.apps.genie.geniewidget");
                        startActivity(Intent.createChooser(intent, "Compartilhar"));
                        return true;
                    default: return true;
                }
            }
        });

        List<String> nowShowing = new ArrayList<String>();


        List<String> comingSoon = new ArrayList<String>();

        listDadosChild.put(listDadosHeader.get(0), colaboradores);
        listDadosChild.put(listDadosHeader.get(1), nowShowing);
        listDadosChild.put(listDadosHeader.get(2), comingSoon);
        listDadosChild.put(listDadosHeader.get(3), comingSoon);

    }



}
