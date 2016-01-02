package com.danielstone.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;


public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
    TextureRegion bird1;
    TextureRegion bird2;
    Texture topTube;
    Texture bottomTube;

    int screenWidth;
    int screenHeight;

    int gameScore;
    int scoringTube = 0;

    float deathTime;

    float currentSecond = 0;


    BitmapFont font;
    //ShapeRenderer shapeRenderer;

    Random randomGenerator;

    Animation wingsAnimation;
    TextureRegion currentFrame;

    float stateTime;
    float birdY = 0;
    float birdVelocity = 0;
    float birdJump = 39;
    float gravity = 2;
    float gap = 600;
    float maxTubeOffset;
    float tubeVelocity = 5;
    int numberOfTubes = 4;
    float[] tubeX = new float[numberOfTubes];
    float[] tubeOffset = new float[numberOfTubes];
    float distanceBetweenTubes;

    int gameState = 0;


    Circle birdCircle;
    Rectangle[] topTubeRectangles;
    Rectangle[] bottomTubeRectangles;


    @Override
	public void create () {
        batch = new SpriteBatch();
        background = new Texture("bg.png");

        font = new BitmapFont(Gdx.files.internal("flappybirdfont.fnt"));
        font.setColor(Color.WHITE);
        font.getData().setScale(3);

        bird1 = new TextureRegion(new Texture("bird.png"));
        bird2 = new TextureRegion(new Texture("bird2.png"));
        wingsAnimation = new Animation(0.1f, bird1, bird2);
        stateTime = 0f;
        randomGenerator = new Random();
        topTube = new Texture("toptube.png");
        bottomTube = new Texture("bottomtube.png");
        maxTubeOffset = (Gdx.graphics.getHeight() / 2) - (gap / 2) - 100;
        distanceBetweenTubes = Gdx.graphics.getWidth() / 2;

        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();

        topTubeRectangles = new Rectangle[numberOfTubes];
        bottomTubeRectangles = new Rectangle[numberOfTubes];

        birdCircle = new Circle();


        gameSetup();


    }

    public void gameSetup() {
        birdY = (Gdx.graphics.getHeight() / 2) - (bird1.getRegionHeight() / 2);

        for (int i = 0; i < numberOfTubes; i++) {

            tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (screenHeight - gap - 200);
            tubeX[i] = screenWidth + topTube.getWidth() + i * distanceBetweenTubes;

            topTubeRectangles[i] = new Rectangle();
            bottomTubeRectangles[i] = new Rectangle();
        }
    }

        @Override
        public void render() {

            batch.begin();
            batch.draw(background, 0f, 0f, screenWidth, screenHeight);

            if (gameState == 1) {



                if (Gdx.input.justTouched()) {

                    birdVelocity = - birdJump;

                }

                for (int i = 0; i < numberOfTubes; i++) {

                    if (tubeX[i] < - topTube.getWidth()) {
                        tubeX[i] = tubeX[i] + numberOfTubes * distanceBetweenTubes;
                        tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (screenHeight - gap - 200);
                    } else {
                        tubeX[i] = tubeX[i] - tubeVelocity;
                        }

                    if (tubeX[i] < screenWidth && tubeX[i] > 0 - topTube.getWidth()) {
                        topTubeRectangles[i].set(tubeX[i] + 5f, screenHeight / 2 + gap / 2 + tubeOffset[i], topTube.getWidth() - 5f, topTube.getHeight());
                        bottomTubeRectangles[i].set(tubeX[i] + 5f, screenHeight / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i], topTube.getWidth() - 5f, topTube.getHeight());
                    }
                }


                if (tubeX[scoringTube] + topTube.getWidth() / 2 < screenWidth / 2) {
                    gameScore += 1;

                    Gdx.app.log("Game Score", "" + gameScore);

                    if (scoringTube < numberOfTubes - 1) {
                        scoringTube++;
                    } else {
                        scoringTube = 0;
                    }
                }

                if (birdY > (bird1.getRegionHeight() / 2)) {

                    birdVelocity = birdVelocity + gravity;

                    if (birdY < screenHeight - (bird1.getRegionHeight()) || birdVelocity > 0) {
                        birdY -= birdVelocity;
                    };

                } else {
                    gameState = 2;
                    deathTime = stateTime;
                }


            } else if (gameState == 0) {

                if (Gdx.input.justTouched()) {

                    gameState = 1;
                    gameScore = 0;

                }
            } else if (gameState == 2) {


                if (birdY > (bird1.getRegionHeight() / 2)) {

                    birdVelocity = birdVelocity + gravity;
                    birdY -= birdVelocity;

                }

                if (Gdx.input.justTouched() && (stateTime - 0.5f) > deathTime) {

                    gameState = 1;
                    gameScore = 0;
                    scoringTube = 0;
                    birdVelocity = 0;
                    gameSetup();

                }

            }

            stateTime += Gdx.graphics.getDeltaTime();
            currentFrame = wingsAnimation.getKeyFrame(stateTime, true);

            for (int i = 0; i < numberOfTubes; i++) {
                if (tubeX[i] < screenWidth && tubeX[i] > 0 - topTube.getWidth()) {
                    batch.draw(topTube, tubeX[i], screenHeight / 2 + gap / 2 + tubeOffset[i]);

                    batch.draw(bottomTube, tubeX[i], screenHeight / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i]);
                }
            }

            if (gameState == 2) {
                font.draw(batch, "Game Over...", 100, screenHeight / 2 + ((font.getLineHeight() * 2) / 2), screenWidth - 100, 1, true);
            }

            batch.draw(currentFrame, (screenWidth / 2) - (bird1.getRegionWidth() / 2), birdY);

            font.draw(batch, String.valueOf(gameScore), 0, screenHeight - 50, screenWidth, 1, false);


            birdCircle.set(screenWidth / 2, birdY + bird1.getRegionHeight() / 2, bird1.getRegionWidth() / 2);



            /*
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.BLACK);
            shapeRenderer.circle(birdCircle.x, birdCircle.y, birdCircle.radius);

            for (int i = 0; i < numberOfTubes; i++) {
                shapeRenderer.rect(topTubeRectangles[i].x, topTubeRectangles[i].y, topTubeRectangles[i].width, topTubeRectangles[i].height);
                shapeRenderer.rect(bottomTubeRectangles[i].x, bottomTubeRectangles[i].y, bottomTubeRectangles[i].width, bottomTubeRectangles[i].height);
            }

            shapeRenderer.end();

            */

                for (int i = 0; i < numberOfTubes; i++) {
                    if (Intersector.overlaps(birdCircle, topTubeRectangles[i]) || Intersector.overlaps(birdCircle, bottomTubeRectangles[i])) {
                        if (gameState != 2) {
                            deathTime = stateTime;
                            birdVelocity = 0;
                        }
                        gameState = 2;
                    }


                }

            if (stateTime > currentSecond) {
                currentSecond ++;
                Gdx.app.log("Framerate", ""+ Gdx.graphics.getFramesPerSecond());
            }

            batch.end();


        }

}
