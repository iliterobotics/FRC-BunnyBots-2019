package us.ilite.robot.modules;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Talon;
import us.ilite.common.config.SystemSettings;
import us.ilite.common.lib.control.PIDController;
import us.ilite.robot.driverinput.DriverInput;

public class Shooter extends Module {
    private Talon mShooterLeft;
    private Talon mShooterRight;
    private static boolean spinning;

    private PIDController kShooterPidController = new PIDController(SystemSettings.kShooterGains, 0, SystemSettings.kMaxShooter, SystemSettings.kControlLoopPeriod );

    public Shooter() {
        kShooterPidController.setOutputRange( 0, 1 );
        kShooterPidController.setSetpoint( SystemSettings.kMaxShooter );
    }

    public static void activate()
    {
        spinning = true;
    }

    public static void deactivate()
    {
        spinning = false;
    }

    @Override
    public void modeInit(double pNow) {

    }

    @Override
    public void periodicInput(double pNow) {

    }

    @Override
    public void update(double pNow) {
        if ( spinning ) {
            mShooterLeft.set( kShooterPidController.calculate( mShooterLeft.getSpeed(), pNow ) );
            mShooterRight.set( kShooterPidController.calculate( mShooterRight.getSpeed(), pNow ) );
        }
        else {
            mShooterLeft.set(0);
            mShooterRight.set(0);
        }

    }

    @Override
    public void shutdown(double pNow) {

    }

}
