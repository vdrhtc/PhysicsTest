package baseObjects;

public interface Collisive {
	public boolean detectIntersection(Collisive c);
	public CollisiveType getCollisiveType();
//	public boolean alreadyCollidedWith(Collisive c);
}