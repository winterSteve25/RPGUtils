package wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.actions.speak;

import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.ForgeRegistries;
import wintersteve25.rpgutils.client.animation.IAnimatedEntity;
import wintersteve25.rpgutils.client.ui.dialogues.DialogueUI;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.actions.base.IDialogueAction;
import wintersteve25.rpgutils.common.utils.IDeserializer;
import wintersteve25.rpgutils.common.utils.JsonUtilities;

public class SpeakAction implements IDialogueAction {

    private final String text;
    private final SoundEvent audio;
    private final int initialTypeSpeed;
    private final boolean waitForInput;

    private SpeakTickableSound sound;

    private String formattedText;
    private int displayIndex;
    private int characterTimer;
    private int typeSpeed;

    private int lastSkipCount;
    private int skipCount;

    public SpeakAction(String text, SoundEvent audio, int typeSpeed, boolean waitForInput) {
        this.text = text;
        this.audio = audio;
        this.initialTypeSpeed = typeSpeed;
        this.waitForInput = waitForInput;
    }

    @Override
    public boolean act(IAnimatedEntity<?> speaker, DialogueUI dialogueUI, Minecraft minecraft, int mouseX, int mouseY) {
        if (displayIndex >= formattedText.length()) {
            if (!waitForInput) return true;
            if (skipCount == lastSkipCount) return false;
            if (sound != null) sound.forceStop();
            return true;
        }

        characterTimer++;

        if (characterTimer % typeSpeed == 0) {
            dialogueUI.displayingDialogueText += formattedText.charAt(displayIndex);
            displayIndex++;
        }

        lastSkipCount = skipCount;

        return false;
    }

    @Override
    public void skip() {
        skipCount++;

        if (skipCount == 1) {
            int value = (int) (typeSpeed * 0.2);
            typeSpeed = value == 0 ? 1 : value;
        }
    }

    @Override
    public void initialize(IAnimatedEntity<?> speaker, DialogueUI dialogueUI, Minecraft minecraft) {
        String speakerNameFormatted = getContentOrTranslation(speaker.getSelf().getName());
        String playerNameFormatted = getContentOrTranslation(dialogueUI.getPlayer().getName());

        formattedText = I18n.get(text, speakerNameFormatted, playerNameFormatted);
        displayIndex = 0;
        characterTimer = 0;
        skipCount = 0;
        typeSpeed = initialTypeSpeed;

        if (audio == null) return;
        sound = new SpeakTickableSound(audio, SoundCategory.VOICE, speaker.getSelf());
        minecraft.getSoundManager().play(sound);
    }

    @Override
    public JsonObject toJson() {
        JsonObject object = new JsonObject();
        object.addProperty("type", "speak");
        if (audio != null) object.addProperty("audio", audio.getLocation().toString());
        object.addProperty("text", text);
        object.addProperty("speed", initialTypeSpeed);
        object.addProperty("waitForInput", waitForInput);
        return object;
    }

    @Override
    public Object[] data() {
        return new Object[] { audio, text, initialTypeSpeed, waitForInput };
    }

    @Override
    public int guiIndex() {
        return 0;
    }

    private static String getContentOrTranslation(ITextComponent text) {
        if (text instanceof TranslationTextComponent) {
            return I18n.get(((TranslationTextComponent) text).getKey());
        }

        return text.getContents();
    }

    public static class Deserializer implements IDeserializer<SpeakAction> {
        @Override
        public SpeakAction fromJson(JsonObject jsonObject) {
            return new SpeakAction(
                    jsonObject.get("text").getAsString(),
                    jsonObject.has("audio") ? ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(jsonObject.get("audio").getAsString())) : null,
                    JsonUtilities.getOrDefault(jsonObject, "speed", 10),
                    JsonUtilities.getOrDefault(jsonObject, "waitForInput", true)
            );
        }
    }
}
