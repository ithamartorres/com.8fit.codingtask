package com.eightfit.codingtask.service;

import com.eightfit.codingtask.dto.ExerciseDTO;
import com.eightfit.codingtask.dto.WorkoutRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class KnapSackSolverTest {


    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Mock
    private KnapSackCacheService knapSackCacheService;

    @InjectMocks
    private WorkoutGeneratorService workoutGeneratorService;

    @Before
    public void setUp() {
        workoutGeneratorService.knapSackSolverDP = new KnapSackSolverDP();
    }

    private static String EXERCISES = "{\n" +
            "  \"exercises\" :\n" +
            "  [\n" +
            "    {\"id\": \"404c1873-96e5-4767-899a-c28697b4ccd4\", \"name\":\"Squats\", \"average_span\": 20, \"average_calorie_consumption\": 120 },\n" +
            "    {\"id\": \"075fe124-5b39-4276-b0ac-de4fd5b38f6f\", \"name\": \"Arm curls\", \"average_span\": 10, \"average_calorie_consumption\": 25},\n" +
            "    {\"id\": \"2085747a-eee5-445a-85fc-92da51709a41\", \"name\": \"Plank\", \"average_span\": 2, \"average_calorie_consumption\": 10},\n" +
            "    {\"id\": \"81fd3a46-e736-4498-9094-f5d7730d1409\", \"name\": \"Jumping jacks\", \"average_span\": 10, \"average_calorie_consumption\": 35}\n" +
            "  ]\n" +
            "}";

    @Test
    public void validScenarioWithoutCache() throws IOException {
        WorkoutRequestDTO requestDTO = OBJECT_MAPPER.readValue(EXERCISES, WorkoutRequestDTO.class);
        List<ExerciseDTO> exercises = requestDTO.getExercises();

        List<ExerciseDTO> filteredExercises = workoutGeneratorService.calculateMostIntenseWorkout(30, exercises);

        int duration = filteredExercises.stream().collect(Collectors.summingInt(ExerciseDTO::getAverageCalorieConsumption));
        Assert.assertEquals(2, filteredExercises.size());
        Assert.assertEquals(155, duration);
    }

    @Test
    public void checkCachingSupport() throws IOException {
        WorkoutRequestDTO requestDTO = OBJECT_MAPPER.readValue(EXERCISES, WorkoutRequestDTO.class);
        List<ExerciseDTO> exercises = requestDTO.getExercises();
        String dataHash = workoutGeneratorService.hashFromObjects(exercises);
        doReturn(new int[50][50]).when(knapSackCacheService).getCachedVersion(dataHash);

        List<ExerciseDTO> filteredExercises = workoutGeneratorService.calculateMostIntenseWorkout(30, exercises);

        int duration = filteredExercises.stream().collect(Collectors.summingInt(ExerciseDTO::getAverageCalorieConsumption));
        Assert.assertEquals(0, filteredExercises.size());
        Assert.assertEquals(0, duration);
    }

    @Test
    public void checkEmptiesScenarios() throws IOException {
        List<ExerciseDTO> exercises = Collections.emptyList();

        List<ExerciseDTO> filteredExercises = workoutGeneratorService.calculateMostIntenseWorkout(30, exercises);

        int duration = filteredExercises.stream().collect(Collectors.summingInt(ExerciseDTO::getAverageCalorieConsumption));
        Assert.assertEquals(0, filteredExercises.size());
        Assert.assertEquals(0, duration);
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkExceptionOnMaximumSupportedScenario() throws IOException {
        List<ExerciseDTO> exercises = Collections.emptyList();
        List<ExerciseDTO> filteredExercises = workoutGeneratorService.calculateMostIntenseWorkout(1000, exercises);
    }

}
