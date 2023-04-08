package wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue;

import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.actions.CharacterLineParsed;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.actions.base.ParsedDialogueAction;
import wintersteve25.rpgutils.common.data.loaded.dialogue.dialogue.actions.base.DialogueActionMapper;

import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
public class DialogueParser {

    private static final Pattern WHITE_SPACES = Pattern.compile("^\\s*");
    private static final Pattern EMBEDDED_ACTIONS = Pattern.compile("\\[.+:.+]");

    private final Stack<ParsedDialogueAction> constructingAction;
    private List<String> lines;
    private Dialogue dialogue;
    private int lineOn;
    private int indentationLevel;

    public DialogueParser() {
        constructingAction = new Stack<>();
    }

    public Dialogue parse(String input) {
        return parse(Arrays.stream(input.split("\n")).collect(Collectors.toList()));
    }

    public Dialogue parse(List<String> lines) {
        this.dialogue = new Dialogue();
        this.lines = lines;
        this.indentationLevel = 0;

        for (lineOn = 0; lineOn < lines.size(); lineOn++) {
            String line = lines.get(lineOn);

            if (line.trim().isEmpty()) continue;

            if (line.startsWith("@")) {
                dialogue.add(parseCharacterLine(line));
            }

            if (line.startsWith("$")) {
                ParsedDialogueAction action = parseAction(line, 1, false);
                if (action != null) {
                    dialogue.add(action);
                }
            }

            int newIndentation = getIndentationLevel(line);
            if (newIndentation != indentationLevel) {
                if (newIndentation > indentationLevel) {
                    indentationLevel = newIndentation;
                    ParsedDialogueAction action = constructingAction.pop();
                    action.parseParameters(captureCodeInCurrentIndentation());
                    dialogue.add(action);
                } else {
                    indentationLevel = newIndentation;
                }
            }
        }

        return dialogue;
    }

    public void parseParameters(ParsedDialogueAction action, List<String> parameters) {
        this.lines = parameters;
        this.indentationLevel = 0;

        for (lineOn = 0; lineOn < lines.size(); lineOn++) {
            String line = lines.get(lineOn);
            if (line.startsWith("-")) {
                action.addListEntry(line.substring(1).trim());
            }

            if (line.startsWith("#")) {
                CaptureResult result = captureUntil(line, 1, ':');
                action.addMapEntry(result.content.trim(), line.substring(result.indexExclusive + 1).trim());
            }

            int newIndentation = getIndentationLevel(line);
            if (newIndentation != indentationLevel) {
                if (newIndentation > indentationLevel) {
                    indentationLevel = newIndentation;
                    action.addEntryParameters(captureCodeInCurrentIndentation());
                } else {
                    indentationLevel = newIndentation;
                }
            }
        }
    }

    private CharacterLineParsed parseCharacterLine(String line) {
        CaptureResult result = captureUntil(line, 1, ':');
        String character = result.content.trim();
        String characterLine = line.substring(result.indexExclusive + 1);

        Map<Integer, ParsedDialogueAction> actions = new HashMap<>();

        Matcher matcher = EMBEDDED_ACTIONS.matcher(characterLine);
        while (matcher.find()) {
            ParsedDialogueAction action = parseAction(matcher.group(), 1, true);
            if (action == null) {
                throw new DialogueParseException("Extended dialogue actions are not supported as embeds, please specify a parameter after the `:`");
            }

            actions.put(matcher.start(), action);
        }

        characterLine = matcher.replaceAll("").trim();
        return new CharacterLineParsed(character, characterLine, actions);
    }

    private ParsedDialogueAction parseAction(String line, int startingPositionInclusive, boolean embed) {
        String action;
        String parameters;

        if (embed) {
            CaptureResult result = captureUntil(line, startingPositionInclusive, ']');
            CaptureResult actionPart = captureUntil(result.content, 0, ':');
            action = actionPart.content.trim();
            parameters = result.content.substring(actionPart.indexExclusive + 1).trim();
        } else {
            CaptureResult result = captureUntil(line, startingPositionInclusive, ':');
            action = result.content.trim();
            parameters = line.substring(result.indexExclusive + 1).trim();
        }

        if (parameters.isEmpty()) {
            constructingAction.push(DialogueActionMapper.create(action));
            return null;
        } else {
            ParsedDialogueAction parsedDialogueAction = DialogueActionMapper.create(action);
            parsedDialogueAction.addNormalParameter(parameters);
            return parsedDialogueAction;
        }
    }

    private CaptureResult captureUntil(String line, int startingPositionInclusive, char character) {
        StringBuilder stringBuilder = new StringBuilder();
        int index = -1;

        if (line.length() > 512) {
            try {
                final Field field = String.class.getDeclaredField("value");
                field.setAccessible(true);

                final char[] chars = (char[]) field.get(line);
                final int len = chars.length;
                for (int i = startingPositionInclusive; i < len; i++) {
                    char c = chars[i];
                    if (c == character) {
                        index = i;
                        break;
                    }
                    stringBuilder.append(c);
                }

            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        } else {
            for (int i = startingPositionInclusive; i < line.length(); i++) {
                char c = line.charAt(i);

                if (c == character) {
                    index = i;
                    break;
                }

                stringBuilder.append(c);
            }
        }

        if (index == -1) {
            throw new DialogueParseException("Expected `" + character + "` but non was found on line " + lineOn);
        }

        return new CaptureResult(stringBuilder.toString(), index);
    }

    private List<String> captureCodeInCurrentIndentation() {
        List<String> lines = new ArrayList<>();
        String line = this.lines.get(lineOn);
        boolean didCapture = false;

        while (getIndentationLevel(line) >= indentationLevel) {
            lines.add(line.substring(indentationLevel));
            lineOn++;
            didCapture = true;

            if (lineOn >= this.lines.size()) {
                break;
            }

            line = this.lines.get(lineOn);
        }

        if (didCapture) {
            lineOn--;
        }

        return lines;
    }

    private int getIndentationLevel(String line) {
        Matcher matcher = WHITE_SPACES.matcher(line);

        if (matcher.find()) {
            return matcher.group().length();
        }

        return 0;
    }

    private static final class CaptureResult {
        public final String content;
        public final int indexExclusive;

        private CaptureResult(String content, int indexExclusive) {
            this.content = content;
            this.indexExclusive = indexExclusive;
        }
    }
}
