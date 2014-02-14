package Controller;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SceneBuilder;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import baseObjects.Body;
import baseObjects.Coordinates;
import baseObjects.TwoBodyConstraint;


public class Launcher extends Application {

	public static void main(String[] args) {
		Application.launch(args);
		
	}
	
	public void start(Stage primaryStage) throws Exception {

		ArrayList<Body> bodies = createBodies();
		
		Updater u = new Updater(bodies);
		
		
		
		canvas = new Canvas(1300, 700);
		Group root = new Group();
		root.getChildren().add(getCanvas());
		Scene scene = SceneBuilder
				.create()
				.root(root)
				.build();
		primaryStage.setScene(scene);
		primaryStage.show();
		
		u.launch(canvas.getGraphicsContext2D());
	}

	private ArrayList<Body> createBodies() {
		ArrayList<Body> bodies = new ArrayList<>();
		TwoBodyConstraint spring1 = new TwoBodyConstraint(0, new Coordinates(0, 0), 10000, 110);
		TwoBodyConstraint spring2 = new TwoBodyConstraint(0, new Coordinates(0, 0), 10, 150);
		Body matDot_start = new Body(10, new Coordinates(500, 350));
		Body matDot_end = new Body(10, new Coordinates(611, 350));
		Body matDot_anotherEnd = new Body(1, new Coordinates(210, 110));
		spring1.setStartBody(matDot_start);
		spring1.setEndBody(matDot_end);
		spring2.setStartBody(matDot_end);
		spring2.setEndBody(matDot_anotherEnd);
		bodies.add(spring1);
//		bodies.add(spring2);
		bodies.add(matDot_end);
		bodies.add(matDot_start);
		bodies.add(matDot_anotherEnd);
		
		return bodies;
	}
	
	public Canvas getCanvas() {
		return canvas;
	}


	private Canvas canvas;

}
