package Controller;

import java.util.ArrayList;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.util.Duration;
import baseObjects.Body;

public class Updater {
	
	public static final Duration updatePeriod = Duration.millis(1);
	public static final Duration drawPeriod = Duration.millis(1);
	
	public Updater(ArrayList<Body> bodies) {
		this.bodies = bodies;
	}
	
	public void launch(GraphicsContext ctxt) {
		
		Task<Object> taskGenerate = new Task<Object>() {

			@Override
			protected Object call() throws Exception {
				while(true){
				Thread.sleep((long) updatePeriod.toMillis());
				updateBodies();
				}
			}

		};

		Thread tG = new Thread(taskGenerate);
		tG.setDaemon(true);
		tG.start();
		
		gc = ctxt;
//		Timeline tU = new Timeline();
//		tU.getKeyFrames().add(
//				new KeyFrame(updatePeriod,
//						new EventHandler<ActionEvent>() {
//
//							@Override
//							public void handle(ActionEvent event) {
//								
//							}
//						}));
//
//		tU.setCycleCount(Timeline.INDEFINITE);
//		tU.play();
		
		Timeline tD = new Timeline();
		tD.getKeyFrames().add(
				new KeyFrame(drawPeriod,
						new EventHandler<ActionEvent>() {

							@Override
							public void handle(ActionEvent event) {
								drawBodies(gc);
							}

							
						}));

		tD.setCycleCount(Timeline.INDEFINITE);
		tD.play();
	}
	
	
	private void updateBodies() {
		for (Body b : bodies) {
			b.update();
		}
	}
	
	private void drawBodies(GraphicsContext ctxt) {
		ctxt.clearRect(0, 0, ctxt.getCanvas().getWidth(), ctxt.getCanvas().getHeight());
		for (Body b : bodies) {
			b.draw(ctxt);
		}
	}
	private ArrayList<Body> bodies;
	private GraphicsContext gc;
	
}
