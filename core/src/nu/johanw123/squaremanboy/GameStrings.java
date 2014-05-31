package nu.johanw123.squaremanboy;

import java.util.HashMap;
import java.util.Map;

public class GameStrings 
{
	private static Map<String, String> strings = new HashMap<String, String>();
	
	public static String getGameString(String key)
	{
		return strings.get(key);
	}	
	
	public static void Setup()
	{
		//Platform-Wide Strings
		strings.put("LevelCleared", "Level cleared!");

		
		
		//Platform-Specific Strings
		if(SGame.CurrentPlatform == SGame.ePlatform.Android)
		{
			strings.put("LevelClearedComment", "Tap anywhere to continue");			
		}
		else if(SGame.CurrentPlatform == SGame.ePlatform.Desktop)
		{			
			strings.put("LevelClearedComment", "Click anywhere to continue (or press space/enter)");			
		}
		else if(SGame.CurrentPlatform == SGame.ePlatform.HTML5)
		{			
			strings.put("LevelClearedComment", "Click anywhere to continue (or press space/enter)");		
		}
		
	
	}
	
	
}
