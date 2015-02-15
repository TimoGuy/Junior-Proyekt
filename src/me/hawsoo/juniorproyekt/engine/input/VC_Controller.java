package me.hawsoo.juniorproyekt.engine.input;


import org.lwjgl.input.Controller;
import org.lwjgl.input.Controllers;

public class VC_Controller extends VirtualController
{
	// Bindings
	private Controller joystick;
	private static final float DEADZONE = 0.5f;		// 50% radius deadzone
	
	private int buttonLeft;
	private int buttonRight;
	private int buttonUp;
	private int buttonDown;
	
	private int buttonJump;
	private int buttonAttack;
	private int buttonItem;
	
	/**
	 * Inputs all the bindings. Each argument is corresponding to the
	 * key in the class <code>VirtualController</code>.
	 * @param keyLeft
	 * @param keyRight
	 * @param keyUp
	 * @param keyDown
	 * @param keyJump
	 * @param keyAttack
	 * @param keyItem
	 * @see me.hawsoo.juniorproyekt.engine.input.VirtualController
	 */
	public VC_Controller(/*int buttonLeft, int buttonRight, int buttonUp, int buttonDown, int buttonJump, int buttonAttack, int buttonItem*/)
	{
		// Find controller
		String controllerName = "";
		boolean controllerfound = false;
		for (net.java.games.input.Controller controller : net.java.games.input.ControllerEnvironment.getDefaultEnvironment().getControllers())
		{
			// If controller is a gamepad...
			if (controller.getType() == net.java.games.input.Controller.Type.GAMEPAD)
			{
				// Add controller
//				joystick = controller;
				System.out.println("> Controller connected:\n\t" + (controllerName = controller.getName()));
				
				// Break
				controllerfound = true;
				break;
			}
		}
		
		// Find the lwjgl register of the controller
		if (controllerfound)
		{
			// Connect controller thru lwjgl controller
			for (int i = 0; i < Controllers.getControllerCount(); i++)
			{
				Controller c = Controllers.getController(i);
				if (c.getName().equals(controllerName))
				{
					joystick = c;
					break;
				}
			}
		}
		
		// Set the controller bindings
		setBindings(buttonLeft, buttonRight, buttonUp, buttonDown, buttonJump, buttonAttack, buttonItem);
	}
	
	/**
	 * Inputs all the bindings. Each argument is corresponding to the
	 * key in the class <code>VirtualController</code>.
	 * @param keyLeft
	 * @param keyRight
	 * @param keyUp
	 * @param keyDown
	 * @param keyJump
	 * @param keyAttack
	 * @param keyItem
	 * @see me.hawsoo.juniorproyekt.engine.input.VirtualController
	 */
	public void setBindings(int buttonLeft, int buttonRight, int buttonUp, int buttonDown, int buttonJump, int buttonAttack, int buttonItem)
	{
		this.buttonLeft 	= buttonLeft;
		this.buttonRight 	= buttonRight;
		this.buttonUp 		= buttonUp;
		this.buttonDown 	= buttonDown;
		
		this.buttonJump 	= buttonJump;
		this.buttonAttack 	= buttonAttack;
		this.buttonItem 	= buttonItem;
	}

	@Override
	public void pumpInput()
	{
		joystick.poll();
		
		// Get axis input
		int xAxis = 0;
		int yAxis = 0;
		
		// Analog joysticks (first two) input, if any
		for (int i = 0; i < Math.min(joystick.getAxisCount(), 2); i++)
		{
			// If even number axis, then it is vertical
			if (i % 2 == 0)
			{
				float val = joystick.getAxisValue(i);
				if (Math.abs(val) >= DEADZONE)
				{
					yAxis = (int)Math.signum(val);
				}
			}
			// If odd number axis, then it is horizontal
			else
			{
				float val = joystick.getAxisValue(i);
				if (Math.abs(val) >= DEADZONE)
				{
					xAxis = (int)Math.signum(val);
				}
			}
		}
		
		// D-pad input
		if (Math.abs(joystick.getPovX()) >= DEADZONE)
		{
			xAxis = (int)Math.signum(joystick.getPovX());
		}
		
		if (Math.abs(joystick.getPovY()) >= DEADZONE)
		{
			yAxis = (int)Math.signum(joystick.getPovY());
		}
		
		// Update key inputs
		left = xAxis < 0;
		right = xAxis > 0;
		up = yAxis < 0;
		down = yAxis > 0;
		
		jump = joystick.isButtonPressed(0);			// 'A' button
		attack = joystick.isButtonPressed(1);		// 'B' button
		item = joystick.isButtonPressed(2);			// 'X' button
	}
	
}