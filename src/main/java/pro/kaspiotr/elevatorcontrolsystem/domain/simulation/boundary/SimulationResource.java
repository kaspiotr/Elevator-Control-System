package pro.kaspiotr.elevatorcontrolsystem.domain.simulation.boundary;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pro.kaspiotr.elevatorcontrolsystem.domain.simulation.control.SimulationService;
import pro.kaspiotr.elevatorcontrolsystem.domain.simulation.repository.Elevator;
import pro.kaspiotr.elevatorcontrolsystem.domain.simulation.repository.Passenger;
import pro.kaspiotr.elevatorcontrolsystem.domain.simulation.repository.SimulationStep;
import pro.kaspiotr.elevatorcontrolsystem.domain.simulation.utils.Constants;

import java.util.ArrayList;
import java.util.List;

@Controller
public class SimulationResource {

    @GetMapping(path = "/simulation", produces = "application/hal+json;charset=utf8")
    public ResponseEntity<Object> getFirstSimulationStep(
            @RequestParam("elevatorsNo") String elevatorsNo,
            @RequestParam("storesNo") String storesNo) {
        SimulationStep simulationStep = SimulationService.createFirstSimulationStep(Integer.parseInt(storesNo), Integer.parseInt(elevatorsNo));
        return ResponseEntity.status(HttpStatus.OK).body(simulationStep);
    }

    @GetMapping(path="/pickup", produces = "application/hal+json;charset=utf8")
    public ResponseEntity<Object> pickup(
            @RequestParam("elevatorId") String elevatorId,
            @RequestParam("requestedStartStoreNo") String requestedStartStoreNo,
            @RequestParam("direction") String direction){
        SimulationStep simulationStep = SimulationService.pickupElevatorAndAddPassengerRequest(elevatorId, requestedStartStoreNo, direction);
        return ResponseEntity.status(HttpStatus.OK).body(simulationStep);
    }

    @GetMapping(path="update", produces = "application/hal+json;charset=utf8")
    public ResponseEntity<Object> update(
            @RequestParam("elevatorId") String elevatorId,
            @RequestParam("currentStoreNo") String currentStoreNo,
            @RequestParam("targetStoreNo") String targetStoreNo) {
        SimulationStep simulationStep = SimulationService.getSimulationStep();
        List<Elevator> elevators = simulationStep.getElevators();
        Elevator elevator = elevators.stream().filter(e -> Integer.valueOf(elevatorId).equals(e.getId())).findAny()
                .orElseThrow(() -> new IllegalStateException(Constants.THERE_IS_NO_ELEVATOR_WITH_ID_ERROR_MESSAGE + elevatorId));
        List<Passenger> passengers = new ArrayList<>(elevator.getPassengers());
        int requestedDirection = (Integer.parseInt(targetStoreNo) - Integer.parseInt(currentStoreNo)) > 0 ? 1 : -1;
        passengers.add(new Passenger(Integer.parseInt(currentStoreNo), requestedDirection, Integer.parseInt(targetStoreNo), false));
        elevator.setPassengers(passengers);
        return ResponseEntity.status(HttpStatus.OK).body(simulationStep);
    }

    @GetMapping(path = "step", produces = "application/hal+json;charset=utf8")
    public ResponseEntity<Object> step() {
        SimulationStep simulationStep = SimulationService.performAlgorithmStep();
        return ResponseEntity.status(HttpStatus.OK).body(simulationStep);
    }

    @GetMapping(path = "status", produces = "application/hal+json;charset=utf8")
    public ResponseEntity<Object> status() {
        SimulationStep simulationStep = SimulationService.getSimulationStep();
        List<Elevator> elevators = simulationStep.getElevators();
        return ResponseEntity.status(HttpStatus.OK).body(elevators);
    }

}
