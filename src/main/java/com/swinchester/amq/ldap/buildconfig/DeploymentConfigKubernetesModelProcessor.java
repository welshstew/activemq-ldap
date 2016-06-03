package com.swinchester.amq.ldap.buildconfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import io.fabric8.kubernetes.api.model.*;
import io.fabric8.openshift.api.model.DeploymentConfigBuilder;
import io.fabric8.openshift.api.model.DeploymentTriggerImageChangeParams;
import io.fabric8.openshift.api.model.DeploymentTriggerPolicy;
import io.fabric8.utils.Lists;

public class DeploymentConfigKubernetesModelProcessor {

    public void on(DeploymentConfigBuilder builder) {
        builder.withSpec(builder.getSpec())
                .editSpec()
                    .withReplicas(1)
                    .withSelector(getSelectors())
                    .withNewStrategy()
                        .withType("Recreate")
                    .endStrategy()
                    .editTemplate()
                        .editSpec()
                            .withContainers(getContainers())
                            .withRestartPolicy("Always")
                            .withVolumes(getVolumes())
                        .endSpec()
                    .endTemplate()
                    .withTriggers(getTriggers())
                .endSpec()
            .build();
    }

    private List<DeploymentTriggerPolicy> getTriggers() {
        DeploymentTriggerPolicy configChange = new DeploymentTriggerPolicy();
        configChange.setType("ConfigChange");

        ObjectReference from = new ObjectReference();
        from.setName(ConfigParameters.APP_NAME + ":${IS_TAG}");
        from.setKind("ImageStreamTag");
        from.setNamespace("${IS_PULL_NAMESPACE}");

        DeploymentTriggerImageChangeParams imageChangeParms = new DeploymentTriggerImageChangeParams();
        imageChangeParms.setFrom(from);
        imageChangeParms.setAutomatic(true);

        DeploymentTriggerPolicy imageChange = new DeploymentTriggerPolicy();
        imageChange.setType("ImageChange");
        imageChange.setImageChangeParams(imageChangeParms);
        imageChangeParms.setContainerNames(Lists.newArrayList(ConfigParameters.APP_NAME));

        List<DeploymentTriggerPolicy> triggers = new ArrayList<DeploymentTriggerPolicy>();
        triggers.add(configChange);
        triggers.add(imageChange);

        return triggers;
    }

    private List<ContainerPort> getPorts() {
        List<ContainerPort> ports = new ArrayList<ContainerPort>();

        ContainerPort ldap = new ContainerPort();
        ldap.setContainerPort(389);
        ldap.setProtocol("TCP");
        ldap.setName("ldap");

        ContainerPort ldaps = new ContainerPort();
        ldaps.setContainerPort(636);
        ldaps.setProtocol("TCP");
        ldaps.setName("ldaps");

        ports.add(ldap);
        ports.add(ldaps);

        return ports;
    }

    private Container getContainers() {
        Container container = new Container();
        container.setImage("${IS_PULL_NAMESPACE}/" + ConfigParameters.APP_NAME + ":${IS_TAG}");
        container.setImagePullPolicy("Always");
        container.setName(ConfigParameters.APP_NAME);
        container.setPorts(getPorts());
        container.setLivenessProbe(getProbe());
        container.setReadinessProbe(getProbe());
        container.setVolumeMounts(getVolumeMounts());
        return container;
    }


    private List<Volume> getVolumes(){

        Volume ldapDataVol = new Volume();
        ldapDataVol.setName(ConfigParameters.AMQ_BROKER_LDAP_DATA);
        ldapDataVol.setPersistentVolumeClaim(new PersistentVolumeClaimVolumeSource(ConfigParameters.AMQ_BROKER_LDAP_DATA_PVC, false));

//        Volume ldapSecret = new Volume();
//        ldapSecret.setSecret(new SecretVolumeSource(ConfigParameters.SECRET_NAME));
//        ldapSecret.setName(ConfigParameters.SECRET_NAME);

//        return new ImmutableList.Builder<Volume>().add(ldapDataVol).add(ldapSecret).build();

        return new ImmutableList.Builder<Volume>().add(ldapDataVol).build();

    }


    private List<VolumeMount> getVolumeMounts(){
        return new ImmutableList.Builder<VolumeMount>()
//                .add(new VolumeMount(ConfigParameters.ETC_OPENLDAP_CERTS,ConfigParameters.AMQ_LDAP_SECRET,true))
                .add(new VolumeMount(ConfigParameters.AMQ_BROKER_LDAP_DATA_DIRECTORY,ConfigParameters.AMQ_BROKER_LDAP_DATA,false))
                .build();

    }

//    private Map<String, String> getSelectors() {
//        Map<String, String> selectors = new HashMap<>();
//        selectors.put("app", ConfigParameters.APP_NAME);
//        selectors.put("deploymentconfig", ConfigParameters.APP_NAME);
//
//        return selectors;
//    }

    private Map<String, String> getSelectors() {
        Map<String, String> labels = new HashMap<>();
        labels.put("app", ConfigParameters.APP_NAME);
        labels.put("project", ConfigParameters.APP_NAME);
        labels.put("version", "1.0.0-SNAPSHOT");
        labels.put("group", ConfigParameters.GROUP_NAME);

        return labels;
    }

    private Probe getProbe() {
        TCPSocketAction ldapAction = new TCPSocketAction();
        ldapAction.setPort(new IntOrString(389));

        Probe probe = new Probe();
        probe.setInitialDelaySeconds(new Integer(15));
        probe.setTimeoutSeconds(new Integer(5));
        probe.setTcpSocket(ldapAction);

        return probe;
    }
}
