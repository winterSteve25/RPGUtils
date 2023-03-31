package wintersteve25.rpgutils.client.ui.quests.player;

import dev.ftb.mods.ftblibrary.ui.Panel;

public class AvailableQuestsPanel extends Panel {

    public AvailableQuestsPanel(Panel panel) {
        super(panel);

//        ((QuestUI) parent).getPlayer().getCapability(ModCapabilities.PLAYER_QUEST).ifPresent(cap -> {
//            for (Quest quest : cap.getAvailableQuests()) {
//                panel.add(new NordButton(panel, quest.getTitle(), Icon.EMPTY) {
//                    @Override
//                    public void onClicked(MouseButton mouseButton) {
//
//                    }
//                });
//            }
//        });
    }
    
    @Override
    public void addWidgets() {
        
    }

    @Override
    public void alignWidgets() {

    }
}
