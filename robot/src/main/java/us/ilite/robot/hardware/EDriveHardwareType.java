package us.ilite.robot.hardware;

import us.ilite.common.config.SystemSettings;

public enum EDriveHardwareType {
    ARYA(new AryaDriveHardware(SystemSettings.kDriveGearboxRatio)),
    PRACTICE(new SrxDriveHardware()),
    MASTER(new NeoDriveHardware(SystemSettings.kDriveGearboxRatio));

    private IDriveHardware mDriveHardware;

    EDriveHardwareType(IDriveHardware pDriveHardware) {
        mDriveHardware = pDriveHardware;
    }

    public IDriveHardware getDriveHardware() {
        return mDriveHardware;
    }
}
