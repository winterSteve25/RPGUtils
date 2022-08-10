package wintersteve25.rpgutils.common.utils;

import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

public class RandomCollection<E> {
    private final NavigableMap<Float, E> map = new TreeMap<>();
    private final Random random;
    private float total = 0;

    public RandomCollection() {
        this(new Random());
    }

    public RandomCollection(Random random) {
        this.random = random;
    }

    public RandomCollection<E> add(float weight, E result) {
        if (weight <= 0) return this;
        total += weight;
        map.put(total, result);
        return this;
    }

    public E next() {
        float value = random.nextFloat() * total;
        return map.higherEntry(value).getValue();
    }
}