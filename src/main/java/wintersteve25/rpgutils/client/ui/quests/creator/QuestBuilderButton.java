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
    private final AllQuestsPanel allQuestsPanel;
    public final Quest.Builder builder;
    
    public QuestBuilderButton(Panel panel, String path, QuestCreationDetailsPanel questDetails, AllQuestsPanel allQuestsPanel) {
        super(panel, new StringTextComponent(path), Icon.EMPTY);
        this.questDetails = questDetails;
        this.allQuestsPanel = allQuestsPanel;
        ResourceLocation resourceLocation = new ResourceLocation(RPGUtils.MOD_ID, path);
        builder = new Quest.Builder(resourceLocation);
    }

    public QuestBuilderButton(Panel panel, QuestCreationDetailsPanel questDetails, AllQuestsPanel allQuestsPanel, Quest.Builder builder) {
        super(panel, new StringTextComponent(builder.getResourceLocation().getPath()), Icon.EMPTY);
        this.questDetails = questDetails;
        this.allQuestsPanel = allQuestsPanel;
        this.builder = builder;
    }

    @Override
    public void onClicked(MouseButton mouseButton) {
        if (mouseButton.isLeft()) {
            playClickSound();
            allQuestsPanel.select(this);
            questDetails.enable(this);
        } else if (mouseButton.isRight()) {
            playClickSound();
            openContextMenu(QuestBuilderContextMenu.create(parent, allQuestsPanel, this));
        }
    }
}
