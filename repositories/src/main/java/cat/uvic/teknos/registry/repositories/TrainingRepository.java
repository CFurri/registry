package cat.uvic.teknos.registry.repositories;

import cat.uvic.teknos.registry.models.Training;

import java.util.Set;

public interface TrainingRepository extends Repository<Integer, Training> {

    /**
     * Finds all trainings that are marked as mandatory.
     * @return A set of mandatory trainings.
     */
    Set<Training> getMandatoryTrainings();
}