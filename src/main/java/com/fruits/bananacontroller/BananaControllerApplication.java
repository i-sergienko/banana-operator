package com.fruits.bananacontroller;

import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.javaoperatorsdk.operator.Operator;
import io.javaoperatorsdk.operator.api.ResourceController;
import io.javaoperatorsdk.operator.config.runtime.DefaultConfigurationService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class BananaControllerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BananaControllerApplication.class, args);
    }

    /**
     * Kubernetes client to access the Kubernetes API.
     * If running locally, it will use credentials from {@code $HOME/.kube/config}.
     * If running in a K8s cluster, it will use the ServiceAccount credentials.
     * <p>
     * No need to configure anything on app level.
     */
    @Bean
    public KubernetesClient kubernetesClient() {
        return new DefaultKubernetesClient();
    }

    /**
     * Operator Framework entry point - on startup it registers all the ResourceController classes,
     * so they can start watching the custom resources.
     */
    @Bean
    public Operator operator(
            KubernetesClient client,
            List<ResourceController> controllers
    ) {
        Operator operator = new Operator(client, DefaultConfigurationService.instance());
        controllers.forEach(operator::register);
        return operator;
    }
}
