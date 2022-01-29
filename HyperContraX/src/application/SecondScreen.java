package application;

import java.sql.*;
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
	private static final int PLAYER_SIZE = 80;
	static final Image PLAYER_IMG = new Image("https://drive.google.com/uc?export=view&id=1lLPxyXTVjJN5iBy5TpfPufXsWERSO7ph"); 
	static final Image EXPLOSION_IMG = new Image("https://drive.google.com/uc?export=view&id=1amqhqupVnR5oG6J2EK6PNZ7G9Gq4aAyl");
	static final int EXPLOSION_WIDTH = 128;
	static final int EXPLOSION_ROWS = 3;
	static final int EXPLOSION_COL = 3;
	static final int EXPLOSION_H = 128;
	static final int EXPLOSION_STEPS = 15;
	
	// Initialize all the save in CAT201 Project google drive
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
	private String playerName;
	VBox y = new VBox();

	// Second screen constructor
	SecondScreen(String name){
		
		y.getChildren();
		
		// Creating new canvas window
		Canvas canvas = new Canvas(WIDTH, HEIGHT);
		
		// Set up the canvas with a 2D graphics
		gc = canvas.getGraphicsContext2D();
		
		// Run all the graphic by animating the node by x axis 
		Timeline timeline = new Timeline(new KeyFrame(Duration.millis(100), e -> run(gc)));
		
		// Set Cycle Count to indefinite for animation to repeat definitely
		timeline.setCycleCount(Timeline.INDEFINITE);
		
		// Play the timeline 
		timeline.play();
		
		// Set the cursor on move
		canvas.setCursor(Cursor.MOVE); 
		
		// Set the mouse to moved in the x-axis only 
		canvas.setOnMouseMoved(e -> mouseX = e.getX());
		
		// When the mouse is clicked
		canvas.setOnMouseClicked(e -> {
			
			// The player will keep playing if the game is not over
			if(shots.size() < MAX_SHOTS) shots.add(player.shoot());
			if(gameOver) { 
				gameOver = false;
				extracted();
				try {
					PlayerHighScore(playerName,score);
				} 
				catch (Exception ex) {}
			}
		});
		
		setup(name);
		this.setScene(new Scene(new StackPane(canvas)));
		this.setTitle("Space Invaders");
		this.show();
		this.toFront();
	}
	
	// Function to close the Second Screen
	private void extracted() {
		close();
	}

	// Function to setup the game
	private void setup(String name) {
		
		// Initialize the variable into an array
		// Initialize all the variable
		univ = new ArrayList<>();
		shots = new ArrayList<>();
		Bombs = new ArrayList<>();
		player = new Rocket(WIDTH / 2, HEIGHT - PLAYER_SIZE, PLAYER_SIZE, PLAYER_IMG);
		score = 0;
		playerName = name;
		IntStream.range(0, MAX_BOMBS).mapToObj(i -> this.newBomb()).forEach(Bombs::add);
	}
	
	// Function to run all the graphics
	private void run(GraphicsContext gc){
		
		// Set the style of the score board at the top left corner
		gc.setFill(Color.grayRgb(20));
		gc.fillRect(0, 0, WIDTH, HEIGHT);
		gc.setTextAlign(TextAlignment.CENTER);
		gc.setFont(Font.font(20));
		gc.setFill(Color.WHITE);
		gc.fillText("Score: " + score, 60,  20);
	
		// Set the style of the text that preview to player their score
		if(gameOver) {
			gc.setFont(Font.font(35));
			gc.setFill(Color.YELLOW);
			gc.fillText("Game Over \n Your Score is: " + score + " \n Click to play again", WIDTH / 2, HEIGHT /2.5);
		}
		
		// Draw the universe
		univ.forEach(Universe::draw);
		
		// Setup the player image at the position of the mouse
		player.update();
		player.draw();
		player.posX = (int) mouseX;
		
		// Ensure the player will explode when the player hits the bombs
		Bombs.stream().peek(Rocket::update).peek(Rocket::draw).forEach(e -> {
			if(player.colide(e) && !player.exploding) {
				player.explode();
			}
		});
		
		// Remove the bullet when the it hits the enemies
		for (int i = shots.size() - 1; i >=0 ; i--) {
			Shot shot = shots.get(i);
			if(shot.posY < 0 || shot.toRemove)  { 
				shots.remove(i);
				continue;
			}
			shot.update();
			shot.draw();
			
			// Increase the score when the bullet of the player hits the enemies
			for (Bomb bomb : Bombs) {
				if(shot.colide(bomb) && !bomb.exploding) {
					score++;
					bomb.explode();
					shot.toRemove = true;
				}
			}
		}
		
		// Set the position of the bombs
		for (int i = Bombs.size() - 1; i >= 0; i--){  
			if(Bombs.get(i).destroyed)  {
				Bombs.set(i, newBomb());
			}
		}
		
		
		gameOver = player.destroyed;
		
		// Ran the universe when the player are destroyed
		if(RAND.nextInt(10) > 2) {
			univ.add(new Universe());
		}
		for (int i = 0; i < univ.size(); i++) {
			if(univ.get(i).posY > HEIGHT)
				univ.remove(i);
		}
		
	}

	// Player
	public class Rocket {

		int posX, posY, size;
		boolean exploding, destroyed;
		Image img;
		int explosionStep = 0;
		
		// Rocket constructor
		public Rocket(int posX, int posY, int size,  Image image) {
			this.posX = posX;
			this.posY = posY;
			this.size = size;
			img = image;
		}
		
		// Function for user to shoot the enemies
		public Shot shoot() {
			
			// Return the value of posX and posY of the bullet when the player click the mouse
			return new Shot(posX + size / 2 - Shot.size / 2, posY - Shot.size);
		}

		public void update() {
			
			// Increase the explosion step to animate the explosion image
			if(exploding) explosionStep++;
			
			// The enemies rocket will destroyed when destroyed are true;
			destroyed = explosionStep > EXPLOSION_STEPS;
		}
		
		public void draw() {
			
			// Show the explosion image at the exact position of the enemies
			if(exploding) {
				gc.drawImage(EXPLOSION_IMG, explosionStep % EXPLOSION_COL * EXPLOSION_WIDTH, (explosionStep / EXPLOSION_ROWS) * EXPLOSION_H + 1,
						EXPLOSION_WIDTH, EXPLOSION_H,
						posX, posY, size, size);
			}
			else {
				// show the enemies rocket
				gc.drawImage(img, posX, posY, size, size);
			}
		}
		
		// Function if player collide with the enemies
		public boolean colide(Rocket other) {
			int d = distance(this.posX + size / 2, this.posY + size /2, 
							other.posX + other.size / 2, other.posY + other.size / 2);
			return d < other.size / 2 + this.size / 2 ;
		}
		
		public void explode() {
			exploding = true;
			explosionStep = -1;
		}

	}
	
	// Computer player (enemies)
	public class Bomb extends Rocket {
		
		int SPEED = (score/5)+2;
		
		// Bomb constructor
		public Bomb(int posX, int posY, int size, Image image) {
			// Initialize the position of rocket
			super(posX, posY, size, image);
		}
		
		public void update() {
			super.update();
			// If the statement is true increase the speed of the enemies
			if(!exploding && !destroyed) posY += SPEED;
			// The Bomb enemies will be destroyed if the y-axis of the 
			// enemies are more than the height of the canvas
			if(posY > HEIGHT) destroyed = true;
		}
	}

	// Bullets
	public class Shot {
		
		public boolean toRemove;

		int posX, posY, speed = 10;
		static final int size = 6;
		
		// Shot constructor
		public Shot(int posX, int posY) {
			this.posX = posX;
			this.posY = posY;
		}
		
		
		public void update() {
			posY-=speed;
		}
		

		public void draw() {
			// Set the style of the bullet
			gc.setFill(Color.YELLOWGREEN);
			// If the statement is true set the colour of the bullet to yellowgreen
			if (score >=50 && score<=70) {
				gc.setFill(Color.RED);
				// Increase the speed of the bullet
				speed = 50;
				// Set the position and size of the the rectangle shape bullet
				gc.fillRect(posX-5, posY-10, size+10, size+30);
			}
			if (score >=100 && score<= 150 || score>= 180) {
				gc.setFill(Color.BLUE);
				// Increase the speed of the bullet
				speed = 60;
				// Set the position and size of the the rectangle shape bullet
				gc.fillRect(posX-5, posY-10, size+10, size+30);
			}else {
			// Set the position and size of the the oval shape bullet
			gc.fillOval(posX, posY, size, size);
			}
		}
		
		// Function when the bullet collide with the rocket
		public boolean colide(Rocket Rocket) {
			int distance = distance(this.posX + size / 2, this.posY + size / 2, 
					Rocket.posX + Rocket.size / 2, Rocket.posY + Rocket.size / 2);
			return distance  < Rocket.size / 2 + size / 2;
		} 
		
		
	}
	
	// Environment
	public class Universe {
		int posX, posY;
		private int h, w, r, g, b;
		private double opacity;
		
		// Universe constructor
		public Universe() {
			posX = RAND.nextInt(WIDTH);
			posY = 0;
			w = RAND.nextInt(5) + 1;
			h =  RAND.nextInt(5) + 1;
			r = RAND.nextInt(100) + 150;
			g = RAND.nextInt(100) + 150;
			b = RAND.nextInt(100) + 150;
			opacity = RAND.nextFloat();
			if(opacity < 0) opacity *=-1;
			if(opacity > 0.5) opacity = 0.5;
		}
		
		// Create a background of the game to look like in universe
		public void draw() {
			if(opacity > 0.8) opacity-=0.01;
			if(opacity < 0.1) opacity+=0.01;
			// Fill the color of the star in the universe
			gc.setFill(Color.rgb(r, g, b, opacity));
			gc.fillOval(posX, posY, w, h);
			posY+=20;
		}
	}
	
	// Initialize the enemies variable in every setup
	Bomb newBomb() {
		return new Bomb(50 + RAND.nextInt(WIDTH - 100), 0, PLAYER_SIZE, BOMBS_IMG[RAND.nextInt(BOMBS_IMG.length)]);
	}
	
	// Initialize the distance
	int distance(int x1, int y1, int x2, int y2) {
		return (int) Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
	}
	
	// Save player name and score into the database 
	public void PlayerHighScore (String name, int score2) throws ClassNotFoundException{
		
		// Convert the value of score to string
		String stdScore = String.valueOf(score2);

        try
        {
        	Class.forName("com.mysql.cj.jdbc.Driver");
        	
        	// Create a connection to the localhost of the database
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost/mysql", "root", "");
			
			// Create table if the table does not exist in the database mysql
			String sqlCreate = "CREATE TABLE IF NOT EXISTS highscore"
            + "  (id           INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,"
            + "   name            VARCHAR(20) NOT NULL,"
            + "   score          INTEGER NOT NULL)";
			
			// Static sql statement to create and execute general purpose SQL statement using Java programs
			Statement stmt = con.createStatement();
			stmt.execute(sqlCreate);
            
			// Prepared statement used to execute the sql statement in more efficient way
			PreparedStatement pst = con.prepareStatement("insert into highscore(name,score)values(?,?)");
			pst.setString(1, name);
			pst.setString(2, stdScore);
			pst.executeUpdate();
            con.close();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
		
        
	}
	
}