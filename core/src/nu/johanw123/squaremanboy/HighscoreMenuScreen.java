package nu.johanw123.squaremanboy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class HighscoreMenuScreen extends SScreen
{

    public static ArrayList<String> sScores = new ArrayList<String>();


	public HighscoreMenuScreen(SGame _game) {
		super(_game);
		
		sScores.clear();
        
		buttonHandler.addListeners();

		setupButtons();
		
		SGame.parse.get_net_score();
		
		
		//SGame.ExternalHandler.showFullAd(true); //- shows banner ad
    }
	
	private void setupButtons()
	{
		 buttonHandler.createButton("Back", new EventListener() {
				@Override
				public boolean handle(Event event) {
					if(event.toString() == "ButtonActivated")
					{
						SGame.changeScreen(SGame.eScreenTypes.MainMenu);
						buttonHandler.setSelectedButton("Leaderboard");
					}
					return false;
				}
	        });
      
      buttonHandler.setSelectedButton(0);
      buttonHandler.setMenuBackButton(0);
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

        
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        String status = "";


        if(sScores.isEmpty())
            status = "Loading Scores";

        SGame.font.draw(batch, status, SRuntime.SCREEN_WIDTH/2 - SGame.font.getBounds(status).width/2, SRuntime.SCREEN_HEIGHT/2 - 100);
       
        float x = 100;
        float y = 590;
        int count = 0;

        for(String s : sScores)
        {
        	y -= SGame.font.getBounds(s).height;
            SGame.font.draw(batch, s, x, y - (++count * 15));
            
        }
        
        batch.end();
	}

}
