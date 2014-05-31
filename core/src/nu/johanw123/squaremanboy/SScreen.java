package nu.johanw123.squaremanboy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.Viewport;

public class SScreen implements Screen, SKeyboardListener, SGamepadListener, STouchListener
{
	 OrthographicCamera camera;
	 ButtonHandler buttonHandler;
	 SGame game;
	 SpriteBatch batch;
	 Stage stage;
	
	public SScreen(SGame _game)
	{
		game = _game;
		camera = (OrthographicCamera)SGame.stage.getCamera();
        batch = new SpriteBatch();
        stage = SGame.stage;
        buttonHandler = SGame.buttonHandler;
		
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
	public void render(float arg0) {
		
		
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
