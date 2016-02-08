package ru.live.toofast;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
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

/**
 * Created by toofast on 07/02/16.
 *
 * The class for communication with the frontend.
 * Front end is rendered, when operation status is updated.
 */

@VariableResolver(org.zkoss.zkplus.spring.DelegatingVariableResolver.class)
public class ViewModel {

    @WireVariable("diningRoomFactory")
    DiningRoomFactory factory;

    private Map<Status, AtomicInteger> status;

    private int numberOfCustomers;

    private Map<DinnerwareType, Integer> initialRequisite;

    private String keyword = "FOO";

    @WireVariable("statusMapConverter")
    private org.zkoss.bind.Converter converter;

    public void runCommand() {

        DiningRoom room = factory.createDiningRoom(numberOfCustomers, initialRequisite);
        List<CompletableFuture<Order>> futures = room.startSimulation();


        futures.parallelStream().forEach(f -> f.whenComplete(new DiningTaskCallback(this, Executions.getCurrent())));
    }

    @Command
    @NotifyChange({"initialRequisite", "numberOfCustomers", "status"})
    public void invokeHappyCase() {
        Executions.getCurrent().getDesktop().enableServerPush(true);
        Map<DinnerwareType, Integer> req = newHashMap();
        req.put(DinnerwareType.FORK, 50);
        req.put(DinnerwareType.SPOON, 70);
        req.put(DinnerwareType.KNIFE, 100);
        req.put(DinnerwareType.TRAY, 80);
        setInitialRequisite(req);
        setNumberOfCustomers(1000);
        Map<Status, AtomicInteger> status = newConcurrentMap();
        status.put(Status.SUCCESS, new AtomicInteger(0));
        status.put(Status.FAILURE, new AtomicInteger(0));
        status.put(Status.NOT_PROCESSED, new AtomicInteger(1000));
        setStatus(status);
        runCommand();
    }

    @Command
    public void invokeCaseWithDifficulties() {
        Executions.getCurrent().getDesktop().enableServerPush(true);
        Map<DinnerwareType, Integer> req = newHashMap();
        req.put(DinnerwareType.FORK, 50);
        req.put(DinnerwareType.SPOON, 0);
        req.put(DinnerwareType.KNIFE, 100);
        req.put(DinnerwareType.TRAY, 80);
        setInitialRequisite(req);
        setNumberOfCustomers(1000);
        Map<Status, AtomicInteger> status = newConcurrentMap();
        status.put(Status.SUCCESS, new AtomicInteger(0));
        status.put(Status.FAILURE, new AtomicInteger(0));
        status.put(Status.NOT_PROCESSED, new AtomicInteger(1000));
        setStatus(status);
        runCommand();

    }


    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Map<Status, AtomicInteger> getStatus() {
        return status;
    }

    public void setStatus(Map<Status, AtomicInteger> status) {
        this.status = status;
    }

    public int getNumberOfCustomers() {
        return numberOfCustomers;
    }

    public void setNumberOfCustomers(int numberOfCustomers) {
        this.numberOfCustomers = numberOfCustomers;
    }

    public Map<DinnerwareType, Integer> getInitialRequisite() {
        return initialRequisite;
    }

    public void setInitialRequisite(Map<DinnerwareType, Integer> initialRequisite) {
        this.initialRequisite = initialRequisite;
    }

    public org.zkoss.bind.Converter getConverter() {
        return converter;
    }

    public void setConverter(org.zkoss.bind.Converter converter) {
        this.converter = converter;
    }
}


