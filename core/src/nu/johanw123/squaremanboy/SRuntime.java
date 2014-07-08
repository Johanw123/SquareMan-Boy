package nu.johanw123.squaremanboy;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class SRuntime
{
	public static final String PREFS_NAME = "my_app";
	public static final String PREFS_FORCE720P = "force720p";
	public static final String PREFS_MUSICON = "musicon";
	public static final String PREFS_CAMERATYPE = "cameratype";
	
	public static int TILE_SIZE;
	public static int SCREEN_WIDTH;
	public static int SCREEN_HEIGHT;
	
	public static int SCREEN_WIDTH_ALT;
	public static int SCREEN_HEIGHT_ALT;

    public static int WORLD_WIDTH;
    public static int WORLD_HEIGHT;

    public static float SCALE_FACTOR;
    
    public static boolean force720p;
    
    public static Random random = new Random();
    
    public static boolean musicOn;
    public static int cameraType;
	
	public static void SetupVars()
    {        
	    SCREEN_WIDTH = 1280;
        SCREEN_HEIGHT = 720;
        
        SCREEN_WIDTH_ALT = 1920;
        SCREEN_HEIGHT_ALT = 1080;
        
        TILE_SIZE = 128;

        SCALE_FACTOR = TILE_SIZE / 32;
        
        force720p = getPrefs().getBoolean(PREFS_FORCE720P, true);
        force720p = true;
        musicOn = getPrefs().getBoolean(PREFS_MUSICON, true);
        cameraType = getPrefs().getInteger(PREFS_CAMERATYPE, 0);

        if(cameraType != OptionsMenuScreen.eCameraType.Directed.ordinal() && cameraType != OptionsMenuScreen.eCameraType.Follow.ordinal())
        {
            cameraType = 0;
        }

    }
	
	 private static Preferences preferences;
	   public static Preferences getPrefs() 
	   {
	      if(preferences==null){
	         preferences = Gdx.app.getPreferences(PREFS_NAME);
	      }
	   return preferences;
	   }
	
	public static int getGameVirtualWidth()
	{
		if(!force720p) //use native or 1080 or whatever
		{
			return SCREEN_WIDTH_ALT;
		}
		
		return SCREEN_WIDTH;
	}
	
	public static int getGameVirtualHeight()
	{
		if(!force720p) //use native or 1080 or whatever
		{
			return SCREEN_HEIGHT_ALT;
		}
		
		return SCREEN_HEIGHT;
	}
}
