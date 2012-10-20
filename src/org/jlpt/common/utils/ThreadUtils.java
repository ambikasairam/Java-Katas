package org.jlpt.common.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Contains thread-related utility methods.
 * 
 * @author BJ Peter DeLaCruz
 */
public final class ThreadUtils {

  /** Do not instantiate this class. */
  private ThreadUtils() {
    // Empty constructor.
  }

  private static final int NUM_THREADS = 10;

  /** A fixed thread pool that is available to all classes on the client or the server. */
  public static final ExecutorService SHARED_THREAD_POOL = Executors
      .newFixedThreadPool(NUM_THREADS);

}
