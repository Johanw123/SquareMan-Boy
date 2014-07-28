package nu.johanw123.squaremanboy;

import java.util.concurrent.Callable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Scaling;

public class LevelSelectMenuScreen extends SScreen
{
	int currentPage = 0;
	 
	public LevelSelectMenuScreen(SGame _game) {
		super(_game);
		
		
		buttonHandler.addListeners();
		
        createLevelButtons(currentPage);
        
        
        //SGame.ExternalHandler.showAd(true);
    }
	private void createLevelButtons(int page)
	{
		buttonHandler.clearButtons();
        int buttonWidth = 600;
        int buttonHeight = 70;
        boolean createForwardsButton = true;
        boolean createBackwardsButton = true;
        float x;
        float y;
                               
        for(int i = 0; i < 7; ++i)
        {
        	x = 100;
        	y = 600 - 90 * i;
        	
        	if(i + page * 7 >= SGame.levelNamesArray.length)
        	{
        		createForwardsButton = false;
        		break;
        	}
        	if(page == 0)
        		createBackwardsButton = false;
        	
        	final String levelName = SGame.levelNamesArray[i + page * 7];        	
	     	
	     	EventListener eventListener = new EventListener() {
				@Override
				public boolean handle(Event event) {
					if(event.toString() == "ButtonActivated")
						levelButtonClicked(levelName);

					return false;
				}
	        };
        	
        	Button button = buttonHandler.createTextButton(SGame.levelNamesArray[i + page * 7].substring(0, SGame.levelNamesArray[i + page * 7].lastIndexOf('.')), eventListener);
            button.setPosition(x, y);
            button.setSize(buttonWidth, buttonHeight);
        }
        

        
        if(createForwardsButton)
    	{        	
        	 Button button = buttonHandler.createTextButton(">", new EventListener() {
  				@Override
  				public boolean handle(Event event) {
  					if(event.toString() == "ButtonActivated")
  					{
  						changePage(1);
  						buttonHandler.setSelectedButton(0);
  						if(!buttonHandler.setSelectedButton(">"))
  							buttonHandler.setSelectedButton("<");
  					}
  					return false;
  				}
  	                });
            button.setPosition(800, 300);
            button.setSize(400, 70);

    	}

    	if(createBackwardsButton)
    	{        	
        	 Button button = buttonHandler.createTextButton("<", new EventListener() {
                 @Override
                 public boolean handle(Event event) {
                     if (event.toString() == "ButtonActivated") {
                         changePage(-1);
                         buttonHandler.setSelectedButton(0);
                         if (!buttonHandler.setSelectedButton("<"))
                             buttonHandler.setSelectedButton(">");
                     }
                     return false;
                 }
             });
            button.setPosition(800, 200);
            button.setSize(400, 70);
    	}

    	Button button = buttonHandler.createTextButton("Back to Menu", new EventListener() {
            @Override
            public boolean handle(Event event) {
                if (event.toString() == "ButtonActivated") {
                    SGame.changeScreen(SGame.eScreenTypes.MainMenu);
                }
                return false;
            }
        });
        button.setPosition(800, 100);
        button.setSize(400, 70);

        buttonHandler.setSelectedButton(0);
        buttonHandler.setMenuBackButton("Back to Menu");
		
	}
	
	private void changePage(int pageRelative)
	{
		stage.clear();
		currentPage += pageRelative;
		createLevelButtons(currentPage);
	}
	
	private void levelButtonClicked(String levelName)
	{
		SGame.GameMode = SGame.eGameMode.Training;
        SGame.changeScreen(SGame.eScreenTypes.Game, "level", levelName);
        SAudioManager.startPlaylist();
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
