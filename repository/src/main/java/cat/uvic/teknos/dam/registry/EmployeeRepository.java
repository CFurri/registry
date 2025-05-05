package cat.uvic.teknos.dam.registry;

import java.util.Set;

public interface EmployeeRepository <Integer, {
    //void save(Employee employee);
    //void delete(Employee employee);
    //Employee get(int id);
    //Set<Employee> getAll();

    Employee getByName(String employeeName);
    void addEmployee(Employee //i alguna cosa més )
}

// crec que està malament perquè ell ho fa amb musician, d'una band, jo amb employee d'enlloc

//save employee
//mètode delete
//get(int id) -> i aixo un employee
// i un getAll -> això retorna un set de band

// serà la capa amb data access
//només crearem repositoris per les classes princiapls,
//public interface Repository<K, V>{
//    V get (K id);
//    void save(V item );
//    void delete(K id);
//    Set<V> getAll();
//
//    //gestiona un objecte i li hem d'especificar la clau, l'id
//    //tots els repositoris que fem servir nosaltres, han d'implementar aquesta interfície
//
//}
