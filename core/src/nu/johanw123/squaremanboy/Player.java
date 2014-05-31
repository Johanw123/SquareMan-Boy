package nu.johanw123.squaremanboy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.controllers.mappings.Ouya;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;


public class Player extends Entity
{
    private Vector2 startPosition;
    public Vector2 freeCameraPosition;

	public Player(float x, float y)
	{
		super(x,y, "Player");

        startPosition = new Vector2(x,y);
        freeCameraPosition = new Vector2(x,y);
        reset();
        mBoundingBox.set(getX(), getY(), getTexture().getWidth(), getTexture().getHeight());
	}
	
	@Override
	public String getType()
	{
		return "Player";
	}

    public void reset()
    {
        //if(Mode == puzzle)
        setPosition(startPosition);
        setVelocity(new Vector2(0,0));
        mBoundingBox.set(getX(), getY(), getTexture().getWidth(), getTexture().getHeight());
    }
	
	public void handleInput(float deltaTime)
	{		
		if(SGame.CurrentPlatform == SGame.ePlatform.Android)
		{
			for(int i = 0; i < 2; ++i)
			{
				if(Gdx.input.isTouched(i))
				{
					SGame.stage.getCamera().unproject(new Vector3(Gdx.input.getX(i), Gdx.input.getY(i), 0));
					if(Gdx.input.getX(i) > SRuntime.SCREEN_WIDTH/2)
					{						
					    this.translateX(10);
					}
		            else if(Gdx.input.getX(i) < SRuntime.SCREEN_WIDTH/2)
		            {
		            	if(Gdx.input.getX(i) < 150 && Gdx.input.getY(i) > Gdx.graphics.getHeight() - 100)
		            		continue;
		            	
		                this.translateX(-10);
		            }					
				}
			}
		}
		
		if(Gdx.input.isKeyPressed(Keys.RIGHT) || Gdx.input.isButtonPressed(Ouya.BUTTON_DPAD_RIGHT) ||
				(SGame.controller != null && SGame.controller.getPov(0) == PovDirection.east))
		
		{
			this.translateX((600) * deltaTime);

		}
		if((SGame.controller != null && SGame.controller.getAxis(0) > 0.3f))
		{
			this.translateX((600 * SGame.controller.getAxis(0)) * deltaTime);
		}
		if((SGame.controller != null && SGame.controller.getAxis(0) < -0.3f))
		{
			this.translateX((600 * SGame.controller.getAxis(0)) * deltaTime);
		}
		
		if((SGame.controller != null && SGame.controller.getAxis(2) > 0.1f))
		{
			this.translateX((600 * -SGame.controller.getAxis(2)) * deltaTime);
		}
		if((SGame.controller != null && SGame.controller.getAxis(5) > 0.1f))
		{
			this.translateX((600 * SGame.controller.getAxis(5)) * deltaTime);
		}
		if(Gdx.input.isKeyPressed(Keys.LEFT) || Gdx.input.isButtonPressed(Ouya.BUTTON_DPAD_LEFT) ||
				(SGame.controller != null && SGame.controller.getPov(0) == PovDirection.west)){
			this.translateX((-600) * deltaTime);
		}
		
		if(Gdx.input.isKeyPressed(Keys.A))
		{
			freeCameraPosition.x -= 400 * deltaTime;
			if(freeCameraPosition.x < 0)
				freeCameraPosition.x = 0;
		}
		if(Gdx.input.isKeyPressed(Keys.D))
		{
			freeCameraPosition.x += 400 * deltaTime;
			if(freeCameraPosition.x > SRuntime.WORLD_WIDTH * 32)
				freeCameraPosition.x = SRuntime.WORLD_WIDTH * 32;
		}
		if(Gdx.input.isKeyPressed(Keys.W))
		{
			freeCameraPosition.y += 400 * deltaTime;
			if(freeCameraPosition.y > SRuntime.WORLD_HEIGHT * 32)
				freeCameraPosition.y = SRuntime.WORLD_HEIGHT * 32;
		}
		if(Gdx.input.isKeyPressed(Keys.S))
		{
			freeCameraPosition.y -= 400 * deltaTime;
			if(freeCameraPosition.y < 0)
				freeCameraPosition.y = 0;
		}
		
		
	}
	
	@Override
	public void update(float deltaTime)
	{
        handleInput(deltaTime);
        
        //System.out.println(deltaTime);
        
        if(deltaTime > 0.04f)
        	deltaTime = 0.04f;

        float newX = getX() + mVelocity.x * deltaTime;
        float newY = getY() + mVelocity.y * deltaTime;
        float oldX = getX();
        float oldY = getY();
        
        //System.out.println(newY - oldX);
        
        setX(newX);
        setY(newY);

        mVelocity.y -= 4000 * deltaTime;
        if(mVelocity.y < -2000)
            mVelocity.y = -2000;
        

        mBoundingBox.set(getX(), getY(), getTexture().getWidth(), getTexture().getHeight());
        
        
	}
	
	@Override
	public boolean handleTileCollision(int tileId)
	{
		switch(tileId)
		{
            case 0:
            	System.out.println("WTF collision: 0");
            	return false;
            case 1:
                mVelocity = new Vector2(0, -1200);
                return false;
            case 2:
                mVelocity = new Vector2(-1200, 0);
                return false;
            case 3:
                mVelocity = new Vector2(1200, 0);
                return false;
            case 4:
                mVelocity = new Vector2(0, 1200);
                return false;
            case 5:
            	//reset();
            	
                return true;
            default:
            	System.out.println("WTF collision: Not Found! ;/");
            	return false;
		
		}
	}
	@Override
	public void handleObjectCollision(Entity other)
	{

		
		
	}
}
