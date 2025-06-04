import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class ClipboardStack {
    
    private Stack<String> texts;

    public Set<ClipboardObserver> clipboardObservers;

    public ClipboardStack () {
        texts = new Stack<>();
        
        clipboardObservers = new HashSet<>();
    }

    void registerClipboardObserver(ClipboardObserver co) {
        clipboardObservers.add(co);
    }
    void removeClipboardObserver(ClipboardObserver co) {
        clipboardObservers.remove(co);
    }
    void notifyObservers() {
        for(ClipboardObserver o : clipboardObservers) {
            o.updateClipboard();
        }
    }

    public String push(String text) {
        String pushed = texts.push(text);
        notifyObservers();
        return pushed;
    }
    public String pop() {
        String popped = texts.pop();
        notifyObservers();
        return popped;
    }
    public String peek() {
        return texts.peek();
    }
    public void clear() {
        texts.clear();
        notifyObservers();
    }
    public boolean empty() {
        return texts.empty();
    }
}




interface ClipboardObserver {
    public void updateClipboard();
}