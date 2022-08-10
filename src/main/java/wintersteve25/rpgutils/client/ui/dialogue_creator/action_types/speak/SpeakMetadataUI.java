package wintersteve25.rpgutils.client.ui.dialogue_creator.action_types.speak;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import org.apache.commons.lang3.math.NumberUtils;
import wintersteve25.rpgutils.client.ui.components.BaseUI;
import wintersteve25.rpgutils.client.ui.components.buttons.ToggleButton;
import wintersteve25.rpgutils.client.ui.selections.select_entity.SelectEntity;
import wintersteve25.rpgutils.client.ui.selections.select_music.SelectSound;
import wintersteve25.rpgutils.client.ui.selections.select_music.SoundOption;
import wintersteve25.rpgutils.common.utils.ModConstants;
import wintersteve25.rpgutils.common.utils.RLHelper;

import java.util.function.Consumer;

import static wintersteve25.rpgutils.client.ui.selections.select_entity.SelectEntity.CONFIRM_TEXT;
import static wintersteve25.rpgutils.client.ui.selections.select_entity.SelectEntity.WIDTH;

public class SpeakMetadataUI extends BaseUI {
    
    private static final TranslationTextComponent SELECT_SOUND = RLHelper.dialogueEditorComponent("select_sound");
    private static final TranslationTextComponent TYPESPEED = RLHelper.dialogueEditorComponent("type_speed");
    private static final TranslationTextComponent WAIT_FOR_INPUT = RLHelper.dialogueEditorComponent("wait_for_input");

    private final Consumer<SpeakMetadata> onSubmit;
    
    private SoundEvent soundEvent;
    
    private Button selectedSound;
    private ToggleButton toggleButton;
    private TextFieldWidget typespeed;

    protected SpeakMetadataUI(Consumer<SpeakMetadata> onSubmit) {
        super(ModConstants.Resources.BLANK_SCREEN, WIDTH, SelectEntity.HEIGHT);
        this.onSubmit = onSubmit;
    }
    
    @Override
    protected void init() {
        super.init();
        
        Button save = new Button(this.x + (WIDTH - 70) / 2, this.y + 145, 70, 20, CONFIRM_TEXT, btn -> {
            onSubmit.accept(new SpeakMetadata(soundEvent, Integer.parseInt(typespeed.getValue()), toggleButton.isStateTriggered()));
        });
        addButton(save);

        selectedSound = new Button(this.x + (WIDTH - 160) / 2, this.y + 10, 160, 20, selectedSound == null ? SELECT_SOUND : selectedSound.getMessage(), btn -> {
            Minecraft.getInstance().setScreen(new SelectSound(false, selected -> {
                Minecraft.getInstance().setScreen(this);
                SoundOption option = selected.get(0);
                soundEvent = option.getSoundEvent();
                selectedSound.setMessage(new StringTextComponent(soundEvent == null ? "none" : soundEvent.getLocation().toString()));
            }));
        });
        addButton(selectedSound);

        boolean initialState = toggleButton != null && toggleButton.isStateTriggered();
        toggleButton = new ToggleButton(this.x + 125, this.y + 35, 12, 12, initialState, btn -> btn.setStateTriggered(!btn.isStateTriggered()));
        toggleButton.initTextureValues(7, 208, 15, 15, ModConstants.Resources.BLANK_SCREEN);
        addButton(toggleButton);

        String value = typespeed == null ? "10" : typespeed.getValue();
        typespeed = new TextFieldWidget(Minecraft.getInstance().font, this.x + 80, y + 53, 86, 20, TYPESPEED);
        typespeed.setMaxLength(50);
        typespeed.setVisible(true);
        typespeed.setTextColor(16777215);
        typespeed.setValue(value);
        typespeed.setFilter(NumberUtils::isCreatable);
        addButton(typespeed);
    }

    @Override
    public void render(MatrixStack matrixStack, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
        super.renderBackground(matrixStack);
        renderBackgroundTexture(matrixStack);
        drawString(matrixStack, minecraft.font, TYPESPEED, this.x + 10, this.y + 59, TextFormatting.WHITE.getColor());
        drawString(matrixStack, minecraft.font, WAIT_FOR_INPUT, this.x + 10, this.y + 37, TextFormatting.WHITE.getColor());
        super.render(matrixStack, p_230430_2_, p_230430_3_, p_230430_4_);
    }
}
