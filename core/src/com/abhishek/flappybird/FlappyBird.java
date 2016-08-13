package com.abhishek.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import org.w3c.dom.Text;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
	//sprite is basically an image
    //spritebatch managing animation
    SpriteBatch batch;
	Texture background;
    //texture is basically image
	//runs when created

    BitmapFont font;

    int score=0;
    int scoringtube =0;
    Texture pipe1;
    Texture pipe2;
    Texture gameover;

    Circle birdCircle ;
    Rectangle[] topRectangle;
    Rectangle[] bottomRectangle;

    Animation animation;

    Texture[] birds;
    int flapstate =0;//which bird to display

  //  ShapeRenderer shapeRenderer ;
    //just like batches
    //collision detection cant be done on batches but can br done on shapes
    //using same shape renderer for circle and rectangle

    float elapsedTime;

    float birdY=0;
    float velocity =0;
    int gamestate =0;
    float gravity=2;


    float gap=400;
    float maxTubeOffset;

    Random randomGenerator;

    int numberOfTubes=4;
    float tubeVelocity =4;
    float[] tubeX = new float[numberOfTubes];
    float[] tubeoffset=new float[numberOfTubes];

    float distanceBetweenTubes;


    @Override
	public void create () {
        batch = new SpriteBatch();
        background = new Texture("bg.png");
        birds = new Texture[2];

        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(10);
        birdCircle =new Circle();
    //    shapeRenderer = new ShapeRenderer();
        birds[0] = new Texture("bird.png");
        birds[1] = new Texture("bird2.png");
        gameover = new Texture("gameoverr.png");
        pipe1 = new Texture("toptube.png");
        pipe2 = new Texture("bottomtube.png");
        animation = new Animation(1 / 30f, new TextureRegion(new Texture("bird.png")), new TextureRegion(new Texture("bird2.png")));


        topRectangle=new Rectangle[numberOfTubes];
        bottomRectangle =new Rectangle[numberOfTubes];

        maxTubeOffset = Gdx.graphics.getHeight() / 2 - gap / 2 - 100;
        randomGenerator = new Random();
        distanceBetweenTubes = Gdx.graphics.getWidth() *3/4;

        startGame();

	}

    public  void startGame()
    {
        birdY = Gdx.graphics.getHeight() / 2 - birds[0].getHeight() / 2;
        for (int i = 0; i < numberOfTubes; i++)
        {
            topRectangle[i] =new Rectangle();
            bottomRectangle[i] = new Rectangle();
            tubeX[i]=(Gdx.graphics.getWidth())/2-pipe2.getWidth()/2 + Gdx.graphics.getWidth()+i*distanceBetweenTubes;
        }

    }



    //happens continously as app processed
	@Override
	public void render () {
        elapsedTime += Gdx.graphics.getDeltaTime();

        float a = (Gdx.graphics.getWidth()) / 2;
        float b = (Gdx.graphics.getHeight()) / 2;

        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        //start the background from 0,0 co-ordinates , and get widght  from the gdx lib
        //as we want wallpaper to take full widgth and height

//batach drawn last appears on the top
        if (gamestate == 1)
        {
            //checking if less than center of screen for score
            if(tubeX[scoringtube]<Gdx.graphics.getWidth()/2)
            {
                score++;
                Gdx.app.log("SCORE", String.valueOf(score));
                if(scoringtube < numberOfTubes-1)
                {
                    scoringtube++;
                }
                else
                {
                    scoringtube=0;
                }
            }

            if(Gdx.input.justTouched())
            {
                velocity=-30;//shooting up
            }

            for (int i = 0; i < numberOfTubes; i++) {
                if(tubeX[i] < -pipe1.getWidth())
                {
                    tubeX[i] +=numberOfTubes*distanceBetweenTubes;
                    tubeoffset[i] = (randomGenerator.nextFloat()-0.5f)*(Gdx.graphics.getHeight()-gap-200);//creates a number between 0 and 1; and 200 due to .5 but actuallu move 100
                }else
                {
                    tubeX[i] -= tubeVelocity;

                }

                batch.draw(pipe2, tubeX[i], b - gap / 2 - pipe2.getHeight() + tubeoffset[i]);
                batch.draw(pipe1, tubeX[i], b + gap / 2 + tubeoffset[i]);

                topRectangle[i] = new Rectangle(tubeX[i], b + gap / 2 + tubeoffset[i],pipe1.getWidth(),pipe1.getHeight());
                bottomRectangle[i] =new Rectangle(tubeX[i], b - gap / 2 - pipe2.getHeight() + tubeoffset[i],pipe2.getWidth(),pipe2.getHeight());
            }
            if(birdY>0)
            {
                velocity = velocity + gravity;
                birdY -= velocity;//as it attains more height it falls with more speed
            }
            else
            {
                gamestate=2;
            }

//            if(birdY<=0)
//            {
//                birdY=0;//JUST FOR TESTINGGGGGGGGGGGGGGGG
//            }
            //Gdx.app.log("HEIGHT", String.valueOf(birdY));

        }
        else
        if(gamestate==0)
        {
            {
                if (Gdx.input.justTouched()) {
                    //Gdx.app.log("TOUCh","TOUCHHHHH");
                    gamestate = 1;
                }
            }
        }else
            if(gamestate==2)
            {
                //center point - starting point of the game
                batch.draw(gameover,Gdx.graphics.getWidth()/2-gameover.getWidth()/2,Gdx.graphics.getHeight()/2-gameover.getHeight()/2);
                if (Gdx.input.justTouched()) {
                    //Gdx.app.log("TOUCh","TOUCHHHHH");
                    gamestate = 1;
                    startGame();
                    score=0;
                    scoringtube=0;//checking for the first one
                    velocity=0;



                }

        }
        //        if(flapstate==0)
        //        {flapstate=1;}
        //        else
        //        {flapstate=0;}
        //        batch.draw(birds[flapstate],a-birds[flapstate].getWidth()/2,b-birds[flapstate].getHeight()/2,birds[flapstate].getWidth(),birds[flapstate].getHeight());
        //        //co-ordinates that we giwe are for bottom left of the spirite not the center

        batch.draw(animation.getKeyFrame(elapsedTime, true), a - birds[flapstate].getWidth() / 2, birdY, birds[flapstate].getWidth(), birds[flapstate].getHeight());

        font.draw(batch,String.valueOf(score),100,200);//displaying score on screen
        batch.end();
        birdCircle.set(Gdx.graphics.getWidth()/2,birdY+birds[0].getHeight()/2,birds[0].getWidth()/2);//last one is the radius

        //shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        //shapeRenderer.setColor(Color.RED);
        //shapeRenderer.circle(birdCircle.x,birdCircle.y,birdCircle.radius);
        for(int i=0;i<numberOfTubes;i++)
        {
          //  shapeRenderer.rect(tubeX[i], b + gap / 2 + tubeoffset[i],pipe1.getWidth(),pipe1.getHeight());
            //shapeRenderer.rect(tubeX[i], b - gap / 2 - pipe2.getHeight() + tubeoffset[i],pipe2.getWidth(),pipe2.getHeight());

            //collision
            if(Intersector.overlaps(birdCircle,topRectangle[i]) || Intersector.overlaps(birdCircle,bottomRectangle[i]))
            {
                //Gdx.app.log("BLAA","OUTTTTTTT");
                gamestate=2;

            }

        }
//        shapeRenderer.end();
    }
	@Override
	public void dispose () {
		batch.dispose();
        background.dispose();
	}
}
