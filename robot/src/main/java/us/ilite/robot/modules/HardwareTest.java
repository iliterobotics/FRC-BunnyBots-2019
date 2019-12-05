package us.ilite.robot.modules;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import us.ilite.lib.drivers.SparkMaxFactory;

public class HardwareTest extends Module {
    private CANSparkMax mCANSparkMax1;
    private CANSparkMax mCANSparkMax2;
    private CANSparkMax mCANSparkMax3;
    private CANSparkMax mCANSparkMax4;
    private EHardwareState mHardwareState;

    public enum EHardwareState {
        NORMAL(1.0),
        INVERSE(-1.0),
        STOP(0.0);
        private double power;
        EHardwareState(double pow) {
            power = pow;
        }
    }

    public HardwareTest() {
        mCANSparkMax1 = SparkMaxFactory.createDefaultSparkMax(6, CANSparkMaxLowLevel.MotorType.kBrushless);
        mCANSparkMax2 = SparkMaxFactory.createDefaultSparkMax(7, CANSparkMaxLowLevel.MotorType.kBrushless);

        mCANSparkMax3 = SparkMaxFactory.createDefaultSparkMax(4, CANSparkMaxLowLevel.MotorType.kBrushless);
        mCANSparkMax4 = SparkMaxFactory.createDefaultSparkMax(5, CANSparkMaxLowLevel.MotorType.kBrushless);

        mCANSparkMax1.setInverted(true);
        mCANSparkMax2.setInverted(true);
    }

    @Override
    public void modeInit(double pNow) {
        mCANSparkMax1.setIdleMode(CANSparkMax.IdleMode.kCoast);
        mCANSparkMax2.setIdleMode(CANSparkMax.IdleMode.kCoast);
        mCANSparkMax3.setIdleMode(CANSparkMax.IdleMode.kCoast);
        mCANSparkMax4.setIdleMode(CANSparkMax.IdleMode.kCoast);
    }

    @Override
    public void periodicInput(double pNow) {

    }

    @Override
    public void update(double pNow) {

        mCANSparkMax1.set(1.0);
        mCANSparkMax2.set(1.0);
        mCANSparkMax3.set(1.0);
        mCANSparkMax4.set(1.0);


    }

    @Override
    public void shutdown(double pNow) {

    }
}
