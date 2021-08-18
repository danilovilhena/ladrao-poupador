package algoritmo;

import java.util.Random;

public class Util {
    // Selecionar número aleatório dentre opções
    public static int selecionarAleatorio(int[] opcoes) {
        int rnd = new Random().nextInt(opcoes.length);
        return opcoes[rnd];
    }
}
