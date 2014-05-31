package nu.johanw123.squaremanboy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Vector;

public class Entity extends Sprite
{
    protected Vector2 mVelocity = Vector2.Zero;
    protected Rectangle mBoundingBox = new Rectangle(0,0,0,0);
    protected static SGame game;
	
	public Entity(float x, float y, String texName)
	{
		setTexture(new Texture(Gdx.files.internal("data/sprites/" + texName  + ".png")));
		setPosition(x, y);
	}

    public static void Init(SGame _game)
    {
        game = _game;
    }
	
	public String getType()
	{
		return "Null";
	}
	
	public void render(SpriteBatch batch)
	{
		batch.draw(this.getTexture(), this.getX(), this.getY(), 32, 32);
	}
	
	public void update(float deltaTime)
	{
		
	}
	
	public boolean handleTileCollision(int tileId)
	{
		return false;
	}

	public void handleObjectCollision(Entity other)
	{
	
	}

    public Vector2 getPosition() {
        return new Vector2(getX(), getY());
    }
    public void setPosition(Vector2 newPosition)
    {
        setPosition(newPosition.x, newPosition.y);
    }

    public Vector2 getVelocity() {
        return mVelocity;
    }

    public void setVelocity(Vector2 mVelocity) {
        this.mVelocity = mVelocity;
    }

    public Rectangle getBoundingBox() {
        return mBoundingBox;
    }

    public void setBoundingBox(Rectangle mBoundingBox) {
        this.mBoundingBox = mBoundingBox;
    }
}
