package us.ilite.robot.commands;

import us.ilite.common.Data;
import us.ilite.common.config.SystemSettings;
import us.ilite.robot.modules.Drive;
import us.ilite.robot.modules.DriveMessage;

public class YeetLeftRight implements ICommand {

    private Drive mDrive;

    private double mCurrentLeftPercentOutput;
    private double mDesiredLeftPercentOutput;
    private double mCurrentRightPercentOutput;
    private double mDesiredRightPercentOutput;
    private double mCurrentTurn;
    private double mDesiredTurn;
    private double kCruisePercentOutput = 1.0;

    private EYeetSide mSideToTurn;
    private EYeetceleration mYeetceleration;

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
        ramp();
        switch (mSideToTurn) {
            case LEFT:
                mDrive.setDriveMessage(DriveMessage.fromThrottleAndTurn(0.0, -mDesiredTurn));
            case RIGHT:
                mDrive.setDriveMessage(DriveMessage.fromThrottleAndTurn(0.0, mDesiredTurn));
        }
        
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
