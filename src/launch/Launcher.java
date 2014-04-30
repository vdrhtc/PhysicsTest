package launch;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SceneBuilder;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import view.ViewUpdater;
import baseObjects.Body;
import baseObjects.Rod;
import baseObjects.RoughBody;
import baseObjects.Spring;
import calculation.RadiusVector;
import calculation.SystemState;
import calculation.SystemStateComputer;

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
			@Override
			public void handle(ActionEvent e) {
				SSC.launchBodiesUpdate();
			}
		});

		root.getChildren().addAll(getCanvas(), launch);

		Scene scene = SceneBuilder.create().root(root).build();
		primaryStage.setScene(scene);
		primaryStage.show();

		SystemState s = new SystemState(bodies);
		SSC = new SystemStateComputer(s);
		ViewUpdater.setGraphicsContext(canvas.getGraphicsContext2D());

		SSC.launchMonitors();

		ViewUpdater.forceDraw(s);
		ViewUpdater.launchDraw();

	}

	private ArrayList<Body> createBodies() {
		ArrayList<Body> bodies = new ArrayList<>();
		Body matDot_start = new RoughBody(1, new RadiusVector(300, 500), 0);
		Body matDot_end = new RoughBody(1, new RadiusVector(400, 500), 0);
		Body matDot_anotherEnd = new RoughBody(1, new RadiusVector(400, 400),0);
		Body matDot_yetAnotherEnd = new RoughBody(1, new RadiusVector(300, 400), 0);
		 
		 
		Body matDot_yetAnotherEnd2 = new RoughBody(100, new RadiusVector(700, 500), 0);
//		 Body matDot_yetAnotherEnd3 = new RoughBody(1, new RadiusVector(450,
//		 750), 0);

		/* Чек */Spring spring1 = new Rod(100, matDot_start, matDot_end);
		 Spring spring2 = new Rod(100, matDot_end, matDot_anotherEnd);
		 Spring spring3 = new Rod(100, matDot_anotherEnd, matDot_yetAnotherEnd);
		 Spring spring4 = new Rod(100, matDot_yetAnotherEnd, matDot_start);
		 
		 Spring spring5 = new Spring(50, 100, matDot_anotherEnd,
		 matDot_yetAnotherEnd2);
		//
		// Body matDot_tStart = new RoughBody(0.1, new RadiusVector(400, 300),
		// 0);
		// Body matDot_tUp = new RoughBody(0.1, new RadiusVector(350, 780), 0);
		// Body matDot_tEnd = new RoughBody(0.1, new RadiusVector(320, 750), 0);
		// Body matDot_tYetAnotherEnd = new RoughBody(0.1, new RadiusVector(300,
		// 700), 0);
		//
		// Spring springT1 = new Spring(10, 70.7107, matDot_tStart, matDot_tUp);
		// Spring springT2 = new Spring(10, 70.7107, matDot_tUp, matDot_tEnd);
		// Spring springT3 = new Spring(10, 100, matDot_tStart, matDot_tEnd);
		 Spring connector = new Rod(100*1.4, matDot_anotherEnd, matDot_start);

		 bodies.add(spring1);
		bodies.add(spring2);
		 bodies.add(spring3);
		 bodies.add(spring4);
		 bodies.add(spring5);
		// bodies.add(springT1);
		// bodies.add(springT2);
		// bodies.add(springT3);
		bodies.add(matDot_end);
		 bodies.add(matDot_start);
		bodies.add(matDot_anotherEnd);
		 bodies.add(matDot_yetAnotherEnd);
		 bodies.add(matDot_yetAnotherEnd2);
//		 bodies.add(matDot_yetAnotherEnd3);
		// bodies.add(matDot_tStart);
		// bodies.add(matDot_tUp);
		// bodies.add(matDot_tEnd);
		 bodies.add(connector);
		return bodies;
	}

	public Canvas getCanvas() {
		return canvas;
	}

	private Canvas canvas;
	private SystemStateComputer SSC;

}
