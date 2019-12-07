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
import us.ilite.common.types.input.ELogitech310;
import us.ilite.robot.commands.YeetLeftRight;
import us.ilite.robot.modules.*;
import us.ilite.robot.modules.Module;


public class DriverInput extends Module implements IThrottleProvider, ITurnProvider {

    protected static final double
    DRIVER_SUB_WARP_AXIS_THRESHOLD = 0.5;
    private ILog mLog = Logger.createLog(DriverInput.class);


    protected final Drive mDrive;
//    private final CommandManager mTeleopCommandManager;
//    private final CommandManager mAutonomousCommandManager;
//    private final Limelight mLimelight;
//    private final Data mData;
    private YeetLeftRight mYeets;
    private Timer mGroundCargoTimer = new Timer();
    private RangeScale mRampRateRangeScale;
    private Data mData;

    private boolean mIsCargo = true; //false;
    private Joystick mDriverJoystick;
    private Joystick mOperatorJoystick;

    private CheesyDriveHelper mCheesyDriveHelper = new CheesyDriveHelper(SystemSettings.kCheesyDriveGains);

    protected Codex<Double, ELogitech310> mDriverInputCodex, mOperatorInputCodex;

    public DriverInput(Drive pDrive, Data pData) {
        mDrive = pDrive;
        mYeets = new YeetLeftRight(mDrive);

        this.mData = pData;

        mOperatorInputCodex = mData.operatorinput;
        mDriverInputCodex = mData.driverinput;

        mOperatorJoystick = new Joystick( 1 );
        mDriverJoystick = new Joystick( 0 );
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
        ELogitech310.map(mDriverInputCodex, mDriverJoystick);
        ELogitech310.map(mOperatorInputCodex, mOperatorJoystick);
    }

    @Override
    public void update(double pNow) {
        mLog.error("Updating The Driver Input Class--------------------------------------------------------");
        updateYeets(pNow);
    }

    public void updateYeets(double pNow) {

        mLog.error("UpdatingDriverInput--------------------------------------------------------");

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

    @Override
    public void shutdown(double pNow) {

    }
}
