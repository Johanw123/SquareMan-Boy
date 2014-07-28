package nu.johanw123.squaremanboy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.Viewport;

public class SScreen implements Screen, SKeyboardListener, SGamepadListener, STouchListener, GestureDetector.GestureListener
{
	 OrthographicCamera camera;
	 ButtonHandler buttonHandler;
	 SGame game;
	 SpriteBatch batch;
	 Stage stage;
     GestureDetector gestureDetector;

	public SScreen(SGame _game)
	{
		game = _game;
		camera = (OrthographicCamera)SGame.stage.getCamera();
        batch = new SpriteBatch();
        stage = SGame.stage;
        buttonHandler = SGame.buttonHandler;

       // gestureDetector = new GestureDetector(5.0f, 1.2f, 2.0f, 2.0f, this);
        gestureDetector = new GestureDetector(this);
        SGame.multiplexer.addProcessor(gestureDetector);

        camera.zoom = 1.0f;
        camera.update();
	}
	
	@Override
	public void dispose() {
		
		
	}

	@Override
	public void hide() {
		
		
	}

	@Override
	public void pause() {
		
		
	}

	@Override
	public void render(float delta) {
        update(delta);

	}

    public void update(float delta)
    {
        buttonHandler.update(delta);
    }

	@Override
	public void resize(int width, int height) {		

		Vector2 size = Scaling.stretch.apply(SRuntime.SCREEN_WIDTH, SRuntime.SCREEN_HEIGHT, width, height);
	    int viewportX = (int)(width - size.x) / 2;
	    int viewportY = (int)(height - size.y) / 2;
	    int viewportWidth = (int)size.x;
	    int viewportHeight = (int)size.y;
	    Gdx.gl.glViewport(viewportX, viewportY, viewportWidth, viewportHeight);	   
	    stage.getViewport().update(viewportWidth, viewportHeight);
	    


	    
	    //stage.getCamera().viewportWidth = viewportWidth;
	    //stage.getCamera().viewportHeight = viewportHeight;

	}

	@Override
	public void resume() {
		
		
	}

	@Override
	public void show() {
		
		
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

    @Override
    public boolean touchDragged(int x, int y, int pointer) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
       // return touchDown(x, y, pointer, button);
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
	public boolean povMoved(Controller controller, int povIndex,
			PovDirection value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean axisMoved(Controller controller, int axisIndex, float value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean buttonDown(Controller controller, int buttonIndex) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	
	
}
