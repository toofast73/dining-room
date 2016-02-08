import com.google.common.collect.Maps
import com.google.common.collect.Queues
import org.apache.commons.collections4.MapUtils
import ru.live.toofast.entity.Meal
import ru.live.toofast.entity.Order
import ru.live.toofast.entity.dinnerware.Dinnerware
import ru.live.toofast.entity.dinnerware.DinnerwareType
import ru.live.toofast.entity.dinnerware.Fork
import ru.live.toofast.entity.dinnerware.Knife
import ru.live.toofast.processing.DiningTask
import spock.lang.Specification

import static com.google.common.collect.Maps.newConcurrentMap
import static com.google.common.collect.Queues.*
import static org.apache.commons.collections4.MapUtils.isEmpty
import static ru.live.toofast.entity.dinnerware.DinnerwareType.*
import static ru.live.toofast.entity.dinnerware.DinnerwareType.KNIFE


/**
 * Created by toofast on 07/02/16.
 */
class DiningTaskTest extends Specification {

    def  "Check prerequisites: failed"(){

        setup:

        Order stub = Mock(Order);
        1 * stub.getDish() >> Meal.PASTA;

        Map<DinnerwareType, Integer> initialRequisiteCount = [:]
        initialRequisiteCount.put(FORK, 0)
        DiningTask task = new DiningTask(stub, null, initialRequisiteCount);

        when:
        task.checkPrerequisites()

        then:
        thrown(RuntimeException)

    }


    def "Check prerequisites: passed"(){

        setup:

        Order stub = Mock(Order);
        1 * stub.getDish() >> Meal.PASTA;

        Map<DinnerwareType, Integer> initialRequisiteCount = [:]
        initialRequisiteCount.put(FORK, 10)
        DiningTask task = new DiningTask(stub, null, initialRequisiteCount);

        expect:
        task.checkPrerequisites()


    }


    def "Return dinnerware test"(){

        setup:
        Map<DinnerwareType, Queue<Dinnerware>> requisite = newConcurrentMap()
        def forks = newConcurrentLinkedQueue();
        forks.add(new Fork())
        def knifes = newConcurrentLinkedQueue();
        requisite.put(FORK, forks)
        requisite.put(KNIFE, knifes)



        Map<DinnerwareType, Dinnerware> usedDinnerware = newConcurrentMap()
        usedDinnerware.put(FORK, new Fork())
        usedDinnerware.put(KNIFE, new Knife())
        DiningTask task = new DiningTask(null, requisite, null);
        task.usedDinnerware = usedDinnerware


        when:
        task.returnDinnerware()

        then:
        isEmpty(usedDinnerware)

        task.requisite.get(FORK).size() == 2
        task.requisite.get(KNIFE).size() == 1




    }


}