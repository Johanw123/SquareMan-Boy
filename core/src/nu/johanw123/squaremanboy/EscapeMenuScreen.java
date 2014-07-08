package nu.johanw123.squaremanboy;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class EscapeMenuScreen extends SScreen
{

	public EscapeMenuScreen(SGame _game) {
        super(_game);
                
        buttonHandler.addListeners();
        
        setupButtons();
        
        SInput.addKeyboardListener(this);
        SInput.addGamepadListener(this);
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
        SInput.removeGamepadListener(this);
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

    @Override
    public boolean buttonDown(Controller controller, int buttonIndex) {

        if(SInput.getButtonIndexMatch(buttonIndex, SInput.eControllerButtons.Menu))
        //if((buttonIndex == Ouya.BUTTON_MENU || buttonIndex == 7) ) //7 is xbox controller start button
        {
            resumeGame();
            return true;
        }



        return false;
    }

}
