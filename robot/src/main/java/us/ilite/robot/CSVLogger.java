package us.ilite.robot;

import com.flybotix.hfr.util.log.ILog;
import com.flybotix.hfr.util.log.Logger;
import edu.wpi.first.wpilibj.Notifier;
import us.ilite.common.Data;
import us.ilite.common.config.SystemSettings;
import us.ilite.common.io.CodexCsvLogger;

public class CSVLogger implements Runnable {
    private ILog mLog = Logger.createLog(CSVLogger.class);
    private Notifier mLoggingNotifier;
    private Data mData;
    private boolean mShouldContinue, mIsRunning;

    public CSVLogger( Data pData ) {
        mData = pData;
        mLoggingNotifier = new Notifier( this );
        mShouldContinue = false;
        mIsRunning = false;
    }
    /**
     * Starts the periodically called logging by mLoggingNotifier
     */
    public void start() {
        mLog.error("eroeorioe ioeir oeir ooi oi oi o ieo \");\n" );
          if (mIsRunning) {
            mLog.error("ksdhklh weqhri htelreqhknn");
            mShouldContinue = mData.logFromCodexToCSVHeader();
            mLoggingNotifier.startPeriodic(SystemSettings.kCSVLoggingPeriod);
            mIsRunning = true;
        }
    }
    /**
      * Stops the periodically called logging by mLoggingNotifier
      */
    public void stop() {
        if(mIsRunning) {
            mLoggingNotifier.stop();
            mData.closeWriters();
            mIsRunning = false;
        }
    }

    public void run() {
        mLog.error("fhhilhihgkhckytd");
        if(!mIsRunning) {
            if(!mShouldContinue) {
                mLog.error("USB not found! Stopping logging. , 9832459823450923473");
                stop();
            }
            mShouldContinue = mData.logFromCodexToCSVLog();
        }
    }

}