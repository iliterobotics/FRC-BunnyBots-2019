package us.ilite.robot.commands;

import us.ilite.robot.modules.Hopper;
import us.ilite.robot.modules.Shooter;

public class ShootFuel implements ICommand
{
    private final Shooter mShooter;
    private final Hopper mHopper;
    private boolean mShouldShoot;

    public ShootFuel()
    {
        mShooter = new Shooter();
        mHopper = new Hopper();
    }

    @Override
    public void init(double pNow) {

    }

    @Override
    public boolean update(double pNow) {
        if (mShouldShoot) {
            mHopper.setHopperState(Hopper.EHopperState.GIVE_TO_SHOOTER);
            mShooter.activate();
        } else {
            mHopper.setHopperState(Hopper.EHopperState.STOP);
            mShooter.deactivate();
        }
        return mShouldShoot;
    }

    public void shoot(boolean pShouldShoot) {
        mShouldShoot = pShouldShoot;
    }

    @Override
    public void shutdown(double pNow) {

    }
}
