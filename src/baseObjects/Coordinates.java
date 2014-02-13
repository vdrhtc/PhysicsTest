package baseObjects;

public class Coordinates {
	public Coordinates(double x, double y) {
		this.setX(x);
		this.setY(y);
	}
	
	
	public Coordinates subtract(Coordinates c) {
		double x = this.getX() + c.getX();
		double y = this.getX()  + c.getX();
		return new Coordinates(x, y);
		
	}
	
	public Vector toVector() {
		return new Vector(new Coordinates(0, 0), this);
	}
	
	public double getX() {
		return x;
	}
	public void setX(double x2) {
		this.x = x2;
	}



	public double getY() {
		return y;
	}



	public void setY(double y2) {
		this.y = y2;
	}

	@Override
	public String toString(){
		return "X = "+x+", Y = "+y;
	}

	private double x;
	private double y;
}

