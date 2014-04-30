package baseObjects;

import java.util.ArrayList;
import java.util.Locale;
import java.util.logging.Logger;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import calculation.RadiusVector;
import calculation.SystemStateComputer;
import calculation.Vector;

public class Ball extends RoughBody implements Collisive {
	public Ball(double mass, RadiusVector coordinates, double frictionFactor,
			double radius) {
		super(mass, coordinates, frictionFactor);
		this.radius = radius;
	}

	public Ball(double mass, RadiusVector coordinates, double frictionFactor,
			Vector velocity, Vector acceleration, Vector netForce, double radius) {
		super(mass, coordinates, frictionFactor, velocity, acceleration,
				netForce);
		this.radius = radius;
	}

	public void update() {
		ArrayList<Collisive> toCollideWith = SystemStateComputer
				.findCollisiveNeighbours(this);
		for (Collisive c : toCollideWith) {
			this.collideWith(c);
		}
		super.update();
	}

	public void collideWith(Collisive c) {
		if (c.getCollisiveType() == CollisiveType.BALL) {
			Ball b = (Ball) c;
			Vector force = calculateForce(b);
			applyForce(force.neg());
			b.applyForce(force);
			// log.info("appliedForce" + force.toString());
		}
		return;
	}

	private Vector calculateForce(Ball b) {
		Vector distanceVec = new Vector(this.getRadiusVector(),
				b.getRadiusVector());
		double distance = distanceVec.mod();

		Vector force = distanceVec.div(distance)
				.div(Math.pow( Math.abs(distance - b.getRadius()) / this.radius,
						4) + 1e-4);

		return force;
	}

	public double getRadius() {
		return radius;
	}

	@Override
	public boolean detectIntersection(Collisive c) {
		if (c.getCollisiveType() == CollisiveType.BALL) {
			Ball b = (Ball) c;
			if (Math.abs(b.getRadiusVector().getX()
					- this.getRadiusVector().getX()) < this.radius
					+ b.getRadius()
					&& Math.abs(b.getRadiusVector().getY()
							- this.getRadiusVector().getY()) < this.radius
							+ b.getRadius()) {
				return true;
			}
		}
		return false;
	}

	public void draw(GraphicsContext gc) {
		gc.setStroke(Color.BLACK);

		gc.fillOval(getRadiusVector().getX() - radius, getRadiusVector().getY()
				- radius, 2 * radius, 2 * radius);
//		gc.strokeText(String.format(Locale.US, "%.3f", getVelocity().mod())
//				+ "px/s", getRadiusVector().getX() - radius, getRadiusVector()
//				.getY() - radius);
	}

	@Override
	public CollisiveType getCollisiveType() {
		return CollisiveType.BALL;
	}

	@Override
	public Ball clone() {
		return new Ball(getMass(), getRadiusVector(), getFrictionFactor(),
				getVelocity(), getAcceleration(), getNetForce(), this.radius);
	}

	private double radius;

	private static Logger log = Logger.getAnonymousLogger();

}
