package homeworkweekthree.demo.service;

import homeworkweekthree.demo.controller.CarController;
import homeworkweekthree.demo.model.Car;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Service
public class CarServiceImpl implements CarService {

    private List<Car> cars;

    public CarServiceImpl() {
        this.cars = new ArrayList<>();
        cars.add(new Car(1L, "Audi", "A4", "Red"));
        cars.add(new Car(2L, "Cupra", "Ateca", "Black"));
        cars.add(new Car(3L, "Fiat", "Uno", "Blue"));
    }

    @Override
    public Optional<Car> getCar(long id) {
        return getCars()
                .stream()
                .filter(car -> car.getCarId() == id)
                .findFirst();
    }

    @Override
    public List<Car> getCars() {
        return this.cars;
    }

    @Override
    public List<Car> getCarByColor(String color) {
        return getCars()
                .stream()
                .filter(car -> car.getColor().equalsIgnoreCase(color))
                .collect(Collectors.toList());
    }

    @Override
    public void updateCarByValues(Car car, Car myCar) {
        if (car.getMark() != null) {
            myCar.setMark(car.getMark());
        }
        if (car.getModel() != null) {
            myCar.setModel(car.getModel());
        }
        if (car.getColor() != null) {
            myCar.setColor(car.getColor());
        }
    }

    @Override
    public Resources<Car> getCarsResources(List<Car> cars) {
        Link link = linkTo(CarController.class).withSelfRel();
        return new Resources<>(cars, link);
    }

    @Override
    public Resource<Car> getCarResource(long id) {
        Link link = linkTo(CarController.class).slash(id).withSelfRel();
        Optional<Car> car = getCar(id);
        return new Resource<>(car.get(), link);
    }

    @Override
    public void addColorLink(List<Car> cars) {
        cars.forEach(car -> car.add(linkTo(CarController.class).withRel("colors")));
    }

    @Override
    public void addCarLink(List<Car> cars) {
        cars.forEach(car -> car.add(linkTo(CarController.class).slash(car.getCarId()).withSelfRel()));
    }

    @Override
    public ResponseEntity<Resource<Car>> getResourceResponseEntity(long id) {
        try {
            Resource<Car> carLink = getCarResource(id);
            return new ResponseEntity<>(carLink, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
