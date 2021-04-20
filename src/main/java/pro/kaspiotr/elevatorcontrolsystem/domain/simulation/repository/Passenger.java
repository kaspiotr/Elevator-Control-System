package pro.kaspiotr.elevatorcontrolsystem.domain.simulation.repository;

import pro.kaspiotr.elevatorcontrolsystem.domain.simulation.control.SimulationService;

import java.util.Objects;

public class Passenger {

    private int startStoreNo;
    private int targetStoreNo;
    private boolean isInElevator;
    private int requestedDirection;

    public Passenger(Integer startStoreNo, Integer direction, Integer targetStoreNo, boolean isInElevator) {
        this.startStoreNo = startStoreNo;
        this.targetStoreNo = Objects.isNull(targetStoreNo) ?
                SimulationService.pickTargetStoreNo(startStoreNo, direction) : targetStoreNo;
        this.isInElevator = isInElevator;
        this.requestedDirection = direction;
    }

    public int getStartStoreNo() {
        return startStoreNo;
    }

    public int getTargetStoreNo() {
        return targetStoreNo;
    }

    public boolean isInElevator() {
        return isInElevator;
    }

    public void setInElevator(boolean inElevator) {
        isInElevator = inElevator;
    }

    public int getRequestedDirection() {
        return requestedDirection;
    }

}
