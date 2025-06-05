import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import java.awt.event.KeyEvent;

public class MyMenu extends JMenuBar implements UndoManagerSubscriber, ClipboardObserver, CursorObserver {

    private JMenu fileMenu = new JMenu("File");
    
    private JMenuItem openItem = new JMenuItem("Open");
 	private JMenuItem saveItem = new JMenuItem("Save");
 	private JMenuItem exitItem = new JMenuItem("Exit"); 


    private JMenu editMenu = new JMenu("Edit");

 	private JMenuItem undoItem = new JMenuItem("Undo");
 	private JMenuItem redoItem = new JMenuItem("Redo");
 	private JMenuItem cutItem = new JMenuItem("Cut");
 	private JMenuItem copyItem = new JMenuItem("Copy");
 	private JMenuItem pasteItem = new JMenuItem("Paste");
 	private JMenuItem pasteAndTakeItem = new JMenuItem("Paste and Take");
 	private JMenuItem deleteSelectionItem = new JMenuItem("Delete selection");
 	private JMenuItem clearDocumentItem = new JMenuItem("Clear document");


    private JMenu moveMenu = new JMenu("Move");

 	private JMenuItem cursorToDocumentStartItem = new JMenuItem("Cursor to document start");
 	private JMenuItem cursorToDocumentEndItem = new JMenuItem("Cursor to document end");


    private UndoManager undoManager = UndoManager.instance();
    private TextEditorModel tem;

     MyMenu (TextEditorModel tem) {
        this.tem = tem;

        addMenus();
        addItems();
        defaultStates();
        addListeners();

        undoManager.subscribe(this);
        tem.clipboard.registerClipboardObserver(this);

    }

    private void addMenus() {
        this.add(fileMenu);
        this.add(editMenu);
        this.add(moveMenu);
    }
    private void addItems() {
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(exitItem);

        editMenu.add(undoItem);
        editMenu.add(redoItem);
        editMenu.add(cutItem);
        editMenu.add(copyItem);
        editMenu.add(pasteItem);
        editMenu.add(pasteAndTakeItem);
        editMenu.add(deleteSelectionItem);
        editMenu.add(clearDocumentItem);

        moveMenu.add(cursorToDocumentStartItem);
        moveMenu.add(cursorToDocumentEndItem);
    }
    private void defaultStates() {
        undoItem.setEnabled(false);
        redoItem.setEnabled(false);
        cutItem.setEnabled(false);
        copyItem.setEnabled(false);
        deleteSelectionItem.setEnabled(false);
    }
    private void addListeners() {
        // TODO
        openItem.addActionListener(null);
        saveItem.addActionListener(null);

        exitItem.addActionListener((event) -> System.exit(0));

        undoItem.addActionListener((event) -> undoManager.undo(tem));
        redoItem.addActionListener((event) -> undoManager.redo(tem));
        cutItem.addActionListener((event) -> tem.cutSeleciton());
        copyItem.addActionListener((event) -> tem.copySelection());
        pasteItem.addActionListener((event) -> tem.softPaste());
        pasteAndTakeItem.addActionListener((event) -> tem.softPaste()); //TODO
        deleteSelectionItem.addActionListener((event) -> tem.deleteRange(tem.getSelectionRange())); 
        clearDocumentItem.addActionListener((event) -> tem.deleteRange(new LocationRange(0, 0, tem.lines.get(tem.lines.size()-1).length(), tem.lines.size()-1))); 

    }

    @Override
    public void updateUndoStack(boolean undoEmpty, boolean redoEmpty) {
        undoItem.setEnabled(!undoEmpty);
        redoItem.setEnabled(!redoEmpty);
    }

    @Override
    public void updateClipboard() {
        
    }

    @Override
    public void updateCursorLocation(Location loc) {
        boolean selected = !tem.selectionRange.empty();
        cutItem.setEnabled(selected);
        copyItem.setEnabled(selected);
        // pasteAndTakeItem.setEnabled(selected);
        deleteSelectionItem.setEnabled(selected);

    }

}
