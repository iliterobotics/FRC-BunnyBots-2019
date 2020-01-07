package us.ilite.robot.commands;

import us.ilite.robot.modules.Conveyor;
import us.ilite.robot.modules.Hopper;
import us.ilite.robot.modules.Shooter;

import java.util.logging.SocketHandler;

public class ShootFlywheel implements ICommand {
    private Shooter mShooter;
    private Conveyor mConveyor;
    private Hopper mHopper;
    private Shooter.EShooterState mShooterState;
    private Hopper.EHopperState mHopperState;
    private Conveyor.EConveyorState mConveyorState;


    public ShootFlywheel ( Shooter pShooter, Hopper pHopper , Conveyor pConveyor ) {
        this.mShooter = pShooter;
        this.mHopper = pHopper;
        this.mConveyor = pConveyor;
    }

    @Override
    public void init(double pNow) {
        mShooter.setShooterState( Shooter.EShooterState.SHOOTING );
    }

    @Override
    public boolean update(double pNow) {
        return !mShooter.isMaxVelocity();
    }

    @Override
    public void shutdown(double pNow) {
       mShooterState =  Shooter.EShooterState.STOP;
       mHopperState = Hopper.EHopperState.STOP;
       mConveyorState = Conveyor.EConveyorState.STOP;
    }
}
