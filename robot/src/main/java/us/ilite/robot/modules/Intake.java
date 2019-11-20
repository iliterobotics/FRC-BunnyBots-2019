package us.ilite.robot.modules;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import us.ilite.common.config.SystemSettings;


public class Intake extends Module {
    private TalonSRX mIntakeRoller = new TalonSRX(SystemSettings.kIntakeId);
    private EIntakeState mDesiredIntakeState;

    public Intake() {
    }

    @Override
    public void modeInit(double pNow) {
    }

    @Override
    public void periodicInput(double pNow) {

    }

    @Override
    public void update(double pNow) {
        switch (mDesiredIntakeState) {
            case INTAKING:
                mIntakeRoller.set(ControlMode.PercentOutput, SystemSettings.kIntakeOutput);
                break;
            case OUTTAKING:
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
        mDesiredIntakeState = pIntakeState;
    }

    public enum EIntakeState {
        INTAKING,
        STOP,
        OUTTAKING;
    }




}
