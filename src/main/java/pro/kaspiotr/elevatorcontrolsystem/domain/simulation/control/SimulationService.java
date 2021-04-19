package pro.kaspiotr.elevatorcontrolsystem.domain.simulation.control;

import org.springframework.stereotype.Service;
import pro.kaspiotr.elevatorcontrolsystem.domain.simulation.repository.Elevator;
import pro.kaspiotr.elevatorcontrolsystem.domain.simulation.repository.Passenger;
import pro.kaspiotr.elevatorcontrolsystem.domain.simulation.repository.SimulationStep;
import pro.kaspiotr.elevatorcontrolsystem.domain.simulation.utils.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class SimulationService {

    private static final int INITIAL_STORE_NO = 0;
    private static final int INITIAL_DIRECTION = 1;
    private static SimulationStep simulationStep;

    public static SimulationStep createFirstSimulationStep(int storesNo, int elevatorsNo) {
        List<Elevator> elevators = new ArrayList<>();
        for (int i = 0; i < elevatorsNo; i++) {
            Elevator elevator = new Elevator(i + 1, INITIAL_STORE_NO, INITIAL_DIRECTION, Collections.emptyList());
            elevators.add(elevator);
        }
        simulationStep = new SimulationStep(storesNo, elevators);
        return simulationStep;
    }

    public static SimulationStep getSimulationStep() {
        return simulationStep;
    }

    public static SimulationStep pickupElevatorAndAddPassengerRequest(String elevatorId, String requestedStartStoreNo, String direction) {
        SimulationStep simulationStep = SimulationService.getSimulationStep();
        List<Elevator> elevators = simulationStep.getElevators();
        Elevator elevator = getElevatorById(elevators, elevatorId);
        elevator.setDirection(Integer.parseInt(direction));
        List<Passenger> passengers = new ArrayList<>(elevator.getPassengers());
        passengers.add(new Passenger(Integer.parseInt(requestedStartStoreNo), Integer.parseInt(direction), null, false));
        elevator.setPassengers(passengers);
        return simulationStep;
    }

    private static Elevator getElevatorById(List<Elevator> elevators, String elevatorId) {
        return elevators.stream().filter(e -> Integer.valueOf(elevatorId).equals(e.getId())).findAny()
                .orElseThrow(() -> new IllegalStateException(Constants.THERE_IS_NO_ELEVATOR_WITH_ID_ERROR_MESSAGE + elevatorId));
    }

    /**
     * Elevator algorithm - it is basically the same algorithm that is used in real elevators:
     * - elevator goes to the highest requested floor number - target floor,
     * - until elevator is going up it accepts request from floors that number is higher than its current target floor
     * - elevator collects passengers from the highest floor and stops on every requested floor
     * - if none from the passengers is not going to the bottom floor it stops on the floor with least number
     * - after a while, when elevator is not called it goes down to the bottom floor if it is not already on it
     * @return SimulationStep
     */
    public static SimulationStep performAlgorithmStep() {
        List<Elevator> elevators = simulationStep.getElevators();
        for (Elevator elevator : elevators) {
            if (!elevator.getPassengers().isEmpty()) {
                int elevatorDirection = elevator.getDirection();
                if (elevatorDirection > 0) {
                    int currentElevatorStoreNo = elevator.getCurrentStoreNo();
                    elevator.setCurrentStoreNo(currentElevatorStoreNo);
                } else if (elevatorDirection < 0) {
                    int currentElevatorStoreNo = elevator.getCurrentStoreNo();
                    elevator.setCurrentStoreNo(currentElevatorStoreNo);
                }
            }
        }
        return simulationStep;
    }

}
