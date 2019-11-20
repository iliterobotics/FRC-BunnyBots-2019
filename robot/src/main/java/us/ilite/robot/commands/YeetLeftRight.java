package us.ilite.robot.commands;

import us.ilite.common.Data;
import us.ilite.common.config.SystemSettings;
import us.ilite.common.types.drive.EDriveData;
import us.ilite.lib.drivers.ECommonControlMode;
import us.ilite.robot.commands.ICommand;
import us.ilite.robot.modules.Drive;
import us.ilite.robot.modules.DriveMessage;

public class YeetLeftRight implements ICommand {

    private Data mData;
    private Drive mDrive;

    private double mCurrentLeftPercentOutput;
    private double mDesiredLeftPercentOutput;
    private double mCurrentRightPercentOutput;
    private double mDesiredRightPercentOutput;
    private double mDesiredTurn;
    private double kCruisePercentOutput = 1.0;

    private EYeetSide mSideToTurn;
    private EYeetceleration mYeetceleration;

    public enum EYeetSide {
        LEFT,
        RIGHT
    }

    public enum EYeetceleration {
        A(SystemSettings.kYeetPositiveRampRate),
        DE(SystemSettings.kYeetNegativeRampRate);

        private double mRampRate;

        EYeetceleration(double pRampRate) {
            mRampRate = pRampRate;
        }
    }

    public YeetLeftRight(Data pData, Drive pDrive) {
        this.mData = pData;
        this.mDrive = pDrive;
    }

    @Override
    public void init(double pNow) {

    }

    @Override
    public boolean update(double pNow) {
        if (mYeetceleration == EYeetceleration.A) {
            switch (mSideToTurn) {
                case LEFT:
                    mCurrentLeftPercentOutput = mData.drive.get(EDriveData.LEFT_MESSAGE_OUTPUT);
                    mDesiredLeftPercentOutput = mCurrentLeftPercentOutput;
                    mDesiredRightPercentOutput = 0.0;
                    ramp();
                    break;

                case RIGHT:
                    mCurrentRightPercentOutput = mData.drive.get(EDriveData.RIGHT_MESSAGE_OUTPUT);
                    mDesiredRightPercentOutput = mCurrentRightPercentOutput;
                    mDesiredLeftPercentOutput = 0.0;
                    ramp();
                    break;

                default:
                    mDesiredLeftPercentOutput = 0.0;
                    mDesiredRightPercentOutput = 0.0;
                    break;
            }
        } else {

        }

        mDrive.setDriveMessage(new DriveMessage(mDesiredLeftPercentOutput, mDesiredRightPercentOutput, ECommonControlMode.PERCENT_OUTPUT));
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
        switch(mSideToTurn){
            case LEFT:

                if (mCurrentLeftPercentOutput < kCruisePercentOutput || mDesiredLeftPercentOutput + mYeetceleration.mRampRate <= kCruisePercentOutput) {
                    mDesiredLeftPercentOutput += SystemSettings.kYeetPositiveRampRate;
                }

            case RIGHT:
                if (mCurrentRightPercentOutput < kCruisePercentOutput || mDesiredRightPercentOutput + mYeetceleration.mRampRate <= kCruisePercentOutput) {
                    mDesiredRightPercentOutput += SystemSettings.kYeetPositiveRampRate;
                }
        }
    }

    @Override
    public void shutdown(double pNow) {

    }
}
