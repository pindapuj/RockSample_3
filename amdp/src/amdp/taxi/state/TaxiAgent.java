package amdp.taxi.state;

import amdp.taxi.TaxiDomain;
import burlap.mdp.core.oo.state.OOStateUtilities;
import burlap.mdp.core.oo.state.ObjectInstance;
import burlap.mdp.core.state.State;
import burlap.mdp.core.state.annotations.DeepCopyState;

import java.util.Arrays;
import java.util.List;

import static amdp.taxi.TaxiDomain.*;
import static amdp.taxi.TaxiDomain.VAR_GOALLOCATION;

/**
 * Created by ngopalan on 6/14/16.
 */
@DeepCopyState
public class TaxiAgent implements ObjectInstance{

    public int x;
    public int y;
    public boolean taxiOccupied;

    protected String name;

    private final static List<Object> keys = Arrays.<Object>asList(VAR_X, VAR_Y, VAR_OCCUPIEDTAXI);


    public TaxiAgent(String name, int x, int y, boolean taxiOccupied) {
        this.name = name;
        this.x =x;
        this.y = y;
        this.taxiOccupied =taxiOccupied;
    }



    public TaxiAgent(String name, int x, int y) {
        this.name = name;
        this.x =x;
        this.y = y;
        this.taxiOccupied =false;
    }

    @Override
    public String className() {
        return TaxiDomain.TAXICLASS;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public TaxiAgent copyWithName(String objectName) {
        TaxiAgent nagent = this.copy();
        nagent.name = objectName;
        return nagent;
    }

    @Override
    public List<Object> variableKeys() {
        return keys;
    }

    @Override
    public Object get(Object variableKey) {
        if(!(variableKey instanceof String)){
            throw new RuntimeException("TaxiAgent variable key must be a string");
        }

        String key = (String)variableKey;
        if(key.equals(VAR_X)){
            return x;
        }
        else if(key.equals(VAR_Y)){
            return y;
        }
        else if(key.equals(VAR_OCCUPIEDTAXI)){
            return taxiOccupied;
        }

        throw new RuntimeException("Unknown key for TaxiAgent: " + key);
    }

    @Override
    public TaxiAgent copy() {
        return new TaxiAgent(name, x,y,taxiOccupied);
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
        if (object instanceof TaxiAgent) {
            TaxiAgent taxiObject = (TaxiAgent) object;
            if((this.taxiOccupied==taxiObject.taxiOccupied && this.x==taxiObject.x && this.y==taxiObject.y && this.name.equals(taxiObject.name))){
                return true;
            }
        }

        return false;


    }

}
