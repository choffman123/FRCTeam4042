
package org.usfirst.frc.team4042.robot;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {
    
    SpeedController leftFront = new Talon(1),
                    	leftBack = new Talon(2),
                    	rightFront = new Talon(3),
                    	rightBack = new Talon(4);
    
    RobotDrive drive = new RobotDrive(leftFront, leftBack, rightFront, rightBack);
    
    Joystick xBox = new Joystick(1),
             	Generic = new Joystick(2);

    DriverStation ds = DriverStation.getInstance();
    
    double xboxAxisLeftX = xBox.getRawAxis(1),
               xboxAxisLeftY = xBox.getRawAxis(2),
               xboxAxisRightX = xBox.getRawAxis(4),
               xboxAxisRightY = xBox.getRawAxis(5),
               xboxAxisBack = xBox.getRawAxis(3),
               winchAxisGen = -Generic.getRawAxis(6),
               armHandleGen = Generic.getRawAxis(5),
               genericDriveX = Generic.getRawAxis(1),
               genericDriveY = -Generic.getRawAxis(2);
    
    AnalogInput red_signature = new AnalogInput(0),
    				orange_x = new AnalogInput(1),
    				yellow_y = new AnalogInput(2),
    				green_area = new AnalogInput(3);
    
    int red_signature_Raw,
    		orange_x_Raw,
    		yellow_y_Raw,
    		green_area_Raw;
    
    public void autonomous() 
    {
        
    }
    
    public void operatorControl() {
    	
    	while (isOperatorControl() && isEnabled()) 
    	{
    		updateJoystickValues();
    		updateAnalog();
    	}
    	
    }
    
    public void test() 
    {
    	
    }
    
    public void updateJoystickValues() {
        
        double leftX = xBox.getRawAxis(1),
               leftY = xBox.getRawAxis(2),
               rightX = xBox.getRawAxis(4),
               rightY = xBox.getRawAxis(5),
               back = xBox.getRawAxis(3),
               winch = -Generic.getRawAxis(6),
               arm = Generic.getRawAxis(5),
               x = Generic.getRawAxis(1),
               y = -Generic.getRawAxis(2);;
        
        xboxAxisBack = back;
        xboxAxisRightY = rightY;
        xboxAxisRightX = rightX;
        xboxAxisLeftY = leftY;
        xboxAxisLeftX = leftX;
        winchAxisGen = winch;
        armHandleGen = arm;
        genericDriveX = x;
        genericDriveY = y;

		drive.arcadeDrive(xboxAxisLeftY + genericDriveY, xboxAxisLeftX + genericDriveX);
    }
    
    public void updateAnalog() {
    	
    	red_signature_Raw = red_signature.getValue();
    	orange_x_Raw = orange_x.getValue();
    	yellow_y_Raw = yellow_y.getValue();
    	green_area_Raw = (green_area.getValue() * 4);
    	
    	SmartDashboard.putNumber("Red", red_signature_Raw);
    	SmartDashboard.putNumber("Orange", orange_x_Raw);
    	SmartDashboard.putNumber("Yellow", yellow_y_Raw);
    	SmartDashboard.putNumber("Green", green_area_Raw);
    }
}
