package us.ilite.robot.modules;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANSparkMaxLowLevel;
import com.team254.lib.drivers.talon.TalonSRXFactory;
import us.ilite.common.Data;
import us.ilite.common.config.SystemSettings;
import com.revrobotics.CANSparkMax;
import us.ilite.lib.drivers.SparkMaxFactory;


public class Hopper extends Module {
    private EHopperState mHopperState;
    private TalonSRX mTalon;

    private int kJamCurrent;
    private boolean kInReverse;

    public enum EHopperState
    {
        GIVE_TO_SHOOTER(1.0),
        REVERSE(-1.0),
        STOP(0.0);

        private double power;

        EHopperState(double pow ) {
            power = pow;
        }

        public double getPower() {
            return power;
        }
    }
    
    public Hopper() {
        mHopperState = EHopperState.STOP;
        mTalon = TalonSRXFactory.createDefaultTalon(SystemSettings.kHopperMotorId);
        kJamCurrent = 0;
        kInReverse = false;
    }
    @Override
    public void modeInit(double pNow) {

    }
    @Override
    public void periodicInput(double pNow) {

    }
    @Override
    public void update(double pNow) {
        updateHopper();
    }

    @Override
    public void shutdown(double pNow) {
        mHopperState = EHopperState.STOP;
    }

    public void setHopperState ( EHopperState pHopperState )
    {
        mHopperState = pHopperState;
    }

    public void updateHopper() {
        double hopperCurrent = mTalon.getOutputCurrent();
        double hopperVoltage = mTalon.getBusVoltage();
        double hopperRatio = hopperCurrent/hopperVoltage;

        if ( kInReverse ) {
            kJamCurrent--;
            mTalon.set(ControlMode.PercentOutput, -mHopperState.getPower()/2);
            if (kJamCurrent <= 0) {
                kInReverse = false;
                kJamCurrent = 0;
            }
        }
        else if (  kJamCurrent > SystemSettings.kJamMaxCycles ) {
            mTalon.set(ControlMode.PercentOutput, -mHopperState.getPower()/2);
            kInReverse = true;
        }
        else if ( hopperRatio > SystemSettings.kMaxCurrentOutput ) {
            kJamCurrent++;
            mTalon.set(ControlMode.PercentOutput, mHopperState.getPower());
        }
        else {
            kJamCurrent = 0;
            mTalon.set(ControlMode.PercentOutput, mHopperState.getPower());
        }
    }






}
