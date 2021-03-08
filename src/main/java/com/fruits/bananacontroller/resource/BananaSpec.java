package com.fruits.bananacontroller.resource;

/**
 * Resource specification - normally set by a human user through YAML.
 * Represents the desired state of the resource.
 */
public class BananaSpec {
    private String color;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
