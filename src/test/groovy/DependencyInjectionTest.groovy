import org.apache.commons.collections4.MapUtils
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import ru.live.toofast.DiningHallSimulatorApplication
import ru.live.toofast.entity.dinnerware.DinnerwareType
import ru.live.toofast.entity.dinnerware.Knife
import ru.live.toofast.entity.dinnerware.Spoon
import ru.live.toofast.service.DiningRoom
import ru.live.toofast.service.DiningRoomFactory

import static org.junit.Assert.*
import static ru.live.toofast.entity.dinnerware.DinnerwareType.KNIFE
import static ru.live.toofast.entity.dinnerware.DinnerwareType.SPOON

/**
 * Created by toofast on 07/02/16.
 */
@RunWith(SpringJUnit4ClassRunner)
@ContextConfiguration(classes = DiningHallSimulatorApplication)
class DependencyInjectionTest {

    @Autowired
    DiningRoomFactory factory;

    @Test
    void "DI works as expected"() {

        def numberOfCustomers = 100
        def initialRequisite = [:]

        DiningRoom room = factory.createDiningRoom(numberOfCustomers, initialRequisite)

        assertNotNull(room.service)

    }


    @Test
    void "Can create a dining room"() {


        Map<DinnerwareType, Integer> quantity = [:]
        quantity.put(SPOON, 10)
        quantity.put(KNIFE, 20)



        DiningRoom room = factory.createDiningRoom(100, quantity)


        assertEquals(100, room.customers.size())
        assertEquals(quantity, room.initialRequisiteCount)
        def req = room.requisite;

        assertTrue(MapUtils.isNotEmpty(req))
        assertEquals(10, req.get(SPOON).size())
        assertEquals(Spoon, req.get(SPOON).poll().getClass())

        assertEquals(20, req.get(KNIFE).size())
        assertEquals(Knife, req.get(KNIFE).poll().getClass())
    }
}
