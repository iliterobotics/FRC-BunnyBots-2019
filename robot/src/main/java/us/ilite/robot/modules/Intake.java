package us.ilite.robot.modules;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.team254.lib.drivers.talon.TalonSRXFactory;
import us.ilite.common.config.SystemSettings;


public class Intake extends Module {
    private TalonSRX mIntakeRoller;
    private EIntakeState mDesiredState;
    
    public enum EIntakeState {
        INTAKE,
        STOP,
        OUTTAKE;
    }

    public Intake() {
        mDesiredState = EIntakeState.STOP;
        mIntakeRoller = TalonSRXFactory.createDefaultTalon(SystemSettings.kIntakeTalonId);
    }

    @Override
    public void modeInit(double pNow) {
    }

    @Override
    public void periodicInput(double pNow) {

    }

    @Override
    public void update(double pNow) {
        switch (mDesiredState) {
            case INTAKE:
                mIntakeRoller.set(ControlMode.PercentOutput, SystemSettings.kIntakeTalonPower);
                break;
            case OUTTAKE:
                mIntakeRoller.set(ControlMode.PercentOutput, -SystemSettings.kIntakeTalonPower);
                break;
            case STOP:
                mIntakeRoller.set(ControlMode.PercentOutput, 0d);
                break;
        }
    }

    @Override
    public void shutdown(double pNow) {
        mIntakeRoller.set(ControlMode.PercentOutput, 0d);
    }

    public void setIntakeState(EIntakeState pIntakeState) {
        mDesiredState = pIntakeState;
    }
}
