package src.hr.fer.ooup.lab4.model;

public interface Renderer {
	void drawLine(Point s, Point e);
	void fillPolygon(Point[] points);
}