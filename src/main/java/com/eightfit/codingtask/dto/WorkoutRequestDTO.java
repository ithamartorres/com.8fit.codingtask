package com.eightfit.codingtask.dto;

import java.util.List;


/**
 * DTO That represents the request for the Workout generator service.
 */
public class WorkoutRequestDTO {

    private int time;
    private List<ExerciseDTO> exercises;

    public WorkoutRequestDTO(int time, List<ExerciseDTO> exercises) {
        this.time = time;
        this.exercises = exercises;
    }

    public WorkoutRequestDTO() {
    }

    public int getTime() {
        return time;
    }

    public List<ExerciseDTO> getExercises() {
        return exercises;
    }

    @Override
    public String toString() {
        return "WorkoutRequestDTO{" +
                "time=" + time +
                ", exercises=" + exercises +
                '}';
    }
}
