package baseObjects;

public class Vector {

	public Vector(Coordinates start, Coordinates end) {
		this.x = end.getX() - start.getX();
		this.y = end.getY() - start.getY();
	}
	
	public Vector(double x, double y) {
		this.setX(x);
		this.setY(y);
	}
	
	public Vector() {}
	
	public Vector add(Vector v) {
		double x = this.getX() + v.getX();
		double y = this.getY() + v.getY();
		return new Vector(x, y);
	
	}
	
	public Coordinates toCoordinates() {
		return new Coordinates(x, y);
	}
	
	public Vector divide(double factor) {
		return this.multiply(1./factor);
	}
	
	public Vector multiply(double factor) {
		double x = this.x*factor;
		double y = this.y*factor;
		return new Vector(x, y);
	}
	
	public double mod() {
		return Math.sqrt(x*x+y*y);
	}
	public Vector neg() {
		return new Vector(-this.x, -this.y);
	}
	
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public String toString() {
		return this.toCoordinates().toString();
	}
	
	private double x;
	private double y;
	
}
