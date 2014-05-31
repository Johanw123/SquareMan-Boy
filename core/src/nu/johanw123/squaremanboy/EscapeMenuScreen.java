package nu.johanw123.squaremanboy;

import java.util.concurrent.Callable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Scaling;

public class EscapeMenuScreen extends SScreen
{

	public EscapeMenuScreen(SGame _game) {
        super(_game);
                
        buttonHandler.addListeners();
        
        setupButtons();
        
        SInput.addKeyboardListener(this);
    }
	
	private void setupButtons()
	{
		 buttonHandler.createButton("Resume Game", new EventListener() {
				@Override
				public boolean handle(Event event) {
					if(event.toString() == "ButtonActivated")
						resumeGame();
					return false;
				}
	        });
	        
	    if(SGame.GameMode == SGame.eGameMode.Training)
	    {
	    
	    	 buttonHandler.createButton("Select Level", new EventListener() {
	 			@Override
	 			public boolean handle(Event event) {
	 				if(event.toString() == "ButtonActivated")
	 				{

	 		            SGame.changeScreen(SGame.eScreenTypes.LevelSelect);
	 				}
	 				return false;
	 			}
	         });
	    }
	    
	    final EscapeMenuScreen thisMenu =  this;
	    buttonHandler.createButton("Back to Menu", new EventListener() {
			@Override
			public boolean handle(Event event) {
				if(event.toString() == "ButtonActivated")
				{
					SGame.resetGame();
					SInput.removeKeyboardListener(thisMenu);
		            SGame.changeScreen(SGame.eScreenTypes.MainMenu);
		            SAudioManager.playMusic("MainMenu", true);	
				}
				return false;
			}
        });
   
	    buttonHandler.setSelectedButton(0);
	}
	
	private void resumeGame()
	{
		SGame.changeScreen(SGame.eScreenTypes.Game, "resume");
		SInput.removeKeyboardListener(this);
	}
	
	@Override
	public void render(float delta) {
		camera.update();

		
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
        Table.drawDebug(stage);

   

	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		
		if(keycode == Keys.ESCAPE)
		{
			resumeGame();			
			return true;
		}
		
		return false;
	}

}
