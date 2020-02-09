package homeworkweekthree.demo.service;

import homeworkweekthree.demo.model.Car;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface CarService {

    Optional<Car> getCar(long id);

    List<Car> getCars();

    List<Car> getCarByColor(String color);

    void updateCarByValues(Car car, Car myCar);

    Resources<Car> getCarsResources(List<Car> cars);

    Resource<Car> getCarResource(long id);

    void addColorLink(List<Car> cars);

    void addCarLink(List<Car> cars);

    ResponseEntity<Resource<Car>> getResourceResponseEntity(long id);
}
