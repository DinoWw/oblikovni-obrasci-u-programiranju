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
    void undo() {
        if(undoStack.empty()) {
            return;
        }

        EditAction action = undoStack.pop();
        action.execute_undo();
        redoStack.push(action);

        if(undoStack.empty() || redoStack.size() == 1) {
            updateSubscribers();
        }
    } 

    // skida naredbu s redoStacka, pusha je na undoStack i izvrsava
    void redo() {
        if(redoStack.empty()) {
            return;
        }

        EditAction action = redoStack.pop();
        action.execute_do();
        undoStack.push(action);

        if(redoStack.empty() || undoStack.size() == 1) {
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


interface EditAction {
    void execute_do();

    void execute_undo();
}


interface UndoManagerSubscriber {
    void updateUndoStack(boolean undoEmpty, boolean redoEmpty);
}