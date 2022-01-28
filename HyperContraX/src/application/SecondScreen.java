// This is the second screen application after starting the program from first screen.
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

public class SecondScreen extends Stage {
	// Initialize all the variables
	private static final Random RAND = new Random();
	private static final int WIDTH = 600;
	private static final int HEIGHT = 600;
	private static final int userSize = 70;
	// User image & variable.
	static final Image PLAYER_PLANE = new Image(
			"https://drive.google.com/uc?export=view&id=1lLPxyXTVjJN5iBy5TpfPufXsWERSO7ph");
	static final Image IMAGE_EXPLOSION = new Image(
			"https://drive.google.com/uc?export=view&id=1amqhqupVnR5oG6J2EK6PNZ7G9Gq4aAyl");
	static final int WIDTH_EXPLOSION = 128;
	static final int ROWS_EXPLOSION = 3;
	static final int COL_EXPLOSION = 3;
	static final int HEIGHT_EXPLOSION = 128;
	static final int STEPS_EXPLOSION = 15;
	// Enemy image.
	static final Image IMAGE_ENEMIES[] = {
			new Image("https://drive.google.com/uc?export=view&id=17rRyuVL5hiCVTNiDuPWqsSgr2UxXP7AI"),
			new Image("https://drive.google.com/uc?export=view&id=1AgsZz6Ik4pMDT653hPDgM5IHDXThOtAr"),
			new Image("https://drive.google.com/uc?export=view&id=1RFQYMdvhZJjSxVwMsBuLoz3sc2rgWDcX"),
			new Image("https://drive.google.com/uc?export=view&id=1or1gZvaed3B7v56JDWwM59as2WQNl7uD"),
			new Image("https://drive.google.com/uc?export=view&id=1aQlbMBqiAqOScpPvKnG4Q9w0Qjx1VzAK"),
			new Image("https://drive.google.com/uc?export=view&id=1CT61W_TwmAwDMQU6sfUTbLocFAoaiowo"),
			new Image("https://drive.google.com/uc?export=view&id=1FBHOiedVDAauajyt310hsJe00oT2DkXn"),
			new Image("https://drive.google.com/uc?export=view&id=1jvy4JClKyZbuSzQ5YrP258qxM9wKbqdc"),
			new Image("https://drive.google.com/uc?export=view&id=10Hbmv9Hm-QqtrmhPwfdp6aVqN98HqcR4"),
			new Image("https://drive.google.com/uc?export=view&id=1d2Ihsuoz1pJKDePpuKZIrbJKp7owPTQz") };
	final int MAXIMUM_BOMBS = 10, MAX_BULLETS = MAXIMUM_BOMBS * 2;
	boolean gameEnd = false;
	private GraphicsContext graphicsCtxt;
	
	Rocket user;
	List<Shot> bullets;
	List<Universe> galaxy;
	List<Bomb> Bombs;

	private double x_mouse;
	private int score;
	private String userName;
	VBox y = new VBox();


	// To start the game.
	SecondScreen(String name) {
		y.getChildren();
		Canvas canvas = new Canvas(WIDTH, HEIGHT);
		graphicsCtxt = canvas.getGraphicsContext2D();
		Timeline timeline = new Timeline(new KeyFrame(Duration.millis(100), e -> run(graphicsCtxt)));
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.play();
		canvas.setCursor(Cursor.MOVE);
		canvas.setOnMouseMoved(e -> x_mouse = e.getX());
		canvas.setOnMouseClicked(e -> {
			if (bullets.size() < MAX_BULLETS)
				bullets.add(user.shoot());
			if (gameEnd) {
				gameEnd = false;
				extracted();
				try {
					PlayerHighScore(userName,score);
				} 
				catch (Exception ex) {}
			}
		});
		setup(name);
		this.setScene(new Scene(new StackPane(canvas)));
		this.setTitle("Space Invaders");
		this.show();
	}

