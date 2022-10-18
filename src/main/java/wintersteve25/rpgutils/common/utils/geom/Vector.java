package wintersteve25.rpgutils.common.utils.geom;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Vector {
    
    private final List<Integer> values;
    
    public Vector(Integer... values) {
        this.values = Arrays.asList(values);
    }

    public Vector(BlockPos blockPos) {
        values = new ArrayList<>();
        Arrays.stream(Direction.Axis.values()).forEachOrdered(a -> values.add(blockPos.get(a)));
    }

    public Vector subtract(Vector other) {
        assertSizeEqual(other);
        Integer[] newList = new Integer[values.size()];
        for (int i = 0; i < values.size(); i++) {
            newList[i] = values.get(i) - other.values.get(i);
        }
        return new Vector(newList);
    }

    public Vector scale(float scale) {
        Integer[] newList = new Integer[values.size()];
        for (int i = 0; i < values.size(); i++) {
            newList[i] = (int) (values.get(i) * scale);
        }
        return new Vector(newList);
    }

    public int get(int index) {
        return values.get(index);
    }

    public BlockPos asBlockPos() {
        assertSizeEqual(3);
        return new BlockPos(get(0), get(1), get(2));
    }
    
    public Vector add(Vector other) {
        assertSizeEqual(other);
        Integer[] newList = new Integer[values.size()];
        for (int i = 0; i < values.size(); i++) {
            newList[i] = values.get(i) + other.values.get(i);
        }
        return new Vector(newList);
    }
    
    public boolean lessThan(Vector other) {
        assertSizeEqual(other);
        for (int i = 0; i < values.size(); i++) {
            if (values.get(i) >= other.values.get(i)) {
                return false;
            }
        }
        return true;
    }

    public boolean greaterThan(Vector other) {
        assertSizeEqual(other);
        for (int i = 0; i < values.size(); i++) {
            if (values.get(i) <= other.values.get(i)) {
                return false;
            }
        }
        return true;
    }

    private void assertSizeEqual(Vector other) {
        assertSizeEqual(other.values.size());
    }

    private void assertSizeEqual(int size) {
        if (size != values.size()) {
            throw new IllegalArgumentException("Invalid size for vector: " + values.size());
        }
    }
}
