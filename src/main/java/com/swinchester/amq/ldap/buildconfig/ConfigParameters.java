package com.swinchester.amq.ldap.buildconfig;

/**
 * Created by swinchester on 17/05/2016.
 */
public class ConfigParameters {

    public static final String SECRET_NAME = "amq-ldap-secret";
    public static final String APP_NAME = "activemq-ldap";
    public static final String GROUP_NAME = "amq";
    public static final String AMQ_BROKER_LDAP_DATA = "amq-broker-ldap-data";
    public static final String AMQ_BROKER_LDAP_DATA_DIRECTORY = "/var/lib/ldap";

    public static final String AMQ_BROKER_LDAP_CONFIG = "amq-ldap-config";
    public static final String AMQ_BROKER_LDAP_CONFIG_DIRECTORY = "/etc/openldap";

    public static final String AMQ_BROKER_LDAP_DATA_PVC = "amq-broker-ldap-data-pvc";
    public static final String AMQ_BROKER_LDAP_CONFIG_PVC = "amq-broker-ldap-config-pvc";
    public static final String STORAGE_REQUIREMENT = "64Mi";

//    public static final String ETC_OPENLDAP_CERTS = "/tmp/certs/pk12";
    //il3-ldap-secret contains the certificates and should be shared across amq-broker-ldap and amq-ldap
    public static final String IL_LDAP_SECRET = "amq-ldap-secret";

}
