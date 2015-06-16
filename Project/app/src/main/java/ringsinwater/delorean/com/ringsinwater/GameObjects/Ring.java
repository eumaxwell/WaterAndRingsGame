package ringsinwater.delorean.com.ringsinwater.GameObjects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

import ringsinwater.delorean.com.ringsinwater.GameEngine.GamePanel;

/**
 * Created by Max on 19/05/2015.
 */
public class Ring extends GameObject {

    private boolean up;
    private int radius = 30;
    private Paint paint = new Paint(Color.BLUE);

    public Ring(int posX, int posY) {
        Random r = new Random();
        x = posX;//r.nextInt();
        y = posY;//r.nextInt();
        dy = 0;
        dx = 0;
    }

    public void setUp(boolean b){
        if(b){
            dy -=5;
        }
    }
    public void update()
    {
        if(dy>15)dy = 15;
        if(dy<-15)dy = -15;

        y += dy;
        dy++;
        if (y < 0) {
            y = 1;
            dx=dy;
            dy=0;
        }
        if (y > GamePanel.HEIGHT-radius) {
            y = GamePanel.HEIGHT - radius;
        }

        if(dx>5)dx = 5;
        if(dx<-5)dx = -5;

        x += dx;
        if (x+radius < radius) {
            x = radius;
            dy=dx;
            dx=0;
        }
        if (x > GamePanel.WIDTH-radius) {
            x = GamePanel.WIDTH - radius;
        }

    }

    public void changeX(float movX)    {
        dx +=movX;
    }
    public void draw(Canvas canvas)
    {
        canvas.drawCircle(x,y,radius, paint);
    }

    public void setX(int x)
    {
        this.x = x;
        if (this.x < radius) {
            this.x = radius;
        }
        if (this.x > GamePanel.HEIGHT-radius) {
            this.x = GamePanel.HEIGHT-radius;
        }
    }

    public int getRadius(){return radius;}
    public int getDx(){return dx;}
    public int getDy(){return dy;}
    public void invertDx(){dx*=-1;}
    public void invertDy(){dy*=-1;}

    public boolean colliding(Ring ball)
    {
        float xd = getX() - ball.getX();
        float yd = getY() - ball.getY();

        float sumRadius = getRadius() + ball.getRadius();
        float sqrRadius = sumRadius * sumRadius;

        float distSqr = (xd * xd) + (yd * yd);

        if (distSqr <= sqrRadius)
        {
            return true;
        }

        return false;
    }


}
