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
import us.ilite.common.types.input.ELogitech310;
import us.ilite.lib.drivers.ECommonControlMode;
import us.ilite.robot.modules.*;
import us.ilite.robot.modules.Module;

public class DriverInput extends Module implements IThrottleProvider, ITurnProvider {

    protected static final double
    DRIVER_SUB_WARP_AXIS_THRESHOLD = 0.5;
    private ILog mLog = Logger.createLog(DriverInput.class);


//    protected final Drive mDrive;
//    private final CommandManager mTeleopCommandManager;
//    private final CommandManager mAutonomousCommandManager;
//    private final Limelight mLimelight;
//    private final Data mData;
    private final Drive mDrive;
    private Timer mGroundCargoTimer = new Timer();
    private RangeScale mRampRateRangeScale;

    private boolean mIsCargo = true; //false;
    private Joystick mDriverJoystick;
    private Joystick mOperatorJoystick;
    private Data mData;
    private DriveMessage mDriveMessage;
    private PIDController mPIDController;

    private CheesyDriveHelper mCheesyDriveHelper = new CheesyDriveHelper(SystemSettings.kCheesyDriveGains);

    protected Codex<Double, ELogitech310> mDriverInputCodex, mOperatorInputCodex;

    public DriverInput(Data pData, Drive pDrive) {
        this.mDrive = pDrive;
        this.mData = pData;

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
        updateDriveTrain();
    }

    @Override
    public void shutdown(double pNow) {

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

        double leftSetpoint = leftDemand * SystemSettings.kDriveTrainMaxVelocity;
        double rightSetpoint = rightDemand * SystemSettings.kDriveTrainMaxVelocity;
//        mDriveMessage = new DriveMessage(leftDemand * SystemSettings.kDriveTrainMaxVelocity, rightDemand * SystemSettings.kDriveTrainMaxVelocity, ECommonControlMode.VELOCITY);
//        mDriveMessage = new DriveMessage(leftDemand, rightDemand, ECommonControlMode.PERCENT_OUTPUT);
        mDriveMessage = new DriveMessage(leftSetpoint, rightSetpoint, ECommonControlMode.VELOCITY);
        mDrive.setDriveMessage(mDriveMessage);

//        mDrive.getDriveHardware().

    }
}
