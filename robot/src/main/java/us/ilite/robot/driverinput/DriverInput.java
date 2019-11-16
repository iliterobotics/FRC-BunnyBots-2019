package us.ilite.robot.driverinput;

import com.flybotix.hfr.codex.Codex;
import com.flybotix.hfr.util.log.ILog;
import com.flybotix.hfr.util.log.Logger;
import us.ilite.common.config.DriveTeamInputMap;
import us.ilite.common.lib.util.CheesyDriveHelper;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import us.ilite.common.config.SystemSettings;
import us.ilite.common.lib.util.RangeScale;
import us.ilite.common.types.input.ELogitech310;
import us.ilite.robot.commands.OutTakeFuel;
import us.ilite.robot.commands.ShootFuel;
import us.ilite.robot.modules.*;
import us.ilite.robot.modules.Module;

import static us.ilite.common.config.DriveTeamInputMap.*;

public class DriverInput extends Module implements IThrottleProvider, ITurnProvider {

    protected static final double
            DRIVER_SUB_WARP_AXIS_THRESHOLD = 0.5;
    private ILog mLog = Logger.createLog(DriverInput.class);


    //    protected final Drive mDrive;
//    private final CommandManager mTeleopCommandManager;
//    private final CommandManager mAutonomousCommandManager;
//    private final Limelight mLimelight;
//    private final Data mData;
    private ShootFuel mShootFuel;
    private OutTakeFuel mOutTakeFuel;
    private Timer mGroundCargoTimer = new Timer();
    private RangeScale mRampRateRangeScale;


    private boolean mIsCargo = true; //false;
    private Joystick mDriverJoystick;
    private Joystick mOperatorJoystick;


    private CheesyDriveHelper mCheesyDriveHelper = new CheesyDriveHelper(SystemSettings.kCheesyDriveGains);

    protected Codex<Double, ELogitech310> mDriverInputCodex, mOperatorInputCodex;

    public DriverInput() {

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
        updateIntakeSystem();
        updateOutTakeFuel();
    }

    @Override
    public void shutdown(double pNow) {

    }

    public void updateIntakeSystem() {
        if (mOperatorInputCodex.isSet(SHOOT)) {
            mShootFuel.shoot(true);
        } else {
            mShootFuel.shoot(false);
        }
    }

    public void updateOutTakeFuel()
    {
        if (mOperatorInputCodex.isSet( OUTTAKE ) )
        {
            mOutTakeFuel.setmOutTake( true );
        }
        else
        {
            mOutTakeFuel.setmOutTake( false );
        }
    }

}
