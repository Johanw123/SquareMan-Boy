package nu.johanw123.squaremanboy;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.controllers.mappings.Ouya;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.TimeUtils;

public class ButtonHandler implements SKeyboardListener, SGamepadListener, STouchListener
{
	private int selectedButton = -1;
    private int menuBackButton = -1;
	private ArrayList<TextButton> buttons;
	
	private TextButtonStyle textButtonStyle;
	private Skin skin;
	
	private final int StandardButtonWidth = 600;
	private final int StandardButtonHeight = 70;
	private final int StandardSpacing = 90;
	
	public ButtonHandler()
	{
		buttons = new ArrayList<TextButton>();

		skin = new Skin();

        Pixmap pixmap = new Pixmap(32, 32, Format.RGBA8888);
        pixmap.setColor(Color.GREEN);        
        pixmap.fill();
 
        skin.add("white", new Texture(pixmap));
        
 
        BitmapFont bfont=new BitmapFont();
        bfont.scale(1);
        skin.add("default",SGame.font);
    
        textButtonStyle = new TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.checked = skin.newDrawable("white", Color.LIGHT_GRAY);
        textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
        
        textButtonStyle.font = SGame.font;
       
        skin.add("default", textButtonStyle);
        
	}
	
	public void addListeners()
	{
		SInput.addKeyboardListener(this);
		SInput.addGamepadListener(this);
		SInput.addTouchListener(this);
	}
	
	public void clearListeners()
	{
		SInput.removeKeyboardListener(this);
		SInput.removeGamepadListener(this);
		SInput.removeTouchListener(this);
	}
	
	private TextButton createButton(float x, float y, int width, int height, String buttonText)
	{
		TextButton button = new TextButton(buttonText, textButtonStyle);
		button.setPosition(x, y);
		button.setSize(width, height);
		button.setName(buttonText);
		return button;
	}
		
	public TextButton createButton(String buttonText, EventListener activateEvent, int width, int height)
	{	
		int spacing = StandardSpacing;
		float x = SRuntime.SCREEN_WIDTH /2 - StandardButtonWidth / 2;
		float y = SRuntime.SCREEN_HEIGHT - 150 - spacing * buttons.size();
        
        return createButton(buttonText, activateEvent, x, y, width, height);
	}
	public TextButton createButton(String buttonText, EventListener activateEvent, float x, float y)
	{	
		int width = StandardButtonWidth;
        int height = StandardButtonHeight;
        
        return createButton(buttonText, activateEvent, x, y, width, height);
	}	
	
	public TextButton createButton(String buttonText, EventListener activateEvent)
	{		
		int spacing = StandardSpacing;
		float x = SRuntime.SCREEN_WIDTH /2 - StandardButtonWidth / 2;
		float y = SRuntime.SCREEN_HEIGHT - 150 - spacing * buttons.size();
		int width = StandardButtonWidth;
        int height = StandardButtonHeight;
        
        return createButton(buttonText, activateEvent, x, y, width, height);
	}
	
	public TextButton createButton(String buttonText, final EventListener activateEvent, float x, float y, int width, int height)
	{	
		return createButton(buttonText, activateEvent, x, y, width, height, SGame.stage);
	}
	
	public TextButton createButton(String buttonText, EventListener activateEvent, Stage stage)
	{		
		int spacing = StandardSpacing;
		float x = SRuntime.SCREEN_WIDTH /2 - StandardButtonWidth / 2;
		float y = SRuntime.SCREEN_HEIGHT - 150 - spacing * buttons.size();
		int width = StandardButtonWidth;
        int height = StandardButtonHeight;
        
        return createButton(buttonText, activateEvent, x, y, width, height, stage);        
	}
	
	public TextButton createButton(String buttonText, final EventListener activateEvent, float x, float y, int width, int height, Stage stage)
	{	
		final TextButton button = createButton(x, y, width, height, buttonText);		               
        
        button.addListener(activateEvent);
        button.addListener(new ClickListener()
        {
        	@Override
        	public void clicked(InputEvent event, float x, float y) {
        		button.fire(new ButtonActivatedEvent());
        		super.clicked(event, x, y);
        	}
        });               
        
        stage.addActor(button);
        registerButton(button);
		return button;
	}
	public void clearButtons()
	{
        buttons.clear();
        selectedButton = -1;
        menuBackButton = -1;
	}
	public TextButton getButton(String buttonName)
	{
		TextButton bButton = null;
		
		for(TextButton button : buttons)
		{
			if(buttonName == button.getName())
				bButton = button;			
		}				
		return bButton;
	}
	
