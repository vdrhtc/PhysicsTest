package baseObjects;

import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import calculation.RadiusVector;
import calculation.SystemStateComputer;
import calculation.Vector;

public class Body {

	public Body(double mass, RadiusVector coordinates) {
		this.mass = mass;
		this.coordinates = this.previousCoordinates = coordinates;
	}

	public Body(double mass, RadiusVector coordinates, Vector velocity,
			Vector acceleration, Vector netForce) {
		this.mass = mass;
		this.coordinates = this.previousCoordinates = coordinates;
		this.velocity = velocity;
		this.acceleration = acceleration;
	}

	public void update() {
		Vector newAcceleration = calculateNewAcceleration();
		Vector newVelocity = calculateVelocity(newAcceleration);
		RadiusVector previousCoordinates = this.coordinates;

		this.coordinates = calculateNewRadiusVector(newVelocity);
//		this.coordinates = calculateNewRadiusVectorVerlet();
		this.velocity = newVelocity;
		this.previousCoordinates = previousCoordinates;
		this.acceleration = newAcceleration;
		this.netForce = new Vector(0, 0);

	}

	private RadiusVector calculateNewRadiusVectorVerlet() {
		return this.coordinates
				.mul(2)
				.add(this.previousCoordinates.neg())
				.add(this.acceleration
						.mul(SystemStateComputer.bodyIntegrationGrain
								* SystemStateComputer.bodyIntegrationGrain));
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
		return this.mass * velocityMod * velocityMod / 2;
	}

	private RadiusVector calculateNewRadiusVector(Vector newVelocity) {
		Vector delta = newVelocity.add(velocity).mul(
				SystemStateComputer.bodyIntegrationGrain / 2);
		return coordinates.add(delta);

	}

	public void applyForce(Vector anotherForce) {
		netForce = netForce.add(anotherForce);
	}

	public RadiusVector getRadiusVector() {
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
	private RadiusVector coordinates;
	private RadiusVector previousCoordinates;
	private Vector netForce = new Vector(0, 0);

	private static Logger log = Logger.getAnonymousLogger();
	static {
		log.setLevel(Level.OFF);
	}

}
