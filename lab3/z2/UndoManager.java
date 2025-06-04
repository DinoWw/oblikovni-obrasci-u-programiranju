import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class UndoManager {
    private static UndoManager undoManager;

    Stack<EditAction> undoStack = new Stack<>();
    Stack<EditAction> redoStack = new Stack<>();

    Set<UndoManagerSubscriber> subscribers = new HashSet<>();

    private UndoManager() {

    }

    public static UndoManager instance() {
        if(null == undoManager) {
            undoManager = new UndoManager();
        }
        return undoManager;
    }

    // skida naredbu s undoStacka, pusha je na redoStack i izvrsava
    void undo(TextEditorModel tem) {
        if(undoStack.empty()) {
            return;
        }

        EditAction action = undoStack.pop();
        action.execute_undo(tem);
        redoStack.push(action);

        if(undoStack.empty()) {
            updateSubscribers();
        }
    } 

    // skida naredbu s redoStacka, pusha je na undoStack i izvrsava
    void redo(TextEditorModel tem) {
        if(redoStack.empty()) {
            return;
        }

        EditAction action = redoStack.pop();
        action.execute_do(tem);
        undoStack.push(action);

        if(redoStack.empty()) {
            updateSubscribers();
        }
    } 
    // brise redoStack, pusha naredbu na undoStack 
    void push(EditAction c) {
        redoStack.clear();
        undoStack.push(c);

        updateSubscribers();
    }

    void subscribe(UndoManagerSubscriber ums) {
        subscribers.add(ums);
    }
    void unsubscribe(UndoManagerSubscriber ums) {
        subscribers.remove(ums);
    }
    void updateSubscribers() {
        for(UndoManagerSubscriber ums : subscribers) {
            ums.updateUndoStack(undoStack.empty(), redoStack.empty());
        }
    }

}


class EditAction {
    public static enum Action {
        TEXT_ADD,
        TEXT_REMOVE,
    }

    private Action action;
    private Location location;
    private String text;

    EditAction (Action action, Location location, String text) {
        this.action = action;
        this.location = new Location(location);
        this.text = text;
    }

    void execute_do(TextEditorModel tem) {
        switch (action) {
            case TEXT_ADD:
                insert(tem);
                break;

            case TEXT_REMOVE:
                deleteChars(tem);
                break;

            default:
                break;
        }
    }

    void execute_undo(TextEditorModel tem) {
        switch (action) {
            case TEXT_ADD:
                deleteChars(tem);
                break;
        
            case TEXT_REMOVE:
                insert(tem);
                break;

            default:
                break;
        }

    }


    // briše broj znakova od pozicije kursora
    void deleteChars(TextEditorModel tem){
        int n = text.length();
        for(int y = location.y; n > 0; y++) {
            String line = tem.lines.get(y);
            int startx = y == location.y ? location.x : 0;
            int endx = n < line.length() - startx ? startx + n : line.length();
            
            if(startx == 0 && endx == line.length()) {
                // TODO potential off by one error with '\n' chars
                n -= tem.lines.remove(y).length() + 1;
                y--;
            }
            else {
                StringBuilder sb = new StringBuilder(line);
                sb.delete(startx, endx);
                tem.lines.set(y, sb.toString());
                n -= endx - startx;
            }
        }
    }

    // umeće proizvoljan tekst (potencijalno više redaka) na mjesto na kojem je kursor i pomiče se kursor
    void insert(TextEditorModel tem) {
        StringBuffer sb = new StringBuffer(tem.lines.get(location.y));
        sb.insert(location.x, text);
        String[] lines = sb.toString().split("\n");

        tem.lines.set(location.y, lines[0]);

        if(lines.length > 1) {
            int y = location.y;
            for(int i = 1; i < lines.length; i++) {
                tem.lines.add( ++y, lines[i]);
            }
        }
    }
}


interface UndoManagerSubscriber {
    void updateUndoStack(boolean undoEmpty, boolean redoEmpty);
}