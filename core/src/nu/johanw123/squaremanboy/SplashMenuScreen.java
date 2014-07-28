package nu.johanw123.squaremanboy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.Timer;

/**
 * Created by johanw123 on 2014-06-19.
 */
public class SplashMenuScreen extends SScreen
{
    private Sprite backgroundSprite;
    private TextureRegion region;
    private Texture backgroundTexture;

    public SplashMenuScreen(SGame _game)
    {
        super(_game);

        SInput.addKeyboardListener(this);
        SInput.addGamepadListener(this);
        SInput.addTouchListener(this);

        setupButtons();
    }
    Button logoButton;

    private void setupButtons()
    {
        backgroundTexture = new Texture(Gdx.files.internal("data/sprites/logo.png"));
        region = new TextureRegion(backgroundTexture, 0, 0, 2560, 1440);
        backgroundSprite = new Sprite(region);



        //backgroundSprite = new Sprite(backgroundTexture);

        logoButton = buttonHandler.createImageButton("logo", new EventListener() {
            @Override
            public boolean handle(Event event) {
                if(event.toString() == "ButtonActivated")
                {
                    //SGame.changeScreen(SGame.eScreenTypes.MainMenu);
                }
                return false;
            }
        });
        logoButton.setPosition(0,0);
        logoButton.setSize(1280, 720);

        //buttonHandler.getButton("Continue").setSize(2560, 1440);
        //buttonHandler.getButton("Continue").setVisible(false);
        //buttonHandler.setSelectedButton(0);

    }

    public void setSize(int width, int height)
    {
        int virtualScreenWidth = SRuntime.getGameVirtualWidth();
        int virtualScreenHeight = SRuntime.getGameVirtualHeight();

        if(SGame.CurrentPlatform == SGame.ePlatform.Desktop)// || SGame.CurrentPlatform == SGame.ePlatform.Ouya)
        {
            //Allows for resize on desktop
            virtualScreenWidth = width;
            virtualScreenHeight = height;
        }

        Vector2 size = Scaling.fill.apply(virtualScreenWidth, virtualScreenHeight, width, height);
        int viewportX = (int)(width - size.x) / 2;
        int viewportY = (int)(height - size.y) / 2;
        int viewportWidth = (int)size.x;
        int viewportHeight = (int)size.y;




        // Create camera with the desired resolution
        camera = new OrthographicCamera(width, height);

        // Move camera center to push 0,0 into the corner
        //camera.translate(width / 2, height / 2);

        // Set Y to point downwards
        camera.setToOrtho(false, virtualScreenWidth, virtualScreenHeight);

        camera.zoom = SRuntime.SCALE_FACTOR;

        // Update camera matrix
        camera.update();

        Gdx.gl.glViewport(viewportX, viewportY, viewportWidth, viewportHeight);
        stage.getViewport().update(viewportWidth, viewportHeight);

    }

    @Override
    public void resize(int width, int height) {
        //	initViewport(width, height, 16.0f / 9.0f);
        super.resize(width, height);
        //setSize(width, height);
        //buttonHandler.getImageButton("logo").setSize(width, height);
        //logoButton.setSize(width, height);
        sw = width;
        sh = height;
    }


    float sw;
    float sh;

    @Override
    public void render(float delta) {
        super.render(delta);

        camera.update();

        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
        Table.drawDebug(stage);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        //backgroundSprite.draw(batch);

        String drawString = "";

        if(SGame.CurrentPlatform == SGame.ePlatform.Android)
            SGame.font.setScale(0.5f);

        if(SGame.CurrentPlatform == SGame.ePlatform.Android)
            drawString = "Tap anywhere to Continue";
        else if(SGame.CurrentPlatform == SGame.ePlatform.Desktop)
            drawString = "Press Any Button/Key to Continue";
        SGame.font.draw(batch, drawString, SRuntime.SCREEN_WIDTH/2 - SGame.font.getBounds(drawString).width / 2, SRuntime.SCREEN_HEIGHT/2 - 100);

        SGame.font.setScale(0.25f);
        //backgroundSprite.draw(batch);
        batch.end();

    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int button)
    {
        continueToMainMenu();
        return true;
    }

    @Override
    public boolean keyDown(int keycode)
    {
        continueToMainMenu();
        return true;
    }

    private void continueToMainMenu() {
        SInput.keyboardListeners.remove(this);
        SInput.gamepadListeners.remove(this);
        SInput.touchListeners.remove(this);
        SGame.changeScreen(SGame.eScreenTypes.MainMenu);
        SAudioManager.playMusic("MainMenu", true);
    }

    @Override
    public boolean buttonDown(Controller controller, int buttonIndex) {

        for (Controller c : SGame.foundControllers) {
            c.removeListener(SGame.sInput);
        }

        SGame.activeController = controller;
        SGame.activeController.addListener(SGame.sInput);

        continueToMainMenu();
        return true;
    }


}
