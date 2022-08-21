package wintersteve25.rpgutils.client.ui.quests.creator;

import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.ui.NordButton;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import wintersteve25.rpgutils.RPGUtils;
import wintersteve25.rpgutils.common.data.loaded.quest.Quest;

public class QuestBuilderButton extends NordButton {
    
    private final QuestCreationDetailsPanel questDetails;
    public final Quest.Builder builder;
    
    public QuestBuilderButton(Panel panel, String path, QuestCreationDetailsPanel questDetails) {
        super(panel, new StringTextComponent(path), Icon.EMPTY);
        this.questDetails = questDetails;
        ResourceLocation resourceLocation = new ResourceLocation(RPGUtils.MOD_ID, path);
        builder = new Quest.Builder(resourceLocation);
    }

    @Override
    public void onClicked(MouseButton mouseButton) {
        if (mouseButton.isLeft()) {
            playClickSound();
            questDetails.enable(this);
        }
    }
}
