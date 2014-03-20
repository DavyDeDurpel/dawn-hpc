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
package org.dawnsci.drmaa.executor;

import org.ggf.drmaa.SessionFactory;

/**
 * A SessionFactory implementation that executes jobs on a JDK ExecutorService.
 * 
 * @author erwindl
 *
 */
public class SessionFactoryImpl extends SessionFactory {
  private SessionImpl session;

  public void activate() {
    SessionFactory.setFactory(this);
  }

  public void deactivate() {
    synchronized (SessionFactory.class) {
      if (this.equals(SessionFactory.getFactory())) {
        SessionFactory.setFactory(null);
      }
    }
  }

  public SessionImpl getSession() {
    synchronized (this) {
      if (session == null) {
        session = new SessionImpl(3);
      }
    }
    return session;
  }
}
