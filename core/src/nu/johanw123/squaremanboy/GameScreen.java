package nu.johanw123.squaremanboy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.controllers.Controller;
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
    
    private boolean temporaryFreeCamera = false;
    
    private HUD hud = new HUD(this);
    
    long timeCount;
    long levelTime;
    

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

        for(MapObject object : objectLayer.getObjects())
        {
            if(object != null && object.getName() != null)
            {
                switch (object.getName())
                {
                    case "Player":
                        RectangleMapObject rectangleMapObject = (RectangleMapObject)object;
                        player = new Player(rectangleMapObject.getRectangle().x, rectangleMapObject.getRectangle().y);
                        break;
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
                
                if(SRuntime.cameraType == 0)
                {
	        		camera.position.y = 300;
	        		camera.position.x = 600;
                }
                else
                {
                	SetCameraOnPlayer();
                }
                
                timeCount = TimeUtils.nanoTime();
            }
        }, delay); 
        
    }
    
    public void SetCameraOnPlayer()
	{   	
		camera.position.x = player.getX();
        camera.position.y = player.getY();       
	}

	public void update(float deltaTime)
	{		
		if(updateWorld && !hidden)
		{
	        player.update(deltaTime);
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
         
         if(offsetX > 100
         		|| offsetX < -100)
         {
         	updateCamX = true;
         	//camDestX = player.getPosition().x + offsetX * 1.2f;
         	
         	if(offsetX < 0)
         		camDestX = player.getPosition().x - 50;
         	else if(offsetX > 0)
	         	camDestX = player.getPosition().x + 50;
         	
         	if(camDestX > SRuntime.WORLD_WIDTH * 32)
         	{
         		updateCamX = false;
         	}
         	//System.out.println(camDestX);
         }
         
         
         if(offsetY > 300
         		|| (offsetY < -300))
         {
         	updateCamY = true;
         	
         	if(offsetY < 0)	
         		camDestY = player.getPosition().y - 150;
         	else if(offsetY > 0)
	         	camDestY = player.getPosition().y + 150;
         	
         	if(camDestY > SRuntime.WORLD_HEIGHT * 32)
         	{
         		updateCamY = false;
         	}	
         	if(camDestY < -100)
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
    	if((camera.position.x < 630 && offsetX < 0)
              	|| (camera.position.x > SRuntime.WORLD_WIDTH * 32 && offsetX > 0)
       			|| (Math.abs(camDestX - camera.position.x) < 10))
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
			|| (camera.position.y > SRuntime.SCREEN_HEIGHT * 32 && offsetY > 0)
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
          	|| (camera.position.x > SRuntime.WORLD_WIDTH * 32 && offsetX > 0)
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
		if((camera.position.y < 300 && offsetY < 0)
			|| (camera.position.y > SRuntime.SCREEN_HEIGHT * 32 && offsetY > 0)
			|| (offsetY < 10 && offsetY > -10))
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
                        	levelTime = TimeUtils.timeSinceNanos(timeCount);
                        	float levelTimeF = TimeUtils.nanosToMillis(levelTime);
                        	levelTimeF /= 1000;
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
        
		update(delta);
		
		if(!mapLoaded)
			return;
		
		camera.update();
		
		//bloom.capture(); 
		
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		
		
		stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
        Table.drawDebug(stage);
		
		
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

	@Override
	public void show() {
		hidden = false;
		//SetCameraOnPlayer();
		
	
		//camera.position.x = SRuntime.WORLD_WIDTH;
		//camera.position.y = 530;
    	
		
		SInput.addKeyboardListener(this);
	}
	
	@Override
	public void resize(int width, int height) {	
		initViewport(width, height, 16.0f / 9.0f);
		/*
		int virtualScreenWidth = SRuntime.getGameVirtualWidth();
		int virtualScreenHeight = SRuntime.getGameVirtualHeight();
		
		
		Vector2 size = Scaling.fill.apply(virtualScreenWidth, virtualScreenHeight, width, height);
	    int viewportX = (int)(width - size.x) / 2;
	    int viewportY = (int)(height - size.y) / 2;
	    int viewportWidth = (int)size.x;
	    int viewportHeight = (int)size.y;
	    Gdx.gl.glViewport(viewportX, viewportY, viewportWidth, viewportHeight);	   
	    stage.getViewport().update(viewportWidth, viewportHeight);	   
	    */
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
	       
	      // Update camera matrix
	      camera.update();
	       
	      // Set viewport to restrict drawing
	      Gdx.gl20.glViewport((int)ox, (int)oy, (int)vw, (int)vh);
	   }
	
	public void pauseGame()
	{
		hidden = true;
		SGame.changeScreen(SGame.eScreenTypes.EscapeMenu);
		SInput.keyboardListeners.remove(this);
		//hud.pauseGame();
		
		isPaused = true;
	}
	
	@Override
	public void hide() {
		SInput.keyboardListeners.remove(this);

	}
	
	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		if(keycode == Keys.ESCAPE && !hidden)
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
	
	Vector2 oldPos = new Vector2(camera.position.x, camera.position.y);
	private void toggleTempFreeCam()
	{
		if(!temporaryFreeCamera)
		{
			oldPos.x = camera.position.x;
			oldPos.y = camera.position.y;
			
			player.freeCameraPosition.x = camera.position.x;
			player.freeCameraPosition.y = camera.position.y;
		}
		else
		{
			camera.position.x = oldPos.x;
			camera.position.y = oldPos.y;
		}
		
		temporaryFreeCamera = !temporaryFreeCamera;
		
	}
	
	@Override
	public boolean buttonDown(Controller controller, int buttonIndex) {
		
		if(buttonIndex == 10 && !hidden) //change 10 to start button
		{
			pauseGame();
			return true;
		}
		
		
		
		return super.buttonDown(controller, buttonIndex);
	}

}
