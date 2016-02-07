package ru.live.toofast;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.live.toofast.entity.Order;
import ru.live.toofast.entity.Status;
import ru.live.toofast.entity.dinnerware.DinnerwareType;
import ru.live.toofast.service.DiningRoom;
import ru.live.toofast.service.DiningRoomFactory;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

import static java.util.Objects.isNull;
import static ru.live.toofast.entity.Status.FAILURE;
import static ru.live.toofast.entity.Status.NOT_PROCESSED;
import static ru.live.toofast.entity.Status.SUCCESS;

/**
 * Created by toofast on 07/02/16.
 */
@Component
public class ViewModel {

    private Map<Status, AtomicInteger> status;
    private int numberOfCustomers;
    private Map<DinnerwareType, Integer> initialRequisite;

    @Autowired
    DiningRoomFactory factory;


    public void runCommand() {

        DiningRoom room = factory.createDiningRoom(numberOfCustomers, initialRequisite);
        List<CompletableFuture<Order>> futures = room.process();

        futures.parallelStream().forEach(f -> f.whenCompleteAsync(new DiningTaskCallback()));


    }


    private class DiningTaskCallback implements BiConsumer<Order, Throwable> {
        @Override
        public void accept(Order order, Throwable throwable) {
            if (isNull(throwable)) {
                order.setStatus(SUCCESS);
                status.get(NOT_PROCESSED).decrementAndGet();
                status.get(SUCCESS).incrementAndGet();
            } else {
                order.setStatus(FAILURE);
                status.get(NOT_PROCESSED).decrementAndGet();
                status.get(FAILURE).incrementAndGet();
                order.setFeedback(throwable.getMessage());
            }
        }

    }
}
