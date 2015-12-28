package br.ufpi.newsufpi.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import br.ufpi.newsufpi.R;
import br.ufpi.newsufpi.model.Noticia;
import livroandroid.lib.fragment.BaseFragment;

/**
 * Created by thasciano on 24/12/15.
 */
public class NoticiaFragment extends BaseFragment {
    private Noticia noticia;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_noticia, container, false);
//        setHasOptionsMenu(true);
        return view;
    }

    /*@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_frag_carro, menu);

//        MenuItem shareItem = menu.findItem(R.id.action_share);
//        ShareActionProvider share = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*if (item.getItemId() == R.id.action_edit) {
            //toast("Editar: " + carro.nome);
            EditarCarroDialog.show(getFragmentManager(), carro, new EditarCarroDialog.Callback() {
                @Override
                public void onCarroUpdated(Carro carro) {
                    toast("Carro [" + carro.nome + "] atualizado.");
                    CarrosApplication.getInstance().setPrecisaAtualizar(carro.tipo, true);
                    // Atualiza o título com o novo nome
                    getActionBar().setTitle(carro.nome);
                }
            });
            return true;
        } else if (item.getItemId() == R.id.action_remove) {
            //toast("Deletar: " + carro.nome);

            DeletarCarroDialog.show(getFragmentManager(), carro, new DeletarCarroDialog.Callback() {
                @Override
                public void onCarroDeleted(Carro carro) {
                    toast("Carro [" + carro.nome + "] deletado.");
                    CarrosApplication.getInstance().setPrecisaAtualizar(carro.tipo, true);
                    // Fecha a activity
                    getActivity().finish();
                }
            });

            return true;
        } else if (item.getItemId() == R.id.action_share) {
            toast("Compartilhar");
        } else if (item.getItemId() == R.id.action_maps) {
            toast("Mapa");
        } else if (item.getItemId() == R.id.action_video) {
            toast("Vídeo");
        }*/
        return super.onOptionsItemSelected(item);
    }

    // Método público chamado pela activity para atualizar os dados do carro
    public void setNoticia(Noticia noticia) {

        if(noticia != null) {
            this.noticia = noticia;
            //setTextString(R.id.tNome,carro.nome);
            setTextString(R.id.title_n, noticia.getTitle());

            setTextString(R.id.desc_n, noticia.getContent());

           /* final ImageView imgView = (ImageView) getView().findViewById(R.id.img);

            Picasso.with(getContext()).load(noticia.getImages().get(0)).fit().into(imgView);*/
        }
    }
}