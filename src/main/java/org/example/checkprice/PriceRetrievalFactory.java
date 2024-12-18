package org.example.checkprice;

import java.util.HashMap;
import java.util.Map;

public class PriceRetrievalFactory {
    private static final Map<String, PriceRetrievalBehaviour> behaviours = new HashMap<>();

    public static void registerBehaviour(String company, PriceRetrievalBehaviour behaviour){
        behaviours.put(company.toUpperCase(), behaviour);
    }

    public PriceRetrievalBehaviour getBehaviour(String company){
        PriceRetrievalBehaviour behaviour = behaviours.get(company.toUpperCase());
        if (behaviour == null){
            throw new IllegalArgumentException("No behaviour found for company: " + company);
        }
        return behaviour;
    }

}
