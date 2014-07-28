package nu.johanw123.squaremanboy;

import java.util.ArrayList;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class CreditsMenuScreen extends SScreen
{

	public CreditsMenuScreen(SGame _game) 
	{
		super(_game);
  
		buttonHandler.addListeners();
		
	
		setupButtons();
    }

	private void setupButtons() 
	{
		
		 buttonHandler.createTextButton("Back", new EventListener() {
				@Override
				public boolean handle(Event event) {
					if(event.toString() == "ButtonActivated")
					{
						SGame.changeScreen(SGame.eScreenTypes.MainMenu);
						buttonHandler.setSelectedButton("Credits");
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
        
        
        float x = 100;
        float y = 600;
        int count = 0;
        
        ArrayList<String> musicNames = new ArrayList<String>();
        //Title - Artist - Website
        musicNames.add("'Ouroboros' Kevin MacLeod (incompetech.com)");
        musicNames.add("'Latin Industries' Kevin MacLeod (incompetech.com)");
        musicNames.add("'Killing Time' Kevin MacLeod (incompetech.com)");
        musicNames.add("'Mana Tangata' Lino Rise (linorise.com)");
        musicNames.add("'Rabenschwarz' Lino Rise (linorise.com)");
        musicNames.add("'Wechselwelt' Lino Rise (linorise.com)");
        musicNames.add("'Rocketry' Dan-O (DanoSongs.com)");
        //Rabenschwarz (Lino Rise)
        //Wechselwelt - meny (Lino Rise)
        //Latin Industries (Incompetech)
        //Killing Time (Incompetech)
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        
      
        //game.font.draw(game.batch, "'Ouroboros' Kevin MacLeod (incompetech.com)", SRuntime.SCREEN_WIDTH/2, SRuntime.SCREEN_HEIGHT/2 - 100);
        
        //game.font.draw(game.batch, "Music:", x, y);
        
        ++count;
        
        /*for(String s : musicNames)
        {
            SGame.font.draw(game.batch, s, x, y - ++count * 50);                      
        }*/
        
        for(String s : musicNames)
        {
        	y -= SGame.font.getBounds(s).height;
            SGame.font.draw(batch, s, x, y - (++count * 15));
            
        }
        
        
        count += 5;
        
        SGame.font.draw(batch, "Licensed under Creative Commons: By Attribution 3.0", x, y - ++count * 15);
        y -= SGame.font.getBounds("Licensed under Creative Commons: By Attribution 3.0").height;
        SGame.font.draw(batch, "http://creativecommons.org/licenses/by/3.0/", x, y - ++count * 15);
        
        
        
        batch.end();  
        
	}	
}
