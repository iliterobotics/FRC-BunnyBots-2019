package us.ilite.robot.modules;

import edu.wpi.first.wpilibj.Servo;
import us.ilite.common.config.SystemSettings;

public class Catapult extends Module {

    private Servo mServo;
    private ECatapultState mECatapultState;
    private boolean mHasShot;

    public enum ECatapultState {
        LOCKED(0.0),
        UNLOCKED(0.5);

        private double position;

        ECatapultState(double position) {
            this.position = position;
        }
    }

    public Catapult() {
        mServo = new Servo(SystemSettings.kCatapultServoChannel);
    }

    @Override
    public void modeInit(double pNow) {
        mECatapultState = ECatapultState.LOCKED;
        mHasShot = false;
    }

    @Override
    public void periodicInput(double pNow) {
        mHasShot = mECatapultState == ECatapultState.UNLOCKED;
    }

    @Override
    public void update(double pNow) {
        mServo.set(mECatapultState.position);
    }

    @Override
    public void shutdown(double pNow) {

    }

    public void releaseCatapult() {
        mECatapultState = ECatapultState.UNLOCKED;
    }

    public boolean hasReleased() {
        return mHasShot;
    }
}