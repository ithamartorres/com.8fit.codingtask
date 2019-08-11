package com.eightfit.codingtask.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO representation of a Exercise. Used in this service as part of the request information and also
 * as part of the response.
 */
public class ExerciseDTO {

    private String id;
    private String name;

    // I would prefer time span as name, but for easy long term maintenance i would use averageSpan instead of
    // timeSpan, but in the proposed input i think that the naming could include the time word (it is not clear
    // that it is liters of water, square meters, or anything). [Also is missing the units in some place easily visible]

    @JsonProperty("average_span")
    private int averageSpam;

    @JsonProperty("average_calorie_consumption")
    private int averageCalorieConsumption;

    public ExerciseDTO() {
    }

    public ExerciseDTO(String id, String name, int averageSpam, int averageCalorieConsumption) {
        this.id = id;
        this.name = name;
        this.averageSpam = averageSpam;
        this.averageCalorieConsumption = averageCalorieConsumption;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAverageSpam() {
        return averageSpam;
    }

    public int getAverageCalorieConsumption() {
        return averageCalorieConsumption;
    }


    @Override
    public String toString() {
        return "ExerciseDTO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", averageSpam=" + averageSpam +
                ", averageCalorieConsumption=" + averageCalorieConsumption +
                '}';
    }
}
