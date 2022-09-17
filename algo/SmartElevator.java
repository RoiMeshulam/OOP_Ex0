package ex0.algo;

import ex0.Building;
import ex0.CallForElevator;
import ex0.Elevator;

import java.util.ArrayList;
import java.util.LinkedList;
/*
This algorithm is an algorithm that solves the allocation of elevator calls to the smart elevator in a way that our waiting time will be as small as possible.
 */
public class SmartElevator implements ElevatorAlgo {
    public static final int UP = 1, DOWN = -1;
    private Building _building;
    //this data base will save active calls for each elevator.
    public static LinkedList<CallForElevator>[] cmdCheck;
    //this data base will allow us to use our elevators faster
    public static ArrayList<Elevator> _allElevators;
    public SmartElevator(Building b) {
        _building = b;
        cmdCheck = new LinkedList[_building.numberOfElevetors()];
        _allElevators = new ArrayList<>();
        int x = _building.maxFloor()-b.minFloor();
        x = x/b.numberOfElevetors();
        for (int i = 0; i < _building.numberOfElevetors(); i++) {
            Elevator e = _building.getElevetor(i);
            cmdCheck[i] = new LinkedList<CallForElevator>();
            _allElevators.add(e);
            //we will want to spread the elevators to "tactical" positions to start getting calls.
            e.goTo(b.minFloor()+(x*i));
        }
    }
    public LinkedList<CallForElevator>[] getCmdCheck(){
        return cmdCheck;
    }

    @Override
    public Building getBuilding() {
        return _building;
    }

