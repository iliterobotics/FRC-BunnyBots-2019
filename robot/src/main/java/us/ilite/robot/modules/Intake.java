package us.ilite.robot.modules;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.team254.lib.drivers.talon.TalonSRXFactory;
import us.ilite.common.config.SystemSettings;


public class Intake extends Module {

    private VictorSPX mVictor;
    private EIntakeState mIntakeState;
    
    public enum EIntakeState {
        INTAKE,
        STOP,
        OUTTAKE;
    }

    public Intake() {
        mIntakeState = EIntakeState.STOP;
        mVictor = TalonSRXFactory.createDefaultVictor(SystemSettings.kIntakeVictorId);
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
                mVictor.set(ControlMode.PercentOutput, SystemSettings.kIntakeVictorPower);
                break;
            case OUTTAKE:
                mVictor.set(ControlMode.PercentOutput, -SystemSettings.kIntakeVictorPower);
                break;
            case STOP:
                mVictor.set(ControlMode.PercentOutput, 0d);
                break;
        }
    }

    @Override
    public void shutdown(double pNow) {
        mVictor.set(ControlMode.PercentOutput, 0d);
    }

    public void setIntakeState(EIntakeState pIntakeState) {
        mIntakeState = pIntakeState;
    }
}
