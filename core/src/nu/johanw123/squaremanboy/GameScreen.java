package nu.johanw123.squaremanboy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.mappings.Ouya;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;


public class GameScreen extends SScreen
{

	boolean hidden = false;
	boolean isPaused = false;
	private TiledMap map;

	public static boolean mapLoaded = false;
	
	private OrthogonalTiledMapRenderer renderer;
    ShapeRenderer shapeRenderer;
    boolean updateWorld;

    boolean updateCamX = false;
    boolean updateCamY = false;
    float camDestX = 0;
    float camDestY = 0;

    float mapZoomScale = SRuntime.SCALE_FACTOR;

    private float mapCameraStartX;
    private float mapCameraStartY;

    public static boolean temporaryFreeCamera = false;
    
    private HUD hud = new HUD(this);
    
    long timeCount;
    long levelTime;

   // private FPSLogger fpsLogger;
    

    private Pool<Rectangle> rectPool = new Pool<Rectangle>() {
        @Override
        protected Rectangle newObject () {
            return new Rectangle();
        }
    };

	public Player player;
	
	public GameScreen(SGame _game) {
		super(_game);
		

		shapeRenderer = new ShapeRenderer();

        //fpsLogger = new FPSLogger();
		//setupButtons();
	}

    public void changeMap(String mapName)
    {
        loadMap(mapName);
    }
    
    private void loadMap(String mapName)
    {
    	mapLoaded = false;
    	
        map = new TmxMapLoader().load("data/maps/" + mapName);

        TiledMapTileLayer tileLayer = (TiledMapTileLayer)map.getLayers().get(0);
        MapLayer objectLayer = map.getLayers().get(1);

        SRuntime.WORLD_WIDTH = tileLayer.getWidth();
        SRuntime.WORLD_HEIGHT = tileLayer.getHeight();

        Object propCamStartX = map.getProperties().get("camStartX");
        Object propCamStartY = map.getProperties().get("camStartY");
        Object propZoom = map.getProperties().get("zoom");

        mapCameraStartX = (propCamStartX != null ? Float.parseFloat(propCamStartX.toString()) : 600) * SRuntime.SCALE_FACTOR; //TODO set in map properties
        mapCameraStartY = (propCamStartY != null ? Float.parseFloat(propCamStartY.toString()) : 300) * SRuntime.SCALE_FACTOR; //TODO set in map properties

        mapZoomScale = propZoom != null ? Float.parseFloat(propZoom.toString()) : SRuntime.SCALE_FACTOR; //TODO set in map properties


        for(MapObject object : objectLayer.getObjects())
        {
            if(object != null && object.getName() != null)
            {
               // if(object.getName() == "Player")
                //switch (object.getName())
                {
                    //case "Player":
                        RectangleMapObject rectangleMapObject = (RectangleMapObject)object;
                        player = new Player(rectangleMapObject.getRectangle().x, rectangleMapObject.getRectangle().y);
                        //player.freeZoomScale = mapZoomScale;
                      //  break;
                }
            }
        }

        renderer = new OrthogonalTiledMapRenderer(map);
        //SetCameraOnPlayer();
        updateWorld = false;
        

        
        float delay = 0.02f; // seconds

        Timer.schedule(new Task(){
            @Override
            public void run() {
                updateWorld = true;
                mapLoaded = true;
                
                //camera.position.x = SRuntime.WORLD_WIDTH;
                
               // if(SRuntime.cameraType == 0)
               // {
                    //TODO set a start cam position in map to set here

                    camera.position.x = mapCameraStartX;
	        		camera.position.y = mapCameraStartY;

                    camera.zoom = mapZoomScale;

                    oldPos.x = camera.position.x;
                    oldPos.y = camera.position.y;

                    player.freeCameraPosition.x = camera.position.x;
                    player.freeCameraPosition.y = camera.position.y;


               // }
               // else
              //  {
              //  	SetCameraOnPlayer();
              //  }
                
                timeCount = TimeUtils.nanoTime();
            }
        }, delay); 
        
    }


    @Override
	public void update(float delta)
	{		
		if(updateWorld && !hidden)
		{
	        player.update(delta);
	        updateCamera();
			checkCollisions();
	        checkBounds();
		}
		else
			updateCamera();		
	}

    private void checkBounds()
    {
        if(player.getY() < -300)
        {
        	if(SGame.GameMode == SGame.eGameMode.Training)
        		player.reset();
        	else if(SGame.GameMode == SGame.eGameMode.Survival)
        	{
        		player.reset();
        		mapLoaded = false;
        		SGame.changeScreen(SGame.eScreenTypes.SurvivalEnd);
        	}
        }
    }
    
