package us.ilite.robot.modules;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj.Talon;
import us.ilite.common.config.SystemSettings;
import us.ilite.common.lib.control.PIDController;

public class Shooter extends Module {
    private Talon mShooterLeft;
    private Talon mShooterRight;
    private boolean spinning;

    private PIDController kShooterPidController = new PIDController(SystemSettings.kShooterGains, 0, SystemSettings.kMaxShooter, SystemSettings.kControlLoopPeriod );

    public Shooter() {
        kShooterPidController.setOutputRange( 0, 1 );
        kShooterPidController.setSetpoint( SystemSettings.kMaxShooter );
    }

    public void activate()
    {
        spinning = true;
    }

    public void deactivate()
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
            mShooterLeft.set( kShooterPidController.calculate( mShooterLeft.getSpeed(), 0 ) );
            mShooterRight.set( kShooterPidController.calculate( mShooterRight.getSpeed(), 0 ) );
        }

    }

    @Override
    public void shutdown(double pNow) {

    }


}
