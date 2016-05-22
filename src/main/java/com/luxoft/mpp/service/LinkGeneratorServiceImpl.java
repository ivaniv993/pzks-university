package com.luxoft.mpp.service;

import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by xXx on 5/18/2016.
 */
@Service("linkGeneratorServiceImpl")
public class LinkGeneratorServiceImpl implements LinkGeneratorService {

    public Map<Integer, List<Integer>> generateGeneralValueForLinks(Integer[] vertex, double correlation){

        Map<Integer, List<Integer>> result = new HashMap<Integer, List<Integer>>();

        int linksSum = getLinkSum(correlation, vertex);

        int linksQuantity = 0;
        Map<Integer, Integer> linksQuantityEachVertex = new HashMap<Integer, Integer>();
        for ( int i = 0; i < vertex.length-1; i ++ ) {
            Random random = new Random();
            int bufValue = 1+random.nextInt(2);
            linksQuantity += bufValue;
            linksQuantityEachVertex.put( i, bufValue );
        }

        if ( linksSum < linksQuantity ){
            // all links will have value 1
            result = getLinksForDefaultValue(linksQuantityEachVertex, 777);
        } else {
            // all links will have different / equal value
            result = getLinksForRandomValue(linksQuantityEachVertex, linksSum, linksQuantity );
        }

        return result;

    }

    public int getLinkQuantity( int[][] matrix ){

        int result = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                if (matrix[i][j] != 0)
                    result++;
            }
        }
        return result;
    }

    public int getLinkSum(double correlation, Integer[] vertex){
        int vertexSum = 0;
        Map<Integer, List<Integer>> result = new HashMap<Integer, List<Integer>>();
        for ( int i = 0; i < vertex.length; i ++ ){
            vertexSum += vertex[i];
        }
        return (int)((vertexSum*(1-correlation))/correlation);
    }
    private Map<Integer, List<Integer>> getLinksForDefaultValue(Map<Integer, Integer> vertexLinks,
                                                                int defaultValue){

        Map<Integer, List<Integer>> result = new HashMap<Integer, List<Integer>>();

        for (Map.Entry<Integer, Integer> entry : vertexLinks.entrySet()){
            List<Integer> links = new ArrayList<Integer>();
            for (int i = 0; i < entry.getValue(); i ++ ){
                links.add(defaultValue);
            }
            result.put(entry.getKey(), links);
        }

        return result;

    }

    private Map<Integer, List<Integer>> getLinksForRandomValue( Map<Integer, Integer> vertexLinks,
                                                                int linksSum,
                                                                int linkQuantity ){

        Map<Integer, List<Integer>> result = new HashMap<Integer, List<Integer>>();

        int times = linksSum/linkQuantity;
        int rest = linksSum % linkQuantity;

        for (Map.Entry<Integer, Integer> entry : vertexLinks.entrySet()){
            List<Integer> links = new ArrayList<Integer>();
            for (int i = 0; i < entry.getValue(); i ++ ){
                links.add(times);
            }

            result.put(entry.getKey(), links);
        }
        result.get(0).set(0, result.get(0).get(0)+rest);

        return result;

    }


}
