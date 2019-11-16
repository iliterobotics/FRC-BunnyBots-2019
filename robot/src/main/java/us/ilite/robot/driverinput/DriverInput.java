package us.ilite.robot.driverinput;

import com.flybotix.hfr.codex.Codex;
import com.flybotix.hfr.util.log.ILog;
import com.flybotix.hfr.util.log.Logger;
import us.ilite.common.Data;
import us.ilite.common.config.DriveTeamInputMap;
import us.ilite.common.lib.util.CheesyDriveHelper;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import us.ilite.common.config.SystemSettings;
import us.ilite.common.lib.util.RangeScale;
import us.ilite.common.types.ETrackingType;
import us.ilite.common.types.input.ELogitech310;
import us.ilite.robot.commands.DriveToTargetDistance;
import us.ilite.robot.commands.LimelightTargetLock;
import us.ilite.robot.modules.*;
import us.ilite.robot.modules.Module;

public class DriverInput extends Module implements IThrottleProvider, ITurnProvider {

    protected static final double
    DRIVER_SUB_WARP_AXIS_THRESHOLD = 0.5;
    private ILog mLog = Logger.createLog(DriverInput.class);


    protected final Drive mDrive;
    private final CommandManager mTeleopCommandManager;
    private final CommandManager mAutonomousCommandManager;
//    private final Limelight mLimelight;
    private final Data mData;
    private final Limelight mLimelight;
    private Timer mGroundCargoTimer = new Timer();
    private RangeScale mRampRateRangeScale;
    private ETrackingType mTrackingType;
    private ETrackingType mLastTrackingType;

    private boolean mIsCargo = true; //false;

    private CheesyDriveHelper mCheesyDriveHelper = new CheesyDriveHelper(SystemSettings.kCheesyDriveGains);

    protected Codex<Double, ELogitech310> mDriverInputCodex, mOperatorInputCodex;

    public DriverInput(CommandManager pTeleopCommandManager, CommandManager pAutonomousCommandManager, Data pData, Drive pDrive, Limelight pLimelight) {
        mTeleopCommandManager = pTeleopCommandManager;
        mAutonomousCommandManager = pAutonomousCommandManager;
        mData = pData;
        mDrive = pDrive;
        mLimelight = pLimelight;
    }

    @Override
    public double getThrottle() {
        return 0;
    }

    @Override
    public double getTurn() {
        return 0;
    }

    @Override
    public void modeInit(double pNow) {

    }

    @Override
    public void periodicInput(double pNow) {

    }

    @Override
    public void update(double pNow) {
        updateVisionCommands(pNow);
    }

    public void updateVisionCommands(double pNow) {
        SystemSettings.VisionTarget visionTarget = null;
        if(mDriverInputCodex.isSet(DriveTeamInputMap.DRIVER_TRACK_HIGH_BTN)) {
            visionTarget = SystemSettings.VisionTarget.High;
            if (mDriverInputCodex.isSet(DriveTeamInputMap.DRIVER_TRACK_LOW_BTN)) {
                visionTarget = SystemSettings.VisionTarget.Low;
            }
            if(!mTrackingType.equals(mLastTrackingType)) {
                mLog.error("Requesting command start");
                mLog.error("Stopping teleop command queue");
                mTeleopCommandManager.stopRunningCommands(pNow);
                mTeleopCommandManager.startCommands(new LimelightTargetLock(mDrive, mLimelight, 2, mTrackingType, new DriveToTargetDistance(mLimelight, mDrive, visionTarget), false).setStopWhenTargetLost(false);
            }
        } else {
            mTrackingType = null;
            if(mTeleopCommandManager.isRunningCommands()) mTeleopCommandManager.stopRunningCommands(pNow);
        }
    }

    @Override
    public void shutdown(double pNow) {

    }
}
