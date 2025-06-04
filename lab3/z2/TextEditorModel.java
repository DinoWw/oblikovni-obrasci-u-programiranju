import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

class TextEditorModel {
    public List<String> lines;
    public Location cursorLocation;
    public LocationRange selectionRange;
    
    private ClipboardStack clipboard;
    private UndoManager undoManager = UndoManager.instance();

    private Set<CursorObserver> cursorObservers;
    private Set<TextObserver> textObservers;




    public TextEditorModel (String text){
        this.lines = new ArrayList<String>(Arrays.asList(text.split("\n")));
        this.cursorLocation = new Location(0, 0);
        this.selectionRange = new LocationRange(0, 0, 0, 0);

        this.clipboard = new ClipboardStack();

        cursorObservers = new HashSet<>();
        textObservers = new HashSet<>();
    }

    public Iterator<String> allLines() {
        return lines.iterator();
    }
    public Iterator<String> linesRange(int index1, int index2) {
        return new LineIterator(lines, index1, index2);
    }

    void registerCursorObserver(CursorObserver co) {
        cursorObservers.add(co);
    }
    void registerTextObserver(TextObserver to) {
        textObservers.add(to);
    }
    void removeCursorObserver(CursorObserver co) {
        cursorObservers.remove(co);
    }
    void removeTextObserver(TextObserver to) {
        textObservers.remove(to);
    }

    void notifyCursorObservers() {
        for(CursorObserver o : cursorObservers) {
            o.updateCursorLocation(cursorLocation);
        }
    }
    void notifyTextObservers() {
        for(TextObserver o : textObservers) {
            o.updateText(lines);
        }
    }

    void moveCursorLeft(){
        cursorLocation.x = Math.max(0, cursorLocation.x-1);
        notifyCursorObservers();
    }
    void moveCursorRight() {
        cursorLocation.x = Math.min( cursorLocation.x + 1 , lines.get(cursorLocation.y).length());
        notifyCursorObservers();
    }
    void moveCursorUp() {
        cursorLocation.y = Math.max(0, cursorLocation.y-1);
        notifyCursorObservers();
    }
    void moveCursorDown() {
        cursorLocation.y =Math.min( cursorLocation.y + 1 , lines.size() -1);
        notifyCursorObservers();
    }

    void setCursorLocation(Location loc) {
        cursorLocation.x = loc.x;
        cursorLocation.y = loc.y;
    }

    private String getLine() {
        return lines.get(cursorLocation.y);
    }

    // briše znak iza kojeg je kursor i pomiče poziciju kursora unatrag (lijevo) 
    boolean deleteBefore() {
        if(0 == cursorLocation.x && 0 == cursorLocation.y) {
            return false;
        }
        if( 0 == cursorLocation.x ) {
            String lineUp = lines.get(cursorLocation.y-1);
            lines.set(cursorLocation.y-1, lineUp.concat(getLine()));
            lines.remove(cursorLocation.y);

            cursorLocation.y --;
            cursorLocation.x = lineUp.length();

            undoManager.push( new EditAction(EditAction.Action.TEXT_REMOVE, cursorLocation, "\n") );
        }
        else {
            StringBuilder sb = new StringBuilder(getLine());

            undoManager.push( 
                new EditAction(
                    EditAction.Action.TEXT_REMOVE, 
                    new Location(cursorLocation.x-1, cursorLocation.y), 
                    Character.toString(getLine().charAt(cursorLocation.x))
            ));

            sb.deleteCharAt(cursorLocation.x - 1);
            lines.set(cursorLocation.y, sb.toString());
            cursorLocation.x -= 1;
        }

        notifyCursorObservers();
        notifyTextObservers();
        return true;
	}

    // briše znak ispred kojeg je kursor i ne mijenja poziciju kursora
    boolean deleteAfter(){
        if( lines.size()-1 == cursorLocation.y && getLine().length() == cursorLocation.x ) {
            return false;
        }
        if(getLine().length() == cursorLocation.x) {
            String lineDown = lines.get(cursorLocation.y+1);
            lines.set(cursorLocation.y, getLine().concat(lineDown));
            lines.remove(cursorLocation.y+1);
            
            undoManager.push( new EditAction(EditAction.Action.TEXT_REMOVE, cursorLocation, "\n") );
        }
        else {
            StringBuilder sb = new StringBuilder(getLine());
            sb.deleteCharAt(cursorLocation.x);
            lines.set(cursorLocation.y, sb.toString());

            undoManager.push( 
                new EditAction(
                    EditAction.Action.TEXT_REMOVE, 
                    cursorLocation, 
                    Character.toString(getLine().charAt(cursorLocation.x))
            ));
        }

        notifyTextObservers();
        return true;
    }

