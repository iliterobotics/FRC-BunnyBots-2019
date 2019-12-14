package us.ilite.robot.auto.paths;

import us.ilite.common.lib.trajectory.TrajectoryGenerator;
import us.ilite.robot.commands.ICommand;

public class ShootBunnyShootCrossNeutralLine extends AutoSequence{

    public ShootBunnyShootCrossNeutralLine(TrajectoryGenerator pTrajectoryGenerator) {
        super(pTrajectoryGenerator);
    }

    @Override
    public ICommand[] generateSequence() {
        return new ICommand[0];
    }
}
