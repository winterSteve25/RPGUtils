package wintersteve25.rpgutils.client.ui.quests.creator;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ITagCollection;
import net.minecraft.util.ResourceLocation;
import wintersteve25.rpgutils.client.ui.dialogues.components.selection.AbstractSelectionUI;
import wintersteve25.rpgutils.client.ui.dialogues.components.selection.SelectionOption;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class SelectTag<T> extends AbstractSelectionUI<SelectTag.SelectTagOption<T>> {

    private static ITagCollection<?> allTags;
    
    private SelectTag(boolean allowMultiple, Consumer<List<SelectTagOption<T>>> onSubmit) {
        super(allowMultiple, onSubmit);
    }

    @Override
    protected void populateOptions(List<SelectTagOption<T>> list) {
        Map<ResourceLocation, ? extends ITag<?>> tags = allTags.getAllTags();
        
        int i = 0;
        
        for (Map.Entry<ResourceLocation, ? extends ITag<?>> entry : tags.entrySet()) {
            list.add(new SelectTagOption<>(this.x + 10, this.y + 40 + i * 12, entry.getKey(), (ITag<T>) entry.getValue(), this, i));
            i++;
        }
    }

    @Override
    protected SelectTagOption<T> copyFrom(SelectTagOption<T> copyFrom) {
        return new SelectTagOption<>(copyFrom);
    }

    @Override
    protected boolean isSameType(IGuiEventListener listener) {
        return listener instanceof SelectTagOption;
    }

    @Override
    protected boolean atLeastOne() {
        return false;
    }

    public static <T> void open(boolean allowMultiple, ITagCollection<T> tagCollection, Consumer<List<SelectTagOption<T>>> onSubmit) {
        allTags = tagCollection;
        Minecraft.getInstance().setScreen(new SelectTag<>(allowMultiple, onSubmit));
    }
    
    public static class SelectTagOption<T> extends SelectionOption<SelectTagOption<T>> {
        private final ITag<T> tag;

        public SelectTagOption(int x, int y, ResourceLocation resourceLocation, ITag<T> tag, AbstractSelectionUI<SelectTagOption<T>> parent, int index) {
            super(x, y, resourceLocation.toString(), parent, index);
            this.tag = tag;
        }

        public SelectTagOption(SelectTagOption<T> copyFrom) {
            super(copyFrom);
            this.tag = copyFrom.tag;
        }

        public ITag<T> getTag() {
            return tag;
        }
    }
}
