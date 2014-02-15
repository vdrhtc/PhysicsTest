package calculation;

import java.util.logging.Level;
import java.util.logging.Logger;

import communication.SystemStateConveyor;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;
import baseObjects.Body;

public class SystemStateComputer {

	public static volatile double bodyIntegrationGrain = 5e-7;
	public static volatile Duration computationFrequencyUpdatePreiod  = Duration.millis(500);
	
	public SystemStateComputer(SystemState bodies) {
		this.bodies = bodies;
	}

	public void launch() {
		
		launchComputationFrequencyMonitor();
	}

	public void launchComputationFrequencyMonitor() {
		Timeline tI = new Timeline();
		tI.getKeyFrames().add(
				new KeyFrame(computationFrequencyUpdatePreiod,
						new EventHandler<ActionEvent>() {

							@Override
							public void handle(ActionEvent event) {
								diffUpdatesNum = totalUpdatesNum;
								totalUpdatesNum = 0;
								updtateFrequency = (diffUpdatesNum/computationFrequencyUpdatePreiod.toSeconds());
							}
						}));

		tI.setCycleCount(Timeline.INDEFINITE);
		tI.play();
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
	
	public static double calculateEnergy(SystemState s) {
		double currTotal=0; 
		for(Body b : s.getBodies()) {
			currTotal+=b.getEnergy();
		}
		return currTotal;
	}

	private void updateBodies() {

		for (Body b : bodies.getBodies()) {
			b.update();
		}

	}
	
	public static double getRealUpdateFrequency() {
		return updtateFrequency;
	}
		
	public static double getRealUpdatePeriod() {
		return 1/updtateFrequency;
	}

	private SystemState bodies;
	private static Logger log = Logger.getAnonymousLogger();

	private static double updtateFrequency;
	private volatile long totalUpdatesNum;
	private static volatile long diffUpdatesNum;
	
	
	static {
		log.setLevel(Level.ALL);
	}

}
