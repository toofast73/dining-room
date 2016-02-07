package ru.live.toofast.service;

import org.springframework.stereotype.Service;
import ru.live.toofast.entity.Meal;
import ru.live.toofast.entity.Person;

import java.util.Random;

/**
 * I'd like Person to be a very simple POJO. This service helps customers with their activities, like dish selection.
 */
@Service
public class DiningRoomService {

    public Meal selectDish(Person person){

        Random rnd = new Random();
        int randomDishIndex = rnd.nextInt(Meal.values().length);

        return Meal.values()[randomDishIndex];

    }



}
