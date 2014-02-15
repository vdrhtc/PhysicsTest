package calculation;


public class Coordinates {
	public Coordinates(double x, double y) {
		this.x = x;
		this.y =y;
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



	public double getY() {
		return y;
	}



	@Override
	public String toString(){
		return "X = "+x+", Y = "+y;
	}
	

	private double x;
	private double y;
}

