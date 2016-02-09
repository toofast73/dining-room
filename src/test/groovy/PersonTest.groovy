import ru.live.toofast.entity.Meal
import ru.live.toofast.entity.Person
import ru.live.toofast.service.DiningRoomService
import spock.lang.Specification

import static org.junit.Assert.assertNotNull;

/**
 * Created by toofast on 07/02/16.
 */
public class PersonTest extends Specification {

    def "Person has a favourite dish. Now it is generated randomly"() {

        given:
        DiningRoomService service = new DiningRoomService();
        Person p = new Person();

        when:
        Meal dish = service.selectDish(p)

        then:
        assertNotNull(dish)

    }


}