    // briše predani raspon znakova
    void deleteRange(LocationRange r){
        StringBuilder removedText = new StringBuilder();
        for(int y = r.end.y; y >= r.start.y; y --) {
            String line = lines.get(y);
            int startx = y == r.start.y ? r.start.x : 0;
            int endx = y == r.end.y ? r.end.x : line.length();
            
            if(startx == 0 && endx == line.length()) {
                removedText.append( lines.remove(y) );
                removedText.append( "\n" );
            }
            else {
                StringBuilder sb = new StringBuilder(line);
                removedText.append( sb.substring(startx, endx) );
                sb.delete(startx, endx);
                lines.set(y, sb.toString());
            }
        }

        undoManager.push(new EditAction(EditAction.Action.TEXT_REMOVE, r.start, removedText.toString()));

        this.cursorLocation.x = r.start.x;
        this.cursorLocation.y = r.start.y;

        notifyTextObservers();
    }

    LocationRange getSelectionRange() {
        return selectionRange;
    }
    void setSelectionRange(LocationRange range){
        this.selectionRange = range;
        notifyCursorObservers();
    }

    // umeće znak na mjesto na kojem je kursor i pomiče se kursor
    public void insert(char c) {
        StringBuffer sb = new StringBuffer(getLine());

        sb.insert(cursorLocation.x, c);

        lines.set(cursorLocation.y, sb.toString());

        undoManager.push(new EditAction(EditAction.Action.TEXT_ADD, cursorLocation, Character.toString(c)));

        this.cursorLocation.x ++;
        notifyTextObservers();
        notifyCursorObservers();
    }

    // umeće proizvoljan tekst (potencijalno više redaka) na mjesto na kojem je kursor i pomiče se kursor
    public void insert(String text) {
        StringBuffer sb = new StringBuffer(getLine());
        sb.insert(cursorLocation.x, text);
        String[] lines = sb.toString().split("\n");

        this.lines.set(cursorLocation.y, lines[0]);

        if(lines.length > 1) {
            for(int i = 1; i < lines.length; i++) {
                this.lines.add( ++cursorLocation.y, lines[i]);
            }
        }

        undoManager.push(new EditAction(EditAction.Action.TEXT_ADD, cursorLocation, text));
        
        // lots of computation for little gain,
        // TODO: optimize
        String[] splittext = text.split("\n");
        if(0 == splittext.length) {
            cursorLocation.x = 0;
        }
        else if(1 < splittext.length) {
            cursorLocation.x = splittext[splittext.length-1].length();
        }
        
        notifyTextObservers();
        notifyCursorObservers();
    }

    public String selectionAsSting() {
        StringBuilder whole = new StringBuilder();
        for(int y = selectionRange.start.y; y <= selectionRange.end.y; y ++) {
            String line = lines.get(y);
            int startx = y == selectionRange.start.y ? selectionRange.start.x : 0;
            int endx = y == selectionRange.end.y ? selectionRange.end.x : line.length();
            
            whole.append(lines.get(y).substring(startx, endx));
            whole.append("\n");
        }
        whole.deleteCharAt(whole.length()-1);
        return whole.toString();
    }

    // trenutnu selekciju (ako postoji) pusha na stack u clipboard
    public void copySelection() {
        clipboard.push(selectionAsSting());
    }

    // trenutnu selekciju (ako postoji) pusha na stack u clipboard i potom je briše u modelu teksta
    public void cutSeleciton() {
        String selection = selectionAsSting();
        clipboard.push(selection);

        // deleteRange calls UndoManager
        deleteRange(selectionRange);
    }

    // tekst s vrha stoga u clipboardu čita (ali ne miče) i ubacuje ga u model pozivom metode insert(...)
    public boolean softPaste() {
        if(clipboard.empty()) {
            return false;
        }

        // insert calls UndoManager
        insert(clipboard.peek());
        return true;
    }

    // tekst s vrha stoga u clipboardu čita i uklanja ga sa stoga te umeće u model teksta pozivom metode insert(...) 
    public boolean hardPaste() {
        if(clipboard.empty()) {
            return false;
        }

        // insert calls UndoManager
        insert(clipboard.pop());
        return true;
    }

    void undo() {
        undoManager.undo(this);
        notifyTextObservers();
    }

    void redo() {
        undoManager.redo(this);
        notifyTextObservers();
    }

}

class Location {
	public int x;
	public int y;

    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public Location(Location loc) {
        this.x = loc.x;
        this.y = loc.y;
    }

}

class LocationRange {
    public Location start;
    public Location end;
    LocationRange(int startx, int starty, int endx, int endy) {
        this.start = new Location(startx, starty);
        this.end = new Location(endx, endy);
    }
    LocationRange(Location start, Location end) {
        this.start = start;
        this.end = end;
    }
    LocationRange(LocationRange range) {
        this.start = range.start;
        this.end = range.end;
    }
}

class LineIterator implements Iterator<String> {
    private int i;
    private int max;
    private List<String> lines;

    public LineIterator(List<String> lines) {
        this.i = 0;
        this.max = lines.size();
        this.lines = lines;

    }

    public LineIterator(List<String> lines, int start, int end) {
        this.i = start;
        this.max = end;
        this.lines = lines;
    }



    @Override
    public boolean hasNext() {
        return i < max;
    }

    @Override
    public String next() {
        return lines.get(i++);

    }
}



interface CursorObserver {
    void updateCursorLocation(Location loc);
}

interface TextObserver {
    void updateText(List<String> text);
}
