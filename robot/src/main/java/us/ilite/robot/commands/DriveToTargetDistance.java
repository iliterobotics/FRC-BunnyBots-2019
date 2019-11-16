package us.ilite.robot.commands;

import us.ilite.common.config.SystemSettings;
import us.ilite.common.lib.control.PIDController;
import us.ilite.common.lib.control.PIDGains;
import us.ilite.robot.modules.Drive;
import us.ilite.robot.modules.Limelight;

import javax.swing.*;

public class DriveToTargetDistance implements ICommand {

    private Limelight mLimelight;
    private Drive mDrive;
    private PIDController mPIDController;
    private PIDGains mDistanceGains;
    private double mDistanceFromTarget;
    private final double kMaxDistance= 0.0; //find value later
    private final double kMinDistance = 0.0; // find value later
    private

    public DriveToTargetDistance(Limelight pLimelight, Drive pDrive) {
        mLimelight = pLimelight;
        mDrive = pDrive;
        mPIDController = new PIDController(mDistanceGains, kMinDistance, kMaxDistance, SystemSettings.kControlLoopPeriod);
    }

    @Override
    public void init(double pNow) {
    }

    @Override
    public boolean update(double pNow) {

        return false;
    }


    @Override
    public void shutdown(double pNow) {

    }
}
