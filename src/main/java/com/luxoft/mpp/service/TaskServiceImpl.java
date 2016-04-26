package com.luxoft.mpp.service;

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





    public boolean isLoop(int[][] lm ){

        boolean[] passedWay = new boolean[lm.length];

        if ( ! findHangingVertex(lm).isEmpty())
            return false;

        for (int i = 0; i < lm.length; i++) {
            for (int j = 0; j < lm[i].length; j++) {
                passedWay[i] = true;
                if (lm[i][j] != 0) {

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

    private boolean isEmptyRow( int[][] matrix,  int y ){
        for (int i =0; i < matrix.length; i++ )
            if (matrix[y][i] != 0)
                return false;
        return true;
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


}
