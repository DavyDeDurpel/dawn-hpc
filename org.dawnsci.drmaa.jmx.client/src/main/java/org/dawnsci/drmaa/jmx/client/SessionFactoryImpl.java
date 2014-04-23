/*
 * Copyright 2014 Diamond Light Source Ltd.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dawnsci.drmaa.jmx.client;

import javax.management.JMX;
import javax.management.MBeanServerConnection;

import org.dawnsci.drmaa.jmx.SessionFactoryMXBean;
import org.dawnsci.drmaa.jmx.SessionMXBean;
import org.dawnsci.drmaa.jmx.connection.ConnectionManager;
import org.ggf.drmaa.Session;
import org.ggf.drmaa.SessionFactory;

/**
 * 
 * @author erwindl
 * 
 */
public class SessionFactoryImpl extends SessionFactory {
  private ConnectionManager connMgr;
  private SessionFactoryMXBean sfProxy;
  private SessionImpl sessionImpl;

  // TODO find a way to notice remote connectivity issues and retry the connection
  // or disable this service when it keeps on failing.
  public void activate() throws Exception {
    connMgr = new ConnectionManager();
    MBeanServerConnection serverConnection = connMgr.getServerConnection(100);
    sfProxy = JMX.newMXBeanProxy(serverConnection, connMgr.getSessionFactoryMxBeanName(), SessionFactoryMXBean.class);
    SessionFactory.setFactory(this);
  }

  public void deactivate() {
    connMgr = null;
  }

  @Override
  public Session getSession() {
    if (sessionImpl == null) {
      sessionImpl = new SessionImpl(getSessionMXBean(), this);
    }
    return sessionImpl;
  }

  // TODO evaluate if these JMX proxies are sufficiently stable to assign them once at activation time,
  // or whether we need to refresh them regularly...
  SessionMXBean getSessionMXBean() {
    try {
      return sfProxy.createSession();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public void clearSession(SessionImpl session) {
    if(sessionImpl==session) {
      sessionImpl = null;
    }
  }
}
