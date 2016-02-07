import spock.lang.Specification

/**
 * Created by toofast on 07/02/16.
 */
class DiningRoomTest extends Specification {

//
//    def "Async processing works"(){
//
//        setup:
//        Map<DinnerwareType, Integer> quantity = [:]
//        quantity.put(SPOON,  10)
//        quantity.put(TRAY, 20)
//        DiningRoomFactory factory = new DiningRoomFactory();
//        DiningRoomService serviceStub = Stub(DiningRoomService)
//        serviceStub.selectDish(_) >> Meal.FISH_AND_CHIPS
//
//        ApplicationContext contextStub = Stub(ApplicationContext)
//        contextStub.getBean(DiningRoomService) >> serviceStub
//        factory.applicationContext = contextStub
//
//        DiningRoom room = factory.createDiningRoom(100, quantity)
//
//
//
//
//        expect:
//        List<CompletableFuture<Order>> futures = room.process();
//        futures.each {
//            it.whenCompleteAsync(new BiConsumer<Order, Throwable>() {
//                @Override
//                void accept(Order order, Throwable throwable) {
//                    println("Future completed for ${order.dish} ${new Date()}")
//                }
//            })
//        }
//
//        Thread.sleep(1000)
//
//
//
//
//
//    }

}