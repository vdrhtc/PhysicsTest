package baseObjects;

import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import calculation.Coordinates;
import calculation.SystemStateComputer;
import calculation.Vector;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Body {

	public Body(double mass, Coordinates coordinates) {
		this.mass = mass;
		this.coordinates = coordinates;
	}

	public Body(double mass, Coordinates coordinates, Vector velocity,
			Vector acceleration, Vector netForce) {
		this.mass = mass;
		this.coordinates = coordinates;
		this.velocity = velocity;
		this.acceleration = acceleration;
	}

	public void update() {
		Vector newAcceleration = calculateNewAcceleration();
		Vector newVelocity = calculateVelocity(newAcceleration);
		this.coordinates = calculateNewCoordinates(newVelocity).toCoordinates();
		this.velocity = newVelocity;
		this.acceleration = newAcceleration;
		this.netForce = new Vector(0, 0);

	}

	private Vector calculateNewAcceleration() {
		return netForce.div(mass);
	}

	private Vector calculateVelocity(Vector newAcceleration) {

		Vector delta = newAcceleration.add(acceleration).mul(
				SystemStateComputer.bodyIntegrationGrain / 2);
		Vector newVelocity = velocity.add(delta);
		return newVelocity;
	}

	public double getEnergy() {
		double velocityMod = this.velocity.mod();
		return this.mass*velocityMod*velocityMod/2;
	}
	
	private Vector calculateNewCoordinates(Vector newVelocity) {
		Vector delta = newVelocity.add(velocity).mul(
				SystemStateComputer.bodyIntegrationGrain / 2);
		return coordinates.toVector().add(delta);

	}

	public void applyForce(Vector anotherForce) {
		netForce = netForce.add(anotherForce);
	}

	public Coordinates getCoordinates() {
		return coordinates;
	}

	public Vector getVelocity() {
		return velocity;
	}

	public void draw(GraphicsContext gc) {
		gc.setStroke(Color.BLACK);

		gc.fillOval(coordinates.getX() - 5, coordinates.getY() - 5, 10, 10);
		gc.strokeText(
				String.format(Locale.US, "%.3f", velocity.mod()) + "px/s",
				coordinates.getX() - 5, coordinates.getY() - 5);
	}

	public Body clone() {
		return new Body(mass, coordinates, velocity, acceleration, netForce);

	}
	

	public Vector getAcceleration() {
		return acceleration;
	}
	
	public Vector getNetForce() {
		return netForce;
	}
	public double getMass() {
		return mass;
	}

	private double mass;
	private Vector velocity = new Vector(0, 0);
	private Vector acceleration = new Vector(0, 0);
	private Coordinates coordinates;
	private Vector netForce = new Vector(0, 0);

	private static Logger log = Logger.getAnonymousLogger();
	static {
		log.setLevel(Level.OFF);
	}

}
