package us.ilite.robot.modules;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.team254.lib.drivers.talon.TalonSRXFactory;
import us.ilite.common.config.SystemSettings;


public class Conveyor extends Module {
    private EConveyorState mConveyorState;
    private TalonSRX mTalon;
    private Shooter mShooter;
    private double mDesiredOutput;

    public enum EConveyorState
    {
        GIVE_TO_SHOOTER,
        REVERSE,
        STOP;
    }
    
    public Conveyor(Shooter pShooter) {
        mConveyorState = EConveyorState.STOP;
        mTalon = TalonSRXFactory.createDefaultTalon(SystemSettings.kConveyorTalonId);
        mShooter = pShooter;
    }
    @Override
    public void modeInit(double pNow) {

    }
    @Override
    public void periodicInput(double pNow) {

    }
    @Override
    public void update(double pNow) {
        switch (mConveyorState) {
            case GIVE_TO_SHOOTER:
                //if(mShooter.isMaxVelocity()) {
                    mTalon.set(ControlMode.PercentOutput, SystemSettings.kConveyorTalonPower);
                //}
                break;
            case REVERSE:
                mTalon.set(ControlMode.PercentOutput, -SystemSettings.kConveyorTalonPower);
                break;
            case STOP:
                mTalon.set(ControlMode.PercentOutput, 0d);
                break;
        }
    }

    @Override
    public void shutdown(double pNow) {
        mTalon.set(ControlMode.PercentOutput, 0d);
    }

    public void setConveyorState ( EConveyorState pConveyorState ) {
        mConveyorState = pConveyorState;
    }





}
