package wintersteve25.rpgutils.common.entities.schedule;

import net.minecraft.world.IDayTimeReader;

public abstract class Schedule {
    abstract boolean isTime(IDayTimeReader timeReader);
}
