import ru.live.toofast.entity.Order
import ru.live.toofast.entity.Person
import ru.live.toofast.service.DiningRoom
import ru.live.toofast.service.DiningRoomService
import spock.lang.Specification

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty
import static ru.live.toofast.entity.Meal.FISH_AND_CHIPS
import static ru.live.toofast.entity.Status.NOT_PROCESSED

/**
 * Created by toofast on 09/02/16.
 */
class DiningRoomTest extends Specification {


    def "Creates order entities for customers"() {

        setup:
        DiningRoom room = new DiningRoom()
        List<Person> customers = [new Person(), new Person()]

        DiningRoomService serviceMock = Mock(DiningRoomService)
        2 * serviceMock.selectDish(_) >> FISH_AND_CHIPS
        room.service = serviceMock


        when:
        Collection<Order> orders = room.createOrders(customers)

        then:
        isNotEmpty(orders)
        orders.size() == 2
        orders.first().status == NOT_PROCESSED
        orders.first().dish == FISH_AND_CHIPS

    }

}