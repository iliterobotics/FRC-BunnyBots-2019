package us.ilite.robot.commands;

import us.ilite.robot.modules.Shooter;

public class ShootCommand implements ICommand {
    private Shooter mShooter;

    public ShootCommand(Shooter pShooter){
        this.mShooter = pShooter;
    }

    @Override
    public void init(double pNow) {

    }

    @Override
    public boolean update(double pNow) {
        return false;
    }

    @Override
    public void shutdown(double pNow) {
        mShooter.shutdown(pNow);
    }
}