    private void updateCamera()
    {
    	int cam = SRuntime.cameraType;
    	
    	if(temporaryFreeCamera)
    		cam = 2;
    	
    	switch(cam)
    	{
	    	case 0:
	    		updateCameraDirected();
	    		break;
	    	case 1:
	    		updateCameraFollow();
	    		break;
	    	case 2:
	    		updateCameraFree();


	    		break;
    	}


    	
    }

    private void updateCameraFree()
    {
    	camera.position.x = player.freeCameraPosition.x; 
    	camera.position.y = player.freeCameraPosition.y;

        if((SGame.activeController != null && SGame.activeController.getAxis(Ouya.AXIS_LEFT_TRIGGER) > 0.1f) || SInput.isKeyDown(Keys.Q))
        {
            player.freeZoomScale -= 0.1f;
            if(player.freeZoomScale < 0.5f)
                player.freeZoomScale = 0.5f;
        }
        if((SGame.activeController != null && SGame.activeController.getAxis(Ouya.AXIS_RIGHT_TRIGGER) > 0.1f) || SInput.isKeyDown(Keys.E))
        {
            player.freeZoomScale += 0.1f;
            if(player.freeZoomScale > 10.0f)
                player.freeZoomScale = 10.0f;
        }

        camera.zoom = player.freeZoomScale;

    	camera.update();
    }
    
    private void updateCameraFollow()
    {
    	camera.position.x += (player.getPosition().x - camera.position.x) * 0.1f;       	
    	camera.position.y += (player.getPosition().y - camera.position.y) * 0.1f;
   		camera.update();
    }
    
    private void updateCameraDirected()
    {
    	 float offsetX = player.getPosition().x - camera.position.x;
    	 float offsetY = player.getPosition().y - camera.position.y;
         
         if(offsetX > 100 * SRuntime.SCALE_FACTOR
         		|| offsetX < -100* SRuntime.SCALE_FACTOR)
         {
         	updateCamX = true;
         	//camDestX = player.getPosition().x + offsetX * 1.2f;
         	
         	if(offsetX < 0)
         		camDestX = player.getPosition().x - 50;
         	else if(offsetX > 0)
	         	camDestX = player.getPosition().x + 50;
         	
         	if(camDestX > SRuntime.WORLD_WIDTH * SRuntime.TILE_SIZE)
         	{
         		updateCamX = false;
         	}
         	//System.out.println(camDestX);
         }
         
         
         if(offsetY > 300* SRuntime.SCALE_FACTOR
         		|| (offsetY < -300* SRuntime.SCALE_FACTOR))
         {
         	updateCamY = true;
         	
         	if(offsetY < 0)	
         		camDestY = player.getPosition().y - 150* SRuntime.SCALE_FACTOR;
         	else if(offsetY > 0)
	         	camDestY = player.getPosition().y + 150* SRuntime.SCALE_FACTOR;
         	
         	if(camDestY > SRuntime.WORLD_HEIGHT * SRuntime.TILE_SIZE)
         	{
         		updateCamY = false;
         	}	
         	if(camDestY < -100* SRuntime.SCALE_FACTOR)
         	{
         		updateCamY = false;
         	}
         	System.out.println(camera.position.y + " - " + camDestY + " - " + offsetY);
         	
         }
    	
         
       if(updateCamX)
    	   updateCamX2(offsetX);
       if(updateCamY)
    	    updateCamY(offsetY);
    }
    
    private void updateCamX2(float offsetX)
    {
    	if((camera.position.x < 630* SRuntime.SCALE_FACTOR && offsetX < 0)
              	|| (camera.position.x > SRuntime.WORLD_WIDTH * SRuntime.TILE_SIZE && offsetX > 0)
       			|| (Math.abs(camDestX - camera.position.x) < 10* SRuntime.SCALE_FACTOR))
           {
          	 updateCamX = false;
          	 return;
           }
       		       	
       		camera.position.x += (camDestX - camera.position.x) * 0.00001f * Math.abs(offsetX * 5);       		
       		camera.update();
    	
    	
    }
    
    private void updateCamY2(float offsetY)
    { 	
		if((camera.position.y < 300 && offsetY < 0)
			|| (camera.position.y > SRuntime.SCREEN_HEIGHT * SRuntime.TILE_SIZE && offsetY > 0)
			|| (Math.abs(camDestY - camera.position.y) < 10))
        {
       	 updateCamY = false;
       	 return;
        }
		
		camera.position.y += (camDestY - camera.position.y) * 0.2f;    		
		camera.update();
    }

