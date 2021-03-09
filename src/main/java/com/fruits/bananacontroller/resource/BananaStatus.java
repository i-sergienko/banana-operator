package com.fruits.bananacontroller.resource;

/**
 * Resource status - this is set by the controller (if necessary), and not touched by the user directly.
 * Represents the actual state of the resource, as recorded by the controller.
 */
public class BananaStatus {
    private String color;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
