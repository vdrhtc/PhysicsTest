package baseObjects;

import calculation.RadiusVector;
import calculation.Vector;

public class RoughBody extends Body {

	public RoughBody(double mass, RadiusVector coordinates, double frictionFactor) {
		super(mass, coordinates);
		this.frictionFactor = frictionFactor;
	}

	public RoughBody(double mass, RadiusVector coordinates,
			double frictionFactor, Vector velocity, Vector acceleration,
			Vector netForce) {
		super(mass, coordinates, velocity, acceleration, netForce);
		this.frictionFactor = frictionFactor;
	}

	@Override
	public boolean update() {
		if(!active)
			return false;
		
		applyNonZeroFriction();
		
		super.update();
		return true;
	}

	private void applyNonZeroFriction() {
		if(getVelocity().approximatelyEquals(Vector.NULLVECTOR))
			return;
		Vector friction = getVelocity().norm().mul(-frictionFactor);
		this.applyForce(friction);
	}
	
	public double getFrictionFactor() {
		return frictionFactor;
	}

	@Override
	public RoughBody clone() {
		return new RoughBody(getMass(), getRadiusVector(), frictionFactor,
				getVelocity(), getAcceleration(), getNetForce());
	}

	private double frictionFactor;
}