    private void updateCamX(float offsetX)
    {  		
   		if((camera.position.x < 600 && offsetX < 0)
          	|| (camera.position.x > SRuntime.WORLD_WIDTH * SRuntime.TILE_SIZE && offsetX > 0)
   			|| (offsetX < 10 && offsetX > -10))
       {
      	 updateCamX = false;
      	 return;
       }
   		       	
   		camera.position.x += (player.getX() - camera.position.x) * 0.2f;       		
   		camera.update();       	
    }
    private void updateCamY(float offsetY)
    { 	
		if((camera.position.y < 300* SRuntime.SCALE_FACTOR && offsetY < 0)
			|| (camera.position.y > SRuntime.SCREEN_HEIGHT * SRuntime.TILE_SIZE && offsetY > 0)
			|| (offsetY < 10* SRuntime.SCALE_FACTOR && offsetY > -10* SRuntime.SCALE_FACTOR))
        {
       	 updateCamY = false;
       	 return;
        }
		
		camera.position.y += (player.getY() - camera.position.y) * 0.2f;    		
		camera.update();
    }
    
	private void checkCollisions()
	{
        int startX, startY, endX, endY;
        startY = (int)(player.getY() / SRuntime.TILE_SIZE) - 1;
        endY = startY + 2;
        startX = (int)(player.getX() / SRuntime.TILE_SIZE) - 1;
        endX = startX + 2;

        Array<Rectangle> tiles = new Array<Rectangle>();
        TiledMapTileLayer layer = (TiledMapTileLayer)map.getLayers().get(0);
        for (int x = startX; x <= endX; x++) {
            for (int y = startY; y <= endY; y++) {
                TiledMapTileLayer.Cell cell = layer.getCell(x, y);
                if (cell != null) {
                    Rectangle tile = rectPool.obtain();
                    tile.set(x * SRuntime.TILE_SIZE, y * SRuntime.TILE_SIZE, SRuntime.TILE_SIZE, SRuntime.TILE_SIZE);
                    tiles.add(tile);

                    if (player.getBoundingBox().overlaps(tile))
                    {
                        if(player.handleTileCollision(cell.getTile().getId()))
                        {
                        	//Level finished
                        	levelTime = TimeUtils.timeSinceNanos(timeCount) - pauseTime;
                        	float levelTimeF = TimeUtils.nanosToMillis(levelTime);
                        	levelTimeF /= 1000;

                            levelTimeF = Extensions.FormatFloatPrecision(levelTimeF, 3);
                        	String levelTimeS = ""+levelTimeF;

                        	if(SGame.GameMode == SGame.eGameMode.Survival)
                        		SGame.survivalTotalTime += levelTimeF;
                        	
                        	GameScreen.mapLoaded = false;
                            SGame.changeScreen(SGame.eScreenTypes.LevelClear, SGame.currentLevelName, levelTimeS);
                        }
                        break;
                    }
                }
            }
        }
        rectPool.freeAll(tiles);
	}

	@Override
	public void render(float delta) {
        super.render(delta);
		
		if(!mapLoaded)
			return;
		
		camera.update();
		
		//bloom.capture(); 
		
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		
		
		stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
        Table.drawDebug(stage);

        //Gdx.graphics.setTitle(""+Gdx.graphics.getFramesPerSecond());
        //fpsLogger.log();

		renderer.setView((OrthographicCamera) camera);
		renderer.render();
		
		//bloom.render(); 

		batch.setProjectionMatrix(camera.combined);
		batch.begin();

		player.render(batch);
		
		//SGame.font.draw(batch, "" + (player.getPosition().x - camera.position.x), 100, 100);
		//SGame.font.draw(batch, "" + camera.position.x, 100, 150);
		batch.end();
		
		hud.render();
		 

		
	}


    public void setPausedCamPos()
    {
        if(pausedCamX != null)
            camera.position.x = pausedCamX;
        if(pausedCamY != null)
            camera.position.y = pausedCamY;
    }



	@Override
	public void show() {
		hidden = false;

		//SetCameraOnPlayer();




		//camera.position.x = SRuntime.WORLD_WIDTH;
		//camera.position.y = 530;
    	
		
		SInput.addKeyboardListener(this);
        SInput.addTouchListener(this);
        SInput.addGamepadListener(this);

        if(isPaused) {
            isPaused = false;
            pauseTime += TimeUtils.timeSinceNanos(timePausedCount);
        }
	}

