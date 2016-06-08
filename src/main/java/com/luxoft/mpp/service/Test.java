package com.luxoft.mpp.service;

import com.luxoft.mpp.entity.model.*;

import java.util.*;

/**
 * Created by iivaniv on 25.04.2016.
 */
public class Test {

    private static int[][] matrix = {{0,1,0,0,0,1},
                                     {1,0,1,1,0,0},
                                     {0,1,0,1,1,0},
                                     {0,1,1,0,1,1},
                                     {0,0,1,1,0,1},
                                     {1,0,0,1,1,0},
    };

    private static int[][] matrix1 =
            {{0,0,0,1,1,0},//0
             {0,0,0,1,0,0},//1
             {0,1,0,0,1,0},//2
             {0,0,0,0,0,0},//3
             {0,0,0,0,0,1},//4
             {0,0,0,0,0,0},//5
    };

    private static Integer[] vertex = {5,6,7,8,9,19};

    public static void main(String[] args){



        getQueueVariant8(matrix1, vertex);

    }

    public static List<SimpleMetaData> getQueueVariant8(int[][] matrix, Integer[] vertex) {
        List<SimpleMetaData> result = new ArrayList<SimpleMetaData>();

        Map<Integer, List<List<SimpleVertex>>> allWaysForEachVertex = getAllWaysForEachVertexByDegrease(matrix);
        Map<Integer, List<List<Integer>>> criticalWayVertexID = convertSimpleVertexToVertexIDByDegrease(allWaysForEachVertex);


        for (Map.Entry<Integer, List<List<Integer>>> waysForCurrVertex : criticalWayVertexID.entrySet()) {
            List<Integer> list = getCriticalWayByTaskQuantity(waysForCurrVertex.getValue(), vertex);

            int sum = 0;
            String criticalWay = "";
            for (Integer aList : list) {
                sum += vertex[aList];
                criticalWay += aList+"; ";
            }
            SimpleMetaData simpleMetaData = new SimpleMetaData(criticalWay, waysForCurrVertex.getKey(), sum, list.size());
            simpleMetaData.setVertexID(list);
            result.add(simpleMetaData);
        }

        Collections.sort(result, new Comparator<SimpleMetaData>() {
            @Override
            public int compare(SimpleMetaData o1, SimpleMetaData o2) {
                if (o1.getVertexQuantity() > o2.getVertexQuantity()) {
                    return 1;
                } else if (o1.getVertexQuantity() < o2.getVertexQuantity()) {
                    return -1;
                }
                return 0;
            }
        });

        return result;
    }

    private static List<Integer> getCriticalWayByTaskQuantity(List<List<Integer>> criticalWays, Integer[] vertex){
        List<Integer> result = new ArrayList<Integer>();
        int taskQuantity = 0;
        for (List<Integer> list : criticalWays) {

            if (list.size() < taskQuantity)
                continue;

            if (list.size() == taskQuantity) {
                result = getCriticalWayByTaskValue(criticalWays, vertex);
            } else {
                taskQuantity = list.size();
                result = list;
            }
        }
        return result;

    }

    public static Map<Integer, List<List<SimpleVertex>>> getAllWaysForEachVertexByDegrease(int matrix[][]){

        Map<Integer, List<List<SimpleVertex>>> result = new HashMap<Integer, List<List<SimpleVertex>>>();

        for (int nextRow = 0; nextRow < matrix.length; nextRow++) {
            result.put(nextRow, getAllWayByDegrease(matrix, nextRow));
        }

        return result;

    }

    private static List<List<SimpleVertex>> getAllWayByDegrease(int matrix[][],  int currentVertexID){

        List<List<SimpleVertex>> result = new ArrayList<List<SimpleVertex>>();
        Stack<SimpleVertex> vertexStack = new Stack<SimpleVertex>();
        System.out.println("===========================");

        int col = currentVertexID;
        int row = 0;

        do {

            while( true ) {

                if ( isEmptyCol(matrix, col) ){
                    vertexStack.push(new SimpleVertex(col, 0));
                    break;
                }

                if (matrix[row][col] != 0){
                    vertexStack.push(new SimpleVertex(col, row));
                    col = row;
                    row = 0;
                }else{
                    row ++;
                }
            }

            result.add(new ArrayList<SimpleVertex>(vertexStack));

            while( ! vertexStack.isEmpty() ){

                SimpleVertex simpleVertex = vertexStack.peek();

                int nextRow = simpleVertex.getRow();
                nextRow++;

                if ( hasReferenceToRow(matrix, simpleVertex.getCol(), nextRow) ){
                    System.out.println("form if : vertex ["+simpleVertex.getRow()+"]["+simpleVertex.getCol()+"]");
                    simpleVertex = vertexStack.pop();
                    row = nextRow;
                    col = simpleVertex.getCol();
                    break;
                }
                System.out.println("vertex [" + simpleVertex.getRow() + "][" + simpleVertex.getCol() + "]");
                vertexStack.pop();
            }
            System.out.println("-------------------------");
        }while( ! vertexStack.isEmpty() || hasReferenceToRow(matrix, col, row) );

        return result;

    }


