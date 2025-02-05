package com.fernandocanabarro.fullstack_ecommerce_app.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fernandocanabarro.fullstack_ecommerce_app.projections.IdProjection;

public class Utils {

    public static <ID> List<? extends IdProjection<ID>> replace(List<? extends IdProjection<ID>> ordered,List<? extends IdProjection<ID>> unordered){

        Map<ID,IdProjection<ID>> map = new HashMap<>();
        for (IdProjection<ID> obj : unordered){
            map.put(obj.getId(), obj);
        }

        List<IdProjection<ID>> result = new ArrayList<>();
        for (IdProjection<ID> obj : ordered){
            result.add(map.get(obj.getId()));
        }
        return result;
    }

}
