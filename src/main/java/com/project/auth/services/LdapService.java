package com.project.auth.services;

import java.util.List;
import java.util.jar.Attributes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Service;

import javax.naming.Name;

import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.query.LdapQueryBuilder;

@Service
public class LdapService {

    @Autowired
    private LdapTemplate ldapTemplate;

    @Autowired
    private LdapContextSource contextSource;

    public void createUser(String uid, String cn, String sn, String password) {
        Name dn = LdapNameBuilder.newInstance()
                .add("ou", "people")
                .add("uid", uid)
                .build();

        DirContextAdapter context = new DirContextAdapter(dn);
        context.setAttributeValues("objectClass", new String[] { "inetOrgPerson", "organizationalPerson", "person", "top" });
        context.setAttributeValue("cn", cn);
        context.setAttributeValue("sn", sn);
        context.setAttributeValue("uid", uid);
        context.setAttributeValue("userPassword", password);

        ldapTemplate.bind(context);
    }

        // Retrieve all entries from an Organizational Unit (OU)
    public List<String> getAllUsersFromOu(String ouName) {
        LdapQuery query = LdapQueryBuilder.query()
                .base("dc=auth,dc=project,dc=com")  // Set the Organizational Unit (OU) to search in
                .where("objectClass").is("person");  // Filter to retrieve only person entries
        System.out.println(query.base().toString());
        return ldapTemplate.search(query, (AttributesMapper<String>) attrs -> {
            return attrs.get("cn").get().toString();
        });
    }

    public void authenticate(String username, String password) {
        contextSource
          .getContext(
            "cn=" + 
             username + 
             ",ou=users,dc=auth,dc=project,dc=com", password);
    }
}