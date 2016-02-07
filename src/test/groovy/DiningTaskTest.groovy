import ru.live.toofast.entity.Meal
import ru.live.toofast.entity.Order
import ru.live.toofast.entity.dinnerware.DinnerwareType
import ru.live.toofast.processing.DiningTask
import spock.lang.Specification


/**
 * Created by toofast on 07/02/16.
 */
class DiningTaskTest extends Specification {

    def "Check prerequisites: failed"(){

        setup:

        Order stub = Mock(Order);
        1 * stub.getDish() >> Meal.PASTA;

        Map<DinnerwareType, Integer> initialRequisiteCount = [:]
        initialRequisiteCount.put(DinnerwareType.FORK, 0)
        DiningTask task = new DiningTask(stub, null, initialRequisiteCount, null);

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
        initialRequisiteCount.put(DinnerwareType.FORK, 10)
        DiningTask task = new DiningTask(stub, null, initialRequisiteCount, null);

        expect:
        task.checkPrerequisites()


    }


}