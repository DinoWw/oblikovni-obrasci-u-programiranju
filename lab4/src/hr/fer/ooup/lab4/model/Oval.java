package src.hr.fer.ooup.lab4.model;

public class Oval extends AbstractGraphicalObject {

    private Point center;

    Oval(Point right, Point bottom) {
            super(new Point[]{right, bottom});
            this.center = new Point(bottom.getX(), right.getY());
    }
    Oval() {
            super(new Point[]{new Point(10, 0), new Point(0, 10)});
            this.center = new Point(getHotPoint(0).getX(), getHotPoint(1).getY());
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
        return new Rectangle(minx-dx, miny-dy, dx, dy);
    }

    @Override
    public double selectionDistance(Point mousePoint) {
        this.center = new Point(getHotPoint(0).getX(), getHotPoint(1).getY());
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
}
