package baseObjects;

import java.util.logging.Level;
import java.util.logging.Logger;

import calculation.Coordinates;
import calculation.Vector;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


public class TwoBodyConstraint extends Body {

	public TwoBodyConstraint(double mass, Coordinates c, double stiffness, double undeformedLength, double currentLengthDelta) {
		super(mass, c);
		this.stiffness = stiffness;
		this.undeformedLength = undeformedLength;
		this.currentLengthDelta = currentLengthDelta;
	}
	public TwoBodyConstraint(double mass, Coordinates c, double stiffness, double undeformedLength) {
		super(mass, c);
		this.stiffness = stiffness;
		this.undeformedLength = undeformedLength;
	}
	@Override
	public void update() {
		broadcastAppliedForce();
		if(log.isLoggable(Level.INFO))
			log.info(this.endBody.getVelocity().toString());
	}

	
	private void broadcastAppliedForce() {
		Vector force = calculateAppliedForce();
		startBody.applyForce(force);
		endBody.applyForce(force.neg());
	}
	
	
	private Vector calculateAppliedForce() {
		return getDirectiveVector().mul(stiffness*calculateLengthDelta());
	}
	
	private double calculateLengthDelta() {
		currentLengthDelta = new Vector(startBody.getCoordinates(), endBody.getCoordinates()).mod()-undeformedLength;
		return currentLengthDelta;
	}
	
	
	private Vector getDirectiveVector() {
		Vector vec = new Vector(startBody.getCoordinates(), endBody.getCoordinates());
		return vec.div(vec.mod());
	}
	
	
	public double getEnergy() {
		return Math.pow(this.currentLengthDelta, 2)*this.stiffness/2;
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

	@Override
	public void draw(GraphicsContext gc) {
		
		if(calculateLengthDelta()>0)
			gc.setStroke(Color.GREEN);
		else
			gc.setStroke(Color.RED);
		
		gc.strokeLine(startBody.getCoordinates().getX(), startBody.getCoordinates().getY(),
				endBody.getCoordinates().getX(), endBody.getCoordinates().getY());
	}
	
	@Override
	public TwoBodyConstraint clone() {
		TwoBodyConstraint copy = new TwoBodyConstraint(getMass(), getCoordinates(), stiffness, undeformedLength, currentLengthDelta);
		copy.setStartBody(startBody.clone());
		copy.setEndBody(endBody.clone());
		return copy;
	}
	private double stiffness;
	private double undeformedLength;
	private double currentLengthDelta;
	
	private Body startBody;
	private Body endBody;
	
	private static Logger log = Logger.getAnonymousLogger();
	static {
		log.setLevel(Level.OFF);
	}
}
