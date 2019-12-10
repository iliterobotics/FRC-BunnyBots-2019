package us.ilite.robot.modules;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.team254.lib.drivers.talon.TalonSRXFactory;
import us.ilite.common.config.SystemSettings;
import us.ilite.robot.modules.Shooter;


public class Hopper extends Module {
    private EHopperState mHopperState;
    private TalonSRX mTalon;
    private Shooter mShooter;
    private double mDesiredOutput;

    public enum EHopperState
    {
        GIVE_TO_SHOOTER,
        REVERSE,
        STOP;
    }

    public Hopper(Shooter pShooter) {
        mHopperState = EHopperState.STOP;
        mTalon = TalonSRXFactory.createDefaultTalon(SystemSettings.kHopperTalonId);
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
        switch (mHopperState) {
            case GIVE_TO_SHOOTER:
                if(mShooter.isMaxVelocity()) {
                    mTalon.set(ControlMode.PercentOutput, SystemSettings.kHopperTalonPower);
                }
                break;
            case REVERSE:
                mTalon.set(ControlMode.PercentOutput, -SystemSettings.kHopperTalonPower);
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

    public void setHopperState ( EHopperState pHopperState ) {
        mHopperState = pHopperState;
    }





}
