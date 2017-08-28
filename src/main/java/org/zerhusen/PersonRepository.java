package org.zerhusen;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zerhusen.model.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    Person findByCpf(String cpf);
}
