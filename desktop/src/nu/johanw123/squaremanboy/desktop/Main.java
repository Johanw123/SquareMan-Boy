package nu.johanw123.squaremanboy.desktop;

import nu.johanw123.squaremanboy.SGame;


import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "The-Adventure-Square";
        cfg.width = 1280;
        cfg.height = 720;
		cfg.resizable = true;
		//cfg.fullscreen = true;

				
		new LwjglApplication(new SGame(new RequestHandler()), cfg);
		SGame.CurrentPlatform = SGame.ePlatform.Desktop;
	}
}