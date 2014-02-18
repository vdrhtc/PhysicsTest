package calculation;

import java.util.ArrayList;

import baseObjects.Body;

public class SystemState {

	public SystemState(ArrayList<Body> bs) {
		this.bodies = bs;
	}
	
	public ArrayList<Body> getBodies() {
		return bodies;
	}
	

	public double calculateEnergy() {
		double currTotal=0; 
		for(Body b : getBodies()) {
			currTotal+=b.getEnergy();
		}
		
		return currTotal;
	}
	
	public SystemState clone() {
		ArrayList<Body> copyBodies = new ArrayList<>();
		for (Body b : bodies) {
			copyBodies.add(b.clone());
		}
		return new SystemState(copyBodies);
	}
	
	private ArrayList<Body> bodies;
	
}
