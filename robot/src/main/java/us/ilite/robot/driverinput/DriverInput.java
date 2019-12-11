package us.ilite.robot.driverinput;

import com.flybotix.hfr.codex.Codex;
import com.flybotix.hfr.util.log.ILog;
import com.flybotix.hfr.util.log.Logger;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import us.ilite.common.lib.control.PIDController;
import us.ilite.common.Data;
import us.ilite.common.config.DriveTeamInputMap;
import com.team254.lib.util.Util;
import us.ilite.common.lib.util.CheesyDriveHelper;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import us.ilite.common.config.SystemSettings;
import us.ilite.common.lib.util.RangeScale;
import us.ilite.common.types.input.EInputScale;
import us.ilite.common.types.input.ELogitech310;
import us.ilite.lib.drivers.ECommonControlMode;
import us.ilite.lib.drivers.ECommonNeutralMode;
import us.ilite.robot.modules.*;
import us.ilite.robot.modules.Module;

public class DriverInput extends Module implements IThrottleProvider, ITurnProvider {

    protected static final double DRIVER_SUB_WARP_AXIS_THRESHOLD = 0.5;
    private ILog mLog = Logger.createLog(DriverInput.class);


    protected final Drive mDrive;
//    private final CommandManager mTeleopCommandManager;
//    private final CommandManager mAutonomousCommandManager;
//    private final Limelight mLimelight;
    private final Data mData;
//    private final Data mData;
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
    private Data mData;


    private CheesyDriveHelper mCheesyDriveHelper = new CheesyDriveHelper(SystemSettings.kCheesyDriveGains);

    protected Codex<Double, ELogitech310> mDriverInputCodex, mOperatorInputCodex;

    public DriverInput(Intake pIntake, Hopper pHopper, Conveyor pConveyor, Shooter pShooter, Data pData) {
        mIntake = pIntake;
        mHopper = pHopper;
        mConveyor = pConveyor;
        mShooter = pShooter;
        mData = pData;
        mOperatorJoystick = new Joystick(1);
        mDriverInputCodex = mData.driverinput;
        mOperatorInputCodex = mData.operatorinput;

        this.mDriverInputCodex = mData.driverinput;
        this.mOperatorInputCodex = mData.operatorinput;

        this.mDriverJoystick = new Joystick(0);
        this.mOperatorJoystick = new Joystick(1);

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
            return mData.driverinput.get(DriveTeamInputMap.DRIVER_TURN_AXIS);
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
        updateDriveTrain();        
    }
    

    private void updateDriveTrain() {

        mDrive.setDriveControlMode(Drive.DriveControlMode.VELOCITY);

        double throttle = getThrottle();
        double turn = getTurn();

        throttle = Math.abs(throttle) > 0.01 ? throttle : 0.0; //Handling Deadband
        turn = Math.abs(turn) > 0.01 ? turn : 0.0; //Handling Deadband

        mDrive.setTurnAndThrottle(turn, throttle);

//        mDrive.getDriveHardware().

    }

    private void updateWholeIntakeSystem() {
        if (mOperatorInputCodex.isSet(DriveTeamInputMap.OPERATOR_SHOOT)) {
            mHopper.setHopperState(Hopper.EHopperState.GIVE_TO_SHOOTER);
            mConveyor.setConveyorState(Conveyor.EConveyorState.GIVE_TO_SHOOTER);
            mShooter.setShooterState(Shooter.EShooterState.SHOOTING);
        } else if ( mOperatorInputCodex.isSet(DriveTeamInputMap.OPERATOR_SPIT_OUT)) {
            mHopper.setHopperState(Hopper.EHopperState.REVERSE);
            mConveyor.setConveyorState(Conveyor.EConveyorState.REVERSE);
            mShooter.setShooterState(Shooter.EShooterState.CLEAN);
        } else {
            mHopper.setHopperState(Hopper.EHopperState.STOP);
            mConveyor.setConveyorState(Conveyor.EConveyorState.STOP);
            mShooter.setShooterState(Shooter.EShooterState.STOP);
        }
        
    }

    private void updateIntake() {
        if (mOperatorInputCodex.isSet(DriveTeamInputMap.OPERATOR_INTAKE)) {
            mIntake.setIntakeState(Intake.EIntakeState.INTAKE);
        } else if (mOperatorInputCodex.isSet(DriveTeamInputMap.OPERATOR_SPIT_OUT)) {
            mIntake.setIntakeState(Intake.EIntakeState.OUTTAKE);
        } else {
            mIntake.setIntakeState(Intake.EIntakeState.STOP);
        }
    }

    @Override
    public void shutdown(double pNow) {

    }

}
