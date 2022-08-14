package wintersteve25.rpgutils.client.ui.dialogues.dialogue_creator;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import wintersteve25.rpgutils.client.ui.components.UIUtilities;
import wintersteve25.rpgutils.client.ui.components.list.EditableListUI;
import wintersteve25.rpgutils.client.ui.dialogues.dialogue_creator.entries.DialogueActionEntryGui;
import wintersteve25.rpgutils.client.ui.dialogues.dialogue_creator.predicates.DialoguePredicateConfigUI;
import wintersteve25.rpgutils.client.ui.dialogues.dialogue_creator.predicates.DialoguePredicateType;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.Dialogue;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.DynamicUUID;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.actions.base.IDialogueAction;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.predicate.AndPredicate;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.predicate.DialoguePredicate;
import wintersteve25.rpgutils.common.utils.JsonUtilities;
import wintersteve25.rpgutils.common.utils.RLHelper;

import java.util.List;

public class DialogueEditorUI extends EditableListUI<DialogueActionEntryGui> {
    private static final TranslationTextComponent TITLE = RLHelper.dialogueEditorComponent("title");
    private static final TranslationTextComponent PREDICATE = RLHelper.dialogueEditorComponent("predicate");

    private final Runnable onSubmit;
    private final ResourceLocation id;
    
    private Button predicateButton;
    private DialoguePredicate dialoguePredicate;
    private DialoguePredicateType dialoguePredicateType;
    private Dialogue initialData;
    
    public DialogueEditorUI(Runnable onSubmit, ResourceLocation id) {
        super(TITLE);
        this.onSubmit = onSubmit;
        this.id = id;
    }

    public DialogueEditorUI(Runnable onSubmit, ResourceLocation id, Dialogue initialData) {
        super(TITLE);
        this.onSubmit = onSubmit;
        this.id = id;
        this.initialData = initialData;
    }

    @Override
    protected void init() {
        super.init();

        int rightX = this.x + texWidth;

        predicateButton = new Button(rightX - 170, this.y + 5, 60, 20, PREDICATE, btn -> {
            minecraft.setScreen(new DialoguePredicateConfigUI((predicate, type) -> {
                dialoguePredicate = predicate;
                dialoguePredicateType = type;
                minecraft.setScreen(this);
            }, dialoguePredicate));
        });
        addButton(predicateButton);
    }

    @Override
    public void render(MatrixStack pMatrixStack, int pMouseX, int pMouseY, float pPartialTicks) {
        super.render(pMatrixStack, pMouseX, pMouseY, pPartialTicks);
        UIUtilities.tooltipWhenOver(pMatrixStack, predicateButton, pMouseX, pMouseY, Lists.newArrayList(new StringTextComponent(dialoguePredicateType == null ? DialoguePredicateType.NONE.toString() : dialoguePredicateType.toString())));
    }

    @Override
    protected DialogueActionEntryGui createEntry(int index) {
        return new DialogueActionEntryGui(index);
    }

    @Override
    protected void save(List<DialogueActionEntryGui> data) {
        JsonObject object = new JsonObject();
        JsonArray lines = new JsonArray();

        if (dialoguePredicate != null) {
            object.add("predicate", dialoguePredicate.toJson());
        }
        
        for (DialogueActionEntryGui actionGui : data) {
            JsonObject line = new JsonObject();
            DynamicUUID selectedEntity = actionGui.getSelectedEntity();
            if (selectedEntity == null) selectedEntity = new DynamicUUID(DynamicUUID.DynamicType.PLAYER);
            line.add("speaker", selectedEntity.toJson());
            line.add("action", actionGui.getActionTypeGui().save().toJson());
            lines.add(line);
        }

        object.add("lines", lines);
        JsonUtilities.saveDialogue(id, object);
        
        onSubmit.run();
    }

    @Override
    protected void populateEntries() {
        if (initialData == null) return;
        
        dialoguePredicate = initialData.getPredicate();
        if (dialoguePredicate != null) {
            dialoguePredicateType = DialoguePredicateType.values()[dialoguePredicate.guiIndex()];
        }
        
        for (Tuple<DynamicUUID, IDialogueAction> entry : initialData.getLines()) {
            DialogueActionEntryGui entryGui = createEntry();
            entryGui.load(entry);
            addEntry(entryGui);
        }
    }
}