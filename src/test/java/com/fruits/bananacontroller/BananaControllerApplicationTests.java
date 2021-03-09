package com.fruits.bananacontroller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fruits.bananacontroller.resource.Banana;
import com.fruits.bananacontroller.resource.BananaSpec;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.base.CustomResourceDefinitionContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BananaControllerApplicationTests {
    @Autowired
    private KubernetesClient kubernetesClient;
    @Autowired
    private ObjectMapper mapper;

    @Test
    public void bananaIsPainted() {
        // Create a banana in the 'default' namespace with spec.color = 'white' and metadata.name = 'white-banana'
        BananaSpec spec = new BananaSpec();
        spec.setColor("white");
        Banana banana = new Banana();
        banana.getMetadata().setName("white-banana");
        banana.getMetadata().setNamespace("default");
        banana.setSpec(spec);

        // There are no bananas before we create one
        assertEquals(0, listBananas("default").size());

        // Create a banana
        applyBanana(banana);
        // Now there is one banana - the one we created
        List<Banana> bananas = listBananas("default");
        assertEquals(1, bananas.size());
        assertEquals(banana.getMetadata().getName(), bananas.get(0).getMetadata().getName());
        assertEquals(banana.getSpec().getColor(), bananas.get(0).getSpec().getColor());
        // Color in the 'status' subresource is null - the operator hasn't run yet
        assertNull(bananas.get(0).getStatus().getColor());

        // Wait for the banana to be painted
        safeWait(4000);

        bananas = listBananas("default");
        assertEquals(1, bananas.size());
        assertNotNull(bananas.get(0).getStatus().getColor());
        assertEquals(banana.getSpec().getColor(), bananas.get(0).getStatus().getColor());

        // Delete the banana
        deleteBanana(banana);
        safeWait(3000);

        // The banana list should again be empty
        assertEquals(0, listBananas("default").size());
    }

    private void applyBanana(Banana banana) {
        try {
            kubernetesClient.customResource(bananaDefinition()).createOrReplace(banana.getMetadata().getNamespace(), writeBanana(banana));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private void deleteBanana(Banana banana) {
        try {
            kubernetesClient.customResource(bananaDefinition()).delete(banana.getMetadata().getNamespace(), banana.getMetadata().getName());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private List<Banana> listBananas(String namespace) {
        List<Object> bananaList = (List<Object>) kubernetesClient.customResource(bananaDefinition()).list(namespace).getOrDefault("items", emptyList());
        return bananaList.stream().map(this::readBanana).collect(Collectors.toList());
    }

    private Banana readBanana(Object raw) {
        try {
            return mapper.readValue(mapper.writeValueAsString(raw), Banana.class);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }

    private String writeBanana(Banana banana) {
        try {
            return mapper.writeValueAsString(banana);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }

    private CustomResourceDefinitionContext bananaDefinition() {
        return new CustomResourceDefinitionContext.Builder()
                .withGroup("fruits.com")
                .withVersion("v1")
                .withKind("Banana")
                .withPlural("bananas")
                .withScope("Namespaced")
                .build();
    }

    private void safeWait(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }
}
