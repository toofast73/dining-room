package ru.live.toofast.entity;

import ru.live.toofast.entity.dinnerware.DinnerwareType;

import java.util.HashSet;
import java.util.Set;

import static ru.live.toofast.entity.dinnerware.DinnerwareType.*;

/**
 * Created by toofast on 06/02/16.
 */
public enum Meal {

    FISH_AND_CHIPS(new HashSet<DinnerwareType>() {{
        add(FORK);
        add(KNIFE);
    }}),

    STEAK_WITH_FRIES(new HashSet<DinnerwareType>() {{
        add(FORK);
        add(KNIFE);
    }}),

    PASTA(new HashSet<DinnerwareType>() {{
        add(FORK);
    }}),

    SALAD(new HashSet<DinnerwareType>() {{
        add(FORK);
    }}),

    SOUP(new HashSet<DinnerwareType>() {{
        add(SPOON);

    }});


    private final Set<DinnerwareType> requisite;

    Meal(Set<DinnerwareType> requisite) {
        this.requisite = requisite;

    }

    public Set<DinnerwareType> getRequisite() {
        return requisite;
    }

}
