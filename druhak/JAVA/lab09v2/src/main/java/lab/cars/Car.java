package lab.cars;

import java.io.Serializable;

public class Car implements Serializable { // Serializable pro kompilator, ze nemam zavislost na GUI, pripojeni k databazi apod., ktere nelze serializovat
    CarProducer producer;
    String model;
    float maxSpeed;

    Car(CarProducer producer, String model, float maxSpeed) {
        this.producer = producer;
        this.model = model;
        this.maxSpeed = maxSpeed;
    }

    @Override
    public String toString() {
        return String.format("%s %s (%.2fkm/h)", producer, model, maxSpeed);
    }
}
