package baseObjects;

import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import calculation.RadiusVector;
import calculation.Vector;

public class Spring extends Body {

	protected Spring(double stiffness, double undeformedLength,
			double currentLengthDelta, double maxLengthDelta, Body startBody, Body endBody) {
		super(0, new RadiusVector(0, 0));
		this.stiffness = stiffness;
		this.undeformedLength = undeformedLength ;
		this.currentLengthDelta = currentLengthDelta;
		this.maxLengthDelta = maxLengthDelta;
		this.startBody = startBody;
		this.endBody = endBody;
	}

	public Spring(double stiffness, double undeformedLength, Body startBody, Body endBody) {
		super(0, new RadiusVector(0, 0));
		this.stiffness = stiffness;
		this.undeformedLength = undeformedLength;
		this.startBody = startBody;
		this.endBody = endBody;
	}

	public Spring(double stiffness, Body body, Body body2) {
		super(0, new RadiusVector(0, 0));
		this.stiffness = stiffness;
		this.startBody = body;
		this.endBody = body2;
		this.undeformedLength = new Vector(body.getRadiusVector(), body2.getRadiusVector()).mod();
		
	}

	@Override
	public void updateForces() {
		updateFields();
		if(Math.abs(currentLengthDelta)<0.5)
			return;
		
		broadcastForce();
		return;
	}
	
	@Override
	public boolean updatePosition() {
		if(Math.abs(currentLengthDelta)<0.5)
			return false;
		return true;
	}

	protected void updateFields() {
		updateCurrentLength();
		updateLengthDelta();
		updateMaxLengthDelta();
	}

	protected void broadcastForce() {
		Vector force = calculateAppliedForce();
		startBody.applyForce(force);
		endBody.applyForce(force.neg());
	}

	private void updateCurrentLength() {
		currentLength = Vector.calculateDistance(startBody.getRadiusVector(),
				endBody.getRadiusVector());
	}

	private double updateLengthDelta() {
		currentLengthDelta = currentLength
				- undeformedLength;
		return currentLengthDelta;
	}

	private void updateMaxLengthDelta() {
		if (Math.abs(maxLengthDelta) < Math.abs(currentLengthDelta))
			maxLengthDelta = currentLengthDelta;
	}
	
	protected Vector calculateAppliedForce() {
		return getDirectiveVector().mul(stiffness * currentLengthDelta);
	}


	public Vector getDirectiveVector() {
		Vector vec = new Vector(startBody.getRadiusVector(),
				endBody.getRadiusVector());
		return vec.div(currentLength);
	}

	public double getEnergy() {
		return this.currentLengthDelta * this.currentLengthDelta * this.stiffness / 2;
	}

	public Body getStartBody() {
		return startBody;
	}


	public Body getEndBody() {
		return endBody;
	}


	public double getStiffness() {
		return stiffness;
	}

	@Override
	public void draw(GraphicsContext gc) {

		double xs = startBody.getRadiusVector().getX();
		double ys = startBody.getRadiusVector().getY();
		double xe = endBody.getRadiusVector().getX();
		double ye = endBody.getRadiusVector().getY();

		if (currentLengthDelta > 0)
			gc.setStroke(Color.GREEN);
		if (currentLengthDelta < 0)
			gc.setStroke(Color.RED);
		if(Math.abs(currentLengthDelta)<0.5)
			gc.setStroke(Color.BLACK);

		gc.strokeLine(xs, ys, xe, ye);
		log.info(maxLengthDelta + "");

//		gc.strokeText(String.format(Locale.US, "%.1f", currentLengthDelta) + "\n"
//				+ String.format(Locale.US, "%.0f", stiffness), (xs + xe) / 2,
//				(ys + ye) / 2);
	}

	@Override
	public Spring clone() {
		Spring copy = new Spring(stiffness, undeformedLength,
				currentLengthDelta, maxLengthDelta, startBody.clone(), endBody.clone());
		return copy;
	}
	
	public double getCurrentLengthDelta() {
		return currentLengthDelta;
	}
	
	public double getUndeformedLength() {
		return undeformedLength;
	}
	
	public double getMaxLengthDelta() {
		return maxLengthDelta;
	}
	
//	private final double period;

	private final double stiffness;
	private final double undeformedLength;
	private double currentLengthDelta;
	private double currentLength;
	private double maxLengthDelta;

	private final Body startBody;
	private final Body endBody;

	private static final Logger log = Logger.getAnonymousLogger();
	static {
		log.setLevel(Level.OFF);
	}
}
