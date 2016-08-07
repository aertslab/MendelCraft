package com.quintenlauwers.interfaces;

/**
 * Created by quinten on 7/08/16.
 */
public interface holdable {
    /**
     * Hold the button in a clicked state like a toggled switch.
     */
    void hold();

    /**
     * Release the button.
     */
    void releaseHold();
}
