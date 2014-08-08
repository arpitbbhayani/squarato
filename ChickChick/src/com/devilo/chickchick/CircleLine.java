package com.devilo.chickchick;

import com.badlogic.gdx.math.Rectangle;

/**
 * Created by devilo on 7/8/14.
 */
public class CircleLine {

    Rectangle left, right;

    public CircleLine(Rectangle circleRectangle1, Rectangle circleRectangle2) {
        left = circleRectangle1;
        right = circleRectangle2;
    }

    public Rectangle getLeft() {
        return left;
    }

    public void setLeft(Rectangle left) {
        this.left = left;
    }

    public Rectangle getRight() {
        return right;
    }

    public void setRight(Rectangle right) {
        this.right = right;
    }
}
