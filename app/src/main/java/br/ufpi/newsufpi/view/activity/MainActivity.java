package br.ufpi.newsufpi.view.activity;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.SearchView;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import br.ufpi.newsufpi.R;
import br.ufpi.newsufpi.view.adapter.NavDrawerMenuAdapter;
import br.ufpi.newsufpi.view.adapter.NavDrawerMenuItem;
import br.ufpi.newsufpi.view.fragments.AboutFragment;
import br.ufpi.newsufpi.view.fragments.EventosFragment;
import br.ufpi.newsufpi.view.fragments.LembreteFragment;
import br.ufpi.newsufpi.view.fragments.NoticiasFragment;
import livroandroid.lib.fragment.NavigationDrawerFragment;

/**
 * Activity Principal
 * @author thasciano
 */
public class MainActivity extends BaseActivity implements
        NavigationDrawerFragment.NavigationDrawerCallbacks {

    private NavigationDrawerFragment mNavDrawerFragment;
    private NavDrawerMenuAdapter listAdapter;
    private int menuPosicao;
    private Bundle args = new Bundle();
    URL url =null;
    /**
     * Cria a Activity principal.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toolbar funcionando como ActionBar
        setUpToolbar();

        // Navigation Drawer 4
        mNavDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.nav_drawer_fragment);

        // Configura o drawer layout
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.setStatusBarBackground(R.color.colorPrimary);
        mNavDrawerFragment.setUp(drawerLayout);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try{
//                    getPage(null);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
    }


    /**
     * Opções de menu Toolbar.
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        //Search View
        SearchView searchView = (SearchView) MenuItemCompat.
                getActionView(menu.findItem(R.id.action_search));
        searchView.setOnQueryTextListener(onSearch());
        return true;
    }

    /**
     * Configurações para a aplicação tratar o evento Search View.
     * @return
     */
    private SearchView.OnQueryTextListener onSearch() {
        return new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Usuário fez a busca
                //toast("Buscar o texto: " + query);

                Context context = getApplicationContext();
                CharSequence start = query;
                //int duration = Toast.LENGTH_SHORT;
                if(menuPosicao == 0) {
                    Fragment fragment = new NoticiasFragment();

                    args.putSerializable("buscar", 1);
                    args.putSerializable("query", query);
                    fragment.setArguments(args);
                    replaceFragment(fragment);
                    // Insert the fragment by replacing any existing fragment
                    /*FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.content_frame, fragment)
                            .commit();*/
                }
                if(menuPosicao == 1) {
                    Fragment fragment = new EventosFragment();
                    Bundle args = new Bundle();
                    args.putSerializable("buscar", 1);
                    args.putSerializable("query", query);
                    fragment.setArguments(args);
                    replaceFragment(fragment);
                    // Insert the fragment by replacing any existing fragment
                   /* FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.content_frame, fragment)
                            .commit();*/
                }

                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        };
    }

    public Document getPage(String url) throws IOException {

        Connection connection = Jsoup.connect("http://ufpi.br/ultimas-noticias-ufpi");
        connection.timeout(5000);
        Document doc = connection.get();
        FormElement e = (FormElement) doc.body().getElementsByAttributeValue("name","adminForm").first();
        Element e1 = e.getElementsByAttributeValue("class", "tileItem").first();

        Log.d("Noticias", e1.getElementsByTag("p").first().toString());
        return doc;
    }

    /**
     * Ações do menu.
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    /**
     *Deve retornar a view e o identificador do Listview.
     * @param navDrawerFrag
     * @param inflater
     * @param container
     * @return
     */
    @Override
    public NavigationDrawerFragment.NavDrawerListView getNavDrawerView(NavigationDrawerFragment navDrawerFrag, LayoutInflater inflater, ViewGroup container) {
        View view = getLayoutInflater().inflate(R.layout.nav_drawer_listview, container, false);
        //preenche o cabeçalho com a foto, nome.
        return (new NavigationDrawerFragment.NavDrawerListView(view, R.id.listView));
    }

    /**
     * Este método deve retornar o adapter que vai preencher o Listview
     * @param navDrawerFrag
     * @return
     */
    @Override
    public ListAdapter getNavDrawerListAdapter(NavigationDrawerFragment navDrawerFrag) {
        List<NavDrawerMenuItem> list = NavDrawerMenuItem.getList();
        // Deixa o primeiro item selecionado
        list.get(0).selected = true;
        this.listAdapter = new NavDrawerMenuAdapter(this, list);
        return listAdapter;
    }

    /**
     * Método chamado ao selecionar um item do Listview.
     * @param navDrawerFrag
     * @param position
     */
    @Override
    public void onNavDrawerItemSelected(NavigationDrawerFragment navDrawerFrag, int position) {
        menuPosicao = position;
        List<NavDrawerMenuItem> list = NavDrawerMenuItem.getList();
        NavDrawerMenuItem selectedItem = list.get(position);

        Fragment fragment;
        Bundle args = new Bundle();
        args.putSerializable("buscar", 0);

        // Seleciona a linha
        this.listAdapter.setSelected(position, true);
        if (position == 0) {
            fragment = new NoticiasFragment();
            fragment.setArguments(args);
            replaceFragment(fragment);
        } else if (position == 1) {
            fragment = new EventosFragment();
            fragment.setArguments(args);
            replaceFragment(fragment);

            //replaceFragment(new EventosFragment());
        } else if (position == 2) {
            fragment = new LembreteFragment();
            fragment.setArguments(args);
            replaceFragment(fragment);
        } else if (position == 3) {
            fragment = new NoticiasFragment();
            args.putBoolean("favorites",true);
            fragment.setArguments(args);
            replaceFragment(fragment);
            //replaceFragment(new LembreteFragment());
        } else if (position == 4) {
            fragment = new AboutFragment();
            fragment.setArguments(args);
            replaceFragment(fragment);

            //replaceFragment(new AboutFragment());
        } else {
            Log.e("NewsUFPI", "Item de menu inválido");
        }

    }

    /**
     * Adiciona o fragment no centro da tela.
     * @param frag
     */
    private void replaceFragment(Fragment frag) {
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_drawer_container, frag, "TAG").commit();
    }
}
