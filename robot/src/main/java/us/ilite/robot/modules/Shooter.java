package us.ilite.robot.modules;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.flybotix.hfr.util.log.ILog;
import com.flybotix.hfr.util.log.Logger;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.ControlType;
import com.team254.lib.drivers.talon.TalonSRXFactory;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.opencv.core.Mat;
import us.ilite.common.Data;
import us.ilite.common.config.SystemSettings;
import us.ilite.common.lib.control.PIDController;
import us.ilite.common.types.ETargetingData;
import us.ilite.lib.drivers.SparkMaxFactory;
import us.ilite.robot.driverinput.DriverInput;
import us.ilite.robot.loops.Loop;

public class Shooter extends Loop {
    private CANSparkMax mCANSparkMax;
    private PIDController kShooterPidController;
    private EShooterState mShooterState;
    private CANEncoder mCANEncoder;
    private Data mData;
    private Limelight mLimelight;

    private ILog mLog = Logger.createLog(Shooter.class);

    private boolean mNotShootingBalls;
    private int mCyclesNotShootingBalls;
    private double mDesiredVelocity;
    private double mLastCurrent;
    private double mShootingCurrent;

    @Override
    public void loop(double pNow) {
        update(pNow);
    }

    public enum EShooterState {
        SHOOTING,
        CLEAN,
        STOP;
    }

    public Shooter(Data pData, Limelight pLimelight) {
        mCANSparkMax = SparkMaxFactory.createDefaultSparkMax(SystemSettings.kShooterNeoID, CANSparkMaxLowLevel.MotorType.kBrushless);

        mData = pData;
        mLimelight = pLimelight;
//        kShooterPidController = new PIDController(SystemSettings.kShooterGains, 0, SystemSettings.kMaxShooterVelocity, SystemSettings.kControlLoopPeriod );
//        kShooterPidController.setOutputRange( 0, 1 );
//        kShooterPidController.setSetpoint( SystemSettings.kMaxShooterVelocity );
        mCANSparkMax.getPIDController().setP(SystemSettings.kShooterPGain);
        mCANSparkMax.getPIDController().setFF(SystemSettings.kShooterFF);
        mShooterState = EShooterState.STOP;
        mCANEncoder = mCANSparkMax.getEncoder();
    }

    @Override
    public void modeInit(double pNow) {
        mNotShootingBalls = true;
        mCyclesNotShootingBalls = 0;
        mCANSparkMax.setIdleMode(CANSparkMax.IdleMode.kCoast);
    }

    @Override
    public void periodicInput(double pNow) {
        double mCurrentCurrent = mCANSparkMax.getOutputCurrent();
        if (mCurrentCurrent - mLastCurrent >= SystemSettings.kShooterCurrentDropThreshold) {
            mShootingCurrent = mLastCurrent;
            mNotShootingBalls = true;
        } else if (mCurrentCurrent - mShootingCurrent >= SystemSettings.kShooterCurrentDropThreshold) {
            mNotShootingBalls = true;
        } else {
            mNotShootingBalls = false;
        }


    }

    @Override
    public void update(double pNow) {
        switch (mShooterState) {
            case SHOOTING:
//                mDesiredVelocity = kShooterPidController.calculate(mTalon.getSelectedSensorVelocity(), pNow);
//                mDesiredVelocity = SystemSettings.kShooterVelocity;
                if(mData.limelight.get(ETargetingData.ty) != null) {
                    double distance = mLimelight.calcTargetDistance(72);
                    mDesiredVelocity = 0.000020395604243142 * (Math.pow ( distance , 4 ) )
                            - 0.0166740145118 * (Math.pow ( distance , 3 ) )
                            + 5.009475610878 * (Math.pow ( distance , 2))
                            - 651.29825023564 * (Math.pow ( distance , 1)) + 33523.96;
                }
                mCANSparkMax.getPIDController().setReference(mDesiredVelocity, ControlType.kVelocity);
//                mCANSparkMax.set(.5);
                break;
            case CLEAN:
//                mDesiredVelocity = -SystemSettings.kShooterMaxVelocity; //-SystemSettings.kShooterVelocity;
                mCANSparkMax.set(-1.0);
                break;
            case STOP:
                mDesiredVelocity = 0d;
                mCANSparkMax.set(0.0);
                break;


        }

        SmartDashboard.putNumber("Flywheel Actual Velocity", mCANEncoder.getVelocity());
        SmartDashboard.putNumber("Flywheel Desired Velocity", mDesiredVelocity);
//        mCANSparkMax.set(.5);
//        mLog.error("(((((((((((((((((((((( " + mCANSparkMax.getAppliedOutput() + " )))))))))))))))))))))))))");

        mLastCurrent = mCANSparkMax.getOutputCurrent();

        if (mNotShootingBalls) {
            mCyclesNotShootingBalls++;
        }
    }

    public void setShooterState(EShooterState pState) {
        mShooterState = pState;
    }

    @Override
    public void shutdown(double pNow) {
        mCANSparkMax.set(0d);
    }
//
//    public boolean isMaxVelocity() {
//        return mCANSparkMax.get() == SystemSettings.kMaxShooterVelocity;
//    }

    public int cyclesNotShootingBalls() {
        return mCyclesNotShootingBalls;
    }
}
