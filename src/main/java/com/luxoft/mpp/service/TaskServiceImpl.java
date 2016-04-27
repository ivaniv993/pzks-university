package com.luxoft.mpp.service;

import com.luxoft.mpp.entity.model.SimpleVertex;
import com.luxoft.mpp.entity.model.TaskElement;
import org.primefaces.model.diagram.Element;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by iivaniv on 22.03.2016.
 *
 */
@Service("taskServiceImpl")
public class TaskServiceImpl implements TaskService {

    private int[][] lm = new int[0][0];
    private Integer[] vertex = new Integer[0];



//    @Autowired
//    private TaskDao taskGraphDao;

    public void saveVertex(List<Element> elements){


    }





    public boolean isLoop(int[][] matrix ){

        boolean[] passedWay = new boolean[matrix.length];

        if ( ! findHangingVertex(matrix).isEmpty())
            return false;

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                passedWay[i] = true;
                if (matrix[i][j] != 0) {

                    if (passedWay[j])
                        return true;

                    i = j;
                    break;
                }

            }

        }
        return false;

    }

    private List<Integer> findHangingVertex( int[][] lm ){

        List<Integer> result = new ArrayList<Integer>();
        for ( int i = 0; i < lm.length; i++ ){

            if (isEmptyColumn(lm, i) && isEmptyRow(lm, i))
                result.add(i);

        }
        return result;
    }

    private boolean isEmptyColumn( int[][] matrix,  int x ){
        for (int i =0; i < matrix[x].length; i++ )
            if (matrix[i][x] != 0)
                return false;
        return true;
    }

    public void getCriticalWay( TaskElement taskElement, Stack<TaskElement> stack){

        if (taskElement.getRelatedTaskElements().isEmpty()){
            for (TaskElement e : stack){
                System.out.print(e.getId() + ", ");
            }
            System.out.println("----------------");
        }
        for ( TaskElement e : taskElement.getRelatedTaskElements()){

            if ( !stack.contains(e) ){
                stack.push(e);
            }
        }

    }


    public Map<Integer, List<List<SimpleVertex>>> getAllWaysForEachVertex(int matrix[][]){

        Map<Integer, List<List<SimpleVertex>>> result = new HashMap<Integer, List<List<SimpleVertex>>>();

        for (int nextRow = 0; nextRow < matrix.length; nextRow++) {
            result.put(nextRow, getAllWayForCurrentVertex(matrix, nextRow));
        }

        return result;

    }

    private static List<List<SimpleVertex>> getAllWayForCurrentVertex( int matrix[][], int currentVertexID){

        List<List<SimpleVertex>> result = new ArrayList<List<SimpleVertex>>();

        Stack<SimpleVertex> vertexStack = new Stack<SimpleVertex>();

        System.out.println("===========================");

        int col = 0;
        int row = currentVertexID;
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

            result.add(new ArrayList<SimpleVertex>(vertexStack));

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

        return result;

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


}
