package us.ilite.robot.commands;

import us.ilite.common.config.SystemSettings;
import us.ilite.common.lib.control.PIDController;
import us.ilite.common.lib.control.PIDGains;
import us.ilite.lib.drivers.ECommonControlMode;
import us.ilite.robot.modules.Drive;
import us.ilite.robot.modules.DriveMessage;
import us.ilite.robot.modules.Limelight;

import javax.swing.*;

public class DriveToTargetDistance implements ICommand {

    private Limelight mLimelight;
    private Drive mDrive;
    private PIDController mPIDController;
    private PIDGains mDistanceGains;
    private SystemSettings.VisionTarget mTarget;
    private double mError;
    private double mOutput;
    private final double kMaxDistance= 0.0; //find value later
    private final double kMinDistance = 0.0; // find value later
    private final double[] kOutputRange = {0.0, 0.0};

    public DriveToTargetDistance(Limelight pLimelight, Drive pDrive, SystemSettings.VisionTarget pVisionTarget) {
        mLimelight = pLimelight;
        mDrive = pDrive;
        mTarget = pVisionTarget;
        mDistanceGains = new PIDGains(0.0, 0.0, 0.0); //find values
        mPIDController = new PIDController(mDistanceGains, kMinDistance, kMaxDistance, SystemSettings.kControlLoopPeriod);
    }

    @Override
    public void init(double pNow) {
        mPIDController.setOutputRange(kOutputRange[0], kOutputRange[1]);
    }

    @Override
    public boolean update(double pNow) {
        mError = mLimelight.calcTargetDistance(mTarget.getHeight()) - mTarget.getDistanceNeeded();
        mOutput = mPIDController.calculate(mError, pNow);
        mDrive.setDriveMessage(new DriveMessage(mOutput, mOutput, ECommonControlMode.PERCENT_OUTPUT));
        if (mError <= SystemSettings.kDistanceToTargetThreshold) {
            return true;
        }
        return false;
    }


    @Override
    public void shutdown(double pNow) {
        mDrive.setNormal();
    }
}
