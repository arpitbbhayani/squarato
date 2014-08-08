package com.devilo.chickchick;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;
import java.util.LinkedList;

public class ChickChick implements ApplicationListener {
	private OrthographicCamera camera;
    private SpriteBatch batch;

    Texture[] cloudImages = new Texture[2];

    LinkedList<Rectangle> cloudRectangles;

    long lastCloudSpawnTime;

    Vector3 touchPos;

    boolean isTouched = false;

    float oldX, oldY, newX, newY, touchX, touchY, currentTouchX, currentTouchY;


    /* Square : Sprite animation Variables */

    private static final String SQUARE_SPRITESHEET_NAME = "data/square_spritesheet.png";

    private static final int SQUARE_WIDTH = 32;
    private static final int SQUARE_HEIGHT = 38;

    Animation squareWalkAnimation;
    Texture squareWalkSheet;
    TextureRegion[] squareWalkFrames;
    SpriteBatch squareSpriteBatch;
    TextureRegion squareCurrentFrame;

    float squareStateTime;

    /* Circle : Sprite animation Variables */

    private static final String CIRCLE_SPRITESHEET_NAME = "data/8circle_spritesheet.png";

    private static final int CIRCLE_WIDTH = 256;    // 32 * 8
    private static final int CIRCLE_HEIGHT = 33;

    Animation circleWalkAnimation;
    Texture circleWalkSheet;
    TextureRegion[] circleWalkFrames;
    SpriteBatch circleSpriteBatch;
    TextureRegion circleCurrentFrame;

    float circleStateTime;


    /* Circle Events Variables */

    LinkedList<CircleLine> circleRectangles;
    long lastCircleSpawnTime;


    boolean isPlayOn = true;

    Rectangle squareRectangle;


	@Override
	public void create() {		
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

        camera = new OrthographicCamera(1, h/w);
		batch = new SpriteBatch();

        /* Loading images */
        cloudImages[0] = new Texture(Gdx.files.internal("data/cloud1_125.png"));
        cloudImages[1] = new Texture(Gdx.files.internal("data/cloud2_125.png"));

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 320, 480);

        circleWalkSheet = new Texture(Gdx.files.internal(CIRCLE_SPRITESHEET_NAME));
        TextureRegion[][] tmp = TextureRegion.split(circleWalkSheet, CIRCLE_WIDTH , CIRCLE_HEIGHT);
        circleWalkFrames = new TextureRegion[3];
        int index = 0;
        for (int j = 0; j < 3; j++) {
                circleWalkFrames[index++] = tmp[0][j];
        }

        circleWalkAnimation = new Animation(0.50f, circleWalkFrames);
        circleSpriteBatch = new SpriteBatch();
        circleStateTime = 0f;

        squareWalkSheet = new Texture(Gdx.files.internal(SQUARE_SPRITESHEET_NAME));
        tmp = TextureRegion.split(squareWalkSheet, SQUARE_WIDTH, SQUARE_HEIGHT);
        squareWalkFrames = new TextureRegion[3];
        index = 0;
        for (int j = 0; j < 3; j++) {
            squareWalkFrames[index++] = tmp[0][j];
        }

        squareWalkAnimation = new Animation(0.25f, squareWalkFrames);
        squareSpriteBatch = new SpriteBatch();
        squareStateTime = 0f;



        cloudRectangles = new LinkedList<Rectangle>();
        touchPos = new Vector3();

        spawnCloud();


        circleRectangles = new LinkedList<CircleLine>();
        spawnCircle();

