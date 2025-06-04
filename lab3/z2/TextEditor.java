import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

class TextEditor extends JPanel implements KeyListener, CursorObserver, TextObserver { 
    public int charH, charW, caretH, caretW;
    private TextEditorModel tem;

    private SelectionState selection;

    public TextEditor (TextEditorModel model){
        this.tem = model;

        this.selection = new CursorState(model);

        this.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        charH = this.getFont().getSize();
        caretH = this.getFont().getSize();
        charW = this.getFontMetrics(this.getFont()).stringWidth("x");
        caretW = charW/5;

        addKeyListener(this);

        tem.registerCursorObserver(this);
        tem.registerTextObserver(this);

    }
    
    public void paintComponent(Graphics g) {  
        // BG 
        setBackground(Color.WHITE);
        g.clearRect(0, 0, this.getWidth(), this.getHeight());

        // Highlight
        g.setColor(new Color(0xAAAAAAAA, true));
        LocationRange sel = tem.getSelectionRange();
        // System.out.println("sx" + sel.start.x + " sy" + sel.start.y + " ex" + sel.end.x + " ey" + sel.end.y);
        for(int y = sel.start.y; y <= sel.end.y; y ++) {
            int startx = y == sel.start.y ? sel.start.x : 0;
            int endx = y == sel.end.y ? sel.end.x : tem.lines.get(y).length();
            g.fillRect(startx*charW, y*charH, (endx-startx) *charW, caretH);
        }

        // Lines
        g.setColor(new Color(0x000000));
        drawText(g, tem.lines);

        // Cursor
        drawCursor(g, tem.cursorLocation);

    }

    private void handleShiftDown() {
        selection = selection.handleShiftDown();
    }
    private void handleShiftUp() {
        selection = selection.handleShiftUp();
    }
    private void handleEsc() {
        selection = selection.handleEsc();
    }
    private void handleBsp() {
        selection = selection.handleBsp();
    }
    private void handleKeyDown(KeyEvent e) {
        selection = selection.handleKeyDown(e);
    }
    private void handleTyped(KeyEvent e) {
        selection = selection.handleTyped(e);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        handleTyped(e);
    }
    @Override
    public void keyPressed(KeyEvent e) {
        int id = e.getID();
        if (id == KeyEvent.KEY_TYPED) {
            // nothing
            // char c = e.getKeyChar();
            return;
        }
        
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_SHIFT:
                handleShiftDown();
                break;
        
            case KeyEvent.VK_ESCAPE:
                handleEsc();
                break;
        
            case KeyEvent.VK_BACK_SPACE:
                handleBsp();
                break;
        
            default:
                handleKeyDown(e);
                break;
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {
        int id = e.getID();
        if (id == KeyEvent.KEY_TYPED) {
            // nothing
            // char c = e.getKeyChar();
            return;
        }
        
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_SHIFT:
                handleShiftUp();
                break;
        
            default:
                break;
        }
    }

    @Override
    public void updateCursorLocation(Location loc) {
        Graphics g = this.getGraphics();

        this.paintComponent(g);
        
    }
    @Override
    public void updateText(List<String> lines) {
        Graphics g = this.getGraphics();

        this.paintComponent(g);
        
    }
    private void drawCursor(Graphics g, Location loc) {
        // Cursor
        g.fillRect(loc.x * charW, loc.y * charH, caretW, caretH);

    }

    public void drawText(Graphics g, List<String> lines) {
        int h = 0;
        for(String line : lines) {
            g.drawString(line, 0, h + charH);
            h += charH;
        }
    }

}