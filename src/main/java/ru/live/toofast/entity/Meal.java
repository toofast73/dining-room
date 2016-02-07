package ru.live.toofast.entity;

import ru.live.toofast.entity.dinnerware.DinnerwareType;

import java.util.HashSet;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;
import static ru.live.toofast.entity.dinnerware.DinnerwareType.FORK;
import static ru.live.toofast.entity.dinnerware.DinnerwareType.KNIFE;
import static ru.live.toofast.entity.dinnerware.DinnerwareType.SPOON;

/**
 * Created by toofast on 06/02/16.
 */
public enum Meal {

    FISH_AND_CHIPS("Delicious", new HashSet<DinnerwareType>(){{
        add(FORK);
        add(KNIFE);
    }}),

    STEAK_WITH_FRIES("Delicious", new HashSet<DinnerwareType>(){{
        add(FORK);
        add(KNIFE);
    }}),

    PASTA("Delicious", new HashSet<DinnerwareType>(){{
        add(FORK);
    }}),

    SALAD("Delicious", new HashSet<DinnerwareType>(){{
        add(FORK);
    }}),

    SOUP("Delicious", new HashSet<DinnerwareType>(){{
        add(SPOON);

    }});


    private final String description;
    private final Set<DinnerwareType> requisite;

    Meal(String description, Set<DinnerwareType> requisite) {
        this.requisite = requisite;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public Set<DinnerwareType> getRequisite() {
        return requisite;
    }

}
