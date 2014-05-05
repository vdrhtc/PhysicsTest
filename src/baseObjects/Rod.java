package baseObjects;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import calculation.SystemStateComputer;
import calculation.Vector;

public class Rod extends Spring {


	public Rod(Body startBody, Body endBody) {
		super(-1, startBody, endBody);
	}
	
	public Rod(double length, Body startBody, Body endBody) {
		super(-1, length, startBody, endBody);
	}
	
	public Rod(double length,
			double currentLengthDelta, double maxLengthDelta, Body startBody, Body endBody) {
		
		super(-1, length, currentLengthDelta, maxLengthDelta, startBody, endBody);
	}
	
	public boolean update() {
		updateFields();
		if(Math.abs(getCurrentLengthDelta())<0.01)
			return false;
		broadcastForce();
		return true;
	}
	
	
	@Override
	protected void broadcastForce() {
		Vector directive = getDirectiveVector();
		double delta = 1./2.*getCurrentLengthDelta();
		
		getStartBody().move(directive.mul(delta));
		getStartBody().accelerate(directive.mul(delta/SystemStateComputer.bodyIntegrationGrain));
//		getStartBody().active=true;
		getEndBody().move(directive.mul(-delta));
		getEndBody().accelerate(directive.mul(-delta/SystemStateComputer.bodyIntegrationGrain));
//		getEndBody().active=true;
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
		if (currentLengthDelta < 0)
			gc.setStroke(Color.RED);
		if(Math.abs(currentLengthDelta)<0.01)
			gc.setStroke(Color.BLACK);

		gc.strokeLine(xs, ys, xe, ye);
		
//		gc.strokeText(String.format(Locale.US, "%.0f", currentLengthDelta), (xs + xe) / 2,
//				(ys + ye) / 2);
	}

	@Override
	public Rod clone() {
		Rod copy = new Rod(getUndeformedLength(),
				getCurrentLengthDelta(), getMaxLengthDelta(), getStartBody().clone(), getEndBody().clone());
		return copy;
	}
	
}