        squareRectangle = new Rectangle();
        squareRectangle.x = (320-32)/2;
        squareRectangle.y = (480-38)/2 + 150;
        squareRectangle.width = 32;
        squareRectangle.height = 38;

    }

    private void spawnCircle() {

        int circlex1 = MathUtils.random(-32*4, 0);
        int circlex2 = circlex1 + 256 + 64;

        Rectangle circleRectangle1 = new Rectangle();
        circleRectangle1.x = circlex1;
        circleRectangle1.y = -33;
        circleRectangle1.width = 256;
        circleRectangle1.height = 33;

        Rectangle circleRectangle2 = new Rectangle();
        circleRectangle2.x = circlex2;
        circleRectangle2.y = -33;
        circleRectangle2.width = 256;
        circleRectangle2.height = 33;

        circleRectangles.add(new CircleLine(circleRectangle1, circleRectangle2));
        lastCircleSpawnTime = TimeUtils.nanoTime();
    }

    private void spawnCloud() {
        Rectangle cloudRectangle = new Rectangle();
        cloudRectangle.x = MathUtils.random(0, 320 - 125);
        cloudRectangle.y = -78;
        cloudRectangle.width = 125;
        cloudRectangle.height = 78;
        cloudRectangles.add(cloudRectangle);
        lastCloudSpawnTime = TimeUtils.nanoTime();
    }

    @Override
	public void dispose() {
        /* Something to dispose goes here */

        for( int i = 0 ; i < cloudImages.length ; i++ ) {
            cloudImages[i].dispose();
        }

        batch.dispose();
	}

	@Override
	public void render() {

        if( isPlayOn == false ) {
            return;
        }

		//Gdx.gl.glClearColor(0.509f, 0.7921f , 1, 1);
        Gdx.gl.glClearColor(0.62745f, 0.81176f , 0.92549f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        circleStateTime += Gdx.graphics.getDeltaTime();
        circleCurrentFrame = circleWalkAnimation.getKeyFrame(circleStateTime, true);
        squareStateTime += Gdx.graphics.getDeltaTime();
        squareCurrentFrame = squareWalkAnimation.getKeyFrame(squareStateTime, true);

        batch.begin();

            /* render the clouds */
            for(Rectangle cloudRectangle: cloudRectangles) {
                batch.draw(cloudImages[0], cloudRectangle.x, cloudRectangle.y);
            }

            for(CircleLine circleLineRectangle: circleRectangles) {
                batch.draw(circleCurrentFrame, circleLineRectangle.getLeft().x, circleLineRectangle.getLeft().y);
                batch.draw(circleCurrentFrame, circleLineRectangle.getRight().x, circleLineRectangle.getRight().y);
            }

            batch.draw(squareCurrentFrame, squareRectangle.x, squareRectangle.y);

		batch.end();

        CircleLine activeCircleLine = circleRectangles.peekFirst();

        if( activeCircleLine.getLeft().y >= (squareRectangle.y) ) {
            if( cloudRectangles.size() > 1 ) {
                activeCircleLine = circleRectangles.get(1);
            }
            else {
                activeCircleLine = null;
            }
        }

        if( activeCircleLine != null ) {

            if (Gdx.input.isTouched()) {

                currentTouchX = Gdx.input.getX();
                currentTouchY = Gdx.input.getY();

                touchPos.set(touchX, touchY, 0);

                camera.unproject(touchPos);

                if( isTouched == false ) {
                    isTouched = true;
                    touchX = Gdx.input.getX();
                    touchY = Gdx.input.getY();
                    oldX = activeCircleLine.getLeft().x;
                    oldY = activeCircleLine.getLeft().y;
                }
                else {
                    if (cloudRectangles.size() > 0) {
                        newX = oldX + (currentTouchX - touchX);
                        if( newX >= -256 && newX <= (320-64-256) ) {
                            activeCircleLine.getLeft().x = newX;
                            activeCircleLine.getRight().x = newX + 256 + 64;
                        }

                    }
                }

            }
            else {
                isTouched = false;
            }

        }

        if( cloudRectangles.peekFirst().y >= 480 ) {
            cloudRectangles.poll();
        }

        if( circleRectangles.peekFirst().getLeft().y >= 480 ) {
            circleRectangles.poll();
        }

        if(TimeUtils.nanoTime() - lastCloudSpawnTime > 2000000000) spawnCloud();
        if(TimeUtils.nanoTime() - lastCircleSpawnTime > 3500000000L) spawnCircle();

        /* Change the coords of clouds */
        Iterator<Rectangle> rectangleIterator = cloudRectangles.iterator();
        while(rectangleIterator.hasNext()) {

            Rectangle cloudRectangle = rectangleIterator.next();

            cloudRectangle.y += 150 * Gdx.graphics.getDeltaTime();

            if(cloudRectangle.y + 78 < 0)
                rectangleIterator.remove();
        }


        /* Co-ords of circle */
        Iterator<CircleLine> circleLineIterator = circleRectangles.iterator();
        System.out.println("-----------------------------------------------");
        while(circleLineIterator.hasNext()) {

            CircleLine circleLine = circleLineIterator.next();

            if( circleLine.getLeft().overlaps(squareRectangle) || circleLine.getRight().overlaps(squareRectangle)) {
                //dropSound.play();
                isPlayOn = false;
                break;
            }

            circleLine.getLeft().y += 50 * Gdx.graphics.getDeltaTime();
            circleLine.getRight().y = circleLine.getLeft().y;

            if(circleLine.getLeft().y + 33 < 0)
                circleLineIterator.remove();

        }

	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
