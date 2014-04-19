package org.dawnsci.passerelle.cluster.service;

import java.util.concurrent.TimeUnit;

/**
 * A basic listener interface to be notified about a job's execution termination or failure.
 * @author erwindl
 *
 */
public interface JobListener {
  /**
   * Notification that a job execution has finished.
   * Whether the job's result is OK or not can be checked on the job's exitStatus.
   * 
   * @param job
   */
  void jobFinished(IJob job);
  
  /**
   * Notification that a job failed to be executed.
   * This is different than a job execution resulting in an error status!
   * @param job
   * @param t
   */
  void jobFailed(IJob job, Throwable t);
  
  /**
   * Notification that a job was not terminated within the given timeout.
   * 
   * @param job
   * @param timeout
   * @param unit
   */
  void jobTimeOut(IJob job, long timeout, TimeUnit unit);
}