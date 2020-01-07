package us.ilite.robot.auto.paths;

import com.flybotix.hfr.util.log.ILog;
import com.flybotix.hfr.util.log.Logger;
import com.team254.lib.geometry.IPose2d;
import com.team254.lib.geometry.Pose2d;
import com.team254.lib.geometry.Pose2dWithCurvature;
import com.team254.lib.geometry.Rotation2d;
import com.team254.lib.trajectory.Trajectory;
import com.team254.lib.trajectory.timing.TimedState;
import us.ilite.common.lib.control.PIDController;
import us.ilite.common.lib.trajectory.TrajectoryGenerator;
import us.ilite.robot.auto.AutonomousRoutines;
import us.ilite.robot.commands.*;
import us.ilite.robot.modules.Drive;
import us.ilite.robot.modules.Hopper;
import us.ilite.robot.modules.Intake;
import us.ilite.robot.modules.Shooter;

public class GoToTrench extends AutoSequence {
    private ILog mLogger = Logger.createLog(GoToTrench.class);
    private PIDController mPIDController;
    private Intake mIntake;
    private Shooter mShooter;
    private Hopper mHopper;
    private Drive mDrive;

    // This is PoseA
    private static Pose2d mPose2d1 = new Pose2d( 94.6 , 120 , Rotation2d.fromDegrees(0) );
    // This is PoseB
    private static Pose2d mPose2d2 = new Pose2d( 55 , 120 , Rotation2d.fromDegrees(0) );
    // This is PoseC
    private static Pose2d mPose2d3 = new Pose2d( 124 , 120 , Rotation2d.fromDegrees(0) );

    public GoToTrench(TrajectoryGenerator pTrajectoryGenerator , Intake pIntake , Shooter pShooter
    , Hopper pHopper , Drive pDrive) {
        super(pTrajectoryGenerator);
        this.mIntake = pIntake;
        this.mShooter = pShooter;
        this.mHopper = pHopper;
        this.mDrive = pDrive;
    }
   // public Trajectory<TimedState<Pose2dWithCurvature>> getStartToTrench() {
     //   return mTrajectoryGenerator.generateTrajectory(false, , AutonomousRoutines.kDefaultTrajectoryConstraints);
    //}
    @Override
    public ICommand[] generateSequence() {
        return new ICommand[] { new MoveForNCycles( 0.5d , 0.5d , 2 , false , 10 , mDrive) ,
                new ShootFlywheel(mShooter , mHopper) , new FeedToFlywheel(mHopper) };
    }
}
