package src.hr.fer.ooup.lab4.graphics;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;

import src.hr.fer.ooup.lab4.model.DocumentModel;
import src.hr.fer.ooup.lab4.model.GraphicalObject;
import src.hr.fer.ooup.lab4.model.Point;
import src.hr.fer.ooup.lab4.model.Renderer;
import src.hr.fer.ooup.lab4.model.state.State;
import src.hr.fer.ooup.lab4.model.state.StateListener;

public class Canvas extends JComponent implements KeyListener, StateListener, MouseListener, MouseMotionListener {
    
    private DocumentModel model;
    private State guiState;

    public Canvas(DocumentModel model) {
        this.model = model;

        setFocusable(true);

        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);

    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;
        Renderer r = new G2DRendererImpl(g2d);
        for(GraphicalObject o : model.list()){ 
            o.render(r);
            guiState.afterDraw(r, o);
        }

        guiState.afterDraw(r);
    }

    @Override
    public void stateChanged(State state) {
        this.guiState = state;
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        guiState.keyPressed(e.getKeyCode());
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
        guiState.mouseDown(new Point(e.getPoint()), e.isShiftDown(), e.isControlDown());
    }
    
    @Override
    public void mouseReleased(MouseEvent e) {
        guiState.mouseUp(new Point(e.getPoint()), e.isShiftDown(), e.isControlDown());
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {
        guiState.mouseDragged(new Point(e.getPoint()));
    }


    // EMPTY METHODS
    
    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

}
