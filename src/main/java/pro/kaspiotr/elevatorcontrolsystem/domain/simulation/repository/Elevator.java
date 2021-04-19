package pro.kaspiotr.elevatorcontrolsystem.domain.simulation.repository;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pro.kaspiotr.elevatorcontrolsystem.domain.simulation.control.SimulationService;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
public class Elevator {

    private int id;
    @Setter(AccessLevel.NONE)
    private int currentStoreNo;
    private int direction; // up when direction > 0; down when direction < 0
    private List<Passenger> passengers;

    public void setCurrentStoreNo(int currentStoreNo) {
        SimulationStep simulationStep = SimulationService.getSimulationStep();
        int storesNo = simulationStep.getStoresNo();
        int maxRequestedStoreNo = Collections.max(passengers.stream().map(Passenger::getTargetStoreNo).collect(Collectors.toList()));
        int minRequestedStoreNo = Collections.min(passengers.stream().map(Passenger::getTargetStoreNo).collect(Collectors.toList()));
        int maxTargetElevatorStoreNo = Math.min(maxRequestedStoreNo, storesNo);
        int minTargetElevatorStoreNo = Math.max(minRequestedStoreNo, 0);
        putPassengerIntoElevatorOrTakeThemOutAndAddToServedPassengers(currentStoreNo, minTargetElevatorStoreNo, maxTargetElevatorStoreNo);
        if (direction > 0 && currentStoreNo + 1 <= maxTargetElevatorStoreNo) {
            this.currentStoreNo = currentStoreNo + 1;
        }
        if (direction < 0 && currentStoreNo - 1 >= minTargetElevatorStoreNo) {
            this.currentStoreNo = currentStoreNo - 1;
        }
        if (!passengers.isEmpty() && (this.currentStoreNo == 0 || this.currentStoreNo == storesNo)) {
            direction *= -1;
        }
    }

    private boolean isBoardingPassenger(Passenger passenger, int currentStoreNo) {
        return passenger.getRequestedDirection() * this.direction > 0 && currentStoreNo == passenger.getStartStoreNo();
    }

    private boolean isAlightingPassenger(Passenger passenger, int currentStoreNo, int minTargetElevatorStoreNo, int maxTargetElevatorStoreNo) {
        return (passenger.getRequestedDirection() * this.direction > 0
                || currentStoreNo == minTargetElevatorStoreNo
                || currentStoreNo == maxTargetElevatorStoreNo)
                && currentStoreNo == passenger.getTargetStoreNo();
    }

    private void putPassengerIntoElevatorOrTakeThemOutAndAddToServedPassengers(int currentStoreNo, int minTargetElevatorStoreNo, int maxTargetElevatorStoreNo) {
        List<Passenger> boardingPassengers = passengers.stream()
                .filter(passenger -> isBoardingPassenger(passenger, currentStoreNo))
                .collect(Collectors.toList());
        List<Passenger> alightingPassengers = passengers.stream()
                .filter(passenger -> isAlightingPassenger(passenger, currentStoreNo, minTargetElevatorStoreNo, maxTargetElevatorStoreNo))
                .collect(Collectors.toList());
        passengers.forEach(passenger -> {
            if (boardingPassengers.contains(passenger) && !passenger.isInElevator()) {
                passenger.setInElevator(true);
            } else if (alightingPassengers.contains(passenger) && passenger.isInElevator()) {
                passenger.setInElevator(false);
            }
        });
    }

}
