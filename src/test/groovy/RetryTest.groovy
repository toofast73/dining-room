import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import ru.live.toofast.DiningHallSimulatorApplication
import ru.live.toofast.entity.Order
import ru.live.toofast.entity.dinnerware.Dinnerware
import ru.live.toofast.entity.dinnerware.DinnerwareType
import ru.live.toofast.entity.dinnerware.Fork
import ru.live.toofast.processing.DiningTask

import java.util.concurrent.CompletionService
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorCompletionService
import java.util.concurrent.Executors

import static com.google.common.collect.Lists.newLinkedList
import static com.google.common.collect.Maps.newHashMap
import static org.junit.Assert.assertTrue
import static ru.live.toofast.entity.Meal.PASTA

/**
 * Created by toofast on 07/02/16.
 */

@RunWith(SpringJUnit4ClassRunner)
@ContextConfiguration(classes = DiningHallSimulatorApplication)
class RetryTest {

    @Test
    void testRetry(){

        Order order = new Order();
        order.setDish(PASTA)

        Map<DinnerwareType, Queue<Dinnerware>> requisite = newHashMap();
        requisite.put(DinnerwareType.FORK, newLinkedList())



        DiningTask task = new DiningTask(order, requisite, null);


        Runnable r1 = new Runnable() {
            @Override
            void run() {
                task.prepareDinnerwareWithRetry()
            }
        }
        Runnable r2 = new Runnable() {
            @Override
            void run() {
                Queue<Dinnerware> forks = requisite.get(DinnerwareType.FORK);
                forks.add(new Fork());
            }
        }

        Executor ex = Executors.newFixedThreadPool(2);

        CompletionService service = new ExecutorCompletionService(ex);
        service.submit(r1, null)
        Thread.sleep(500)
        service.submit(r2, null)
        Thread.sleep(300)
        def poll = service.poll()
        def poll1 = service.poll()
        assertTrue(poll.isDone())
        assertTrue(poll1.isDone())


    }



}
