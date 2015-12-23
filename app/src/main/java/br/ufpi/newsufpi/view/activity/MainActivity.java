package br.ufpi.newsufpi.view.activity;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.SearchView;

import java.util.List;

import br.ufpi.newsufpi.R;
import br.ufpi.newsufpi.view.adapter.NavDrawerMenuAdapter;
import br.ufpi.newsufpi.view.adapter.NavDrawerMenuItem;
import livroandroid.lib.fragment.NavigationDrawerFragment;

/**
 * classe Principal.
 */
public class MainActivity extends BaseActivity implements
        NavigationDrawerFragment.NavigationDrawerCallbacks {

    private NavigationDrawerFragment mNavDrawerFragment;
    private NavDrawerMenuAdapter listAdapter;
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

       /* //Nav Drawer
        mNavDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().
                findFragmentById(R.id.drawer_layout);
        //Configura o drawer layout
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavDrawerFragment.setUp(drawerLayout);*/

    }

    /**
     * Opções de menu.
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        //Search View
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
//        searchView.setOnQueryTextListener(onSearch());
        return true;
    }

    /**
     * Configurações para a aplicação tratar o evento Serach View.
     * @return
     */
    private SearchView.OnQueryTextListener onSearch() {
        return new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Usuário fez a busca
                toast("Buscar o texto: " + query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                // Mudou o texto digitado
                return false;
            }
        };
    }

    /**
     * Ações do menu.
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            toast("Clicou no Search!");
            return true;
        } else if (id == R.id.action_refresh) {
            toast("Clicou no Refresh!");
            return true;
        } else if (id == R.id.action_settings) {
            toast("Clicou no Settings!");
            return true;
        }
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
//        navDrawerFrag.setHeaderValues(view, R.id.listViewContainer, R.drawable.nav_drawer_header, R.drawable.ic_drawer, R.string.app_name, R.string.app_name);
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
        List<NavDrawerMenuItem> list = NavDrawerMenuItem.getList();
        NavDrawerMenuItem selectedItem = list.get(position);

        // Seleciona a linha
        this.listAdapter.setSelected(position, true);

        toast("Clicou no item: " + getString(selectedItem.title));

    }
}
