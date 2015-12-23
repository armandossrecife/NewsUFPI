package br.ufpi.newsufpi.util;

import android.content.Context;

import java.util.List;

import br.ufpi.newsufpi.controller.EventoController;
import br.ufpi.newsufpi.controller.NoticiaController;
import br.ufpi.newsufpi.model.Evento;
import br.ufpi.newsufpi.model.Noticia;

/**
 * Created by thasciano on 23/12/15.
 */
public class ServerConnection {
    private NoticiaController noticiaController;
    private EventoController eventoController;
    private List<Noticia> noticias;
    private List<Evento> eventos;


    public ServerConnection(Context context) {
        this.noticiaController = new NoticiaController(context);
        this.eventoController = new EventoController(context);
    }

    public void downloadNoticias() throws Exception {
        // String representando json ficticio simulando download de um
        // webservice
        String json = "[{\"id\":1,\"title\":\"Abertas as inscrições para o I Curso de Férias em Ciências Fisiológicas\",\"content\":\"No período de 20 a 28 de janeiro acontece o I Curso de Férias em Ciências Fisiológicas da Universidade Federal do Piauí (UFPI). O curso é promovido pelo Departamento de Biofísica e Fisiologia e pela Liga Acadêmica de Ciências Fisiológicas (Lacif-UFPI). As inscrições começam nesta segunda-feira (05) e serão realizadas até o dia 13/01. Os alunos concluintes receberão certificados de 56 horas/aulas, por meio da Pró-Reitoria de Extensão (PREX). Os interessados devem ter cursado a disciplina de Fisiologia. As matrículas serão realizadas na secretaria do Departamento de Biofísica e Fisiologia, os estudantes têm que entregar a seguinte documentação: I - Ficha de inscrição devidamente preenchida; II - Carta de interesse, feita pelo próprio discente, manifestando as razões pelas quais gostaria de participar do curso; II - Histórico escolar e atestado de matrícula (ou declaração de vínculo à instituição de ensino); IV - Termos de compromisso devidamente preenchido, (ANEXO II), assumindo o compromisso do comparecimento integral às aulas;\", \"date\":\"13/01/2015\", \"images\":[\"http://www.ufpi.br/arquivos/Image/curso%20de%20ferias%20ciencias%20fisiologicias.png\"]}, "
                + "{\"id\":2,\"title\":\"PARFOR-UFPI realiza Seminário de abertura das atividades acadêmicas\",\"content\":\"Aconteceu na manhã de hoje (05), no Cine Teatro da Universidade Federal do Piauí (UFPI), a abertura do Seminário de Abertura das Atividades Acadêmicas dos Cursos Especiais Presenciais do Plano Nacional de Formação de Professores da Educação Básica (PARFOR-UFPI). O evento tem como objetivo promover uma formação sólida, contextualizada, ampla e multidisciplinar de professores da Educação Básica em exercício na rede pública do Estado do Piauí. O Seminário contou com a apresentação artística da Prof.ª Dra. Érica Fontes, Coordenadora do Curso de Inglês do Parfor, com composição da mesa de honra e ainda com a palestra Conhecimento e atuação docente: qual é a formação que se busca?, ministrada pelo Prof. Ms. Francisco Williams Gonçalves. Para finalizar a cerimônia, foi realizada mesa-redonda composta pelos coordenadores do PARFOR/UFPI. A mesa de honra foi formada pelo Reitor da UFPI, Prof. Dr. José Arimatéia Dantas Lopes; Pró-Reitora de Assuntos Estudantis e Acadêmicos (PRAEC), Prof.ª Dra. Cristiane Batista; Coordenadora do PARFOR, Prof.ª Ms. Glória Ferro; Coordenador Adjunto do PARFOR, Prof. Ms. José Ribamar Lopes; Palestrante do Evento, Prof. Ms. Francisco Williams Gonçalves; e representando os professores vinculados ao PARFOR, a Prof.ª Dra. Guiomar de Oliveira Passos. Para a Coordenadora do PARFOR, Prof.ª Ms. Glória Ferro, o Seminário é de extrema importância para os alunos e professores que compõem o Plano. Este Seminário tem o propósito inicial de inserir cada aluno e aluna do PARFOR na cultura acadêmica ufpiana. Este momento foi pensado para que possamos fomentar ainda mais essa reflexão, esse pensar, sobre o conhecimento que nós estamos adquirindo através do PARFOR da UFPI e da nossa atuação docente, enfatizou a Coordenadora. Durante a cerimônia, o Reitor da UFPI, Prof. Dr. José Arimatéia, destacou os esforços da Administração Superior para atender às demandas do PARFOR. Considero o PARFOR um dos programas mais importantes desenvolvidos pelo Governo Federal, e que tem todo o nosso apoio na Universidade Federal do Piauí. Desde que assumimos estamos trabalhando conjuntamente com a Professora Glória Ferro, atendendo todas as demandas que nos são apresentadas para o programa. Pode ter certeza que não faltará empenho, não faltará determinação da Administração Superior para que o PARFOR continue exercendo suas atividades da melhor forma possível, declarou. O evento tem continuidade a partir das 14h, com orientações gerais para utilização do SIGAA e para oferta de cursos especiais presenciais do PARFOR. Durante a cerimônia de encerramento será distribuído material pedagógico para os alunos do Plano.\", \"date\":\"05/01/2015\", \"images\":[\"http://www.ufpi.br/arquivos/20150105/jpg_20150105112215_52.jpg\", \"http://www.ufpi.br/arquivos/Image/2014/DEZEMBRO/_MG_0631%20(Copy)(1).jpg\", \"http://www.ufpi.br/arquivos/Image/2014/DEZEMBRO/_MG_0598%20(Copy).jpg\", \"http://www.ufpi.br/arquivos/Image/2014/DEZEMBRO/_MG_0624%20(Copy).jpg\"]},"
                + "{\"id\":3,\"title\":\"Reitor participa de transmissão do cargo do novo ministro da Educação\",\"content\":\"O Reitor da Universidade Federal do Piauí, Prof. Dr. José Arimatéia Dantas Lopes, participou da cerimônia de transmissão do cargo do novo ministro da Educação, Cid Gomes (PROS), realizada na última sexta-feira (02), no Ministério da Educação. A solenidade contou com a presença de ministros, parlamentares, governadores, secretários de estado e reitores de universidades federais. Estiveram presentes o governador Wellington Dias (PT), a deputada federal e futura secretária estadual da Educação, Rejane Dias (PT), o senador Elmano Férrer (PTB) e o presidente do Banco do Nordeste, o piauiense Nelson Souza. Cid Gomes Natural de Sobral, município do sertão cearense, Cid Gomes começou a militar na política na década de 1980, época em que cursava engenharia na Universidade Federal do Ceará (UFC). O primeiro cargo público de Cid Gomes foi conquistado em 1990 - uma cadeira na Assembleia Legislativa do Ceará. Reeleito quatro anos mais tarde, ele comandou o Legislativo cearense em seu segundo mandato. Em 1996, foi eleito, no primeiro turno, prefeito de Sobral, dando continuidade à tradição dos Ferreira Gomes na prefeitura da cidade. O pai, José Euclides Ferreira, comandou o município do sertão cearense entre 1977 a 1983. Em 2000, Cid Gomes se reelegeu para o cargo. Ao final do mandato de prefeito, Cid Gomes interrompeu a carreira política para trabalhar como consultor do Banco Interamericano de Desenvolvimento (BID), em Washington (EUA). Retornou ao Brasil, em 2006, para concorrer ao governo do Ceará. Em 2010 foi reeleito. Confira o vídeo da transmissão do cargo: http://centraldemidia.mec.gov.br/index.php\", \"date\":\"05/01/2015\", \"images\":[\"http://www.ufpi.br/arquivos/Image/posse%20ministro%20da%20educacao.jpg\", \"http://www.ufpi.br/arquivos/Image/posse%20ministro%20da%20educacao.jpg\"]},"
                + "{\"id\":4,\"title\":\"Aulas do Curso de Verão em Análise Real iniciam dia 12/01\",\"content\":\"A Coordenação do Mestrado Acadêmico em Matemática informa que as aulas do Curso de Verão em Análise Real, marcadas para iniciar dia 05/01, serão adiadas e terão início somente dia 12/01. Para mais informações, entrar em contato pelo e-mail pgmat@ufpi.edu.br ou pelo telefone (86) 3237 1609.\", \"date\":\"05/01/2015\", \"images\":[ ]},"
                + "{\"id\":5,\"title\":\"Seminário abre atividades acadêmicas dos cursos do PARFOR/ UFPI\",\"content\":\"A Universidade Federal do Piauí por meio da Pró-Reitora de Ensino de Graduação e da Coordenação Geral do PARFOR, convida alunos e professores vinculados ao programa para participarem do Seminário de Abertura das Atividades Acadêmicas dos Cursos Especiais Presenciais do PARFOR/UFPI - período letivo 2014/2, a ser realizado em 05/01/2015(segunda-feira), no Cine Teatro da UFPI (Campus Ministro Petrônio Portella, em Teresina), às 8 horas. A solenidade contará com a presença do magnífico reitor da UFPI, Prof. Dr. José Arimatéia Dantas Lopes, da Pró-Reitora de Ensino de Graduação, Profa. Dra. Maria do Socorro Leal Lopes e representantes da administração superior da Universidade. O evento ocorrerá durante todo o dia. No período da manhã, será apresentada a palestra Conhecimento e atuação docente: qual é a formação que se busca?, que será proferida pelo Prof. Ms. Francisco Williams de Assis Soares Gonçalves, e a mesa-redonda Orientações gerais para a oferta dos cursos especiais do PARFOR/UFPI, com moderação da Prof.ª Dr.ª Gardene Maria de Sousa. No horário das 14 às 18 horas, os participantes terão orientações gerais para utilização do SIGAA (Sistema de Gestão de Atividades Acadêmicas) e receberão material didático/pedagógico.\", \"date\":\"05/01/2015\", \"images\":[ ]},"
                + "{\"id\":6,\"title\":\"Mais Cultura nas Universidades: Prorrogado prazo para envio de propostas\",\"content\":\"A Pró-Reitoria de Extensão da Universidade Federal do Piauí, por meio da Comissão de Cultura da UFPI, informa que o prazo para envio de propostas referentes ao edital Mais Cultura nas Universidades foi prorrogado até o dia 9 de janeiro de 2015. O edital Mais Cultura nas Universidades tem como objetivo apresentar ao MEC/Minc uma proposta única do Plano de Cultura da UFPI. Para isso, a comunidade acadêmica deve encaminhar propostas segundo exigências e formulários do edital para serem analisadas e refletidas pela Comissão de Cultura até a condição de que tais propostas, possam ser incorporadas à Proposta Única da Instituição, segundo alinhamento e compatibilidade das mesmas. A Pró-Reitoria de Extensão orienta a todos que utilizem o Anexo I do edital, para a escrita de suas propostas, e que as encaminhem à PREX/UFPI no e-mail institucional: prex@ufpi.edu.br.\", \"date\":\"09/01/2015\", \"images\":[ ]}]";
        ;
        /*
        // formatando a data
        GsonBuilder builder = new GsonBuilder();
        builder.setDateFormat("dd/MM/yyyy");

        // Criando uma lista de noticias a partir do json
        Gson gson = builder.create();
        Type listType = new TypeToken<List<Noticia>>() {
        }.getType();
        noticias = gson.fromJson(json, listType);

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
                    for (Noticia noticia : noticias) {

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

                            FileOutputStream fileOutputStream = new FileOutputStream(
                                    new File(dir, "img_ntc_" + noticia.getId()
                                            + "_" + i + ".png"));

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
                            path.add(dir.getAbsolutePath() + "/img_ntc_"
                                    + noticia.getId() + "_" + i + ".png");
                        }
                        noticia.setImages(path);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        */
        // inserindo as noticias no banco de dados
        noticiaController.insertNotices(noticias);
    }

