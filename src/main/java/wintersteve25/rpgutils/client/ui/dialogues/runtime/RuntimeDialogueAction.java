package wintersteve25.rpgutils.client.ui.dialogues.runtime;

import wintersteve25.rpgutils.client.ui.dialogues.DialogueUI;

public interface RuntimeDialogueAction {

    /**
     * @return Is this action complete?
     */
    boolean isComplete(DialogueUI ui);

    /**
     * Called every ClientTick
     * @return whether or not it should trigger a UI rebuild
     */
    default boolean tick(DialogueUI ui, float progress) {
        return false;
    }

    /**
     * Called when the Dialogue Action starts
     * @return whether or not it should trigger a UI rebuild
     */
    default void execute(DialogueUI ui) { }

    /**
     * Called when the Dialogue Action finishes
     * @return whether or not it should trigger a UI rebuild
     */
    default void complete(DialogueUI ui) { }
}