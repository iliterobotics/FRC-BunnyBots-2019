package us.ilite.robot.modules;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.team254.lib.drivers.talon.TalonSRXFactory;
import us.ilite.common.config.SystemSettings;
import us.ilite.common.lib.control.PIDController;
import us.ilite.robot.driverinput.DriverInput;

public class Shooter extends Module {
    private TalonSRX mTalon;
    private VictorSPX mVictor;
    private PIDController kShooterPidController;
    private EShooterState mShooterState;
    private double mDesiredOutput;

    public enum EShooterState {
        SHOOTING,
        CLEAN,
        STOP;
    }

    public Shooter() {
        mTalon = TalonSRXFactory.createDefaultTalon(SystemSettings.kShooterTalonId);
        mVictor = TalonSRXFactory.createPermanentSlaveVictor(SystemSettings.kShooterVictorId, mTalon);
        mVictor.setInverted(true);

        kShooterPidController = new PIDController(SystemSettings.kShooterGains, 0, SystemSettings.kMaxShooterVelocity, SystemSettings.kControlLoopPeriod );
        kShooterPidController.setOutputRange( 0, 1 );
        kShooterPidController.setSetpoint( SystemSettings.kMaxShooterVelocity );
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
                mDesiredOutput = kShooterPidController.calculate(mTalon.getSelectedSensorVelocity(), pNow);
                break;
            case CLEAN:
                mDesiredOutput = -SystemSettings.kShooterTalonPower;
                break;
            case STOP:
                mDesiredOutput = 0d;
                break;
        }

        mTalon.set(ControlMode.PercentOutput, mDesiredOutput);
    }

    public void setShooterState(EShooterState pState) {
        mShooterState = pState;
    }

    @Override
    public void shutdown(double pNow) {
        mTalon.set(ControlMode.PercentOutput, 0d);
    }

    public boolean isMaxVelocity() {
        return mTalon.getSelectedSensorVelocity() == SystemSettings.kMaxShooterVelocity;
    }
}
