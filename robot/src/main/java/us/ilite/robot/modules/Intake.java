package us.ilite.robot.modules;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.flybotix.hfr.util.log.ILog;
import com.flybotix.hfr.util.log.Logger;
import com.team254.lib.drivers.talon.TalonSRXFactory;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import us.ilite.common.Data;
import us.ilite.common.config.SystemSettings;
//import us.ilite.common.types.manipulator.EIntake;
import us.ilite.common.types.sensor.EPowerDistPanel;
//import us.ilite.robot.driverinput.DriverInput.EGamePiece;
import us.ilite.robot.hardware.SolenoidWrapper;

public class Intake extends Module {

    private final double kZero = 0.0;
    private final String kDefaultError = "This message should not be displayed";

    private ILog mLog = Logger.createLog(Intake.class);
    private Data mData;

    private VictorSPX mIntakeRoller;
    private double mIntakeRollerCurrent;
    private double mIntakeRollerVoltage;

    private TalonSRX mTalon;

    @Override
    public void modeInit(double pNow) {

    }

    @Override
    public void periodicInput(double pNow) {

    }

    @Override
    public void update(double pNow) {

    }

    @Override
    public void shutdown(double pNow) {

    }
}
