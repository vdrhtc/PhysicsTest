package launch;

import java.util.ArrayList;

import view.ViewUpdater;
import calculation.Coordinates;
import calculation.SystemState;
import calculation.SystemStateComputer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SceneBuilder;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import baseObjects.Body;
import baseObjects.RoughBody;
import baseObjects.TwoBodyConstraint;


public class Launcher extends Application {

	public static void main(String[] args) {
		Application.launch(args);
		
	}
	
	public void start(Stage primaryStage) throws Exception {

		ArrayList<Body> bodies = createBodies();
		
		
		
		canvas = new Canvas(1300, 700);
		Group root = new Group();
		Button launch = new Button("Launch");
		launch.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		        SSC.launchBodiesUpdate();
		    }
		});

		root.getChildren().addAll(getCanvas(), launch);
		
		Scene scene = SceneBuilder
				.create()
				.root(root)
				.build();
		primaryStage.setScene(scene);
		primaryStage.show();
		
		SSC = new SystemStateComputer(new SystemState(bodies));
		VU = new ViewUpdater(canvas.getGraphicsContext2D());
		
		SSC.launch();
		VU.launchDraw();
		
	}

	private ArrayList<Body> createBodies() {
		ArrayList<Body> bodies = new ArrayList<>();
		TwoBodyConstraint spring1 = new TwoBodyConstraint(0, new Coordinates(0, 0), 10000, 110);
		TwoBodyConstraint spring2 = new TwoBodyConstraint(0, new Coordinates(0, 0), 10, 50);
		TwoBodyConstraint spring3 = new TwoBodyConstraint(0, new Coordinates(0, 0), 50, 50);
		Body matDot_start = new RoughBody(1, new Coordinates(500, 350), 0.1);
		Body matDot_end = new RoughBody(1, new Coordinates(610, 350), 0.1);
		Body matDot_anotherEnd = new RoughBody(1, new Coordinates(210, 200), 1000);
		spring1.setStartBody(matDot_start);
		spring1.setEndBody(matDot_end);
		spring2.setStartBody(matDot_end);
		spring2.setEndBody(matDot_anotherEnd);
		spring3.setStartBody(matDot_anotherEnd);
		spring3.setEndBody(matDot_start);
		bodies.add(spring1);
		bodies.add(spring2);
		bodies.add(matDot_end);
		bodies.add(matDot_start);
		bodies.add(matDot_anotherEnd);
		
		return bodies;
	}
	
	public Canvas getCanvas() {
		return canvas;
	}


	private Canvas canvas;
	private SystemStateComputer SSC;
	private ViewUpdater VU;

}
