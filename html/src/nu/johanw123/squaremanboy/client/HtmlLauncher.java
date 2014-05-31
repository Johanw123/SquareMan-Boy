package nu.johanw123.squaremanboy.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import nu.johanw123.squaremanboy.SGame;

public class HtmlLauncher extends GwtApplication {

    @Override
    public GwtApplicationConfiguration getConfig () {
        GwtApplicationConfiguration cfg = new GwtApplicationConfiguration(1280, 720);
        cfg.fps = 60;
        cfg.useDebugGL = false;

        return cfg;
    }

    @Override
    public ApplicationListener getApplicationListener () {
        SGame sGame = new SGame(new RequestHandler());
        SGame.CurrentPlatform = SGame.ePlatform.HTML5;
        return sGame;
    }
}