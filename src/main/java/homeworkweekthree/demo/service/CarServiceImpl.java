package homeworkweekthree.demo.service;

import homeworkweekthree.demo.model.Car;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
}
