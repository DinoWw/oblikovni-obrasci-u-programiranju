package src.hr.fer.ooup.lab4.model;

public class LineSegment extends AbstractGraphicalObject {

    public LineSegment(Point a, Point b) {
            super(new Point[]{a, b});
    }
    public LineSegment() {
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
    @Override
    public void render(Renderer r) {
        final int hpSize = 2;
        
        r.drawLine(getHotPoint(0), getHotPoint(1));
        for(int i = 0; i < getNumberOfHotPoints(); i++) {
            Point p = getHotPoint(i);
            r.fillPolygon(new Point[]{
                new Point(p.getX()-hpSize, p.getY()-hpSize), 
                new Point(p.getX()-hpSize, p.getY()+hpSize), 
                new Point(p.getX()+hpSize, p.getY()+hpSize),
                new Point(p.getX()+hpSize, p.getY()-hpSize)});
        }
    }
}
