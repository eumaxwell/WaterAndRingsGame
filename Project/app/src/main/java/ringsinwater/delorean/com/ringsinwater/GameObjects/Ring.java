package ringsinwater.delorean.com.ringsinwater.GameObjects;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

import ringsinwater.delorean.com.ringsinwater.GameEngine.GamePanel;

/**
 * Created by Max on 19/05/2015.
 */
public class Ring extends GameObject {
    private Bitmap bmpRing;
    private boolean up;

    public Ring(Bitmap res) {

        bmpRing = res;
        Random r = new Random();
        x = 100;//r.nextInt();
        y = 100;//r.nextInt();
        dy = 0;
    }

    public void setUp(boolean b){up = b;}
    public void update()
    {
        if(up){
            dy -=2;
        }
        else{
            dy +=2;
        }

        if(dy>20)dy = 20;
        if(dy<-20)dy = -20;

        y += dy*2;
        if (y < 0)
            y = 1;
        if (y > GamePanel.HEIGHT-50)
            y = GamePanel.HEIGHT-51;
    }

    public void changeX(float x)    {

        this.x += x;
        if (this.x < 0)
            this.x = 1;
        if (this.x > GamePanel.WIDTH-50)
            this.x = GamePanel.WIDTH-51;

    }
    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(bmpRing,x,y,null);
    }
}
