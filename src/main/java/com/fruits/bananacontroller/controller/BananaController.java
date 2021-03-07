package com.fruits.bananacontroller.controller;

import com.fruits.bananacontroller.resource.Banana;
import io.javaoperatorsdk.operator.api.Context;
import io.javaoperatorsdk.operator.api.DeleteControl;
import io.javaoperatorsdk.operator.api.ResourceController;
import io.javaoperatorsdk.operator.api.UpdateControl;

public class BananaController implements ResourceController<Banana> {
    @Override
    public DeleteControl deleteResource(Banana resource, Context<Banana> context) {
        return null;
    }

    @Override
    public UpdateControl<Banana> createOrUpdateResource(Banana resource, Context<Banana> context) {
        return null;
    }
}
