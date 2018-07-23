/**
 * Pergunta referente: https://pt.stackoverflow.com/q/316014/132
 */
import java.util.function.Function;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Victor Williams Stafusa da Silva
 */
class Colorido {
    private void complica() {
        try {
            Thread.sleep((int) (Math.random() * 50));
        } catch (InterruptedException e) {
            // Ignora.
        }
    }
    public String getVermelho() { complica(); return "vermelho"; }
    public String getLaranja()  { complica(); return "laranja";  }
    public String getAmarelo()  { complica(); return "amarelo";  }
    public String getLima()     { complica(); return "lima";     }
    public String getVerde()    { complica(); return "verde";    }
    public String getCiano()    { complica(); return "ciano";    }
    public String getAzul()     { complica(); return "azul";     }
    public String getVioleta()  { complica(); return "violeta";  }
    public String getRosa()     { complica(); return "rosa";     }
    public String getRoxo()     { complica(); return "roxo";     }
    public String getBranco()   { complica(); return "branco";   }
    public String getPreto()    { complica(); return "preto";    }
    public String getMarrom()   { complica(); return "marrom";   }
    public String getOliva()    { complica(); return "oliva";    }
    public String getBege()     { complica(); return "bege";     }
    public String getCinza()    { complica(); return "cinza";    }
    public static List<Function<Colorido, String>> funcoes() {
        return Arrays.asList(
            Colorido::getVermelho, Colorido::getLaranja, Colorido::getAmarelo, Colorido::getLima,
            Colorido::getVerde, Colorido::getCiano, Colorido::getAzul, Colorido::getVioleta,
            Colorido::getRosa, Colorido::getRoxo, Colorido::getBranco, Colorido::getPreto,
            Colorido::getMarrom, Colorido::getOliva, Colorido::getBege, Colorido::getCinza
        );
    }
}

/**
 * @author Victor Williams Stafusa da Silva
 */
public class TesteMapReduce {
    public static void main(String[] args) {
        System.out.println("mapReduceSequencial:");
        System.out.println(mapReduceSequencial(Colorido.funcoes(), new Colorido()));
        
        System.out.println("\nmapReduceParalelo1:");
        System.out.println(mapReduceParalelo1(Colorido.funcoes(), new Colorido()));

        System.out.println("\nmapReduceParalelo2:");
        System.out.println(mapReduceParalelo2(Colorido.funcoes(), new Colorido()));

        System.out.println("\nmapCollectSequencial:");
        System.out.println(mapCollectSequencial(Colorido.funcoes(), new Colorido()));

        System.out.println("\nmapCollectParalelo:");
        System.out.println(mapCollectParalelo(Colorido.funcoes(), new Colorido()));
    }

    public static <T> List<String> mapReduceSequencial(
        List<Function<T, String>> funcoes, // inicializa com as diversas funções
        T objetoSobProcessamento) // parâmetro arbitrário
    {
        List<String> retornoProcessamento = funcoes
                .stream()
                .map(f -> f.apply(objetoSobProcessamento))
                .reduce(new ArrayList<>(), (listaAcumulada, novoValor) -> {
                    listaAcumulada.add(novoValor);
                    return listaAcumulada;
                }, (l1, l2) -> {
                    l1.addAll(l2);
                    return l1;
                });
        return Collections.unmodifiableList(retornoProcessamento);
    }

    public static <T> List<String> mapReduceParalelo1(
        List<Function<T, String>> funcoes, // inicializa com as diversas funções
        T objetoSobProcessamento) // parâmetro arbitrário
    {
        List<String> retornoProcessamento = funcoes
                .parallelStream()
                .map(f -> f.apply(objetoSobProcessamento))
                .reduce(new ArrayList<>(), (listaAcumulada, novoValor) -> {
                    listaAcumulada.add(novoValor);
                    return listaAcumulada;
                }, (l1, l2) -> {
                    l1.addAll(l2);
                    return l1;
                });
        return Collections.unmodifiableList(retornoProcessamento);
    }

    public static <T> List<String> mapReduceParalelo2(
        List<Function<T, String>> funcoes, // inicializa com as diversas funções
        T objetoSobProcessamento) // parâmetro arbitrário
    {
        List<String> retornoProcessamento = funcoes
                .parallelStream()
                .map(f -> f.apply(objetoSobProcessamento))
                .reduce(Collections.emptyList(), (listaAcumulada, novoValor) -> {
                    List<String> novaLista = listaAcumulada.isEmpty() ? new ArrayList<>() : listaAcumulada;
                    novaLista.add(novoValor);
                    return novaLista;
                }, (l1, l2) -> {
                    List<String> novaLista = new ArrayList<>(l1.size() + l2.size());
                    novaLista.addAll(l1);
                    novaLista.addAll(l2);
                    return novaLista;
                });
        return Collections.unmodifiableList(retornoProcessamento);
    }

    public static <T> List<String> mapCollectSequencial(
        List<Function<T, String>> funcoes, // inicializa com as diversas funções
        T objetoSobProcessamento) // parâmetro arbitrário
    {
        List<String> retornoProcessamento = funcoes
                .stream()
                .map(f -> f.apply(objetoSobProcessamento))
                .collect(ArrayList::new, List::add, List::addAll);
        return Collections.unmodifiableList(retornoProcessamento);
    }

    public static <T> List<String> mapCollectParalelo(
        List<Function<T, String>> funcoes, // inicializa com as diversas funções
        T objetoSobProcessamento) // parâmetro arbitrário
    {
        List<String> retornoProcessamento = funcoes
                .parallelStream()
                .map(f -> f.apply(objetoSobProcessamento))
                .collect(ArrayList::new, List::add, List::addAll);
        return Collections.unmodifiableList(retornoProcessamento);
    }
}