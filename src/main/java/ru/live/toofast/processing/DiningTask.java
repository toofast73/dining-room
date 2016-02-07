package ru.live.toofast.processing;


import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import ru.live.toofast.entity.Meal;
import ru.live.toofast.entity.Order;
import ru.live.toofast.entity.dinnerware.Dinnerware;
import ru.live.toofast.entity.dinnerware.DinnerwareType;
import ru.live.toofast.service.DiningRoomService;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

import static com.google.common.collect.Maps.newHashMap;
import static java.util.Objects.isNull;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;

/**
 * Created by toofast on 07/02/16.
 */

public class DiningTask implements Supplier<Order> {

    private final Order order;
    private final Map<DinnerwareType, Queue<Dinnerware>> requisite;
    private final Map<DinnerwareType, Integer> initialRequisiteCount;
    private final Map<DinnerwareType, Dinnerware> usedDinnerware = newHashMap();


    public DiningTask(Order order, Map<DinnerwareType, Queue<Dinnerware>> requisite, Map<DinnerwareType, Integer> initialRequisiteCount) {
        this.order = order;
        this.requisite = requisite;
        this.initialRequisiteCount = initialRequisiteCount;
    }


    @Override
    public Order get() {

        checkPrerequisites();
        pay();
        prepareDinnerwareWithRetry();
        takeFood();
        returnDinnerware();

        return order;
    }

    private void returnDinnerware() {
        for (Map.Entry<DinnerwareType, Dinnerware> e : usedDinnerware.entrySet()) {
            requisite.get(e.getKey()).add(e.getValue());
            usedDinnerware.remove(e.getKey());
        }

    }

    /**
     * We spend some time there to simulate eating.
     */
    private void takeFood() {
        try {

            Thread.sleep(100);
        } catch (InterruptedException e) {
            //TODO logger
        }
    }

    private void prepareDinnerwareWithRetry() {
        Map<Class<? extends Throwable>, Boolean> registeredExceptions = newHashMap();
        registeredExceptions.put(NoDinnerwareAvailableException.class, true);

        RetryPolicy policy = new SimpleRetryPolicy(Integer.MAX_VALUE, registeredExceptions);

        RetryTemplate template = new RetryTemplate();
        template.setRetryPolicy(policy);

        FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
        backOffPolicy.setBackOffPeriod(100);
        template.setBackOffPolicy(backOffPolicy);

        template.execute((RetryCallback<Void, RuntimeException>) context -> {
            prepareDinnerware();
            return null;
        });
    }


    private void prepareDinnerware() {
        Set<DinnerwareType> req = order.getDish().getRequisite();
        ReentrantLock lock = new ReentrantLock();
        lock.lock();
        for (DinnerwareType dinnerwareType : req) {
            if (isEmpty(requisite.get(dinnerwareType))) {
                throw new NoDinnerwareAvailableException();
            }
        }

        for (DinnerwareType dinnerwareType : req) {
            Dinnerware dw = requisite.get(dinnerwareType).remove();
            usedDinnerware.put(dinnerwareType, dw);
        }

        lock.unlock();
    }

    private void pay() {
        //do nothing
    }

    private void checkPrerequisites() {
        Meal dish = order.getDish();
        Set<DinnerwareType> req = dish.getRequisite();

        for (DinnerwareType dinnerwareType : req) {
            Integer quantity = initialRequisiteCount.get(dinnerwareType);
            if (isNull(quantity) || quantity < 1) {
                throw new RuntimeException(String.format("There is not enought requisite of type: %s", dinnerwareType)); //TODO SPECIAL
            }
        }
    }
}
