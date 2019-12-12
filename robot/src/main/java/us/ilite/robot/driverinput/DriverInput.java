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
import us.ilite.robot.commands.YeetLeftRight;
import us.ilite.robot.modules.*;
import us.ilite.robot.modules.Module;

import java.lang.annotation.ElementType;

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
    private DriveMessage mDriveMessage;
    private PIDController mPIDController;
    private YeetLeftRight mYeets;

    private CheesyDriveHelper mCheesyDriveHelper = new CheesyDriveHelper(SystemSettings.kCheesyDriveGains);

    protected Codex<Double, ELogitech310> mDriverInputCodex, mOperatorInputCodex;

    public DriverInput(Data pData, Drive pDrive) {
        this.mDrive = pDrive;
        this.mData = pData;

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
        updateDriveTrain();
        if (mDriverInputCodex.isSet(DriveTeamInputMap.DRIVER_YEET_LEFT) || mDriverInputCodex.isSet(DriveTeamInputMap.DRIVER_YEET_RIGHT)) {
            updateYeets( pNow );
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
        double turn = getTurn();

        throttle = Math.abs(throttle) > 0.01 ? throttle : 0.0; //Handling Deadband
        turn = Math.abs(turn) > 0.01 ? turn : 0.0; //Handling Deadband

        mDrive.setMaxVelocity(getMaxVelocity());
        mDrive.setTurnAndThrottle(turn, throttle);

    }

    private double getMaxVelocity() {
        if (mData.driverinput.get(DriveTeamInputMap.DRIVER_NITRO_BUTTON) > 0.5) {
            return (42*5676/60) * 3;
        } else {
            return SystemSettings.kDriveTrainMaxVelocity;
        }
    }
}
