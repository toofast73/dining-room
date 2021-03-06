package ru.live.toofast.service;


import ru.live.toofast.entity.Order;
import ru.live.toofast.entity.Person;
import ru.live.toofast.entity.dinnerware.Dinnerware;
import ru.live.toofast.entity.dinnerware.DinnerwareType;
import ru.live.toofast.processing.DiningTask;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.stream.Collectors.toList;
import static ru.live.toofast.entity.Status.NOT_PROCESSED;

/**
 * Created by toofast on 06/02/16.
 * <p>
 * The stateful service. Emulates the room, where people take food.
 */
public class DiningRoom {

    private List<Person> customers;

    private Map<DinnerwareType, Queue<Dinnerware>> requisite;

    private Map<DinnerwareType, Integer> initialRequisiteCount;

    private DiningRoomService service;


    public List<CompletableFuture<Order>> startSimulation() {

        Collection<Order> orders = createOrders(customers);

        return orders.parallelStream().map(order ->
                CompletableFuture.supplyAsync(new DiningTask(order, requisite, initialRequisiteCount)))
                .collect(toList());

    }

    private Collection<Order> createOrders(Collection<Person> customers) {
        Collection<Order> orders = newArrayList();
        for (Person customer : customers) {
            Order order = new Order();
            order.setCustomer(customer);
            order.setDish(service.selectDish(customer));
            order.setStatus(NOT_PROCESSED);
            orders.add(order);
        }
        return orders;
    }

    public List<Person> getCustomers() {
        return customers;
    }


    public void setCustomers(List<Person> customers) {
        this.customers = customers;
    }

    public Map<DinnerwareType, Queue<Dinnerware>> getRequisite() {
        return requisite;
    }

    public void setRequisite(Map<DinnerwareType, Queue<Dinnerware>> requisite) {
        this.requisite = requisite;
    }

    public Map<DinnerwareType, Integer> getInitialRequisiteCount() {
        return initialRequisiteCount;
    }


    public void setInitialRequisiteCount(Map<DinnerwareType, Integer> initialRequisiteCount) {
        this.initialRequisiteCount = initialRequisiteCount;
    }


    public void setService(DiningRoomService service) {
        this.service = service;
    }
}
