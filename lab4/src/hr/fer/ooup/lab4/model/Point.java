package src.hr.fer.ooup.lab4.model;

public class Point {

	private int x;
	private int y;
	
	public Point(int x, int y) {
        this.x = x;
        this.y = y;
	}
	
	public Point(java.awt.Point p) {
        this.x = (int)p.getX();
        this.y = (int)p.getY();
	}
	
	public int getX() {
        return x;
	}
	
	public int getY() {
        return y;
	}

	public Point translate(Point dp) {
        return new Point(x + dp.x, y + dp.y);
	}
	
	public Point difference(Point p) {
        return new Point(x - p.x, y - p.y);
	}
}