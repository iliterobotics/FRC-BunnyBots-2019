package us.ilite.robot.driverinput;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Supplier;

public class InputHandler {
    private Supplier[] mMethods;

    public InputHandler() {
        mMethods = new Supplier[]{}; //Add method names to this list
    }

    public void handleInputs() {
        for (Supplier s : mMethods) {
            s.get();
        }
    }

    // Write input handling module update methods below here

}
