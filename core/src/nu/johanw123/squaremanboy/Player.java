package nu.johanw123.squaremanboy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.controllers.mappings.Ouya;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;




public class Player extends Entity
{
    private Vector2 startPosition;
    public Vector2 freeCameraPosition;

	public Player(float x, float y)
	{
		super(x,y, "Player128");

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
					    this.translateX(600 * SRuntime.SCALE_FACTOR * deltaTime);
					}
		            else if(Gdx.input.getX(i) < SRuntime.SCREEN_WIDTH/2)
		            {
		            	if(Gdx.input.getX(i) < 150 && Gdx.input.getY(i) > Gdx.graphics.getHeight() - 100)
		            		continue;
		            	
		                this.translateX(-600 * SRuntime.SCALE_FACTOR * deltaTime);
		            }					
				}
			}
		}

        if(SGame.activeController != null) {
            float axis = SGame.activeController.getAxis(Ouya.AXIS_LEFT_X);
            if (checkAxis(axis)) {
                this.translateX((600 * SRuntime.SCALE_FACTOR * axis) * deltaTime);
            }
        }
		
		if((SGame.activeController != null && SGame.activeController.getAxis(Ouya.AXIS_LEFT_TRIGGER) > 0.1f))
		{
			this.translateX((600 * SRuntime.SCALE_FACTOR * -SGame.activeController.getAxis(Ouya.AXIS_LEFT_TRIGGER)) * deltaTime);
		}
		if((SGame.activeController != null && SGame.activeController.getAxis(Ouya.AXIS_RIGHT_TRIGGER) > 0.1f))
		{
			this.translateX((600 * SRuntime.SCALE_FACTOR * SGame.activeController.getAxis(Ouya.AXIS_RIGHT_TRIGGER)) * deltaTime);
		}

		if(Gdx.input.isKeyPressed(Keys.LEFT) || Gdx.input.isButtonPressed(Ouya.BUTTON_DPAD_LEFT) ||
				(SGame.activeController != null && SGame.activeController.getPov(0) == PovDirection.west))
        {
			this.translateX((-600 * SRuntime.SCALE_FACTOR) * deltaTime);
		}
        if(Gdx.input.isKeyPressed(Keys.RIGHT) || Gdx.input.isButtonPressed(Ouya.BUTTON_DPAD_RIGHT) ||
                (SGame.activeController != null && SGame.activeController.getPov(0) == PovDirection.east))

        {
            this.translateX((600 * SRuntime.SCALE_FACTOR) * deltaTime);

        }
        if(SGame.activeController != null) {
            float axis = SGame.activeController.getAxis(Ouya.AXIS_RIGHT_X);
            if (checkAxis(axis)) {
                freeCameraPosition.x += 600 * deltaTime * SRuntime.SCALE_FACTOR * axis;

                if (freeCameraPosition.x < 0)
                    freeCameraPosition.x = 0;

                if (freeCameraPosition.x > SRuntime.WORLD_WIDTH * SRuntime.TILE_SIZE)
                    freeCameraPosition.x = SRuntime.WORLD_WIDTH * SRuntime.TILE_SIZE;
            }
        }

        if(SGame.activeController != null) {
            float axis = SGame.activeController.getAxis(Ouya.AXIS_RIGHT_Y);
            if (checkAxis(axis)) {
                freeCameraPosition.y -= 600 * deltaTime * SRuntime.SCALE_FACTOR * axis;

                if (freeCameraPosition.y < 0)
                    freeCameraPosition.y = 0;

                if (freeCameraPosition.y > SRuntime.WORLD_HEIGHT * SRuntime.TILE_SIZE)
                    freeCameraPosition.y = SRuntime.WORLD_HEIGHT * SRuntime.TILE_SIZE;
            }
        }

		if(Gdx.input.isKeyPressed(Keys.A))
		{
			freeCameraPosition.x -= 600 * deltaTime * SRuntime.SCALE_FACTOR;
			if(freeCameraPosition.x < 0)
				freeCameraPosition.x = 0;
		}
		if(Gdx.input.isKeyPressed(Keys.D))
		{
			freeCameraPosition.x += 600 * deltaTime * SRuntime.SCALE_FACTOR;
			if(freeCameraPosition.x > SRuntime.WORLD_WIDTH * SRuntime.TILE_SIZE)
				freeCameraPosition.x = SRuntime.WORLD_WIDTH * SRuntime.TILE_SIZE;
		}
		if(Gdx.input.isKeyPressed(Keys.W))
		{
			freeCameraPosition.y += 600 * deltaTime * SRuntime.SCALE_FACTOR;
			if(freeCameraPosition.y > SRuntime.WORLD_HEIGHT * SRuntime.TILE_SIZE)
				freeCameraPosition.y = SRuntime.WORLD_HEIGHT * SRuntime.TILE_SIZE;
		}
		if(Gdx.input.isKeyPressed(Keys.S))
		{
			freeCameraPosition.y -= 600 * deltaTime * SRuntime.SCALE_FACTOR;
			if(freeCameraPosition.y < 0)
				freeCameraPosition.y = 0;
		}


		
	}

    public boolean checkAxis(float axisValue)
    {
        if(SGame.activeController != null &&  (axisValue > 0.3f || axisValue < -0.3f))
            return true;
        return false;
    }
	
	@Override
	public void update(float deltaTime)
	{
        handleInput(deltaTime);
        
        //System.out.println(deltaTime);
        
        if(deltaTime > 0.04f)
        	deltaTime = 0.04f;

        float newX = getX() + mVelocity.x * deltaTime * SRuntime.SCALE_FACTOR;
        float newY = getY() + mVelocity.y * deltaTime * SRuntime.SCALE_FACTOR;
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
