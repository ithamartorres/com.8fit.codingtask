package com.eightfit.codingtask.controller;

import com.eightfit.codingtask.dto.ExerciseDTO;
import com.eightfit.codingtask.dto.WorkoutRequestDTO;
import com.eightfit.codingtask.dto.WorkoutResponseDTO;
import com.eightfit.codingtask.service.WorkoutGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Rest Endpoint. Provides the external API for workout selection.
 */
@RestController
public class WorkoutGeneratorController {

    /**
     * Workout generator service. Responsible of all business logic needed by this controller.
     */
    @Autowired
    private WorkoutGeneratorService workoutGeneratorService;

    /**
     *
     */
    @RequestMapping(value = "/workout-selectors", method = RequestMethod.POST)
    public @ResponseBody
    WorkoutResponseDTO filterWorkout(@RequestBody WorkoutRequestDTO requestDTO) throws Exception {
        List<ExerciseDTO> exerciseDTOList = requestDTO.getExercises();
        int timeSpan = requestDTO.getTime();

        List<ExerciseDTO> filteredExercises = workoutGeneratorService.calculateMostIntenseWorkout(timeSpan, exerciseDTOList);
        // This field is already available in the generator but I believe should not be included in the API.
        // Here is added just to match the problem requirement (but the field is redundant)
        int duration = filteredExercises.stream().collect(Collectors.summingInt(ExerciseDTO::getAverageCalorieConsumption));

        return new WorkoutResponseDTO(duration, filteredExercises);
    }
}
