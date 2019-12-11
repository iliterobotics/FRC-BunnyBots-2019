package us.ilite.robot.auto;

import com.team254.lib.trajectory.timing.CentripetalAccelerationConstraint;
import us.ilite.common.Data;
import us.ilite.common.lib.trajectory.TrajectoryConstraints;
import us.ilite.common.lib.trajectory.TrajectoryGenerator;
import us.ilite.lib.drivers.VisionGyro;
import us.ilite.robot.auto.paths.AutoSequence;
import us.ilite.robot.auto.paths.CrossNeutralLine;
import us.ilite.robot.commands.*;
import us.ilite.robot.modules.*;

public class AutonomousRoutines {

    public static final TrajectoryConstraints kDefaultTrajectoryConstraints = new TrajectoryConstraints(
            100.0,
            40.0,
            12.0,
            new CentripetalAccelerationConstraint(20.0)
    );

    private TrajectoryGenerator mTrajectoryGenerator;
    private CrossNeutralLine mCrossNeutralLine;

    private Drive mDrive;
    private Limelight mLimelight;
    private VisionGyro mVisionGyro;
    private Data mData;

    private ICommand[] mStartToCrossNeutralLine;

    public AutonomousRoutines(TrajectoryGenerator mTrajectoryGenerator, Drive mDrive, Limelight mLimelight, VisionGyro mVisionGyro, Data mData) {
        this.mTrajectoryGenerator = mTrajectoryGenerator;
        this.mDrive = mDrive;
        this.mLimelight = mLimelight;
        this.mVisionGyro = mVisionGyro;
        this.mData = mData;

        mCrossNeutralLine = new CrossNeutralLine(mTrajectoryGenerator, mDrive);

    }

    public void generateTrajectories() {
        mStartToCrossNeutralLine = mCrossNeutralLine.generateSequence();
    }

    public ICommand[] getDefault() {
        return mStartToCrossNeutralLine;
    }

    public CrossNeutralLine getCrossNeutralLineSequence() {
        return mCrossNeutralLine;
    }

    public TrajectoryGenerator getTrajectoryGenerator() {
        return mTrajectoryGenerator;
    }

}
