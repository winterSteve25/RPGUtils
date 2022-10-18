package wintersteve25.rpgutils.common.utils.geom;

import net.minecraft.util.Direction;
import wintersteve25.rpgutils.common.utils.Counter;

import java.util.Iterator;

public class Cuboid implements Iterable<Vector> {

    private final Vector start;
    private final Vector size;

    public Cuboid(int x, int y, int z, int width, int height, int depth) {
        start = new Vector(x, y, z);
        size = new Vector(width, height, depth);
    }

    public boolean contains(Vector matrix) {
        return matrix.greaterThan(start) && matrix.lessThan(start.add(size));
    }

    public Vector getStart() {
        return start;
    }

    public Vector getSize() {
        return size;
    }

    public int MID(Direction.Axis axis) {
        return start.add(size.scale(0.5f)).get(indexOf(axis));
    }

    private static int indexOf(Direction.Axis axis) {
        if (axis == Direction.Axis.X) {
            return 0;
        } else if (axis == Direction.Axis.Y) {
            return 1;
        } else if (axis == Direction.Axis.Z) {
            return 2;
        }
        return -1;
    }

    @Override
    public Iterator<Vector> iterator() {
        return new Iterator<Vector>() {
            private final Counter counter = new Counter(size.get(0), size.get(1), size.get(2));

            @Override
            public boolean hasNext() {
                return counter.hasNext();
            }

            @Override
            public Vector next() {
                Integer[] coords = counter.next();
                return new Vector(coords);
            }
        };
    }
}
