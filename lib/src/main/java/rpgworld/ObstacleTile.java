package rpgworld;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ObstacleTile extends Rectangle {
	public ObstacleTile(double width, double height, double x, double y) {
		super(width, height);  // 50, 53
		setFill(Color.rgb(0, 0, 0, 0));  // make rectangle transparent
		setX(x);
		setY(y);
	}
}