package amdp.taxiamdpdomains.taxiamdp;

import amdp.rocksample.RockSampleDomain;
import amdp.rocksample.RockSampleRewardFunction;
import amdp.rocksample.RockSampleTerminationFunction;
import amdp.taxiamdpdomains.testingtools.BoundedRTDPForTests;
import amdp.taxiamdpdomains.testingtools.MutableGlobalInteger;
import burlap.behavior.policy.Policy;
import burlap.behavior.policy.PolicyUtils;
import burlap.behavior.singleagent.Episode;
import burlap.behavior.valuefunction.ConstantValueFunction;
import burlap.debugtools.DPrint;
import burlap.debugtools.RandomFactory;
import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.environment.SimulatedEnvironment;
import burlap.mdp.singleagent.model.RewardFunction;
import burlap.mdp.singleagent.oo.OOSADomain;
import burlap.statehashing.simple.SimpleHashableStateFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by ngopalan on 8/14/16.
 */
public class TaxiBasePlanner {

    public static List<BoundedRTDPForTests> brtdpList= new ArrayList<BoundedRTDPForTests>();
//    DPrint.(3214986, false);


    static protected MutableGlobalInteger bellmanBudget = new MutableGlobalInteger(-1);


    static int  maxTrajectoryLength = 102;


    public static void main(String[] args) {

        DPrint.toggleCode(3214986, false);
        RandomFactory randomFactory = new RandomFactory();
        Random rand = randomFactory.getMapped(0);

        boolean randomStart = false;
        boolean singlePassenger = true;

        bellmanBudget.setValue(4000);

        for(int i =0;i<args.length;i++){
            String str = args[i];
//            System.out.println(str);
            if(str.equals("-r")){
                randomStart = Boolean.parseBoolean(args[i+1]);
            }
            if(str.equals("-b")){
                bellmanBudget.setValue(Integer.parseInt(args[i+1]));
            }
            if(str.equals("-s")){
                singlePassenger = Boolean.parseBoolean(args[i+1]);
            }
        }

        TerminalFunction tf = new RockSampleTerminationFunction();
        RewardFunction rf = new RockSampleRewardFunction(1,tf);

        RockSampleDomain tdGen = new RockSampleDomain(rf,tf);



        tdGen.setTransitionDynamicsLikeFickleTaxiProlem();
        tdGen.setFickleTaxi(true);


        OOSADomain td = tdGen.generateDomain();

        OOSADomain tdEnv = tdGen.generateDomain();


        State startState;

        if(randomStart){
            if(singlePassenger){
                startState = RockSampleDomain.getRandomClassicState(rand, td);
            }
            else{
                startState = RockSampleDomain.getComplexState();
            }

        }
        else{
            if(singlePassenger) {
                startState = RockSampleDomain.getClassicState(td);
            }
            else{
                startState = RockSampleDomain.getComplexState();
            }
        }


//        startState = RockSampleDomain.getComplexState(false);

        BoundedRTDPForTests brtdp = new BoundedRTDPForTests(td, 0.99,  new SimpleHashableStateFactory(false),new ConstantValueFunction(0.),
                new ConstantValueFunction(1.), 0.01, 500);
        brtdp.setRemainingNumberOfBellmanUpdates(bellmanBudget);


        brtdp.setMaxRolloutDepth(25);//5
        brtdp.toggleDebugPrinting(false);
        Policy p = brtdp.planFromState(startState);
        brtdpList.add(brtdp);



        SimulatedEnvironment envN = new SimulatedEnvironment(tdEnv, startState);

        Episode e = PolicyUtils.rollout(p, startState, td.getModel(),maxTrajectoryLength);

//        Visualizer v = RockSampleVisualizer.getVisualizer(5, 5);
//        List<Episode> eaList = new ArrayList<Episode>();
//        eaList.add(e);
//        new EpisodeSequenceVisualizer(v, td, eaList);




//        System.out.println("actions taken: " + e.actionSequence.size());
//        System.out.println("rewards: " + e.discountedReturn(1.));
//        System.out.println("Total updates used: " + count);
        System.out.println(e.actionSequence.size());
        System.out.println(e.discountedReturn(1.));
        System.out.println( brtdp.getNumberOfBellmanUpdates());
//        System.out.println("Total planners used: " + brtdpList.size());
        System.out.println("Base level Taxi");
//        for(BoundedRTDPForTests b:brtdpList){
//            System.out.println(b.getNumberOfBellmanUpdates());
//        }
        System.out.println("random start state: " + randomStart);
        System.out.println("single start state: " + singlePassenger);






    }



}
