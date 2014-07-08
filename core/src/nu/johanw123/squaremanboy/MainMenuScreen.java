package nu.johanw123.squaremanboy;

import java.beans.EventHandler;
import java.util.concurrent.Callable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputEvent.Type;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;

public class MainMenuScreen extends SScreen
{
	public MainMenuScreen(SGame _game) 
	{
		super(_game);
  
		buttonHandler.addListeners();					
		
		setupButtons();
    }

	private void setupButtons() 
	{
		buttonHandler.createButton("Play Puzzle Mode", new EventListener() {
			@Override
			public boolean handle(Event event) {
				if(event.toString() == "ButtonActivated")
					SGame.changeScreen(SGame.eScreenTypes.LevelSelect);

				return false;
			}
        });
		buttonHandler.createButton("Play Survival Mode (1 Life)", new EventListener() {
			@Override
			public boolean handle(Event event) {
				if(event.toString() == "ButtonActivated")
				{
				  	SGame.GameMode = SGame.eGameMode.Survival;
                    SGame.changeScreen(SGame.eScreenTypes.Game, "firstlevel");
                    SAudioManager.startPlaylist();
				}

				return false;
			}
        });
		buttonHandler.createButton("Leaderboard", new EventListener() {
			@Override
			public boolean handle(Event event) {
				if(event.toString() == "ButtonActivated")
					SGame.changeScreen(SGame.eScreenTypes.HighScore);

				return false;
			}
        });
		
		buttonHandler.createButton("Options", new EventListener() {
			@Override
			public boolean handle(Event event) {
				if(event.toString() == "ButtonActivated")
					SGame.changeScreen(SGame.eScreenTypes.OptionsMenu);

				return false;
			}
        });
		

		buttonHandler.createButton("Credits", new EventListener() {
			@Override
			public boolean handle(Event event) {
				if(event.toString() == "ButtonActivated")
					SGame.changeScreen(SGame.eScreenTypes.CreditsMenu);

				return false;
			}
        });
		
		if(SGame.CurrentPlatform != SGame.ePlatform.HTML5)
		{
		
	        buttonHandler.createButton("Exit", new EventListener() {
				@Override
				public boolean handle(Event event) {
					if(event.toString() == "ButtonActivated")
						Gdx.app.exit();

					return false;
				}
	        });
		}
	        
	        buttonHandler.setSelectedButton(0);
	}
	
	@Override
	public void render(float delta) {
        super.render(delta);

        camera.update();
       
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
        Table.drawDebug(stage);
	}	

}
