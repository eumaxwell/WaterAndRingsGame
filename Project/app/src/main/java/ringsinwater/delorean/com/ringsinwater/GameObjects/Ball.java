package ringsinwater.delorean.com.ringsinwater.GameObjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

import ringsinwater.delorean.com.ringsinwater.Game;
import ringsinwater.delorean.com.ringsinwater.GameEngine.GamePanel;

/**
 * Created by Max on 31/05/2015.
 */
public class Ball extends GameObject {

    public int speedX;
    public int speedY;
    public int ballRadius;
    private int maximumX;
    private int maximumY;

    public Ball(int xPos, int yPos, int rad)
    {
        Random rand = new Random();
        speedX = rand.nextInt(40)+2;
        speedY = rand.nextInt(40)+2;
        x = xPos;
        y = yPos;
        ballRadius = rad;

        maximumX = GamePanel.WIDTH-ballRadius;
        maximumY = GamePanel.HEIGHT-ballRadius;

    }

    public void update()
    {
        x += speedX;
        y += speedY;

        speedY++;

        //if (speedY > 0)
        //    speedY--;
        //else
        if (speedY < 0)
            speedY++;
        if (speedX > 0)
            speedX--;
        else if (speedX < 0)
            speedX++;

        if (x >= maximumX && speedX > 0){
            speedX = -speedX;
        }
        if (x <= ballRadius && speedX < 0){
            speedX = -speedX;
        }
        if (y >= maximumY && speedY > 0){
            speedY = -speedY;
        }
        if (y <= ballRadius && speedY < 0){
            speedY = -speedY;
        }

        if (y < 0) {
            y = 1;
        }
        if (y > maximumY) {
            y = maximumY-5;
        }
        if (x < ballRadius) {
            x = ballRadius+5;
        }
        if (x >= maximumX) {
            x = maximumX-5;
        }
    }

    public void draw(Canvas canvas)
    {
        canvas.drawCircle(x,y,ballRadius,new Paint(Color.GRAY));
    }

    public void up() {
        speedX+= speedX/2;
        speedY-= 20;
    }

    public void changeX(float x) {
        speedX+=x;
    }

    //public void detectCollisions()
    //{
    //    //this.transform.colorTransform = new ColorTransform(1, 1, 1, 1, 0, 0, 0, 0);
    //    for (int n = 0; n < Game.balls.length; n++) {
    //        if (Game.balls[n] != this) {
    //            if (x + ballRadius + Game.balls[n].ballRadius > Game.balls[n].x
    //                    && x < Game.balls[n].x + ballRadius + Game.balls[n].ballRadius
    //                    && y + ballRadius + Game.balls[n].ballRadius > Game.balls[n].y
    //                    && y < Game.balls[n].y + ballRadius + Game.balls[n].ballRadius) {
    //                if (distanceTo(this, Game.balls[n]) < ballRadius + Game.balls[n].ballRadius) {
    //                    //this.transform.colorTransform = new ColorTransform(0, 0, 0, 1, 0, 255, 0, 0);
    //                    drawCollision(this, Game.balls[n]);
    //                    calculateNewVelocities(this, Game.balls[n]);
    //                }
    //            }
    //        }
    //    }
    //}



/*
        public function drawCollision(firstBall:Ball, secondBall:Ball):void {
            var x1:Number = firstBall.x;
            var x2:Number = secondBall.x;
            var y1:Number = firstBall.y;
            var y2:Number = secondBall.y;
            var r1:Number = firstBall.ballRadius;
            var r2:Number = secondBall.ballRadius;

            var collisionPoint:Point = new Point( (x1 * r2 + x2 * r1) / (r1 + r2), (y1 * r2 + y2 * r1) / (r1 + r2) );

            Game.collisionLayer.graphics.beginFill(0x0000FF);
            Game.collisionLayer.graphics.drawCircle(collisionPoint.x, collisionPoint.y, 5);
            Game.collisionLayer.graphics.endFill();
        }
*/



}
