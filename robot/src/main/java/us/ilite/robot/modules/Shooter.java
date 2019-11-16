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
    private EShooterState mShooterState;
    private static boolean spinning;
    private double mDesiredOutput;

    private PIDController kShooterPidController = new PIDController(SystemSettings.kShooterGains, 0, SystemSettings.kMaxShooter, SystemSettings.kControlLoopPeriod );

    private enum EShooterState {
        SHOOTING,
        GIVE_TO_HOPPER,
        STOP;
    }

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
        switch (mShooterState) {
            case SHOOTING:
                mDesiredOutput = kShooterPidController.calculate(mShooterLeft.getSpeed(), pNow);
            case GIVE_TO_HOPPER:
                mDesiredOutput = -1.0;
            case STOP:
                mDesiredOutput = 0.0;
        }

        mShooterLeft.set(mDesiredOutput);
        mShooterRight.set(-mDesiredOutput);
    }

    public void setShooterState(EShooterState pState) {
        mShooterState = pState;
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
