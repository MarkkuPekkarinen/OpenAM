/*
 * The contents of this file are subject to the terms of the Common Development and
 * Distribution License (the License). You may not use this file except in compliance with the
 * License.
 *
 * You can obtain a copy of the License at legal/CDDLv1.0.txt. See the License for the
 * specific language governing permission and limitations under the License.
 *
 * When distributing Covered Software, include this CDDL Header Notice in each file and include
 * the License file at legal/CDDLv1.0.txt. If applicable, add the following below the CDDL
 * Header, with the fields enclosed by brackets [] replaced by your own identifying
 * information: "Portions copyright [year] [name of copyright owner]".
 *
 * Copyright 2019 Open Identity Platform Community.
 */

package org.openidentityplatform.openam.cassandra;

import java.util.concurrent.CompletionStage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.AsyncResultSet;
import com.datastax.oss.driver.api.core.cql.ExecutionInfo;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Statement;


public class ExecuteCallback {
	final static Logger logger=LoggerFactory.getLogger(ExecuteCallback.class.getName());
	
	final CqlSession session;
	final Statement<?> statement;
	Long start;
	
	public ExecuteCallback(String profile,CqlSession session,Statement<?> statement){
		this.session=session;
		this.statement=statement;
		this.statement.setExecutionProfileName(profile);
	}
	
	public ResultSet execute(){
		start=System.currentTimeMillis();
		try{
			final ResultSet result=session.execute(statement);
			onSuccess(result.getExecutionInfo());
			return result;
		}catch(Throwable e){
			onFailure(e);
			throw e;
		}

	}
	
	public CompletionStage<AsyncResultSet> executeAsync(){
		start=System.currentTimeMillis();
		final CompletionStage<AsyncResultSet> sessionStage = session.executeAsync(statement);
		sessionStage.whenComplete(
		    (version, error) -> {
		        if (error != null) {
		          System.out.printf("Failed to retrieve the version: %s%n", error.getMessage());
		        } else {
		          System.out.printf("Server version: %s%n", version);
		          onSuccess(version.getExecutionInfo());
		        }
		      });
		return sessionStage;
	}


	public void onSuccess(ExecutionInfo result) {
		if (logger.isTraceEnabled())
			logger.trace("{} ms {} {} ({}) ->{} {}"
					,System.currentTimeMillis()-start
					,statement
					,statement.getConsistencyLevel()
					,result.getCoordinator() 
					//,result.getQueryTrace()
			);
	}
	
	public void onFailure(Throwable t) {
		logger.warn("{} ms {} {}: {} {}"
				,System.currentTimeMillis()-start
				,statement
				,statement.getConsistencyLevel()
				,t.getMessage()
		);
	}
}