    public void downloadEventos() throws Exception {
        String json = "[{\"id\":1,\"title\":\"III Feira de Profissões da UFPI\",\"content\":\"A III Feira de Profissões da UFPI acontecerá nos dias 12 e 13 de maio de 2015 no Campus Ministro Petrônio Portela em Teresina.\", \"local\":\"Campus Ministro Petrônio Portella (Teresina)\", \"date\":\"12/05/2015\", \"images\":[\"http://www.ufpi.br/arquivos/Image/SEMINARIO_PRAEC.png\", \"http://www.ufpi.br/arquivos/Image/2014/DEZEMBRO/foto%202%20(Copy).jpg\"]},"
                + "{\"id\":2,\"title\":\"XVI Simpósio Brasileiro de Geografia Física Aplicada\",\"content\":\"Professores, alunos da Graduação e Pós-Graduação e a comunidade científica em geral estão convidados para o XVI SBGFA - Simpósio Brasileiro de Geografia Física Aplicada que acontecerá de 28 de junho a 04 de julho de 2015. O evento será realizado em Teresina e conta com o apoio das Universidades Federal e Estadual do Piauí (UFPI e UESPI) e do programa de Pós-Graduação em Geografia da UFPI - PPGGEO. O evento promoverá a discussão de assuntos relativos aos vários segmentos da Geografia Física.\", \"local\":\"Teresina - Piauí\", \"date\":\"28/05/2015\", \"images\":[\"http://www.ufpi.br/arquivos/Image/IMG_9847%20(Copy).jpg\"]}, "
                + "{\"id\":3,\"title\":\"Curso de oratória - ADUFPI\",\"content\":\"A Associação dos Docentes da Universidade Federal do Piauí (ADUFPI) promove o curso de oratória que será realizado nos dias 17 e 18 de janeiro . As inscrições para o curso estão abertas até o dia 16 de janeiro de 2015, na secretaria da ADUFPI. São 90 vagas disponíveis com certificação de 40 horas. O curso é destinado para todos os profissionais que buscam aprimorar suas carreiras. O telefone de contato é o (86) 3233-1110\", \"local\":\"Auditório da ADUFPI\", \"date\":\"17/01/2015\", \"images\":[]}, "
                + "{\"id\":4,\"title\":\"IV COGITE- Colóquio sobre Gêneros e Textos\",\"content\":\"A Universidade Federal do Piauí (UFPI) sedia dos dias 19 a 21 de Novembro o IV COGITE- Colóquio sobre Gêneros e Textos. O evento promove a discussão de pesquisas sobre o gênero de texto/discurso e suas relações com a vida social. Também agrega a função de criação de um espaço para divulgação das pesquisas individuais realizadas por professores, pesquisadores, alunos de graduação e de estudantes que realizam iniciação científica. O Colóquio tem como objetivos divulgar e rediscutir os fundamentos teóricos e as ferramentas metodológicas das principais teorias em voga para estudos de gêneros do texto/discurso, especialmente a Teoria Sociorretórica e a Teoria sócio-discursiva de viés bakhtiniano. Promover um espaço de discussão teórico-metodológica de questões que gravitam em torno das práticas sociais de linguagem e divulgar pesquisas atuais sobre as relações entre gêneros, textos e discurso. Os resumos para apresentação de trabalho (Comunicação e Pôster) devem ser enviados pelo site do evento entre os dias 20/07/2014 a 20/08/2014. Para conferir a Programação do Evento, Simpósios Temáticos realizar sua inscrição no IV COGITE acesse a site: http://coloquiocogite.com.br/ Para mais informações entre em contato pelo e-mail: informacoes.cogite@gmail.com\", \"local\":\"Centro de Ciências Humanas e Letras\", \"date\":\"19/11/2014\", \"images\":[]}, "
                + "{\"id\":5,\"title\":\"XXIII Seminário de Iniciação Científica da UFPI\",\"content\":\"O XXIII Seminário de Iniciação Científica da UFPI tem como objetivo divulgar os resultados das pesquisas desenvolvidas no Programa Institucional de Bolsas de Iniciação Científica (PIBIC) e Iniciação Científica Voluntária (ICV). O evento é voltado para docentes e discentes participantes dos programas no período de agosto de 2013 a julho de 2014. O evento acontecerá nos dias 19, 20 e 21 de novembro de 2014.\", \"local\":\"Campus Ministro Petrônio Portella (Teresina)\", \"date\":\"19/11/2014\", \"images\":[]}]";

        /*
        GsonBuilder builder = new GsonBuilder();
        builder.setDateFormat("dd/MM/yyyy");
        Gson gson = builder.create();
        Type listType = new TypeToken<List<Evento>>() {
        }.getType();
        eventos = gson.fromJson(json, listType);

        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    File root = Environment.getExternalStorageDirectory();
                    File dir = new File(root.getAbsolutePath() + "/newsUfpi/img");
                    dir.mkdirs();

                    for (Evento evento : eventos) {
                        List<String> path = new ArrayList<String>();
                        for (int i = 0; i < evento.getImagens().size(); i++) {
                            URL url = new URL(evento.getImagens().get(i));

                            HttpURLConnection c = (HttpURLConnection) url.openConnection();
                            c.setRequestMethod("GET");
                            c.setDoOutput(true);
                            // c.connect();
                            InputStream in = c.getInputStream();
                            FileOutputStream fileOutputStream = new FileOutputStream(
                                    new File(dir, "img_evt_" + evento.getId() + "_" + i
                                            + ".png"));

                            byte[] buffer = new byte[1024];
                            int len1 = 0;
                            while ((len1 = in.read(buffer)) > 0) {
                                fileOutputStream.write(buffer, 0, len1);
                            }
                            fileOutputStream.close();

                            path.add(dir.getAbsolutePath() + "/img_evt_" + evento.getId()
                                    + "_" + i + ".png");
                        }
                        evento.setImages(path);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        */
        eventoController.insertEvents(eventos);
    }
}
