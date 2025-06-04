import java.awt.event.KeyEvent;

public abstract class SelectionState {
    protected TextEditorModel tem;
    SelectionState(TextEditorModel tem) {
        this.tem = tem;
        enter();
    }
    void enter() {

    }

    void exit() {
        
    }

    SelectionState toState(SelectionState state) {
        if(state != this) {
            exit();
        }
        return state;
    }

    SelectionState handleShiftDown() {
        return new SelectingState(tem);
    }
    SelectionState handleShiftUp() {
        return this;
    }
    SelectionState handleEsc() {
        return this;
    }
    SelectionState handleBsp() {
        return this;
    }
    SelectionState handleTyped(KeyEvent e) {
        char c = e.getKeyChar();
        return this;
    }
    SelectionState handleKeyDown(KeyEvent e) {
        return this;
    }

}

// TODO: use singletons for states

class CursorState extends SelectionState {

    CursorState(TextEditorModel tem) {
        super(tem);
    }

    @Override
    SelectionState handleTyped(KeyEvent e) {
        char c = e.getKeyChar();
        int modifiersEx = e.getModifiersEx();

        if(modifiersEx == 0) {
            switch (c) {
                case 'h':
                case 'H':
                    return handleLeft();
            
                case 'j':
                case 'J':
                    return handleDown();
            
                case 'k':
                case 'K':
                    return handleUp();
            
                case 'l':
                case 'L':
                    return handleRight();
                case 'x':
                    return handleDel();
                case 'X':
                    return handleBsp();
            
                case 'i':
                    return new InsertState(tem);
            
                default:
                    return this;
            }
        }
        return this;
    }

    @Override
    SelectionState handleKeyDown(KeyEvent e) {
        int code = e.getKeyCode();
        int modifiersEx = e.getModifiersEx();


        if((modifiersEx & (KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK)) == KeyEvent.CTRL_DOWN_MASK) {
            switch (code) {
                case KeyEvent.VK_C:
                    return handleCopy();
                case KeyEvent.VK_V:
                    return handlePaste();
                case KeyEvent.VK_Z:
                    tem.undo();
                    return this;
                case KeyEvent.VK_Y:
                    tem.redo();
                    return this;
                default:
                    return this;
            }
        }
        else if((modifiersEx & (KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK)) == (KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK)) {
            switch (code) {
                case KeyEvent.VK_C:
                    return handleCopy();
                case KeyEvent.VK_V:
                    return handleHardPaste();
                default:
                    return this;
            }
        }
        return this;
    }


    SelectionState handleLeft(){
        tem.moveCursorLeft();
        return this;
    }
    SelectionState handleRight() {
        tem.moveCursorRight();
        return this;
    }
    SelectionState handleUp() {
        tem.moveCursorUp();
        return this;
    }
    SelectionState handleDown() {
        tem.moveCursorDown();
        return this;
    }
    SelectionState handleDel() {
        tem.deleteAfter();
        return this;
    }
    SelectionState handleBsp() {
        tem.deleteBefore();
        return this;
    }
    SelectionState handleCopy() {
        tem.copySelection();
        return this;
    }
    SelectionState handleHardPaste() {
        // tem.deleteRange(tem.getSelectionRange());
        tem.hardPaste();
        return toState(new CursorState(tem));
    }
    SelectionState handlePaste() {
        // tem.deleteRange(tem.getSelectionRange());
        tem.softPaste();
        return toState(new CursorState(tem));
    }
}

class SelectingState extends SelectionState{
    SelectingState(TextEditorModel tem) {
        super(tem);
    }
    @Override
    void enter() {
        LocationRange sel = tem.getSelectionRange();
        sel.start = new Location(tem.cursorLocation);
        sel.end = tem.cursorLocation;
    }

    @Override
    SelectionState handleTyped(KeyEvent e) {
        char c = e.getKeyChar();
        int modifiersEx = e.getModifiersEx();
        if((modifiersEx & KeyEvent.SHIFT_DOWN_MASK) == KeyEvent.SHIFT_DOWN_MASK) {
            switch (c) {
                case 'h':
                case 'H':
                    return handleLeft();
            
                case 'j':
                case 'J':
                    return handleDown();
            
                case 'k':
                case 'K':
                    return handleUp();
            
                case 'l':
                case 'L':
                    return handleRight();
            
                case 'x':
                case 'X':
                    return handleDel();
            
                case 'i':
                    return new InsertState(tem);
            
                default:
                    return this;
            }
        }
        return this;
    }

    @Override
    SelectionState handleKeyDown(KeyEvent e) {
        int code = e.getKeyCode();
        int modifiersEx = e.getModifiersEx();

        if((modifiersEx & (KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK)) == (KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK)) {
            switch (code) {
                case KeyEvent.VK_V:
                    return handleHardPaste();
                default:
                    return this;
            }
        }
        return this;
    }

    SelectionState handleLeft(){
        tem.moveCursorLeft();
        return this;
    }
    SelectionState handleRight() {
        tem.moveCursorRight();
        return this;
    }
    SelectionState handleUp() {
        tem.moveCursorUp();
        return this;
    }
    SelectionState handleDown() {
        tem.moveCursorDown();
        return this;
    }
    SelectionState handleDel() {
        tem.deleteRange(tem.getSelectionRange());
        return this;
    }
    SelectionState handleHardPaste() {
        // tem.deleteRange(tem.getSelectionRange());
        tem.hardPaste();
        return toState(new CursorState(tem));
    }

