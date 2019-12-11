package us.ilite.robot.auto.paths;

import com.team254.lib.geometry.Pose2d;
import com.team254.lib.geometry.Pose2dWithCurvature;
import com.team254.lib.geometry.Rotation2d;
import com.team254.lib.trajectory.Trajectory;
import com.team254.lib.trajectory.timing.TimedState;
import us.ilite.common.lib.trajectory.TrajectoryGenerator;
import us.ilite.robot.auto.AutonomousRoutines;
import us.ilite.robot.commands.FollowTrajectory;
import us.ilite.robot.commands.FollowTrajectoryToPoint;
import us.ilite.robot.commands.ICommand;
import us.ilite.robot.modules.Drive;

import java.util.Arrays;
import java.util.List;

public class CrossNeutralLine extends AutoSequence {

    private Drive mDrive;

    public CrossNeutralLine(TrajectoryGenerator pTrajectoryGenerator, Drive pDrive) {
        super(pTrajectoryGenerator);
        mDrive = pDrive;
    }

    public static Pose2d kCrossNeutralLineFromStart = new Pose2d(FieldElementLocations.kNeutralLine, Rotation2d.fromDegrees(0.0));

//    public static final List<Pose2d> kSideStartToCrossNeutralLinePath = Arrays.asList(
//            StartingPoses.kStart,
//            kCrossNeutralLineFromStart
//    );
//
//    public Trajectory<TimedState<Pose2dWithCurvature>> getCrossNeutralLineTrajectory() {
//        return mTrajectoryGenerator.generateTrajectory(false, kSideStartToCrossNeutralLinePath, AutonomousRoutines.kDefaultTrajectoryConstraints);
//    }

    @Override
    public ICommand[] generateSequence() {
        return new ICommand[] {
                new FollowTrajectoryToPoint(mDrive, mTrajectoryGenerator, false, kCrossNeutralLineFromStart)
        };
    }
}
