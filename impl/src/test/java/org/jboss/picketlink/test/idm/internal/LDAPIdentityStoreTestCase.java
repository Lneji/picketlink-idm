/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.picketlink.test.idm.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.jboss.picketlink.idm.internal.LDAPIdentityStore;
import org.jboss.picketlink.idm.model.Group;
import org.jboss.picketlink.idm.model.Role;
import org.jboss.picketlink.idm.model.User;
import org.junit.Before;
import org.junit.Test;
import org.picketbox.test.ldap.AbstractLDAPTest;
/**
 * Unit test the {@link LDAPIdentityStore}
 * @author anil saldhana
 * @since Aug 30, 2012
 */
public class LDAPIdentityStoreTestCase extends AbstractLDAPTest {
    @Before
    public void setup() throws Exception {
        super.setup();
        importLDIF("ldap/users.ldif");
    }
    
    @Test
    public void testLDAPIdentityStore() throws Exception{
        LDAPIdentityStore store = new LDAPIdentityStore();
        Map<String,String> config = new HashMap<String,String>();
        config.put("userDNSuffix", "ou=People,dc=jboss,dc=org");
        config.put("roleDNSuffix", "ou=Roles,dc=jboss,dc=org");
        config.put("groupDNSuffix", "ou=Groups,dc=jboss,dc=org");
        config.put("url", "ldap://localhost:10389");
        config.put("bindDN", adminDN);
        config.put("password", adminPW);
        
        store.config(config);
        
        //Users
        User user = store.createUser("Pedro Silva");
        assertNotNull(user);
        
        User pedro = store.getUser("Pedro Silva");
        assertNotNull(pedro);
        assertEquals("Pedro Silva", pedro.getFullName());
        assertEquals("Pedro", pedro.getFirstName());
        assertEquals("Silva", pedro.getLastName());
        
        //Roles
        Role role = store.createRole("testRole");
        assertNotNull(role);
        assertEquals("testRole", role.getName());
        
        Role ldapRole = store.getRole("testRole");
        assertNotNull(ldapRole);
        assertEquals("testRole", ldapRole.getName());
        
        
        //Groups
        Group ldapGroup = store.createGroup("PicketBox Team", null);
        assertNotNull(ldapGroup);
        
        Group retrievedLDAPGroup = store.getGroup("PicketBox Team");
        assertNotNull(retrievedLDAPGroup);
        assertNull(retrievedLDAPGroup.getParentGroup());
        
        //Parent Groups Now
        Group devGroup = store.createGroup("Dev", ldapGroup);
        assertNotNull(devGroup);
        
        Group retrievedDevGroup = store.getGroup("Dev");
        assertNotNull(retrievedDevGroup);
        Group parentOfDevGroup = retrievedDevGroup.getParentGroup();
        assertNotNull(parentOfDevGroup);
        assertEquals("PicketBox Team", parentOfDevGroup.getName());
        
        //Deal with removal of users, roles and groups
        store.removeUser(pedro);
        store.removeRole(ldapRole);
        store.removeGroup(ldapGroup);
        store.removeGroup(devGroup);
        
        pedro = store.getUser("Pedro Silva");
        assertNull(pedro);
        ldapRole = store.getRole("testRole");
        assertNull(ldapRole);
        assertNull(store.getGroup("Dev"));
        assertNull(store.getGroup("PicketBox Team"));
    }
}