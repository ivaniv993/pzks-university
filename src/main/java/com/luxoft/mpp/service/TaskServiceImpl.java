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


    public void getCriticalWay(  int[][] matrix, Integer[] vertex ){

        System.out.println("Link matrix ");
        for (int[] aMatrix : matrix) {
            for (int anAMatrix : aMatrix) {
                System.out.print("  " + anAMatrix);
            }
            System.out.println();
        }
        System.out.println("Link matrix -------------");

        for (int i = 0; i < matrix.length; i++) {

            Stack<Integer> ways =  new Stack<Integer>();
            Map<Integer, Integer> wayMap = new HashMap<Integer, Integer>();

            int buf = i;
            do{

                for (int j = buf; j < matrix[buf].length; j++) {

                    if ( matrix[buf][j] != 0 ){
                        ways.push(j);
                        wayMap.put(j, i);
                        break;
                    }
                }

                while (!ways.isEmpty()){
                    buf = ways.pop();
                    int k = wayMap.get(buf);
                    buf++;
                    if ( !emptyRow(matrix, k, buf))
                        break;

                }

                for ( Integer way : ways ){
                    System.out.print(way + ", ");
                }
                System.out.println("----------------");



            }while(!ways.isEmpty());


        }

    }

    private boolean emptyRow(int[][] matrix, int i, int j){
        for (int k = j; k < matrix[i].length; k++) {
            if (matrix[i][j] != 0)
                return true;
        }
        return false;
    }

}
