package baseObjects;

import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import calculation.Coordinates;
import calculation.Vector;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class TwoBodyConstraint extends Body {

	public TwoBodyConstraint(double mass, Coordinates c, double stiffness,
			double undeformedLength, double currentLengthDelta,
			double maxLengthDelta) {
		super(mass, c);
		this.stiffness = stiffness;
		this.undeformedLength = undeformedLength;
		this.currentLengthDelta = currentLengthDelta;
		this.maxLengthDelta = maxLengthDelta;
	}

	public TwoBodyConstraint(double mass, Coordinates c, double stiffness,
			double undeformedLength) {
		super(mass, c);
		this.stiffness = stiffness;
		this.undeformedLength = undeformedLength;
	}

	@Override
	public void update() {
		currMod = Math.sqrt((startBody.getCoordinates().getX() - endBody
				.getCoordinates().getX())
				* (startBody.getCoordinates().getX() - endBody.getCoordinates()
						.getX())
				+ (startBody.getCoordinates().getY() - endBody.getCoordinates()
						.getY())
				* (startBody.getCoordinates().getY() - endBody.getCoordinates()
						.getY()));
		broadcastAppliedForce();
	}

	private void broadcastAppliedForce() {
		Vector force = calculateAppliedForce();
		startBody.applyForce(force);
		endBody.applyForce(force.neg());
	}

	private Vector calculateAppliedForce() {
		return getDirectiveVector().mul(stiffness * calculateLengthDelta());
	}

	private double calculateLengthDelta() {
		currentLengthDelta = new Vector(startBody.getCoordinates(),
				endBody.getCoordinates()).mod()
				- undeformedLength;
		if (Math.abs(maxLengthDelta) < Math.abs(currentLengthDelta)) {
			maxLengthDelta = currentLengthDelta;
		} else if(Math.abs(currentLengthDelta)<1) 
			maxLengthDelta = currentLengthDelta;
		return currentLengthDelta;
	}

	private Vector getDirectiveVector() {
		Vector vec = new Vector(startBody.getCoordinates(),
				endBody.getCoordinates());
		return vec.div(currMod);
	}

	public double getEnergy() {
		return Math.pow(this.currentLengthDelta, 2) * this.stiffness / 2;
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

		double xs = startBody.getCoordinates().getX();
		double ys = startBody.getCoordinates().getY();
		double xe = endBody.getCoordinates().getX();
		double ye = endBody.getCoordinates().getY();

		if (currentLengthDelta > 0)
			gc.setStroke(Color.GREEN);
		else
			gc.setStroke(Color.RED);

		gc.strokeLine(xs, ys, xe, ye);
//		log.info(maxLengthDelta+"");
		
		gc.strokeText(String.format(Locale.US, "%.1f", maxLengthDelta)+"\n"+String.format(Locale.US, "%.0f", stiffness),
				(xs + xe) / 2, (ys + ye) / 2);
	}

	@Override
	public TwoBodyConstraint clone() {
		TwoBodyConstraint copy = new TwoBodyConstraint(getMass(),
				getCoordinates(), stiffness, undeformedLength,
				currentLengthDelta, maxLengthDelta);
		copy.setStartBody(startBody.clone());
		copy.setEndBody(endBody.clone());
		return copy;
	}

	private double stiffness;
	private double undeformedLength;
	private double currentLengthDelta;
	private double currMod;
	private double maxLengthDelta;

	private Body startBody;
	private Body endBody;

	private static Logger log = Logger.getAnonymousLogger();
	static {
		log.setLevel(Level.ALL);
	}
}
