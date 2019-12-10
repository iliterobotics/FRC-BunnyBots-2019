package us.ilite.robot.commands;

import us.ilite.common.config.SystemSettings;
import us.ilite.robot.modules.Shooter;

public class Shoot implements ICommand {

    private Shooter mShooter;
    private boolean mShootUntilEmpty;

    public Shoot(Shooter pShooter, boolean pShootUntilEmpty) {
        mShooter = pShooter;
        mShootUntilEmpty = pShootUntilEmpty;
    }

    @Override
    public void init(double pNow) {
        mShooter.setShooterState(Shooter.EShooterState.SHOOTING);
    }

    @Override
    public boolean update(double pNow) {
        return mShootUntilEmpty && mShooter.cyclesNotShootingBalls() >= SystemSettings.kShootUntilEmptyCycleThreshold;
    }

    @Override
    public void shutdown(double pNow) {
        mShooter.shutdown(pNow);
    }
}
