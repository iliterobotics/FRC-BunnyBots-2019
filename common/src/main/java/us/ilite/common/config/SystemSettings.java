package us.ilite.common.config;

import java.util.Arrays;
import java.util.List;

import com.team254.lib.util.CheesyDriveGains;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import us.ilite.common.lib.control.PIDGains;
import us.ilite.common.lib.util.NetworkTablesConstantsBase;
import us.ilite.common.types.ETrackingType;
import us.ilite.common.types.input.ELogitech310;
import us.ilite.common.types.sensor.EPowerDistPanel;

public class SystemSettings extends NetworkTablesConstantsBase {

    public static double kControlLoopPeriod = 0.01; // seconds
    public static double kCSVLoggingPeriod = 0.02;  // seconds
    public static double kMaxShooterVelocity = 1.0;
    public static double kNetworkTableUpdateRate = 0.01;

    public static int sCODEX_COMMS_PORT = 5805;

    public static SmartDashboard kSmartDashboard;

    // ===========================
    // System ID's
    // ================================
    public static int kCANAddressPCM = 20;

    //==============================================================================
    // Logging
    // =============================================================================

    public static int kCANTimeoutMs = 10; //use for on the fly updates
    public static int kLongCANTimeoutMs = 100; //use for constructors

    // =============================================================================
    // Drive Train Constants
    // =============================================================================
    public static double kDriveGearboxRatio = 0;
    public static double kDriveWheelDiameterInches = 6.0;
    public static double kDriveWheelCircumference = kDriveWheelDiameterInches * Math.PI;
    public static double kDriveTicksPerRotation = 1.0;
    public static double kDriveVelTimeScale = 1.0;
    public static double kDriveEffectiveWheelbase = 23.25;

    public static double kDriveClosedLoopVoltageRampRate = 0.5;
    public static double kDriveMinOpenLoopVoltageRampRate = 0.1;
    public static double kDriveMaxOpenLoopVoltageRampRate = 0.9;
    public static int kDriveCurrentLimitAmps = 40;//50;
    public static int kDriveCurrentLimitTriggerDurationMs = 100;

    public static CheesyDriveGains kCheesyDriveGains = new CheesyDriveGains();
    public static PIDGains kDriveClosedLoopPIDGains = new PIDGains(0.5, 0.0, 0.2);

    // =============================================================================
    // IMU Constants
    // =============================================================================
    public static double kGyroCollisionThreshold = 0.0;
    public static int kGyroUpdateRate = 200;

    // =============================================================================
    // Heading Gains
    // =============================================================================
    public static PIDGains kDriveHeadingGains = new PIDGains(0.03, 0.0, 0.0);
    public static double kDriveLinearPercentOutputLimit = 0.5;

    // =============================================================================
    // Input Constants
    // =============================================================================
	public static double kNormalPercentThrottleReduction = 1.0;
	
	// These are applied AFTER the normal throttle reduction
    public static double kSnailModePercentThrottleReduction = 0.5;
    public static double kSnailModePercentRotateReduction = 0.3;

    public static double kTargetLockThrottleReduction = 0.40;
	
	// Applied after any scaling
    public static double kDriverInputTurnMaxMagnitude = 0.5;

    public static double kTurnInPlaceThrottleBump = 0.05;
    
	public static double kInputDeadbandF310Joystick = 0.05;
    public static double kInputDeadbandF310Trigger = 0.5;
    public static int kJoystickPortDriver = 0;
    public static int kJoystickPortOperator = 1;
    public static int kJoystickPortTester = 2;

    public static int kLimelightDefaultPipeline = ETrackingType.TARGET.getPipeline();
//    public static List<ELogitech310> kTeleopCommandTriggers = Arrays.asList(DriveTeamInputMap.DRIVER_TRACK_TARGET_BTN,
//                                                                            DriveTeamInputMap.DRIVER_TRACK_CARGO_BTN,
//                                                                            DriveTeamInputMap.DRIVER_TRACK_HATCH_BTN);
//
//    public static List<ELogitech310> kAutonOverrideTriggers = Arrays.asList(DriveTeamInputMap.DRIVER_THROTTLE_AXIS,
//                                                                            DriveTeamInputMap.DRIVER_TURN_AXIS);
    public static double kAutonOverrideAxisThreshold = 0.3;

    // =============================================================================
    // Whole-Intake-System Power
    // =============================================================================

    public static double kIntakeTalonPower = 1d;
    public static double kHopperTalonPower = -1d * 0.3333;
    public static double kHopperUnjamTalonPower = 1d;
    public static double kConveyorTalonPower = -1d;
    public static double kShooterVelocity = 2200;

    // ============================================================================
    // Shooter
    // ============================================================================

    public static double kShooterPGain = (1.0/5676d) * 2;
    public static double kShooterFF = 1.0/5676d;//1.0/473.0;
    public static double kShooterMaxVelocity = 5676; //RPMs
    public static int kShooterTalonID = 13;
    public static int kShooterVictorID = 14;
    public static double kShooterCurrentDropThreshold = 0.0; //find later
    public static double kShooterNotShootingCurrentThreshold = 0.0; //find later

    // =============================================================================
    // Motion Magic Constants
    // =============================================================================
    public static int kDriveMotionMagicLoopSlot = 0;
    public static int kDriveMotionMagicCruiseVelocity = 0;
    public static int kDriveMotionMagicMaxAccel = 0;

