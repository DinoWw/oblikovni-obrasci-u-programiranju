// visual studio forsira src. 
package src.hr.fer.ooup.lab4;

import src.hr.fer.ooup.lab4.model.GeometryUtil;
import src.hr.fer.ooup.lab4.model.Point;

public class GUI {

    public static void main(String[] args) {
        Point a = new Point(0, 1);
        Point b = new Point(1, 0);
        Point c = new Point(0, 2);

        System.out.println(GeometryUtil.distanceFromLineSegment(a, b, c));
    }

}
