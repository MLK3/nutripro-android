package com.oddsix.nutripro.rest.models.responses;

import java.util.List;

/**
 * Created by filippecl on 21/12/16.
 */

public class AnalysedPictureResponse {
    private String status;
    private String picture_id;
    private String pictureUrl;
    private List<Food> foods;

    public String getStatus() {
        return status;
    }

    public String getPicture_id() {
        return picture_id;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public List<Food> getFoods() {
        return foods;
    }

    public class Food {
        private String area_id;
        private List<Suggestion> suggestions;
        private List<Point> points;

        public List<Point> getPoints() {
            return points;
        }

        public String getArea_id() {
            return area_id;
        }

        public List<Suggestion> getSuggestions() {
            return suggestions;
        }

        public class Suggestion {
            private String id;
            private String name;
            private int quantity;
            private List<Nutrient> nutrients;

            public String getId() {
                return id;
            }

            public String getName() {
                return name;
            }

            public int getQuantity() {
                return quantity;
            }

            public List<Nutrient> getNutrients() {
                return nutrients;
            }

            public class Nutrient {
                private String name;
                private int quantity;
                private String unit;

                public String getName() {
                    return name;
                }

                public int getQuantity() {
                    return quantity;
                }

                public String getUnit() {
                    return unit;
                }
            }
        }

        public class Point {
            private int x;
            private int y;

            public int getX() {
                return x;
            }

            public int getY() {
                return y;
            }
        }
    }

}
