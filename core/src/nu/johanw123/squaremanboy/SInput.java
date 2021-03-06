package nu.johanw123.squaremanboy;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.controllers.mappings.Ouya;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by johanw123 on 3/14/14.
 */

interface SKeyboardListener
{
	public boolean keyDown(int keycode);
	public boolean keyUp(int keycode);
}
interface SGamepadListener
{
	public boolean povMoved(Controller controller, int povIndex, PovDirection value);
    public boolean axisMoved(Controller controller, int axisIndex, float value);
    public boolean buttonDown(Controller controller, int buttonIndex);
}
interface STouchListener
{
	public boolean touchDown(int x, int y, int pointer, int button);
	public boolean touchUp(int x, int y, int pointer, int button);
    boolean touchDragged (int x, int y, int pointer);
}

public class SInput implements InputProcessor, ControllerListener
{
	   static ArrayList<SKeyboardListener> keyboardListeners = new ArrayList<SKeyboardListener>();
	   static ArrayList<SGamepadListener> gamepadListeners = new ArrayList<SGamepadListener>(); 
	   static ArrayList<STouchListener> touchListeners = new ArrayList<STouchListener>(); 
	   
	   public static void addKeyboardListener(SKeyboardListener keyboardListener){
		   keyboardListeners.add(0, keyboardListener);
	   }
	   public static void addGamepadListener(SGamepadListener gamepadListener){
		   gamepadListeners.add(0, gamepadListener);
	   }
	   public static void addTouchListener(STouchListener touchListener){
		   touchListeners.add(0, touchListener);
	   }	   
	   public static void removeKeyboardListener(SKeyboardListener keyboardListener) {
		   keyboardListeners.remove(keyboardListener);
	    }
	   public static void removeGamepadListener(SGamepadListener gamepadListener) {
		   gamepadListeners.remove(gamepadListener);
	    }
	   public static void removeTouchListener(STouchListener touchListener) {
		   touchListeners.remove(touchListener);
	    }
	   	  
	   @Override
	   public boolean keyDown (int keycode) {
		   for (SKeyboardListener hl : keyboardListeners)
	        {
	            if(hl.keyDown(keycode))
	            	break;
	        }
		   return false;
	   }

	   @Override
	   public boolean keyUp (int keycode) {
		   for (SKeyboardListener hl : keyboardListeners)
	        {
	            if(hl.keyUp(keycode))
	            	break;
	        }
	      return false;
	   }

	   @Override
	   public boolean keyTyped (char character) {
	      return false;
	   }

	   @Override
	   public boolean touchDown (int x, int y, int pointer, int button) {
		  for (STouchListener hl : touchListeners)
	        {
	            if(hl.touchDown(x, y, pointer, button))
	            	break;
	        }
	      return false;
	   }

	   @Override
	   public boolean touchUp (int x, int y, int pointer, int button) {
		   for (STouchListener hl : touchListeners)
	        {
	            if(hl.touchUp(x, y, pointer, button))
	            	break;
	        }
	      return false;
	   }

	   @Override
	   public boolean touchDragged (int x, int y, int pointer) {
		   
	      return false;
	   }

	   @Override
	   public boolean scrolled (int amount) {
	      return false;
	   }

		@Override
		public boolean mouseMoved(int screenX, int screenY) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean accelerometerMoved(Controller arg0, int arg1,
				Vector3 arg2) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean axisMoved(Controller controller, int axisIndex, float value) {
			for (SGamepadListener hl : gamepadListeners)
	        {
	            if(hl.axisMoved(controller, axisIndex, value))
	            	break;
	        }

			return false;
		}

		@Override
		public boolean buttonDown(Controller controller, int buttonIndex) {

            for (Controller c : SGame.foundControllers) {
                c.removeListener(SGame.sInput);
            }

            SGame.activeController = controller;
            SGame.activeController.addListener(SGame.sInput);

			for (SGamepadListener hl : gamepadListeners)
	        {
	            if(hl.buttonDown(controller, buttonIndex))
	            	break;
	        }

			return false;
		}

		@Override
		public boolean buttonUp(Controller arg0, int arg1) {
			// TODO Auto-generated method stub
			return false;
		}
		@Override
		public void connected(Controller arg0) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void disconnected(Controller arg0) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public boolean povMoved(Controller controller, int povIndex, PovDirection value) {
			for (SGamepadListener hl : gamepadListeners)
	        {
	            if(hl.povMoved(controller, povIndex, value))
	            	break;
	        }
			
			return false;
		}
		@Override
		public boolean xSliderMoved(Controller arg0, int arg1, boolean arg2) {
			// TODO Auto-generated method stub
			return false;
		}
		@Override
		public boolean ySliderMoved(Controller arg0, int arg1, boolean arg2) {
			// TODO Auto-generated method stub
			return false;
		}


    public enum eControllerButtons
        {
            Menu,
            Menu_Accept,
            Menu_Back,
            CameraToggle,
            Menu_Left,
            Menu_Right,
            Menu_Up,
            Menu_Down




        }

        public enum eControllerAxis
        {
            Menu_Move_X,
            Menu_Move_Y,
            Camera_Move_X,
            Camera_Move_Y,
            Game_Move_X,
            Game_Move_Y

        }

        public static boolean getButtonIndexMatch(int buttonIndex, eControllerButtons controllerButton)
        {
            int acceptedIndexes;
            List<Integer> indexList = new ArrayList<Integer>();
            switch(controllerButton)
            {
                case Menu_Accept:
                    if(Ouya.runningOnOuya) {
                        indexList.add(Ouya.BUTTON_O);
                    }
                    else {
                        indexList.add(Xbox360Pad.BUTTON_A);
                        indexList.add(Xbox360Pad.BUTTON_START);
                    }
                    break;
                case Menu_Back:
                    if(Ouya.runningOnOuya) {
                        indexList.add(Ouya.BUTTON_A);
                    }
                    else {
                        indexList.add(Xbox360Pad.BUTTON_B);
                        indexList.add(Xbox360Pad.BUTTON_BACK);
                    }
                    break;
                case CameraToggle:
                    if(Ouya.runningOnOuya) {
                        indexList.add(Ouya.BUTTON_U);
                    }
                    else {
                        indexList.add(Xbox360Pad.BUTTON_X);
                    }
                    break;
                case Menu:
                    if(Ouya.runningOnOuya) {
                        indexList.add(82); //Ouya Button Menu
                    }
                    else {
                        indexList.add(Xbox360Pad.BUTTON_START);
                    }
                    break;
                case Menu_Left:
                    if(Ouya.runningOnOuya) {
                        indexList.add(Ouya.BUTTON_DPAD_LEFT);
                    }
                    break;
                case Menu_Right:
                    if(Ouya.runningOnOuya) {
                        indexList.add(Ouya.BUTTON_DPAD_RIGHT);
                    }
                    break;
                case Menu_Up:
                    if(Ouya.runningOnOuya) {
                        indexList.add(Ouya.BUTTON_DPAD_UP);
                    }
                    break;
                case Menu_Down:
                    if(Ouya.runningOnOuya) {
                        indexList.add(Ouya.BUTTON_DPAD_DOWN);
                    }
                    break;

            }

            for (int i = 0; i < indexList.size(); i++) {
                if(buttonIndex == indexList.get(i))
                    return true;
            }

            return false;
        }

        public static float getAxisValue(int index)
        {
            return SGame.activeController.getAxis(index);
        }
		
		//Polling methods
		public static boolean isKeyDown(int key)
		{
			return Gdx.input.isKeyPressed(key);
		}
		
		
		
		
	}

