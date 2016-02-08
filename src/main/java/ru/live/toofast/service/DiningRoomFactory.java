package ru.live.toofast.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import ru.live.toofast.entity.Person;
import ru.live.toofast.entity.dinnerware.*;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Queues.newConcurrentLinkedQueue;

/**
 * Created by toofast on 07/02/16.
 */
@Component("diningRoomFactory")
public class DiningRoomFactory {


    @Autowired
    private ApplicationContext applicationContext;


    public DiningRoom createDiningRoom(int numberOfCustomers, Map<DinnerwareType, Integer> initialRequisite) {

        List<Person> customers = generateCustomers(numberOfCustomers);
        Map<DinnerwareType, Queue<Dinnerware>> dinnerware = generateRequisite(initialRequisite);


        DiningRoom room = new DiningRoom();
        room.setCustomers(customers);
        room.setInitialRequisiteCount(initialRequisite);
        room.setRequisite(dinnerware);

        DiningRoomService service = applicationContext.getBean(DiningRoomService.class);
        room.setService(service);

        return room;
    }

    private Map<DinnerwareType, Queue<Dinnerware>> generateRequisite(Map<DinnerwareType, Integer> initialRequisite) {
        Map<DinnerwareType, Queue<Dinnerware>> result = new ConcurrentHashMap<>();//newHashMap();

        for (Map.Entry<DinnerwareType, Integer> e : initialRequisite.entrySet()) {


            Integer quantity = e.getValue();
            DinnerwareType type = e.getKey();

            Queue<Dinnerware> dw = generateDinnerware(quantity, type);

            result.put(type, dw);

        }

        return result;
    }

    private Queue<Dinnerware> generateDinnerware(Integer quantity, DinnerwareType type) {
        Queue<Dinnerware> dw = newConcurrentLinkedQueue();

        switch (type) {
            case FORK:
                for (int i = 0; i < quantity; i++) {
                    dw.add(new Fork());
                }
                break;
            case SPOON:
                for (int i = 0; i < quantity; i++) {
                    dw.add(new Spoon());
                }
                break;
            case KNIFE:
                for (int i = 0; i < quantity; i++) {
                    dw.add(new Knife());
                }
                break;
            case TRAY:
                for (int i = 0; i < quantity; i++) {
                    dw.add(new Tray());
                }
                break;
        }
        return dw;
    }

    private List<Person> generateCustomers(int numberOfCustomers) {
        List<Person> customers = newArrayList();

        for (int i = 0; i < numberOfCustomers; i++) {
            customers.add(new Person());
        }

        return customers;
    }


}
