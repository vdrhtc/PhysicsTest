package baseObjects;

import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


public class TwoBodyConstraint extends Body {

	public TwoBodyConstraint(double mass, Coordinates c, double stiffness, double undeformedLength) {
		super(mass, c);
		this.setStiffness(stiffness);
		this.setUndeformedLenght(undeformedLength);
	}
	
	@Override
	public void update() {
		broadcastAppliedForce();
	}

	
	private void broadcastAppliedForce() {
		Vector force = calculateAppliedForce();
		startBody.applyForce(force);
		endBody.applyForce(force.neg());
	}
	
	
	private Vector calculateAppliedForce() {
		return getDirectiveVector().multiply(stiffness*calculateLengthDelta());
	}
	
	private double calculateLengthDelta() {
		currentLengthDelta = new Vector(startBody.getCoordinates(), endBody.getCoordinates()).mod()-undeformedLenght;
		return currentLengthDelta;
	}
	
	private Vector getDirectiveVector() {
		Vector vec = new Vector(startBody.getCoordinates(), endBody.getCoordinates());
		return vec.divide(vec.mod());
	}
	
	
	public Body getStartBody() {
		return startBody;
	}

	public void setStartBody(Body startBody) {
		this.startBody = startBody;
	}

	public Body getEndBody() {
		return endBody;
	}

	public void setEndBody(Body endBody) {
		this.endBody = endBody;
	}

	public double getStiffness() {
		return stiffness;
	}
	public void setStiffness(double stiffness) {
		this.stiffness = stiffness;
	}
	
	private void setUndeformedLenght(double undeformedLenght) {
		this.undeformedLenght = undeformedLenght;
	}

	@Override
	public void draw(GraphicsContext gc) {
		
		if(calculateLengthDelta()>0)
			gc.setStroke(Color.GREEN);
		else
			gc.setStroke(Color.RED);
		
		gc.strokeLine(startBody.getCoordinates().getX(), startBody.getCoordinates().getY(),
				endBody.getCoordinates().getX(), endBody.getCoordinates().getY());
	}
	private double stiffness;
	private double undeformedLenght;
	private double currentLengthDelta;
	
	private Body startBody;
	private Body endBody;
	
	private static Logger log = Logger.getAnonymousLogger();
	static {
		log.setLevel(Level.ALL);
	}
}
