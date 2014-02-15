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
import javafx.util.Duration;

public class ViewUpdater {

	public static Duration drawPeriod = Duration.millis(10);
	public static Duration interfaceUpdatePeriod = Duration.millis(500);

	public ViewUpdater(GraphicsContext gc) {
		this.gc = gc;
	}

	public void launchDraw() {

		Timeline tD = new Timeline();
		tD.getKeyFrames().add(
				new KeyFrame(drawPeriod, new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent event) {
						elapsedTime += drawPeriod.toSeconds();
						draw();
					}
				}));

		tD.setCycleCount(Timeline.INDEFINITE);
		tD.play();
	}

	private void draw() {
		gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas()
				.getHeight());
		SystemState s = SystemStateConveyor.getNextSystemState();
		if (s != null) {
			drawInterface(s);
			drawBodies(s);
		}
	}

	private void drawBodies(SystemState s) {

		for (Body b : s.getBodies()) {
			b.draw(gc);
		}
	}

	private void drawInterface(SystemState s) {

		double currTotal = SystemStateComputer.calculateEnergy(s);
		gc.strokeText(
				"Updates/s: "
						+ String.format(Locale.US, "%.2e",
								SystemStateComputer.getRealUpdateFrequency())
						+ ", Total energy: "
						+ String.format(Locale.US, "%.2f", currTotal)
						+ ", Elapsed time: "
						+ String.format(Locale.US, "%.2f", elapsedTime), 200,
				100);

	}

	public GraphicsContext gc;
	public double elapsedTime;
}