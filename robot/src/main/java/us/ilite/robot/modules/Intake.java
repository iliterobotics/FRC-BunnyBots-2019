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
        mIntakeRoller = TalonSRXFactory.createDefaultTalon(4/*SystemSettings.kIntakeId*/);
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
                mIntakeRoller.set(ControlMode.PercentOutput, SystemSettings.kIntakeOutput);
                break;
            case OUTTAKE:
                mIntakeRoller.set(ControlMode.PercentOutput, -SystemSettings.kIntakeOutput);
                break;
            case STOP:
                mIntakeRoller.set(ControlMode.PercentOutput, 0.0 );
                break;
        }
    }

    @Override
    public void shutdown(double pNow) {
        mIntakeRoller.set(ControlMode.PercentOutput, 0.0);
    }

    public void setIntakeState(EIntakeState pIntakeState) {
        mDesiredState = pIntakeState;
    }
}
