package us.ilite.robot.driverinput;

import com.flybotix.hfr.codex.Codex;
import com.flybotix.hfr.util.log.ILog;
import com.flybotix.hfr.util.log.Logger;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import us.ilite.common.lib.control.PIDController;
import us.ilite.common.Data;
import us.ilite.common.config.DriveTeamInputMap;
import us.ilite.common.lib.util.CheesyDriveHelper;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import us.ilite.common.config.SystemSettings;
import us.ilite.common.lib.util.RangeScale;
import us.ilite.common.types.ETrackingType;
import us.ilite.common.types.input.ELogitech310;
import us.ilite.lib.drivers.ECommonControlMode;
import us.ilite.robot.commands.LimelightTargetLock;
import us.ilite.robot.commands.YeetLeftRight;
import us.ilite.robot.modules.*;
import us.ilite.robot.modules.Module;

import java.lang.annotation.ElementType;

public class DriverInput extends Module implements IThrottleProvider, ITurnProvider {

    protected static final double
            DRIVER_SUB_WARP_AXIS_THRESHOLD = 0.5;
    private ILog mLog = Logger.createLog(DriverInput.class);


    //    protected final Drive mDrive;
//    private final CommandManager mTeleopCommandManager;
//    private final CommandManager mAutonomousCommandManager;
//    private final Limelight mLimelight;
    private final Drive mDrive;
    private final Data mData;
    private Catapult mCatapult;
    private Shooter mShooter;
    private Conveyor mConveyor;
    private Intake mIntake;
    private Hopper mHopper;
    private Timer mGroundCargoTimer = new Timer();
    private RangeScale mRampRateRangeScale;
    private Joystick mDriverJoystick;
    private Joystick mOperatorJoystick;
    private DriveMessage mDriveMessage;
    private PIDController mPIDController;
    private YeetLeftRight mYeets;

    private ETrackingType mTrackingType;
    private ETrackingType mLastTrackingType = null;
    private LimelightTargetLock mLimelightTargetLock;
    private Limelight mLimelight;

    private CommandManager mTeleopCommandManager;

    private double pNow;


    private CheesyDriveHelper mCheesyDriveHelper = new CheesyDriveHelper(SystemSettings.kCheesyDriveGains);

    protected Codex<Double, ELogitech310> mDriverInputCodex, mOperatorInputCodex;

    public DriverInput(Intake pIntake, Hopper pHopper, Conveyor pConveyor, Shooter pShooter, Data pData, Catapult pCatapult, Drive pDrive) {
        mIntake = pIntake;
        mHopper = pHopper;
        mConveyor = pConveyor;
        mShooter = pShooter;
        mData = pData;
        mOperatorJoystick = new Joystick(1);
        mDriverInputCodex = mData.driverinput;
        mOperatorInputCodex = mData.operatorinput;
        mCatapult = pCatapult;
        mDrive = pDrive;

        this.mDriverInputCodex = mData.driverinput;
        this.mOperatorInputCodex = mData.operatorinput;

        this.mDriverJoystick = new Joystick(0);
        this.mOperatorJoystick = new Joystick(1);

        this.mYeets = new YeetLeftRight(mDrive);

        this.mPIDController = new PIDController(SystemSettings.kDriveClosedLoopPIDGains,
                0, SystemSettings.kDriveTrainMaxVelocity, SystemSettings.kControlLoopPeriod );
    }

    @Override
    public double getThrottle() {
        if(mData.driverinput.isSet(DriveTeamInputMap.DRIVER_THROTTLE_AXIS)) {
            return -mData.driverinput.get(DriveTeamInputMap.DRIVER_THROTTLE_AXIS);
        } else {
            return 0.0;
        }
    }

    @Override
    public double getTurn() {

        if (mData.driverinput.isSet(DriveTeamInputMap.DRIVER_TURN_AXIS)) {
            return mData.driverinput.get(DriveTeamInputMap.DRIVER_TURN_AXIS) * ((1 - mData.driverinput.get(DriveTeamInputMap.DRIVER_REDUCE_TURN_AXIS) + 0.3));
        } else {
            return 0.0;
        }
    }

    @Override
    public void modeInit(double pNow) {

    }

    @Override
    public void periodicInput(double pNow) {
        ELogitech310.map(mData.driverinput, mDriverJoystick);
        ELogitech310.map(mData.operatorinput, mOperatorJoystick);
    }

    @Override
    public void update(double pNow) {
        updateIntake();
        updateWholeIntakeSystem();
        updateHopper();
        updateDriveTrain();
        updateCatapult();
        updateLimelightTargetLock();
        if (mDriverInputCodex.isSet(DriveTeamInputMap.DRIVER_YEET_LEFT) || mDriverInputCodex.isSet(DriveTeamInputMap.DRIVER_YEET_RIGHT)) {
            updateYeets( pNow );
        }
    }

