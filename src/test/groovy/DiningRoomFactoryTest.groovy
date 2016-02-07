import org.apache.commons.collections4.CollectionUtils
import org.apache.commons.collections4.MapUtils
import ru.live.toofast.entity.Person
import ru.live.toofast.entity.dinnerware.Dinnerware
import ru.live.toofast.entity.dinnerware.DinnerwareType
import ru.live.toofast.entity.dinnerware.Fork
import ru.live.toofast.entity.dinnerware.Knife
import ru.live.toofast.entity.dinnerware.Spoon
import ru.live.toofast.entity.dinnerware.Tray
import ru.live.toofast.service.DiningRoom
import ru.live.toofast.service.DiningRoomFactory
import spock.lang.Shared
import spock.lang.Specification

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty
import static ru.live.toofast.entity.dinnerware.DinnerwareType.FORK
import static ru.live.toofast.entity.dinnerware.DinnerwareType.FORK
import static ru.live.toofast.entity.dinnerware.DinnerwareType.KNIFE
import static ru.live.toofast.entity.dinnerware.DinnerwareType.SPOON
import static ru.live.toofast.entity.dinnerware.DinnerwareType.TRAY


/**
 * Created by toofast on 07/02/16.
 */
class DiningRoomFactoryTest extends Specification {

    @Shared
    DiningRoomFactory factory = new DiningRoomFactory();


    def "Should be able to generate dinnerware, based on the quantity provided"(){

        when:
        Collection<Dinnerware> dinnerware = factory.generateDinnerware(10, FORK)

        then:
        isNotEmpty(dinnerware)
        dinnerware.size() == 10
        dinnerware.first().getClass() == Fork

    }


    def "Should be able to generate customers"(){

        when:
        Integer quantity = 1000;
        Collection<Person> customers = factory.generateCustomers(quantity)

        then:
        isNotEmpty(customers)
        customers.size() == quantity

    }

    def "Should be able to generate requisite"(){

        setup:
        Map<DinnerwareType, Integer> quantity = [:]
        quantity.put(FORK,  10)
        quantity.put(KNIFE, 20)

        when:
        Map<DinnerwareType, Collection<Dinnerware>> requisite = factory.generateRequisite(quantity)

        then:
        MapUtils.isNotEmpty(requisite)
        requisite.get(FORK).size()  == 10
        requisite.get(KNIFE).size() == 20
        requisite.get(KNIFE).first().getClass() == Knife
    }


}