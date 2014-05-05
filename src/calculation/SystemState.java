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
	public ArrayList<ArrayList<Body>> split(int partsNumber) {
		
		int partLen = bodies.size()/partsNumber;
		ArrayList<ArrayList<Body>> parts = new ArrayList<>();
		ArrayList<Body> part = new ArrayList<>();
		for (int i=0; i<bodies.size(); i++) {
			if(i%partLen == 0 && i!=0) {
				parts.add(part);
				part = new ArrayList<>();
			}
			part.add(bodies.get(i));
		}

		parts.add(part);
		return parts;
	}
	
	private ArrayList<Body> bodies;

	
}
