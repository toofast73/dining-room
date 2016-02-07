package ru.live.toofast.entity;

/**
 * Created by toofast on 07/02/16.
 */
public class Order {

    private Person customer;

    private Status status;

    private Meal dish;

    private String feedback;


    public Person getCustomer() {
        return customer;
    }

    public void setCustomer(Person customer) {
        this.customer = customer;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Meal getDish() {
        return dish;
    }

    public void setDish(Meal dish) {
        this.dish = dish;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
