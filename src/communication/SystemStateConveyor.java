package communication;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import view.ViewUpdater;
import calculation.SystemState;
import calculation.SystemStateComputer;

public class SystemStateConveyor {

	public static void recieveNextSystemState(SystemState s) {
		
		cyclesToSkip = (int) ( ViewUpdater.drawPeriod.toSeconds() / SystemStateComputer.bodyIntegrationGrain);
		if (skippedCycles < cyclesToSkip) {
			skippedCycles++;
			return;
		}
		skippedCycles = 0;
//		log.info("Getting next state, storing "+states.size()+" states");
		try {
			states.put(s.clone());
		} catch (InterruptedException e) {
			log.warning("Blocking queue was interrupted on recieve");
			return;
		}
	}

	public static SystemState getNextSystemState() {
		return states.poll();
	}

	private static int cyclesToSkip;
	private static int skippedCycles;
	private static ArrayBlockingQueue<SystemState> states = new ArrayBlockingQueue<>(
			2);
	private static Logger log = Logger.getAnonymousLogger();

	static {
		log.setLevel(Level.OFF);
	}
}