    private void updateWholeIntakeSystem() {
//        if ( mOperatorInputCodex.isSet(DriveTeamInputMap.OPERATOR_HOPPER_CLEAN)) {
//            mHopper.setHopperState(Hopper.EHopperState.REVERSE);
//            mConveyor.setConveyorState(Conveyor.EConveyorState.REVERSE);
//            mShooter.setShooterState(Shooter.EShooterState.CLEAN);
        if (mOperatorInputCodex.isSet(DriveTeamInputMap.OPERATOR_SHOOT)) {
//            mHopper.setHopperState(Hopper.EHopperState.GIVE_TO_SHOOTER);
            mConveyor.setConveyorState(Conveyor.EConveyorState.GIVE_TO_SHOOTER);
            mShooter.setShooterState(Shooter.EShooterState.SHOOTING);
        } else {
//            mHopper.setHopperState(Hopper.EHopperState.STOP);
            mConveyor.setConveyorState(Conveyor.EConveyorState.STOP);
            mShooter.setShooterState(Shooter.EShooterState.STOP);
        }
    }
        
    public void updateCatapult() {
        if (mOperatorInputCodex.isSet(DriveTeamInputMap.OPERATOR_CATAPULT_BTN)) {
            mCatapult.releaseCatapult();
        }
    }

    private void updateIntake() {
        if (mOperatorInputCodex.isSet(DriveTeamInputMap.OPERATOR_INTAKE)) {
            mIntake.setIntakeState(Intake.EIntakeState.INTAKE);
        } else if (mOperatorInputCodex.isSet(DriveTeamInputMap.OPERATOR_REVERSE_INTAKE)) {
            mIntake.setIntakeState(Intake.EIntakeState.REVERSE);
        } else {
            mIntake.setIntakeState(Intake.EIntakeState.STOP);
        }
    }

    private void updateHopper() {
        if (mOperatorInputCodex.isSet(DriveTeamInputMap.OPERATOR_HOPPER_UNJAM)) {
            mHopper.setHopperState(Hopper.EHopperState.REVERSE);
        } else if (mOperatorInputCodex.isSet(DriveTeamInputMap.OPERATOR_SHOOT)) {
            mHopper.setHopperState(Hopper.EHopperState.GIVE_TO_SHOOTER);
        } else {
            mHopper.setHopperState(Hopper.EHopperState.STOP);
        }
    }

    private void updateLimelightTargetLock() {
        if ( mOperatorInputCodex.isSet(DriveTeamInputMap.LimelightTargetLock)){

            if(!mTrackingType.equals(mLastTrackingType)) {
                mLog.error("Requesting command start");
                mLog.error("Stopping teleop command queue");
                mTeleopCommandManager.stopRunningCommands(pNow);
                mTeleopCommandManager.startCommands(new LimelightTargetLock(mDrive, mLimelight, 2, mTrackingType, this, false).setStopWhenTargetLost(false));
            }
        }
    }

    @Override
    public void shutdown(double pNow) {

    }

    public void updateYeets(double pNow) {


        if ( mDriverInputCodex.isSet(DriveTeamInputMap.DRIVER_YEET_LEFT) &&
                mDriverInputCodex.isSet(DriveTeamInputMap.DRIVER_YEET_RIGHT)) {
            mYeets.slowToStop();
        }
        else if (mDriverInputCodex.isSet(DriveTeamInputMap.DRIVER_YEET_LEFT)) {
            mYeets.turn(YeetLeftRight.EYeetSide.LEFT);
        }
        else if (mDriverInputCodex.isSet(DriveTeamInputMap.DRIVER_YEET_RIGHT)) {
            mYeets.turn(YeetLeftRight.EYeetSide.RIGHT);
        }
        else {
            mYeets.slowToStop();
        }
        mYeets.update( pNow );
    }

    private void updateDriveTrain() {

        mDrive.setDriveControlMode(Drive.DriveControlMode.VELOCITY);

        double throttle = getThrottle();
        double rotate = getTurn();


        double leftDemand = ( throttle + rotate ) /* SystemSettings.kDriveTrainMaxVelocity */;
        double rightDemand = ( throttle - rotate )/* * SystemSettings.kDriveTrainMaxVelocity*/;

        leftDemand = Math.abs(leftDemand) > 0.01 ? leftDemand : 0.0; //Handling Deadband
        rightDemand = Math.abs(rightDemand) > 0.01 ? rightDemand : 0.0; //Handling Deadband

        SmartDashboard.putNumber("Left Drive Demand", leftDemand);
        SmartDashboard.putNumber("Right Drive Demand", rightDemand);


        double leftSetpoint = leftDemand * getMaxVelocity();//SystemSettings.kDriveTrainMaxVelocity;
        double rightSetpoint = rightDemand * getMaxVelocity();//SystemSettings.kDriveTrainMaxVelocity;
//        mDriveMessage = new DriveMessage(leftDemand * SystemSettings.kDriveTrainMaxVelocity, rightDemand * SystemSettings.kDriveTrainMaxVelocity, ECommonControlMode.VELOCITY);
//        mDriveMessage = new DriveMessage(leftDemand, rightDemand, ECommonControlMode.PERCENT_OUTPUT);
        mDriveMessage = new DriveMessage(leftSetpoint, rightSetpoint, ECommonControlMode.VELOCITY);
        mDrive.setDriveMessage(mDriveMessage);

//        mDrive.getDriveHardware().

    }

    private double getMaxVelocity() {
        if (mData.driverinput.get(DriveTeamInputMap.DRIVER_NITRO_BUTTON) > 0.5) {
            return (42*5676/60) * 3;
        } else {
            return SystemSettings.kDriveTrainMaxVelocity;
        }
    }

}
