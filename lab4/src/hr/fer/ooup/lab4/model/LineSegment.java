package src.hr.fer.ooup.lab4.model;

public class LineSegment extends AbstractGraphicalObject {

    LineSegment(Point a, Point b) {
            super(new Point[]{a, b});
    }
    LineSegment() {
            super(new Point[]{new Point(0, 0), new Point(10, 0)});
    }

    @Override
    public Rectangle getBoundingBox() {
        Point hp1 = getHotPoint(0);
        Point hp2 = getHotPoint(1);
        int maxx = Math.max(hp1.getX(), hp2.getX());
        int minx = Math.min(hp1.getX(), hp2.getX());
        int maxy = Math.max(hp1.getY(), hp2.getY());
        int miny = Math.min(hp1.getY(), hp2.getY());
        return new Rectangle(minx, miny, maxx-minx, maxy-miny);
    }

    @Override
    public double selectionDistance(Point mousePoint) {
        return GeometryUtil.distanceFromLineSegment(getHotPoint(0), getHotPoint(1), mousePoint);
    }

    @Override
    public String getShapeName() {
        return "Linija";
    }

    @Override
    public GraphicalObject duplicate() {
        return new LineSegment(getHotPoint(0), getHotPoint(1));
    }
}
