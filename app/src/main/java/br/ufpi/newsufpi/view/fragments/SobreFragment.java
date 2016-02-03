package br.ufpi.newsufpi.view.fragments;

import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import livroandroid.lib.fragment.BaseFragment;

import br.ufpi.newsufpi.R;


import static br.ufpi.newsufpi.R.layout.fragment_sobre;

/**
 * Created by katia cibele on 02/02/2016.
 */
public class SobreFragment  extends BaseFragment {
    private static final String TEXT1 = "text1";
    private static final String TEXT2 = "text2";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sobre, container, false);


        //convertendo o array enum para um tipo map

        final Equipe[] equipe = {
                new Equipe("Armando Sousa ","Gerente"),
                new Equipe("Francisco Wender","Desenvolvedor"),
                new Equipe("Kátia Cibele","Desenvolvedor"),
                new Equipe("Luís Guilherme Teixeira","Desenvolvedor"),
                new Equipe("Ronnie Santos Cardoso","Desenvolvedor"),
                new Equipe("Ronyerison Dantas Braga","Desenvolvedor"),
                new Equipe("Thasciano Lima Carvalho","Desenvolvedor"),
                new Equipe("Wemerson Reis","Desenvolvedor")

        };
        final ListAdapter listAdapter = createListAdapter(equipe);
        ListView listView = (ListView) view.findViewById(R.id.listViewTelaSobre);

       listView.setClickable(false);
         listView.setAdapter(listAdapter);







        return view;


    }

    public class Equipe {
        String name;
        String info;

        public Equipe(String name, String info) {
            this.name = name;
            this.info = info;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

    }

    private List<Map<String, String>> convertToListItems(final Equipe[] equipe) {
        final List<Map<String, String>> listItem =
                new ArrayList<Map<String, String>>(equipe.length);

        for (final Equipe eq: equipe) {
            final Map<String, String> listItemMap = new HashMap<String, String>();
            listItemMap.put(TEXT1, eq.getName());
            listItemMap.put(TEXT2, eq.getInfo());
            listItem.add(Collections.unmodifiableMap(listItemMap));
        }

        return Collections.unmodifiableList(listItem);
    }
    private ListAdapter createListAdapter(final Equipe[] eq) {
        final String[] fromMapKey = new String[] {TEXT1, TEXT2};
        final int[] toLayoutId = new int[] {android.R.id.text1, android.R.id.text2};
        final List<Map<String, String>> list = convertToListItems(eq);

        return new SimpleAdapter(getActivity(),
                list,
                android.R.layout.simple_list_item_2,
                fromMapKey, toLayoutId);
    }
}
