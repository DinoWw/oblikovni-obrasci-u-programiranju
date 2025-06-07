package src.hr.fer.ooup.lab4;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import src.hr.fer.ooup.lab4.model.GraphicalObject;
import src.hr.fer.ooup.lab4.model.LineSegment;
import src.hr.fer.ooup.lab4.model.Oval;

public class Main {
		public static void main(String[] args) {

		List<GraphicalObject> objects = new ArrayList<>();

		objects.add(new LineSegment());
		objects.add(new Oval());

		GUI gui = new GUI(objects);

		gui.setTitle("OOUP");
		gui.setSize(700,500);
		gui.setLocation(new java.awt.Point(300,200));
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);



		gui.setVisible(true);
	}
}
