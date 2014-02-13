package Controller;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.stage.Stage;
import baseObjects.Body;
import baseObjects.Coordinates;
import baseObjects.TwoBodyConstraint;


public class Launcher extends Application {

	public static void main(String[] args) {
		Application.launch(args);
		
	}
	
	public void start(Stage primaryStage) throws Exception {

		ArrayList<Body> bodies = new ArrayList<>();
		TwoBodyConstraint spring = new TwoBodyConstraint(0, new Coordinates(0, 0), 1, 10);
		Body matDot_start = new Body(10, new Coordinates(-1, 0));
		Body matDot_end = new Body(10, new Coordinates(1, 0));
		spring.setStartBody(matDot_start);
		spring.setEndBody(matDot_end);
		bodies.add(spring);
		bodies.add(matDot_end);
		bodies.add(matDot_start);
		
		Updater u = new Updater(bodies);
		u.launch();	
	}

}
