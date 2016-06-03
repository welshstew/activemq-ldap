# LDAP for ActiveMQ

Write some doco...!


###  php ldapadmin

    oc new-app --docker-image=osixia/phpldapadmin:0.6.8 -e PHPLDAPADMIN_LDAP_HOSTS=amq-broker-ldap-service -e PHPLDAPADMIN_HTTPS=false  --allow-missing-images
	oc create -f kube/amq-phpldapadmin-svc.yaml
	oc expose svc/phpldapadmin