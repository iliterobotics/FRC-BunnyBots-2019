package us.ilite.robot.auto.paths;

import com.team254.lib.geometry.Pose2d;
import com.team254.lib.geometry.Rotation2d;
import us.ilite.common.Data;
import us.ilite.common.lib.control.PIDController;
import us.ilite.common.lib.trajectory.TrajectoryGenerator;
import us.ilite.lib.drivers.VisionGyro;
import us.ilite.robot.auto.AutonomousRoutines;
import us.ilite.robot.commands.ICommand;
import us.ilite.robot.commands.MoveForNCycles;
import us.ilite.robot.commands.ReleaseCatapult;
import us.ilite.robot.commands.TurnToDegree;
import us.ilite.robot.modules.Catapult;
import us.ilite.robot.modules.Drive;
import us.ilite.robot.modules.Limelight;

public class ShootBunnyShootCrossNeutralLine extends AutoSequence{

    private TrajectoryGenerator mTrajectoryGenerator;
    private Catapult mCatapult;
    private Drive mDrive;
    private Data mData;
    private Limelight mLimelight;
    private VisionGyro mVisionGyro;

    public ShootBunnyShootCrossNeutralLine(TrajectoryGenerator pTrajectoryGenerator, Catapult pCatapult, Drive pDrive, Data pData) {
        super(pTrajectoryGenerator);
        mCatapult = pCatapult;
        mDrive = pDrive;
        mData = pData;
    }

    @Override
    public ICommand [] generateSequence() {
        return new ICommand[] {
            new ReleaseCatapult (mCatapult),
            new TurnToDegree(mDrive, Rotation2d.fromDegrees(45), 2, mData),
            new MoveForNCycles(0.5, 0.5, 2, false, 5, mDrive),
            new TurnToDegree(mDrive, Rotation2d.fromDegrees(-45), 2, mData),
            new MoveForNCycles(.25, .25, 2, false, 2, mDrive)
        };
    }
}
