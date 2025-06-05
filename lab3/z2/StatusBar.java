import java.awt.Color;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

public class StatusBar extends JLabel implements TextObserver, CursorObserver {

    int lineCount = 0;
    Location cursor = new Location(0, 0);


    StatusBar(TextEditorModel tem) {
        tem.registerTextObserver(this);
        tem.registerCursorObserver(this);

        setBorder(BorderFactory.createLineBorder(Color.BLACK, 5));


        updateLabel();
    }

    private void updateLabel() {
        this.setText(lineCount + " - x" + cursor.x + "y" + cursor.y);
    }   

    @Override
    public void updateText(List<String> text) {
        lineCount = text.size();
        updateLabel();
    }

    @Override
    public void updateCursorLocation(Location loc) {
        cursor.x = loc.x;
        cursor.y = loc.y;
        updateLabel();
    } 
}
