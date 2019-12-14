package us.ilite.robot.hardware;

import com.ctre.phoenix.sensors.PigeonIMU;
import com.flybotix.hfr.util.log.ILog;
import com.flybotix.hfr.util.log.Logger;
import com.revrobotics.*;
import com.team254.lib.geometry.Rotation2d;
import us.ilite.common.config.SystemSettings;
import us.ilite.common.lib.util.Conversions;
import us.ilite.common.lib.util.RangeScale;
import us.ilite.lib.drivers.ECommonControlMode;
import us.ilite.lib.drivers.IMU;
import us.ilite.lib.drivers.Pigeon;
import us.ilite.lib.drivers.SparkMaxFactory;
import us.ilite.robot.modules.DriveMessage;

public class NeoDriveHardware implements IDriveHardware {

    private final ILog mLogger = Logger.createLog(SrxDriveHardware.class);
    private final double kGearRatio;

    private IMU mGyro;

    private final CANSparkMax mLeftMaster, mRightMaster, mLeftRear, mRightRear;
    private ControlType mLeftControlMode, mRightControlMode;
    private CANSparkMax.IdleMode mLeftNeutralMode, mRightNeutralMode;
    private int mPidSlot = SystemSettings.kDriveVelocityLoopSlot;
    private double mCurrentOpenLoopRampRate = SystemSettings.kDriveMinOpenLoopVoltageRampRate;
    private RangeScale mRangeScale;
    private CANEncoder mLeftMasterEncoder;
    private CANEncoder mRightMasterEncoder;

    public NeoDriveHardware(double pGearRatio) {
        kGearRatio = pGearRatio;
        mGyro = new Pigeon(new PigeonIMU(SystemSettings.kPigeonId), SystemSettings.kGyroCollisionThreshold);
        // mGyro = new NavX(SerialPort.Port.kMXP);

        mLeftMaster = SparkMaxFactory.createDefaultSparkMax(SystemSettings.kDriveLeftMasterNeoID, CANSparkMaxLowLevel.MotorType.kBrushless);
        mLeftRear = SparkMaxFactory.createPermanentSlaveSparkMax(SystemSettings.kDriveLeftRearNeoID, mLeftMaster, CANSparkMaxLowLevel.MotorType.kBrushless);

        mRightMaster = SparkMaxFactory.createDefaultSparkMax(SystemSettings.kDriveRightMasterNeoID, CANSparkMaxLowLevel.MotorType.kBrushless);
        mRightRear = SparkMaxFactory.createPermanentSlaveSparkMax(SystemSettings.kDriveRightRearNeoID, mRightMaster, CANSparkMaxLowLevel.MotorType.kBrushless);

        this.mLeftMasterEncoder = mLeftMaster.getEncoder();
        this.mRightMasterEncoder = mRightMaster.getEncoder();

        configureMaster(mLeftMaster, true);
        configureMotor(mLeftMaster);
        configureMotor(mLeftRear);

        configureMaster(mRightMaster, false);
        configureMotor(mRightMaster);
        configureMotor(mRightRear);

        mLeftMaster.setInverted(false);
        mLeftMiddle.setInverted(true);
        mLeftRear.setInverted(true);

        mRightMaster.setInverted(true);
        mRightMiddle.setInverted(false);
        mRightRear.setInverted(true);

        // Invert sensor readings by multiplying by 1 or -1
        mLeftMasterEncoder.setPositionConversionFactor(1.0 * kGearRatio);
        mLeftMasterEncoder.setVelocityConversionFactor(1.0 * kGearRatio);

        mRightMasterEncoder.setPositionConversionFactor(1.0 * kGearRatio);
        mRightMasterEncoder.setVelocityConversionFactor(1.0 * kGearRatio);

        configSparkForVelocity(mLeftMaster);
        configSparkForVelocity(mRightMaster);

        reloadVelocityGains(mLeftMaster);
        reloadVelocityGains(mRightMaster);

    }

    @Override
    public void init() {
        zero();
        mLeftControlMode = mRightControlMode = ControlType.kDutyCycle;
//        mLeftNeutralMode = mRightNeutralMode = CANSparkMax.IdleMode.kBrake;
        mLeftNeutralMode = mRightNeutralMode = CANSparkMax.IdleMode.kCoast;

        set(DriveMessage.kNeutral);
    }

    @Override
    public void zero() {

        mLogger.error("ZEROING THE ENCODERS!!!!!");
        mGyro.zeroAll();

//        mLeftMaster.getEncoder().setPosition(0.0);
//        mRightMaster.getEncoder().setPosition(0.0);
        mLeftMasterEncoder.setPosition(0d);
        mRightMasterEncoder.setPosition(0d);
        // Bypass state machine in set() and configure directly
        configSparkForPercentOutput(mLeftMaster);
        configSparkForPercentOutput(mRightMaster);
        setNeutralMode(CANSparkMax.IdleMode.kBrake, mLeftMaster, mLeftRear);
        setNeutralMode(CANSparkMax.IdleMode.kBrake, mRightMaster, mRightRear);

        mLeftMaster.set(0.0);
        mRightMaster.set(0.0);
    }

