package com.luxoft.mpp.entity.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * Created by iivaniv on 07.06.2016.
 */
public class Test1 {

    public static void main( String[] args ){

        List<Processor> processors = createMockCS();

        List<List<ProcLink>> listOfWay = new ArrayList<List<ProcLink>>();
        Stack<ProcLink> currentWay = new Stack<ProcLink>();

        Stack<Processor> procWay = new Stack<Processor>();

        Processor from  = processors.get(4);
        Processor to = processors.get(0);

        findShooterWayBetweenProcessors(from, to, listOfWay);

        System.out.println("size = " + currentWay.size());

    }




    private static Processor getProcessorById( List<Processor> processors,  int id ){

        for ( Processor proc : processors ){
            if (proc.getID() == id)
                return proc;
        }

        throw new IllegalArgumentException("No proc with id");

    }


    private static void findShooterWayBetweenProcessors(Processor from, Processor to, List<List<ProcLink>> listOfWay){

        from.setProcPassed(true);
        Stack<ProcLink> currentWay =  new Stack<ProcLink>();
        Stack<Processor> currentProcessors =  new Stack<Processor>();
        currentProcessors.push(from);
        do {
            while (true) {

                boolean exitFromLoop = false;

                for (ProcLink link : from.getLinks()) {

                    if (link.isPassed()) {
                        System.out.println("Link passed");
                        continue;
                    }

                    if (currentWay.contains(link))
                        continue;

                    Processor currentFrom = link.getDest();
                    if (currentFrom == from)
                        currentFrom = link.getSource();
                    if (hasPassedThisProcessor(currentWay, currentFrom))
                        continue;


                    currentFrom.setProcPassed(true);
                    currentProcessors.push(currentFrom);
                    link.setPassed(true);
                    currentWay.push(link);
                    if (link.getSource() == to || link.getDest() == to) {
                        // find  destination
                        exitFromLoop = true;
                    } else {
                        from = currentFrom;
                    }
                    break;
                }

                System.out.println("search");

                if (exitFromLoop) {
                    break;
                }


            }

            listOfWay.add(new ArrayList<ProcLink>(currentWay));



            // rollback
            List<ProcLink> curLinks;

            ProcLink lastLink = currentWay.pop();
            Processor  lastProc = currentProcessors.pop();

            Processor curProc = lastLink.getDest();
            if (curProc == lastProc)
                curProc = lastLink.getSource();

            curLinks = curProc.getLinks();

            Processor prevProc = currentProcessors.peek();

            while( allProcessPassed(curLinks, prevProc, curProc) && ! currentWay.isEmpty()){

                lastProc = curProc;
                curProc = prevProc;
                curLinks  = curProc.getLinks();

                lastLink = currentWay.pop();
                prevProc = lastLink.getDest();
                if (prevProc == curProc)
                    prevProc = lastLink.getSource();





            } ;


        }while( ! currentWay.isEmpty() );
    }

    public static boolean allLinksPassed(List<ProcLink> curLinks, Processor prevProc){

        for (ProcLink procLink : curLinks ){
            if ( prevProc != procLink.getDest() && prevProc != procLink.getSource() ) {
                if ( ! procLink.isPassed()){
                        return false;
                    }
            }
        }
        return true;

    }

    public static boolean allProcessPassed(List<ProcLink> curLinks, Processor prevProc, Processor curProc){

        for (ProcLink procLink : curLinks ){
            if (prevProc != procLink.getDest() && prevProc != procLink.getSource()){
                if ( procLink.getDest() != curProc && ! procLink.getDest().isProcPassed() )
                    return false;
                if ( procLink.getSource() != curProc && ! procLink.getSource().isProcPassed() )
                    return false;
            }

        }
        return true;

    }

    public static void releaseAllVertex(List<ProcLink> links, Stack<Processor> processors){

        for (ProcLink procLink : links ){
            if (! processors.contains(procLink.getDest())) {
                procLink.getDest().setProcPassed(false);
            } else if ( ! processors.contains(procLink.getSource()) )
                procLink.getSource().setProcPassed(false);
        }

    }

    private static boolean hasPassedThisProcessor( List<ProcLink> currentWay, Processor processor ){

        for ( ProcLink link : currentWay){
            if ( link.getDest() == processor || link.getSource() == processor)
                return  true;
        }
        return false;

    }


    public static List<Processor> createMockCS(){

        List< Processor > result = new ArrayList<Processor>();

        Processor
                p0 = new Processor(0),
                p1 = new Processor(1),
                p2 = new Processor(2),
                p3 = new Processor(3),
                p4 = new Processor(4),
                p5 = new Processor(5);

        ProcLink
                link01 = new ProcLink(p0, p1, 2),
                link05 = new ProcLink(p0, p5, 2),
                link13 = new ProcLink(p1, p3, 2),
                link12 = new ProcLink(p1, p2, 2),
                link32 = new ProcLink(p3, p2, 2),
                link34 = new ProcLink(p3, p4, 2),
                link35 = new ProcLink(p3, p5, 2),
                link42 = new ProcLink(p4, p2, 2),
                link45 = new ProcLink(p4, p5, 2);

        Collections.addAll(p0.getLinks(), link01, link05);
        Collections.addAll(p1.getLinks(), link01, link13, link12);
        Collections.addAll(p2.getLinks(), link12, link32, link42);
        Collections.addAll(p3.getLinks(), link13, link32, link34, link35);
        Collections.addAll(p4.getLinks(), link42, link34, link45);
        Collections.addAll(p5.getLinks(), link05, link35, link45);

        Collections.addAll(result, p0, p1, p2, p3, p4, p5);

        return result;
    }

}
