package pro.kaspiotr.elevatorcontrolsystem.domain.simulation.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class SimulationStep {

    private int storesNo;
    private List<Elevator> elevators;

}
