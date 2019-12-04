package us.ilite.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import us.ilite.common.Data;
import us.ilite.common.config.SystemSettings;
import us.ilite.robot.modules.Drive;
import us.ilite.robot.modules.DriveMessage;

public class  YeetLeftRight implements ICommand {

    private Drive mDrive;
    private EYeetSide mSideToTurn;
    private EYeetceleration mYeetceleration;
    private double mCurrentTurn;
    private double mDesiredTurn;

    public enum EYeetSide {
        LEFT,
        RIGHT
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
        this.mDrive = pDrive;
    }

    @Override
    public void init(double pNow) {
    }

    @Override
    public boolean update(double pNow) {
        double output = mDesiredTurn;
        ramp();
        if (mSideToTurn == EYeetSide.LEFT) {
            output *= -1;
        }
        SmartDashboard.putNumber("Output for Yeets" , output);
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
    }

    public void slowToStop() {
        mYeetceleration = EYeetceleration.DE;
    }

    public void ramp() {
        if (mCurrentTurn != mYeetceleration.mDesiredOutput) {
            mDesiredTurn += mYeetceleration.mRampRate;
        }
    }

    @Override
    public void shutdown(double pNow) {

    }
}
