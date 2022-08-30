package wintersteve25.rpgutils.client.ui.quests.player;

import dev.ftb.mods.ftblibrary.ui.BaseScreen;
import net.minecraft.entity.player.PlayerEntity;

public class QuestUI extends BaseScreen {

    private final PlayerEntity player;
    private final AvailableQuestsPanel availableQuestsPanel;
    
    private QuestUI(PlayerEntity player) {
        this.player = player;
        this.availableQuestsPanel = new AvailableQuestsPanel(this);
    }

    @Override
    public void addWidgets() {
        this.add(availableQuestsPanel);
    }

    @Override
    public void alignWidgets() {
        availableQuestsPanel.initGui();
        availableQuestsPanel.setPosAndSize(0, 0, 100, getScreen().getGuiScaledHeight());
    }

    @Override
    public boolean onInit() {
        setPosAndSize(100, 0, getScreen().getGuiScaledWidth() - 100, getScreen().getGuiScaledHeight());
        return super.onInit();
    }

    @Override
    public int getX() {
        return posX;
    }

    @Override
    public int getY() {
        return posY;
    }

    public PlayerEntity getPlayer() {
        return player;
    }

    public static void open(PlayerEntity player) {
        new QuestUI(player).openGui();
    }
}
