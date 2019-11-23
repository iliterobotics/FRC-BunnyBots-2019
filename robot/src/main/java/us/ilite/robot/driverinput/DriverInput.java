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
    private Shooter mShooter;
    private Hopper mHopper;
    private Intake mIntake;

    private Timer mGroundCargoTimer = new Timer();
    private RangeScale mRampRateRangeScale;
    private Joystick mDriverJoystick;
    private Joystick mOperatorJoystick;
    private Data mData;


    private CheesyDriveHelper mCheesyDriveHelper = new CheesyDriveHelper(SystemSettings.kCheesyDriveGains);

    protected Codex<Double, ELogitech310> mDriverInputCodex, mOperatorInputCodex;

    public DriverInput(Shooter pShooter, Hopper pHopper, Intake pIntake, Data pData ) {
        mShooter = pShooter;
        mHopper = pHopper;
        mIntake = pIntake;
        mData = pData;
        mOperatorJoystick = new Joystick(1);
        mDriverInputCodex = mData.driverinput;
        mOperatorInputCodex = mData.operatorinput;

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
        ELogitech310.map(mOperatorInputCodex, mOperatorJoystick);
    }

    @Override
    public void update(double pNow) {
        updateIntake();
        updateWholeIntakeSystem();
    }

    private void updateWholeIntakeSystem() {
//        if (mOperatorInputCodex.isSet(DriveTeamInputMap.OPERATOR_SHOOT)) {
////            mHopper.setHopperState(Hopper.EHopperState.GIVE_TO_SHOOTER);
////            mShooter.setShooterState(Shooter.EShooterState.SHOOTING);
//        }
//        if ( mOperatorInputCodex.isSet(DriveTeamInputMap.OPERATOR_SPIT_OUT)) {
////            mHopper.setHopperState(Hopper.EHopperState.REVERSE);
////            mShooter.setShooterState(Shooter.EShooterState.CLEAN);
//        } else {
////            mHopper.setHopperState(Hopper.EHopperState.STOP);
////            mShooter.setShooterState(Shooter.EShooterState.STOP);
//        }
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
