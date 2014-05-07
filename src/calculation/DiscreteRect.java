package calculation;

import org.javatuples.Pair;

public class DiscreteRect {

	public DiscreteRect(Pair<Integer, Integer> xInterval,
			Pair<Integer, Integer> yInterval) {

		this.xInterval = xInterval;
		this.yInterval = yInterval;
	}

	public Pair<Integer, Integer> getxInteval() {
		return xInterval;
	}

	public Pair<Integer, Integer> getyInterval() {
		return yInterval;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof DiscreteRect))
			return false;
		DiscreteRect other = (DiscreteRect) obj;
		if (xInterval == null) {
			if (other.xInterval != null)
				return false;
		} else if (!xInterval.equals(other.xInterval))
			return false;
		if (yInterval == null) {
			if (other.yInterval != null)
				return false;
		} else if (!yInterval.equals(other.yInterval))
			return false;
		return true;
	}

	public static RadiusVector[] findDifference(DiscreteRect from,
			DiscreteRect what) {
		RadiusVector[] difference = new RadiusVector[1000];
		int n = 0;
		for (int i = from.xInterval.getValue0(); i <= from.xInterval
				.getValue1(); i += SystemStateComputer.getEther().GRID_STEP)
			for (int j = from.yInterval.getValue0(); j <= from.yInterval
					.getValue1(); j += SystemStateComputer.getEther().GRID_STEP) {
				if (i <= what.xInterval.getValue1()
						&& i >= what.xInterval.getValue0()
						&& j <= what.yInterval.getValue1()
						&& j >= what.yInterval.getValue0()) {
					continue;
				}
				difference[n] = new RadiusVector(i, j);
				n++;
			}
		return difference;
	}

	private Pair<Integer, Integer> xInterval;
	private Pair<Integer, Integer> yInterval;
}
