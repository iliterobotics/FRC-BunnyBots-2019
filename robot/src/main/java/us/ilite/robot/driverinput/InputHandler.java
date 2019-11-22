package us.ilite.robot.driverinput;

import java.util.function.Supplier;

public class InputHandler {
    private Supplier[] mMethods;

    public InputHandler() {
        mMethods = new Supplier[]{/*() -> test()*/}; //Add methods to list like this
    }

    public void handleInputs() {
        for (Supplier s : mMethods) {
            s.get();
        }
    }

    // Write input handling module update methods below here. They must return object Void

}
