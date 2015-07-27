package ringsinwater.delorean.com.ringsinwater.GameEngine;

/**
 * Created by Max on 16/05/2015.
 */
import android.app.ActionBar;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Random;

import ringsinwater.delorean.com.ringsinwater.GameObjects.Background;
import ringsinwater.delorean.com.ringsinwater.GameObjects.Ball;
import ringsinwater.delorean.com.ringsinwater.GameObjects.Explosion;
import ringsinwater.delorean.com.ringsinwater.GameObjects.Player;
import ringsinwater.delorean.com.ringsinwater.R;


public class GamePanel extends SurfaceView implements SurfaceHolder.Callback, SensorEventListener
{
    public static int WIDTH ;
    public static int HEIGHT;

    private float alcanceXBotao;
    private float alcanceYBotao;
    private float alcanceXBotaoB;

    public static final int MOVESPEED = -5;

    private MainThread thread;
    private Background bg;
    private Player player;

    private ArrayList<Ball> balls;

    private Random rand = new Random();

    private boolean topDown = true;
    private boolean botDown = true;
    private boolean newGameCreated;

    //increase to slow down difficulty progression, decrease to speed up difficulty progression
    private int progressDenom = 20;

    private Explosion explosion;
    private long startReset;
    private boolean reset;
    private boolean dissapear;
    private boolean started;
    private int best;
    private long lastUpdate;

    private SensorManager senSensorManager;
    private Sensor senAccelerometer;

    private Paint paint;

    private Button buttonA;

    public GamePanel(Context context)
    {

        super(context);

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

        WIDTH = displayMetrics.widthPixels;
        HEIGHT = displayMetrics.heightPixels;

        //add the callback to the surfaceholder to intercept events
        getHolder().addCallback(this);

        //make gamePanel focusable so it can handle events
        setFocusable(true);

        paint = new Paint();

        // as dimensões de um quadrado
        alcanceXBotao = (WIDTH / 3); // 2/3 do tamanho de X
        alcanceYBotao = HEIGHT - (HEIGHT / 3)*2; // 1/3 do tamanho de Y
        alcanceXBotaoB = WIDTH - alcanceXBotao;

        senSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        //super.onPause();
        senSensorManager.unregisterListener(this);
    }

    protected void onResume() {
        //super.onResume();
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if (balls != null) {
            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 100) {
                //long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;
                Sensor mySensor = sensorEvent.sensor;

                if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {

                    float x = sensorEvent.values[0];
                    //float y = sensorEvent.values[1];
                    //float z = sensorEvent.values[2];
                    for(Ball r : balls) {
                        r.changeX(-(x*2));
                    }
                }
            }
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){}

    @Override
    public void surfaceDestroyed(SurfaceHolder holder){
        boolean retry = true;
        int counter = 0;
        while(retry && counter<1000)
        {
            counter++;
            try{thread.setRunning(false);
                thread.join();
                retry = false;
                thread = null;

            }catch(InterruptedException e){e.printStackTrace();}

        }

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder){

        bg = new Background(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getResources(), R.drawable.fundo), WIDTH, HEIGHT, true));
        player = new Player(BitmapFactory.decodeResource(getResources(), R.drawable.helicopter), 65, 25, 3);

        balls = new ArrayList<Ball>();
        for (int i =0; i < 5; i++){
            Ball ball = new Ball(rand.nextInt(WIDTH),rand.nextInt(HEIGHT),30);
            balls.add(ball);
        }

        buttonA = new Button(getContext());
        buttonA.setText("TestButton");
        buttonA.setClickable(true);
        buttonA.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                //Log.i(TAG, "Button Pressed!");
            }
        });

        ActionBar.LayoutParams layoutButton = new ActionBar.LayoutParams(10,10);
        buttonA.setLayoutParams(layoutButton);

        thread = new MainThread(getHolder(), this);
        //we can safely start the game loop
        thread.setRunning(true);
        thread.start();

    }
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if(!player.getPlaying() && newGameCreated && reset)
        {
            player.setPlaying(true);
            player.setUp(true);

        }
        if(player.getPlaying()) {
            float x = event.getX();
            float y = event.getY();

            if (x < alcanceXBotao)
            {
                if (y > alcanceYBotao) {
                    for (Ball b : balls) {
                        if (b.getX() < alcanceXBotao && b.getY() > alcanceYBotao) {
                            b.up();
                        }
                    }
                }
            }
            else if (x > alcanceXBotaoB)
            {
                if (y > alcanceYBotao) {
                    for (Ball b : balls) {
                        if (b.getX() > alcanceXBotaoB && b.getY() > alcanceYBotao) {
                            b.up();
                        }
                    }
                }
            }
        }
