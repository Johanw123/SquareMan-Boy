package nu.johanw123.squaremanboy;

import java.util.Locale;
import java.util.concurrent.Callable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;

public class LevelClearMenuScreen extends SScreen
{
	String clearTime;
	String personalBest;
	String levelName;
	
	public LevelClearMenuScreen(SGame _game, String _levelName, String _clearTime) {
		super(_game);

		clearTime = _clearTime;
		levelName = _levelName;
		

		
        setupButtons();
        
        
        float savedBest = SRuntime.getPrefs().getFloat("personalbest" + levelName, Float.MAX_VALUE);
        float newTime = Float.parseFloat(clearTime);
        if(newTime < savedBest)
        {
        	personalBest = ""+newTime;
        	SRuntime.getPrefs().putFloat("personalbest" + levelName, newTime);
        	SRuntime.getPrefs().flush();
        }
        else
        {
        	personalBest = ""+savedBest;
        }


        float delay = 0.1f; // seconds

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                buttonHandler.addListeners();
            }
        }, delay);
    }
	
	private void setupButtons()
	{			
		buttonHandler.createButton("Main Menu", new EventListener() {
			@Override
			public boolean handle(Event event) {
				if(event.toString() == "ButtonActivated")
				{
					SGame.changeScreen(SGame.eScreenTypes.MainMenu);
				}
				return false;
			}
        }, 50, 50, 200, 70);
		
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
	        }, 400, 50, 200, 70);
			
			
			buttonHandler.createButton("Replay Level", new EventListener() {
				@Override
				public boolean handle(Event event) {
					if(event.toString() == "ButtonActivated")
					{
						SGame.changeScreen(SGame.eScreenTypes.Game, "replaylevel");
					}
					return false;
				}
	        }, 650, 50, 200, 70);
			
		}		
		
	 buttonHandler.createButton("Next Level", new EventListener() {
			@Override
			public boolean handle(Event event) {
				if(event.toString() == "ButtonActivated")
				{
					SGame.changeScreen(SGame.eScreenTypes.Game, "nextlevel");
				}
				return false;
			}
        }, SRuntime.SCREEN_WIDTH - 250, 50, 200, 70);
		
     buttonHandler.setSelectedButton(3);
	}

	@Override
	public void render(float delta) {
        super.render(delta);

		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();


        
        String clearString1 = levelName.substring(0, levelName.lastIndexOf('.')) + " Cleared!";
        SGame.font.draw(batch, clearString1, SRuntime.SCREEN_WIDTH/2 - SGame.font.getBounds(clearString1).width / 2, SRuntime.SCREEN_HEIGHT - 100);
        
        String clearString2 = "Clear Time: " + clearTime + " s";
        SGame.font.draw(batch, clearString2, SRuntime.SCREEN_WIDTH/2 - SGame.font.getBounds(clearString2).width / 2, SRuntime.SCREEN_HEIGHT - 200);
        
        String clearString3 = "Personal Best: " + personalBest + " s";
        SGame.font.draw(batch, clearString3, SRuntime.SCREEN_WIDTH/2 - SGame.font.getBounds(clearString3).width / 2, SRuntime.SCREEN_HEIGHT - 300);
        
        
        if(SGame.GameMode == SGame.eGameMode.Survival)
        {
        	  String clearString4 = "Total Time: " + Extensions.FormatFloatPrecision(SGame.survivalTotalTime, 3) + " s";
              SGame.font.draw(batch, clearString4, SRuntime.SCREEN_WIDTH/2 - SGame.font.getBounds(clearString4).width / 2, SRuntime.SCREEN_HEIGHT - 400);
              
        }
        
        
        
        batch.end();
        
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
        Table.drawDebug(stage);
        
        
	}
	
}
