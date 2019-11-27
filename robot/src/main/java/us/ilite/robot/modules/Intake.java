package us.ilite.robot.modules;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.team254.lib.drivers.talon.TalonSRXFactory;
import us.ilite.common.config.SystemSettings;


public class Intake extends Module {

    private TalonSRX mTalon;
    private EIntakeState mIntakeState;
    
    public enum EIntakeState {
        INTAKE,
        STOP,
        OUTTAKE;
    }

    public Intake() {
        mIntakeState = EIntakeState.STOP;
        mTalon = TalonSRXFactory.createDefaultTalon(SystemSettings.kIntakeTalonId);
    }

    @Override
    public void modeInit(double pNow) {
    }

    @Override
    public void periodicInput(double pNow) {

    }

    @Override
    public void update(double pNow) {
        switch (mIntakeState) {
            case INTAKE:
                mTalon.set(ControlMode.PercentOutput, SystemSettings.kIntakeTalonPower);
                break;
            case OUTTAKE:
                mTalon.set(ControlMode.PercentOutput, -SystemSettings.kIntakeTalonPower);
                break;
            case STOP:
                mTalon.set(ControlMode.PercentOutput, 0d);
                break;
        }
    }

    @Override
    public void shutdown(double pNow) {
        mTalon.set(ControlMode.PercentOutput, 0d);
    }

    public void setIntakeState(EIntakeState pIntakeState) {
        mIntakeState = pIntakeState;
    }
}
