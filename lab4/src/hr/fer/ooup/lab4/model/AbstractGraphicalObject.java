package src.hr.fer.ooup.lab4.model;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractGraphicalObject implements GraphicalObject{
    private Point[] hotPoints;
    private boolean[] hotPointSelected;
    private boolean selected;
    List<GraphicalObjectListener> listeners = new ArrayList<>();

    protected AbstractGraphicalObject(Point[] hotPoints) {
        this.hotPoints = hotPoints;
        this.hotPointSelected = new boolean[hotPoints.length];
        selected = false;
    }

    public Point getHotPoint(int index) {
	    return hotPoints[index];
    }
    public void setHotPoint(int index, Point point) {
        hotPoints[index] = point;
        alertGraphicalObjectChanged();
    }
	
    public int getNumberOfHotPoints() {
	    return hotPoints.length;
    }

    public double getHotPointDistance(int index, Point point) {
	    return GeometryUtil.distanceFromPoint(hotPoints[index], point);
    }

    public boolean isHotPointSelected(int index) {
	    return hotPointSelected[index];
    }
	
    public void setHotPointSelected(int index, boolean selected) {
	    this.hotPointSelected[index] = selected;
        alertGraphicalObjectSelectionChanged();
    }

    public boolean isSelected() {
	    return this.selected;
    }

    @Override
    public void setSelected(boolean selected) {
	    this.selected = selected;
        alertGraphicalObjectSelectionChanged();
	}

    public void translate(Point point) {
        for(int i = 0; i < hotPoints.length; i++) {
            hotPoints[i] = hotPoints[i].translate(point);
        }
        alertGraphicalObjectChanged();
    }

    public void addGraphicalObjectListener(GraphicalObjectListener lis) {
        this.listeners.add(lis);
    }

    public void removeGraphicalObjectListener(GraphicalObjectListener lis) {
        this.listeners.remove(lis);
    }

    protected void alertGraphicalObjectChanged() {
        for(GraphicalObjectListener lis : listeners) {
            lis.graphicalObjectChanged(this);
        }
    }
    
    protected void alertGraphicalObjectSelectionChanged() {
        for(GraphicalObjectListener lis : listeners) {
            lis.graphicalObjectSelectionChanged(this);
        }
    }
}
