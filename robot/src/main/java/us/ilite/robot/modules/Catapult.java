package us.ilite.robot.modules;

import edu.wpi.first.wpilibj.Servo;
import us.ilite.common.config.SystemSettings;

public class Catapult extends Module {

    private Servo mServo;

    private CatapultState mCatapultState;

    public enum CatapultState {
        LOCKED(0.0),
        UNLOCKED(0.5);

        private double position;

        CatapultState(double position) {
            this.position = position;
        }
    }

    public Catapult() {
        mServo = new Servo(SystemSettings.kCatapultServoChannel);
    }

    @Override
    public void modeInit(double pNow) {
        mCatapultState = CatapultState.LOCKED;
    }

    @Override
    public void periodicInput(double pNow) {

    }

    @Override
    public void update(double pNow) {
        mServo.set(mCatapultState.position);
    }

    @Override
    public void shutdown(double pNow) {

    }

    public void releaseCatapult() {
        mCatapultState = CatapultState.UNLOCKED;
    }
}