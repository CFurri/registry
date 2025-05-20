package cat.uvic.teknos.dam.registry;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

public interface ShiftRepository extends Repository<Integer, Shift> {
    // Métodos básicos para Shift
    Set<Shift> findByName(String name);
    Set<Shift> findByLocation(String location);
    
    // Métodos para gestionar la relación con Employee (N:M)
    Set<Employee> getShiftEmployees(Integer shiftId);
    void assignEmployeeToShift(Integer shiftId, Integer employeeId);
    void removeEmployeeFromShift(Integer shiftId, Integer employeeId);
    
    // Métodos personalizados (solo 3 de los más útiles)
    Set<Shift> findByTimeRange(LocalTime startTime, LocalTime endTime);
    int countEmployeesInShift(Integer shiftId);
    Set<Shift> getShiftsForDate(LocalDate date);
}