/*
        if(event.getAction()==MotionEvent.ACTION_DOWN){
            if(!player.getPlaying() && newGameCreated && reset)
            {
                player.setPlaying(true);
                player.setUp(true);

            }
            if(player.getPlaying())
            {
                if(!started)started = true;
                reset = false;
                player.setUp(true);

                for(Ball b : balls) {
                    b.up();
                }
            }
            return true;
        }
        if(event.getAction()==MotionEvent.ACTION_UP)
        {
            player.setUp(false);
            for(Ring r : rings) {
                r.setUp(false);
            }
            return true;
        }
*/
        return super.onTouchEvent(event);
    }

    public void update()
    {
        if(player.getPlaying()) {

            bg.update();
            player.update();

            for (Ball b:balls)
            {
                b.update();
            }
            detectCollisions();

        }
        else{
            player.resetDY();
            if(!reset)
            {
                newGameCreated = false;
                startReset = System.nanoTime();
                reset = true;
                dissapear = true;
                explosion = new Explosion(BitmapFactory.decodeResource(getResources(),R.drawable.explosion),player.getX(),
                        player.getY()-30, 100, 100, 25);
            }

            explosion.update();
            long resetElapsed = (System.nanoTime()-startReset)/1000000;

            if(resetElapsed > 2500 && !newGameCreated)
            {
                newGame();
            }
        }
    }

    @Override
    public void draw(Canvas canvas)
    {
        final float scaleFactorX = getWidth()/(WIDTH*1.f);
        final float scaleFactorY = getHeight()/(HEIGHT*1.f);

        if(canvas!=null) {
            final int savedState = canvas.save();
            canvas.scale(scaleFactorX, scaleFactorY);
            bg.draw(canvas, paint);
            if(!dissapear) {
                player.draw(canvas);
            }

            for(Ball b: balls)
            {
                b.draw(canvas, paint);
            }

            //draw explosion
            if(started)
            {
                explosion.draw(canvas);
            }

            buttonA.draw(canvas);

            drawText(canvas);
            canvas.restoreToCount(savedState);

        }
    }

    public void newGame()
    {
        dissapear = false;

        player.resetDY();
        player.resetScore();
        player.setY(HEIGHT/2);

        if(player.getScore()>best)
        {
            best = player.getScore();

        }

        newGameCreated = true;
    }
    public void drawText(Canvas canvas)
    {
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(30);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("DISTANCE: " + (player.getScore()*3), 10, HEIGHT - 10, paint);
        canvas.drawText("BEST: " + best, WIDTH - 215, HEIGHT - 10, paint);

        if(!player.getPlaying()&&newGameCreated&&reset)
        {
            Paint paint1 = new Paint();
            paint1.setTextSize(40);
            paint1.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            canvas.drawText("PRESS TO START", WIDTH/2-50, HEIGHT/2, paint1);

            paint1.setTextSize(20);
            canvas.drawText("PRESS AND HOLD TO GO UP", WIDTH/2-50, HEIGHT/2 + 20, paint1);
            canvas.drawText("RELEASE TO GO DOWN", WIDTH/2-50, HEIGHT/2 + 40, paint1);
        }
    }

    public void detectCollisions()
    {
        //this.transform.colorTransform = new ColorTransform(1, 1, 1, 1, 0, 0, 0, 0);
        for (int i = 0; i < balls.size(); i++) {
            Ball ballA = balls.get(i);
            for (int j = i+1; j < balls.size(); j++) {
                Ball ballB = balls.get(j);
                if (ballA.getX() + ballA.ballRadius + ballB.ballRadius > ballB.getX()
                        && ballA.getX() < ballB.getX() + ballA.ballRadius + ballB.ballRadius
                        && ballA.getY() + ballA.ballRadius + ballB.ballRadius > ballB.getY()
                        && ballA.getY() < ballB.getY() + ballA.ballRadius + ballB.ballRadius) {
                    if (distanceTo(ballA, ballB) < ballA.ballRadius + ballB.ballRadius) {
                        //this.transform.colorTransform = new ColorTransform(0, 0, 0, 1, 0, 255, 0, 0);
                        //drawCollision(ballA, ballB);
                        calculateNewVelocities(ballA, ballB);
                    }
                }
            }
        }
    }

    public double distanceTo(Ball a, Ball b)
    {
        double distance = Math.sqrt(((a.getX() - b.getX()) * (a.getX() - b.getX())) + ((a.getY() - b.getY()) * (a.getY() - b.getY())));
        if (distance < 0)
        {
            distance = distance * -1;
        }
        return distance;
    }

    public void calculateNewVelocities( Ball firstBall, Ball secondBall)
    {
        int mass1 = firstBall.ballRadius;
        int mass2 = secondBall.ballRadius;
        int velX1 = firstBall.speedX;
        int velX2 = secondBall.speedX;
        int velY1 = firstBall.speedY;
        int velY2 = secondBall.speedY;

        int newVelX1 = (velX1 * (mass1 - mass2) + (2 * mass2 * velX2)) / (mass1 + mass2);
        int newVelX2 = (velX2 * (mass2 - mass1) + (2 * mass1 * velX1)) / (mass1 + mass2);
        int newVelY1 = (velY1 * (mass1 - mass2) + (2 * mass2 * velY2)) / (mass1 + mass2);
        int newVelY2 = (velY2 * (mass2 - mass1) + (2 * mass1 * velY1)) / (mass1 + mass2);
        //trace (velX1 * (mass1 - mass2) );
        //trace (2 * mass2 * velX2);
        //trace(newVelX1);

        firstBall.speedX = newVelX1;
        secondBall.speedX = newVelX2;
        firstBall.speedY = newVelY1;
        secondBall.speedY = newVelY2;

        firstBall.setX(firstBall.getX() + newVelX1);
        firstBall.setY(firstBall.getY() + newVelY1);
        secondBall.setX(secondBall.getX() + newVelX2);
        secondBall.setY(secondBall.getY() + newVelY2);
    }

}