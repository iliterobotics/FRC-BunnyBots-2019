package us.ilite.robot.driverinput;

import com.flybotix.hfr.codex.Codex;
import com.flybotix.hfr.util.log.ILog;
import com.flybotix.hfr.util.log.Logger;
import com.team254.lib.util.Util;
import us.ilite.common.Data;
import us.ilite.common.config.DriveTeamInputMap;
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
    private Timer mGroundCargoTimer = new Timer();
    private RangeScale mRampRateRangeScale;

    private boolean mIsCargo = true; //false;
    private Joystick mDriverJoystick;
    private Joystick mOperatorJoystick;

    private CheesyDriveHelper mCheesyDriveHelper = new CheesyDriveHelper(SystemSettings.kCheesyDriveGains);

    public DriverInput(Drive pDrive, Data pData) {
        mDrive = pDrive;
        mData = pData;
        mDriverJoystick = new Joystick(0);
    }

    @Override
    public double getThrottle() {
        if (mData.driverinput.isSet(DriveTeamInputMap.DRIVER_THROTTLE_AXIS)) {
            return - mData.driverinput.get(DriveTeamInputMap.DRIVER_THROTTLE_AXIS);
        } else {
            return 0.0;
        }
    }

    @Override
    public double getTurn() {
        if (mData.driverinput.isSet(DriveTeamInputMap.DRIVER_TURN_AXIS)) {
            return - mData.driverinput.get(DriveTeamInputMap.DRIVER_TURN_AXIS);
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
    }

    @Override
    public void update(double pNow) {
        updateDriveTrain();
    }

    private void updateDriveTrain() {
        double rotate = getTurn();
        double throttle = getThrottle();

        mDrive.setRampRate(SystemSettings.kDriveMinOpenLoopVoltageRampRate);

//        throttle = EInputScale.EXPONENTIAL.map(throttle, 2);
        rotate = EInputScale.EXPONENTIAL.map(rotate, 2);
        rotate *= SystemSettings.kNormalPercentThrottleReduction;

        if (mData.driverinput.isSet(DriveTeamInputMap.DRIVER_SUB_WARP_AXIS) && mData.driverinput.get(DriveTeamInputMap.DRIVER_SUB_WARP_AXIS) > DRIVER_SUB_WARP_AXIS_THRESHOLD || mData.driverinput.get(DriveTeamInputMap.DRIVER_ACCEL_LIMIT_BYPASS) > 0.5) {
            throttle *= SystemSettings.kSnailModePercentThrottleReduction;
            rotate *= SystemSettings.kSnailModePercentRotateReduction;
        }

        double lowestOutput = Math.min(Math.abs(rotate), Math.abs(throttle));
        double highestOutput = Math.max(Math.abs(rotate), Math.abs(throttle));
        double saturatedOutput = 1.0;
        if (highestOutput > 0.0) {
            saturatedOutput = lowestOutput / highestOutput + 1.0;
        }
        throttle /= saturatedOutput;
        rotate /= saturatedOutput;

        // Handled AFTER any scaling - we don't want the output of this to be scaled
        if(Math.abs(throttle) < Util.kEpsilon) {
            throttle = SystemSettings.kTurnInPlaceThrottleBump;
        }

        DriveMessage driveMessage = DriveMessage.fromThrottleAndTurn(throttle, rotate);
        driveMessage.setNeutralMode(ECommonNeutralMode.BRAKE);
        driveMessage.setControlMode(ECommonControlMode.PERCENT_OUTPUT);

        mDrive.setDriveMessage(driveMessage);
    }

    @Override
    public void shutdown(double pNow) {

    }
}
