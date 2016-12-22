package com.oddsix.nutripro.rest.models.responses;

import java.util.List;

/**
 * Created by filippecl on 21/12/16.
 */

public class AnalysedRecognisedFoodResponse extends RecognisedFoodResponse {

    private List<Suggestion> suggestions;

    public List<Suggestion> getSuggestions() {
        return suggestions;
    }

    public AnalysedRecognisedFoodResponse(String id, String name) {
        super(id, name);
    }

    public AnalysedRecognisedFoodResponse() {
    }

    public class Suggestion {
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
