package homeworkweekthree.demo.controller;

import homeworkweekthree.demo.model.Car;
import homeworkweekthree.demo.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
        carService.addCarLink(cars);
        Resources<Car> resources = carService.getCarsResources(cars);
        return new ResponseEntity<>(resources, HttpStatus.OK);
    }

    @GetMapping(params = "color")
    public ResponseEntity<Resources<Car>> getCarsByColor(@RequestParam("color") String color) {
        List<Car> cars = carService.getCarByColor(color);
        carService.addCarLink(cars);
        carService.addColorLink(cars);
        Resources<Car> resources = carService.getCarsResources(cars);
        return new ResponseEntity<>(resources, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resource<Car>> getCar(@PathVariable("id") long id) {
        return carService.getResourceResponseEntity(id);
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

}
