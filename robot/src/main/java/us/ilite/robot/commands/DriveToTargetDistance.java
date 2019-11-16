package us.ilite.robot.commands;

import us.ilite.common.config.SystemSettings;
import us.ilite.common.lib.control.PIDController;
import us.ilite.common.lib.control.PIDGains;
import us.ilite.robot.modules.Drive;
import us.ilite.robot.modules.DriveMessage;
import us.ilite.robot.modules.Limelight;

import javax.swing.*;

public class DriveToTargetDistance implements ICommand {

    private Limelight mLimelight;
    private Drive mDrive;
    private PIDController mPIDController;
    private PIDGains mDistanceGains;
    private double mDistanceFromTarget;
    private double mOutput;

    private final double kMaxDistance= 0.0; //find value later
    private final double kMinDistance = 0.0; // find value later
    private final double[] kOutputRange = {0.0, 0.0};

    public DriveToTargetDistance(Limelight pLimelight, Drive pDrive) {
        mLimelight = pLimelight;
        mDrive = pDrive;
        mPIDController = new PIDController(mDistanceGains, kMinDistance, kMaxDistance, SystemSettings.kControlLoopPeriod);
    }

    @Override
    public void init(double pNow) {
        mPIDController.setOutputRange(kOutputRange[0], kOutputRange[1]);
    }

    @Override
    public boolean update(double pNow) {
//        mOutput = mPIDController.calculate(mLimelight.calcTargetDistance());
        mDrive.setDriveMessage(new DriveMessage());
        return false;
    }


    @Override
    public void shutdown(double pNow) {

    }
}
