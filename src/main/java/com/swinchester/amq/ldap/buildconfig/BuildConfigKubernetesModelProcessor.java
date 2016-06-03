package com.swinchester.amq.ldap.buildconfig;

import java.util.HashMap;
import java.util.Map;

import io.fabric8.openshift.api.model.BuildTriggerPolicy;
import io.fabric8.openshift.api.model.TemplateBuilder;

public class BuildConfigKubernetesModelProcessor {

    public void on(TemplateBuilder builder) {
        builder.addNewBuildConfigObject()
                .withNewMetadata()
                    .withName(ConfigParameters.APP_NAME)
                    .withLabels(getLabels())
                .endMetadata()
                .withNewSpec()
                    .withTriggers(getTriggers())
                    .withNewSource()
                        .withNewGit()
                            .withUri("https://github.com/welshstew/activemq-ldap.git")
                        .endGit()
                        .withContextDir("src/main/docker")
                        .withType("Git")
                    .endSource()
                    .withNewStrategy()
                        .withNewDockerStrategy()
                            .withNewFrom()
                                .withKind("DockerImage")
                                .withName("registry.access.redhat.com/rhel7/rhel")
                            .endFrom()
                        .endDockerStrategy()
                        .withType("Docker")
                    .endStrategy()
                    .withNewOutput()
                        .withNewTo()
                            .withKind("ImageStreamTag")
                            .withName(ConfigParameters.APP_NAME + ":${IS_TAG}")
                        .endTo()
                    .endOutput()
                .endSpec()
            .endBuildConfigObject()
            .build();
    }

    private BuildTriggerPolicy getTriggers() {
        BuildTriggerPolicy policy = new BuildTriggerPolicy();
        policy.setType("ImageChange");

        return policy;
    }

    private Map<String, String> getLabels() {
        Map<String, String> labels = new HashMap<>();
        labels.put("app", ConfigParameters.APP_NAME);
        labels.put("project", ConfigParameters.APP_NAME);
        labels.put("version", "1.0.0-SNAPSHOT");
        labels.put("group", ConfigParameters.GROUP_NAME);

        return labels;
    }

}
