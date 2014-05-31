package nu.johanw123.squaremanboy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Music.OnCompletionListener;

public class SAudioManager 
{
    private static Map<String, String> musicNames;
	
	private static Music currentMusic;
	
	private static ArrayList<String> playlist = new ArrayList<String>();
	private static ArrayList<String> playlistAll = new ArrayList<String>();

	public static void setup()	
	{
		musicNames = new HashMap<String, String>();
    	musicNames.put("MainMenu", "Lino Rise - Wechselwelt Trance Intro");
		
		musicNames.put("Song1", "danosongs.com-rocketry");
		musicNames.put("Song2", "Killing Time");
		musicNames.put("Song3", "Latin Industries");
		musicNames.put("Song4", "Lino Rise - Mana Tangata");
		musicNames.put("Song5", "Lino Rise - Rabenschwarz");		
		musicNames.put("Song6", "Ouroboros - Full Mix");
		
		playlistAll.add("Song1");
		playlistAll.add("Song2");
		playlistAll.add("Song3");
		playlistAll.add("Song4");
		playlistAll.add("Song5");
		playlistAll.add("Song6");
	}
	
	public static void startPlaylist()
	{
		nextSong();
	}
	
	private static void nextSong()
	{
		if(playlist.size() <= 0)
		{
			playlist.addAll(playlistAll);
		}
		
		int id = SRuntime.random.nextInt(playlist.size());
		String s = playlist.get(id);
		playlist.remove(id);
		
		playMusic(s, false);
				
		currentMusic.setOnCompletionListener(new OnCompletionListener(){
			@Override
			public void onCompletion(Music music) {
				nextSong();				
			}
		});		
	}
	
	public static void toggleMusic()
	{
		if(currentMusic != null)
		{
			if(currentMusic.getVolume() <= 0.1f)
			{
				currentMusic.setVolume(0.5f);
			}
			else if(currentMusic.getVolume() >= 0.5f)
			{
				currentMusic.setVolume(0);
			}			
		}
	}
	
	public static void playMusic(String key, boolean loop)
	{
		if(currentMusic != null)
		{
			//playingPlaylist = false;
			currentMusic.stop();
			currentMusic.dispose();
		}
		
		if(musicNames.containsKey(key))
		{			
			currentMusic = Gdx.audio.newMusic(Gdx.files.internal("data/audio/" + musicNames.get(key) + ".mp3"));			
			currentMusic.setLooping(loop);
			if(SRuntime.musicOn)
				currentMusic.setVolume(0.5f);
			else
				currentMusic.setVolume(0);
			currentMusic.play();			
		}
		
	}
	

}
