import com.sun.org.apache.xpath.internal.operations.Or
import org.zkoss.zk.ui.Execution
import ru.live.toofast.ViewModel
import ru.live.toofast.entity.Order
import ru.live.toofast.entity.Status
import ru.live.toofast.processing.DiningTaskCallback
import spock.lang.Specification

import java.util.concurrent.atomic.AtomicInteger

import static ru.live.toofast.entity.Status.FAILURE
import static ru.live.toofast.entity.Status.NOT_PROCESSED
import static ru.live.toofast.entity.Status.SUCCESS


/**
 * Created by toofast on 09/02/16.
 */
class DiningTaskCallbackTest extends Specification {

    def "Update status: no throwable"(){

        setup:
        ViewModel viewModel = new ViewModel()
        Map<Status, AtomicInteger> statusSample = [:]
        statusSample.put(SUCCESS, new AtomicInteger(1))
        statusSample.put(NOT_PROCESSED, new AtomicInteger(100))
        viewModel.setStatus(statusSample)

        Execution execution = Mock(Execution)
        DiningTaskCallback callback = new DiningTaskCallback(viewModel, execution)
        Order order = new Order();
        order.status = NOT_PROCESSED

        when:
        callback.updateStatus(order, null);

        then:
        order.status == SUCCESS
        viewModel.status.get(SUCCESS).get() == 2
        viewModel.status.get(NOT_PROCESSED).get() == 99
    }


    def "Update status: there was an exception"(){

        setup:
        ViewModel viewModel = new ViewModel()
        Map<Status, AtomicInteger> statusSample = [:]
        statusSample.put(SUCCESS, new AtomicInteger(1))
        statusSample.put(NOT_PROCESSED, new AtomicInteger(100))
        statusSample.put(FAILURE, new AtomicInteger(0))
        viewModel.setStatus(statusSample)

        Execution execution = Mock(Execution)
        DiningTaskCallback callback = new DiningTaskCallback(viewModel, execution)
        Order order = new Order();
        order.status = NOT_PROCESSED

        when:
        callback.updateStatus(null, new RuntimeException());

        then:
        viewModel.status.get(SUCCESS).get() == 1
        viewModel.status.get(FAILURE).get() == 1
        viewModel.status.get(NOT_PROCESSED).get() == 99

    }
}