    public void set(DriveMessage pDriveMessage) {

        mLeftControlMode = configForControlMode(mLeftMaster, mLeftControlMode, pDriveMessage.leftControlMode.kRevControlType);
        mRightControlMode = configForControlMode(mRightMaster, mRightControlMode, pDriveMessage.rightControlMode.kRevControlType);

        mLeftNeutralMode = configForNeutralMode(mLeftNeutralMode, pDriveMessage.leftNeutralMode.kRevIdleMode, mLeftMaster, mLeftRear);
        mRightNeutralMode = configForNeutralMode(mRightNeutralMode, pDriveMessage.rightNeutralMode.kRevIdleMode, mRightMaster, mRightRear);

        mLeftMaster.getPIDController().setReference(pDriveMessage.leftOutput, mLeftControlMode, mPidSlot, pDriveMessage.leftDemand);
        mRightMaster.getPIDController().setReference(pDriveMessage.rightOutput, mRightControlMode, mPidSlot, pDriveMessage.rightDemand);

    }

    public void setTarget(DriveMessage pDriveMessage) {
        mLeftMaster.getPIDController().setReference(pDriveMessage.leftDemand, ControlType.kVelocity);
        mRightMaster.getPIDController().setReference(pDriveMessage.rightDemand, ControlType.kVelocity);
    }

    /**
     * Allows external users to request that our control mode be pre-configured instead of configuring on the fly.
     * @param pControlMode
     */
    public void configureMode(ECommonControlMode pControlMode) {
        mLeftControlMode = configForControlMode(mLeftMaster, mLeftControlMode, pControlMode.kRevControlType);
        mRightControlMode = configForControlMode(mRightMaster, mRightControlMode, pControlMode.kRevControlType);
    }

    @Override
    public void setImu(IMU pImu) {
        mGyro = pImu;
    }

    public IMU getImu() {
        return mGyro;
    }

    private ControlType configForControlMode(CANSparkMax pSparkMax, ControlType pCurrentControlMode, ControlType pDesiredControlMode) {
        ControlType controlMode = pCurrentControlMode;

        if(pCurrentControlMode != pDesiredControlMode) {
            switch(pDesiredControlMode) {
                case kDutyCycle:
                    controlMode = ControlType.kDutyCycle;
                    configSparkForPercentOutput(pSparkMax);
                    break;
                case kSmartMotion:
                    controlMode = ControlType.kSmartMotion;
                    configSparkForSmartMotion(pSparkMax);
                    break;
                case kVelocity:
                    controlMode = ControlType.kVelocity;
                    configSparkForVelocity(pSparkMax);
                    break;
                default:
                    mLogger.error("Unimplemented control mode - defaulting to PercentOutput.");
                    controlMode = ControlType.kDutyCycle;
                    break;
            }
        }

        return controlMode;
    }

    private CANSparkMax.IdleMode configForNeutralMode(CANSparkMax.IdleMode pCurrentNeutralMode, CANSparkMax.IdleMode pDesiredNeutralMode, CANSparkMax... pSparkMaxes) {
        if(pCurrentNeutralMode != pDesiredNeutralMode) {
            setNeutralMode(pDesiredNeutralMode, pSparkMaxes);
        }

        return pDesiredNeutralMode;
    }

    private void setNeutralMode(CANSparkMax.IdleMode pNeutralMode, CANSparkMax ... pSparkMaxes) {
        for(CANSparkMax sparkMax : pSparkMaxes) {
            mLogger.info("Setting neutral mode to: ", pNeutralMode.name(), " for Talon ID ", sparkMax.getDeviceId());
            sparkMax.setIdleMode(pNeutralMode);
        }
    }

    private void configureMaster(CANSparkMax sparkMax, boolean pIsLeft) {
        // Velocity, temperature, voltage, and current according the REV docs
        sparkMax.setPeriodicFramePeriod(CANSparkMaxLowLevel.PeriodicFrame.kStatus1, 5);
        // Position according to REV docs
        sparkMax.setPeriodicFramePeriod(CANSparkMaxLowLevel.PeriodicFrame.kStatus2, 5);

        sparkMax.setSmartCurrentLimit(SystemSettings.kDriveCurrentLimitAmps);
        sparkMax.setSecondaryCurrentLimit(SystemSettings.kDriveCurrentLimitAmps);
        // Set a peak current limit duration??
    }

    private void configureMotor(CANSparkMax motorController) {
        /*
        TODO Disabled voltage comp for now because of:
        https://www.chiefdelphi.com/t/sparkmax-voltage-compensation/350540/5
         */
//        motorController.enableVoltageCompensation(12.0);
        // No velocity measurement filter
        motorController.setOpenLoopRampRate(SystemSettings.kDriveMaxOpenLoopVoltageRampRate);
        motorController.setClosedLoopRampRate(SystemSettings.kDriveClosedLoopVoltageRampRate);
        // motorController.configNeutralDeadband(0.04, 0);
    }

    private void configSparkForPercentOutput(CANSparkMax pSparkMax) {
        // talon.configNeutralDeadband(0.04, 0);
    }

