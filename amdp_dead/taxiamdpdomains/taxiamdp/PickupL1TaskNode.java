package amdp.taxiamdpdomains.taxiamdp;

import amdp.amdpframework.GroundedPropSC;
import amdp.amdpframework.GroundedTask;
import amdp.amdpframework.NonPrimitiveTaskNode;
import amdp.amdpframework.TaskNode;
import amdp.taxi.TaxiDomain;
import amdp.taxiamdpdomains.taxiamdplevel1.TaxiL1Domain;
import burlap.mdp.auxiliary.common.GoalConditionTF;
import burlap.mdp.auxiliary.stateconditiontest.StateConditionTest;
import burlap.mdp.core.action.Action;
import burlap.mdp.core.action.ActionType;
import burlap.mdp.core.action.UniversalActionType;
import burlap.mdp.core.oo.propositional.GroundedProp;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.common.GoalBasedRF;
import burlap.mdp.singleagent.model.RewardFunction;
import burlap.mdp.singleagent.oo.OOSADomain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ngopalan on 8/14/16.
 */
public class PickupL1TaskNode extends NonPrimitiveTaskNode{



    ActionType pickupType;

    public PickupL1TaskNode(ActionType pickupType, OOSADomain taxiL1Domain, OOSADomain taxiL0Domain, TaskNode[] children){
        this.pickupType = pickupType;
//        this.oosaDomain = taxiL1Domain;
        this.name = pickupType.typeName();
        this.oosaDomain = taxiL0Domain;
        this.oosaDomain.clearActionTypes();
        this.oosaDomain.addActionTypes(
//                new UniversalActionType(ACTION_NORTH),
//                new UniversalActionType(ACTION_SOUTH),
//                new UniversalActionType(ACTION_EAST),
//                new UniversalActionType(ACTION_WEST),
//                new UniversalActionType(ACTION_DROPOFF),
//                new UniversalActionType(ACTION_FILLUP),
                new UniversalActionType(TaxiDomain.ACTION_PICKUP));

        this.childTaskNodes = children;
    }

    @Override
    public Object parametersSet(State s) {
        List<String[]> params = new ArrayList<String[]>();
        params.add(new String[]{});
        return params;
    }

    @Override
    public boolean terminal(State s, Action action) {
        StateConditionTest sc =  new GroundedPropSC(new GroundedProp(oosaDomain.propFunction(TaxiDomain.PASSENGERPICKUPPF), new String[]{}));
        return new GoalConditionTF(sc).isTerminal(s);
    }

    @Override
    public RewardFunction rewardFunction(Action action) {
        StateConditionTest sc =  new GroundedPropSC(new GroundedProp(oosaDomain.propFunction(TaxiDomain.PASSENGERPICKUPPF), new String[]{}));
        return new GoalBasedRF(sc);
    }

    @Override
    public List<GroundedTask> getApplicableGroundedTasks(State s) {
        List<GroundedTask> gtList = new ArrayList<GroundedTask>();
        List<Action> gtActions = pickupType.allApplicableActions(s);
        for(Action a:gtActions){
            gtList.add(new GroundedTask(this,a));
        }
        return gtList;
    }




}
