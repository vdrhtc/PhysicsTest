package view;

import java.util.Locale;

import communication.SystemStateConveyor;
import calculation.SystemState;
import calculation.SystemStateComputer;
import baseObjects.Body;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class ViewUpdater {

	public static Duration drawPeriod = Duration.millis(20);
	public static Duration interfaceUpdatePeriod = Duration.millis(500);

	
	public static void setGraphicsContext(GraphicsContext gc) {
		ViewUpdater.gc = gc;

	}

	public static void launchDraw() {

		Timeline tD = new Timeline();
		tD.getKeyFrames().add(
				new KeyFrame(drawPeriod, new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {
						draw();
						elapsedTime += drawPeriod.toSeconds();
					}
				}));

		tD.setCycleCount(Timeline.INDEFINITE);
		tD.play();
	}

	private static void draw() {
		SystemState s = SystemStateConveyor.getNextSystemState();
		if (s != null) {
			gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas()
					.getHeight());
			drawInterface(s);
			drawBodies(s);
		}
	}
	
	public static void forceDraw(SystemState s) {
		drawInterface(s);
		drawBodies(s);
	}

	private static void drawBodies(SystemState s) {

		for (Body b : s.getBodies()) {
			b.draw(gc);
		}
	}

	private static void drawInterface(SystemState s) {

		gc.setStroke(Color.BLACK);

		
		double currTotal = s.calculateEnergy();
		
		gc.strokeText(
				"Updates/s: "
						+ String.format(Locale.US, "%.2e",
								SystemStateComputer.getRealUpdateFrequency())
						+ ", Bodies updated: "
						+ SystemStateComputer.getNumberOfBodiesUpdated()
						+ ", Total energy: "
						+ String.format(Locale.US, "%.2f", currTotal)
						+ ", Energy delta per second: "
						+ String.format(Locale.US, "%.2f", SystemStateComputer.getEnergyDeltaPerSecond())
						+ ", Elapsed real time: "
						+ String.format(Locale.US, "%.2f", elapsedTime)
						+ ", Inner system time: "
						+ String.format(Locale.US, "%.2f", SystemStateComputer.getInnerTime()), 200,
				100);

	}

	public static GraphicsContext gc;
	public static double elapsedTime;
}