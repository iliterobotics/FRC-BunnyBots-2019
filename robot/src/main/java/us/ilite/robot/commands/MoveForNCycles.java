package us.ilite.robot.commands;

import us.ilite.lib.drivers.ECommonControlMode;
import us.ilite.robot.modules.Drive;
import us.ilite.robot.modules.DriveMessage;

public class MoveForNCycles implements ICommand {

    private double mLeftPower;
    private double mRightPower;
    private int mCycles;
    private boolean mBackwards;
    private int mVector;

    private Drive mDrive;

    public MoveForNCycles(double pLeftPower, double pRightPower, int pCycles, boolean pBackwards, Drive pDrive) {
        this.mLeftPower = pLeftPower;
        this.mRightPower = pRightPower;
        this.mCycles = pCycles;
        this.mDrive = pDrive;
        this.mBackwards = pBackwards;
        this.mVector = pBackwards ? -1 : 1;
    }


    @Override
    public void init(double pNow) {

    }

    @Override
    public boolean update(double pNow) {
        mDrive.setDriveMessage(new DriveMessage(mVector * mLeftPower, mVector * mRightPower, ECommonControlMode.PERCENT_OUTPUT));
        mCycles--;
        return mCycles <= 0;
    }

    @Override
    public void shutdown(double pNow) {

    }
}
