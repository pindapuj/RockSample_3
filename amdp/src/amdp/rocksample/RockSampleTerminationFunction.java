package amdp.rocksample;

/**
 * Created by ngopalan on 6/18/16.
 */


import amdp.rocksample.state.TaxiLocation;
import amdp.rocksample.state.TaxiPassenger;
import amdp.rocksample.state.RockSampleState;
import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.oo.state.ObjectInstance;
import burlap.mdp.core.state.State;

import java.util.List;

/**
 * Created by ngopalan.
 */
public class RockSampleTerminationFunction implements TerminalFunction {


    @Override
    public boolean isTerminal(State state) {
        List<ObjectInstance> passengerList = ((RockSampleState)state).objectsOfClass(RockSampleDomain.PASSENGERCLASS);
        List<ObjectInstance> locationList = ((RockSampleState)state).objectsOfClass(RockSampleDomain.LOCATIONCLASS);
        for(ObjectInstance p:passengerList){
            if(((TaxiPassenger)p).inTaxi){
                return false;
            }
            String goalLocation = ((TaxiPassenger)p).goalLocation;
            for(ObjectInstance l :locationList){
//                System.out.println("goal: " + goalLocation);
//                System.out.println("location attribute: " + l.getStringValForAttribute(RockSampleDomain.LOCATIONATT));
                if(goalLocation.equals(((TaxiLocation)l).colour)){
                    if(((TaxiLocation)l).x==((TaxiPassenger)p).x
                            && ((TaxiLocation)l).y==((TaxiPassenger)p).y && ((TaxiPassenger)p).pickedUpAtLeastOnce){
                        break;
                    }
                    else{
                        return false;
                    }
                }
            }
        }

        return true;
    }


}
