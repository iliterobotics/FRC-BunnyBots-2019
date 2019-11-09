package us.ilite.robot.modules;

import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import us.ilite.robot.hardware.NeoDriveHardware;

public class Shooter extends Module
{
    private CANSparkMax mMotor;
    private boolean spinning;
    public static final double SHOOTER_MAX_VELOCITY = 1.0;
    public Shooter( CANSparkMax mMotor, Joystick mActivate, Joystick mDeactivate )
    {
        this.mMotor = mMotor;
    }
    @Override
    public void modeInit(double pNow)
    {

    }

    @Override
    public void periodicInput(double pNow)
    {

    }

    @Override
    public void update(double pNow)
    {
        if ( spinning )
        {
            mMotor.set( SHOOTER_MAX_VELOCITY );
        }
    }

    @Override
    public void shutdown(double pNow)
    {

    }

    public void activate()
    {
        spinning = true;
    }

    public void deactivate()
    {
        spinning = false;
    }
}