	public void registerButton(TextButton button)
	{
		buttons.add(button);
		
		
		//button.setChecked(true);
		//buttons.get(0).setChecked(true);	       
	}
	
	public void disableButton(String buttonName)
	{
		getButton(buttonName).setDisabled(true);
		getButton(buttonName).setVisible(false);		
	}
	
	public void enableButton(String buttonName)
	{		
		getButton(buttonName).setDisabled(false);
		getButton(buttonName).setVisible(true);
	}

	public void setMenuBackButton(int buttonId)
    {
        if(buttonId < 0)
            buttonId = 0;
        else if (buttonId > buttons.size() -1)
            buttonId = buttons.size() -1;

        menuBackButton = buttonId;
    }

    public boolean setMenuBackButton(String buttonName)
    {
        if(SGame.CurrentPlatform == SGame.ePlatform.Android)
            return true;

        boolean found = false;
        int foundValue = -1;

        for(int i = 0; i < buttons.size(); ++i)
        {
            if(buttonName == buttons.get(i).getName())
            {
                foundValue = i;
                found = true;
            }
        }

        if(found)
        {
            menuBackButton = foundValue;
        }

        return found;
    }

	public void setSelectedButton(int buttonId)
	{
		if(SGame.CurrentPlatform == SGame.ePlatform.Android)
			return;
		
		
		if(selectedButton < buttons.size())
            if(selectedButton != -1)
			    buttons.get(selectedButton).setChecked(false);
		
		if(buttonId < 0)
			buttonId = 0;
		else if (buttonId > buttons.size() -1)
			buttonId = buttons.size() -1;
		
		selectedButton = buttonId;
		
		buttons.get(selectedButton).setChecked(true);		
	}
	
	public boolean setSelectedButton(String buttonName)
	{	
		if(SGame.CurrentPlatform == SGame.ePlatform.Android)
			return true;
		
		boolean found = false;
		int foundValue = -1;
					
		for(int i = 0; i < buttons.size(); ++i)
		{
			if(buttonName == buttons.get(i).getName())
			{
				foundValue = i;
				found = true;
			}			
		}
		
		if(found)
		{
            if(selectedButton != -1)
			    buttons.get(selectedButton).setChecked(false);
			selectedButton = foundValue;
			buttons.get(selectedButton).setChecked(true);
		}
		
		return found;
	}
	
	void fireActivateButtonEvent()
	{		
		if(!buttons.get(selectedButton).isDisabled())
			buttons.get(selectedButton).fire(new ButtonActivatedEvent());
	}

    void fireMenuBackEvent()
    {
        if(menuBackButton != -1) {
            if (!buttons.get(menuBackButton).isDisabled())
                buttons.get(menuBackButton).fire(new ButtonActivatedEvent());
        }
    }

    public void menuUp()
    {
        int oldBtn = selectedButton;
        int newBtn = oldBtn - 1;

        if(newBtn < 0)
            newBtn = 0;

        if(buttons.get(newBtn).isDisabled())
            return;

        buttons.get(oldBtn).setChecked(false);
        buttons.get(newBtn).setChecked(true);

        selectedButton = newBtn;
    }

    public void menuDown()
    {
        int oldBtn = selectedButton;
        int newBtn = oldBtn + 1;

        if(newBtn > buttons.size() -1)
            newBtn = buttons.size() -1;

        if(buttons.get(newBtn).isDisabled())
            return;

        buttons.get(oldBtn).setChecked(false);
        buttons.get(newBtn).setChecked(true);

        selectedButton = newBtn;
    }
	
