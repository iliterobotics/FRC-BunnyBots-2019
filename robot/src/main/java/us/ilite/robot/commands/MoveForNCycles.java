package us.ilite.robot.commands;

import com.flybotix.hfr.util.log.ILog;
import com.flybotix.hfr.util.log.Logger;
import us.ilite.lib.drivers.ECommonControlMode;
import us.ilite.robot.modules.Drive;
import us.ilite.robot.modules.DriveMessage;

public class MoveForNCycles implements ICommand {

    private double mLeftPower;
    private double mRightPower;
    private int mCycles;
    private boolean mBackwards;
    private int mVector;
    private ILog mLog = Logger.createLog(MoveForNCycles.class);
    private double mDelay;

    private Drive mDrive;

    public MoveForNCycles(double pLeftPower, double pRightPower, int pCycles, boolean pBackwards, double pDelay, Drive pDrive) {
        this.mLeftPower = pLeftPower;
        this.mRightPower = pRightPower;
        this.mCycles = pCycles;
        this.mDrive = pDrive;
        this.mBackwards = pBackwards;
        this.mDelay = pDelay;
        this.mVector = pBackwards ? -1 : 1;
    }


    @Override
    public void init(double pNow) {

    }

    @Override
    public boolean update(double pNow) {
        //Only delays once
        mLog.error("--------------------------------" + mDelay);
        if (delay()) {
            mDrive.setDriveMessage(new DriveMessage(mVector * mLeftPower, mVector * mRightPower, ECommonControlMode.PERCENT_OUTPUT));
            mCycles--;
            if (mCycles <= 0) {
                mDrive.setDriveMessage(new DriveMessage(0, 0, ECommonControlMode.PERCENT_OUTPUT));
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public void shutdown(double pNow) {

    }

    private boolean delay() {
        mDelay--;
        return mDelay <= 0;
    }
}
