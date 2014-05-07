package calculation;


public class Vector {
	
	public static final Vector NULLVECTOR = new Vector(0,0);

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
	
	public boolean approximatelyEquals(Vector vector) {
		if (Math.abs(vector.x-this.x)<0.1 && Math.abs(vector.y-this.y)<0.1) 
			return true;
		return false;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Vector))
			return false;
		Vector other = (Vector) obj;
		if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
			return false;
		if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
			return false;
		return true;
	}
	
	private double x;
	private double y;
	
}
