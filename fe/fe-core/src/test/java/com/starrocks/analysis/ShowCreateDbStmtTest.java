// Licensed to the Apache Software Foundation (ASF) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The ASF licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

package com.starrocks.analysis;

import com.starrocks.common.AnalysisException;
import com.starrocks.common.UserException;
import com.starrocks.mysql.privilege.Auth;
import com.starrocks.mysql.privilege.MockedAuth;
import com.starrocks.qe.ConnectContext;
import mockit.Mocked;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ShowCreateDbStmtTest {

    @Mocked
    private Auth auth;
    @Mocked
    private ConnectContext ctx;

    @Before
    public void setUp() {
        MockedAuth.mockedAuth(auth);
        MockedAuth.mockedConnectContext(ctx, "root", "192.168.1.1");
    }

    @Test
    public void testNormal() throws AnalysisException, UserException {
        ShowCreateDbStmt stmt = new ShowCreateDbStmt("testDb");
        stmt.analyze(AccessTestUtil.fetchAdminAnalyzer(true));
        Assert.assertEquals("default_cluster:testDb", stmt.getDb());
        Assert.assertEquals(2, stmt.getMetaData().getColumnCount());
        Assert.assertEquals("SHOW CREATE DATABASE `default_cluster:testDb`", stmt.toString());
    }

    @Test(expected = AnalysisException.class)
    public void testEmptyDb() throws AnalysisException, UserException {
        ShowCreateDbStmt stmt = new ShowCreateDbStmt("");
        stmt.analyze(AccessTestUtil.fetchAdminAnalyzer(false));
        Assert.fail("No exception throws.");
    }
}
