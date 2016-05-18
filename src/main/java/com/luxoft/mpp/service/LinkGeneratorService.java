package com.luxoft.mpp.service;

import java.util.List;
import java.util.Map;

/**
 * Created by xXx on 5/18/2016.
 */
public interface LinkGeneratorService {

    Map<Integer, List<Integer>> generateGeneralValueForLinks(Integer[] vertex, double correlation);

}
