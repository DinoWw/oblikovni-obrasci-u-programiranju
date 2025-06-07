package src.hr.fer.ooup.lab4.graphics;

import java.awt.Color;
import java.awt.Graphics2D;
import src.hr.fer.ooup.lab4.model.Point;
import src.hr.fer.ooup.lab4.model.Renderer;

public class G2DRendererImpl implements Renderer {

	private Graphics2D g2d;
	
	public G2DRendererImpl(Graphics2D g2d) {
        this.g2d = g2d;
	}

    // Postavi boju na plavu
    // Nacrtaj linijski segment od S do E
	@Override
	public void drawLine(Point s, Point e) {
        g2d.setPaint(Color.BLUE);
        g2d.drawLine(s.getX(), s.getY(), e.getX(), e.getY());

	}

    // Postavi boju na plavu
    // Popuni poligon definiran danim točkama
    // Postavi boju na crvenu
    // Nacrtaj rub poligona definiranog danim točkama
	@Override
	public void fillPolygon(Point[] points) {
        // Fill
        int[] xPoints = new int[points.length];
        int[] yPoints = new int[points.length];
        for(int i = 0; i < points.length; i++) {
            xPoints[i] = points[i].getX();
            yPoints[i] = points[i].getY();
        }

        g2d.setPaint(Color.BLUE);
        g2d.fillPolygon(xPoints, yPoints, points.length);

        // Outline
        g2d.setPaint(Color.RED);
        g2d.drawLine(points[0].getX(), points[0].getY(), points[points.length-1].getX(), points[points.length-1].getY());
        for(int i = 1; i < points.length; i++) {
            g2d.drawLine(points[i-1].getX(), points[i-1].getY(), points[i].getX(), points[i].getY());
        }
	}

}
