package us.ilite.robot.modules;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.flybotix.hfr.util.log.ILog;
import com.flybotix.hfr.util.log.Logger;
import com.team254.lib.drivers.talon.TalonSRXFactory;
import us.ilite.common.config.SystemSettings;


public class Intake extends Module {

    private TalonSRX mTalon;
    private EIntakeState mIntakeState;
    private ILog mLog = Logger.createLog(Intake.class);
    // private Hopper mHopper;
    public enum EIntakeState {
        INTAKE(SystemSettings.kIntakeTalonPower),
        STOP(0),
        REVERSE(-SystemSettings.kIntakeTalonPower);

        double mPower;

        EIntakeState(double pPower) {
            this.mPower = pPower;
        }

        double getPower() {
            return mPower;
        }
    }

    public Intake() {
        mIntakeState = EIntakeState.STOP;
        mTalon = TalonSRXFactory.createDefaultTalon(SystemSettings.kIntakeTalonId);
        mTalon.setInverted(true);
    }

    @Override
    public void modeInit(double pNow) {
    }

    @Override
    public void periodicInput(double pNow) {

    }

    @Override
    public void update(double pNow) {
        mTalon.set(ControlMode.PercentOutput, mIntakeState.getPower());
    }

    @Override
    public void shutdown(double pNow) {
        mTalon.set(ControlMode.PercentOutput, 0d);
    }

    public void setIntakeState(EIntakeState pIntakeState) {
        mIntakeState = pIntakeState;
    }
}
