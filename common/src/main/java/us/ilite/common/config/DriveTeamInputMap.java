package us.ilite.common.config;


import us.ilite.common.types.input.ELogitech310;

public class DriveTeamInputMap {

    public static final ELogitech310
            
    OPERATOR_SHOOT = ELogitech310.A_BTN/*R_BTN*/,               //hopper forward, conveyor up, shooter shoot
    OPERATOR_INTAKE = ELogitech310.LEFT_TRIGGER_AXIS,           //intake in/up
    OPERATOR_REVERSE_INTAKE = ELogitech310.RIGHT_TRIGGER_AXIS,   //intake out/down
    OPERATOR_HOPPER_UNJAM = ELogitech310.X_BTN;

}
