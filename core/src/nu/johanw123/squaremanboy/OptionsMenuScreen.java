package nu.johanw123.squaremanboy;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class OptionsMenuScreen extends SScreen
{
	enum eResolution{
		r720p,
		r1080p
		
	}
	
	public enum eCameraType
	{
		Directed,		
		Follow
	}
	
	private enum CameraType
	{
		Directed,
		Follow
	}
	
	private eResolution currentResolution;
	private CameraType currentCameraType;
	
	public OptionsMenuScreen(SGame _game) 
	{
		super(_game);
  
		buttonHandler.addListeners();
		
		if(SRuntime.force720p)
			currentResolution = eResolution.r720p;
		else
			currentResolution = eResolution.r1080p;
		
		switch(SRuntime.cameraType)
		{
			case 0:
				currentCameraType = CameraType.Directed;
				break;
			case 1:
				currentCameraType = CameraType.Follow;
				break;
            default:
                currentCameraType = CameraType.Directed;
                break;
		}	
				
		setupButtons();
		setMusicButtonText();
		
		setButton();
		setButtonCameraType();
    }

	private void setupButtons() 
	{
		/*
		buttonHandler.createButton("Gameplay Resolution", new EventListener() {
			@Override
			public boolean handle(Event event) {
				if(event.toString() == "ButtonActivated")
				{
					toggleResolution();
				}
					//Toggle Resolution

				return false;
			}
        });
		*/
		//buttonHandler.getButton("Gameplay Resolution").setText("Gameplay Resolution: Native");
		
		buttonHandler.createButton("Music", new EventListener() {
			@Override
			public boolean handle(Event event) {
				if(event.toString() == "ButtonActivated")
				{
					SAudioManager.toggleMusic();
					SRuntime.musicOn = !SRuntime.musicOn;
					setMusicButtonText();
					SRuntime.getPrefs().putBoolean(SRuntime.PREFS_MUSICON, SRuntime.musicOn);
			        SRuntime.getPrefs().flush();
				}
					//Toggle Resolution

				return false;
			}
        });
		
		buttonHandler.createButton("Camera Type", new EventListener() {
			@Override
			public boolean handle(Event event) {
				if(event.toString() == "ButtonActivated")
				{					
					toggleCameraType();
				}
					//Toggle Resolution

				return false;
			}
        });
		
		
	
			 buttonHandler.createButton("Back", new EventListener() {
					@Override
					public boolean handle(Event event) {
						if(event.toString() == "ButtonActivated")
						{
							SGame.changeScreen(SGame.eScreenTypes.MainMenu);
							buttonHandler.setSelectedButton("Options");
						}
						return false;
					}
		        });
		
	        
	    buttonHandler.setSelectedButton(0);
        buttonHandler.setMenuBackButton("Back");
	}
	
	private void setMusicButtonText()
	{
		if(SRuntime.musicOn)
			buttonHandler.getButton("Music").setText("Music: ON");
		else
			buttonHandler.getButton("Music").setText("Music: OFF");
	}
	
	private void toggleResolution()
	{		
		if(currentResolution == eResolution.r1080p)
		{
			currentResolution = eResolution.r720p;
			SRuntime.force720p = true;
		}
		else if(currentResolution == eResolution.r720p)
		{
			currentResolution = eResolution.r1080p;
			SRuntime.force720p = false;
		}
		
		setButton();
		SRuntime.getPrefs().putBoolean(SRuntime.PREFS_FORCE720P, SRuntime.force720p);
        SRuntime.getPrefs().flush();
        
    
	}
	
	private void toggleCameraType()
	{		
		switch(currentCameraType)
		{
			case Directed:
				currentCameraType = CameraType.Follow;				
				break;
			case Follow:
				currentCameraType = CameraType.Directed;
                break;
            default:
                currentCameraType = CameraType.Directed;
                break;
		}
		
		setButtonCameraType();
		SRuntime.cameraType = currentCameraType.ordinal();
		SRuntime.getPrefs().putInteger(SRuntime.PREFS_CAMERATYPE, currentCameraType.ordinal());
        SRuntime.getPrefs().flush();		
	}
	
	private void setButtonCameraType()
	{
		buttonHandler.getButton("Camera Type").setText("Camera Type: " + currentCameraType.toString());
	}
	
	private void setButton()
	{
		//buttonHandler.getButton("Gameplay Resolution").setText("Gameplay Resolution: " + currentResolution.toString().substring(1));
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
