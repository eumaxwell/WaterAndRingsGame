package ringsinwater.delorean.com.ringsinwater.GameObjects;

/**
 * Created by Max on 16/05/2015.
 */
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import ringsinwater.delorean.com.ringsinwater.GameEngine.GamePanel;

public class Background {

    private Bitmap image;
    private int x, y, dx;

    public Background(Bitmap res)
    {
        image = res;
    }
    public void update()
    {
        x+=dx;
        if(x<-GamePanel.WIDTH){
            x=0;
        }
    }
    public void draw(Canvas canvas, Paint paint)
    {
        canvas.drawBitmap(image, x, y,null);
        if(x<0)
        {
            canvas.drawBitmap(image, x+GamePanel.WIDTH, y, paint);
        }
    }
    public void setVector(int dx)
    {
        this.dx = dx;
    }
}