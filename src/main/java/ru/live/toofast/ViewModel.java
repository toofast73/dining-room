package ru.live.toofast;

import org.zkoss.bind.Validator;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import ru.live.toofast.entity.Order;
import ru.live.toofast.entity.Status;
import ru.live.toofast.entity.dinnerware.DinnerwareType;
import ru.live.toofast.processing.DiningTaskCallback;
import ru.live.toofast.service.DiningRoom;
import ru.live.toofast.service.DiningRoomFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

import static com.google.common.collect.Maps.newConcurrentMap;
import static com.google.common.collect.Maps.newHashMap;
import static java.util.Objects.isNull;

/**
 * Created by toofast on 07/02/16.
 * <p>
 * The class for communication with the frontend.
 * Front end is rendered, when operation status is updated.
 */

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class ViewModel {

    @WireVariable("diningRoomFactory")
    DiningRoomFactory factory;

    Integer spoons;
    Integer forks;
    Integer knifes;
    Integer numberOfCustomers;


    private Map<Status, AtomicInteger> status;

    @WireVariable("statusMapConverter")
    private org.zkoss.bind.Converter converter;

    public void runCommand(Integer numberOfCustomers, Map<DinnerwareType, Integer> initialRequisite) {

        DiningRoom room = factory.createDiningRoom(numberOfCustomers, initialRequisite);
        List<CompletableFuture<Order>> futures = room.startSimulation();


        futures.parallelStream().forEach(f -> f.whenComplete(new DiningTaskCallback(this, Executions.getCurrent())));
    }


    @Command
    @NotifyChange("status")
    public void simulate() {
        Executions.getCurrent().getDesktop().enableServerPush(true);
        Map<DinnerwareType, Integer> req = newHashMap();
        req.put(DinnerwareType.FORK, forks);
        req.put(DinnerwareType.SPOON, spoons);
        req.put(DinnerwareType.KNIFE, knifes);
        Integer numberOfCustomers = getNumberOfCustomers();

        if (isValid(req, numberOfCustomers)) {
            prepareProcessingStatus(numberOfCustomers);
            runCommand(numberOfCustomers, req);
        }
    }

    private boolean isValid(Map<DinnerwareType, Integer> req, Integer numberOfCustomers) {
        boolean isValid = true;
        for (Map.Entry<DinnerwareType, Integer> e : req.entrySet()) {
            if (isNull(e.getValue()) || e.getValue() < 0) {
                Clients.alert(String.format("Validation failed: field %s is not valid", e.getKey()));
                isValid = false;
            }

        }
        if (isNull(numberOfCustomers) || numberOfCustomers < 0) {
            Clients.alert("Validation failed: number of customers is not valid");
            isValid = false;

        }

        return isValid;
    }

    private void prepareProcessingStatus(Integer numberOfCustomers) {
        Map<Status, AtomicInteger> status = newConcurrentMap();
        status.put(Status.SUCCESS, new AtomicInteger(0));
        status.put(Status.FAILURE, new AtomicInteger(0));
        status.put(Status.NOT_PROCESSED, new AtomicInteger(numberOfCustomers));
        setStatus(status);
    }

    public Map<Status, AtomicInteger> getStatus() {
        return status;
    }

    public void setStatus(Map<Status, AtomicInteger> status) {
        this.status = status;
    }


    public org.zkoss.bind.Converter getConverter() {
        return converter;
    }

    public void setConverter(org.zkoss.bind.Converter converter) {
        this.converter = converter;
    }


    public Integer getSpoons() {
        return spoons;
    }

    public void setSpoons(Integer spoons) {
        this.spoons = spoons;
    }

    public Integer getForks() {
        return forks;
    }

    public void setForks(Integer forks) {
        this.forks = forks;
    }

    public Integer getKnifes() {
        return knifes;
    }

    public void setKnifes(Integer knifes) {
        this.knifes = knifes;
    }

    public Integer getNumberOfCustomers() {
        return numberOfCustomers;
    }

    public void setNumberOfCustomers(Integer numberOfCustomers) {
        this.numberOfCustomers = numberOfCustomers;
    }

}


