package us.ilite.robot.modules;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import us.ilite.common.Data;
import us.ilite.common.config.SystemSettings;
import us.ilite.lib.drivers.SparkMaxFactory;
import us.ilite.robot.driverinput.DriverInput;


public class DriveModule extends Module{
    private CANSparkMax mLeftMaster;
    private CANSparkMax mLeftMiddle;
    private CANSparkMax mLeftFollower;

    private CANSparkMax mRightMaster;
    private CANSparkMax mRightMiddle;
    private CANSparkMax mRightFollower;

    private CANEncoder mRightEncoder;
    private CANEncoder mLeftEncoder;

    private EDriveState mDriveState;

    private double mLeftDemand, mRightDemand;

    public DriveModule() {
        mLeftMaster = SparkMaxFactory.createDefaultSparkMax(SystemSettings.kDriveLeftMasterTalonId, CANSparkMaxLowLevel.MotorType.kBrushless);
        mLeftMiddle = SparkMaxFactory.createPermanentSlaveSparkMax(SystemSettings.kDriveLeftMiddleTalonId, mLeftMaster, CANSparkMaxLowLevel.MotorType.kBrushless);
        mLeftFollower = SparkMaxFactory.createPermanentSlaveSparkMax(SystemSettings.kDriveLeftRearTalonId, mLeftMaster, CANSparkMaxLowLevel.MotorType.kBrushless);

        mRightMaster = SparkMaxFactory.createDefaultSparkMax(SystemSettings.kDriveRightMasterTalonId, CANSparkMaxLowLevel.MotorType.kBrushless);
        mRightMiddle = SparkMaxFactory.createPermanentSlaveSparkMax(SystemSettings.kDriveRightMiddleTalonId, mRightMaster, CANSparkMaxLowLevel.MotorType.kBrushless);
        mRightFollower = SparkMaxFactory.createPermanentSlaveSparkMax(SystemSettings.kDriveRightRearTalonId, mRightMaster, CANSparkMaxLowLevel.MotorType.kBrushless);

        mLeftMaster.setInverted(false);
        mLeftMiddle.setInverted(true);
        mLeftFollower.setInverted(true);

        mLeftEncoder = new CANEncoder(mLeftMaster);
        mRightEncoder = new CANEncoder(mRightMaster);

        mLeftEncoder.setPositionConversionFactor(1.0 * SystemSettings.kDriveGearboxRatio);
        mRightEncoder.setVelocityConversionFactor(1.0 * SystemSettings.kDriveGearboxRatio);

        mLeftEncoder.setPositionConversionFactor(1.0 * SystemSettings.kDriveGearboxRatio);
        mRightEncoder.setVelocityConversionFactor(1.0 * SystemSettings.kDriveGearboxRatio);

        mLeftMaster.burnFlash();
        mLeftFollower.burnFlash();
        mRightMaster.burnFlash();
        mRightFollower.burnFlash();
    }

    @Override
    public void modeInit(double pNow) {

    }

    @Override
    public void periodicInput(double pNow) {

    }

    @Override
    public void update(double pNow) {
        switch (mDriveState) {
            case PERCENT_OUTPUT:
                mLeftMaster.set(mLeftDemand);
                mRightMaster.set(mRightDemand);
                break;
            case NONE:
                mRightMaster.set(0);
                mLeftMaster.set(0);
                break;
            case VELOCITY:
                mLeftMaster.set(mLeftDemand*SystemSettings.kDriveTrainMaxVelocity);
                mRightMaster.set(mRightDemand*SystemSettings.kDriveTrainMaxVelocity);
                break;
        }
    }

    @Override
    public void shutdown(double pNow) {
        mLeftMaster.set(0);
        mRightMaster.set(0);
    }

    public void setDriveState(EDriveState EState) {
        mDriveState = EState;
    }

    public enum EDriveState {
        PERCENT_OUTPUT,
        NONE,
        VELOCITY
    }

    public void setDesiredPct(double pLeftDemand, double pRightDemand) {
        mLeftDemand = pLeftDemand;
        mRightDemand = pRightDemand;
    }
}
