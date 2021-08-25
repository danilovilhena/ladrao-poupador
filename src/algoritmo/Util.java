package algoritmo;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import estruturas.ColecaoAleatoria;

public class Util {
    // Selecionar número aleatório dentre opções
    public static int selecionarAleatorio(int[] opcoes) {
        int rnd = new Random().nextInt(opcoes.length);
        return opcoes[rnd];
    }

    public static double[] transformarEmProbabilidade(int[] resultados) {
        int smallest = 999;

        // Encontrar o menor valor
        for (int i = 0; i < resultados.length; i++) {
            if (resultados[i] < smallest) {
                smallest = resultados[i];
            }
        }

        // Adicionar o menor valor + 1 em todo mundo
        // Somar os valores na variável sum
        int sum = 0;
        for (int i = 0; i < resultados.length; i++) {
            resultados[i] += Math.abs(smallest) + 1;
            sum += resultados[i];
        }

        // Multiplicando os valores pela parte
        double part = 100 / (double) sum;
        double[] probabilidades = new double[4];
        for (int i = 0; i < resultados.length; i++) {
            probabilidades[i] = (resultados[i] * part) / 100;
        }

        return probabilidades;
    }

    public static int selecionarProbabilidade(double[] probs) {
        ColecaoAleatoria<Integer> dicionario = new ColecaoAleatoria<>();

        for (int i = 0; i < probs.length; i++) {
            dicionario.add(probs[i], i);
        }

        return dicionario.next();
    }

    public static int indexOf(int[] arr, int element) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == element)
                return i;
        }

        return -1;
    }

    public static int indexOfDouble(double[] arr, double element) {
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

    public static boolean isSamePoint(Point a, Point b) {
        if (((int) a.getX() == (int) b.getX()) && ((int) a.getY() == (int) b.getY())) {
            return true;
        } else
            return false;
    }

    public static boolean containsPoint(ArrayList<Point> arr, Point element) {
        for (int i = 0; i < arr.size(); i++) {
            if (isSamePoint(arr.get(i), element))
                return true;
        }

        return false;
    }
}