    @Override
    SelectionState handleShiftDown() {
        // theoretically unreachable
        return this;
    }

    @Override
    SelectionState handleShiftUp() {
        return toState(new SelectedState(tem));
    }
    
    
    
}

class SelectedState extends SelectionState{
    SelectedState(TextEditorModel tem) {
        super(tem);
    }

    @Override
    void exit() {
        tem.setSelectionRange( new LocationRange(new Location(tem.cursorLocation), new Location(tem.cursorLocation)) );
    }

    @Override
    SelectionState handleTyped(KeyEvent e) {
        char c = e.getKeyChar();
        int modifiersEx = e.getModifiersEx();

        if(modifiersEx == 0) {
            switch (c) {
                case 'h':
                case 'H':
                    return handleLeft();
            
                case 'j':
                case 'J':
                    return handleDown();
            
                case 'k':
                case 'K':
                    return handleUp();
            
                case 'l':
                case 'L':
                    return handleRight();
            
                case 'i':
                    return toState(new InsertState(tem));
            
                case 'x':
                case 'X':
                    return handleDel();
            
                default:
                    return this;
            }
        }
        return this;
    }

    @Override
    SelectionState handleKeyDown(KeyEvent e) {
        int code = e.getKeyCode();
        int modifiersEx = e.getModifiersEx();

        if((modifiersEx & (KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK)) == KeyEvent.CTRL_DOWN_MASK) {
            switch (code) {
                case KeyEvent.VK_C:
                    return handleCopy();
                case KeyEvent.VK_V:
                    return handlePaste();
                case KeyEvent.VK_X:
                    return handleCut();
                case KeyEvent.VK_Z:
                    tem.undo();
                    return this;
                case KeyEvent.VK_Y:
                    tem.redo();
                    return this;
                default:
                    return this;
            }
        }
        else if((modifiersEx & (KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK)) == (KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK)) {
            switch (code) {
                case KeyEvent.VK_C:
                    return handleCopy();
                case KeyEvent.VK_V:
                    return handleHardPaste();
                default:
                    return this;
            }
        }
        return this;
    }


    SelectionState handleLeft(){
        tem.moveCursorLeft();
        return toState(new CursorState(tem));
    }
    SelectionState handleRight() {
        tem.moveCursorRight();
        return toState(new CursorState(tem));
    }
    SelectionState handleUp() {
        tem.moveCursorUp();
        return toState(new CursorState(tem));
    }
    SelectionState handleDown() {
        tem.moveCursorDown();
        return toState(new CursorState(tem));
    }
    SelectionState handleDel() {
        tem.deleteRange(tem.getSelectionRange());
        return toState(new CursorState(tem));
    }
    SelectionState handleCopy() {
        tem.copySelection();
        return this;
    }
    SelectionState handleHardPaste() {
        // tem.deleteRange(tem.getSelectionRange());
        tem.hardPaste();
        return toState(new CursorState(tem));
    }
    SelectionState handlePaste() {
        // tem.deleteRange(tem.getSelectionRange());
        tem.softPaste();
        return toState(new CursorState(tem));
    }
    SelectionState handleCut() {
        tem.cutSeleciton();
        return toState(new CursorState(tem));
    }

}

// TODO exit insert mode
class InsertState extends SelectionState {
    InsertState(TextEditorModel tem) {
        super(tem);
    }

    @Override
    void enter() {
    }

    @Override
    SelectionState handleTyped(KeyEvent e) {
        char c = e.getKeyChar();
        int modifiersEx = e.getModifiersEx();
        if(modifiersEx == 0){
            if(Character.isDigit(c) || Character.isLetter(c)){
                tem.insert(c);
            }
            // enter
            else if(c == 10) {
                tem.insert("\n");
            }
        }

    return this;
    }

    @Override
    SelectionState handleKeyDown(KeyEvent e) {
        int code = e.getKeyCode();
        int modifiersEx = e.getModifiersEx();


        if((modifiersEx & (KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK)) == KeyEvent.CTRL_DOWN_MASK) {
            switch (code) {
                case KeyEvent.VK_V:
                    return handlePaste();
                case KeyEvent.VK_Z:
                    tem.undo();
                    return this;
                case KeyEvent.VK_Y:
                    tem.redo();
                    return this;
                default:
                    return this;
            }
        }
        else if((modifiersEx & (KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK)) == (KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK)) {
            switch (code) {
                case KeyEvent.VK_C:
                    return handleCopy();
                case KeyEvent.VK_V:
                    return handleHardPaste();
                default:
                    return this;
            }
        }
        return this;
    }

    @Override
    SelectionState handleShiftUp() {
        return this;
    }

    @Override
    SelectionState handleShiftDown() {
        return this;
    }

    @Override
    SelectionState handleEsc() {
        return toState(new CursorState(tem));
    }
    @Override
    SelectionState handleBsp() {
        tem.deleteBefore();
        return this;
    }

    SelectionState handleCopy() {
        tem.copySelection();
        return this;
    }
    SelectionState handleHardPaste() {
        tem.hardPaste();
        return toState(new CursorState(tem));
    }
    SelectionState handlePaste() {
        tem.softPaste();
        return toState(new CursorState(tem));
    }
    SelectionState handleCut() {
        tem.cutSeleciton();
        return toState(new CursorState(tem));
    }

}