    @Override
    public String algoName() {
        return "Ex0_OOP_Cii_Algo";
    }
    /*
    This function gets a call and allocates an elevator that will handle it.
    We will check which elevator should use it by steps to find the best way to take the call.
     */
    @Override
    public int allocateAnElevator(CallForElevator c) {
        //if the building has only 1 elevator, its pretty obvious the only elevator will accept the call.
        if(_building.numberOfElevetors()==1){
            cmdCheck[0].add(c);
            return 0;}
        //this loop will search for free elevators (according to its calls array size)
        ArrayList<Integer> freeElevators = new ArrayList<>();
        for(int i=0;i<this._building.numberOfElevetors();i++){
            if(cmdCheck[i].size()==0){
                freeElevators.add(i);
            }
        }
        //if we have 1 free elevator -> allocate it to the free elevator
        if (freeElevators.size()==1){
            cmdCheck[freeElevators.get(0)].add(c);
            return freeElevators.get(0);
        }
        //if we have more than 1 free elevator -> allocate it to the closest&fastest one (Inner math is inside the private function).
        else if(freeElevators.size()>1){
            int ans = fastestCalculation(freeElevators,c.getSrc(),c.getDest());
            cmdCheck[ans].add(c);
            return ans;
        }
        //incase we dont find a free from calls elevator, we will try to find one that its LAST destenation is close enough to the call's source level.
        for (int i = 0; i < _building.numberOfElevetors(); i++) {
            if (!cmdCheck[i].isEmpty()) {
                if (Math.abs(cmdCheck[i].getLast().getDest()-c.getSrc())<=5) {
                    cmdCheck[i].add(c);
                    return i;
                }
            }
        }
        //incase we dont find an elevator that is close enough we will find a fast elevator that doesnt have TOO many calls.
        int ans = fastestOne(cmdCheck);
        if (ans != -1) {
            cmdCheck[ans].add(c);
            return ans;
        }
        //incase all the elevators are busy we will calculate the elevator that her speed&distance from source level will be the fastest too accept the call
        ans = fastestCalculation(cmdCheck, c.getSrc(), c.getDest());
        cmdCheck[ans].add(c);
        return ans;
    }
    /*
    This is the "stupidest" command an elevator takes, all it does is "refreshing" the elevator to check if it needs to move somewhere.
    Obviously if the elevator is on the way somewhere, we will let it reach its target to not accidently delete a call.
     */
    @Override
    public void cmdElevator(int elev) {
        //if the elevator doesnt have any calls -> DO NOTHING.
        if (cmdCheck[elev].size() == 0){
            return;
        }
        else{
            //if the elevator is "resting" and the elevator should be on its way to SRC -> we will sent it to SRC.
            if (_building.getElevetor(elev).getState() == Elevator.LEVEL && _building.getElevetor(elev).getPos() != cmdCheck[elev].get(0).getSrc() && cmdCheck[elev].get(0).getState() == CallForElevator.GOING2SRC) {
                _allElevators.get(elev).goTo(cmdCheck[elev].get(0).getSrc());
                return;
            }
            //if the elevator is "resting" and the elevator should be on its way to DEST -> we will sent it to DEST.
            if (_building.getElevetor(elev).getState() == Elevator.LEVEL && cmdCheck[elev].get(0).getState() == CallForElevator.GOIND2DEST) {
                _allElevators.get(elev).goTo(cmdCheck[elev].get(0).getDest());
                return;
            }
            //if the elevator is "resting" and the call is done -> remove the call and attend to next call.
            if (_building.getElevetor(elev).getState() == Elevator.LEVEL && cmdCheck[elev].get(0).getState() == CallForElevator.DONE) {
                cmdCheck[elev].removeFirst();
                cmdElevator(elev);
                return;
            }
        }
    }
    /*
    This functions recieves all the elevators and checks if there is an elevator with a low amount of calls and fast speed.
    @param - _allElevators
    @return - elevator index
     */
    public int fastestOne(LinkedList[] cmdCheck) {
        int sum=0;
        for (int i = 0; i < this.cmdCheck.length; i++) {
            sum+= this.cmdCheck[i].size();
        }
        int average = sum/ this.cmdCheck.length;
        double max = Integer.MIN_VALUE;
        int index = 0;
        for (int i = 0; i < cmdCheck.length; i++) {
            if (cmdCheck[i].size() < 2) {
                if (_allElevators.get(i).getSpeed() > max) {
                    max = _allElevators.get(i).getSpeed();
                    index = i;

                }
            }
        }
        if (max == Integer.MIN_VALUE) {
            return -1;
        }
        return index;
    }
    /*
    These 2 functions are calculating which of all the elevators is the fastest one to do the WHOLE "trip"
    @param - _allElevators/_freeElevators, Call SRC, Call DEST
    @return - elevator index
     */
    public int fastestCalculation(LinkedList[] cmdCheck, int s, int d) {
        double max = Integer.MAX_VALUE;
        int index = 0;
        for (int i = 0; i < cmdCheck.length; i++) {
            double x = Math.abs(_allElevators.get(i).getPos() - s) / (_building.getElevetor(i).getSpeed());
            double y = Math.abs(d - s) / (_allElevators.get(i).getSpeed());
            double speed = (x + y) + ((_allElevators.get(i).getTimeForOpen() + _allElevators.get(i).getTimeForClose()) * 2);
            if (speed < max) {
                max = speed;
                index = i;
            }
        }
        return index;
    }
    public int fastestCalculation(ArrayList<Integer> freeElev, int s, int d) {
        double max = Integer.MAX_VALUE;
        int index = 0;
        for (int i = 0; i < freeElev.size(); i++) {
            double x = Math.abs(_allElevators.get(freeElev.get(i)).getPos() - s) / (_building.getElevetor(freeElev.get(i)).getSpeed());
            double y = Math.abs(d - s) / (_allElevators.get(freeElev.get(i)).getSpeed());
            double speed = (x + y) + ((_allElevators.get(i).getTimeForOpen() + _building.getElevetor(i).getTimeForClose()) * 2);
            if (speed < max) {
                max = speed;
                index = freeElev.get(i);
            }
        }
        return index;
    }
    /*
    This function seaches in a "stupid" way the closest elevator.
    @param - _allElevators, Call SRC
    @return - elevator index
     */
    private int closestElev(int src, ArrayList<Integer> freeElev) {
        int ans = 0;
        int x=Integer.MAX_VALUE;
        for(int i=0;i<freeElev.size();i++){
            if(Math.abs(_allElevators.get(freeElev.get(i)).getPos()-src)<x){
                x=_allElevators.get(freeElev.get(i)).getPos()-src;
                ans=freeElev.get(i);
            }
        }
        return ans;
    }
}