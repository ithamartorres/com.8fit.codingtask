package com.eightfit.codingtask.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;


public class ExerciseDTOParsingTest {

    private String testString = "{\n" +
            "  \"exercises\" :\n" +
            "  [\n" +
            "    {\"id\": \"404c1873-96e5-4767-899a-c28697b4ccd4\", \"name\":\"Squats\", \"average_span\": 20, \"average_calorie_consumption\": 120 },\n" +
            "    {\"id\": \"075fe124-5b39-4276-b0ac-de4fd5b38f6f\", \"name\": \"Arm curls\", \"average_span\": 10, \"average_calorie_consumption\": 25},\n" +
            "    {\"id\": \"2085747a-eee5-445a-85fc-92da51709a41\", \"name\": \"Plank\", \"average_span\": 2, \"average_calorie_consumption\": 10},\n" +
            "    {\"id\": \"81fd3a46-e736-4498-9094-f5d7730d1409\", \"name\": \"Jumping jacks\", \"average_span\": 10, \"average_calorie_consumption\": 35}\n" +
            "  ]\n" +
            "}";

    @Test
    public void checkExerciseJsonParsing() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        WorkoutRequestDTO exercises = objectMapper.readValue(testString, WorkoutRequestDTO.class);

        int exercisesCount = exercises.getExercises().size();
        Assert.assertEquals(4, exercisesCount);
        ExerciseDTO exerciseDTO = exercises.getExercises().get(0);
        Assert.assertNotNull(exerciseDTO);
    }


}
