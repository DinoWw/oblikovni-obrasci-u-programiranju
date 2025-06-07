package src.hr.fer.ooup.lab4.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DocumentModel {

	public final static double SELECTION_PROXIMITY = 10;

	// Kolekcija svih grafičkih objekata:
	private List<GraphicalObject> objects = new ArrayList<>();
	// Read-Only proxy oko kolekcije grafičkih objekata:
	private List<GraphicalObject> roObjects = Collections.unmodifiableList(objects);
	// Kolekcija prijavljenih promatrača:
	private List<DocumentModelListener> listeners = new ArrayList<>();
	// Kolekcija selektiranih objekata:
	private List<GraphicalObject> selectedObjects = new ArrayList<>();
	// Read-Only proxy oko kolekcije selektiranih objekata:
	private List<GraphicalObject> roSelectedObjects = Collections.unmodifiableList(selectedObjects);

	// Promatrač koji će biti registriran nad svim objektima crteža...
	private final GraphicalObjectListener goListener = new GraphicalObjectListener() {
        @Override
        public void graphicalObjectChanged(GraphicalObject go) {
            notifyListeners();
        };
        @Override
        public void graphicalObjectSelectionChanged(GraphicalObject go) {
            if(go.isSelected() && !selectedObjects.contains(go)) {
                selectedObjects.add(go);
            }
            else if (!go.isSelected() && selectedObjects.contains(go)){
                selectedObjects.remove(go);
            }
            notifyListeners();
        };
    };
	
	// Konstruktor...
	public DocumentModel() {

    }

	// Brisanje svih objekata iz modela (pazite da se sve potrebno odregistrira)
	// i potom obavijeste svi promatrači modela
	public void clear() {
        for(GraphicalObject go : this.objects) {
            go.removeGraphicalObjectListener(goListener);
        }

        this.objects.clear();
        this.selectedObjects.clear();

        notifyListeners();
    }

	// Dodavanje objekta u dokument
	public void addGraphicalObject(GraphicalObject obj) {
        this.objects.add(obj);

        if(obj.isSelected()) {
            this.selectedObjects.add(obj);
        }
        
        obj.addGraphicalObjectListener(goListener);
        notifyListeners();
    }
	
	// Uklanjanje objekta iz dokumenta
	public void removeGraphicalObject(GraphicalObject obj) {
        this.objects.remove(obj);

        if(obj.isSelected()) {
            this.selectedObjects.remove(obj);
        }

        obj.removeGraphicalObjectListener(goListener);
        
        notifyListeners();
    }

	// Vrati nepromjenjivu listu postojećih objekata
	public List<GraphicalObject> list() {
        return roObjects;
    }

	// Prijava...
	public void addDocumentModelListener(DocumentModelListener l) {
        this.listeners.add(l);
    }
	
	// Odjava...
	public void removeDocumentModelListener(DocumentModelListener l) {
        this.listeners.remove(l);
    }

	// Obavještavanje...
	public void notifyListeners() {
        for(DocumentModelListener l : listeners) {
            l.documentChange();
        }
    }
	
	// Vrati nepromjenjivu listu selektiranih objekata
	public List<GraphicalObject> getSelectedObjects() {
        return this.roSelectedObjects;
    }

	// Pomakni predani objekt u listi objekata na jedno mjesto kasnije...
	// Time će se on iscrtati kasnije (pa će time možda veći dio biti vidljiv)
	public void increaseZ(GraphicalObject go) {
        int index = objects.indexOf(go);
        if(index >= objects.size()-1)
            return;
        objects.remove(index);
        objects.add(index + 1, go);
    }
	
	// Pomakni predani objekt u listi objekata na jedno mjesto ranije...
	public void decreaseZ(GraphicalObject go) {
        int index = objects.indexOf(go);
        if(index <= 0)
            return;
        objects.remove(index);
        objects.add(index - 1, go);}
	
	// Pronađi postoji li u modelu neki objekt koji klik na točku koja je
	// predana kao argument selektira i vrati ga ili vrati null. Točka selektira
	// objekt kojemu je najbliža uz uvjet da ta udaljenost nije veća od
	// SELECTION_PROXIMITY. Status selektiranosti objekta ova metoda NE dira.
	public GraphicalObject findSelectedGraphicalObject(Point mousePoint) {
        for(GraphicalObject go : objects) {
            if(go.selectionDistance(mousePoint) > SELECTION_PROXIMITY) 
                continue;
            return go;
        }
        return null;
    }

	// Pronađi da li u predanom objektu predana točka miša selektira neki hot-point.
	// Točka miša selektira onaj hot-point objekta kojemu je najbliža uz uvjet da ta
	// udaljenost nije veća od SELECTION_PROXIMITY. Vraća se indeks hot-pointa 
	// kojeg bi predana točka selektirala ili -1 ako takve nema. Status selekcije 
	// se pri tome NE dira.
	public int findSelectedHotPoint(GraphicalObject object, Point mousePoint) {
        for(int i = 0; i < object.getNumberOfHotPoints(); i ++) {
            if(GeometryUtil.distanceFromPoint(object.getHotPoint(i), mousePoint) > SELECTION_PROXIMITY) 
                continue;
            return i;
        }
        return -1;
    }
}