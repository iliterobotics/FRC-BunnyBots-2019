package us.ilite.robot.commands;

import us.ilite.common.lib.trajectory.TrajectoryGenerator;
import us.ilite.robot.auto.AutonomousRoutines;
import us.ilite.robot.modules.Drive;
import us.ilite.robot.modules.Shooter;

public class CrossNeutralLineReturnToShoot extends CommandQueue {
    public CrossNeutralLineReturnToShoot(AutonomousRoutines pAutonomousRoutines, Shooter pShooter) {
        setCommands(
                pAutonomousRoutines.getDefault(),
                new ParallelCommand(
                        new Shoot(pShooter, false)
                )
        );
    }
}
