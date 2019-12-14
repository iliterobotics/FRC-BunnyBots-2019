package us.ilite.robot.auto.paths;

import com.team254.lib.geometry.Pose2d;
import com.team254.lib.geometry.Pose2dWithCurvature;
import com.team254.lib.geometry.Rotation2d;
import com.team254.lib.trajectory.Trajectory;
import com.team254.lib.trajectory.timing.TimedState;
import us.ilite.common.lib.trajectory.TrajectoryGenerator;
import us.ilite.robot.auto.AutonomousRoutines;
import us.ilite.robot.commands.*;
import us.ilite.robot.modules.Catapult;
import us.ilite.robot.modules.Drive;

import java.util.Arrays;
import java.util.List;

public class ShootBunnyCrossNeutralLine extends AutoSequence {

    private Drive mDrive;
    private Catapult mCatapult;

    public ShootBunnyCrossNeutralLine(TrajectoryGenerator pTrajectoryGenerator, Drive pDrive, Catapult pCatapult) {
        super(pTrajectoryGenerator);
        mDrive = pDrive;
        mCatapult = pCatapult;
    }

    public static Pose2d kShootBunnyCrossNeutralLineFromStart = new Pose2d(FieldElementLocations.kNeutralLine, Rotation2d.fromDegrees(0.0));

//    public static final List<Pose2d> kSideStartToShootBunnyCrossNeutralLinePath = Arrays.asList(
//            StartingPoses.kStart,
//            kShootBunnyCrossNeutralLineFromStart
//    );
//
//    public Trajectory<TimedState<Pose2dWithCurvature>> getShootBunnyCrossNeutralLineTrajectory() {
//        return mTrajectoryGenerator.generateTrajectory(false, kSideStartToShootBunnyCrossNeutralLinePath, AutonomousRoutines.kDefaultTrajectoryConstraints);
//    }

    @Override
    public ICommand[] generateSequence() {
        return new ICommand[] {
                new ReleaseCatapult(mCatapult),
//                new FollowTrajectoryToPoint(mDrive, mTrajectoryGenerator, false, kShootBunnyCrossNeutralLineFromStart)
                new MoveForNCycles(1d, 1d, 25, true, 10, mDrive)
        };
    }
}
