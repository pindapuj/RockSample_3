package amdp.rocksample;

import amdp.rocksample.state.RockSampleState;
import amdp.rocksample.state.RoverAgent;
import amdp.rocksample.state.TaxiLocation;
import amdp.rocksample.state.TaxiPassenger;
import burlap.mdp.core.action.Action;
import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.oo.state.ObjectInstance;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.model.RewardFunction;

import java.util.List;

/**
 * Created by ngopalan on 5/25/16.
 */
public class RockSampleRewardFunction implements RewardFunction {

    public double stepReward = -1.0;
    public double illegalAction = -10;
    public double goalReward = +20;

    TerminalFunction tf;
    Integer numPass = 0;
    public RockSampleRewardFunction(Integer numPassengers, TerminalFunction tf) {
        numPass = numPassengers;
        this.tf = tf;

    }

    @Override
    public double reward(State state, Action groundedAction, State state1) {

        if(tf.isTerminal(state1)){
            return numPass * goalReward + stepReward;
        }
        // illegal dropoff
        if(groundedAction.actionName().equals(RockSampleDomain.ACTION_DROPOFF)){
            boolean flag = false;
            ObjectInstance taxi = ((RockSampleState)state).objectsOfClass(RockSampleDomain.TAXICLASS).get(0);
//            boolean taxiOccupied = ((RoverAgent)rocksample).taxiOccupied;
            List<ObjectInstance> passengers = ((RockSampleState)state).objectsOfClass(RockSampleDomain.PASSENGERCLASS);
            List<ObjectInstance> locationList = ((RockSampleState)state).objectsOfClass(RockSampleDomain.LOCATIONCLASS);
            for(ObjectInstance p : passengers){
                if(((TaxiPassenger)p).inTaxi){
                    String goalLocation = ((TaxiPassenger)p).goalLocation;
                    for(ObjectInstance l :locationList){
                        if(goalLocation.equals(((TaxiLocation)l).colour)){
                            if(((TaxiLocation)l).x==((TaxiPassenger)p).x
                                    && ((TaxiLocation)l).y==((TaxiPassenger)p).y){
                                flag = true;
                                break;
                            }
                        }
                    }


                }
            }
            if(!flag){
                return illegalAction+stepReward;
            }

        }
        // illegal pickup when picking up when a passenger is not present at rocksample's location
        if(groundedAction.actionName().equals(RockSampleDomain.ACTION_PICKUP)){
            boolean flag = false;
            ObjectInstance taxi = ((RockSampleState)state).objectsOfClass(RockSampleDomain.TAXICLASS).get(0);
            boolean taxiOccupied = ((RoverAgent)taxi).taxiOccupied;

            if(taxiOccupied){
                return illegalAction + stepReward;
            }
            List<ObjectInstance> passengers = ((RockSampleState)state).objectsOfClass(RockSampleDomain.PASSENGERCLASS);
            int taxiX = ((RoverAgent)taxi).x;
            int taxiY = ((RoverAgent)taxi).y;
//            List<ObjectInstance> locationList = state.getObjectsOfClass(RockSampleDomain.LOCATIONCLASS);
            for(ObjectInstance p : passengers){
                if(!((TaxiPassenger)p).inTaxi){
                    if(taxiX==((TaxiPassenger)p).x
                            && taxiY==((TaxiPassenger)p).y){
                        flag = true;
                        break;
                    }
                }
            }
            if(!flag){
                return illegalAction+stepReward;
            }
        }

        return stepReward;
    }
}
