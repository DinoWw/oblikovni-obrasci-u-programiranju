package src.hr.fer.ooup.lab4.model;

import java.util.ArrayList;
import java.util.List;

public class Oval extends AbstractGraphicalObject {

	public final static double RESOLUTION = 50;

    private Point center;

    public Oval(Point right, Point bottom) {
            super(new Point[]{right, bottom});
            calculateCenter();
    }
    public Oval() {
            super(new Point[]{new Point(10, 0), new Point(0, 10)});
            calculateCenter();
    }

    public void translate(Point point) {
        super.translate(point);
        calculateCenter();
    }

    private void calculateCenter() {
        this.center = new Point(getHotPoint(1).getX(), getHotPoint(0).getY());
    }

    @Override
    public Rectangle getBoundingBox() {
        Point hp1 = getHotPoint(0);
        Point hp2 = getHotPoint(1);
        int maxx = Math.max(hp1.getX(), hp2.getX());
        int minx = Math.min(hp1.getX(), hp2.getX());
        int maxy = Math.max(hp1.getY(), hp2.getY());
        int miny = Math.min(hp1.getY(), hp2.getY());
        int dx = maxx-minx;
        int dy = maxy-miny;
        return new Rectangle(minx-dx, miny-dy, 2*dx, 2*dy);
    }

    @Override
    public double selectionDistance(Point mousePoint) {
        return GeometryUtil.distanceFromPoint(this.center, mousePoint) 
            - Math.max(
                GeometryUtil.distanceFromPoint(getHotPoint(0), center),
                GeometryUtil.distanceFromPoint(getHotPoint(1), center));
    }

    @Override
    public String getShapeName() {
        return "Oval";
    }

    @Override
    public GraphicalObject duplicate() {
        return new Oval(getHotPoint(0), getHotPoint(1));
    }
    @Override
    public void render(Renderer r) {
        
        Point h1 = getHotPoint(0);
        Point h2 = getHotPoint(1);
        System.out.println(center.getX() + "x y " + center.getY());
        List<Point> ovalPoints = new ArrayList<>();
        for(double a = 0; a < Math.TAU; a += Math.TAU / RESOLUTION) {
            ovalPoints.add(new Point(
                (int)( center.getX() + (h1.getX()-center.getX())*Math.cos(a) ), 
                (int)( center.getY() + (h2.getY()-center.getY())*Math.sin(a) )));
            
        }
        r.fillPolygon(ovalPoints.toArray(new Point[0]));
        
    }
}
