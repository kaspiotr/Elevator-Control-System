package pro.kaspiotr.elevatorcontrolsystem.domain.simulation.control;

import org.junit.jupiter.api.Test;
import pro.kaspiotr.elevatorcontrolsystem.domain.simulation.repository.SimulationStep;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SimulationServiceTest {

    @Test
    void whenDirectionDownAndOnTheFirstFlore_pickTargetStore_fromStoresBelowTargetStore() {
        // given
        int requestedStoreNo = 0;
        int direction = - 1; // move down

        // when
        List<Integer> expectedStoreNumbers = Collections.singletonList(0);

        // than
        assertTrue(expectedStoreNumbers.contains(SimulationService.pickTargetStoreNo(requestedStoreNo, direction)));
    }

    @Test
    void whenDirectionUpAndOnTheLastFlore_pickTargetStore_fromStoresAboveTargetStore() {
        // given
        int requestedStoreNo = 4;
        int direction = 1; // move up
        SimulationStep simulationStep = SimulationService.getSimulationStep();
        if (Objects.isNull(simulationStep)) {
            simulationStep = SimulationService.createFirstSimulationStep(requestedStoreNo, 1);
        }

        // when
        int[] storesRange = IntStream.rangeClosed(0, simulationStep.getStoresNo()).toArray();
        List<Integer> expectedStoreNumbers = Arrays.stream(storesRange).boxed().collect(Collectors.toList());

        // than
        assertTrue(expectedStoreNumbers.contains(SimulationService.pickTargetStoreNo(requestedStoreNo, direction)));
    }

    @Test
    void whenDirectionDown_pickTargetStore_fromStoresBelowTargetStore() {
        // given
        int requestedStoreNo = 5;
        int direction = - 1; // move down

        // when
        int[] storesRange = IntStream.rangeClosed(0, 5).toArray();
        List<Integer> expectedStoreNumbers = Arrays.stream(storesRange).boxed().collect(Collectors.toList());

        // than
        assertTrue(expectedStoreNumbers.contains(SimulationService.pickTargetStoreNo(requestedStoreNo, direction)));
    }

    @Test
    void whenDirectionUp_pickTargetStore_fromStoresAboveTargetStore() {
        // given
        int requestedStoreNo = 6;
        int direction = 1; // move up
        SimulationStep simulationStep = SimulationService.getSimulationStep();
        if (Objects.isNull(simulationStep)) {
            simulationStep = SimulationService.createFirstSimulationStep(8, 1);
        }

        // when
        int[] storesRange = IntStream.rangeClosed(requestedStoreNo, simulationStep.getStoresNo()).toArray();
        List<Integer> expectedStoreNumbers = Arrays.stream(storesRange).boxed().collect(Collectors.toList());

        // than
        assertTrue(expectedStoreNumbers.contains(SimulationService.pickTargetStoreNo(requestedStoreNo, direction)));
    }

}
