package com.swinchester.amq.ldap.buildconfig;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.fabric8.kubernetes.api.model.PersistentVolumeClaimVolumeSource;
import io.fabric8.kubernetes.api.model.Quantity;
import io.fabric8.kubernetes.api.model.ResourceRequirements;
import io.fabric8.kubernetes.api.model.Volume;
import io.fabric8.openshift.api.model.TemplateBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersistentVolumeClaimKubernetesModelProcessor {

    public void on(TemplateBuilder builder) {
        builder
                .addNewPersistentVolumeClaimObject()
                .withNewMetadata()
                .withName(ConfigParameters.AMQ_BROKER_LDAP_DATA_PVC)
                .withLabels(getLDAPLabels())
                .endMetadata()
                .withNewSpec()
                .withAccessModes("ReadWriteOnce")
                .withResources(getResourceRequirement())
                .endSpec()
                .endPersistentVolumeClaimObject()
//                .addNewPersistentVolumeClaimObject()
//                .withNewMetadata()
//                .withName("il3-broker-ldap-config-pvc")
//                .withLabels(getLDAPLabels())
//                .endMetadata()
//                .withNewSpec()
//                .withAccessModes("ReadWriteOnce")
//                .withResources(getResourceRequirement())
//                .endSpec()
//                .endPersistentVolumeClaimObject()
            .build();
    }

    private ResourceRequirements getResourceRequirement() {
        Map<String, Quantity> requests = new HashMap<>();
        requests.put("storage", new Quantity(ConfigParameters.STORAGE_REQUIREMENT));
        ResourceRequirements rc = new ResourceRequirements();
        rc.setRequests(requests);
        return rc;
    }




    private Map<String, String> getLDAPLabels() {
        return ImmutableMap.<String, String> builder()
                .put("app", ConfigParameters.APP_NAME)
                .put("project", ConfigParameters.APP_NAME)
                .put("version", "1.0.0-SNAPSHOT")
                .put("group", ConfigParameters.GROUP_NAME)
                .build();
    }
}