	@Override
	public boolean keyDown(int keycode) {
		
		if(keycode == Keys.ENTER || keycode == Keys.SPACE)
		{
			fireActivateButtonEvent();
		
	        return true;
		}
        else if(keycode == Keys.ESCAPE || keycode == Keys.BACKSPACE)
        {
            fireMenuBackEvent();

            return true;
        }
		else if(keycode == Keys.UP || keycode == Keys.LEFT)
		{
			menuUp();
			
			return true;
		}
		else if(keycode == Keys.DOWN || keycode == Keys.RIGHT)
		{
			menuDown();
			
			return true;
		}
		
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		
		
		
		return false;
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		
		return false;
	}

	@Override
	public boolean povMoved(Controller controller, int povIndex, PovDirection value) {
		 switch(value)
		 {
			case north:
             case west:
				menuUp();
				break;
			case south:
             case east:
				menuDown();
				break;
			default:
				break;	
		  }
		 
		return false;
	}

    public void update(float delta)
    {
        if(SGame.activeController != null)
            updateAxis();
    }

    private void updateAxis(float axis)
    {
        if(timeSinceLast > 100)
        {
            if (axis > 0.5f) {
                menuDown();
                timeSinceLast = 0;
            } else if (axis < -0.5f) {
                menuUp();
                timeSinceLast = 0;
            }
        }
    }

    private void updateAxis()
    {
        timeSinceLast += TimeUtils.timeSinceMillis(lastTime);
        lastTime = TimeUtils.millis();

        updateAxis(SInput.getAxisValue(Ouya.AXIS_LEFT_X));
        updateAxis(SInput.getAxisValue(Ouya.AXIS_LEFT_Y));
        //updateAxis(SGame.controller.getAxis(Ouya.AXIS_LEFT_X));
        //updateAxis(SGame.controller.getAxis(Ouya.AXIS_LEFT_Y));
    }

    private long timeSinceLast = 0;
    private long lastTime = 0;
	@Override
	public boolean axisMoved(Controller controller, int axisIndex, float value) {
/*
        timeSinceLast += TimeUtils.timeSinceMillis(lastTime);
        lastTime = TimeUtils.millis();
        System.out.println(timeSinceLast);

        if(timeSinceLast > 50)
        {
            if (axisIndex == Ouya.AXIS_LEFT_X || axisIndex == Ouya.AXIS_LEFT_Y) //1 is left stick y axis, 0 is x axis
            {
                if (value > 0.5f) {
                    menuDown();
                    timeSinceLast = 0;
                    return true;
                } else if (value < -0.5f) {
                    menuUp();
                    timeSinceLast = 0;
                    return true;
                }

            }
        }
*/
		return false;
	}

	@Override
	public boolean buttonDown(Controller controller, int buttonIndex) {
        if(SInput.getButtonIndexMatch(buttonIndex, SInput.eControllerButtons.Menu_Accept))
		//if(buttonIndex == 0 || buttonIndex == 7 || buttonIndex == Ouya.BUTTON_O || buttonIndex == Ouya.BUTTON_MENU) //0 is A, 7 is start
		{
			fireActivateButtonEvent();
			return true;
		}
        if(SInput.getButtonIndexMatch(buttonIndex, SInput.eControllerButtons.Menu_Back))
        //if(buttonIndex == 1 || buttonIndex == 6 || buttonIndex == Ouya.BUTTON_A) //1 is B, 6 is back
        {
            fireMenuBackEvent();
            return true;
        }

        if(SInput.getButtonIndexMatch(buttonIndex, SInput.eControllerButtons.Menu_Up)
                || SInput.getButtonIndexMatch(buttonIndex, SInput.eControllerButtons.Menu_Left))
        //if(buttonIndex == Ouya.BUTTON_DPAD_UP || buttonIndex == Ouya.BUTTON_DPAD_LEFT)
		{
			menuUp();
			return true;
		}
        if(SInput.getButtonIndexMatch(buttonIndex, SInput.eControllerButtons.Menu_Down)
                || SInput.getButtonIndexMatch(buttonIndex, SInput.eControllerButtons.Menu_Right))
		//if(buttonIndex == Ouya.BUTTON_DPAD_DOWN || buttonIndex == Ouya.BUTTON_DPAD_RIGHT)
		{
			menuDown();
			return true;
		}
		
		return false;
	}
	
	
	
	
	
}
