package src.hr.fer.ooup.lab4.model.state;


import java.util.List;

import src.hr.fer.ooup.lab4.model.DocumentModel;
import src.hr.fer.ooup.lab4.model.GraphicalObject;
import src.hr.fer.ooup.lab4.model.Point;
import src.hr.fer.ooup.lab4.model.Rectangle;
import src.hr.fer.ooup.lab4.model.Renderer;

public class SelectShapeState implements State {

    private static final int HOT_POINT_SIZE = 2;

    private DocumentModel model;

    public SelectShapeState(DocumentModel model) {
        this.model = model;
    }

    @Override
    public void mouseDown(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
        if(!ctrlDown) {
            List<GraphicalObject> selected = model.getSelectedObjects();

            for(int i = selected.size()-1; i >= 0; i--) {
                selected.get(i).setSelected(false);
            }
        }

        GraphicalObject toSelect = model.findSelectedGraphicalObject(mousePoint);
        if(null != toSelect) {
            toSelect.setSelected(!toSelect.isSelected());
        }
    }

    @Override
    public void mouseUp(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
    }

    @Override
    public void mouseDragged(Point mousePoint) {
    }

    @Override
    public void keyPressed(int keyCode) {
        switch (keyCode) {
            case 0:
                
                break;
        
            default:
                break;
        }
    }

    // draw bounding box of selected shapes
    @Override
    public void afterDraw(Renderer r, GraphicalObject go) {
        if(!go.isSelected())
            return;

        Rectangle bb = go.getBoundingBox();
        Point tl = new Point(bb.getX(),                 bb.getY()                   );
        Point tr = new Point(bb.getX() + bb.getWidth(), bb.getY()                   );
        Point br = new Point(bb.getX() + bb.getWidth(), bb.getY() + bb.getHeight()  );
        Point bl = new Point(bb.getX(),                 bb.getY() + bb.getHeight()  );

        r.drawLine(tl, tr);
        r.drawLine(tr, br);
        r.drawLine(br, bl);
        r.drawLine(bl, tl);

        
    }

    // draw hot points of the only selected object
    @Override
    public void afterDraw(Renderer r) {
        List<GraphicalObject> selected = model.getSelectedObjects();
        if(selected.size() != 1) 
            return;

        GraphicalObject go = selected.get(0);
        for(int i = 0; i < go.getNumberOfHotPoints(); i++) {
            Point p = go.getHotPoint(i);
            r.fillPolygon(new Point[]{
                new Point(p.getX()-HOT_POINT_SIZE, p.getY()-HOT_POINT_SIZE), 
                new Point(p.getX()-HOT_POINT_SIZE, p.getY()+HOT_POINT_SIZE), 
                new Point(p.getX()+HOT_POINT_SIZE, p.getY()+HOT_POINT_SIZE),
                new Point(p.getX()+HOT_POINT_SIZE, p.getY()-HOT_POINT_SIZE)});
        }

    }

    @Override
    public void onLeaving() {
    }
    
}
