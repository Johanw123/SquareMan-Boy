package nu.johanw123.squaremanboy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class HUD 
{
	Stage pauseStage = new Stage();
	Stage menuStage = new Stage();
	OrthographicCamera cam = new OrthographicCamera();
	SpriteBatch batch = new SpriteBatch();
	GameScreen gameScreen;
	
	int currentPage = 0;
	
	boolean gamePaused = false;
	
	public HUD(GameScreen _gameScreen)
	{
		gameScreen = _gameScreen;


		((OrthographicCamera) pauseStage.getCamera()).setToOrtho(false, 1280, 720);
						
		setupButtonsPause();
		
	}
	
	private void setupButtonsPause()
	{
		SGame.multiplexer.addProcessor(pauseStage);
		
		SGame.buttonHandler.createButton("Menu", new EventListener() {
			@Override
			public boolean handle(Event event) {
				if(event.toString() == "ButtonActivated")
				{
					gameScreen.pauseGame();
				}
				return false;
			}
        }, 0, 0, 100, 40, pauseStage);
		
		//SGame.buttonHandler.clearButtons();
	}
	
	private void setupButtonsMenu()
	{	
		SGame.multiplexer.addProcessor(menuStage);
		
		SGame.buttonHandler.createButton("Resume Game", new EventListener() {
				@Override
				public boolean handle(Event event) {
					if(event.toString() == "ButtonActivated")
						resumeGame();
					return false;
				}
	        }, menuStage);
	        
	    if(SGame.GameMode == SGame.eGameMode.Training)
	    {	    
	    	SGame.buttonHandler.createButton("Select Level", new EventListener() {
	 			@Override
	 			public boolean handle(Event event) {
	 				if(event.toString() == "ButtonActivated")
	 				{
	 					removeButtons();
	 					SGame.multiplexer.addProcessor(menuStage);
	 					setupButtonsLevelSelect();
	 		            //SGame.changeScreen(SGame.eScreenTypes.LevelSelect);
	 				}
	 				return false;
	 			}
	         }, menuStage);
	    }
	    
	    SGame.buttonHandler.createButton("Back to Menu", new EventListener() {
			@Override
			public boolean handle(Event event) {
				if(event.toString() == "ButtonActivated")
				{
					SGame.resetGame();
					//SInput.removeKeyboardListener(menuStage);
		            SGame.changeScreen(SGame.eScreenTypes.MainMenu);
		            SAudioManager.playMusic("MainMenu", true);	
				}
				return false;
			}
        }, menuStage);
   
	    SGame.buttonHandler.setSelectedButton(0);
	}
	
	
	
	private void setupButtonsLevelSelect()
	{
		createLevelButtons(currentPage);
   
	    SGame.buttonHandler.setSelectedButton(0);
		
	}
	
	public void pauseGame()
	{
		gamePaused = true;
		
		SGame.buttonHandler.clearButtons();
		
		setupButtonsMenu();
		
		pauseStage.clear();
		
		SGame.buttonHandler.addListeners();
	}
	
	private void resumeGame()
	{
		gamePaused = false;
		
		removeButtons();
		
		//SGame.changeScreen(SGame.eScreenTypes.Game, "resume");
		
		gameScreen.show();
		
		setupButtonsPause();
		
		//SGame.buttonHandler.clearListeners();
	}
	
	private void removeButtons()
	{
		menuStage.clear();
		//pauseStage.clear();
		SGame.buttonHandler.clearButtons();
		SGame.multiplexer.removeProcessor(menuStage);
		//SGame.multiplexer.removeProcessor(pauseStage);
	}
	
	public void render()
	{
		
		cam.update();
		pauseStage.act(Gdx.graphics.getDeltaTime());
		pauseStage.draw();
        
		//menuStage.act(Gdx.graphics.getDeltaTime());
		//menuStage.draw();
		
		
	}
	
	private void createLevelButtons(int page)
	{
		SGame.buttonHandler.clearButtons();
		
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
        	
	        SGame.buttonHandler.createButton(SGame.levelNamesArray[i + page * 7], eventListener, x, y, buttonWidth, buttonHeight, menuStage);        	
        }
        
        SGame.buttonHandler.setSelectedButton(0);
        
        if(createForwardsButton)
    	{        	
        	SGame.buttonHandler.createButton(">", new EventListener() {
  				@Override
  				public boolean handle(Event event) {
  					if(event.toString() == "ButtonActivated")
  					{
  						changePage(1);
  						SGame.buttonHandler.setSelectedButton(0);
  						if(!SGame.buttonHandler.setSelectedButton(">"))
  							SGame.buttonHandler.setSelectedButton("<");
  					}
  					return false;
  				}
  	        }, 800, 300, 400, 70, menuStage);    
    	}

    	if(createBackwardsButton)
    	{        	
    		SGame.buttonHandler.createButton("<", new EventListener() {
 				@Override
 				public boolean handle(Event event) {
 					if(event.toString() == "ButtonActivated")
 					{
 						changePage(-1);
 						SGame.buttonHandler.setSelectedButton(0);
 						if(!SGame.buttonHandler.setSelectedButton("<"))
 							SGame.buttonHandler.setSelectedButton(">");
 					}
 					return false;
 				}
 	        }, 800, 200, 400, 70, menuStage);        	
    	}
    	
    	
    	SGame.buttonHandler.createButton("Back", new EventListener() {
				@Override
				public boolean handle(Event event) {
					if(event.toString() == "ButtonActivated")
					{
						removeButtons();
						setupButtonsMenu();
					}
					return false;
				}
	        }, 800, 100, 400, 70, menuStage);  
		
	}
	
	private void changePage(int pageRelative)
	{
		//SGame.multiplexer.removeProcessor(menuStage);
		menuStage.clear();
		currentPage += pageRelative;
		createLevelButtons(currentPage);
	}
	
	private void levelButtonClicked(String levelName)
	{
		SGame.GameMode = SGame.eGameMode.Training;
        SGame.changeScreen(SGame.eScreenTypes.Game, "level", levelName);
        SAudioManager.startPlaylist();
	}
	
	
}