    private void configSparkForVelocity(CANSparkMax pSparkMax) {
        mPidSlot = SystemSettings.kDriveVelocityLoopSlot;
        mLogger.info("Configuring Spark ID ", pSparkMax.getDeviceId(), " for velocity mode");
    }


    private void reloadVelocityGains(CANSparkMax pSparkMax) {
        mLogger.info("Reloading gains for Talon ID ", pSparkMax.getDeviceId());

        CANPIDController sparkMaxPid = pSparkMax.getPIDController();

        sparkMaxPid.setSmartMotionAllowedClosedLoopError(SystemSettings.kDriveVelocityTolerance, SystemSettings.kDriveVelocityLoopSlot);
        sparkMaxPid.setP(SystemSettings.kDriveVelocity_kP, SystemSettings.kDriveVelocityLoopSlot);
        sparkMaxPid.setI(SystemSettings.kDriveVelocity_kI, SystemSettings.kDriveVelocityLoopSlot);
        sparkMaxPid.setD(SystemSettings.kDriveVelocity_kD, SystemSettings.kDriveVelocityLoopSlot);
        sparkMaxPid.setFF(SystemSettings.kDriveVelocity_kF, SystemSettings.kDriveVelocityLoopSlot);
    }

    private void configSparkForSmartMotion(CANSparkMax talon) {
        configSparkForVelocity(talon);

        talon.getPIDController().setSmartMotionMaxVelocity(SystemSettings.kDriveMotionMagicCruiseVelocity, SystemSettings.kLongCANTimeoutMs);
        talon.getPIDController().setSmartMotionMaxAccel(SystemSettings.kDriveMotionMagicMaxAccel, SystemSettings.kLongCANTimeoutMs);
    }

    public Rotation2d getHeading() {
        return mGyro.getHeading();
    }

    public double getLeftInches() {
        return Conversions.ticksToInches(mLeftMasterEncoder.getPosition());
    }

    public double getRightInches() {
        return Conversions.ticksToInches(mRightMasterEncoder.getPosition());
    }

    public double getLeftVelTicks() {
        return mLeftMasterEncoder.getVelocity();
    }

    public double getRightVelTicks() {
        return mRightMasterEncoder.getVelocity();
    }

    /**
     * TODO Not available with current API
     * @return
     */
    public double getLeftTarget() {
        return 0.0;
    }

    /**
     * TODO Not available with current API
     * @return
     */
    public double getRightTarget() {
        return 0.0;
    }

    public double getLeftVelInches() {
        return Conversions.ticksPerTimeUnitToRadiansPerSecond(getLeftVelTicks());
    }

    public double getRightVelInches() {
        return Conversions.ticksPerTimeUnitToRadiansPerSecond(getRightVelTicks());
    }

    @Override
    public double getLeftCurrent() {
        return mLeftMaster.getOutputCurrent();
    }

    @Override
    public double getRightCurrent() {
        return mRightMaster.getOutputCurrent();
    }

    @Override
    public double getLeftVoltage() {
        return mLeftMaster.getAppliedOutput() * 12.0;
    }

    @Override
    public double getRightVoltage() {
        return mRightMaster.getAppliedOutput() * 12.0;
    }

    @Override
    public void setOpenLoopRampRate(double pOpenLoopRampRate) {
        mLeftMaster.setOpenLoopRampRate(pOpenLoopRampRate);
        mRightMaster.setOpenLoopRampRate(pOpenLoopRampRate);
    }

    @Override
    public boolean checkHardware() {

        // TODO Implement testing for VictorSPX
        // CheckerConfigBuilder checkerConfigBuilder = new CheckerConfigBuilder();
        // checkerConfigBuilder.setCurrentFloor(2);
        // checkerConfigBuilder.setCurrentEpsilon(2.0);
        // checkerConfigBuilder.setRPMFloor(1500);
        // checkerConfigBuilder.setRPMEpsilon(250);
        // checkerConfigBuilder.setRPMSupplier(()->mLeftMaster.getSelectedSensorVelocity(0));

        // boolean leftSide = TalonSRXChecker.CheckTalons(Drive.class,
        //         Arrays.asList(new TalonSRXChecker.TalonSRXConfig("left_master", mLeftMaster),
        //             new TalonSRXChecker.TalonSRXConfig("left_slave", mLeftRear)),
        //         checkerConfigBuilder.build());

        // checkerConfigBuilder.setRPMSupplier(()->mRightMaster.getSelectedSensorVelocity(0));

        // boolean rightSide = TalonSRXChecker.CheckTalons(Drive.class,
        //         Arrays.asList(new TalonSRXChecker.TalonSRXConfig("right_master", mRightMaster),
        //                 new TalonSRXChecker.TalonSRXConfig("right_slave", mRightRear)),
        //         checkerConfigBuilder.build());
        // return leftSide && rightSide;
        return true;
    }

    public void setMotorReference(CANPIDController pCanpidController, double pReference) {
        pCanpidController.setReference(pReference, ControlType.kVelocity);
    }

}