    // =============================================================================
    // Closed-Loop Position Constants
    // =============================================================================
    public static int kDrivePositionTolerance = 0;
    public static int kDrivePositionLoopSlot = 1;
    public static double kDrivePosition_kP = 0;
    public static double kDrivePosition_kI = 0;
    public static double kDrivePosition_kD = 0;
    public static double kDrivePosition_kF = 0;

    // =============================================================================
    // Closed-Loop Velocity Constants
    // =============================================================================
    public static int kDriveVelocityTolerance = 0;
    public static int kDriveVelocityLoopSlot = 0;
    public static double kDriveVelocity_kP = 1.5234375e-4;
    public static double kDriveVelocity_kI = 0.0;
    public static double kDriveVelocity_kD = 0.001174257 * 4;
    public static double kDriveTrainMaxVelocity = 5676;//(42*5676/60) * 3;

    //    public static double kDriveVelocity_kF = (1023.0 / 1155.0);
    public static double kDriveVelocity_kF = 0.0; // We don't care about this feedforward because we inject our own with ArbitraryFeedforward

    // =============================================================================
    // Turn-To PID constants
    // =============================================================================
    public static PIDGains kPIDGains = new PIDGains( 0.0, 0.0, 0.0, 0.085 );
    public static double kTurnSensitivity = 0.85;
   
    // =============================================================================
    // Robot constants (configure later)
    // TO-DO: Configure torque constant
    // =============================================================================





    // =============================================================================
    // LimeLight Camera Constants
    // Note: These constants need to be recalculated for a specific robot geometry
    // =============================================================================
    public static double llCameraHeightIn = 58.0;
    public static double llCameraToBumperIn = 10.0;
    public static double llCameraAngleDeg = 28.55;

    // Left angle coefficients for angle = a + bx + cx^2
    //    a	0.856905324060421
    //    b	-3.01414088331715
    //    c	-0.0331854848038372
    public static double llLeftACoeff = 0.856905324060421;
    public static double llLeftBCoeff = -3.01414088331715;
    public static double llLeftCCoeff = -0.0331854848038372;

    // Right angle coefficients for angle = a + bx + cx^2
    // a	-54.3943883842204
    // b	-4.53956454545558
    // c	-0.0437470770400814
    public static double llRightACoeff = -54.3943883842204;
    public static double llRightBCoeff = -4.53956454545558;
    public static double llRightCCoeff = -0.0437470770400814;

    // =============================================================================
    // PID TargetLock constants
    // =============================================================================
    public static PIDGains kTargetAngleLockGains = new PIDGains(0.00001, 0.000, 0.0);
    public static PIDGains kTargetDistanceLockGains = new PIDGains( 0.1, 0.0, 0.0);

    public static double kTargetAngleLockMinPower = -1.0;
    public static double kTargetAngleLockMaxPower = 1.0;
    public static double kTargetAngleLockMinInput = -27;
    public static double kTargetAngleLockMaxInput = 27;
    public static double kTargetAngleLockFrictionFeedforward = 0.071544619136622825;
    public static double kTargetAngleLockLostTargetThreshold = 10;
    public static double kYeetPositiveRampRate = 1.0;
    public static double kYeetCruiseOutput = 1.0;
    public static double kYeetNegativeRampRate = -1d;
    public static double kMaxCurrentOutput;
    public static double kJamMaxCycles;


    // =============================================================================
    // Target Constants
    // Note: These constants need to be recalculted for the specific target geometry
    // =============================================================================
    // TODO These values are specific to the targets, not the camera, and may belong elsewhere
    // The current target values assume the limelight processing stream is configured to target
    // the bottom of the vision target
    public enum VisionTarget {
        HatchPort(25.6875), // height of the bottom of the reflective tape in inches for the hatch port
        CargoPort(33.3125), // height of the bottom of the reflective tape in inches for the cargo port
        Ground(0.0), //The ground
        CargoHeight(6.5d);//This may change, not sure what the correct value

        private final double height;

        VisionTarget( double height)  {
            this.height = height;
        }

        /**
         * @return the height
         */
        public double getHeight() {
            return height;
        }
        /**
         * @return the pipelineName
         */
    }

    // =============================================================================
    // 2019 Module Addresses
    // =============================================================================
    public static int kPigeonId = 30;
    public static int kCanifierAddress = 40;

    public static  int kDriveLeftMasterTalonId = 7;//1;
    public static int kDriveLeftMiddleTalonId = 3;
    public static  int kDriveLeftRearTalonId = 5;//5;
    public static  int kDriveRightMasterTalonId = 6;//2;
    public static int kDriveRightMiddleTalonId = 4;
    public static  int kDriveRightRearTalonId = 4;//6;

    //INTAKE
    public static int kIntakeTalonId = 20;
    public static int kHopperTalonId = 9;
    public static int kConveyorTalonId = 11;
    public static int kShooterNeoID = 16;

    public static int kCatapultServoChannel = 9;

    public static EPowerDistPanel[] kDrivePdpSlots = new EPowerDistPanel[]{
            /* Left */
            EPowerDistPanel.CURRENT1,
            EPowerDistPanel.CURRENT2,

            /* Right */
            EPowerDistPanel.CURRENT13,
            EPowerDistPanel.CURRENT14,

    };

    public static int kPowerDistPanelAddress = 21;

    // public static int kElevatorRedundantEncoderAddress = -1;





    public static EPowerDistPanel[] kFourBarPdpSlots = new EPowerDistPanel[] {
            EPowerDistPanel.CURRENT0,
            EPowerDistPanel.CURRENT15
    };



    // TO-DO DO spreadsheet empty


}
