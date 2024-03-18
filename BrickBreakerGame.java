	import javafx.animation.AnimationTimer;
	import javafx.application.Application;
	import javafx.scene.Scene;
	import javafx.scene.input.KeyCode;
	import javafx.scene.layout.Pane;
	import javafx.scene.paint.Color;
	import javafx.scene.shape.Rectangle;
	import javafx.scene.text.Font;
	import javafx.scene.text.Text;
	import javafx.stage.Stage;

	import java.util.ArrayList;
	import java.util.List;
	import java.util.Random;

	public class BrickBreakerGame extends Application {

	    private static final int WIDTH = 800;
	    private static final int HEIGHT = 600;
	    private static final int PADDLE_WIDTH = 100;
	    private static final int PADDLE_HEIGHT = 10;
	    private static final int BALL_RADIUS = 10;
	    private static final int BRICK_WIDTH = 70;
	    private static final int BRICK_HEIGHT = 30;
	    private static final int BRICK_GAP = 5;
	    private static final int BRICK_ROWS = 5;
	    private static final int BRICK_COLUMNS = 10;
	    private static final int PADDLE_SPEED = 7;
	    private static double BALL_SPEED = 5.0;

	    private Pane root;
	    private Rectangle paddle;
	    private Rectangle ball;
	    private List<Rectangle> bricks;
	    private Text scoreText;
	    private int score = 0;
	    private boolean ballMoving = false;

	    @Override
	    public void start(Stage primaryStage) {
	        root = new Pane();
	        Scene scene = new Scene(root, WIDTH, HEIGHT);
	        primaryStage.setScene(scene);
	        primaryStage.setTitle("Brick Breaker Game");
	        primaryStage.setResizable(false);
	        primaryStage.show();

	        createPaddle();
	        createBall();
	        createBricks();
	        createScoreText();

	        scene.setOnKeyPressed(event -> {
	            if (event.getCode() == KeyCode.LEFT) {
	                movePaddle(-PADDLE_SPEED);
	            } else if (event.getCode() == KeyCode.RIGHT) {
	                movePaddle(PADDLE_SPEED);
	            } else if (event.getCode() == KeyCode.SPACE && !ballMoving) {
	                startGame();
	            }
	        });

	        AnimationTimer timer = new AnimationTimer() {
	            @Override
	            public void handle(long now) {
	                if (ballMoving) {
	                    moveBall();
	                    checkCollisions();
	                }
	            }
	        };
	        timer.start();
	    }

	    private void createPaddle() {
	        paddle = new Rectangle((WIDTH - PADDLE_WIDTH) / 2, HEIGHT - PADDLE_HEIGHT - 10, PADDLE_WIDTH, PADDLE_HEIGHT);
	        paddle.setFill(Color.BLUE);
	        root.getChildren().add(paddle);
	    }

	    private void createBall() {
	        ball = new Rectangle((WIDTH - BALL_RADIUS) / 2, HEIGHT - PADDLE_HEIGHT - 10 - BALL_RADIUS, BALL_RADIUS, BALL_RADIUS);
	        ball.setFill(Color.WHITE);
	        root.getChildren().add(ball);
	    }

	    private void createBricks() {
	        bricks = new ArrayList<>();
	        Random rand = new Random();
	        for (int i = 0; i < BRICK_ROWS; i++) {
	            for (int j = 0; j < BRICK_COLUMNS; j++) {
	                Rectangle brick = new Rectangle(j * (BRICK_WIDTH + BRICK_GAP) + BRICK_GAP, i * (BRICK_HEIGHT + BRICK_GAP) + BRICK_GAP,
	                        BRICK_WIDTH, BRICK_HEIGHT);
	                brick.setFill(Color.RED);
	                bricks.add(brick);
	                root.getChildren().add(brick);
	            }
	        }
	    }

	    private void createScoreText() {
	        scoreText = new Text(10, 20, "Score: " + score);
	        scoreText.setFont(Font.font(20));
	        scoreText.setFill(Color.WHITE);
	        root.getChildren().add(scoreText);
	    }

	    private void movePaddle(int deltaX) {
	        double newX = paddle.getX() + deltaX;
	        if (newX >= 0 && newX <= WIDTH - PADDLE_WIDTH) {
	            paddle.setX(newX);
	        }
	    }

	    private void startGame() {
	        ballMoving = true;
	    }

	    private void moveBall() {
	        ball.setLayoutX(ball.getLayoutX() + BALL_SPEED);
	        ball.setLayoutY(ball.getLayoutY() - BALL_SPEED);
	    }

	    private void checkCollisions() {
	        if (ball.getBoundsInParent().intersects(paddle.getBoundsInParent())) {
	            // Ball hits paddle
	             BALL_SPEED *= -1; // Reverse ball direction
	        }

	        for (Rectangle brick : bricks) {
	            if (ball.getBoundsInParent().intersects(brick.getBoundsInParent())) {
	                // Ball hits brick
	                root.getChildren().remove(brick);
	                bricks.remove(brick);
	                score++;
	                scoreText.setText("Score: " + score);
	                break;
	            }
	        }

	        // Ball hits walls
	        if (ball.getLayoutX() <= 0 || ball.getLayoutX() >= WIDTH - BALL_RADIUS) {
	            BALL_SPEED *= -1; // Reverse ball direction
	        }
	        if (ball.getLayoutY() <= 0) {
	            BALL_SPEED *= -1; // Reverse ball direction
	        }

	        // Game over condition
	        if (ball.getLayoutY() >= HEIGHT) {
	            ballMoving = false;
	            ball.setLayoutX((WIDTH - BALL_RADIUS) / 2);
	            ball.setLayoutY(HEIGHT - PADDLE_HEIGHT - 10 - BALL_RADIUS);
	            BALL_SPEED = Math.abs(BALL_SPEED); // Reset ball speed
	            score = 0;
	            scoreText.setText("Score: " + score);
	            for (Rectangle brick : bricks) {
	                root.getChildren().add(brick);
	            }
	            bricks.clear();
	            createBricks();
	        }
	    }

	    public static void main(String[] args) {
	        launch(args);
	    }
	}
