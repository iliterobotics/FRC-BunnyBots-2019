package us.ilite.robot.modules;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.flybotix.hfr.util.log.ILog;
import com.flybotix.hfr.util.log.Logger;

//import us.ilite.common.Data;
import us.ilite.common.config.SystemSettings;


public class Intake extends Module {

    private ILog mLog = Logger.createLog(Intake.class);
    private TalonSRX mIntakeRoller;
    private double mDesiredPower;
    private EIntakeState mDesiredIntakeState;

    public Intake() {
        mIntakeRoller = new TalonSRX(SystemSettings.kIntakeId);
        mDesiredPower = 0d;
    }

    @Override
    public void modeInit(double pNow) {
        mLog.error("MODE INIT");
    }

    @Override
    public void periodicInput(double pNow) {
        //TODO
    }

    @Override
    public void update(double pNow) {
        switch (mDesiredIntakeState) {
            case INTAKING:
                mIntakeRoller.set(ControlMode.PercentOutput, mDesiredPower);
                break;
            case OUTTAKING:
                mIntakeRoller.set(ControlMode.PercentOutput, -mDesiredPower);
                break;
            case STOP:
                mIntakeRoller.set(ControlMode.PercentOutput, 0 );
                break;
        }
    }

    @Override
    public void shutdown(double pNow) {
        mDesiredPower = 0d;
    }

    public void setIntakeState(EIntakeState pIntakeState) {
        mDesiredIntakeState = pIntakeState;
    }

    private enum EIntakeState {
        INTAKING,
        STOP,
        OUTTAKING;
    }




}
