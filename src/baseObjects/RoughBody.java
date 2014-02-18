package baseObjects;

import calculation.Coordinates;
import calculation.Vector;

public class RoughBody extends Body {

	public RoughBody(double mass, Coordinates coordinates, double frictionFactor) {
		super(mass, coordinates);
		this.frictionFactor = frictionFactor;
	}

	public RoughBody(double mass, Coordinates coordinates,
			double frictionFactor, Vector velocity, Vector acceleration,
			Vector netForce) {
		super(mass, coordinates, velocity, acceleration, netForce);
		this.frictionFactor = frictionFactor;
	}

	@Override
	public void update() {
		Vector friction = getVelocity().norm().mul(-frictionFactor);
		this.applyForce(friction);
		super.update();
	}

	@Override
	public RoughBody clone() {
		return new RoughBody(getMass(), getCoordinates(), frictionFactor,
				getVelocity(), getAcceleration(), getNetForce());
	}

	private double frictionFactor;
}
