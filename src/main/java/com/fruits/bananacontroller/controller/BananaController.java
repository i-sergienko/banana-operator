package com.fruits.bananacontroller.controller;

import com.fruits.bananacontroller.resource.Banana;
import com.fruits.bananacontroller.resource.BananaStatus;
import io.javaoperatorsdk.operator.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * This is the heart of the application - BananaController will react to changes made to Banana resources.
 * <p>
 * Any time a Banana is modified (i.e. any time you run {@code kubectl apply/delete} with a Banana resource),
 * this controller will get the modification/deletion event and will be able to react to it.
 * <p>
 * <p>
 * IMPORTANT: the logic in both {@code createOrUpdateResource} and {@code deleteResource} methods has to be idempotent,
 * that is, invoking one method with the same resource multiple times should produce the same result.
 * <p>
 * It must be idempotent because Controller apps can only guarantee "at least once" processing - each event is guaranteed
 * to be processed, but one event might be processed multiple times (e.g. if the controller crashes during processing).
 */
@Component
@Controller
public class BananaController implements ResourceController<Banana> {
    private static final Logger log = LoggerFactory.getLogger(BananaController.class);

    /**
     * Reacts to a {@code Banana} being created or updated.
     * That is, normally it's invoked any time you run {@code kubectl apply -f banana.yaml}.
     * <p>
     * There is no way to distinguish between a creation and modification events, so they should be treated as the same.
     *
     * @return {@code UpdateControl.noUpdate()} - if you don't want to modify the {@code Banana} resource after handler execution.
     * <p>
     * {@code UpdateControl.updateCustomResource(resource)} - if you want to modify the {@code BananaSpec} content after
     * handler execution. WARNING: this will trigger the handler again!
     * <p>
     * {@code UpdateControl.updateCustomResourceAndStatus(resource)} - if you want to modify both the {@code BananaSpec}
     * and {@code BananaStatus} after execution. Will trigger the handler again.
     * <p>
     * {@code UpdateControl.updateStatusSubResource(resource)} - if you want to modify just the {@code BananaStatus}
     * content. This will NOT trigger the handler again.
     */
    @Override
    public UpdateControl<Banana> createOrUpdateResource(Banana resource, Context<Banana> context) {
        if (resource.getStatus() == null || !resource.getSpec().getColor().equals(resource.getStatus().getColor())) {
            // Status is empty, or actual color is different from the desired color.
            // Simulate the banana being painted and update the Status subresource.
            BananaStatus status = new BananaStatus();
            status.setColor(resource.getSpec().getColor());
            resource.setStatus(status);
            // Pretend that painting the Banana takes 3 seconds
            paintBanana(resource);

            return UpdateControl.updateStatusSubResource(resource);
        } else {
            log.info(String.format(
                    "Banana %s is already colored %s. Nothing to do.",
                    resource.getMetadata().getName(),
                    resource.getSpec().getColor())
            );
            return UpdateControl.noUpdate();
        }
    }

    /**
     * Reacts to a {@code Banana} being deleted.
     * That is, normally it's invoked any time you run {@code kubectl delete -f banana.yaml}.
     *
     * @return {@code DeleteControl.DEFAULT_DELETE} - in case the deletion handling was successful and you want the
     * manifest removed from the cluster.
     * <p>
     * {@code DeleteControl.NO_FINALIZER_REMOVAL} - in case you want to prevent the manifest from being deleted.
     * (for example, the deletion handling wasn't successful and
     * an admin has to resolve the problem and remove it manually).
     */
    @Override
    public DeleteControl deleteResource(Banana resource, Context<Banana> context) {
        return DeleteControl.DEFAULT_DELETE;
    }

    private void paintBanana(Banana banana) {
        try {
            log.info(String.format(
                    "Banana %s is being painted %s",
                    banana.getMetadata().getName(),
                    banana.getSpec().getColor())
            );
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
