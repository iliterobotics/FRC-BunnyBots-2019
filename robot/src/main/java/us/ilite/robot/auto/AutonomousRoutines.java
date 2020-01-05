package us.ilite.robot.auto;

import com.team254.lib.trajectory.timing.CentripetalAccelerationConstraint;
import us.ilite.common.Data;
import us.ilite.common.lib.trajectory.TrajectoryConstraints;
import us.ilite.common.lib.trajectory.TrajectoryGenerator;
import us.ilite.lib.drivers.VisionGyro;
import us.ilite.robot.auto.paths.AutoSequence;
import us.ilite.robot.auto.paths.ShootBunnyCrossNeutralLine;
import us.ilite.robot.auto.paths.TestTrajectory;
import us.ilite.robot.commands.*;
import us.ilite.robot.modules.*;

public class AutonomousRoutines {

    public static final TrajectoryConstraints kDefaultTrajectoryConstraints = new TrajectoryConstraints(
            100.0,
            60,// Was 40.0 originally,
            12.0,
            new CentripetalAccelerationConstraint(20.0)
    );

    private TrajectoryGenerator mTrajectoryGenerator;
   // private ShootBunnyCrossNeutralLine mShootBunnyCrossNeutralLine;
    private TestTrajectory mTestTrajectory;

   private Drive mDrive;
   //private Limelight mLimelight;
   // private VisionGyro mVisionGyro;
   // private Data mData;
   // private Catapult mCatapult;
    private Shooter mShooter;
    private Hopper mHopper;
   // private IThrottleProvider mThrottleProvider;

    private ICommand[] mTestTrajectory1;

    public AutonomousRoutines(TrajectoryGenerator mTrajectoryGenerator, Drive mDrive, Shooter pShooter, Hopper pHopper) {
        this.mTrajectoryGenerator = mTrajectoryGenerator;
        this.mDrive = mDrive;
        this.mShooter = pShooter;
        this.mHopper = pHopper;
        mTestTrajectory = new TestTrajectory(mTrajectoryGenerator, mDrive, mShooter , mHopper);

    }

    public void generateTrajectories() {
        mTestTrajectory1 = mTestTrajectory.generateSequence();
    }

    public ICommand[] getDefault() {
        return mTestTrajectory1;
    }

    public TestTrajectory getTestTrajectory() {
        return mTestTrajectory;
    }

    public TrajectoryGenerator getTrajectoryGenerator() {
        return mTrajectoryGenerator;
    }

}