    private static boolean isEmptyCol( int[][] matrix, int inCol ){
        for (int i = 0; i < matrix[inCol].length; i++) {
            if (matrix[i][inCol] != 0)
                return false;
        }
        return true;
    }


    private static boolean isEmptyRow( int[][] matrix, int inRow ){
        for (int i = 0; i < matrix[inRow].length; i++) {
            if (matrix[inRow][i] != 0)
                return false;
        }
        return true;
    }

    private static boolean isEmptyRestOfRow( int[][] matrix, int inRow, int fromNextCol ){
        if ( fromNextCol >= matrix[inRow].length )
            return true;
        for (int i = fromNextCol; i < matrix[inRow].length; i++) {
            if (matrix[inRow][i] != 0)
                return false;
        }
        return true;
    }




    private static List<ProcLink> getVerticalLink(int[][] matrix, int col ){

        List<ProcLink > result = new ArrayList<ProcLink>();
        for (int i = 0; i < matrix.length; i++) {

            if( matrix[i][col] != 0){
                result.add(new ProcLink(i, col));
            }
        }
        return result;
    }

    private static List<ProcLink> getLink(int[][] matrix, int row ){

        List<ProcLink > result = new ArrayList<ProcLink>();
        for (int i = 0; i < matrix.length; i++) {

            if( matrix[row][i] != 0){
                result.add(new ProcLink(row, i));
            }
        }
        return result;
    }

    private static List<ProcLink> getHorizontalLink(int[][] matrix, int row ){

        List<ProcLink > result = new ArrayList<ProcLink>();
        for (int i = 0; i < matrix.length; i++) {

            if( matrix[row][i] != 0){
                result.add(new ProcLink(row, i));
            }
        }
        return result;
    }




    private static void findShooterWayBetweenProcessors(Processor from, Processor to,
                                                        List<List<ProcessorLink>> listOfWay , List<ProcessorLink> currentWay){

//        for ( ProcessorLink procLink : from.getLinks() ) {
//
//            if (currentWay.contains(procLink))
//                continue;
//
//            boolean existingVertex = false;
//            for (ProcessorLink pl : currentWay) {
//                if (pl.getTo() == procLink.getTo() || pl.getFrom() == procLink.getTo()) {
//                    existingVertex = true;
//                    break;
//                }
//            }
//            if (existingVertex)
//                continue;
//
//
//            if ( isHasTail(currentWay, from))//procLink.getFrom() == from  && currentWay.contains()
//                removeLink(currentWay, from);
//            currentWay.add(procLink);
//            List<ProcessorLink> way = new ArrayList<ProcessorLink>(currentWay);
//            if ( procLink.getTo() == to ){
//                listOfWay.add(way);
//            } else {
//                findShooterWayBetweenProcessors(procLink.getTo(), to, listOfWay, way );
//            }
//        }
    }

    private static boolean isHasTail( List<ProcessorLink> currentWay, Processor from){

        for ( ProcessorLink pl : currentWay ){
            if ( pl.getFrom() == from ){
                return true;
            }
        }
        return false;
    }

    private static boolean removeLink( List<ProcessorLink> currentWay, Processor from ){
        ProcessorLink target = new ProcessorLink();
        for ( ProcessorLink pl : currentWay ){
            if ( pl.getFrom() == from ){
                target = pl;
            }
        }
        return currentWay.remove(target);
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

//        ProcessorLink
//                link01 = new ProcessorLink(p0, p1, 1),
//                link05 = new ProcessorLink(p0, p5, 2),
//
//
//                link13 = new ProcessorLink(p1, p3, 2),
//                link12 = new ProcessorLink(p1, p2, 5),
//
//                link32 = new ProcessorLink(p3, p2, 2),
//                link34 = new ProcessorLink(p3, p4, 2),
//                link35 = new ProcessorLink(p3, p5, 2),
//
//                link42 = new ProcessorLink(p4, p2, 6),
//                link45 = new ProcessorLink(p4, p5, 7);
//
//        Collections.addAll(p0.getLinks(), link01, link05);
//        Collections.addAll(p1.getLinks(), link01, link13, link12);
//        Collections.addAll(p2.getLinks(), link12, link32, link42);
//        Collections.addAll(p3.getLinks(), link13, link32, link34, link35);
//        Collections.addAll(p4.getLinks(), link42, link34, link45);
//        Collections.addAll(p5.getLinks(), link05, link35, link45);
//
//        Collections.addAll(result, p0, p1, p2, p3, p4, p5);

        return result;
    }



