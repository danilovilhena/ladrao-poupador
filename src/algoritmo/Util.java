package algoritmo;

import java.util.Random;

public class Util {
    // Selecionar número aleatório dentre opções
    public static int selecionarAleatorio(int[] opcoes) {
        int rnd = new Random().nextInt(opcoes.length);
        return opcoes[rnd];
    }

    public static int indexOf(int[] arr, int element) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == element)
                return i;
        }

        return -1;
    }

    public static boolean contains(int[] arr, int element) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == element)
                return true;
        }

        return false;
    }
}
