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
@DeepCopyState
public class RockSampleMapWall implements ObjectInstance {
    public int wallMin;
    public int wallMax;
    public int wallOffset;

    public String name;

    /*
    a variable for book keeping, true implies the wall object is vertical
     */
    public boolean verticalWall;

    private final static List<Object> keys = Arrays.<Object>asList(VAR_WALLOFFSET, VAR_WALLMIN, VAR_WALLMAX);

    public RockSampleMapWall(String name, int wallMin, int wallMax, int wallOffset, boolean verticalWall) {
        this.name = name;
        this.wallMax = wallMax;
        this.wallMin = wallMin;
        this.wallOffset = wallOffset;
        this.verticalWall = verticalWall;
    }

    @Override
    public String className() {
        return RockSampleDomain.WALLCLASS;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public RockSampleMapWall copyWithName(String objectName) {
        RockSampleMapWall nWall = this.copy();
        nWall.name = objectName;
        return nWall;
    }

    @Override
    public List<Object> variableKeys() {
        return keys;
    }

    @Override
    public Object get(Object variableKey) {
        if(!(variableKey instanceof String)){
            throw new RuntimeException("RockSampleMapWall variable key must be a string");
        }
        String key = (String)variableKey;
        if(key.equals(VAR_WALLMAX)){
            return wallMax;
        }
        else if(key.equals(VAR_WALLMIN)){
            return wallMin;
        }
        else if(key.equals(VAR_WALLOFFSET)){
            return wallOffset;
        }
        else if(key.equals(VAR_VERTICALWALL)){
            return verticalWall;
        }

        throw new RuntimeException("Unknown key for TaxiMap Wall: " + key);
    }

    @Override
    public RockSampleMapWall copy() {
        return new RockSampleMapWall(name, wallMin, wallMax,wallOffset, verticalWall);
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
        if (object instanceof RockSampleMapWall) {
            RockSampleMapWall wallObject = (RockSampleMapWall) object;
            if((this.verticalWall == wallObject.verticalWall
                    && this.wallMax == wallObject.wallMax
                    && this.wallMin == wallObject.wallMin
                    && this.wallOffset == wallObject.wallOffset
                    && this.name().equals(wallObject.name()) )){
                return true;
            }

        }

        return false;

    }
}
