package us.ilite.robot.modules;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.team254.lib.drivers.talon.TalonSRXFactory;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Talon;
import us.ilite.common.config.SystemSettings;
import us.ilite.common.lib.control.PIDController;
import us.ilite.robot.driverinput.DriverInput;

public class Shooter extends Module {
    private TalonSRX mTalonShooter;
    private VictorSPX mVictorShooter;
    private EShooterState mShooterState;
    private double mDesiredOutput;

    private PIDController kShooterPidController = new PIDController(SystemSettings.kShooterGains, 0, SystemSettings.kMaxShooter, SystemSettings.kControlLoopPeriod );

    public enum EShooterState {
        SHOOTING,
        GIVE_TO_HOPPER,
        STOP;
    }

    public Shooter() {
        mTalonShooter = TalonSRXFactory.createDefaultTalon(SystemSettings.kShooterTalonID);
        mVictorShooter = TalonSRXFactory.createPermanentSlaveVictor(SystemSettings.kShooterVictorID, mTalonShooter);
        mVictorShooter.setInverted(true);

        kShooterPidController.setOutputRange( 0, 1 );
        kShooterPidController.setSetpoint( SystemSettings.kMaxShooter );
        mShooterState = EShooterState.STOP;
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
                mDesiredOutput = kShooterPidController.calculate(mTalonShooter.getSelectedSensorVelocity(), pNow);
            case GIVE_TO_HOPPER:
                mDesiredOutput = -1.0;
            case STOP:
                mDesiredOutput = 0.0;
        }

        mTalonShooter.set(ControlMode.PercentOutput, mDesiredOutput);
    }

    public void setShooterState(EShooterState pState) {
        mShooterState = pState;
    }

    @Override
    public void shutdown(double pNow) {

    }
}
