package src.hr.fer.ooup.lab4.model;

public class GeometryUtil {

    // izračunaj euklidsku udaljenost između dvije točke
	public static double distanceFromPoint(Point point1, Point point2) {
        Point d = point1.difference(point2);
        return Math.sqrt(d.getX() * d.getX() + d.getY() * d.getY());
	}
	
    // Izračunaj koliko je točka P udaljena od linijskog segmenta određenog
    // početnom točkom S i završnom točkom E. Uočite: ako je točka P iznad/ispod
    // tog segmenta, ova udaljenost je udaljenost okomice spuštene iz P na S-E.
    // Ako je točka P "prije" točke S ili "iza" točke E, udaljenost odgovara
    // udaljenosti od P do početne/konačne točke segmenta.
	public static double distanceFromLineSegment(Point s, Point e, Point p) {
        if(dot(e.difference(s), p.difference(s)) < 0) {
            return distanceFromPoint(s, p);
        }
        else if(dot(s.difference(e), p.difference(e)) < 0) {
            return distanceFromPoint(e, p);
        }
        // else
        return Math.abs(
            (e.getY() - s.getY()) * p.getX() 
            - (e.getX() - s.getX()) * p.getY() 
            + e.getX() * s.getY() - e.getY() * s.getX()
            ) / distanceFromPoint(s, e);
	}

    private static double dot(Point a, Point b) {
        return a.getX()*b.getX() + a.getY()*b.getY();
    }


}