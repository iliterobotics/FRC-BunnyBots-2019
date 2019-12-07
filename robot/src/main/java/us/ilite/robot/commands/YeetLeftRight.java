package us.ilite.robot.commands;

import com.flybotix.hfr.util.log.ILog;
import com.flybotix.hfr.util.log.Logger;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import us.ilite.common.Data;
import us.ilite.common.config.SystemSettings;
import us.ilite.robot.driverinput.DriverInput;
import us.ilite.robot.modules.Drive;
import us.ilite.robot.modules.DriveMessage;

public class YeetLeftRight implements ICommand {
    private ILog mLog = Logger.createLog(YeetLeftRight.class);

    private Drive mDrive;
    private EYeetSide mSideToTurn;
    private EYeetceleration mYeetceleration;
    private double mCurrentTurn;
    private double mDesiredTurn;
    private double mVector;

    public enum EYeetSide {
        LEFT,
        NOTHING,
        RIGHT;
    }

    public enum EYeetceleration {
        A(SystemSettings.kYeetPositiveRampRate, SystemSettings.kYeetCruiseOutput),
        DE(SystemSettings.kYeetNegativeRampRate, 0.0);

        private double mRampRate;
        private double mDesiredOutput;

        EYeetceleration(double pRampRate, double pDesiredOutput) {
            mRampRate = pRampRate;
            mDesiredOutput = pDesiredOutput;
        }
    }

    public YeetLeftRight(Drive pDrive) {
        mDesiredTurn = 0d;
        mCurrentTurn = 0d;
        mVector = -1d;
        this.mDrive = pDrive;
    }

    @Override
    public void init(double pNow) {
    }

    @Override
    public boolean update(double pNow) {
        mLog.error("Updating Yeets--------------------------------------------------------");

        ramp();
        double output = mDesiredTurn;

//        if (mSideToTurn == EYeetSide.LEFT || ) {
//            output *= -1;
//        }
        output *= mVector;



        SmartDashboard.putNumber("Output for Yeets" , output);
        SmartDashboard.putString("EYeetAccel", mYeetceleration.toString());
        mDrive.setDriveMessage(DriveMessage.fromThrottleAndTurn(0.0, output));
        mCurrentTurn = mDesiredTurn;

        if (mYeetceleration.mDesiredOutput == 0 && mCurrentTurn == 0) {
            return true;
        }
        return false;
    }

    public void turn(EYeetSide pSideToTurn) {
        mSideToTurn = pSideToTurn;
        mYeetceleration = EYeetceleration.A;

        if (pSideToTurn == EYeetSide.LEFT) {
            mVector = 1d;
        } else {
            mVector = -1d;
        }

    }

    public void slowToStop() {
        mYeetceleration = EYeetceleration.DE;
        mSideToTurn = EYeetSide.NOTHING;
    }

    public void ramp() {
        if (mCurrentTurn != mYeetceleration.mDesiredOutput) {
            mDesiredTurn += mYeetceleration.mRampRate;
            mLog.error("mDesiredTurn: ", mDesiredTurn);
        }
        if ((mDesiredTurn <= 0 && mSideToTurn == EYeetSide.NOTHING)) {
            mDesiredTurn = 0;
        }
        SmartDashboard.putNumber("Desired Yeet Turn", mDesiredTurn);
    }

    @Override
    public void shutdown(double pNow) {

    }
}
