package calculation;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;
import baseObjects.Body;
import baseObjects.Collisive;
import communication.SystemStateConveyor;

public class SystemStateComputer {

	public static volatile double bodyIntegrationGrain = 5e-4;
	public static volatile Duration computationFrequencyUpdatePeriod  = Duration.millis(500);
	
	public SystemStateComputer(SystemState bodies) {
		SystemStateComputer.bodies = bodies;
	}
	
	public void launchMonitors() {
		Timeline tI = new Timeline();
		tI.getKeyFrames().add(
				new KeyFrame(computationFrequencyUpdatePeriod,
						new EventHandler<ActionEvent>() {

							@Override
							public void handle(ActionEvent event) {
								refreshCalculationFrequency();
								refreshEnergyErrorPerSecond();
							}

							
						}));

		tI.setCycleCount(Timeline.INDEFINITE);
		tI.play();
		
	}
	
	private void refreshEnergyErrorPerSecond() {
		double newEnergy = bodies.calculateEnergy();
		energyDeltaPerSecond = (newEnergy-lastEnergy);
		lastEnergy = newEnergy;
	}
	
	private void refreshCalculationFrequency() {
		diffUpdatesNum = totalUpdatesNum;
		totalUpdatesNum = 0;
		updtateFrequency = (diffUpdatesNum/computationFrequencyUpdatePeriod.toSeconds());
	}


	public void launchBodiesUpdate() {
		Task<Object> taskGenerate = new Task<Object>() {

			@Override
			protected Object call() throws Exception {
				while (true) {
					updateBodies();
					totalUpdatesNum++;
					SystemStateConveyor.recieveNextSystemState(bodies);
				}
			}

		};

		Thread tG = new Thread(taskGenerate);
		tG.setDaemon(true);
		tG.start();
	}
	

	private void updateBodies() {

		for (Body b : bodies.getBodies()) {
			b.update();
		}
	}
	
	public static ArrayList<Collisive> findCollisiveNeighbours(Collisive collisive) {
		ArrayList<Collisive> a = new ArrayList<>();
		for (Body b : bodies.getBodies()) {
			if(b instanceof Collisive && !b.equals(collisive))
				if(collisive.detectIntersection((Collisive) b)) {
					a.add((Collisive) b);
				}
		}
		return a;
	}
	
	public static double getRealUpdateFrequency() {
		return updtateFrequency;
	}
		
	public static double getRealUpdatePeriod() {
		return 1/updtateFrequency;
	}

	public static double getEnergyDeltaPerSecond() {
		return energyDeltaPerSecond;
	}
	
	private static SystemState bodies;
	private static Logger log = Logger.getAnonymousLogger();

	private static double updtateFrequency;
	private volatile long totalUpdatesNum;
	private static volatile long diffUpdatesNum;
	
	private double lastEnergy;
	private static volatile double energyDeltaPerSecond;
	
	
	static {
		log.setLevel(Level.ALL);
	}

}
