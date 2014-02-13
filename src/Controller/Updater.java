package Controller;

import java.util.ArrayList;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;
import baseObjects.Body;

public class Updater {
	
	public static final Duration updatePeriod = Duration.millis(100);
	
	public Updater(ArrayList<Body> bodies) {
		this.bodies = bodies;
	}
	
	public void launch() {
		Timeline tR = new Timeline();
		tR.getKeyFrames().add(
				new KeyFrame(Duration.millis(100),
						new EventHandler<ActionEvent>() {

							@Override
							public void handle(ActionEvent event) {
								updateBodies();
							}

							
						}));

		tR.setCycleCount(Timeline.INDEFINITE);
		tR.play();
	}
	
	
	private void updateBodies() {
		for (Body b : bodies) {
			b.update();
		}
	}
	private ArrayList<Body> bodies;
	
}
