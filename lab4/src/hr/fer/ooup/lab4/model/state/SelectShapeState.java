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
        List<GraphicalObject> selected = model.getSelectedObjects();
        if(selected.size() == 1) {    //drag control points
            GraphicalObject o = selected.get(0);
            for(int i = 0; i < o.getNumberOfHotPoints(); i++) {
                if(o.getHotPointDistance(i, mousePoint) <= DocumentModel.SELECTION_PROXIMITY) {
                    o.setHotPointSelected(i, true);
                    return;
                }
            }
        }


        if(!ctrlDown) {
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
        List<GraphicalObject> selected = model.getSelectedObjects();
        if(selected.size() == 1) {
            GraphicalObject o = selected.get(0);
            for(int i = o.getNumberOfHotPoints()-1; i >= 0; i--) {
                o.setHotPointSelected(i, false);
            }
        }
    }

    @Override
    public void mouseDragged(Point mousePoint) {
        System.out.println("dragged");
        List<GraphicalObject> selected = model.getSelectedObjects();
        if(selected.size() == 1) {    //drag control points
        System.out.println("dragged only one");
            GraphicalObject o = selected.get(0);
            for(int i = 0; i < o.getNumberOfHotPoints(); i++) {
                if(o.isHotPointSelected(i)) {
        System.out.println("dragged selected");
        System.out.println("mouse" + mousePoint.getX() + "x y" + mousePoint.getY());
        Point hp = o.getHotPoint(i);
        System.out.println("hp" + hp.getX() + "x y" + hp.getY());
                    o.setHotPoint(i, hp.translate(mousePoint.difference(hp)));
                }
            }
        }
    }

    @Override
    public void keyPressed(int keyCode) {
        System.out.println(keyCode);
        switch (keyCode) {
            case 61:    // plus
            {
                List<GraphicalObject> selected = model.getSelectedObjects();
                for(int i = selected.size()-1; i >= 0; i--) {
                    model.increaseZ(selected.get(i));
                }
                break;
            }
            case 45:    // minus
            {
                model.getSelectedObjects().forEach(o -> model.decreaseZ(o));
                break;
            }
            case 37:    // left
            {
                model.getSelectedObjects().forEach(o -> o.translate(new Point(-1, 0)));
                break;
            }
            case 38:    // up
            {
                model.getSelectedObjects().forEach(o -> o.translate(new Point(0, -1)));
                break;
            }
            case 39:    // right
            {
                model.getSelectedObjects().forEach(o -> o.translate(new Point(1, 0)));
                break;
            }
            case 40:    // down
            {
                model.getSelectedObjects().forEach(o -> o.translate(new Point(0, 1)));
                break;
            }
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

        List<GraphicalObject> selected = model.getSelectedObjects();
        for(int i = selected.size()-1; i >= 0; i--) {
            selected.get(i).setSelected(false);
        }
    }
    
}
