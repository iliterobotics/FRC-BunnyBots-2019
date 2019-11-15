package us.ilite.robot.modules;

import com.revrobotics.CANSparkMax;

public class Hopper extends Module {

    private CANSparkMax mHopperMotor;

    public enum Outake {

        SENDTOSHOOTER,
        SENDTOINTAKE,
        HOLDBALLS;

    }

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

