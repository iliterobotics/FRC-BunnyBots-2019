package us.ilite.robot.commands;

import us.ilite.common.config.SystemSettings;
import us.ilite.common.lib.control.PIDController;
import us.ilite.common.lib.control.PIDGains;
import us.ilite.robot.modules.IThrottleProvider;
import us.ilite.robot.modules.Limelight;

import javax.swing.*;

public class DriveToTargetDistance implements ICommand, IThrottleProvider {

    private Limelight mLimelight;
    private PIDController mPIDController;
    private PIDGains mDistanceGains;
    private SystemSettings.VisionTarget mTarget;
    private double mError;
    private double mOutput;
    private final double kMaxDistance= 0.0; //find value later
    private final double kMinDistance = 0.0; // find value later

    public DriveToTargetDistance(Limelight pLimelight, SystemSettings.VisionTarget pVisionTarget) {
        mLimelight = pLimelight;
        mTarget = pVisionTarget;
        mDistanceGains = new PIDGains(0.0, 0.0, 0.0); //tune when we have robot
        mPIDController = new PIDController(mDistanceGains, kMinDistance, kMaxDistance, SystemSettings.kControlLoopPeriod);
        mPIDController.setSetpoint(mTarget.getDistanceNeeded());
        mPIDController.setOutputRange(-1, 1);
        mPIDController.setDeadband(SystemSettings.kDistanceToTargetDeadband);
    }

    @Override
    public void init(double pNow) {
    }

    @Override
    public boolean update(double pNow) {
        mError = mLimelight.calcTargetDistance(mTarget.getHeight()) - mTarget.getDistanceNeeded();
        return (mOutput = mPIDController.calculate(mError, pNow)) == 0.0;
    }


    @Override
    public void shutdown(double pNow) {
        mOutput = 0.0;
    }

    @Override
    public double getThrottle() {
        return mOutput;
    }
}
