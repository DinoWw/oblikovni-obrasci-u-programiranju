package src.hr.fer.ooup.lab4.model.state;


import src.hr.fer.ooup.lab4.model.DocumentModel;
import src.hr.fer.ooup.lab4.model.GraphicalObject;
import src.hr.fer.ooup.lab4.model.Point;
import src.hr.fer.ooup.lab4.model.Renderer;

public class AddShapeState implements State {
	
	private GraphicalObject prototype;
	private DocumentModel model;
	
	public AddShapeState(DocumentModel model, GraphicalObject prototype) {
        this.model = model;
        this.prototype = prototype;
	}

    // dupliciraj zapamćeni prototip, pomakni ga na poziciju miša i dodaj u model
	@Override
	public void mouseDown(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
        GraphicalObject o = prototype.duplicate();
        o.translate(new src.hr.fer.ooup.lab4.model.Point((int)mousePoint.getX(), (int)mousePoint.getY()));
        model.addGraphicalObject(o);
	}

    @Override
    public void mouseUp(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
    }

    @Override
    public void mouseDragged(Point mousePoint) {
    }

    @Override
    public void keyPressed(int keyCode) {
    }

    @Override
    public void afterDraw(Renderer r, GraphicalObject go) {
    }

    @Override
    public void afterDraw(Renderer r) {
    }

    @Override
    public void onLeaving() {
    }
}
