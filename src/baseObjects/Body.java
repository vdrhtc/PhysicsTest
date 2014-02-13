package baseObjects;

import java.util.logging.Logger;

import Controller.Updater;

public class Body {
	public Body(double mass, Coordinates coordinates) {
		this.mass = mass;
		this.coordinates = coordinates;
	}
	public void update() {
		Vector newAcceleration = calculateNewAcceleration();
		Vector newVelocity = calculateVelocity(newAcceleration);
		this.coordinates = calculateNewCoordinates(newVelocity).toCoordinates();
		this.velocity = newVelocity;
		this.acceleration = newAcceleration;
		this.netForce = new Vector(0,0);
		log.info(this.coordinates.toString());
		
	}
	
	private Vector calculateNewAcceleration() {
		return netForce.divide(mass);
	}
	
	private Vector calculateVelocity(Vector newAcceleration) {
		Vector delta = newAcceleration.add(acceleration).multiply(Updater.updatePeriod.toSeconds()/2);
		return velocity.add(delta);
	}
	
	private Vector calculateNewCoordinates(Vector newVelocity) {
		Vector delta = newVelocity.add(velocity).multiply(Updater.updatePeriod.toSeconds()/2);
		return coordinates.toVector().add(delta);

	}
	
	public void applyForce(Vector anotherForce) {
		netForce = netForce.add(anotherForce);
	}
	

	public Coordinates getCoordinates() {
		return coordinates;
	}


	private double mass;
	private Vector velocity = new Vector(0, 0);
	private Vector acceleration = new Vector(0, 0 );
	private Coordinates coordinates;
	private Vector netForce = new Vector(0, 0);
	
	Logger log = Logger.getAnonymousLogger();
}
