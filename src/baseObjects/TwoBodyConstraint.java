package baseObjects;

import java.util.logging.Logger;


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
		return new Vector(startBody.getCoordinates(), endBody.getCoordinates()).mod()-undeformedLenght;
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

	private double stiffness;
	private double undeformedLenght;
	
	private Body startBody;
	private Body endBody;
	
	Logger log = Logger.getAnonymousLogger();

}
