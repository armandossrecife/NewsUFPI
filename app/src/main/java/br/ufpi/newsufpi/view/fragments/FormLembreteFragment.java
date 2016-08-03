package br.ufpi.newsufpi.view.fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.ufpi.newsufpi.R;
import br.ufpi.newsufpi.broadcastReceiver.NotificationService;
import br.ufpi.newsufpi.model.Evento;
import br.ufpi.newsufpi.persistence.FacadeDao;

/**
 * Created by katia cibele on 18/02/2016.
 */
public class FormLembreteFragment extends BaseFragment {
    private Spinner spinner;

    private Button btnConcluido;
    private ImageButton btnVoltar;
    private RadioGroup radioGroup;
    private RadioButton radioUmaHora, radioUmDia, radioButtonLembrete;
    private TextView localView, dataView;
    List<Evento> eventos;
    private Evento eventoSelecionado;
    private ArrayList<String> op;

    private SpinnerAdapter spinnerAdapter;
    private FacadeDao dadosBB = new FacadeDao(getContext());

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.form_lembrete_fragment, container, false);

        localView = (TextView) view.findViewById(R.id.textViewSetLocal);
        dataView = (TextView) view.findViewById(R.id.textViewSetData);

        //tratando spinner

        spinner = (Spinner) view.findViewById(R.id.spinnerLembreteEvento);

        spinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());

        //popula spinner


        eventos = dadosBB.listAllEvents();
        //cria estrutura de array adapter para inserir strings no componente spinner

        op = new ArrayList<String>();
        for (int i = 0; i < eventos.size(); i++) {
            op.add(eventos.get(i).getTitle());
        }
        ArrayAdapter<String> eventosAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, op);

        spinner.setAdapter(eventosAdapter);


        //setando radio group

        radioGroup = (RadioGroup) view.findViewById(R.id.radioGroupLembrete);
        radioUmaHora = (RadioButton) view.findViewById(R.id.radioUmaHoraAntes);

        //setando buttons
        btnConcluido = (Button) view.findViewById(R.id.buttonConfirmaLembrete);


        btnVoltar = (ImageButton) view.findViewById(R.id.buttonCancelaLembrete);

        btnConcluido.setOnClickListener(new View.OnClickListener() {
                                            // caso comfirmação para insercao  de dados
                                            public void onClick(View v) {
                                                // da get no item selecionado pelo usuario e nas demais nformações para criar lembrete
                                                int selectedId = radioGroup.getCheckedRadioButtonId();
                                                radioButtonLembrete = (RadioButton) view.findViewById(selectedId);

                                                //  Toast.makeText(getActivity(),
                                                //        radioButtonLembrete.getText(), Toast.LENGTH_SHORT).show();
                                                spinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());

                                                //evento selecionado pelo spinner para poder gravar no banco
                                                //eventoSelecionado

                                                Toast.makeText(getActivity(), " Salvo com sucesso", Toast.LENGTH_SHORT).show();
                                                gerarNotificacao();
                                                replaceFragment(new LembreteFragment());
                                            }
                                        }
        );

        btnVoltar.setOnClickListener(new View.OnClickListener() {
                                         // caso comfirmação para insercao  de dados
                                         public void onClick(View v) {
                                             // da get no item selecionado pelo usuario e nas demais nformações para criar lembrete
                                             replaceFragment(new LembreteFragment());
                                         }
                                     }
        );


        return view;
    }

    public void gerarNotificacao() {
        AlarmManager manager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getContext(), NotificationService.class);
        int valor = (radioGroup.getCheckedRadioButtonId() == radioUmaHora.getId()) ? 0 : 1;
        long when = 0L;
        switch (valor) {
            case 0:
                when = 3600000L;
                break;
            case 1:
                when = 86400000L;
        }
        intent.putExtra("message", valor);
        intent.putExtra("id", eventoSelecionado.getId());
        if (new Date(eventoSelecionado.getDataInicio().getTime() - when).before(new Date())) {
            Toast.makeText(getContext(), "Não é possível configurar alarme para um evento passado", Toast.LENGTH_SHORT).show();
        } else {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), (int) System.currentTimeMillis(), intent, 0);
            NotificationService.alarms.put(eventoSelecionado.getId().toString(), pendingIntent);
            manager.set(AlarmManager.RTC_WAKEUP, eventoSelecionado.getDataInicio().getTime() - when, pendingIntent);
        }
    }

    public class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            // Toast.makeText(parent.getContext(),
            //          "OnItemSelectedListener : " + parent.getItemAtPosition(pos).toString(),
            //         Toast.LENGTH_SHORT).show();
            try {


                List<Evento> e = dadosBB.listAllEvents();
                for (Evento ev : e) {
                    if (ev.getTitle().compareTo(parent.getItemAtPosition(pos).toString()) == 0) {
                        eventoSelecionado = ev;
                    }
                }

                localView.setText(eventoSelecionado.getLocal());
                dataView.setText(eventoSelecionado.getDataInicioString());

            } catch (Exception e) {

            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub
        }

    }

    private void replaceFragment(Fragment frag) {
        this.getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_drawer_container, frag, "TAG").commit();
    }


}
