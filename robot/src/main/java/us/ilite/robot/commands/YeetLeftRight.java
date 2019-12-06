package us.ilite.robot.commands;

<<<<<<< HEAD
import com.flybotix.hfr.util.log.ILog;
import com.flybotix.hfr.util.log.Logger;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
=======
>>>>>>> yeet-left-right
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import us.ilite.common.Data;
import us.ilite.common.config.SystemSettings;
import us.ilite.robot.driverinput.DriverInput;
import us.ilite.robot.modules.Drive;
import us.ilite.robot.modules.DriveMessage;

<<<<<<< HEAD
public class YeetLeftRight implements ICommand {
    private ILog mLog = Logger.createLog(YeetLeftRight.class);
=======
public class  YeetLeftRight implements ICommand {
>>>>>>> yeet-left-right

    private Drive mDrive;
    private EYeetSide mSideToTurn;
    private EYeetceleration mYeetceleration;
    private double mCurrentTurn;
    private double mDesiredTurn;

    public enum EYeetSide {
        LEFT,
        RIGHT;
    }

    public enum EYeetceleration {
        A(SystemSettings.kYeetPositiveRampRate, SystemSettings.kYeetCruiseOutput),
        DE(SystemSettings.kYeetNegativeRampRate, 0.0);

        private double mRampRate;
        private double mDesiredOutput;

        EYeetceleration(double pRampRate, double pDesiredOutput) {
            mRampRate = pRampRate;
            mDesiredOutput = pDesiredOutput;
        }
    }

    public YeetLeftRight(Drive pDrive) {
        this.mDrive = pDrive;
    }

    @Override
    public void init(double pNow) {
    }

    @Override
    public boolean update(double pNow) {

        double output = mDesiredTurn;
        ramp();

        if (mSideToTurn == EYeetSide.LEFT) {
            output *= -1;
        }
<<<<<<< HEAD

        SmartDashboard.putNumber("Output value of Yeet", output);
        mDrive.setDriveMessage(DriveMessage.fromThrottleAndTurn( 0.0, output ));
=======
        SmartDashboard.putNumber("Output for Yeets" , output);
        mDrive.setDriveMessage(DriveMessage.fromThrottleAndTurn(0.0, output));
>>>>>>> yeet-left-right
        mCurrentTurn = mDesiredTurn;

        if (mYeetceleration.mDesiredOutput == 0 && mCurrentTurn == 0) {
            return true;
        }
        return false;
    }

    public void turn(EYeetSide pSideToTurn) {
        mSideToTurn = pSideToTurn;
        mYeetceleration = EYeetceleration.A;
    }

    public void slowToStop() {
        mYeetceleration = EYeetceleration.DE;
    }

    public void ramp() {
        if (mCurrentTurn != mYeetceleration.mDesiredOutput) {
            mDesiredTurn += mYeetceleration.mRampRate;
        }
    }

    @Override
    public void shutdown(double pNow) {

    }
}
