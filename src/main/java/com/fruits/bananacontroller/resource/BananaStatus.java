package com.fruits.bananacontroller.resource;

/**
 * Resource status - this is set by the controller (if necessary), and not touched by the user directly.
 * Represents the actual state of the resource, as recorded by the controller.
 */
public class BananaStatus {
    private Boolean readyToBeEaten;

    public Boolean getReadyToBeEaten() {
        return readyToBeEaten;
    }

    public void setReadyToBeEaten(Boolean readyToBeEaten) {
        this.readyToBeEaten = readyToBeEaten;
    }
}
