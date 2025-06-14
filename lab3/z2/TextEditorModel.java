import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

class TextEditorModel {
    public List<String> lines;
    public Location cursorLocation;
    public LocationRange selectionRange;
    
    public ClipboardStack clipboard;
    private UndoManager undoManager = UndoManager.instance();

    private Set<CursorObserver> cursorObservers;
    private Set<TextObserver> textObservers;

    private File ofFile;


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

    void cursorToDocumentStart() {
        cursorLocation.x = 0;
        cursorLocation.y = 0;
        notifyCursorObservers();
    }
    void cursorToDocumentEnd() {
        cursorLocation.x = lines.get(lines.size()-1).length();
        cursorLocation.y = lines.size()-1;
        notifyCursorObservers();
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

            undoManager.push( new EditActionDelete(cursorLocation, "\n") );
        }
        else {
            StringBuilder sb = new StringBuilder(getLine());

            undoManager.push( 
                new EditActionDelete(
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
            
            undoManager.push( new EditActionDelete(cursorLocation, "\n") );
        }
        else {
            StringBuilder sb = new StringBuilder(getLine());
            sb.deleteCharAt(cursorLocation.x);
            lines.set(cursorLocation.y, sb.toString());

            undoManager.push( 
                new EditActionDelete(
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

        undoManager.push(new EditActionDelete(r.start, removedText.toString()));

        this.cursorLocation.x = r.start.x;
        this.cursorLocation.y = r.start.y;

        notifyTextObservers();
    }

    void clearDocument() {
        deleteRange(new LocationRange(0, 0, lines.get(lines.size()-1).length(), lines.size()-1));
        lines.add("");
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

        undoManager.push(new EditActionAdd(cursorLocation, Character.toString(c)));

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

        undoManager.push(new EditActionAdd(cursorLocation, text));
        
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
        undoManager.undo();
        notifyTextObservers();
    }

    void redo() {
        undoManager.redo();
        notifyTextObservers();
    }

    boolean loadFromFile(File file) {
        try {
            Scanner myReader = new Scanner(file);

            lines = new ArrayList<>();

            while (myReader.hasNextLine()) {
                lines.add(myReader.nextLine());
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            return false;
        }
        ofFile = file;
        notifyTextObservers();
        return true;
    }

    boolean save() {
        if(null == ofFile) {
            return false;
        }
        try {
            FileWriter myWriter = new FileWriter(ofFile);
            for(String line : lines) {
                myWriter.write(line);
            }
            myWriter.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    class EditActionDelete implements EditAction {

        private Location location;
        private String text;

        EditActionDelete (Location location, String text) {
            this.location = new Location(location);
            this.text = text;
        }

        public void execute_do() {
            deleteChars();
            notifyTextObservers();
        }

        public void execute_undo() {
            insert();
            notifyTextObservers();
        }


        // briše broj znakova od pozicije kursora
        void deleteChars(){
            int n = text.length();
            for(int y = location.y; n > 0; y++) {
                String line = lines.get(y);
                int startx = y == location.y ? location.x : 0;
                int endx = n < line.length() - startx ? startx + n : line.length();
                
                if(startx == 0 && endx == line.length()) {
                    // TODO potential off by one error with '\n' chars
                    n -= lines.remove(y).length() + 1;
                    y--;
                }
                else {
                    StringBuilder sb = new StringBuilder(line);
                    sb.delete(startx, endx);
                    lines.set(y, sb.toString());
                    n -= endx - startx;
                }
            }
        }

        // umeće proizvoljan tekst (potencijalno više redaka) na mjesto na kojem je kursor i pomiče se kursor
        void insert() {
            StringBuffer sb = new StringBuffer(lines.get(location.y));
            sb.insert(location.x, text);
            String[] newlines = sb.toString().split("\n");

            lines.set(location.y, newlines[0]);

            if(newlines.length > 1) {
                int y = location.y;
                for(int i = 1; i < newlines.length; i++) {
                    lines.add( ++y, newlines[i]);
                }
            }
        }
    }

    class EditActionAdd implements EditAction {

        private Location location;
        private String text;

        EditActionAdd (Location location, String text) {
            this.location = new Location(location);
            this.text = text;
        }

        public void execute_do() {
            insert();
            notifyTextObservers();
        }
        
        public void execute_undo() {
            deleteChars();
            notifyTextObservers();
        }


        // briše broj znakova od pozicije kursora
        void deleteChars(){
            int n = text.length();
            for(int y = location.y; n > 0; y++) {
                String line = lines.get(y);
                int startx = y == location.y ? location.x : 0;
                int endx = n < line.length() - startx ? startx + n : line.length();
                
                if(startx == 0 && endx == line.length()) {
                    // TODO potential off by one error with '\n' chars
                    n -= lines.remove(y).length() + 1;
                    y--;
                }
                else {
                    StringBuilder sb = new StringBuilder(line);
                    sb.delete(startx, endx);
                    lines.set(y, sb.toString());
                    n -= endx - startx;
                }
            }
        }

        // umeće proizvoljan tekst (potencijalno više redaka) na mjesto na kojem je kursor i pomiče se kursor
        void insert() {
            StringBuffer sb = new StringBuffer(lines.get(location.y));
            sb.insert(location.x, text);
            String[] newlines = sb.toString().split("\n");

            lines.set(location.y, newlines[0]);

            if(newlines.length > 1) {
                int y = location.y;
                for(int i = 1; i < newlines.length; i++) {
                    lines.add( ++y, newlines[i]);
                }
            }
        }
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

    boolean empty() {
        return start.x == end.x && start.y == end.y;
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
