package amdp.rocksample;

/**
 * Created by ngopalan on 6/18/16.
 */


import amdp.rocksample.state.RockSampleState;
import amdp.rocksample.state.RoverAgent;
import amdp.rocksample.state.TaxiLocation;
import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.state.State;

/**
 * Created by ngopalan.
 */
public class TaxiToLocationTerminationFunction implements TerminalFunction {

    TaxiLocation lEnd;
    public TaxiToLocationTerminationFunction(TaxiLocation l) {
        this.lEnd = l;
    }

    @Override
    public boolean isTerminal(State state) {
        RockSampleState sTemp = (RockSampleState)state;
        RoverAgent taxi = sTemp.taxi;
        int taxiX = taxi.x;
        int taxiY = taxi.y;

        if(taxiX==lEnd.x && taxiY==lEnd.y ){
            return true;
        }
        return false;
    }


}
