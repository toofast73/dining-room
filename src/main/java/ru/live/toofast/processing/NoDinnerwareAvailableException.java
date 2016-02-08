package ru.live.toofast.processing;

/**
 * Created by toofast on 07/02/16.
 *
 * This exception should be thrown, when there is not enough dinnerware at the moment.
 *
 * @see DiningTask prepareDinnerwareWithRetry()
 */
public class NoDinnerwareAvailableException extends RuntimeException {
}