    public void setSize(int width, int height)
    {
        int virtualScreenWidth = SRuntime.getGameVirtualWidth();
        int virtualScreenHeight = SRuntime.getGameVirtualHeight();

        if(SGame.CurrentPlatform == SGame.ePlatform.Desktop || SGame.CurrentPlatform == SGame.ePlatform.Ouya) {
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

        setSize(width, height);

	}
	
	public void initViewport(float width, float height, float aspect) {
	      // Get the window size in pixels
	      float w = Gdx.graphics.getWidth();
	      float h = Gdx.graphics.getHeight();

	      float vw, vh; // Viewport size in screen coordinates
	      float ox, oy; // Viewport offset in screen coordinates
	       
	      // Check aspect ratio
	      if(w > h * aspect) {
	         // Black bars on the sides
	         vh = h;
	         vw = Math.round(vh * aspect);
	         oy = 0;
	         ox = (w - vw) / 2;
	      } else {
	         // Black bars on top and bottom
	         vw = w;
	         vh = Math.round(vw * (1 / aspect));
	         ox = 0;
	         oy = (h - vh) / 2;
	      }
	       
	      // Create camera with the desired resolution
	      camera = new OrthographicCamera(width, height);
	       
	      // Move camera center to push 0,0 into the corner
	      camera.translate(width / 2, height / 2);
	       
	      // Set Y to point downwards
	      camera.setToOrtho(false, width, height);

          camera.zoom = SRuntime.SCALE_FACTOR;
	       
	      // Update camera matrix
	      camera.update();


	       
	      // Set viewport to restrict drawing
	      Gdx.gl20.glViewport((int)ox, (int)oy, (int)vw, (int)vh);
	   }


    Float pausedCamX = null;
    Float pausedCamY = null;

    long timePausedCount = 0;
    long pauseTime = 0;

	public void pauseGame()
	{
		hidden = true;

        pausedCamX = camera.position.x;
        pausedCamY = camera.position.y;

        timePausedCount = TimeUtils.nanoTime();

		SGame.changeScreen(SGame.eScreenTypes.EscapeMenu);
		SInput.keyboardListeners.remove(this);
        SInput.touchListeners.remove(this);
        SInput.gamepadListeners.remove(this);
		//hud.pauseGame();
		
		isPaused = true;
	}
	
	@Override
	public void hide() {
		SInput.keyboardListeners.remove(this);
        SInput.touchListeners.remove(this);
        SInput.gamepadListeners.remove(this);
	}
	

	
	Vector2 oldPos = new Vector2(camera.position.x, camera.position.y);
	private void toggleTempFreeCam()
	{
		if(!temporaryFreeCamera)
		{
			oldPos.x = camera.position.x;
			oldPos.y = camera.position.y;
			
			//player.freeCameraPosition.x = camera.position.x;
			//player.freeCameraPosition.y = camera.position.y;


		}
		else
		{
			camera.position.x = oldPos.x;
			camera.position.y = oldPos.y;

            camera.zoom = mapZoomScale;
		}
		
		temporaryFreeCamera = !temporaryFreeCamera;
		
	}

    @Override
    public boolean keyDown(int keycode) {
        // TODO Auto-generated method stub
        if(!hidden && keycode == Keys.ESCAPE)
        {
            pauseGame();
            return true;
        }
        if(keycode == Keys.SPACE)
        {
            toggleTempFreeCam();
        }

        return false;
    }
	
	@Override
	public boolean buttonDown(Controller controller, int buttonIndex) {

        if(SInput.getButtonIndexMatch(buttonIndex, SInput.eControllerButtons.Menu))
		//if(!hidden && (buttonIndex == Ouya.BUTTON_MENU || buttonIndex == 7) ) //7 is xbox controller start button
		{
			pauseGame();
            return true;
		}

        if(SInput.getButtonIndexMatch(buttonIndex, SInput.eControllerButtons.CameraToggle)) //2 is x on xbox-controller
        {
            toggleTempFreeCam();
            return true;
        }

		return false;
	}

/*
    float prevRatio = 4.0f;
    @Override
    public boolean zoom(float initialDistance, float distance) {
        float ratio = initialDistance / distance;
        //camera.zoom = camera.zoom * ratio;
        //System.out.println(camera.zoom);

        if(ratio < prevRatio) {
            player.freeZoomScale -= 0.1f;
            if (player.freeZoomScale < 0.5f)
                player.freeZoomScale = 0.5f;
        }
        if(ratio > prevRatio) {
            player.freeZoomScale += 0.1f;
            if (player.freeZoomScale > 10.0f)
                player.freeZoomScale = 10.0f;
        }


        prevRatio = ratio;
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {

        if(count >= 2)
            toggleTempFreeCam();

        Gdx.app.log("tap", ""+count);
        return false;
    }
*/
}
