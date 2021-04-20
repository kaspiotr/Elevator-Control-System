package pro.kaspiotr.elevatorcontrolsystem.domain.simulation.control;

import org.springframework.stereotype.Service;
import pro.kaspiotr.elevatorcontrolsystem.domain.simulation.repository.Elevator;
import pro.kaspiotr.elevatorcontrolsystem.domain.simulation.repository.Passenger;
import pro.kaspiotr.elevatorcontrolsystem.domain.simulation.repository.SimulationStep;
import pro.kaspiotr.elevatorcontrolsystem.domain.simulation.utils.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

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

    public static int pickTargetStoreNo(Integer startStoreNo, Integer direction) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return direction < 0
                ? random.nextInt(0, startStoreNo + 1)
                : random.nextInt(startStoreNo, SimulationService.getSimulationStep().getStoresNo() + 1);
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
     * - elevator starts from the first floor (floor no. 0)
     * - elevator moves between the highest requested floor number and lowest requested floor,
     * - elevator takes passengers that are going in the same direction as the current elevator's direction if they were
     *   not already surpassed by the elevator
     * - requests from passengers that are going in a different direction than the elevator's direction are ignored till
     *   elevator changes its direction (when reaching the highest or the lowest requested floor)
     * - simulation continues till they are passengers available
     *
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