	// To close the screen
	private void extracted() {
		close();
	}

	// Setup the game size object
	private void setup(String name) {
		galaxy = new ArrayList<>();
		bullets = new ArrayList<>();
		Bombs = new ArrayList<>();
		user = new Rocket(WIDTH / 2, HEIGHT - userSize, userSize, PLAYER_PLANE);
		score = 0;
		userName = name;
		IntStream.range(0, MAXIMUM_BOMBS).mapToObj(i -> this.newBomb()).forEach(Bombs::add);
	}

	// To run the graphic
	private void run(GraphicsContext graphicsCtxt) {
		graphicsCtxt.setFill(Color.grayRgb(20));
		graphicsCtxt.fillRect(0, 0, WIDTH, HEIGHT);
		graphicsCtxt.setTextAlign(TextAlignment.CENTER);
		graphicsCtxt.setFont(Font.font(20));
		graphicsCtxt.setFill(Color.WHITE);
		graphicsCtxt.fillText("Score: " + score, 60, 20);

		// If user get killed & back to main page when click the application.
		if (gameEnd) {
			graphicsCtxt.setFont(Font.font(35));
			graphicsCtxt.setFill(Color.YELLOW);
			graphicsCtxt.fillText("Game Over \n Your Score is: " + score + " \n Click to play again", WIDTH / 2, HEIGHT / 2.5);
		}

		galaxy.forEach(Universe::draw);
		user.update();
		user.draw();
		user.posX = (int) x_mouse;

		Bombs.stream().peek(Rocket::update).peek(Rocket::draw).forEach(e -> {
			if (user.colide(e) && !user.exploding) {
				user.explode();
			}
		});

		for (int i = bullets.size() - 1; i >= 0; i--) {
			Shot shot = bullets.get(i);
			if (shot.posY < 0 || shot.toRemove) {
				bullets.remove(i);
				continue;
			}
			shot.update();
			shot.draw();
			for (Bomb bomb : Bombs) {
				if (shot.colide(bomb) && !bomb.exploding) {
					score++;
					bomb.explode();
					shot.toRemove = true;
				}
			}
		}

		for (int i = Bombs.size() - 1; i >= 0; i--) {
			if (Bombs.get(i).destroyed) {
				Bombs.set(i, newBomb());
			}
		}

		gameEnd = user.destroyed;
		if (RAND.nextInt(10) > 2) {
			galaxy.add(new Universe());
		}
		for (int i = 0; i < galaxy.size(); i++) {
			if (galaxy.get(i).posY > HEIGHT)
				galaxy.remove(i);
		}
		
	}

	// Player viewpoint
	public class Rocket {

		int posX, posY, size;
		boolean exploding, destroyed;
		Image img;
		int explosionStep = 0;

		// Rocket design
		public Rocket(int posX, int posY, int size, Image image) {
			this.posX = posX;
			this.posY = posY;
			this.size = size;
			img = image;
		}

		// Edit size of bullet.
		public Shot shoot() {
			return new Shot(posX + size / 2 - Shot.size / 2, posY - Shot.size);
		}

		// To update the score when user hit the targets
		public void update() {
			if (exploding)
				explosionStep++;
			destroyed = explosionStep > STEPS_EXPLOSION;
		}

		// Explosion image & size
		public void draw() {
			if (exploding) {
				graphicsCtxt.drawImage(IMAGE_EXPLOSION, explosionStep % COL_EXPLOSION * WIDTH_EXPLOSION,
						(explosionStep / ROWS_EXPLOSION) * HEIGHT_EXPLOSION + 1, WIDTH_EXPLOSION, HEIGHT_EXPLOSION,
						posX, posY, size, size);
			} else {
				graphicsCtxt.drawImage(img, posX, posY, size, size);
			}
		}

