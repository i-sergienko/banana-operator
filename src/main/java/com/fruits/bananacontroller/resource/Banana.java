package com.fruits.bananacontroller.resource;

import io.fabric8.kubernetes.api.model.Namespaced;
import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.model.annotation.Group;
import io.fabric8.kubernetes.model.annotation.Kind;
import io.fabric8.kubernetes.model.annotation.Version;

/**
 * Our Custom Resource.
 *
 * {@code implements CustomResource<BananaSpec, BananaStatus>} means that this is a custom resource,
 * with a {@code spec} field of type {@code BananaSpec},
 * and a {@code status} field of type {@code BananaStatus} as the second argument.
 *
 * {@code apiVersion}/{@code kind}/{@code metadata} fields are already included in the base {@code CustomResource} class.
 *
 * {@code implements Namespaced} means it's a namespaced resource (like {@code Pod}, {@code Deployment}, etc.),
 * as opposed to cluster-scoped (like {@code ClusterRole}).
 */
@Group("fruits.com") // @Group is required
@Version("v1") // @Version is required
@Kind("Banana") // @Kind is optional - if not present, the annotated class name would be used instead
public class Banana extends CustomResource<BananaSpec, BananaStatus> implements Namespaced {
}
