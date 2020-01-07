package us.ilite.robot.auto.paths;

import com.team254.lib.geometry.Pose2d;
import com.team254.lib.geometry.Rotation2d;

/**
 * Defines the starting location of the *center* of the robot as (x, y, theta) coordinates.
 * Units are in inches.
 */
public class StartingPoses {

//    public static final Pose2d kLeftStart = new Pose2d();
//    public static final Pose2d kMiddleStart = new Pose2d();
    public static final Pose2d kStart = new Pose2d();
    /**
     * X: 4 feet + length from back to center
     * Y: 11.5 feet + length from side of robot to center
     */
//    public static final Pose2d kSideStart = new Pose2d((4.0 * 12.0) + RobotDimensions.kBackToCenter, (11.5 * 12.0) + RobotDimensions.kSideToCenter, Rotation2d.fromDegrees(0.0));
    // This is Pose A
    public static Pose2d mPose2d1 = new Pose2d( 94.6 , 120 , Rotation2d.fromDegrees(0) );
    // This is Pose B
    public static Pose2d mPose2d2 = new Pose2d( 55 , 120 , Rotation2d.fromDegrees(0) );
    // This is Pose C
    public static Pose2d mPose2d3 = new Pose2d( 124 , 120 , Rotation2d.fromDegrees(0) );


}
