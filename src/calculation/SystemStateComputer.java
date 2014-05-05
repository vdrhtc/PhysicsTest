package calculation;

import java.util.ArrayList;
import java.util.concurrent.CyclicBarrier;
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

	public static volatile double bodyIntegrationGrain = 0.5e-3;
	public static volatile Duration monitorsUpdatePeriod = Duration
			.millis(300);

	public SystemStateComputer(SystemState bodies) {
		SystemStateComputer.bodies = bodies;
	}

	public void launchMonitors() {
		Timeline tI = new Timeline();
		tI.getKeyFrames().add(
				new KeyFrame(monitorsUpdatePeriod,
						new EventHandler<ActionEvent>() {

							@Override
							public void handle(ActionEvent event) {
								refreshCalculationFrequency();
								refreshEnergyErrorPerSecond();
								refreshUpdatedBodiesNumber();
								refreshInnerTime();
							}

						}));

		tI.setCycleCount(Timeline.INDEFINITE);
		tI.play();

	}
	
	private void refreshInnerTime() {
		innerTime += diffUpdatesNum*SystemStateComputer.bodyIntegrationGrain;
	}

	private void refreshUpdatedBodiesNumber() {
		lastNumberOfBodiesUpdated = bodiesReallyUpdated;
	}

	private void refreshEnergyErrorPerSecond() {
		double newEnergy = bodies.calculateEnergy();
		energyDeltaPerSecond = (newEnergy - lastEnergy);
		lastEnergy = newEnergy;
	}

	private void refreshCalculationFrequency() {
		diffUpdatesNum = totalUpdatesNum;
		totalUpdatesNum = 0;
		updtateFrequency = (diffUpdatesNum / monitorsUpdatePeriod
				.toSeconds());
	}

	public void launchBodiesUpdate() {

		int desiredPartsNumber = 8;

		final ArrayList<ArrayList<Body>> parts = bodies.split(desiredPartsNumber);
		System.out.println(parts.size());

		endBarrier = new CyclicBarrier(parts.size(), new Runnable() {

			@Override
			public void run() {
				startBarrier.reset();
				SystemStateConveyor.recieveNextSystemState(bodies);
				totalUpdatesNum++;
				log.info("Step made");
			}

		});

		startBarrier = new CyclicBarrier(parts.size(), new Runnable() {

			@Override
			public void run() {
				endBarrier.reset();
			}

		});

		for (final ArrayList<Body> part : parts) {

			Task<Object> taskGenerate = new Task<Object>() {

				@Override
				protected Object call() throws Exception {
					while (true) {
						startBarrier.await();
						updateBodies(part);
//						log.info("Processing "
//								+ Thread.currentThread().getName());
						endBarrier.await();
//						log.info("Thread " + Thread.currentThread().getName()
//								+ " crossed the barrier");
					}
//						return null;
				}

			};

			Thread tG = new Thread(taskGenerate);
			tG.setDaemon(true);
			tG.start();
		}
	}

	private void updateBodies(ArrayList<Body> bodiesToUpdate) {

		bodiesReallyUpdated = 0;
		for (Body b : bodiesToUpdate) {
			if (b.update()) {
				bodiesReallyUpdated++;
			}
		}
	}

	public static ArrayList<Collisive> findCollisiveNeighbours(
			Collisive collisive) {
		ArrayList<Collisive> a = new ArrayList<>();
		for (Body b : bodies.getBodies()) {
			if (b instanceof Collisive && !b.equals(collisive))
				if (collisive.detectIntersection((Collisive) b)) {
					a.add((Collisive) b);
				}
		}
		return a;
	}

	public static double getRealUpdateFrequency() {
		return updtateFrequency;
	}

	public static double getInnerTime() {
		return innerTime;
	}
	
	public static double getRealUpdatePeriod() {
		return 1 / updtateFrequency;
	}

	public static double getEnergyDeltaPerSecond() {
		return energyDeltaPerSecond;
	}

	public static int getNumberOfBodiesUpdated() {
		return lastNumberOfBodiesUpdated;
	}

	private static SystemState bodies;
	private static Logger log = Logger.getAnonymousLogger();

	private static double updtateFrequency;
	private volatile long totalUpdatesNum;
	private static volatile long diffUpdatesNum;
	private static volatile int bodiesReallyUpdated = 0;
	private static volatile double innerTime=0;

	private double lastEnergy;
	private static volatile double energyDeltaPerSecond;
	private static volatile int lastNumberOfBodiesUpdated;

	private CyclicBarrier endBarrier;
	private CyclicBarrier startBarrier;

	static {
		log.setLevel(Level.OFF);
	}

}
