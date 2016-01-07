package br.ufpi.newsufpi.util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import br.ufpi.newsufpi.controller.EventoController;
import br.ufpi.newsufpi.controller.NoticiaController;
import br.ufpi.newsufpi.model.Evento;
import br.ufpi.newsufpi.model.Noticia;

/**
 * Created by thasciano on 23/12/15.
 */
public class ServerConnection {
    private static NoticiaController noticiaController;
    private EventoController eventoController;
    private static List<Noticia> listaDeNoticias;
    private List<Evento> listaDeEventos;
    private static final String TAG = "ServerConnection";

    public static List<Noticia> getListaDeNoticias(Context context) {
        try {
        //List<Noticia> noticia = sincronizarNoticiasJson();

            return downloadNoticias();
        } catch (Exception e) {
            // TODO explicar exception
            Log.e(TAG, "Erro ao ler as noticias: " + e.getMessage(), e);
            return null;
        }
    }



    public ServerConnection(Context context) {
        noticiaController = new NoticiaController(context);
        this.eventoController = new EventoController(context);
    }

    public static List<Noticia> downloadNoticias() throws Exception {

        // String representando json ficticio simulando download de um
        // webservice
        /*String json = "[{\"id\":1,\"title\":\"Abertas as inscrições para o I Curso de Férias em Ciências Fisiológicas\",\"content\":\"No período de 20 a 28 de janeiro acontece o I Curso de Férias em Ciências Fisiológicas da Universidade Federal do Piauí (UFPI). O curso é promovido pelo Departamento de Biofísica e Fisiologia e pela Liga Acadêmica de Ciências Fisiológicas (Lacif-UFPI). As inscrições começam nesta segunda-feira (05) e serão realizadas até o dia 13/01. Os alunos concluintes receberão certificados de 56 horas/aulas, por meio da Pró-Reitoria de Extensão (PREX). Os interessados devem ter cursado a disciplina de Fisiologia. As matrículas serão realizadas na secretaria do Departamento de Biofísica e Fisiologia, os estudantes têm que entregar a seguinte documentação: I - Ficha de inscrição devidamente preenchida; II - Carta de interesse, feita pelo próprio discente, manifestando as razões pelas quais gostaria de participar do curso; II - Histórico escolar e atestado de matrícula (ou declaração de vínculo à instituição de ensino); IV - Termos de compromisso devidamente preenchido, (ANEXO II), assumindo o compromisso do comparecimento integral às aulas;\", \"date\":\"13/01/2015\", \"images\":[\"http://www.ufpi.br/arquivos/Image/curso%20de%20ferias%20ciencias%20fisiologicias.png\"]}, "
                + "{\"id\":2,\"title\":\"PARFOR-UFPI realiza Seminário de abertura das atividades acadêmicas\",\"content\":\"Aconteceu na manhã de hoje (05), no Cine Teatro da Universidade Federal do Piauí (UFPI), a abertura do Seminário de Abertura das Atividades Acadêmicas dos Cursos Especiais Presenciais do Plano Nacional de Formação de Professores da Educação Básica (PARFOR-UFPI). O evento tem como objetivo promover uma formação sólida, contextualizada, ampla e multidisciplinar de professores da Educação Básica em exercício na rede pública do Estado do Piauí. O Seminário contou com a apresentação artística da Prof.ª Dra. Érica Fontes, Coordenadora do Curso de Inglês do Parfor, com composição da mesa de honra e ainda com a palestra Conhecimento e atuação docente: qual é a formação que se busca?, ministrada pelo Prof. Ms. Francisco Williams Gonçalves. Para finalizar a cerimônia, foi realizada mesa-redonda composta pelos coordenadores do PARFOR/UFPI. A mesa de honra foi formada pelo Reitor da UFPI, Prof. Dr. José Arimatéia Dantas Lopes; Pró-Reitora de Assuntos Estudantis e Acadêmicos (PRAEC), Prof.ª Dra. Cristiane Batista; Coordenadora do PARFOR, Prof.ª Ms. Glória Ferro; Coordenador Adjunto do PARFOR, Prof. Ms. José Ribamar Lopes; Palestrante do Evento, Prof. Ms. Francisco Williams Gonçalves; e representando os professores vinculados ao PARFOR, a Prof.ª Dra. Guiomar de Oliveira Passos. Para a Coordenadora do PARFOR, Prof.ª Ms. Glória Ferro, o Seminário é de extrema importância para os alunos e professores que compõem o Plano. Este Seminário tem o propósito inicial de inserir cada aluno e aluna do PARFOR na cultura acadêmica ufpiana. Este momento foi pensado para que possamos fomentar ainda mais essa reflexão, esse pensar, sobre o conhecimento que nós estamos adquirindo através do PARFOR da UFPI e da nossa atuação docente, enfatizou a Coordenadora. Durante a cerimônia, o Reitor da UFPI, Prof. Dr. José Arimatéia, destacou os esforços da Administração Superior para atender às demandas do PARFOR. Considero o PARFOR um dos programas mais importantes desenvolvidos pelo Governo Federal, e que tem todo o nosso apoio na Universidade Federal do Piauí. Desde que assumimos estamos trabalhando conjuntamente com a Professora Glória Ferro, atendendo todas as demandas que nos são apresentadas para o programa. Pode ter certeza que não faltará empenho, não faltará determinação da Administração Superior para que o PARFOR continue exercendo suas atividades da melhor forma possível, declarou. O evento tem continuidade a partir das 14h, com orientações gerais para utilização do SIGAA e para oferta de cursos especiais presenciais do PARFOR. Durante a cerimônia de encerramento será distribuído material pedagógico para os alunos do Plano.\", \"date\":\"05/01/2015\", \"images\":[\"http://www.ufpi.br/arquivos/20150105/jpg_20150105112215_52.jpg\", \"http://www.ufpi.br/arquivos/Image/2014/DEZEMBRO/_MG_0631%20(Copy)(1).jpg\", \"http://www.ufpi.br/arquivos/Image/2014/DEZEMBRO/_MG_0598%20(Copy).jpg\", \"http://www.ufpi.br/arquivos/Image/2014/DEZEMBRO/_MG_0624%20(Copy).jpg\"]},"
                + "{\"id\":3,\"title\":\"Reitor participa de transmissão do cargo do novo ministro da Educação\",\"content\":\"O Reitor da Universidade Federal do Piauí, Prof. Dr. José Arimatéia Dantas Lopes, participou da cerimônia de transmissão do cargo do novo ministro da Educação, Cid Gomes (PROS), realizada na última sexta-feira (02), no Ministério da Educação. A solenidade contou com a presença de ministros, parlamentares, governadores, secretários de estado e reitores de universidades federais. Estiveram presentes o governador Wellington Dias (PT), a deputada federal e futura secretária estadual da Educação, Rejane Dias (PT), o senador Elmano Férrer (PTB) e o presidente do Banco do Nordeste, o piauiense Nelson Souza. Cid Gomes Natural de Sobral, município do sertão cearense, Cid Gomes começou a militar na política na década de 1980, época em que cursava engenharia na Universidade Federal do Ceará (UFC). O primeiro cargo público de Cid Gomes foi conquistado em 1990 - uma cadeira na Assembleia Legislativa do Ceará. Reeleito quatro anos mais tarde, ele comandou o Legislativo cearense em seu segundo mandato. Em 1996, foi eleito, no primeiro turno, prefeito de Sobral, dando continuidade à tradição dos Ferreira Gomes na prefeitura da cidade. O pai, José Euclides Ferreira, comandou o município do sertão cearense entre 1977 a 1983. Em 2000, Cid Gomes se reelegeu para o cargo. Ao final do mandato de prefeito, Cid Gomes interrompeu a carreira política para trabalhar como consultor do Banco Interamericano de Desenvolvimento (BID), em Washington (EUA). Retornou ao Brasil, em 2006, para concorrer ao governo do Ceará. Em 2010 foi reeleito. Confira o vídeo da transmissão do cargo: http://centraldemidia.mec.gov.br/index.php\", \"date\":\"05/01/2015\", \"images\":[\"http://www.ufpi.br/arquivos/Image/posse%20ministro%20da%20educacao.jpg\", \"http://www.ufpi.br/arquivos/Image/posse%20ministro%20da%20educacao.jpg\"]},"
                + "{\"id\":4,\"title\":\"Aulas do Curso de Verão em Análise Real iniciam dia 12/01\",\"content\":\"A Coordenação do Mestrado Acadêmico em Matemática informa que as aulas do Curso de Verão em Análise Real, marcadas para iniciar dia 05/01, serão adiadas e terão início somente dia 12/01. Para mais informações, entrar em contato pelo e-mail pgmat@ufpi.edu.br ou pelo telefone (86) 3237 1609.\", \"date\":\"05/01/2015\", \"images\":[ ]},"
                + "{\"id\":5,\"title\":\"Seminário abre atividades acadêmicas dos cursos do PARFOR/ UFPI\",\"content\":\"A Universidade Federal do Piauí por meio da Pró-Reitora de Ensino de Graduação e da Coordenação Geral do PARFOR, convida alunos e professores vinculados ao programa para participarem do Seminário de Abertura das Atividades Acadêmicas dos Cursos Especiais Presenciais do PARFOR/UFPI - período letivo 2014/2, a ser realizado em 05/01/2015(segunda-feira), no Cine Teatro da UFPI (Campus Ministro Petrônio Portella, em Teresina), às 8 horas. A solenidade contará com a presença do magnífico reitor da UFPI, Prof. Dr. José Arimatéia Dantas Lopes, da Pró-Reitora de Ensino de Graduação, Profa. Dra. Maria do Socorro Leal Lopes e representantes da administração superior da Universidade. O evento ocorrerá durante todo o dia. No período da manhã, será apresentada a palestra Conhecimento e atuação docente: qual é a formação que se busca?, que será proferida pelo Prof. Ms. Francisco Williams de Assis Soares Gonçalves, e a mesa-redonda Orientações gerais para a oferta dos cursos especiais do PARFOR/UFPI, com moderação da Prof.ª Dr.ª Gardene Maria de Sousa. No horário das 14 às 18 horas, os participantes terão orientações gerais para utilização do SIGAA (Sistema de Gestão de Atividades Acadêmicas) e receberão material didático/pedagógico.\", \"date\":\"05/01/2015\", \"images\":[ ]},"
                + "{\"id\":6,\"title\":\"Mais Cultura nas Universidades: Prorrogado prazo para envio de propostas\",\"content\":\"A Pró-Reitoria de Extensão da Universidade Federal do Piauí, por meio da Comissão de Cultura da UFPI, informa que o prazo para envio de propostas referentes ao edital Mais Cultura nas Universidades foi prorrogado até o dia 9 de janeiro de 2015. O edital Mais Cultura nas Universidades tem como objetivo apresentar ao MEC/Minc uma proposta única do Plano de Cultura da UFPI. Para isso, a comunidade acadêmica deve encaminhar propostas segundo exigências e formulários do edital para serem analisadas e refletidas pela Comissão de Cultura até a condição de que tais propostas, possam ser incorporadas à Proposta Única da Instituição, segundo alinhamento e compatibilidade das mesmas. A Pró-Reitoria de Extensão orienta a todos que utilizem o Anexo I do edital, para a escrita de suas propostas, e que as encaminhem à PREX/UFPI no e-mail institucional: prex@ufpi.edu.br.\", \"date\":\"09/01/2015\", \"images\":[ ]}]";
        ;*/

        String json = "[{\"id\":1,\"title\":\"Abertas as inscrições para o I Curso de Férias em Ciências Fisiológicas\",\"content\":\"No período de 20 a 28 de janeiro acontece o I Curso de Férias em Ciências Fisiológicas da Universidade Federal do Piauí (UFPI). O curso é promovido pelo Departamento de Biofísica e Fisiologia e pela Liga Acadêmica de Ciências Fisiológicas (Lacif-UFPI). As inscrições começam nesta segunda-feira (05) e serão realizadas até o dia 13/01. Os alunos concluintes receberão certificados de 56 horas/aulas, por meio da Pró-Reitoria de Extensão (PREX). Os interessados devem ter cursado a disciplina de Fisiologia. As matrículas serão realizadas na secretaria do Departamento de Biofísica e Fisiologia, os estudantes têm que entregar a seguinte documentação: I - Ficha de inscrição devidamente preenchida; II - Carta de interesse, feita pelo próprio discente, manifestando as razões pelas quais gostaria de participar do curso; II - Histórico escolar e atestado de matrícula (ou declaração de vínculo à instituição de ensino); IV - Termos de compromisso devidamente preenchido, (ANEXO II), assumindo o compromisso do comparecimento integral às aulas;\", \"date\":\"13/01/2015\", \"images\":[\"http://ufpi.edu.br/images/imagensNoticias/curso%20de%20ferias%20ciencias%20fisiologicias.png\"]}, "
                + "{\"id\":2,\"title\":\"PARFOR-UFPI realiza Seminário de abertura das atividades acadêmicas\",\"content\":\"Aconteceu na manhã de hoje (05), no Cine Teatro da Universidade Federal do Piauí (UFPI), a abertura do Seminário de Abertura das Atividades Acadêmicas dos Cursos Especiais Presenciais do Plano Nacional de Formação de Professores da Educação Básica (PARFOR-UFPI). O evento tem como objetivo promover uma formação sólida, contextualizada, ampla e multidisciplinar de professores da Educação Básica em exercício na rede pública do Estado do Piauí. O Seminário contou com a apresentação artística da Prof.ª Dra. Érica Fontes, Coordenadora do Curso de Inglês do Parfor, com composição da mesa de honra e ainda com a palestra Conhecimento e atuação docente: qual é a formação que se busca?, ministrada pelo Prof. Ms. Francisco Williams Gonçalves. Para finalizar a cerimônia, foi realizada mesa-redonda composta pelos coordenadores do PARFOR/UFPI. A mesa de honra foi formada pelo Reitor da UFPI, Prof. Dr. José Arimatéia Dantas Lopes; Pró-Reitora de Assuntos Estudantis e Acadêmicos (PRAEC), Prof.ª Dra. Cristiane Batista; Coordenadora do PARFOR, Prof.ª Ms. Glória Ferro; Coordenador Adjunto do PARFOR, Prof. Ms. José Ribamar Lopes; Palestrante do Evento, Prof. Ms. Francisco Williams Gonçalves; e representando os professores vinculados ao PARFOR, a Prof.ª Dra. Guiomar de Oliveira Passos. Para a Coordenadora do PARFOR, Prof.ª Ms. Glória Ferro, o Seminário é de extrema importância para os alunos e professores que compõem o Plano. Este Seminário tem o propósito inicial de inserir cada aluno e aluna do PARFOR na cultura acadêmica ufpiana. Este momento foi pensado para que possamos fomentar ainda mais essa reflexão, esse pensar, sobre o conhecimento que nós estamos adquirindo através do PARFOR da UFPI e da nossa atuação docente, enfatizou a Coordenadora. Durante a cerimônia, o Reitor da UFPI, Prof. Dr. José Arimatéia, destacou os esforços da Administração Superior para atender às demandas do PARFOR. Considero o PARFOR um dos programas mais importantes desenvolvidos pelo Governo Federal, e que tem todo o nosso apoio na Universidade Federal do Piauí. Desde que assumimos estamos trabalhando conjuntamente com a Professora Glória Ferro, atendendo todas as demandas que nos são apresentadas para o programa. Pode ter certeza que não faltará empenho, não faltará determinação da Administração Superior para que o PARFOR continue exercendo suas atividades da melhor forma possível, declarou. O evento tem continuidade a partir das 14h, com orientações gerais para utilização do SIGAA e para oferta de cursos especiais presenciais do PARFOR. Durante a cerimônia de encerramento será distribuído material pedagógico para os alunos do Plano.\", \"date\":\"05/01/2015\", \"images\":[\"http://www.ufpi.edu.br/images/imagensNoticias/not%C3%ADcias/DSC_0196_.jpg\", \"http://www.ufpi.edu.br/images/imagensNoticias/not%C3%ADcias/PARFOR_2016_-__8_of_11.jpg\", \"http://www.ufpi.edu.br/images/imagensNoticias/not%C3%ADcias/PARFOR_2016_-__6_of_11.jpg\", \"http://www.ufpi.edu.br/images/imagensNoticias/not%C3%ADcias/DSC_0202_.jpg\", \"http://www.ufpi.edu.br/images/imagensNoticias/not%C3%ADcias/PARFOR_2016_-__5_of_11.jpg\"]},"
                + "{\"id\":3,\"title\":\"Reitor participa de transmissão do cargo do novo ministro da Educação\",\"content\":\"O Reitor da Universidade Federal do Piauí, Prof. Dr. José Arimatéia Dantas Lopes, participou da cerimônia de transmissão do cargo do novo ministro da Educação, Cid Gomes (PROS), realizada na última sexta-feira (02), no Ministério da Educação. A solenidade contou com a presença de ministros, parlamentares, governadores, secretários de estado e reitores de universidades federais. Estiveram presentes o governador Wellington Dias (PT), a deputada federal e futura secretária estadual da Educação, Rejane Dias (PT), o senador Elmano Férrer (PTB) e o presidente do Banco do Nordeste, o piauiense Nelson Souza. Cid Gomes Natural de Sobral, município do sertão cearense, Cid Gomes começou a militar na política na década de 1980, época em que cursava engenharia na Universidade Federal do Ceará (UFC). O primeiro cargo público de Cid Gomes foi conquistado em 1990 - uma cadeira na Assembleia Legislativa do Ceará. Reeleito quatro anos mais tarde, ele comandou o Legislativo cearense em seu segundo mandato. Em 1996, foi eleito, no primeiro turno, prefeito de Sobral, dando continuidade à tradição dos Ferreira Gomes na prefeitura da cidade. O pai, José Euclides Ferreira, comandou o município do sertão cearense entre 1977 a 1983. Em 2000, Cid Gomes se reelegeu para o cargo. Ao final do mandato de prefeito, Cid Gomes interrompeu a carreira política para trabalhar como consultor do Banco Interamericano de Desenvolvimento (BID), em Washington (EUA). Retornou ao Brasil, em 2006, para concorrer ao governo do Ceará. Em 2010 foi reeleito. Confira o vídeo da transmissão do cargo: http://centraldemidia.mec.gov.br/index.php\", \"date\":\"05/01/2015\", \"images\":[\"http://ufpi.edu.br/images/imagensNoticias/posse%20ministro%20da%20educacao.jpg\"]},"
                + "{\"id\":4,\"title\":\"Aulas do Curso de Verão em Análise Real iniciam dia 12/01\",\"content\":\"A Coordenação do Mestrado Acadêmico em Matemática informa que as aulas do Curso de Verão em Análise Real, marcadas para iniciar dia 05/01, serão adiadas e terão início somente dia 12/01. Para mais informações, entrar em contato pelo e-mail pgmat@ufpi.edu.br ou pelo telefone (86) 3237 1609.\", \"date\":\"05/01/2015\", \"images\":[ ]},"
                + "{\"id\":5,\"title\":\"Seminário abre atividades acadêmicas dos cursos do PARFOR/ UFPI\",\"content\":\"A Universidade Federal do Piauí por meio da Pró-Reitora de Ensino de Graduação e da Coordenação Geral do PARFOR, convida alunos e professores vinculados ao programa para participarem do Seminário de Abertura das Atividades Acadêmicas dos Cursos Especiais Presenciais do PARFOR/UFPI - período letivo 2014/2, a ser realizado em 05/01/2015(segunda-feira), no Cine Teatro da UFPI (Campus Ministro Petrônio Portella, em Teresina), às 8 horas. A solenidade contará com a presença do magnífico reitor da UFPI, Prof. Dr. José Arimatéia Dantas Lopes, da Pró-Reitora de Ensino de Graduação, Profa. Dra. Maria do Socorro Leal Lopes e representantes da administração superior da Universidade. O evento ocorrerá durante todo o dia. No período da manhã, será apresentada a palestra Conhecimento e atuação docente: qual é a formação que se busca?, que será proferida pelo Prof. Ms. Francisco Williams de Assis Soares Gonçalves, e a mesa-redonda Orientações gerais para a oferta dos cursos especiais do PARFOR/UFPI, com moderação da Prof.ª Dr.ª Gardene Maria de Sousa. No horário das 14 às 18 horas, os participantes terão orientações gerais para utilização do SIGAA (Sistema de Gestão de Atividades Acadêmicas) e receberão material didático/pedagógico.\", \"date\":\"05/01/2015\", \"images\":[ ]},"
                + "{\"id\":6,\"title\":\"Mais Cultura nas Universidades: Prorrogado prazo para envio de propostas\",\"content\":\"A Pró-Reitoria de Extensão da Universidade Federal do Piauí, por meio da Comissão de Cultura da UFPI, informa que o prazo para envio de propostas referentes ao edital Mais Cultura nas Universidades foi prorrogado até o dia 9 de janeiro de 2015. O edital Mais Cultura nas Universidades tem como objetivo apresentar ao MEC/Minc uma proposta única do Plano de Cultura da UFPI. Para isso, a comunidade acadêmica deve encaminhar propostas segundo exigências e formulários do edital para serem analisadas e refletidas pela Comissão de Cultura até a condição de que tais propostas, possam ser incorporadas à Proposta Única da Instituição, segundo alinhamento e compatibilidade das mesmas. A Pró-Reitoria de Extensão orienta a todos que utilizem o Anexo I do edital, para a escrita de suas propostas, e que as encaminhem à PREX/UFPI no e-mail institucional: prex@ufpi.edu.br.\", \"date\":\"09/01/2015\", \"images\":[ ]}]";
        ;

        // formatando a data
        GsonBuilder builder = new GsonBuilder();
        builder.setDateFormat("dd/MM/yyyy");

        // Criando uma lista de noticias a partir do json
        Gson gson = builder.create();
        Type listType = new TypeToken<List<Noticia>>() {
        }.getType();
        listaDeNoticias = gson.fromJson(json, listType);

        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    // diretorio onde vamos salvar
                    File root = Environment.getExternalStorageDirectory();
                    File dir = new File(root.getAbsolutePath()
                            + "/newsUfpi/img");
                    // cria a pasta se ela não existir
                    dir.mkdirs();

                    // Realizando o download das imagens das noticias e salvando
                    // no cartao
                    // de memoria
                    for (Noticia noticia : listaDeNoticias) {

                        List<String> path = new ArrayList<String>();
                        for (int i = 0; i < noticia.getImages().size(); i++) {

                            // url da imagem
                            URL url = new URL(noticia.getImages().get(i));
                            // abrindo conexão com a url
                            HttpURLConnection c = (HttpURLConnection) url
                                    .openConnection();
                            c.setRequestMethod("GET");
                            c.setDoOutput(true);

                            // recebendo o arquivo referente a url
                            InputStream in = c.getInputStream();
                            FileOutputStream fileOutputStream = new FileOutputStream(new File(dir, "img_ntc_" + noticia.getId() + i + ".jpg"));
                            //FileOutputStream fileOutputStream = new FileOutputStream(new File(dir, "img_ntc_" + noticia.getId()+ "_" + i + ".png"));

                            // salvando a imagem no cartao de memoria
                            byte[] buffer = new byte[1024];
                            int len1 = 0;
                            while ((len1 = in.read(buffer)) > 0) {
                                fileOutputStream.write(buffer, 0, len1);
                            }
                            fileOutputStream.close();
                            // alterando o path da imagem da noticia que será
                            // salvo
                            // na
                            // tabela noticias da aplicação
                            //path.add(dir.getAbsolutePath() + "/img_ntc_" + noticia.getId() + "_" + i + ".png");
                            path.add(dir.getAbsolutePath() + "/img_ntc_" + noticia.getId() + "_" + i + ".jpg");
                        }
                        noticia.setImages(path);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        // inserindo as noticias no banco de dados
        noticiaController.insertNotices(listaDeNoticias);
        return listaDeNoticias;
    }

    /**
     * Método que Manipula o Json de Notícias.
     */
    public static List<Noticia> sincronizarNoticiasJson() {
        String json = "[{\"id\":1,\"title\":\"Abertas as inscrições para o I Curso de Férias em Ciências Fisiológicas\",\"content\":\"No período de 20 a 28 de janeiro acontece o I Curso de Férias em Ciências Fisiológicas da Universidade Federal do Piauí (UFPI). O curso é promovido pelo Departamento de Biofísica e Fisiologia e pela Liga Acadêmica de Ciências Fisiológicas (Lacif-UFPI). As inscrições começam nesta segunda-feira (05) e serão realizadas até o dia 13/01. Os alunos concluintes receberão certificados de 56 horas/aulas, por meio da Pró-Reitoria de Extensão (PREX). Os interessados devem ter cursado a disciplina de Fisiologia. As matrículas serão realizadas na secretaria do Departamento de Biofísica e Fisiologia, os estudantes têm que entregar a seguinte documentação: I - Ficha de inscrição devidamente preenchida; II - Carta de interesse, feita pelo próprio discente, manifestando as razões pelas quais gostaria de participar do curso; II - Histórico escolar e atestado de matrícula (ou declaração de vínculo à instituição de ensino); IV - Termos de compromisso devidamente preenchido, (ANEXO II), assumindo o compromisso do comparecimento integral às aulas;\", \"date\":\"13/01/2015\", \"images\":[\"http://www.ufpi.br/arquivos/Image/curso%20de%20ferias%20ciencias%20fisiologicias.png\"]}, "
                + "{\"id\":2,\"title\":\"PARFOR-UFPI realiza Seminário de abertura das atividades acadêmicas\",\"content\":\"Aconteceu na manhã de hoje (05), no Cine Teatro da Universidade Federal do Piauí (UFPI), a abertura do Seminário de Abertura das Atividades Acadêmicas dos Cursos Especiais Presenciais do Plano Nacional de Formação de Professores da Educação Básica (PARFOR-UFPI). O evento tem como objetivo promover uma formação sólida, contextualizada, ampla e multidisciplinar de professores da Educação Básica em exercício na rede pública do Estado do Piauí. O Seminário contou com a apresentação artística da Prof.ª Dra. Érica Fontes, Coordenadora do Curso de Inglês do Parfor, com composição da mesa de honra e ainda com a palestra Conhecimento e atuação docente: qual é a formação que se busca?, ministrada pelo Prof. Ms. Francisco Williams Gonçalves. Para finalizar a cerimônia, foi realizada mesa-redonda composta pelos coordenadores do PARFOR/UFPI. A mesa de honra foi formada pelo Reitor da UFPI, Prof. Dr. José Arimatéia Dantas Lopes; Pró-Reitora de Assuntos Estudantis e Acadêmicos (PRAEC), Prof.ª Dra. Cristiane Batista; Coordenadora do PARFOR, Prof.ª Ms. Glória Ferro; Coordenador Adjunto do PARFOR, Prof. Ms. José Ribamar Lopes; Palestrante do Evento, Prof. Ms. Francisco Williams Gonçalves; e representando os professores vinculados ao PARFOR, a Prof.ª Dra. Guiomar de Oliveira Passos. Para a Coordenadora do PARFOR, Prof.ª Ms. Glória Ferro, o Seminário é de extrema importância para os alunos e professores que compõem o Plano. Este Seminário tem o propósito inicial de inserir cada aluno e aluna do PARFOR na cultura acadêmica ufpiana. Este momento foi pensado para que possamos fomentar ainda mais essa reflexão, esse pensar, sobre o conhecimento que nós estamos adquirindo através do PARFOR da UFPI e da nossa atuação docente, enfatizou a Coordenadora. Durante a cerimônia, o Reitor da UFPI, Prof. Dr. José Arimatéia, destacou os esforços da Administração Superior para atender às demandas do PARFOR. Considero o PARFOR um dos programas mais importantes desenvolvidos pelo Governo Federal, e que tem todo o nosso apoio na Universidade Federal do Piauí. Desde que assumimos estamos trabalhando conjuntamente com a Professora Glória Ferro, atendendo todas as demandas que nos são apresentadas para o programa. Pode ter certeza que não faltará empenho, não faltará determinação da Administração Superior para que o PARFOR continue exercendo suas atividades da melhor forma possível, declarou. O evento tem continuidade a partir das 14h, com orientações gerais para utilização do SIGAA e para oferta de cursos especiais presenciais do PARFOR. Durante a cerimônia de encerramento será distribuído material pedagógico para os alunos do Plano.\", \"date\":\"05/01/2015\", \"images\":[\"http://www.ufpi.br/arquivos/20150105/jpg_20150105112215_52.jpg\", \"http://www.ufpi.br/arquivos/Image/2014/DEZEMBRO/_MG_0631%20(Copy)(1).jpg\", \"http://www.ufpi.br/arquivos/Image/2014/DEZEMBRO/_MG_0598%20(Copy).jpg\", \"http://www.ufpi.br/arquivos/Image/2014/DEZEMBRO/_MG_0624%20(Copy).jpg\"]},"
                + "{\"id\":3,\"title\":\"Reitor participa de transmissão do cargo do novo ministro da Educação\",\"content\":\"O Reitor da Universidade Federal do Piauí, Prof. Dr. José Arimatéia Dantas Lopes, participou da cerimônia de transmissão do cargo do novo ministro da Educação, Cid Gomes (PROS), realizada na última sexta-feira (02), no Ministério da Educação. A solenidade contou com a presença de ministros, parlamentares, governadores, secretários de estado e reitores de universidades federais. Estiveram presentes o governador Wellington Dias (PT), a deputada federal e futura secretária estadual da Educação, Rejane Dias (PT), o senador Elmano Férrer (PTB) e o presidente do Banco do Nordeste, o piauiense Nelson Souza. Cid Gomes Natural de Sobral, município do sertão cearense, Cid Gomes começou a militar na política na década de 1980, época em que cursava engenharia na Universidade Federal do Ceará (UFC). O primeiro cargo público de Cid Gomes foi conquistado em 1990 - uma cadeira na Assembleia Legislativa do Ceará. Reeleito quatro anos mais tarde, ele comandou o Legislativo cearense em seu segundo mandato. Em 1996, foi eleito, no primeiro turno, prefeito de Sobral, dando continuidade à tradição dos Ferreira Gomes na prefeitura da cidade. O pai, José Euclides Ferreira, comandou o município do sertão cearense entre 1977 a 1983. Em 2000, Cid Gomes se reelegeu para o cargo. Ao final do mandato de prefeito, Cid Gomes interrompeu a carreira política para trabalhar como consultor do Banco Interamericano de Desenvolvimento (BID), em Washington (EUA). Retornou ao Brasil, em 2006, para concorrer ao governo do Ceará. Em 2010 foi reeleito. Confira o vídeo da transmissão do cargo: http://centraldemidia.mec.gov.br/index.php\", \"date\":\"05/01/2015\", \"images\":[\"http://www.ufpi.br/arquivos/Image/posse%20ministro%20da%20educacao.jpg\", \"http://www.ufpi.br/arquivos/Image/posse%20ministro%20da%20educacao.jpg\"]},"
                + "{\"id\":4,\"title\":\"Aulas do Curso de Verão em Análise Real iniciam dia 12/01\",\"content\":\"A Coordenação do Mestrado Acadêmico em Matemática informa que as aulas do Curso de Verão em Análise Real, marcadas para iniciar dia 05/01, serão adiadas e terão início somente dia 12/01. Para mais informações, entrar em contato pelo e-mail pgmat@ufpi.edu.br ou pelo telefone (86) 3237 1609.\", \"date\":\"05/01/2015\", \"images\":[ ]},"
                + "{\"id\":5,\"title\":\"Seminário abre atividades acadêmicas dos cursos do PARFOR/ UFPI\",\"content\":\"A Universidade Federal do Piauí por meio da Pró-Reitora de Ensino de Graduação e da Coordenação Geral do PARFOR, convida alunos e professores vinculados ao programa para participarem do Seminário de Abertura das Atividades Acadêmicas dos Cursos Especiais Presenciais do PARFOR/UFPI - período letivo 2014/2, a ser realizado em 05/01/2015(segunda-feira), no Cine Teatro da UFPI (Campus Ministro Petrônio Portella, em Teresina), às 8 horas. A solenidade contará com a presença do magnífico reitor da UFPI, Prof. Dr. José Arimatéia Dantas Lopes, da Pró-Reitora de Ensino de Graduação, Profa. Dra. Maria do Socorro Leal Lopes e representantes da administração superior da Universidade. O evento ocorrerá durante todo o dia. No período da manhã, será apresentada a palestra Conhecimento e atuação docente: qual é a formação que se busca?, que será proferida pelo Prof. Ms. Francisco Williams de Assis Soares Gonçalves, e a mesa-redonda Orientações gerais para a oferta dos cursos especiais do PARFOR/UFPI, com moderação da Prof.ª Dr.ª Gardene Maria de Sousa. No horário das 14 às 18 horas, os participantes terão orientações gerais para utilização do SIGAA (Sistema de Gestão de Atividades Acadêmicas) e receberão material didático/pedagógico.\", \"date\":\"05/01/2015\", \"images\":[ ]},"
                + "{\"id\":6,\"title\":\"Mais Cultura nas Universidades: Prorrogado prazo para envio de propostas\",\"content\":\"A Pró-Reitoria de Extensão da Universidade Federal do Piauí, por meio da Comissão de Cultura da UFPI, informa que o prazo para envio de propostas referentes ao edital Mais Cultura nas Universidades foi prorrogado até o dia 9 de janeiro de 2015. O edital Mais Cultura nas Universidades tem como objetivo apresentar ao MEC/Minc uma proposta única do Plano de Cultura da UFPI. Para isso, a comunidade acadêmica deve encaminhar propostas segundo exigências e formulários do edital para serem analisadas e refletidas pela Comissão de Cultura até a condição de que tais propostas, possam ser incorporadas à Proposta Única da Instituição, segundo alinhamento e compatibilidade das mesmas. A Pró-Reitoria de Extensão orienta a todos que utilizem o Anexo I do edital, para a escrita de suas propostas, e que as encaminhem à PREX/UFPI no e-mail institucional: prex@ufpi.edu.br.\", \"date\":\"09/01/2015\", \"images\":[ ]}]";
        ;

        Noticia noticiaJson;
        listaDeNoticias = new ArrayList<Noticia>();
        try {
            JSONArray ja = new JSONArray(json);
            ArrayList<String> im = new ArrayList<String>();
            JSONArray imagens;

            /*percorre toda a lista do JSONArray que contém a String json.*/
            for (int i = 0; i < ja.length(); i++) {
                noticiaJson = new Noticia();
                noticiaJson.setId(ja.getJSONObject(i).getInt("id"));
                noticiaJson.setTitle(ja.getJSONObject(i).getString("title"));
                noticiaJson.setContent(ja.getJSONObject(i).getString("content"));
                noticiaJson.setDate(ja.getJSONObject(i).getString("date"));
//                noticiaJson.set(ja.getJSONObject(i).getString("linkNoticia"));
                imagens = ja.getJSONObject(i).getJSONArray("images");
                for (int j = 0; j < imagens.length(); j++) {
                    noticiaJson.setImagens(imagens.getString(j));
                }
                listaDeNoticias.add(noticiaJson);
            }
            /**
             * Thread para baixar as imagens do site.
             */
            new Thread(new Runnable() {
                //**execução da Thread.

                @Override
                public void run() {
                    try {
                        // Diretorio onde as imagens vão ser salvadas.
                        File root = Environment.getExternalStorageDirectory();
                        File dir = new File(root.getAbsolutePath() + "/aneUFPI/img");
                        // cria a pasta, caso não não exista uma.
                        dir.mkdirs();
                        //StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                        //StrictMode.setThreadPolicy(policy);
                        // Realiza os downloads das imagens para suas respectivas noticias e salva no cartão de memoria. *//*
                        for (Noticia noticia : listaDeNoticias) {
                            ArrayList<String> pasta = new ArrayList<String>();
                            for (int i = 0; i < noticia.getImages().size(); i++) {
                                //* URL da imagem. *//*
                                URL url = new URL(noticia.getImages().get(i));
                                //* Abrindo uma conexão com a URL. *//*
                                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                                c.setRequestMethod("GET");
                                c.setDoOutput(true);

                                //* Recebendo o arquivo referente a URL. *//*
                                InputStream in = c.getInputStream();
                                FileOutputStream fileOutputStream = new FileOutputStream(new File(dir, "img_ntc_" + noticia.getId() + i + ".jpg"));
                                //* Salvando a imagem no cartão de memória. *//*
                                byte[] buffer = new byte[1024];
                                int l = 0;
                                while ((l = in.read(buffer)) > 0) {
                                    fileOutputStream.write(buffer, 0, l);
                                }
                                fileOutputStream.close();
                                /**
                                 * Alterando a pasta da imagem da notícia que será salvo na
                                 * tabela notícia da aplicação.
                                 * */
                                pasta.add(dir.getAbsolutePath() + "/img_ntc_" + noticia.getId() + "_" + i + ".jpg");
                            }
                            noticia.setImages(pasta);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            noticiaController.insertNotices(listaDeNoticias);
            return listaDeNoticias;
            /*Todas as notícias já manipuladas são inseridas no banco.*//*
            for (int i = 0; i < listaDeNoticias.size(); i++) {
                noticiaDAO.inserir(listaDeNoticias.get(i));
            }*/
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }
}
