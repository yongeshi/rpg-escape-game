package rpgworld;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Use arrow keys to move character to around the screen.
 * Hold down space and an arrow key to make character run.
 */
public class Character {
	Pane root;
	Stage stage;
	AnimationTimer timer;
    public String standingImage = "Down2.png";;
    public ImageView character_image;
    LinkedList<ObstacleTile> barrier;
    boolean moveUp, moveRight, moveDown, moveLeft, run;
    ArrayList<Image> walkingUpImageList; 
    ArrayList<Image> walkingDownImageList;
    ArrayList<Image> walkingRightImageList; 
    ArrayList<Image> walkingLeftImageList; 
	int switchWhenZero = 0;  // used to help prevent switching through each walking image too fast
    int upCount;
    int downCount;
    int rightCount;
    int leftCount;
    boolean foundKey = false;
    boolean nearKey = false;

	public Character(Pane root, Stage stage, Scene scene, LinkedList<ObstacleTile> barrier, ImageView dialog, Text dialogText, ImageView key, ImageView character_image) {
		this.barrier = barrier;
		this.character_image = character_image;
		this.root = root;
		this.stage = stage;

		// Initialize the Character's Walking Images Lists
		walkingUpImageList = new ArrayList<Image>();
		walkingUpImageList.add(new Image("Up1.png"));
		walkingUpImageList.add(new Image("Up2.png"));
		walkingUpImageList.add(new Image("Up3.png"));
		
		walkingDownImageList = new ArrayList<Image>();
		walkingDownImageList.add(new Image("Down1.png"));
		walkingDownImageList.add(new Image("Down2.png"));
		walkingDownImageList.add(new Image("Down3.png"));
		
		walkingRightImageList = new ArrayList<Image>();
		walkingRightImageList.add(new Image("Right1.png"));
		walkingRightImageList.add(new Image("Right2.png"));
		walkingRightImageList.add(new Image("Right3.png"));
		
		walkingLeftImageList = new ArrayList<Image>();
		walkingLeftImageList.add(new Image("Left1.png"));
		walkingLeftImageList.add(new Image("Left2.png"));
		walkingLeftImageList.add(new Image("Left3.png"));
		
	    upCount = 0;
	    downCount = 0;
	    rightCount = 0;
	    leftCount = 0;

        // Set EventHandler on arrow key press/release
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent e) {
            	KeyCode code = e.getCode();
                if (code == KeyCode.UP) { moveUp = true; }
    	        else if (code == KeyCode.DOWN) { moveDown = true; }
    	        else if (code == KeyCode.RIGHT) { moveRight  = true; }
    	        else if (code == KeyCode.LEFT) { moveLeft  = true; }
    	        else if (code == KeyCode.SHIFT) { run = true; }
    	        else if (code == KeyCode.SPACE) {  // Space pressed - check for key and pop up dialog message
    	        	if (nearKey) {
        	        	foundKey = true; 	
        	        	dialogText.setText("Got Key!");
        	        	dialogText.setOpacity(1);
        	        	dialog.setOpacity(1);
        	        	key.setOpacity(1);
        	            
        	            PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
        	            pause.setOnFinished(ev -> { dialogText.setOpacity(0); dialog.setOpacity(0); });
        	            pause.play();

    	        	}
    	        	else {
    	        		dialogText.setText("Didn't find key.");
    	        		dialogText.setOpacity(1);
        	        	dialog.setOpacity(1);

        	            PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
        	            pause.setOnFinished(ev -> { dialogText.setOpacity(0); dialog.setOpacity(0); });
        	            pause.play();
    	        	}
    	        }
            }
        });
 
        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent e) {
            	KeyCode code = e.getCode();
                if (code == KeyCode.UP) { moveUp = false; }
    	        else if (code == KeyCode.DOWN) { moveDown = false; }
    	        else if (code == KeyCode.RIGHT) { moveRight  = false; }
    	        else if (code == KeyCode.LEFT) { moveLeft  = false; }
    	        else if (code == KeyCode.SHIFT) { run = false; }
            }
        });
        
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                int dx = 0;
                int dy = 0;
                
                // switchWhenZero is used because the walking images change too fast - used a count to change image after every 4 rounds
                if (moveUp) {  // Set speed and correct image
                	dy -= 3; 
        			if (switchWhenZero == 0) {
        				character_image.setImage(walkingUpImageList.get(upCount % 3)); 
	                	upCount++; 
	                	switchWhenZero = 4;
        			}
                	else {
                		switchWhenZero--;
                	}
                }
                else if (moveDown) { 
                	dy += 3; 
        			if (switchWhenZero == 0) {
        				character_image.setImage(walkingDownImageList.get(downCount % 3)); 
	                	downCount++; 
	                	switchWhenZero = 4;
        			}
                	else {
                		switchWhenZero--;
                	}
                }
                else if (moveRight) { 
                	dx += 3; 
        			if (switchWhenZero == 0) {
        				character_image.setImage(walkingRightImageList.get(rightCount % 3)); 
	                	rightCount++; 
	                	switchWhenZero = 4;
        			}
                	else {
                		switchWhenZero--;
                	}
                }
                else if (moveLeft) { 
                	dx -= 3;
        			if (switchWhenZero == 0) {
        				character_image.setImage(walkingLeftImageList.get(leftCount % 3)); 
	                	leftCount++; 
	                	switchWhenZero = 4;
        			}
                	else {
                		switchWhenZero--;
                	}
                }
                if (run) { 
                	dx *= 2; 
                	dy *= 2; 
                }
 
                moveCharacter(dx, dy);
            }
        };
        timer.start();
	}
	
	// Move character in the correct direction and also check for key/exit
    private void moveCharacter(int dx, int dy) {
        if (dx != 0 || dy != 0) {  // Only move if character has "speed" - added/subtracted from the key presses
	        double cx = character_image.getBoundsInLocal().getWidth()  / 2;
	        double cy = character_image.getBoundsInLocal().getHeight() / 2;
	 
	        double x = cx + character_image.getLayoutX() + dx;
	        double y = cy + character_image.getLayoutY() + dy;
	        
	        // Check if character should move
	        if ( x - cx >= 0 && 
	        		x + cx <= 737 &&  // Scene width
	                y - cy >= 0 &&
	                y + cy <= 800 && // Scene height
	                !hitBarrier(x - cx, y - cy) ) {
	        		character_image.relocate(x - cx, y - cy); 
	            }

	        // Check if character is near the key
	        if (x >= 587 && x <= 590 && y >= 130 && y <= 145) { nearKey = true; }
	        
	        // Check if character found key and is near the door
	        // Won the game!
	        if (foundKey && x >= 137 && x <= 257 && y >= 757 && y <= 778) {
	        	timer.stop(); // Need to end animation timer or else it will keep entering this if statement and pop up the win screen infinitely
	    		root = new Pane();
	    		Scene scene = new Scene(root, 737, 800);
	    		stage.setTitle("You Won!");
	    		stage.setScene(scene);
	    		stage.show();
	    		
	    		// Set background image
	    		Image backgroundImage = new Image("start_background.png");
	    		BackgroundSize size = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false);
	    		Background background = new Background(new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, size));
	    		root.setBackground(background);
	    		
	    		// Title
	    		ImageView title = new ImageView(new Image("You-Won.png"));
	    		title.setLayoutX(160);
	    		title.setLayoutY(275);
	    		root.getChildren().add(title);
	        }
        }
        else { character_image.setImage(new Image(standingImage)); }  // if not moving, set character image to standing image

    }
    
    // Returns true if character hit a wall/object
    private boolean hitBarrier(double wantsToGoToThisX, double wantsToGoToThisY) {
    	Iterator<ObstacleTile> it = barrier.iterator();
		while (it.hasNext()) {
			ObstacleTile t = it.next();
			
			double spriteMinX = wantsToGoToThisX + 5;
			double spriteMinY = wantsToGoToThisY + 5;
			double spriteMaxX = wantsToGoToThisX + character_image.getBoundsInLocal().getWidth() - 10; 
			double spriteMaxY = wantsToGoToThisY + character_image.getBoundsInLocal().getHeight();

			double tMinX = t.getX();
			double tMinY = t.getY();
			double tMaxX = t.getX() + t.getWidth();
			double tMaxY = t.getY() + t.getHeight();
			
			boolean inside = spriteMaxX > tMinX && spriteMinX < tMaxX && spriteMaxY > tMinY && spriteMinY < tMaxY;

			if (inside) {
				return true;
			}
		}
    	return false;
    }
}
