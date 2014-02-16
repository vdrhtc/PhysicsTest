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
		
		SystemState s = new SystemState(bodies);
		SSC = new SystemStateComputer(s);
		ViewUpdater.setGraphicsContext(canvas.getGraphicsContext2D());
		
		SSC.launch();
		
		ViewUpdater.forceDraw(s);
		ViewUpdater.launchDraw();
		
	}

	private ArrayList<Body> createBodies() {
		ArrayList<Body> bodies = new ArrayList<>();
/*Чек*/	TwoBodyConstraint spring1 = new TwoBodyConstraint(0, new Coordinates(0, 0), 10, 210);
		TwoBodyConstraint spring2 = new TwoBodyConstraint(0, new Coordinates(0, 0), 10000, 90);
		TwoBodyConstraint spring3 = new TwoBodyConstraint(0, new Coordinates(0, 0), 50, 110);
		TwoBodyConstraint spring4 = new TwoBodyConstraint(0, new Coordinates(0, 0), 50, 100);
		TwoBodyConstraint spring5 = new TwoBodyConstraint(0, new Coordinates(0, 0), 50, 100);
		Body matDot_start = new RoughBody(100, new Coordinates(400, 350), 1);
		Body matDot_end = new RoughBody(1, new Coordinates(610, 300), 100);
		Body matDot_anotherEnd = new RoughBody(1, new Coordinates(700, 300), 100);
		Body matDot_yetAnotherEnd = new RoughBody(100, new Coordinates(800, 450), 1);
		Body matDot_yetAnotherEnd2 = new RoughBody(100, new Coordinates(900, 450), 1);
		Body matDot_yetAnotherEnd3 = new RoughBody(1, new Coordinates(950, 250), 1);
		spring1.setStartBody(matDot_start);
		spring1.setEndBody(matDot_end);
		spring2.setStartBody(matDot_end);
		spring2.setEndBody(matDot_anotherEnd);
		spring3.setStartBody(matDot_anotherEnd);
		spring3.setEndBody(matDot_yetAnotherEnd);
		spring4.setStartBody(matDot_anotherEnd);
		spring4.setEndBody(matDot_yetAnotherEnd2);
		spring5.setStartBody(matDot_anotherEnd);
		spring5.setEndBody(matDot_yetAnotherEnd3);
		
		Body matDot_tStart = new RoughBody(0.1, new Coordinates(900, 400), 1);
		Body matDot_tUp = new RoughBody(0.1, new Coordinates(950, 350), 1);
		Body matDot_tEnd = new RoughBody(0.1, new Coordinates(1000, 400), 1);

		TwoBodyConstraint springT1  = new TwoBodyConstraint(0, new Coordinates(0, 0), 10000, 70.7107);
		TwoBodyConstraint springT2 = new TwoBodyConstraint(0, new Coordinates(0, 0), 10000, 70.7107);
		TwoBodyConstraint springT3 = new TwoBodyConstraint(0, new Coordinates(0, 0), 10000, 100);
		TwoBodyConstraint connector = new TwoBodyConstraint(0, new Coordinates(0, 0), 10, 100);

		
		springT1.setStartBody(matDot_tStart);
		springT1.setEndBody(matDot_tUp);		
		springT2.setStartBody(matDot_tUp);
		springT2.setEndBody(matDot_tEnd);		
		springT3.setStartBody(matDot_tStart);
		springT3.setEndBody(matDot_tEnd);	

		connector.setEndBody(matDot_tStart);
		connector.setStartBody(matDot_yetAnotherEnd3);
		
		bodies.add(spring1);
		bodies.add(spring2);
		bodies.add(spring3);
		bodies.add(spring4);
		bodies.add(spring5);
		bodies.add(springT1);
		bodies.add(springT2);
		bodies.add(springT3);
		bodies.add(matDot_end);
		bodies.add(matDot_start);
		bodies.add(matDot_anotherEnd);
		bodies.add(matDot_yetAnotherEnd);
		bodies.add(matDot_yetAnotherEnd2);
		bodies.add(matDot_yetAnotherEnd3);
		bodies.add(matDot_tStart);
		bodies.add(matDot_tUp);
		bodies.add(matDot_tEnd);
		bodies.add(connector);
		
		return bodies;
	}
	
	public Canvas getCanvas() {
		return canvas;
	}


	private Canvas canvas;
	private SystemStateComputer SSC;

}
