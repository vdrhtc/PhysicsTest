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
	
	public SystemState clone() {
		ArrayList<Body> copyBodies = new ArrayList<>();
		for (Body b : bodies) {
			copyBodies.add(b.clone());
		}
		return new SystemState(copyBodies);
	}
	
	private ArrayList<Body> bodies;
	
}
