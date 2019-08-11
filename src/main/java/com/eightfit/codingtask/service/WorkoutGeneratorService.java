package com.eightfit.codingtask.service;

import com.eightfit.codingtask.dto.ExerciseDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class WorkoutGeneratorService {

    /**
     * Maximum supported time span (in minutes) for any particular time duration of a workout
     */
    private static final int MAXIMUM_TIME_SPAN = 8 * 60;

    /**
     * Json object serializer. Used for hashing method of required exercise dtos
     * and cache base on that hashed string.
     */
    private static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * Component responsible of data caching optimal exercises selection
     */
    @Autowired
    private KnapSackCacheService knapSackCacheService;

    /**
     * Component responsible of computing the selection of exercises.
     */
    @Autowired
    protected KnapSackSolverDP knapSackSolverDP;

    /**
     * Select the most intense workout based on calories from a given set of exercises and with the restriction
     * that it doesn't last more than the provided time span.
     */
    public List<ExerciseDTO> calculateMostIntenseWorkout(int timeSpan, List<ExerciseDTO> exerciseDTOS) throws IOException {

        if (timeSpan > MAXIMUM_TIME_SPAN) throw new IllegalArgumentException("Exceded maximum time span support");

        if (timeSpan == 0 || exerciseDTOS == null || exerciseDTOS.size() == 0) {
            return Collections.emptyList();
        }

        // Checks for cached solution first
        String sha256hex = hashFromObjects(exerciseDTOS);
        int[][] cachedVersion = knapSackCacheService.getCachedVersion(sha256hex);

        // Creates data structures used for the knapsack solver and to assemble the selected exercises based
        // on the knapsack solution.
        int n = exerciseDTOS.size();
        int w = timeSpan;

        int[] val = new int[n];
        int[] wt = new int[n];
        for (int i = 0; i < n; i++) {
            ExerciseDTO currentExercise = exerciseDTOS.get(i);
            wt[i] = currentExercise.getAverageSpam();
            val[i] = currentExercise.getAverageCalorieConsumption();
        }

        if (cachedVersion == null) {
            // We solve a bigger problem to have already a cached version of all possible time spans.
            int[][] newGlobalSolution = knapSackSolverDP.solve(MAXIMUM_TIME_SPAN, wt, val, n);
            knapSackCacheService.putInCache(sha256hex, newGlobalSolution);
            cachedVersion = newGlobalSolution;
        }

        // Creates the actual set of exercises based from
        List<Integer> selection = knapSackSolverDP.knapsackSelection(cachedVersion, wt, val, n, w);
        List<ExerciseDTO> filteredSelection = new ArrayList<>();
        for (Integer index : selection) {
            filteredSelection.add(exerciseDTOS.get(index));
        }

        return filteredSelection;
    }

    /**
     * Helper method used to create a unique id from a set of exercises.
     * TODO: Sort the exercises alphabetically and creates the appropriate tests for that behavior (Jira Task-ABC)
     */
    protected String hashFromObjects(List<ExerciseDTO> exerciseDTOS) throws JsonProcessingException {
        String exerciseString = OBJECT_MAPPER.writeValueAsString(exerciseDTOS);
        return DigestUtils.md5DigestAsHex(exerciseString.getBytes());
    }
}
