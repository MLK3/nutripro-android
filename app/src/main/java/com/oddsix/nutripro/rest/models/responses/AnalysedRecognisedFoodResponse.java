package com.oddsix.nutripro.rest.models.responses;

import java.io.Serializable;
import java.util.List;

/**
 * Created by filippecl on 21/12/16.
 */

public class AnalysedRecognisedFoodResponse extends RecognisedFoodResponse implements Serializable {

    private List<Suggestion> suggestions;

    public List<Suggestion> getSuggestions() {
        return suggestions;
    }

    public AnalysedRecognisedFoodResponse(String id, String name) {
        super(id, name);
    }

    public AnalysedRecognisedFoodResponse(String id, String name, int quantity) {
        super(id, name, quantity);
    }

    public AnalysedRecognisedFoodResponse() {
    }

    public class Suggestion implements Serializable {
        String id;
        String name;
        int quantity;

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public int getQuantity() {
            return quantity;
        }
    }

}
