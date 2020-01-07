package us.ilite.robot.commands;

import com.flybotix.hfr.util.log.ILog;
import com.flybotix.hfr.util.log.Logger;
import us.ilite.robot.modules.Intake;

public class ActivateIntake implements ICommand {

    private Intake mIntake;
    private ILog mLogger = Logger.createLog(this.getClass());
    public ActivateIntake ( Intake pIntake){
        this.mIntake = pIntake;
    }
    @Override
    public void init(double pNow) {
        mIntake.setIntakeState(Intake.EIntakeState.INTAKE);
    }

    @Override
    public boolean update(double pNow) {
        mLogger.info("---------------intake activated--------------------");
        // Fix this ASAP
        if ( mIntake.getmIntakeState() == Intake.EIntakeState.INTAKE){
            return true;
        }
        return false;
    }

    @Override
    public void shutdown(double pNow) {
        mLogger.info("---------------INTAKE SHUTDOWN--------------------");
        mIntake.setIntakeState(Intake.EIntakeState.STOP);
    }
}
