package ex0.algo;

import ex0.Building;
import ex0.Elevator;
import ex0.simulator.Call_A;
import ex0.simulator.ElevetorCallList;
import ex0.simulator.Simulator_A;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SmartElevatorTest {


    Building building1;
    Building building2;
    SmartElevator b1_tester;
    SmartElevator b2_tester;

    public SmartElevatorTest() {

        Simulator_A.initData(1, null);
        building1 = Simulator_A.getBuilding();
        Simulator_A.initData(9, null);
        building2 = Simulator_A.getBuilding();

        b1_tester = new SmartElevator(building1);
        b2_tester = new SmartElevator(building2);

    }

    @Test
    void allocateAnElevator() {
        /*
        In this test we check if a call is allocated to the only elevator
         */
        SmartElevatorTest a = new SmartElevatorTest();
        a.b1_tester.allocateAnElevator(new Call_A(0,1,2));
        assertTrue(SmartElevator.cmdCheck[0].size() == 1);

        /*
        In this test we check if the call is being allocated to the fastest elevator as it should be
         */
        SmartElevatorTest b = new SmartElevatorTest();
        b.b2_tester.allocateAnElevator(new Call_A(0,1,2));
        assertTrue(SmartElevator.cmdCheck[3].size() == 1);

    }

    @Test
    void cmdElevator() {
        SmartElevatorTest a = new SmartElevatorTest();
        int elev1 = a.b1_tester.allocateAnElevator(new Call_A(0,0,10));
        a.b1_tester.cmdElevator(elev1);
        assertTrue(SmartElevator._allElevators.get(0).getState() == Elevator.UP);
        assertFalse(SmartElevator._allElevators.get(0).getState() == Elevator.DOWN);
        assertFalse(SmartElevator._allElevators.get(0).getState() == Elevator.LEVEL);

        SmartElevatorTest b = new SmartElevatorTest();
        int elev2 = a.b2_tester.allocateAnElevator(new Call_A(0,95,10));
        a.b2_tester.cmdElevator(elev2);
        assertTrue(SmartElevator._allElevators.get(0).getState() == Elevator.DOWN);
        assertFalse(SmartElevator._allElevators.get(0).getState() == Elevator.UP);
        assertFalse(SmartElevator._allElevators.get(0).getState() == Elevator.LEVEL);


    }
}