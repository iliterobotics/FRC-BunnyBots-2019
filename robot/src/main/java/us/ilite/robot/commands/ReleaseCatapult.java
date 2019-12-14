package us.ilite.robot.commands;

import us.ilite.robot.modules.Catapult;

public class ReleaseCatapult implements ICommand {
    private Catapult mCatapult;

    public ReleaseCatapult(Catapult pCatapult) {
        mCatapult = pCatapult;
    }

    @Override
    public void init(double pNow) {
        mCatapult.releaseCatapult();
    }

    @Override
    public boolean update(double pNow) {
        return mCatapult.getPosition() != Catapult.ECatapultState.LOCKED.getPosition();
    }

    @Override
    public void shutdown(double pNow) {

    }
}