    private static Map<Integer, List<List<Integer>>> convertSimpleVertexToVertexIDByDegrease(Map<Integer, List<List<SimpleVertex>>> criticalWay){
        Map<Integer, List<List<Integer>>> result = new HashMap<Integer, List<List<Integer>>>();

        for (Map.Entry<Integer, List<List<SimpleVertex>>> waysForCurrVertex : criticalWay.entrySet()){

            List<List<Integer>> wayList =  new ArrayList<List<Integer>>();
            for (List<SimpleVertex> list : waysForCurrVertex.getValue()){

                List<Integer> idsList = new ArrayList<Integer>();
                for (SimpleVertex e :list) {
                    idsList.add(e.getCol());

                }
                wayList.add(idsList);
            }
            result.put(waysForCurrVertex.getKey(), wayList);
        }
        return result;
    }






    private static List<Integer> getCriticalWayByTaskValue(List<List<Integer>> criticalWays, Integer[] vertex){
        List<Integer> result = new ArrayList<Integer>();
        int criticalWay = 0;
        for (List<Integer> list : criticalWays) {
            int sum = 0;
            for (Integer taskId : list) {
                sum += vertex[taskId];
            }
            if (sum > criticalWay) {
                criticalWay = sum;
                result = list;
            }
        }
        return result;

    }


    public static void criticalWay(int[][] matrix){


        for (int nextRow = 0; nextRow < matrix.length; nextRow++) {

            Stack<SimpleVertex> vertexStack = new Stack<SimpleVertex>();

            System.out.println("===========================");

            int col = 0;
            int row = nextRow;
            do{

                while ( true ){

                    if ( isEmptyRow(matrix, row ) ) {
                        SimpleVertex simpleVertex = new SimpleVertex(0, row); // last vertex (empty row)
                        vertexStack.push(simpleVertex);
                        break;
                    }

                    if ( matrix[row][col] != 0){
                        SimpleVertex simpleVertex = new SimpleVertex(col, row);
                        vertexStack.push(simpleVertex);
                        row = col;
                        col = 0;
                    } else {
                        col ++;
                    }
                }

                while ( ! vertexStack.isEmpty() ){
                    SimpleVertex simpleVertex = vertexStack.peek();

                    int nextCol = simpleVertex.getCol();
                    nextCol++;

                    if ( ! isEmptyRestOfRow(matrix, simpleVertex.getRow(), nextCol) ){
                        System.out.println("form if : vertex ["+simpleVertex.getRow()+"]["+simpleVertex.getCol()+"]");
                        simpleVertex = vertexStack.pop();
                        col = nextCol;
                        row = simpleVertex.getRow();
                        break;
                    }
                    System.out.println("vertex [" + simpleVertex.getRow() + "][" + simpleVertex.getCol() + "]");
                    vertexStack.pop();

                }
                System.out.println("-------------------------");

            }while ( ! vertexStack.isEmpty() || !isEmptyRestOfRow(matrix, row, col));

        }

    }






    private static List<SimpleVertex> testWaySearchForCurrentRow(int[][] matrix, int row){

        Stack<SimpleVertex> vertexStack = new Stack<SimpleVertex>();

        int currentCol = row;
        int nextRow = 0;
        vertexStack.push(new SimpleVertex(currentCol, nextRow));
        while(  hasReferenceToRow(matrix, currentCol, nextRow) ) {

            if (matrix[nextRow][currentCol] != 0){
                vertexStack.push(new SimpleVertex(currentCol, nextRow));
                currentCol = nextRow;
                nextRow = 0;
            }else{
                nextRow ++;
            }

        };

        return vertexStack;
    }

    private static boolean hasReferenceToRow( int[][] matrix, int col, int fromRow ){

        for (int i = fromRow; i < matrix.length; i++ ){
            if (matrix[i][col] != 0)
                return true;
        }
        return false;
    }







}
