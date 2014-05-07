package baseObjects;

import java.util.ArrayList;
import java.util.logging.Logger;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import calculation.DiscreteRect;
import calculation.Ether;
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

	@Override
	public void updateEther() {
		SystemStateComputer.getEther().modifyInteractionFieldFor(this);

	}

	@Override
	public void updateForces() {

		ArrayList<Collisive> toCollideWith = SystemStateComputer.getEther()
				.getBosonOwnersNear(this);

		for (Collisive c : toCollideWith) {
			if (!c.equals(this))
				this.collideWith(c);
		}
		super.updateForces();
	}

	@Override
	public boolean updatePosition() {
		if (!active)
			return false;
		super.updatePosition();
		return true;
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
				.div(Math.pow(Math.abs(distance - b.getRadius()) / this.radius,
						5) + .5e-4);

		return force;
	}

	public double getRadius() {
		return radius;
	}

	@Override
	public boolean detectCollision(Collisive c) {
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

		gc.strokeOval(getRadiusVector().getX() - radius, getRadiusVector()
				.getY() - radius, 2 * radius, 2 * radius);
		// gc.strokeText(String.format(Locale.US, "%.3f", getVelocity().mod())
		// + "px/s", getRadiusVector().getX() - radius, getRadiusVector()
		// .getY() - radius);
		gc.fillOval(getRadiusVector().getX() - 0.5,
				getRadiusVector().getY() - 0.5, 1, 1);
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

	public DiscreteRect getFieldArea() {
		return fieldArea;
	}

	public void modifyFieldArea(DiscreteRect newFieldArea) {
	
		
		if (this.fieldArea != null) {
			Ether e = SystemStateComputer.getEther();
			DiscreteRect r = this.fieldArea;

			for (int x = r.getxInteval().getValue0(); x <= r.getxInteval()
					.getValue1(); x += e.GRID_STEP) {
				for (int y = r.getyInterval().getValue0(); y <= r
						.getyInterval().getValue1(); y += e.GRID_STEP) {
					e.removeCollisiveBosonFromPoint(new RadiusVector(x, y),
							this);

				}
			}
			
		}
		this.fieldArea = newFieldArea;
	}

	private double radius;
	private DiscreteRect fieldArea;

	private static Logger log = Logger.getAnonymousLogger();

}
