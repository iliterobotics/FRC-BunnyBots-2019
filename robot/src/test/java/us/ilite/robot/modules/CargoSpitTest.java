package us.ilite.robot.modules;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import us.ilite.common.Data;
import us.ilite.common.config.DriveTeamInputMap;
import us.ilite.lib.drivers.Clock;
import us.ilite.robot.driverinput.DriverInput;
import static org.junit.Assert.*;

import static org.mockito.Mockito.spy;

public class CargoSpitTest {

    // We're only testing integration between CommandManager and DriverInput, so we can mock this
    @Mock private Drive mDrive;
    private CommandManager mAutonomousCommandManager;
    private CommandManager mTeleopCommandManager;
    @Mock private TalonSRX mTalon;
    private DriverInput mDriverInput;
    private Limelight mLimelight;

    private Data mData;
    private Clock mClock;
    private ModuleList mModuleList;

    @Before
    public void setup() {

        mData = new Data();
        mClock = new Clock().simulated();
        mModuleList = new ModuleList();
        mLimelight = new Limelight( mData );
        mAutonomousCommandManager = new CommandManager();
        mDriverInput = spy(new DriverInput() );
        mModuleList.setModules( mDriverInput, mDrive );
        mModuleList.modeInit( mClock.getCurrentTime() );
    }

    @After
    public void cleanup() {
        mData = new Data();
        mClock = new Clock().simulated();
        mModuleList = new ModuleList();

        mDriverInput = spy(new DriverInput( ));
        mModuleList.setModules( mDriverInput, mDrive );
        mModuleList.modeInit( mClock.getCurrentTime() );
    }

//    @Test
//    public void testIntakeOneCycle() {
//        mData.operatorinput.set( DriveTeamInputMap.OPERATOR_CARGO_SELECT, 1.0 );
//        updateRobot();
//        assertTrue( mCargoSpit.isIntaking() );
//
//        mData.operatorinput.set( DriveTeamInputMap.OPERATOR_CARGO_SELECT, null );
//        updateRobot();
//        assertFalse( mCargoSpit.isIntaking() );
//    }

    private void updateRobot() {
        mModuleList.update( mClock.getCurrentTime() );
        mClock.cycleEnded();
    }
}
