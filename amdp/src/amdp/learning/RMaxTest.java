package amdp.learning;

import amdp.rocksample.RockSampleDomain;
import amdp.rocksample.RockSampleRewardFunction;
import amdp.rocksample.RockSampleTerminationFunction;
import amdp.rocksample.RockSampleVisualizer;
import burlap.behavior.singleagent.Episode;
import burlap.behavior.singleagent.learning.modellearning.rmax.PotentialShapedRMax;
import burlap.debugtools.RandomFactory;
import burlap.mdp.singleagent.SADomain;
import burlap.mdp.core.state.*;
import burlap.mdp.singleagent.environment.Environment;
import burlap.mdp.singleagent.environment.SimulatedEnvironment;
import burlap.statehashing.simple.SimpleHashableStateFactory;
import burlap.visualizer.Visualizer;

/**
 * Created by ngopalan on 2/23/17.
 */
public class RMaxTest {

    public static void main(String[] args) {

//        GridWorldDomain gw = new GridWorldDomain(11,11); //11x11 grid world
//        gw.setMapToFourRooms(); //four rooms layout
//        gw.setProbSucceedTransitionDynamics(1); //stochastic transitions with 0.8 success rate
//        GridWorldRewardFunction rf = new GridWorldRewardFunction(11,11);
//        rf.setReward(10, 10, 1);
//        GridWorldTerminalFunction tf = new GridWorldTerminalFunction(10, 10);
//        gw.setRf(rf);
//        gw.setTf(tf);

        RandomFactory randomFactory = new RandomFactory();
        RockSampleTerminationFunction ttf = new RockSampleTerminationFunction();
        RockSampleRewardFunction trf = new RockSampleRewardFunction(1,ttf);
        RockSampleDomain tdGen = new RockSampleDomain(trf,ttf);
//        taxiDomain.fickleTaxi = true;


//        tdGen.setDeterministicTransitionDynamics();
        tdGen.setTransitionDynamicsLikeFickleTaxiProlem();
//        tdGen.setDeterministicTransitionDynamics();
        tdGen.setFickleTaxi(true);

        SADomain domain = tdGen.generateDomain();
//        SADomain domain = gw.generateDomain(); //generate the grid world domain

        //setup initial state
//    State s = new GridWorldState(new GridAgent(0, 0), new GridLocation(10, 10, "loc0"));
//        State s = new GridWorldState(new GridAgent(0, 0));
        State s = RockSampleDomain.getRandomClassicState(randomFactory.getMapped(0), domain);

        //create visualizer and explorer
//        Visualizer v = GridWorldVisualizer.getVisualizer(gw.getMap());
        Visualizer v = RockSampleVisualizer.getVisualizer(5, 5);
//    SampleModel model = new
        Environment env = new SimulatedEnvironment(domain, s);
//        VisualExplorer exp = new VisualExplorer(domain, env, v);

        //set control keys to use w-s-a-d
//    exp.addKeyAction("w", GridWorldDomain.ACTION_NORTH, "");
//    exp.addKeyAction("s", GridWorldDomain.ACTION_SOUTH, "");
//    exp.addKeyAction("a", GridWorldDomain.ACTION_WEST, "");
//    exp.addKeyAction("d", GridWorldDomain.ACTION_EAST, "");

//        exp.initGUI();
//        exp.startLiveStatePolling(1000/20);

        // Learn it
        PotentialShapedRMax rMax = new PotentialShapedRMax(domain, 0.9, new SimpleHashableStateFactory(), 100, 3, 0.05, 100);

//        StateEnumerator senum = new StateEnumerator(domain, new SimpleHashableStateFactory());

//        senum.findReachableStatesAndEnumerate(env.currentObservation());

        for(int i=0;i<40;i++) {
            Episode e = rMax.runLearningEpisode(env);
            env.resetEnvironment();
            System.out.println(e.numTimeSteps());
        }

//        for(int i=0;i<senum.numStatesEnumerated();i++){
//            for(ActionType actionType : domain.getActionTypes()){
//                List<Action> actionList = actionType.allApplicableActions(s);
//                for(Action a:actionList){
//                    System.out.println();
//                }
//            }
//        }

    }

}
