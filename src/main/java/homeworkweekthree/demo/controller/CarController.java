package homeworkweekthree.demo.controller;

import homeworkweekthree.demo.model.Car;
import homeworkweekthree.demo.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RestController
@RequestMapping(value = "/api/cars",
        produces = {MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XHTML_XML_VALUE})
public class CarController {

    private CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping
    public ResponseEntity<Resources<Car>> getAllCars() {
        List<Car> cars = carService.getCars();
        addCarLink(cars);
        Resources<Car> resources = getCarsResources(cars);
        return new ResponseEntity<>(resources, HttpStatus.OK);
    }

    @GetMapping(params = "color")
    public ResponseEntity<Resources<Car>> getCarsByColor(@RequestParam("color") String color) {
        List<Car> cars = carService.getCarByColor(color);
        addCarLink(cars);
        addColorLink(cars);
        Resources<Car> resources = getCarsResources(cars);
        return new ResponseEntity<>(resources, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resource<Car>> getCar(@PathVariable("id") long id) {
        return getResourceResponseEntity(id);
    }

    @PostMapping
    public ResponseEntity<?> addCar(@RequestBody Car car) {
        carService.getCars().add(car);
        return new ResponseEntity<>(car, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeCar(@PathVariable("id") long id) {
        Optional<Car> exist = carService.getCars().stream().filter(myCar -> myCar.getCarId() == id).findFirst();
        if (exist.isPresent()) {
            carService.getCars().remove(exist.get());
            return new ResponseEntity<>(exist.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping
    public ResponseEntity<?> replaceCar(@RequestBody Car car) {
        Optional<Car> exist = carService.getCars().stream().filter(myCar -> myCar.getCarId() == car.getCarId()).findFirst();
        if (exist.isPresent()) {
            carService.getCars().remove(exist.get());
            carService.getCars().add(car);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PatchMapping
    public ResponseEntity<?> updateCar(@RequestBody Car car) {
        boolean carExist = carService.getCar(car.getCarId()).isPresent();
        if (carExist) {
            carService.getCar(car.getCarId()).ifPresent(myCar -> {
                carService.updateCarByValues(car, myCar);
            });
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    private ResponseEntity<Resource<Car>> getResourceResponseEntity(long id) {
        try {
            Resource<Car> carLink = getCarResource(id);
            return new ResponseEntity<>(carLink, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    private void addCarLink(List<Car> cars) {
        cars.forEach(car -> car.add(linkTo(CarController.class).slash(car.getCarId()).withSelfRel()));
    }

    private void addColorLink(List<Car> cars) {
        cars.forEach(car -> car.add(linkTo(CarController.class).withRel("colors")));
    }

    private Resource<Car> getCarResource(long id) {
        Link link = linkTo(CarController.class).slash(id).withSelfRel();
        Optional<Car> car = carService.getCar(id);
        return new Resource<>(car.get(), link);
    }

    private Resources<Car> getCarsResources(List<Car> cars) {
        Link link = linkTo(CarController.class).withSelfRel();
        return new Resources<>(cars, link);
    }
}
