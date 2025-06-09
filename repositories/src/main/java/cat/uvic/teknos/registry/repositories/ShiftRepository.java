package cat.uvic.teknos.registry.repositories;

import cat.uvic.teknos.registry.models.Shift;

import java.util.Set;

public interface ShiftRepository extends Repository<Integer, Shift> {

    /**
     * Finds all shifts that occur at a specific location.
     * @param location The location to search for.
     * @return A set of shifts at that location.
     */
    Set<Shift> getByLocation(String location);
}