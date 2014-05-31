package nu.johanw123.squaremanboy;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SGame extends Game {
    private SpriteBatch batch;
    public static BitmapFont font;

    public static InputMultiplexer multiplexer;    
    public static ButtonHandler buttonHandler;
    public static int currentLevelId = -1;
    public static float survivalTotalTime;
    public static String currentLevelName;
    public static Parse parse;
    public static Stage stage;
    public static Controller controller;
        
    private static ArrayList<String> levelNames = new ArrayList<String>();    
    private static GameScreen gameScreen;
    private static Screen currentScreen;
    private static SGame game;
    private static SInput sInput;

    public enum eScreenTypes
    {
        MainMenu,
        Game,
        EscapeMenu,
        OptionsMenu,
        CreditsMenu,
        LevelSelect,
        LevelClear,
        HighScore,
        SurvivalEnd,
        TrainingEnd
    }

    enum eGameMode
    {
        None,
        Survival,
        Training
    }
    
    public enum ePlatform
    {
    	None,
    	Desktop,
    	Android,
    	HTML5
    }
    
    public static ePlatform CurrentPlatform = ePlatform.None;
    public static eGameMode GameMode = eGameMode.None;

    public static String[] levelNamesArray =
    {
        "Training 001.tmx",
        "Training 002.tmx",
        "Training 003.tmx",
        "Training 004.tmx",
        "Training 005.tmx",
        "Training 006.tmx",
        "Training 007.tmx",
        "Training 008.tmx",
        "Training 009.tmx",
        "Training 010.tmx",
        
        "The Green Bridge.tmx",
        "Small Steps.tmx",
        "Giant Leap.tmx",
        "Into The Basket.tmx",
        "Pillars of.. Something.tmx",
        "Follow The Flow.tmx",
        "Get Out.tmx",
        "Look Behind.tmx",
        "Down Below.tmx",
        "Up And Around.tmx",
        "Watch Your Step.tmx",
        "Tower of Mischief.tmx",
        "Basket Case.tmx",
        "Get Up There.tmx",
        "In Between.tmx",
        "Way Over There.tmx",
        "The Right Way.tmx",
        
        "Climb The Tree.tmx",
        "Good Luck.tmx"
        
	};   

    public static IRequestHandler ExternalHandler;

	public SGame(IRequestHandler irh){
		SGame.ExternalHandler = irh;
	}

    public static void resetGame()
    {
        currentLevelId = -1;
        survivalTotalTime = 0;
    }

    public static void fillLevelNamesManual()
    {
        for(String name : levelNamesArray)
        {
            levelNames.add(name);
        }
    }

    @Override
    public void create()
    {
    	GameStrings.Setup();
    	
        batch = new SpriteBatch();

        //fillLevelNames();
        fillLevelNamesManual();

        font = new BitmapFont(Gdx.files.internal("data/fonts/FreeSerifBold.fnt"));       
        
        SAudioManager.setup();
        SRuntime.SetupVars();

        game = this;
        parse = new Parse();
        
        stage = new Stage();
        

        sInput = new SInput();        
        
        for (Controller controller : Controllers.getControllers()) {
            System.out.println(controller.getName());
        }
        if(Controllers.getControllers().size > 0)
        {
        	controller = Controllers.getControllers().first();
        	controller.addListener(sInput);
        }
        
        
        
        
        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(sInput);
        multiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(multiplexer);
        

        buttonHandler = new ButtonHandler();
        
       
        
        Timer.schedule(new Task(){
            @Override
            public void run() {
            	SGame.changeScreen(eScreenTypes.MainMenu);
            	
            	SAudioManager.playMusic("MainMenu", true);
            }
        }, 0.5f);         
        
    }    

    public static void changeScreen(eScreenTypes screenType, String... params)
    {
        currentScreen = null;
        stage.clear();
        buttonHandler.clearButtons();
        buttonHandler.clearListeners();        
        
        SGame.ExternalHandler.showAd(false);
        SGame.ExternalHandler.showFullAd(false);

        String command = "";
       
        switch(screenType)
        {
            case Game:                 
             
                if(params.length > 0)
                {
                    command = params[0];
                }
                if(command != "resume")
                	gameScreen = new GameScreen(game);
                
                if(command.toLowerCase() == "reset")
                {
                    resetGame();
                    command = "firstlevel";
                }
                if(command.toLowerCase() == "replaylevel")
                {
                	--currentLevelId;
                	command = "nextlevel";
                }
                if(command.toLowerCase() == "level")
                {
                	String commandArg = params[1];
                	
                	currentLevelId = levelNames.indexOf(commandArg) - 1;
                	
                	command = "nextlevel";
                }
                             
                if(command.toLowerCase() == "nextlevel" || command.toLowerCase() == "firstlevel")
                {
                    //next level
                    ++currentLevelId;
                    if(command.toLowerCase() == "firstlevel")
                    {
                    	if(SGame.GameMode == eGameMode.Survival)
                    	{
                    		currentLevelId = 10;
                    		
                    	}
                    	else
                    		currentLevelId = 0;
                    }

                    if(currentLevelId < levelNames.size())
                    {
                        String levelName = levelNames.get(currentLevelId);

                       gameScreen.changeMap(levelName);
                        //gameScreen.changeMap("008.tmx");

                        currentLevelName = levelName;
                    }
                    else
                    {
                        if(GameMode == eGameMode.Survival)
                        {
                            changeScreen(eScreenTypes.SurvivalEnd);
                            return;
                        }
                        else if(GameMode == eGameMode.Training)
                        {
                            changeScreen(eScreenTypes.TrainingEnd);
                            return;
                        }
                    }
                }

                currentScreen = gameScreen;
                break;
            case MainMenu:
                currentScreen = new MainMenuScreen(game);
                break;
            case LevelSelect:
            	currentScreen = new LevelSelectMenuScreen(game);
            	break;
            case OptionsMenu:         	            	
            	currentScreen = new OptionsMenuScreen(game);
            	break;
            case CreditsMenu:
            	currentScreen = new CreditsMenuScreen(game);
            	break;
            case EscapeMenu:
                currentScreen = new EscapeMenuScreen(game);
                break;
            case LevelClear:
            	
                String levelName = params[0];
                String clearTime = params[1];
                
               
            	
                currentScreen = new LevelClearMenuScreen(game, levelName, clearTime);
                break;
            case HighScore:
                currentScreen = new HighscoreMenuScreen(game);
                break;
            case SurvivalEnd:
                currentScreen = new SurvivalEndScreen(game);
                break;
            case TrainingEnd:
                currentScreen = new TrainingEndScreen(game);
                break;
        }        
        
        //currentScreen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());  
        
        float scale = Gdx.graphics.getWidth() / 1280;
	    
	    SGame.font.setScale(scale);

    	game.setScreen(currentScreen);
    	
    	int virtualWidth = SRuntime.SCREEN_WIDTH;
    	int virtualHeight = SRuntime.SCREEN_HEIGHT;
    	
    	if(screenType == eScreenTypes.Game)
    	{
    		virtualWidth = SRuntime.getGameVirtualWidth();
    		virtualHeight = SRuntime.getGameVirtualHeight();
    	}
    	
		((OrthographicCamera) SGame.stage.getCamera()).setToOrtho(false, virtualWidth, virtualHeight);
    	//if(screenType == eScreenTypes.Game)
    		//gameScreen.SetCameraOnPlayer();
    	
    	//currentScreen.resize(1280, 720); 
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void resize(int width, int height) {
    	Vector2 size = Scaling.fit.apply(SRuntime.SCREEN_WIDTH, SRuntime.SCREEN_HEIGHT, width, height);
	    int viewportX = (int)(width - size.x) / 2;
	    int viewportY = (int)(height - size.y) / 2;
	    int viewportWidth = (int)size.x;
	    int viewportHeight = (int)size.y;
	    Gdx.gl.glViewport(viewportX, viewportY, viewportWidth, viewportHeight);	   
	    stage.getViewport().update(viewportWidth, viewportHeight);
	    stage.getCamera().viewportWidth = viewportWidth;
	    stage.getCamera().viewportHeight = viewportHeight;
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }
}