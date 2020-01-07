package us.ilite.robot.commands;

import us.ilite.robot.modules.Conveyor;
import us.ilite.robot.modules.Hopper;

public class FeedToFlywheel implements ICommand {
    private Hopper mHopper;
    private Conveyor mConveyor;
    public FeedToFlywheel ( Hopper pHopper , Conveyor pConveyor){
        this.mHopper = pHopper;
        this.mConveyor = pConveyor;
    }


    @Override
    public void init(double pNow) {
        mHopper.setHopperState(Hopper.EHopperState.GIVE_TO_SHOOTER);
        mConveyor.setConveyorState(Conveyor.EConveyorState.GIVE_TO_SHOOTER);
    }

    @Override
    public boolean update(double pNow) {
        if ( mHopper.getmHopperState() == Hopper.EHopperState.GIVE_TO_SHOOTER){
            if (mConveyor.getmConveyorState() == Conveyor.EConveyorState.GIVE_TO_SHOOTER){
                return true;
            }
        }
        return false;
    }

    @Override
    public void shutdown(double pNow) {

    }
}
