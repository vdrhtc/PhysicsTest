package baseObjects;

import calculation.DiscreteRect;
import calculation.RadiusVector;

public interface Collisive {
	public RadiusVector getRadiusVector() ;
	public boolean detectCollision(Collisive c);
	public CollisiveType getCollisiveType();
//	public boolean alreadyCollidedWith(Collisive c);
	public void modifyFieldArea(DiscreteRect r);
	public DiscreteRect getFieldArea();
}