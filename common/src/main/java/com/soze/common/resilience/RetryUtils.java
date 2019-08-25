package com.soze.common.resilience;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * Some static methods for circuit breakers and retries. Uses resilience4j library underneath.
 */
public class RetryUtils {

	private static final Logger LOG = LoggerFactory.getLogger(RetryUtils.class);

	public static void retry(int attempts, Duration waitDuration, Runnable runnable) {
		CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults("defaultBreaker");

		RetryConfig retryConfig = RetryConfig.custom().maxAttempts(attempts).waitDuration(waitDuration).build();
		Retry retry = Retry.of("defaultRetry", retryConfig);

		runnable = Retry.decorateRunnable(retry, runnable);
		runnable = CircuitBreaker.decorateRunnable(circuitBreaker, runnable);

		Try.runRunnable(runnable);
	}

	public static <T> T retry(int attempts, Duration waitDuration, Callable<T> callable) {
		long startTime = System.currentTimeMillis();
		CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults("defaultBreaker");

		RetryConfig retryConfig = RetryConfig.custom().maxAttempts(attempts).waitDuration(waitDuration).build();
		Retry retry = Retry.of("defaultRetry", retryConfig);

		callable = Retry.decorateCallable(retry, callable);
		callable = CircuitBreaker.decorateCallable(circuitBreaker, callable);

		return Try.ofCallable(callable)
							.onFailure(t -> {
								LOG.warn("", t);
								long endTime = System.currentTimeMillis();
								LOG.trace("Took {} ms for a retried function until failure", TimeUnit.MILLISECONDS.toMillis(endTime - startTime));
							})
							.onSuccess(result -> {
								long endTime = System.currentTimeMillis();
								LOG.trace("Took {} ms for a retried function until success", TimeUnit.MILLISECONDS.toMillis(endTime - startTime));
							})
							.get();
	}

}
