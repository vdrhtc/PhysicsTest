package calculation;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import org.javatuples.Pair;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import baseObjects.Ball;
import baseObjects.Body;
import baseObjects.Collisive;
import baseObjects.CollisiveType;

public class Ether {
	
	public double GRID_STEP = 10;
	
	public Ether() {
		
	}
	

	public synchronized void modifyInteractionFieldFor(Collisive c) {		
		if(fieldNeedsNoModification(c))
			return;
		if(c.getCollisiveType() != CollisiveType.BALL)
			return;
		
		DiscreteRect r = c.getFieldArea();
		
		for (int x = r.getxInteval().getValue0(); x<=r.getxInteval().getValue1(); x+=GRID_STEP) {
			for (int y = r.getyInterval().getValue0(); y<=r.getyInterval().getValue1(); y+=GRID_STEP) {
				addCollisiveBosonToPoint(new RadiusVector(x, y), c);
				
			}
		}
	}
	
	
	private DiscreteRect calculateFieldArea(Collisive c) {

		Pair<Integer, Integer> xInterval = calculateBboxXFor(c);
		Pair<Integer, Integer> yInterval = calculateBboxYFor(c);
		return new DiscreteRect(xInterval, yInterval);
	}
	
	private boolean fieldNeedsNoModification(Collisive c) {
		DiscreteRect newArea = calculateFieldArea(c);
		if(c.getFieldArea()==null){
			c.modifyFieldArea(newArea);
			return false;
		}
		
		if(!c.getFieldArea().equals(newArea)) {
			c.modifyFieldArea(newArea);
			return false;
		}
		
		return true;
		
	}

	private Pair<Integer, Integer> calculateBboxYFor(Collisive c) {
		Ball b = (Ball) c;

		int discreteUpperBound =(int) (GRID_STEP*(Math.floor( (b.getRadiusVector().getY()-2*b.getRadius())/GRID_STEP )));
		int discreteLowerBound = (int) (GRID_STEP*(Math.ceil( (b.getRadiusVector().getY()+2*b.getRadius())/GRID_STEP )));
		return new Pair<Integer, Integer>(discreteUpperBound, discreteLowerBound);
	}
	
	
	private Pair<Integer, Integer> calculateBboxXFor(Collisive c) {
		Ball b = (Ball) c;
		
		int discreteLeftBound =(int) (GRID_STEP*(Math.floor( (b.getRadiusVector().getX()-2*b.getRadius())/GRID_STEP )));
		int discreteRightBound = (int) (GRID_STEP*(Math.ceil( (b.getRadiusVector().getX()+2*b.getRadius())/GRID_STEP )));
		return new Pair<Integer, Integer>(discreteLeftBound, discreteRightBound);
	}
	
	public synchronized ArrayList<Collisive> getBosonOwnersNear(Collisive c) {
		return grid.get(discretizeRadiusVector(c));
	}
	
	public void removeCollisiveBosonFromPoint(RadiusVector point,
			Collisive c) {
		if(grid.get(point).size()==1)
			grid.remove(point);
		else
			grid.get(point).remove(c);
	}
	
	public void addCollisiveBosonToPoint(RadiusVector point, Collisive c) {
		if(grid.containsKey(point)) {
			grid.get(point).add(c);
			return;
		}
		ArrayList<Collisive> a = new ArrayList<>();
		a.add(c);
		grid.put(point, a);
	}
	
	private RadiusVector discretizeRadiusVector(Collisive c) {
		Body body = (Body) c;
		double discretizedX = 0;
		double discretizedY = 0;
		if(body.getVelocity().getX()>0) {
			discretizedX = GRID_STEP*Math.ceil(c.getRadiusVector().getX()/GRID_STEP);
		}
		else if(body.getVelocity().getX()<=0) {
			discretizedX = GRID_STEP*Math.floor(c.getRadiusVector().getX()/GRID_STEP);
		}
		if(body.getVelocity().getY()>0 ) {
			discretizedY = GRID_STEP*Math.ceil(c.getRadiusVector().getY()/GRID_STEP);	
		}
		else if(body.getVelocity().getY()<=0) {
			discretizedY = GRID_STEP*Math.floor(c.getRadiusVector().getY()/GRID_STEP);	
		}
		return new RadiusVector(discretizedX, discretizedY);

	}
	
	public void draw(GraphicsContext gc) {
		gc.setStroke(Color.RED);
		
		for (RadiusVector v : grid.keySet())
		gc.fillOval(v.getX()-1, v.getY()
				- 1, 2, 2);
		
	}
	
	
	public void clearGrid() {
		grid = new ConcurrentHashMap<>(100*SystemStateComputer.getNumberOfBodies());
	}
	
	public ArrayList<RadiusVector> getBosons() {
		ArrayList<RadiusVector> bosons = new ArrayList<>(grid.keySet().size());
		for (RadiusVector r : grid.keySet()) {
			bosons.add(r);
		}
		return bosons;
	}
	
	
	private ConcurrentHashMap<RadiusVector, ArrayList<Collisive>> grid = new ConcurrentHashMap<>(100*SystemStateComputer.getNumberOfBodies());



}
