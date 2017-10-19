package amdp.maxq.taximaxq;

import amdp.maxq.framework.*;
import amdp.rocksample.RockSampleDomain;
import amdp.rocksample.RockSampleRewardFunction;
import amdp.rocksample.RockSampleTerminationFunction;
import amdp.rocksample.state.TaxiLocation;
import amdp.rocksample.state.TaxiPassenger;
import amdp.rocksample.state.RockSampleState;
import amdp.utilities.BoltzmannQPolicyWithCoolingSchedule;
import burlap.behavior.singleagent.Episode;
import burlap.debugtools.DPrint;
import burlap.debugtools.RandomFactory;
import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.action.ActionType;
import burlap.mdp.core.oo.state.ObjectInstance;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.environment.SimulatedEnvironment;
import burlap.mdp.singleagent.model.RewardFunction;
import burlap.mdp.singleagent.oo.OOSADomain;
import burlap.statehashing.simple.SimpleHashableStateFactory;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Runner for the rocksample max Q runner
 * Created by ngopalan on 5/24/16.
 */
public class TaxiMaxQTest {


    public static void main(String[] args) {

        int debugCode = 12344356;
        RandomFactory randomFactory = new RandomFactory();
        Random rand = randomFactory.getMapped(0);
        DPrint.toggleCode(debugCode,true);


        boolean randomStart = false;
        boolean singlePassenger = true;
        int bellmanUpdateBudget = 256000;

        for(int i =0;i<args.length;i++){
            String str = args[i];
//            System.out.println(str);
            if(str.equals("-r")){
                randomStart = Boolean.parseBoolean(args[i+1]);
            }
            if(str.equals("-b")){
                bellmanUpdateBudget = Integer.parseInt(args[i+1]);
            }
            if(str.equals("-s")){
                singlePassenger = Boolean.parseBoolean(args[i+1]);
            }
        }



//        State state = RockSampleDomain.getComplexState(d);
        TerminalFunction taxiTF = new RockSampleTerminationFunction();
        RewardFunction taxiRF = new RockSampleRewardFunction(1,taxiTF);

        RockSampleDomain TDGen = new RockSampleDomain(taxiRF, taxiTF);

//        tdGen.setDeterministicTransitionDynamics();
        TDGen.setTransitionDynamicsLikeFickleTaxiProlem();
//        tdGen.setDeterministicTransitionDynamics();
        TDGen.setFickleTaxi(true);

        final OOSADomain d = TDGen.generateDomain();

        State s;
        if(singlePassenger){
            //sNew = RockSampleDomain.getRandomClassicState(rand, d, false);
            s = RockSampleDomain.getClassicState(d);
        }
        else{
            s = RockSampleDomain.getComplexState();
        }


        List<ObjectInstance> passengers = ((RockSampleState)s).objectsOfClass(RockSampleDomain.PASSENGERCLASS);
        List<String[]> passengersList = new ArrayList<String[]>();
        for(ObjectInstance p : passengers){
            passengersList.add(new String[]{((TaxiPassenger)p).name()});
        }

        List<ObjectInstance> locations = ((RockSampleState)s).objectsOfClass(RockSampleDomain.LOCATIONCLASS);
        List<String[]> locationsList = new ArrayList<String[]>();
        for(ObjectInstance l : locations){
            locationsList.add(new String[]{((TaxiLocation)l).name()});
        }

        ActionType east = d.getAction(RockSampleDomain.ACTION_EAST);
        ActionType west = d.getAction(RockSampleDomain.ACTION_WEST);
        ActionType south = d.getAction(RockSampleDomain.ACTION_SOUTH);
        ActionType north = d.getAction(RockSampleDomain.ACTION_NORTH);
        ActionType pickup = d.getAction(RockSampleDomain.ACTION_PICKUP);
        ActionType dropoff = d.getAction(RockSampleDomain.ACTION_DROPOFF);

        TaskNode et = new TaxiMAXQL0CardinalMoveTaskNode(east);
        TaskNode wt = new TaxiMAXQL0CardinalMoveTaskNode(west);
        TaskNode st = new TaxiMAXQL0CardinalMoveTaskNode(south);
        TaskNode nt = new TaxiMAXQL0CardinalMoveTaskNode(north);
        TaskNode[] navigateSubTasks = new TaskNode[]{et,wt,st,nt};

        TaskNode stTest = new TaxiMAXQL0CardinalMoveTaskNode(south);





        TaskNode pt = new PickupTaskNode(pickup);
        TaskNode dt = new DropTaskNode(dropoff);




        TaskNode navigate = new NavigateTaskNode("navigate",locationsList,navigateSubTasks);
        TaskNode[] getNodeSubTasks = new TaskNode[]{pt,navigate};
        TaskNode[] putNodeSubTasks = new TaskNode[]{dt,navigate};


        TaskNode getNode = new GetTaskNode("get",passengersList,getNodeSubTasks);

        TaskNode putNode = new PutTaskNode("put",passengersList,putNodeSubTasks);

        TaskNode[] rootNodeSubTasks = new TaskNode[]{getNode, putNode};




        final TaskNode rootNode = new RootTaskNode("root", rootNodeSubTasks,taxiTF);


        String str = "-------- MAXQ Test! ----------";
        DPrint.cl(debugCode,str);
//
//            State state = RockSampleDomain.getClassicState(d, false);

        int numberOfTests = 1;
        int numberOfLearningEpisodes = 100;
        int takeModOf = 10;
        int startTest = 200;





        List<Episode> episodesMAXQ = new ArrayList<Episode>();
        List<Episode> testEpisodesMAXQ = new ArrayList<Episode>();
//                MAXQLearningAgent maxqLearningAgent = new MAXQLearningAgent(rootNode, new SimpleHashableStateFactory(), 1.0, 1.0);
//                MAXQStateAbstractionAgent maxqLearningAgent = new MAXQStateAbstractionAgent(rootNode, new SimpleHashableStateFactory(), 1.0, 0.25);
//        MaxQForTesting maxqLearningAgent = new MaxQForTesting(rootNode, new SimpleHashableStateFactory(), 1.0, 0.25, bellmanUpdateBudget);
        MAXQCleanupTesting maxqLearningAgent = new MAXQCleanupTesting(rootNode, new SimpleHashableStateFactory(), 1.0, 0.25, bellmanUpdateBudget);
//                MAX0LearningAgent maxqLearningAgent = new MAX0LearningAgent(rootNode, new SimpleHashableStateFactory(), 0.95, 0.5);
//                MAX0FasterLearningAgent maxqLearningAgent = new MAX0FasterLearningAgent(rootNode, new SimpleHashableStateFactory(), 0.95, 0.70);
//                maxqLearningAgent.setRmax(0.123 * (1 - 0.95));
        DPrint.toggleCode(maxqLearningAgent.debugCode,false);
        maxqLearningAgent.setVmax(0.123);
        maxqLearningAgent.setQProviderForTaskNode(rootNode);
        maxqLearningAgent.setQProviderForTaskNode(getNode);
        maxqLearningAgent.setQProviderForTaskNode(putNode);
        maxqLearningAgent.setQProviderForTaskNode(navigate);

        maxqLearningAgent.setSolverDerivedPolicyForTaskNode(rootNode, new BoltzmannQPolicyWithCoolingSchedule(50, 0.9996));
        maxqLearningAgent.setSolverDerivedPolicyForTaskNode(getNode, new BoltzmannQPolicyWithCoolingSchedule(50, 0.9939));
        maxqLearningAgent.setSolverDerivedPolicyForTaskNode(putNode, new BoltzmannQPolicyWithCoolingSchedule(50, 0.9996));
        maxqLearningAgent.setSolverDerivedPolicyForTaskNode(navigate, new BoltzmannQPolicyWithCoolingSchedule(50, 0.9879));
//            maxqLearningAgent.setQProviderForTaskNode(rootNode);


        int episodeNum = 0;
        while(maxqLearningAgent.getNumberOfBackups()<bellmanUpdateBudget) {

            str = "MAXQ learning episode: " + episodeNum;
            DPrint.cl(debugCode,str);
            str = "-------------------------------------------------------------";
            DPrint.cl(debugCode,str);
            State sNew;// = RockSampleDomain.getRandomClassicState(rand, d, false);

                if(singlePassenger){
                    //sNew = RockSampleDomain.getRandomClassicState(rand, d, false);
                    sNew = RockSampleDomain.getClassicState(d);
                }
                else{
                    sNew = RockSampleDomain.getComplexState();
                }


//            State sNew = RockSampleDomain.getComplexState(false);
            SimulatedEnvironment envN = new SimulatedEnvironment(d, sNew);

            Episode ea = maxqLearningAgent.runLearningEpisode(envN, 5000);
            episodesMAXQ.add(ea);

            episodeNum++;


        }


        maxqLearningAgent.setFreezeLearning(true);
        str = "-------------------------------------------------------------";
        DPrint.cl(debugCode,str);
        State sNew1;
        if(randomStart){
            if(singlePassenger){
                sNew1 = RockSampleDomain.getRandomClassicState(rand, d);
            }
            else{
                sNew1 = RockSampleDomain.getComplexState();
            }

        }
        else{
            if(singlePassenger) {
                sNew1 = RockSampleDomain.getClassicState(d);
            }
            else{
                sNew1 = RockSampleDomain.getComplexState();
            }
        }

        SimulatedEnvironment envN1 = new SimulatedEnvironment(d, sNew1);
        Episode ea1 = maxqLearningAgent.runLearningEpisode(envN1, 100);
//                    episodesMAXQ.add(ea1);

        maxqLearningAgent.setFreezeLearning(false);
        int numActions = 0;
        for (Episode eaTemp : episodesMAXQ) {
            numActions += eaTemp.actionSequence.size();
        }

        int testActions = ea1.numActions();
        str = "test actions:" + testActions;
//        DPrint.cl(debugCode,str);
        testEpisodesMAXQ.add(ea1);
        System.out.println(testActions);
        System.out.println(ea1.discountedReturn(1.0));
        str = "number of backups: " + maxqLearningAgent.getNumberOfBackups();
//        DPrint.cl(debugCode,str);
//        System.out.println(str);
        System.out.println(maxqLearningAgent.getNumberOfBackups());
        System.out.println("random start: " +randomStart);
        System.out.println("MAXQ!");
        System.out.println("single start state: " + singlePassenger);

        str = "number of params MAXQQ = " + maxqLearningAgent.numberOfParams();
//        DPrint.cl(debugCode,str);
        System.out.println(str);
//        Visualizer v = RockSampleVisualizer.getVisualizer(5, 5);
//        new EpisodeSequenceVisualizer(v, d, testEpisodesMAXQ);

//        str = "num backups: " + maxqLearningAgent.getNumberOfBackups();
//        DPrint.cl(debugCode,str);


    }

}
