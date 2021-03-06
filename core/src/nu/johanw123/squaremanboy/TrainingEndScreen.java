package nu.johanw123.squaremanboy;


import com.badlogic.gdx.Gdx;
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
import com.badlogic.gdx.utils.Scaling;

import java.lang.*;
import java.util.concurrent.Callable;

public class TrainingEndScreen extends SScreen
{
	public TrainingEndScreen(SGame _game) {
       super(_game);
       
       buttonHandler.addListeners();
        
       setupButtons();
    }
	
	private void setupButtons()
	{
		 buttonHandler.createTextButton("Play Training Again", new EventListener() {
				@Override
				public boolean handle(Event event) {
					if(event.toString() == "ButtonActivated")
					{
						SGame.GameMode = SGame.eGameMode.Training;
						SGame.changeScreen(SGame.eScreenTypes.Game, "reset");
					}
					return false;
				}
	        });   
		 
	        buttonHandler.createTextButton("Play Survival Mode", new EventListener() {
				@Override
				public boolean handle(Event event) {
					if(event.toString() == "ButtonActivated")
					{
						SGame.GameMode = SGame.eGameMode.Survival;
			             SGame.changeScreen(SGame.eScreenTypes.Game, "reset");
					}
					return false;
				}
	        });
	        
	        buttonHandler.createTextButton("Back to Menu", new EventListener() {
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

        String text = "Training mode is over!";

        SGame.font.draw(batch, text, SRuntime.SCREEN_WIDTH/2 - SGame.font.getBounds(text).width / 2, SRuntime.SCREEN_HEIGHT/2 - 50);
        batch.end();

	}
}
