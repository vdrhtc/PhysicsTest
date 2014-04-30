package calculation;


public class Vector {

	public Vector(RadiusVector start, RadiusVector end) {
		this.x = end.getX() - start.getX();
		this.y = end.getY() - start.getY();
	}
	
	public Vector(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector() {}
	
	public Vector add(Vector v) {
		double x = this.getX() + v.getX();
		double y = this.getY() + v.getY();
		return new Vector(x, y);
	
	}
	
	public static double dotProduct(Vector a, Vector b) {
		return a.getX()*b.getX()+a.getX()*b.getX();
	}
	
	public Vector div(double factor) {
		double x = this.x/factor;
		double y = this.y/factor;
		return new Vector(x, y);
	}
	
	public Vector mul(double factor) {
		double x = this.x*factor;
		double y = this.y*factor;
		return new Vector(x, y);
	}
	
	public static double calculateDistance(RadiusVector start, RadiusVector end) {
		return Math.sqrt((start.getX() - end.getX())
				* (start.getX() - end.getX())
						+ (start.getY() - end.getY())
								* (start.getY() - end.getY()));
	}
	
	public Double mod() {
		return Math.sqrt(x*x+y*y);
	}
	public Vector neg() {
		return new Vector(-this.x, -this.y);
	}
	
	public Vector norm() {
		double mod = mod();
		if(mod!=0)
			return new Vector(x, y).div(mod());
		else
			return new Vector(0, 0);
	}
	
	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public String toString() {
		return "("+this.x+", "+this.y+")";
	}
	
	private double x;
	private double y;
	
}
