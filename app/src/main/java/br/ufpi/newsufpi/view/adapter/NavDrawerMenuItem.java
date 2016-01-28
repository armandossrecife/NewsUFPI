package br.ufpi.newsufpi.view.adapter;

import java.util.ArrayList;
import java.util.List;

import br.ufpi.newsufpi.R;

/**
 * Created by thasciano on 24/12/15.
 */
public class NavDrawerMenuItem {
    // Título: R.string.xxx
    public int title;
    // Figura: R.drawable.xxx
    public int img;
    // Para colocar um fundo cinza quando a linha está selecionada
    public boolean selected;

    public NavDrawerMenuItem(int title, int img) {
        this.title = title;
        this.img = img;
    }
    // Cria a lista com os itens de menu
    public static List<NavDrawerMenuItem> getList() {
        List<NavDrawerMenuItem> list = new ArrayList<NavDrawerMenuItem>();
        list.add(new NavDrawerMenuItem(R.string.menu_notice, R.mipmap.ic_newspaper_black_48dp));
        list.add(new NavDrawerMenuItem(R.string.menu_event, R.mipmap.ic_calendar_black_48dp));
        list.add(new NavDrawerMenuItem(R.string.menu_lembretes, R.mipmap.ic_alarm_black_48dp));
        list.add(new NavDrawerMenuItem(R.string.menu_favoritos, R.mipmap.ic_grade_black_48dp));
        //list.add(new NavDrawerMenuItem(R.string.menu_ufpiMaps, R.mipmap.ic_calendar_clock_black_48dp));
        list.add(new NavDrawerMenuItem(R.string.menu_contats, R.mipmap.ic_help_black_48dp));
        return list;
    }
}
