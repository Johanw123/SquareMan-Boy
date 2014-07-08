package nu.johanw123.squaremanboy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
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
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.lang.*;
import java.util.concurrent.Callable;

public class SurvivalEndScreen extends SScreen
{
    public static int uploadCode = -1; //TODO make enum

	public SurvivalEndScreen(SGame _game) {
		super(_game);
        uploadCode = -1;
        
        buttonHandler.addListeners();

        setupButtons();
    }
	
	private void setupButtons()
	{
		 buttonHandler.createButton("Submit Score", new EventListener() {
				@Override
				public boolean handle(Event event) {
					if(event.toString() == "ButtonActivated")
					{
                        if(!Parse.inputShowing)
						    SGame.parse.add_net_score();
						
						//SGame.parse.clean_net_score();
					}
					return false;
				}
	        });

	        buttonHandler.createButton("Play Again", new EventListener() {
				@Override
				public boolean handle(Event event) {
					if(event.toString() == "ButtonActivated")
					{
						SGame.resetGame();
						SGame.GameMode = SGame.eGameMode.Survival;
			            SGame.changeScreen(SGame.eScreenTypes.Game, "firstlevel");
					}
					return false;
				}
	        });
	        
	        buttonHandler.createButton("Back to Menu", new EventListener() {
				@Override
				public boolean handle(Event event) {
					if(event.toString() == "ButtonActivated")
					{
						SGame.resetGame();
			            SGame.changeScreen(SGame.eScreenTypes.MainMenu);
			            SAudioManager.playMusic("MainMenu", true);
					}
					return false;
				}
	        });
	        
	        buttonHandler.setSelectedButton(0);
	}
	
	String status = "";
	@Override
	public void render(float delta) {
        super.render(delta);

        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
        Table.drawDebug(stage);

        camera.update();
        batch.setProjectionMatrix(camera.combined);


        batch.begin();
        String drawString = "Score: " + (SGame.currentLevelId - 10);
        
        SGame.font.draw(batch, drawString, SRuntime.SCREEN_WIDTH/2 - SGame.font.getBounds(drawString).width / 2, SRuntime.SCREEN_HEIGHT/2 - 100);

        if(uploadCode == -1)
        	status = "";

        if(uploadCode == -2)
        {
        	status = "Error Error!";
        	
        	buttonHandler.enableButton("Submit Score");
            //buttonHandler.setSelectedButton("Play Again");
        	
        	uploadCode = 0;
        	/*
        	TextButton submitButton = buttonHandler.getButton("Submit Score");
        	submitButton.setDisabled(true);
			submitButton.setChecked(false);
			submitButton.setDisabled(false);
			*/
        	//submitButton.setVisible(true);
        	//submitButton.reset();
        	//submitButton.setChecked(false);
        	
        }
        
        if(uploadCode == 1)
        {
        	if(status != "Uploading score...")
        	{
        		buttonHandler.disableButton("Submit Score");
                buttonHandler.setSelectedButton("Play Again");
        	}
        	
            status = "Uploading score...";
        }
        if(uploadCode == 2)
        {
        	/*
        	TextButton submitButton = buttonHandler.getButton("Submit Score");
        	submitButton.setDisabled(true);
			submitButton.setChecked(false);
			*/
            status = "Score uploaded successfully";
            
           
            
        }

        SGame.font.draw(batch, status, SRuntime.SCREEN_WIDTH/2 - SGame.font.getBounds(status).width / 2, SRuntime.SCREEN_HEIGHT/2 - 50);



        //game.font.draw(game.batch, personalBestScore, SRuntime.SCREEN_WIDTH/2, SRuntime.SCREEN_HEIGHT/2 - 150);

        batch.end();

	}	
}
