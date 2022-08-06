package wintersteve25.rpgutils.common.utils;

import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.IDataSerializer;
import net.minecraft.util.ResourceLocation;

public class DataUtils {
    public static final IDataSerializer<ResourceLocation> RESOURCE_LOCATION_SERIALIZER = new IDataSerializer<ResourceLocation>() {
        @Override
        public void write(PacketBuffer pBuffer, ResourceLocation pValue) {
            pBuffer.writeUtf(pValue.toString());
        }

        @Override
        public ResourceLocation read(PacketBuffer pBuffer) {
            return new ResourceLocation(pBuffer.readUtf(32767));
        }

        @Override
        public ResourceLocation copy(ResourceLocation pValue) {
            return new ResourceLocation(pValue.toString());
        }
    };
    
    public static void register() {
        DataSerializers.registerSerializer(RESOURCE_LOCATION_SERIALIZER);
    }
}