		public boolean colide(Rocket other) {
			int d = distance(this.posX + size / 2, this.posY + size / 2, other.posX + other.size / 2,
					other.posY + other.size / 2);
			return d < other.size / 2 + this.size / 2;
		}

		public void explode() {
			exploding = true;
			explosionStep = -1;
		}

	}

	// Player Computer
	// Speed change when score increases
	public class Bomb extends Rocket {

		int SPEED = (score / 5) + 2;

		public Bomb(int posX, int posY, int size, Image image) {
			super(posX, posY, size, image);
		}

		public void update() {
			super.update();
			if (!exploding && !destroyed)
				posY += SPEED;
			if (posY > HEIGHT)
				destroyed = true;
		}
	}

	// Bullet
	public class Shot {

		public boolean toRemove;

		int posX, posY, speed = 10;
		static final int size = 6;

		public Shot(int posX, int posY) {
			this.posX = posX;
			this.posY = posY;
		}

		public void update() {
			posY -= speed;
		}

		// Bullet color and change when hit specific score.
		public void draw() {
			graphicsCtxt.setFill(Color.RED);
			if (score >= 50 && score <= 70 || score >= 120) {
				graphicsCtxt.setFill(Color.YELLOWGREEN);
				speed = 50;
				graphicsCtxt.fillRect(posX - 5, posY - 10, size + 10, size + 30);
			} else {
				graphicsCtxt.fillOval(posX, posY, size, size);
			}
		}

		public boolean colide(Rocket Rocket) {
			int distance = distance(this.posX + size / 2, this.posY + size / 2, Rocket.posX + Rocket.size / 2,
					Rocket.posY + Rocket.size / 2);
			return distance < Rocket.size / 2 + size / 2;
		}

	}

	// Environment the background universe.
	public class Universe {
		int posX, posY;
		private int h, w, r, g, b;
		private double opacity;

		public Universe() {
			posX = RAND.nextInt(WIDTH);
			posY = 0;
			w = RAND.nextInt(5) + 1;
			h = RAND.nextInt(5) + 1;
			r = RAND.nextInt(100) + 150;
			g = RAND.nextInt(100) + 150;
			b = RAND.nextInt(100) + 150;
			opacity = RAND.nextFloat();
			if (opacity < 0)
				opacity *= -1;
			if (opacity > 0.5)
				opacity = 0.5;
		}

		public void draw() {
			if (opacity > 0.8)
				opacity -= 0.01;
			if (opacity < 0.1)
				opacity += 0.01;
			graphicsCtxt.setFill(Color.rgb(r, g, b, opacity));
			graphicsCtxt.fillOval(posX, posY, w, h);
			posY += 20;
		}
	}

	// Appear random bomb at the screen
	Bomb newBomb() {
		return new Bomb(50 + RAND.nextInt(WIDTH - 100), 0, userSize, IMAGE_ENEMIES[RAND.nextInt(IMAGE_ENEMIES.length)]);
	}

	int distance(int x1, int y1, int x2, int y2) {
		return (int) Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
	}

	// Save user name and score into the database 
	public void PlayerHighScore (String name, int score2) throws ClassNotFoundException{
		
		String stdScore = String.valueOf(score2);

        try
        {
        	
        	Class.forName("com.mysql.cj.jdbc.Driver");
        	// Create a connection to the localhost of the database
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost/mysql", "root", "");
			
			// Create table if the table does not exist in the database mysql
			String sqlCreate = "CREATE TABLE IF NOT EXISTS highScore"
            + "  (id           INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,"
            + "   name            VARCHAR(20) NOT NULL,"
            + "   score          INTEGER NOT NULL)";

			Statement stmt = con.createStatement();
			stmt.execute(sqlCreate);
             
			PreparedStatement pst = con.prepareStatement("insert into highscore(name,score)values(?,?)");
			pst.setString(1, name);
			pst.setString(2, stdScore);
			pst.executeUpdate();
            con.close();
        }
        catch(Exception e){}
		  
	}
}
