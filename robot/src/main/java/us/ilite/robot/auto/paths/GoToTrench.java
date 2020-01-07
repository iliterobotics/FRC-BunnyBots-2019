package us.ilite.robot.auto.paths;

import com.flybotix.hfr.util.log.ILog;
import com.flybotix.hfr.util.log.Logger;
import com.team254.lib.geometry.*;
import com.team254.lib.trajectory.Trajectory;
import com.team254.lib.trajectory.timing.TimedState;
import us.ilite.common.lib.control.PIDController;
import us.ilite.common.lib.trajectory.TrajectoryGenerator;
import us.ilite.robot.auto.AutonomousRoutines;
import us.ilite.robot.commands.*;
import us.ilite.robot.modules.*;

import java.util.Arrays;
import java.util.List;

public class GoToTrench extends AutoSequence {
    private ILog mLogger = Logger.createLog(GoToTrench.class);
    private PIDController mPIDController;
    private Intake mIntake;
    private Shooter mShooter;
    private Hopper mHopper;
    private Conveyor mConveyor;
    private Drive mDrive;



    //Driving towards the trench
    public static Pose2d kDriveTowardsTrench = new Pose2d( 94.6 / 2 , 80 , Rotation2d.fromDegrees(90));

    //Driving to the beginning of the trench
    public static Pose2d kReachBeginningOfTrench = new Pose2d( new Translation2d(24 , 206) , Rotation2d.fromDegrees(180));

    //Driving into the trench
    public static Pose2d kIntoTheTrench = new Pose2d( new Translation2d(24 , 310 ), Rotation2d.fromDegrees(180));

    public static final List<Pose2d> kFirstDriveToTrench= Arrays.asList(
            StartingPoses.mPose2d1,
            kDriveTowardsTrench
    );

    public static final List<Pose2d> kMovingTowardsTrench = Arrays.asList(
            new Pose2d(kDriveTowardsTrench.getTranslation(), Rotation2d.fromDegrees(0)),
            kReachBeginningOfTrench
    );

    // Drive (also probably in reverse) to the rocket
    public static final List<Pose2d> kDriveStraightThroughTrench = Arrays.asList(
            new Pose2d(kReachBeginningOfTrench.getTranslation(), Rotation2d.fromDegrees(0)) ,
            kIntoTheTrench
    );

    public Trajectory<TimedState<Pose2dWithCurvature>> getStart() {
        return mTrajectoryGenerator.generateTrajectory(false, kFirstDriveToTrench, AutonomousRoutines.kDefaultTrajectoryConstraints);
    }

    public Trajectory<TimedState<Pose2dWithCurvature>> getMiddle() {
        return mTrajectoryGenerator.generateTrajectory(false, kMovingTowardsTrench , AutonomousRoutines.kDefaultTrajectoryConstraints);
    }

    public Trajectory<TimedState<Pose2dWithCurvature>> getEnd() {
        return mTrajectoryGenerator.generateTrajectory(false, kDriveStraightThroughTrench, AutonomousRoutines.kDefaultTrajectoryConstraints);
    }

    public GoToTrench(TrajectoryGenerator pTrajectoryGenerator , Intake pIntake , Shooter pShooter
    , Hopper pHopper , Drive pDrive , Conveyor pConveyor) {
        super(pTrajectoryGenerator);
        this.mIntake = pIntake;
        this.mShooter = pShooter;
        this.mHopper = pHopper;
        this.mDrive = pDrive;
        this.mConveyor = pConveyor;
    }
   // public Trajectory<TimedState<Pose2dWithCurvature>> getStartToTrench() {
     //   return mTrajectoryGenerator.generateTrajectory(false, , AutonomousRoutines.kDefaultTrajectoryConstraints);
    //}
    @Override
    public ICommand[] generateSequence() {
        mLogger.info("----------------------THE TRENCH SEQUENCE HAS BEGUN-------------------------");
        return new ICommand[] {  new ShootFlywheel(mShooter, mHopper , mConveyor) , new FeedToFlywheel( mHopper , mConveyor),
                new FollowTrajectory(getStart(), mDrive, true),
                new FunctionalCommand(() -> System.out.println("TRAJECTORY DONE")),
                new FollowTrajectory(getMiddle(), mDrive, true) , new ActivateIntake(mIntake),
                new FollowTrajectory(getEnd(), mDrive, true) ,
                };

    }
}
