package us.ilite.robot.commands;

import us.ilite.common.Data;
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

    private double kCruisePercentOutput = 1.0;

    private EYeetSide mSideToTurn;

    public enum EYeetSide {
        LEFT,
        RIGHT
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

        switch(mSideToTurn) {
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

        mDrive.setDriveMessage(new DriveMessage(mDesiredLeftPercentOutput, mDesiredRightPercentOutput, ECommonControlMode.PERCENT_OUTPUT));
        return false;
    }

    public void setSideToTurn(EYeetSide pSideToTurn) {
        mSideToTurn = pSideToTurn;
    }

    public void ramp() {
        switch(mSideToTurn){
            case LEFT:

                // Whether mCurrentPercentOutput is less than or equal to kCruisePercentOutput may be subject to change.

                if (mCurrentLeftPercentOutput < kCruisePercentOutput || mDesiredLeftPercentOutput + SystemSettings.kPositiveRampRate <= kCruisePercentOutput) {
                    mDesiredLeftPercentOutput += SystemSettings.kPositiveRampRate;
                }

            case RIGHT:
                if (mCurrentRightPercentOutput < kCruisePercentOutput || mDesiredRightPercentOutput + SystemSettings.kPositiveRampRate <= kCruisePercentOutput) {
                    mDesiredRightPercentOutput += SystemSettings.kPositiveRampRate;
                }
        }
    }

    @Override
    public void shutdown(double pNow) {

    }
}
