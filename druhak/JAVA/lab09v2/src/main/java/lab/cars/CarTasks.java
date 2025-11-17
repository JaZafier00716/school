package lab.cars;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class CarTasks {

    public static void main(String[] args) {
        System.out.println("Starting Java programing test - part Car Tasks.");
        CarTasks carTasks = new CarTasks();
        carTasks.printAllCarProducers();
        carTasks.generateTwoCars();
        carTasks.store("cars.bin");
        carTasks.load("cars.bin");
    }

    /**
     * Vytvořte enum CarProducer se třemi konstantami:BMW, PORSCHE a SKODA.
     * Každá konstanta bude mít dvě hodnoty/parametry name a slogan:
     * BMW -> Sheer Driving Pleasure
     * Porsche -> Driven by Dreams
     * Škoda -> Let´s Explore
     * Kromě jiného bude mít enum gettry pro jméno a slogan a
     * toString metodu, která bude vracet jméno.
     *
     * Vypište do konzole všechny konstanty z CarProducer ve formátu jméno - slogan
     */

    public void printAllCarProducers(){
        //TODO
        for (CarProducer producer : CarProducer.values()) {
            System.out.println(String.format("%s - %s", producer.getName(), producer.getSlogan()));
        }
    }

    /**
     *Vytvořte třídu Car s instančními proměnnými:
     * CarProducer producer
     * String model
     * float maxSpeed
     *
     * Vytvořte třídě konstruktor se třemi parametry a toString metodu,
     * která vypíše auto ve formátu:
     * výrobce model (max_rychlost_km/h)
     * Např:Škoda Rapid (180.0km/h)
     *
     * Vytvořte kolekci, vložte do ní dvě auta a kolekci vraťte.
     *
     * Neměňte typ návratové hodnoty.
     */
    public List<Car> generateTwoCars() {
        //TODO
        List<Car>  cars = new ArrayList<>();

        cars.add(new Car(CarProducer.SKODA, "Rapid", 180));
//        cars.add(new Car(CarProducer.SKODA, "Octavia", 220));
//        cars.add(new Car(CarProducer.BMW, "X6", 180));
        cars.add(new Car(CarProducer.PORSCHE, "911", 320));

        return cars;  //notno změnit
    }

    /**
     * Metoda uloží do souboru fileName obsah kolekce vrácené metodou generateTwoCars.
     * Obsah se uloží jako binární objektová data.
     * Pokud ukládání selže metoda vrací false a do konzoly se vypíše příčina chyby.
     */
    public boolean store(String fileName){
        //TODO
        try(ObjectOutputStream out = new ObjectOutputStream(
            new FileOutputStream(fileName)
        )) {

            List<Car> cars = generateTwoCars();
            out.writeObject(cars);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * Metoda načte obsah souboru fileName vytvořeného v metodě store.
     * Obsah je uložen jako binární objektová data.
     *
     * Načtená auta se vypíší do konzole.
     *
     * Pokud ukládání selže metoda vrací false a do konzoly se vypíše příčina chyby.
     */
    public boolean load(String fileName){
        //TODO
        try(ObjectInputStream in = new ObjectInputStream(
            new FileInputStream(fileName)
        ))   {
            List<Car> cars = (List<Car>) in.readObject();
            for(Car car : cars){
                System.out.println(car);
            }
            return true;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}
