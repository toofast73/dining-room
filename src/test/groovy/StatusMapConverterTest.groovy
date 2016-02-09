import org.zkoss.zul.ListModelList
import ru.live.toofast.StatusMapConverter
import ru.live.toofast.entity.Status
import spock.lang.Specification

import java.util.concurrent.atomic.AtomicInteger

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty
import static org.junit.Assert.assertNull
import static ru.live.toofast.entity.Status.NOT_PROCESSED
import static ru.live.toofast.entity.Status.SUCCESS

/**
 * Created by toofast on 09/02/16.
 */
class StatusMapConverterTest extends Specification {

    def "Converts Map to the List of transfer objects"() {

        setup:
        StatusMapConverter converter = new StatusMapConverter();
        Map<Status, AtomicInteger> statusSample = [:]
        statusSample.put(SUCCESS, new AtomicInteger(1000))
        statusSample.put(NOT_PROCESSED, new AtomicInteger(0))



        when:
        ListModelList<StatusMapConverter.StatusTO> result = converter.coerceToUi(statusSample, null, null)

        then:
        isNotEmpty(result)
        result.size == 2
        result.first().status == "SUCCESS"
        result.first().quantity == 1000


    }


    def "Return nothing. It's one way conversion only"() {

        setup:
        StatusMapConverter converter = new StatusMapConverter();

        expect:

        assertNull(converter.coerceToBean([], null, null))

    }

}