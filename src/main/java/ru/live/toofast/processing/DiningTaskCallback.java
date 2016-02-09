package ru.live.toofast.processing;

import org.apache.log4j.Logger;
import org.zkoss.bind.BindUtils;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.Clients;
import ru.live.toofast.ViewModel;
import ru.live.toofast.entity.Order;

import java.util.function.BiConsumer;

import static java.util.Objects.isNull;
import static org.apache.log4j.LogManager.getLogger;
import static ru.live.toofast.entity.Status.*;

/**
 * Created by toofast on 09/02/16.
 */
public class DiningTaskCallback implements BiConsumer<Order, Throwable> {
    private final ViewModel viewModel;
    private final Execution execution;
    Logger logger = getLogger(DiningTaskCallback.class);

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
            viewModel.getStatus().get(NOT_PROCESSED).decrementAndGet();
            viewModel.getStatus().get(SUCCESS).incrementAndGet();
        } else {
            viewModel.getStatus().get(NOT_PROCESSED).decrementAndGet();
            viewModel.getStatus().get(FAILURE).incrementAndGet();
        }
    }

    /**
     * We don't need to send notifications very often. One for #n customers is enough.
     * <p>
     * TODO: substitute hardcoded variables with properties
     */
    private void notifyFrontend() {
        if (viewModel.getStatus().get(NOT_PROCESSED).get() == 0 || viewModel.getStatus().get(SUCCESS).get() % 100 == 0) {
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
        if (viewModel.getStatus().get(NOT_PROCESSED).get() == 0) {
            if (viewModel.getStatus().get(FAILURE).get() > 0) {
                Clients.alert("Some orders were processed with errors. There were not enough initial tableware to serve the meal.");
            }
            execution.getDesktop().enableServerPush(false);
        }
    }

}
