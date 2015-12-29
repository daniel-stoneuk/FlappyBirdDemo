package com.danielstone.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;

import java.util.Random;


public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
    TextureRegion bird1;
    TextureRegion bird2;
    Texture topTube;
    Texture bottomTube;

    Circle birdCircle;

    ShapeRenderer shapeRenderer;

    Random randomGenerator;

    Animation wingsAnimation;
    TextureRegion currentFrame;

    float stateTime;
    float birdY = 0;
    float birdVelocity = 0;
    float birdJump = 39;
    float gravity = 2;
    float gap = 490;
    float maxTubeOffset;
    float tubeVelocity = 5;
    int numberOfTubes = 4;
    float[] tubeX = new float[numberOfTubes];
    float[] tubeOffset = new float[numberOfTubes];
    float distanceBetweenTubes;

    int gameState = 0;

	
	@Override
	public void create () {
        batch = new SpriteBatch();
        background = new Texture("bg.png");
        bird1 = new TextureRegion(new Texture("bird.png"));
        bird2 = new TextureRegion(new Texture("bird2.png"));
        wingsAnimation = new Animation(0.1f, bird1, bird2);
        stateTime = 0f;
        birdY = (Gdx.graphics.getHeight() / 2) - (bird1.getRegionHeight() / 2);
        randomGenerator = new Random();
        topTube = new Texture("toptube.png");
        bottomTube = new Texture("bottomtube.png");
        maxTubeOffset = (Gdx.graphics.getHeight() / 2) - (gap / 2) - 100;
        distanceBetweenTubes = Gdx.graphics.getWidth() / 2;

        shapeRenderer = new ShapeRenderer();
        birdCircle = new Circle();

        for (int i = 0; i < numberOfTubes; i++) {

            tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);
            tubeX[i] = Gdx.graphics.getWidth() / 2 - topTube.getWidth() / 2 + i * distanceBetweenTubes;
        }

    }

        @Override
        public void render() {

            batch.begin();
            batch.draw(background, 0f, 0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

            if (gameState != 0) {



                if (Gdx.input.justTouched()) {

                    birdVelocity = - birdJump;

                }

                for (int i = 0; i < numberOfTubes; i++) {

                    if (tubeX[i] < - topTube.getWidth()) {
                        tubeX[i] = tubeX[i] + numberOfTubes * distanceBetweenTubes;
                        tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);
                    } else {
                        tubeX[i] = tubeX[i] - tubeVelocity;

                        batch.draw(topTube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]);

                        batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i]);
                    }
                }


                if (birdY > (bird1.getRegionHeight() / 2) || birdVelocity < 0) {

                    birdVelocity = birdVelocity + gravity;
                    birdY -= birdVelocity;

                }


            } else if (Gdx.input.justTouched()) {

                gameState = 1;

            }

            stateTime += Gdx.graphics.getDeltaTime();
            currentFrame = wingsAnimation.getKeyFrame(stateTime, true);

            batch.draw(currentFrame, (Gdx.graphics.getWidth() / 2) - (bird1.getRegionWidth() / 2), birdY);
            batch.end();

            birdCircle.set(Gdx.graphics.getWidth() / 2, birdY + bird1.getRegionHeight() / 2, bird1.getRegionWidth() / 2);

            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.circle(birdCircle.x, birdCircle.y, birdCircle.radius);
            shapeRenderer.end();
        }

}
