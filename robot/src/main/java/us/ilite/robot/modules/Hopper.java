package us.ilite.robot.modules;
import us.ilite.common.config.SystemSettings;
import com.revrobotics.CANSparkMax;


public class Hopper extends Module {
    private HopperState kHopperState;
    private CANSparkMax mHopperMotor;
    private int mMotorInt;
    //Enum for the current state of the hopper
    public enum HopperState
    {
        GIVETOSHOOTER(1.0),
        REVERSE(1.0),
        NORMALSTATE(1.0),
        STOP(0.0);

        private double power;

        HopperState(double pow ) {
            power = pow;
        }
    }
    public Hopper( CANSparkMax pHopperMotor)
    {

    }

    {
    }
    @Override
    public void modeInit(double pNow) {

    }
    @Override
    public void periodicInput(double pNow) {

    }
    @Override
    public void update(double pNow) {

    }
    @Override
    public void shutdown(double pNow) {
        kHopperState = HopperState.STOP;
    }
    public void ballsJammedInHopper ()
    {
        kHopperState = HopperState.REVERSE;
    }
    public void giveBallsToShooter ()
    {
        kHopperState = HopperState.GIVETOSHOOTER;
    }
    public void normalHopperMethod()
    {
        kHopperState = HopperState.NORMALSTATE;
    }



}
