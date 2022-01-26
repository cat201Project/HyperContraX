package application;

//import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SecondScreen extends Stage{
	
	//Initialize all the variables
	private static final Random RAND = new Random();
	private static final int WIDTH = 600;
	private static final int HEIGHT = 600;
	private static final int PLAYER_SIZE = 70;
	static final Image PLAYER_IMG = new Image("https://drive.google.com/uc?export=view&id=1lLPxyXTVjJN5iBy5TpfPufXsWERSO7ph"); 
	static final Image EXPLOSION_IMG = new Image("https://drive.google.com/uc?export=view&id=1amqhqupVnR5oG6J2EK6PNZ7G9Gq4aAyl");
	static final int EXPLOSION_WIDTH = 128;
	static final int EXPLOSION_ROWS = 3;
	static final int EXPLOSION_COL = 3;
	static final int EXPLOSION_H = 128;
	static final int EXPLOSION_STEPS = 15;
	//image for enemies
	static final Image BOMBS_IMG[] = {
			new Image("https://drive.google.com/uc?export=view&id=17rRyuVL5hiCVTNiDuPWqsSgr2UxXP7AI"),
			new Image("https://drive.google.com/uc?export=view&id=1AgsZz6Ik4pMDT653hPDgM5IHDXThOtAr"),
			new Image("https://drive.google.com/uc?export=view&id=1RFQYMdvhZJjSxVwMsBuLoz3sc2rgWDcX"),
			new Image("https://drive.google.com/uc?export=view&id=1or1gZvaed3B7v56JDWwM59as2WQNl7uD"),
			new Image("https://drive.google.com/uc?export=view&id=1aQlbMBqiAqOScpPvKnG4Q9w0Qjx1VzAK"),
			new Image("https://drive.google.com/uc?export=view&id=1CT61W_TwmAwDMQU6sfUTbLocFAoaiowo"),
			new Image("https://drive.google.com/uc?export=view&id=1FBHOiedVDAauajyt310hsJe00oT2DkXn"),
			new Image("https://drive.google.com/uc?export=view&id=1jvy4JClKyZbuSzQ5YrP258qxM9wKbqdc"),
			new Image("https://drive.google.com/uc?export=view&id=10Hbmv9Hm-QqtrmhPwfdp6aVqN98HqcR4"),
			new Image("https://drive.google.com/uc?export=view&id=1d2Ihsuoz1pJKDePpuKZIrbJKp7owPTQz")
	};
	
	final int MAX_BOMBS = 10,  MAX_SHOTS = MAX_BOMBS * 2;
	boolean gameOver = false;
	private GraphicsContext gc;
	
	
	Rocket player;
	List<Shot> shots;
	List<Universe> univ;
	List<Bomb> Bombs;
	
	private double mouseX;
	private int score;
	VBox y = new VBox();
	Connection con;
	PreparedStatement pst;


	//start
	SecondScreen(String name){
		y.getChildren();
		Canvas canvas = new Canvas(WIDTH, HEIGHT);
		gc = canvas.getGraphicsContext2D();
		Timeline timeline = new Timeline(new KeyFrame(Duration.millis(100), e -> run(gc,name)));
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.play();
		canvas.setCursor(Cursor.MOVE);
		canvas.setOnMouseMoved(e -> mouseX = e.getX());
		canvas.setOnMouseClicked(e -> {
			if(shots.size() < MAX_SHOTS) shots.add(player.shoot());
			if(gameOver) { 
				gameOver = false;
				extracted();
				//setup();
			}
		});
		setup();
		this.setScene(new Scene(new StackPane(canvas)));
		this.setTitle("Space Invaders");
		this.show();
	}
	
}
	