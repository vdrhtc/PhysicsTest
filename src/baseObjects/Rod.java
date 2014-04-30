package baseObjects;

import java.util.Locale;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import calculation.Vector;

public class Rod extends Spring {

	public static final double STIFFNESS = 1e4;
	
	public Rod(double length, Body startBody, Body endBody) {
		super(STIFFNESS, length, startBody, endBody);
	}
	
	public Rod(double length,
			double currentLengthDelta, double maxLengthDelta, Body startBody, Body endBody, double extensionRate) {
		
		super(STIFFNESS, length, currentLengthDelta, maxLengthDelta, startBody, endBody);
		this.extensionRate = extensionRate;
	}
	
	@Override
	protected Vector calculateAppliedForce() {
		Vector directive = getDirectiveVector();
		double delta = getCurrentLengthDelta();
		double projectedVelocityOfStart = Vector.dotProduct(getStartBody().getVelocity(), directive);
		double projectedVelocityOfEnd = Vector.dotProduct(getEndBody().getVelocity(), directive);
		
		extensionRate = (projectedVelocityOfEnd-projectedVelocityOfStart);
		
		return getDirectiveVector().mul(STIFFNESS * delta+10*extensionRate);
	}
	
	@Override
	public void draw(GraphicsContext gc) {
		double currentLengthDelta = getCurrentLengthDelta();
		Body startBody = getStartBody();
		Body endBody = getEndBody();
		
		double xs = startBody.getRadiusVector().getX();
		double ys = startBody.getRadiusVector().getY();
		double xe = endBody.getRadiusVector().getX();
		double ye = endBody.getRadiusVector().getY();

		if (currentLengthDelta > 0)
			gc.setStroke(Color.GREEN);
		else if (currentLengthDelta < 0)
			gc.setStroke(Color.RED);
		else
			gc.setStroke(Color.BLACK);

		gc.strokeLine(xs, ys, xe, ye);
		
		gc.strokeText(String.format(Locale.US, "%.0f", extensionRate), (xs + xe) / 2,
				(ys + ye) / 2);
	}

	@Override
	public Rod clone() {
		Rod copy = new Rod(getUndeformedLength(),
				getCurrentLengthDelta(), getMaxLengthDelta(), getStartBody().clone(), getEndBody().clone(), this.extensionRate);
		return copy;
	}
	
	private double extensionRate;
}
