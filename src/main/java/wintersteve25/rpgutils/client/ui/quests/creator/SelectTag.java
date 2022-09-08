package wintersteve25.rpgutils.client.ui.quests.creator;

import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ITagCollection;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;
import wintersteve25.rpgutils.client.ui.dialogues.components.selection.AbstractSelectionUI;
import wintersteve25.rpgutils.client.ui.dialogues.components.selection.SelectionOption;
import wintersteve25.rpgutils.common.data.loaded.quest.Quest;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class SelectTag<T> extends AbstractSelectionUI<SelectTag.SelectTagOption<T>> {

    private final ITagCollection<T> allTags;
    
    public SelectTag(boolean allowMultiple, ITagCollection<T> allTags, Consumer<List<SelectTagOption<T>>> onSubmit) {
        super(allowMultiple, onSubmit);
        this.allTags = allTags;
    }

    @Override
    protected void populateOptions(List<SelectTagOption<T>> list) {
        Map<ResourceLocation, ITag<T>> tags = allTags.getAllTags();
        
        int i = 0;
        
        for (Map.Entry<ResourceLocation, ITag<T>> entry : tags.entrySet()) {
            list.add(new SelectTagOption<>(this.x + 10, this.y + 40 + i * 12, entry.getKey(), entry.getValue(), this, i));
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

    public static class SelectTagOption<T> extends SelectionOption<SelectTag.SelectTagOption<T>> {
        private final ITag<T> tag;

        public SelectTagOption(int x, int y, ResourceLocation resourceLocation, ITag<T> tag, AbstractSelectionUI<SelectTag.SelectTagOption<T>> parent, int index) {
            super(x, y, resourceLocation.toString(), parent, index);
            this.tag = tag;
        }

        public SelectTagOption(SelectTag.SelectTagOption<T> copyFrom) {
            super(copyFrom);
            this.tag = copyFrom.tag;
        }

        public ITag<T> getTag() {
            return tag;
        }
    }
}
