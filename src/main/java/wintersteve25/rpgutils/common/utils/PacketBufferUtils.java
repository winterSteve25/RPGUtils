package wintersteve25.rpgutils.common.utils;

import net.minecraft.network.PacketBuffer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class PacketBufferUtils {

    public static <T> void writeList(List<T> list, PacketBuffer buffer, Consumer<T> writeFunction) {
        buffer.writeInt(list.size());
        list.forEach(writeFunction);
    }

    public static <T> List<T> readList(PacketBuffer buffer, Supplier<T> supplier) {
        int size = buffer.readInt();
        List<T> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(supplier.get());
        }
        return list;
    }

    public static <K, V> void writeMap(Map<K, V> map, PacketBuffer buffer, Consumer<K> keyConsumer, Consumer<V> valueConsumer) {
        buffer.writeInt(map.size());
        for (Map.Entry<K, V> entry : map.entrySet()) {
            keyConsumer.accept(entry.getKey());
            valueConsumer.accept(entry.getValue());
        }
    }

    public static <K, V> Map<K, V> readMap(PacketBuffer buffer, Supplier<K> keySupplier, Supplier<V> valueSupplier) {
        int size = buffer.readInt();
        Map<K, V> map = new HashMap<>();
        for (int i = 0; i < size; i++) {
            map.put(keySupplier.get(), valueSupplier.get());
        }
        return map;
    }
}
