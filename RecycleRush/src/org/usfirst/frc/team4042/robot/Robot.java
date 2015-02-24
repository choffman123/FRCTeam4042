package org.usfirst.frc.team4042.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
//if(fails())
//	try(again());
public class Robot extends IterativeRobot {
	RobotDrive myRobot;
	Joystick genStick;
	double autoLoopCounter, lightCounter, offCounter, topCounter, range, autoPickup;
	SpeedController //liftRight, 
	liftLeft, liftMiddle;
	DriverStation ds;
	Servo red, green, blue;
	Relay redTop, greenTop, blueTop;
	Ultrasonic auto;
	
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	myRobot = new RobotDrive(0,1,2,3);
        myRobot.setExpiration(0.1);
    	genStick = new Joystick(0);
    	liftRight = new Talon(4);
    	liftLeft = new Talon(5);
    	liftMiddle = new Talon(6);
    	red = new Servo(7);
    	green = new Servo(8);
    	blue = new Servo(9);
    	redTop = new Relay(0, Relay.Direction.kForward);
    	greenTop = new Relay(1, Relay.Direction.kForward);
    	blueTop = new Relay(2, Relay.Direction.kForward);
    	auto = new Ultrasonic(0, 1);
    	ds = DriverStation.getInstance();
    }
    
    /**
     * This function is run once each time the robot enters autonomous mode
     */
    public void autonomousInit() {
    	autoLoopCounter = 0;
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
    	//1 Second is 100 loops as the driver station takes 20ms to update, do the math
    	if (autoLoopCounter < 50)//Lift for .5 seconds
    	{
    		liftRight.set(.5);
    		liftLeft.set(.5);
    	}
    	if (autoLoopCounter < 100 && autoLoopCounter > 50)//Stop the lift motors and turn right for .5 seconds
    	{
    		liftLeft.set(0);
    		liftRight.set(0);
    		myRobot.tankDrive(.75, .25);
    	}
    	if(autoLoopCounter < 200 && autoLoopCounter > 100)//Drive forward for 1 second
		{
			myRobot.drive(-0.35, 0.0);
		}
    	if (autoLoopCounter > 300)//Stop
    	{
    		myRobot.drive(0,0);
    	}
		autoLoopCounter++;
    }
    
    /**
     * This function is called once each time the robot enters tele-operated mode
     */
    public void teleopInit(){
    	lightCounter = 0;
    	offCounter = 300;
    	topCounter = 0;
    	range = 0;
    	autoPickup = 0;
    	red.set(0);
    	green.set(0);
    	blue.set(0);
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {    	
    	JoystickIO();
    	bottomLights();
    	topLights();
        Timer.delay(0.005);
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    	LiveWindow.run();
    }
    
    public void disabledInit() {
    	lightCounter = 0;
    	offCounter = 300;
    	topCounter = 0;
    	red.set(0);
    	green.set(0);
    	blue.set(0);
    }
    
    public void disabledPeriodic() {
    	bottomLights();
    	topLights();
    }
    
    public void JoystickIO() 
{
        myRobot.arcadeDrive(genStick);//Driving with the generic stick
        
        if (genStick.getRawButton(1)) {
        	liftRight.set(0.5);
        	liftLeft.set(0.5);
        	liftMiddle.set(.5);
        } else if (genStick.getRawButton(2)) {
        	liftRight.set(-.5);
        	liftLeft.set(-.5);
        	liftMiddle.set(-.5);
        } else
        {
        	liftLeft.set(0);
        	liftRight.set(0);
        	liftMiddle.set(0);
        }
        
        if (genStick.getRawButton(3))
        {
        	autoPickup();
        }
        
        SmartDashboard.putNumber("Voltage", ds.getBatteryVoltage());//Shows the voltage on the smart dashboard from a meter perspective
        SmartDashboard.putNumber("Range (Inches)", auto.getRangeInches());//Shows the range to whatever you are looking at
        SmartDashboard.putNumber("Drive X", genStick.getRawAxis(0));
        SmartDashboard.putNumber("Drive Y", genStick.getRawAxis(1) * -1);
        SmartDashboard.putBoolean("Lifting", genStick.getRawButton(1));
        SmartDashboard.putBoolean("Setting", genStick.getRawButton(2));
        SmartDashboard.putBoolean("Running Auto Pickup:", genStick.getRawButton(3));
        
        SmartDashboard.putBoolean("To Stop Auto-Function press 12 TWICE!", genStick.getRawButton(12));
	}
    public void bottomLights() 
    {
    	if (lightCounter >= 0 && lightCounter <= 300)
    	{
    		red.set((lightCounter/300));
    		green.set(0);
    		blue.set((offCounter/300));
    	}
    	else if (lightCounter > 300 && lightCounter <= 600)
    	{
    		red.set(((offCounter + 300)/300));
    		green.set(((lightCounter - 300)/300));
    		blue.set(0);
    	}
    	else if (lightCounter > 600 && lightCounter <= 900)
    	{
    		red.set(0);
    		green.set(((offCounter + 600)/300));
    		blue.set(((lightCounter - 600)/ 300));
    	}
    	else
    	{
    		offCounter = 300;
    		lightCounter = 0;
    	}
    	lightCounter++;
    	offCounter--;
    	
    	SmartDashboard.putNumber("RED", red.get());
    	SmartDashboard.putNumber("GREEN", green.get());
    	SmartDashboard.putNumber("BLUE", blue.get());
    }
    public void topLights() 
    {
    	if(topCounter > 150 && topCounter <= 450)
    	{
    		redTop.set(Relay.Value.kOn);
    		greenTop.set(Relay.Value.kOff);
    		blueTop.set(Relay.Value.kOff);
    	}
    	else if (topCounter > 450 && topCounter <= 750)
    	{
    		redTop.set(Relay.Value.kOff);
    		greenTop.set(Relay.Value.kOn);
    		blueTop.set(Relay.Value.kOff);
    	}
    	else if (topCounter > 750 && topCounter <= 1050 || topCounter > 0 && topCounter <= 150)
    	{
    		redTop.set(Relay.Value.kOff);
    		greenTop.set(Relay.Value.kOff);
    		blueTop.set(Relay.Value.kOn);
    	}
    	else
    	{
    		topCounter = 0;
    	}
    	topCounter++;
    }
    public void autoPickup() 
    {
    	range = auto.getRangeInches();
    	while (!genStick.getRawButton(12) && auto.isRangeValid() && range > 2.0)
    	{
    		range = auto.getRangeInches();
    		myRobot.tankDrive(range/50, range/50);
    	}
    	myRobot.tankDrive(0, 0);
    	while(!genStick.getRawButton(12) && autoPickup < 50)
    	{
    		liftLeft.set(.5);
    		liftRight.set(-.5);
    		autoPickup++;
    	}
    }
}