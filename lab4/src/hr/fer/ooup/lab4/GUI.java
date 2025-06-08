// visual studio forsira src. 
package src.hr.fer.ooup.lab4;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import src.hr.fer.ooup.lab4.graphics.Canvas;
import src.hr.fer.ooup.lab4.model.DocumentModel;
import src.hr.fer.ooup.lab4.model.DocumentModelListener;
import src.hr.fer.ooup.lab4.model.GraphicalObject;
import src.hr.fer.ooup.lab4.model.state.AddShapeState;
import src.hr.fer.ooup.lab4.model.state.IdleState;
import src.hr.fer.ooup.lab4.model.state.SelectShapeState;
import src.hr.fer.ooup.lab4.model.state.State;
import src.hr.fer.ooup.lab4.model.state.StateListener;

public class GUI extends JFrame {

    private List<GraphicalObject> defaultObjects;
    private DocumentModel model;
    private Canvas canvas;

    private State state = new IdleState();
    private List<StateListener> stateListeners = new ArrayList<>();

    private DocumentModelListener modelListener = new DocumentModelListener() {
        public void documentChange() {
            canvas.repaint();
        };
    };
    
    public GUI (List<GraphicalObject> defaultObjects) {
        this.defaultObjects = defaultObjects;

        // primjerak modela dokumenta
        this.model = new DocumentModel();
        model.addDocumentModelListener(modelListener);
        
        // initialize menu bar
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        
        for(int i = 0; i < defaultObjects.size(); i ++) {
            GraphicalObject o = defaultObjects.get(i);
            JMenuItem button = new JMenuItem(o.getShapeName());
            button.addActionListener((ActionEvent e) -> {
                changeState(new AddShapeState(model, o));
            });
            menuBar.add(button);
        }

        JMenuItem selectItem = new JMenuItem("Select");
        selectItem.addActionListener((e) -> {
			changeState(new SelectShapeState(model));
        });

		menuBar.add(selectItem);

        // primjerak platna za crtanje 
        this.canvas = new Canvas(model);
        addStateListener(canvas);
        canvas.setSize(this.getWidth(), this.getHeight()-menuBar.getHeight());
        canvas.requestFocusInWindow();
        add(canvas);

        KeyboardFocusManager.getCurrentKeyboardFocusManager()
        .addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if(e.getKeyCode() == 0) {       // esc
                    changeState(new IdleState());
                }
                return false;
            }
        });


        notifyStateListeners();
    }

    private void changeState(State state) {
        this.state.onLeaving();
        this.state = state;
        notifyStateListeners();
    }

    public void addStateListener(StateListener listener) { 
        this.stateListeners.add(listener);
    }

    public void removeStateListener(StateListener listener) {
        this.stateListeners.remove(listener);
    }

    public void notifyStateListeners() {
        for(StateListener listener : stateListeners) {
            listener.stateChanged(state);
        }
    }


    
}
