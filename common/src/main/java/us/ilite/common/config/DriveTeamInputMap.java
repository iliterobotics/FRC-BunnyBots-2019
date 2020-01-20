package us.ilite.common.config;


import us.ilite.common.types.input.ELogitech310;

public class DriveTeamInputMap {

    public static final ELogitech310
  
    DRIVER_TURN_AXIS = ELogitech310.RIGHT_X_AXIS,
    DRIVER_THROTTLE_AXIS = ELogitech310.LEFT_Y_AXIS,

    DRIVER_LIMELIGHT_LOCK_TARGET = ELogitech310.B_BTN,
    DRIVER_LIMELIGHT_LOCK_BALL = ELogitech310.A_BTN,
    DRIVER_LIMELIGHT_LOCK_TARGET_ZOOM = ELogitech310.Y_BTN,
    DRIVER_LIMELIGHT_LOCK_BALL_DUAL = ELogitech310.START,
    DRIVER_LIMELIGHT_LOCK_BALL_TRI = ELogitech310.X_BTN,

            
    OPERATOR_SHOOT = ELogitech310.A_BTN/*R_BTN*/,               //hopper forward, conveyor up, shooter shoot
    OPERATOR_INTAKE = ELogitech310.LEFT_TRIGGER_AXIS,           //intake in/up
    OPERATOR_REVERSE_INTAKE = ELogitech310.RIGHT_TRIGGER_AXIS,   //intake out/down
    OPERATOR_HOPPER_UNJAM = ELogitech310.X_BTN,

    DRIVER_YEET_LEFT = ELogitech310.L_BTN,
    DRIVER_YEET_RIGHT = ELogitech310.R_BTN,
    DRIVER_REDUCE_TURN_AXIS = ELogitech310.RIGHT_TRIGGER_AXIS,
    DRIVER_NITRO_BUTTON = ELogitech310.LEFT_TRIGGER_AXIS,
    OPERATOR_CATAPULT_BTN = ELogitech310.START;
}
