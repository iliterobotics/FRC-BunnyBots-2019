package us.ilite.robot.commands;

import us.ilite.robot.modules.Hopper;

public class FeedToFlywheel implements ICommand {
    private Hopper mHopper;
    public FeedToFlywheel ( Hopper pHopper){
        this.mHopper = pHopper;
    }


    @Override
    public void init(double pNow) {

    }

    @Override
    public boolean update(double pNow) {
        if ( mHopper.getmHopperState() == Hopper.EHopperState.GIVE_TO_SHOOTER){
            return true;
        }
        return false;
    }

    @Override
    public void shutdown(double pNow) {

    }
}
