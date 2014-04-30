package calculation;

public class RadiusVector extends Vector {

	public RadiusVector(double x, double y) {
		super(x, y);
	}
	
	public RadiusVector add(Vector v) {
		double x = this.getX() + v.getX();
		double y = this.getY() + v.getY();
		return new RadiusVector(x, y);
	
	}
}
