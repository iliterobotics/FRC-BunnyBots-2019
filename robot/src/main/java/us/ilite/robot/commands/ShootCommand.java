package us.ilite.robot.commands;

import com.flybotix.hfr.util.log.ILog;
import com.flybotix.hfr.util.log.Logger;
import us.ilite.robot.modules.Drive;
import us.ilite.robot.modules.Shooter;

public class ShootCommand implements ICommand {
    private Shooter mShooter;
    public ILog mLog = Logger.createLog(ShootCommand.class);
    private double mDelay;
    private Drive mDrive;

    MoveForNCycles mMoveForNCycles = new MoveForNCycles( 1.0 , 1.0 ,  7 , false , 2 , mDrive);

    public ShootCommand(Shooter pShooter){
        this.mShooter = pShooter;
    }

    @Override
    public void init(double pNow) {
        mShooter.setShooterState(Shooter.EShooterState.SHOOTING);
    }

    @Override
    public boolean update(double pNow) {
        mLog.info("-------------------Working----------------------");
        if ( mMoveForNCycles.update(pNow) ){

        }
        return false;

    }

    @Override
    public void shutdown(double pNow) {
        mShooter.shutdown(pNow);
    }

    public void createDelay(){

    }

}
