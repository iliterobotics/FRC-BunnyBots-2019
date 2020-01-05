package us.ilite.robot.auto.paths;

import us.ilite.common.lib.trajectory.TrajectoryGenerator;
import us.ilite.common.types.ETrackingType;
import us.ilite.robot.commands.*;
import us.ilite.robot.modules.*;

public class TestTrajectory extends AutoSequence {
  private Drive mDrive;
  private Shooter mShooter;
  private Limelight mAimBot;
  private ETrackingType mTrackingType;
  private Hopper mHopper;
  private IThrottleProvider mThrottleProvider;

    public TestTrajectory(TrajectoryGenerator pTrajectoryGenerator , Drive pDrive , Shooter pShooter,
                          Limelight pAimBot , ETrackingType pTrackingType, Hopper pHopper , IThrottleProvider pThrottleProvider) {
        super(pTrajectoryGenerator);
        this.mDrive = pDrive;
        this.mShooter = pShooter;
        this.mAimBot = pAimBot;
        this.mTrackingType = pTrackingType;
        this.mHopper = pHopper;
        this.mThrottleProvider = pThrottleProvider;
    }

    @Override
    public ICommand[] generateSequence() {
        return new ICommand[] { new LimelightTargetLock( mDrive , mAimBot , 10 , mTrackingType , mThrottleProvider)  ,
                 new MoveForNCycles ( 0.5d , 0.5d , 2 , false , 10 , mDrive) ,
                new ShootFlywheel(mShooter , mHopper) , new FeedToFlywheel(mHopper) , new MoveForNCycles (1d , 1d ,
                5 , true , 10 , mDrive) };
    }
}