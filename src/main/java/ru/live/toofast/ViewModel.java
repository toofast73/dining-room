package ru.live.toofast;

import com.google.common.collect.Maps;
import org.apache.log4j.Logger;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.annotation.VariableResolver;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zk.ui.util.Clients;
import ru.live.toofast.entity.Order;
import ru.live.toofast.entity.Status;
import ru.live.toofast.entity.dinnerware.DinnerwareType;
import ru.live.toofast.service.DiningRoom;
import ru.live.toofast.service.DiningRoomFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

import static com.google.common.collect.Maps.newHashMap;
import static java.util.Objects.isNull;
import static ru.live.toofast.entity.Status.*;

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

    private org.zkoss.bind.Converter converter = new StatusMapConverter();

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
        Map<Status, AtomicInteger> status = Maps.newConcurrentMap();
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
        Map<Status, AtomicInteger> status = Maps.newConcurrentMap();
        status.put(Status.SUCCESS, new AtomicInteger(0));
        status.put(Status.FAILURE, new AtomicInteger(0));
        status.put(Status.NOT_PROCESSED, new AtomicInteger(1000));
        setStatus(status);
        runCommand();

    }



    private class DiningTaskCallback implements BiConsumer<Order, Throwable> {
        private final ViewModel viewModel;
        private final Execution execution;
        Logger logger = org.apache.log4j.LogManager.getLogger(DiningTaskCallback.class);

        public DiningTaskCallback(ViewModel viewModel, Execution current) {
            this.viewModel = viewModel;
            this.execution = current;
        }

        @Override
        public void accept(Order order, Throwable throwable) {
            updateStatus(order, throwable);
            notifyFrontend();
            whenAllTasksCompleted();
        }

        private void updateStatus(Order order, Throwable throwable) {
            if (isNull(throwable)) {
                order.setStatus(SUCCESS);
                status.get(NOT_PROCESSED).decrementAndGet();
                status.get(SUCCESS).incrementAndGet();
            } else {
                status.get(NOT_PROCESSED).decrementAndGet();
                status.get(FAILURE).incrementAndGet();
            }
        }

        /**
         * We don't need to send notifications very often. One for #n customers is enough.
         *
         * TODO: substitute hardcoded variables with properties
         */
        private void notifyFrontend() {
            if (status.get(NOT_PROCESSED).get() == 0 || status.get(SUCCESS).get() % 100 == 0) {
                try {
                    Executions.activate(execution.getDesktop());
                    BindUtils.postNotifyChange(null, null, viewModel, "status");
                } catch (InterruptedException e) {
                    logger.warn(e);
                } finally {
                    Executions.deactivate(execution.getDesktop());
                }
            }
        }

        private void whenAllTasksCompleted() {
            if (status.get(NOT_PROCESSED).get() == 0) {
                if (status.get(FAILURE).get() > 0) {
                    Clients.alert("Some orders were processed with errors. There were not enough initial tableware to serve the meal.");
                }
                execution.getDesktop().enableServerPush(false);
            }
        }

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
