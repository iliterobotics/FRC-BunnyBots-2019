package us.ilite.robot.modules;
import com.revrobotics.CANSparkMaxLowLevel;
import us.ilite.common.Data;
import us.ilite.common.config.SystemSettings;
import com.revrobotics.CANSparkMax;


public class Hopper extends Module {
    private EHopperState mHopperState;
    private CANSparkMax mHopperMotor;
    //Enum for the current state of the hopper
    public enum EHopperState
    {
        GIVE_TO_SHOOTER(1.0, true),
        REVERSE(-1.0, false ),
        STOP(0.0, false );


        private double power;

        EHopperState(double pow, boolean isFeeding ) {
            power = pow;
        }

        public double getPower() {
            return power;
        }
    }
    public Hopper()
    {
        mHopperMotor = new CANSparkMax( SystemSettings.kHopperCANMotor, CANSparkMaxLowLevel.MotorType.kBrushless);
        mHopperState = EHopperState.STOP;
    }

    @Override
    public void modeInit(double pNow) {

    }
    @Override
    public void periodicInput(double pNow) {

    }
    @Override
    public void update(double pNow) {
        switch(mHopperState){
            case GIVE_TO_SHOOTER:
                mHopperMotor.set(mHopperState.getPower());

            case REVERSE:
                mHopperMotor.set(mHopperState.getPower());

            case STOP:
                mHopperMotor.set(mHopperState.getPower());

        }
    }
    @Override
    public void shutdown(double pNow) {
        mHopperState = EHopperState.STOP;
    }
    public void setHopperState ( EHopperState pHopperState )
    {
        mHopperState = pHopperState;
    }
}
