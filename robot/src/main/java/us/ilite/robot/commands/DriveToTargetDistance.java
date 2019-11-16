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
    private final double[] kOutputRange = {0.0, 0.0};

    public DriveToTargetDistance(Limelight pLimelight, SystemSettings.VisionTarget pVisionTarget) {
        mLimelight = pLimelight;
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
        if (mError <= SystemSettings.kDistanceToTargetThreshold) {
            mOutput = 0.0;
            return true;
        }
        return false;
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
