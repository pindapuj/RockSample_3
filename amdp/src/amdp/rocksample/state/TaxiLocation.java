package amdp.rocksample.state;

import amdp.rocksample.RockSampleDomain;
import burlap.mdp.core.oo.state.OOStateUtilities;
import burlap.mdp.core.oo.state.ObjectInstance;
import burlap.mdp.core.state.annotations.DeepCopyState;

import java.util.Arrays;
import java.util.List;

import static amdp.rocksample.RockSampleDomain.*;

/**
 * Created by ngopalan on 6/14/16.
 */

// so are the taxi locations the depot locations?
@DeepCopyState
public class TaxiLocation implements ObjectInstance {

    public int x;
    public int y;
    public String colour;

    String name;
    private final static List<Object> keys = Arrays.<Object>asList(VAR_LOCATION, VAR_Y, VAR_X);
    public TaxiLocation(int x, int y, String name, String colour) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.colour = colour;
    }

    @Override
    public String className() {
        return RockSampleDomain.LOCATIONCLASS;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public TaxiLocation copyWithName(String objectName) {
        TaxiLocation nLocation = this.copy();
        nLocation.name = objectName;
        return nLocation;
    }

    @Override
    public List<Object> variableKeys() {
        return keys;
    }

    @Override
    public Object get(Object variableKey) {
        if(!(variableKey instanceof String)){
            throw new RuntimeException("TaxiLocation variable key must be a string");
        }
        String key = (String)variableKey;
        if(key.equals(VAR_X)){
            return x;
        }
        else if(key.equals(VAR_Y)){
            return y;
        }
        else if(key.equals(VAR_LOCATION)){
            return colour;
        }

        throw new RuntimeException("Unknown key for TaxiLocation " + key);
    }

    @Override
    public TaxiLocation copy() {
        return new TaxiLocation(x,y,name,colour);
    }

    @Override
    public String toString() {
        return OOStateUtilities.objectInstanceToString(this);
    }

    @Override
    public boolean equals(Object object){
        if (this == object) {
            return true;
        }
        if (object instanceof TaxiLocation) {
            TaxiLocation locationObject = (TaxiLocation) object;
            if((this.x == locationObject.x
                    && this.y == locationObject.y
                    && this.colour.equals(locationObject.colour)
                    && this.name.equals(locationObject.name))){
                return true;
            }

        }

        return false;

    }
}
