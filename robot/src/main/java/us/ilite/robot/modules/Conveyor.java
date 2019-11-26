package us.ilite.robot.modules;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.team254.lib.drivers.talon.TalonSRXFactory;
import us.ilite.common.config.SystemSettings;


public class Conveyor extends Module {
    private EConveyorState mConveyorState;
    private TalonSRX mTalon;
    private VictorSPX mVictor;

    public enum EConveyorState
    {
        GIVE_TO_SHOOTER(1.0),
        REVERSE(-1.0 ),
        STOP(0.0 );


        private double power;

        EConveyorState(double pow) {
            power = pow;
        }
    }
    
    public Conveyor() {
        mConveyorState = EConveyorState.STOP;
        mTalon = TalonSRXFactory.createDefaultTalon(SystemSettings.kConveyorTalonId);
        mVictor = TalonSRXFactory.createPermanentSlaveVictor(SystemSettings.kConveyorVictorId, mTalon);
    }
    @Override
    public void modeInit(double pNow) {

    }
    @Override
    public void periodicInput(double pNow) {

    }
    @Override
    public void update(double pNow) {
        mTalon.set(ControlMode.PercentOutput, mConveyorState.power);
    }

    @Override
    public void shutdown(double pNow) {
        mConveyorState = EConveyorState.STOP;
    }

    public void setConveyorState ( EConveyorState pConveyorState )
    {
        mConveyorState = pConveyorState;
    }





}
