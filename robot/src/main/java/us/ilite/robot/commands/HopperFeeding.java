package us.ilite.robot.commands;

import us.ilite.robot.modules.Hopper;

public class HopperFeeding implements ICommand {

    private Hopper mHopper;

    public HopperFeeding(Hopper pHopper) {
        mHopper = pHopper;
    }
    @Override
    public void init(double pNow) {
        mHopper.setHopperState(Hopper.EHopperState.GIVE_TO_SHOOTER);
    }

    @Override
    public boolean update(double pNow) {
        return false;
    }

    @Override
    public void shutdown(double pNow) {
        mHopper.shutdown(pNow);
    }
}
