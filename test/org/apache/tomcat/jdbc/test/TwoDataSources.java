/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.apache.tomcat.jdbc.test;

import java.sql.Connection;

import org.apache.tomcat.jdbc.pool.DataSourceProxy;

public class TwoDataSources extends DefaultTestCase {

    public TwoDataSources(String name) {
        super(name);
    }
    
    public void testTwoDataSources() throws Exception {
        org.apache.tomcat.jdbc.pool.DataSource d1 = this.createDefaultDataSource();
        org.apache.tomcat.jdbc.pool.DataSource d2 = this.createDefaultDataSource();
        d1.setRemoveAbandoned(true);
        d1.setRemoveAbandonedTimeout(10);
        d1.setTimeBetweenEvictionRunsMillis(1000);
        d2.setRemoveAbandoned(false);
        Connection c1 = d1.getConnection();
        Connection c2 = d2.getConnection();
        Thread.sleep(5000);
        try {
            c1.createStatement();
            assertTrue("Connection should have been abandoned.",false);
        }catch (Exception x) {
            assertTrue("This is correct, c1 is abandoned",true);
        }

        try {
            c2.createStatement();
            assertTrue("Connection should not have been abandoned.",true);
        }catch (Exception x) {
            assertTrue("Connection c2 should be working",false);
        }
        try {
            assertTrue("Connection should have been closed.",c1.isClosed());
        }catch (Exception x) {
            assertTrue("This is correct, c1 is closed",true);
        }
        try {
            assertFalse("Connection c2 should not have been closed.",c2.isClosed());
        }catch (Exception x) {
            assertTrue("Connection c2 should be working",false);
        }
        

        
    }

}
