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
    // private double mDesiredOutput; TUNE THIS
    private boolean kInReverse;
   //  private int kMaxCurrentOutput; TUNE THIS
    private double kJamCurrent;


    public enum EHopperState {
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
    public void shutdown(double pNow) {
        mTalon.set(ControlMode.PercentOutput, 0d);
    }

    @Override
    public void update(double pNow) {
        if ( mHopperState != EHopperState.REVERSE ) {
            unjam();
        }
        if ( !kInReverse ) {
            switch (mHopperState) {
                case GIVE_TO_SHOOTER:
                    //if(mShooter.isMaxVelocity()) {
                    mTalon.set(ControlMode.PercentOutput, SystemSettings.kHopperTalonPower);
                    //}
                    break;
                case REVERSE:
                    mTalon.set(ControlMode.PercentOutput, SystemSettings.kHopperUnjamTalonPower);
                    break;
                case STOP:
                    mTalon.set(ControlMode.PercentOutput, 0d);
                    break;
            }
        }

    }

    public void unjam() {
        double hopperCurrent = mTalon.getOutputCurrent();
        double hopperVoltage = mTalon.getBusVoltage();
        double hopperRatio = hopperCurrent / hopperVoltage;

        if (kInReverse) {
            kJamCurrent--;
            if (kJamCurrent <= 0) {
                kInReverse = false;
                kJamCurrent = 0;
            }
        } else if (kJamCurrent > SystemSettings.kJamMaxCycles) {
            kInReverse = true;
            setHopperState(EHopperState.REVERSE);
        } else if (hopperRatio > SystemSettings.kMaxCurrentOutput) {
            kJamCurrent++;
            setHopperState(EHopperState.GIVE_TO_SHOOTER);
        } else {
            if ( kJamCurrent > 0 ) {
                kJamCurrent--;
            }
        }
    }

    public void setHopperState(EHopperState pHopperState) {
        mHopperState = pHopperState;
    }

}
