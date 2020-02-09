package homeworkweekthree.demo.service;

import homeworkweekthree.demo.model.Car;

import java.util.List;
import java.util.Optional;

public interface CarService {

    Optional<Car> getCar(long id);

    List<Car> getCars();

    List<Car> getCarByColor(String color);

    void updateCarByValues(Car car, Car myCar);
}
