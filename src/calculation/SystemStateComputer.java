package calculation;

import java.util.ArrayList;
import java.util.concurrent.BrokenBarrierException;
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

	public static volatile double bodyIntegrationGrain = 5e-4;
	public static volatile Duration monitorsUpdatePeriod = Duration
			.millis(300);

	public SystemStateComputer(SystemState bodies) {
		SystemStateComputer.bodies = bodies;
		ether = new Ether();
	}
	
	public static SystemState getSystemState() {
		return bodies;
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

		int desiredPartsNumber = 1;

		final ArrayList<ArrayList<Body>> parts = bodies.split(desiredPartsNumber);
		System.out.println(parts.size());

		positionsUpdateBarrier = new CyclicBarrier(parts.size(), new Runnable() {

			@Override
			public void run() {
				startBarrier.reset();
				SystemStateConveyor.recieveNextSystemState(bodies);
//				ether.clearGrid();
				totalUpdatesNum++;
			}

		});

		forcesUpdateBarrier = new CyclicBarrier(parts.size());

		etherUpdateBarrier = new CyclicBarrier(parts.size());
		
		startBarrier = new CyclicBarrier(parts.size(), new Runnable() {

			@Override
			public void run() {
				forcesUpdateBarrier.reset();
				positionsUpdateBarrier.reset();
			}

		});

		for (final ArrayList<Body> part : parts) {

			Task<Object> taskGenerate = new Task<Object>() {

				@Override
				protected Object call() throws InterruptedException, BrokenBarrierException {
					while (true) {
						try {
						startBarrier.await();
						
						updateEther(part);
						
						etherUpdateBarrier.await();
						
						updateForces(part);
						
						forcesUpdateBarrier.await();
						
						updatePositions(part);
						
						positionsUpdateBarrier.await();
						} catch(NullPointerException e) {
							e.printStackTrace();
						}
					}
				}
			};

			Thread tG = new Thread(taskGenerate);
			tG.setDaemon(true);
			tG.start();
		}
	}
	
	private void updateEther(ArrayList<Body> bodiesToUpdate) {
		for (Body b : bodiesToUpdate) {
			b.updateEther();
		}
	}

	private void updateForces(ArrayList<Body> bodiesToUpdate) {
		for (Body b : bodiesToUpdate) {
			b.updateForces();
		}
	}

	private void updatePositions(ArrayList<Body> bodiesToUpdate) {

		bodiesReallyUpdated = 0;
		for (Body b : bodiesToUpdate) {
			if (b.updatePosition()) {
				bodiesReallyUpdated++;
			}
		}
	}
	
	public static ArrayList<Collisive> findCollisiveNeighbours(
			Collisive collisive) {
		ArrayList<Collisive> a = new ArrayList<>();
		for (Body b : bodies.getBodies()) {
			if (b instanceof Collisive && !b.equals(collisive))
				if (collisive.detectCollision((Collisive) b)) {
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
	
	public static int getNumberOfBodies() {
		return bodies.getBodies().size();
	}
	
	public static Ether getEther() {
		return ether;
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
	private static volatile Ether ether;

	private CyclicBarrier forcesUpdateBarrier;
	private CyclicBarrier startBarrier;
	private CyclicBarrier positionsUpdateBarrier;
	private CyclicBarrier etherUpdateBarrier;

	static {
		log.setLevel(Level.OFF);
	}

}
