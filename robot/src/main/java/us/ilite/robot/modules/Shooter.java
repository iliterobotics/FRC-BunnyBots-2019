package us.ilite.robot.modules;

import com.flybotix.hfr.codex.Codex;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Talon;
import us.ilite.common.config.SystemSettings;
import us.ilite.common.lib.control.PIDController;
import us.ilite.robot.driverinput.DriverInput;

public class Shooter extends Module {
    private Talon mShooterLeft;
    private Talon mShooterRight;
    private EShootingState mShootingState;
    private static boolean spinning;

    private PIDController kShooterPidController = new PIDController(SystemSettings.kShooterGains, 0, SystemSettings.kMaxShooter, SystemSettings.kControlLoopPeriod );

    public Shooter() {
        kShooterPidController.setOutputRange( 0, 1 );
        kShooterPidController.setSetpoint( SystemSettings.kMaxShooter );
        mShootingState = EShootingState.STOP;
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
//        if ( spinning ) {
//            mShooterLeft.set( kShooterPidController.calculate( mShooterLeft.getSpeed(), pNow ) );
//            mShooterRight.set( kShooterPidController.calculate( mShooterRight.getSpeed(), pNow ) );
//        }
//        else {
//            mShooterLeft.set(0);
//            mShooterRight.set(0);
//        }

        switch (mShootingState) {
            
        }

    }

    @Override
    public void shutdown(double pNow) {

    }

    public boolean getSpinningStatus()
    {
        return spinning;
    }

    public enum EShootingState
    {
        FORWARD( 1.0 ),
        BACKWARD( -1.0 ),
        STOP( 0.0 );

        private double mDesiredPower;

        EShootingState( double pDesiredPower )
        {
            mDesiredPower = pDesiredPower;
        }

        public double getDesiredPower()
        {
            return mDesiredPower;
        }

    }

    public void setEShootingState( EShootingState pShootingState )
    {
        mShootingState = pShootingState;
    }
}
