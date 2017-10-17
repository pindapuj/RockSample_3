//package amdp.maxq.taximaxq;
//
//import amdp.rocksample.RockSampleDomain;
//import burlap.oomdp.core.TerminalFunction;
//import burlap.oomdp.core.objects.ObjectInstance;
//import burlap.oomdp.core.states.State;
//
//import java.util.List;
//
///**
// * Created by ngopalan on 5/25/16.
// */
//public class RockSampleTerminationFunction implements TerminalFunction {
//
//
//    @Override
//    public boolean isTerminal(State state) {
//        List<ObjectInstance> passengerList = state.getObjectsOfClass(RockSampleDomain.PASSENGERCLASS);
//        List<ObjectInstance> locationList = state.getObjectsOfClass(RockSampleDomain.LOCATIONCLASS);
//        for(ObjectInstance p:passengerList){
//            if(p.getBooleanValForAttribute(RockSampleDomain.INTAXIATT)){
//                return false;
//            }
//            String goalLocation = p.getStringValForAttribute(RockSampleDomain.GOALLOCATIONATT);
//            for(ObjectInstance l :locationList){
////                System.out.println("goal: " + goalLocation);
////                System.out.println("location attribute: " + l.getStringValForAttribute(RockSampleDomain.LOCATIONATT));
//                if(goalLocation.equals(l.getStringValForAttribute(RockSampleDomain.LOCATIONATT))){
//                    if(l.getIntValForAttribute(RockSampleDomain.XATT)==p.getIntValForAttribute(RockSampleDomain.XATT)
//                            && l.getIntValForAttribute(RockSampleDomain.YATT)==p.getIntValForAttribute(RockSampleDomain.YATT)){
//                        break;
//                    }
//                    else{
//                        return false;
//                    }
//                }
//            }
//        }
//
//        return true;
//    }
//}
