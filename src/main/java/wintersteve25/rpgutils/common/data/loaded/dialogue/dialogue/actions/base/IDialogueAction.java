package wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.actions.base;

import com.google.gson.JsonObject;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import wintersteve25.rpgutils.client.animation.IAnimatedEntity;
import wintersteve25.rpgutils.client.ui.DialogueUI;

public interface IDialogueAction {

    /**
     * @param speaker The speaker
     * @param dialogueUI The UI itself
     * @param matrixStack The render matrix
     * @param minecraft Minecraft instance
     * @param windowWidth Current window width
     * @param windowHeight Current window Height
     * @param speakerNameX Speaker Name Rendering X Position
     * @param speakerNameY Speaker Name Rendering Y Position
     * @param mouseX Mouse X Position
     * @param mouseY Mouse Y Position
     *                     
     * Should be used to render used in the action
     */
    default void render(IAnimatedEntity<?> speaker, DialogueUI dialogueUI, MatrixStack matrixStack, Minecraft minecraft, int windowWidth, int windowHeight, int speakerNameX, int speakerNameY, int mouseX, int mouseY) {
    }

    /**
     * @param speaker The speaker
     * @param dialogueUI The UI itself
     * @param minecraft Minecraft Instance
     * @param mouseX Mouse X Position
     * @param mouseY Mouse Y Position
     * @return If the action is completed. If returns true it will move on the the next action
     * 
     * Should be used to update the logic in the action. Will not be called when game is paused.
     */
    boolean act(IAnimatedEntity<?> speaker, DialogueUI dialogueUI, Minecraft minecraft, int mouseX, int mouseY);

    /**
     * Called when player presses the skip dialogue line button
     */
    default void skip() {
    }

    /**
     * Will be called when the action is executed the first time. Can be used to initialize data or play audio.
     */
    default void initialize(IAnimatedEntity<?> speaker, DialogueUI dialogueUI, Minecraft minecraft) {
    }
    
    JsonObject toJson();
}