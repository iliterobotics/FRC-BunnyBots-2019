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

    // Write input handling module update methods below the example.
    // Note: they must return the object Void not the primitive datatype void

    /* Ex. (Still no need for a return statement)
    public Void updateDriveTrain() {
        if (button pressed) {
            do this;
        else {
            do this;
        }
    }
     */

}
