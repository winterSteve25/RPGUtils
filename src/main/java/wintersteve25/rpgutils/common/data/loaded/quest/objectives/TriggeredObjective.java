package wintersteve25.rpgutils.common.data.loaded.quest.objectives;

import java.util.function.Predicate;

public abstract class TriggeredObjective<T> extends AbstractTriggeredObjective {
    
    private final Predicate<T> predicate;
    private final Class<T> clazz;

    public TriggeredObjective(Class<T> clazz, Predicate<T> predicate) {
        this.predicate = predicate;
        this.clazz = clazz;
    }

    @Override
    public void trigger(Object trigger) {
        if (clazz.isInstance(trigger)) {
            if (predicate.test((T) trigger)) {
                triggered();
            }
        }
    }
}
