package us.ilite.robot.commands;

import us.ilite.robot.modules.Hopper;
import us.ilite.robot.modules.Intake;
import us.ilite.robot.modules.Shooter;

public class OutTakeFuel implements ICommand
{
    private Intake mIntake;
    private Shooter mShooter;
    private Hopper mHopper;
    private boolean mOutTake;

    public OutTakeFuel()
    {
        mIntake = new Intake();
        mShooter = new Shooter();
        mHopper = new Hopper();
    }

    @Override
    public void init(double pNow) {

    }

    @Override
    public boolean update(double pNow)
    {
        if ( mOutTake )
        {
            outTake();
        }
        else
        {
            mIntake.setIntakeState( null );
            mHopper.setHopperState( Hopper.EHopperState.STOP );
            mShooter.setEShootingState( Shooter.EShootingState.STOP );
        }
        return false;
    }

    @Override
    public void shutdown(double pNow) {

    }

    public void outTake()
    {
        mIntake.setIntakeState( Intake.EIntakeState.OUTTAKING );
        mHopper.setHopperState( Hopper.EHopperState.REVERSE );
        mShooter.setEShootingState( Shooter.EShootingState.BACKWARD );
    }

    public void setmOutTake( boolean outTakeState)
    {
        mOutTake = outTakeState;
    }
}
