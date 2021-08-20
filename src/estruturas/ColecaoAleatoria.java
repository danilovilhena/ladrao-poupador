package estruturas;

import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

public class ColecaoAleatoria<E> {
    private final NavigableMap<Double, E> map = new TreeMap<Double, E>();
    private final Random random;
    private double total = 0;

    public ColecaoAleatoria() {
        this(new Random());
    }

    public ColecaoAleatoria(Random random) {
        this.random = random;
    }

    public ColecaoAleatoria<E> add(double peso, E resultado) {
        if (peso <= 0)
            return this;
        total += peso;
        map.put(total, resultado);
        return this;
    }

    public E next() {
        double valor = random.nextDouble() * total;
        return map.higherEntry(valor).getValue();
    }
}
