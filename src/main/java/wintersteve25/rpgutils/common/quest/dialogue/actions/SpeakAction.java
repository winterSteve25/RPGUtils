package wintersteve25.rpgutils.common.quest.dialogue.actions;

import com.google.gson.JsonObject;
import com.mojang.blaze3d.matrix.MatrixStack;
import jdk.nashorn.internal.runtime.regexp.joni.Regex;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.ForgeRegistries;
import wintersteve25.rpgutils.client.animation.IAnimatedEntity;
import wintersteve25.rpgutils.client.ui.DialogueUI;
import wintersteve25.rpgutils.common.utils.ISerializer;
import wintersteve25.rpgutils.common.utils.JsonUtilities;

import java.util.regex.Pattern;

public class SpeakAction implements IDialogueAction {
    private final String text;
    private final SoundEvent audio;
    private final int initialTypeSpeed;
    
    private SpeakTickableSound sound;

    private boolean initialized;
    private String formattedText;
    private String displayText;
    private int displayIndex;
    private int characterTimer;
    private int typeSpeed;

    private int lastSkipCount;
    private int skipCount;
    
    private long startPauseMills;
    private float pause;
    
    public SpeakAction(String text, SoundEvent audio, int typeSpeed) {
        this.text = text;
        this.audio = audio;
        this.initialTypeSpeed = typeSpeed;
    }

    @Override
    public void render(IAnimatedEntity<?> speaker, DialogueUI dialogueUI, MatrixStack matrixStack, Minecraft minecraft, int windowWidth, int windowHeight, int speakerX, int speakerY, int mouseX, int mouseY) {
        AbstractGui.drawString(matrixStack, minecraft.font, displayText, speakerX, speakerY + 10, 16777215);
    }

    @Override
    public boolean act(IAnimatedEntity<?> speaker, DialogueUI dialogueUI, Minecraft minecraft, int mouseX, int mouseY) {
        if (displayIndex >= formattedText.length()) {
            if (skipCount == lastSkipCount) return false;
            if (sound != null) sound.forceStop();
            return true;
        }

        if (pause > 0) {
            float timeElapsed = (System.currentTimeMillis() - startPauseMills) / 1000f;
            
            System.out.println(timeElapsed);
            
            if (timeElapsed >= pause) {
                pause = 0;
                startPauseMills = 0;
            }
            return false;
        }
        
        characterTimer++;
        
        if (characterTimer % typeSpeed == 0) {
            displayText += formattedText.charAt(displayIndex);

            if (peek(1) == '<') {
                int offset = 2;

                while (peek(offset) != '>' && peek(offset) != '\n') {
                    offset++;
                }

                String tag = formattedText.substring(displayIndex + 2, displayIndex + offset);
                if (Pattern.matches("pause=\\d+", tag)) {
                    pause = Float.parseFloat(tag.substring(6));
                    startPauseMills = System.currentTimeMillis();
                    displayIndex = displayIndex + offset + 1;
                    lastSkipCount = skipCount;
                    return false;
                }
            }
            
            displayIndex++;
        }
        
        lastSkipCount = skipCount;
        
        return false;
    }

    @Override
    public void skip() {
        if (!initialized) return;
        
        skipCount++;

        if (skipCount == 1) {
            int value = (int) (typeSpeed * 0.2);
            typeSpeed = value == 0 ? 1 : value;
        }
    }

    @Override
    public void initialize(IAnimatedEntity<?> speaker, DialogueUI dialogueUI, Minecraft minecraft) {
        initialized = true;
        
        String speakerNameFormatted = getContentOrTranslation(speaker.getSelf().getName());
        String playerNameFormatted = getContentOrTranslation(minecraft.player.getName());

        formattedText = I18n.get(text, speakerNameFormatted, playerNameFormatted);
        displayText = "";
        displayIndex = 0;
        characterTimer = 0;
        skipCount = 0;
        typeSpeed = initialTypeSpeed;
 
        if (audio == null) return;
        sound = new SpeakTickableSound(audio, SoundCategory.VOICE, speaker.getSelf());
        minecraft.getSoundManager().play(sound);
    }
    
    private char peek(int offset) {
        if (displayIndex + offset >= formattedText.length()) return '\n';
        return formattedText.charAt(displayIndex + offset);
    }
    
    private static String getContentOrTranslation(ITextComponent text) {
        if (text instanceof TranslationTextComponent) {
            return I18n.get(((TranslationTextComponent) text).getKey());
        }
        
        return text.getContents();
    }
    
    public static class Serializer implements ISerializer<IDialogueAction> {
        @Override
        public IDialogueAction fromJson(JsonObject jsonObject) {
            return new SpeakAction(
                    jsonObject.get("text").getAsString(),
                    jsonObject.has("audio") ? ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(jsonObject.get("audio").getAsString())) : null,
                    JsonUtilities.getOrDefault(jsonObject, "speed", 8));
        }
    }
}
