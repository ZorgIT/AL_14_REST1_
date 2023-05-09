package ru.matushov.srpingcourser.FirstRestApp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.matushov.srpingcourser.FirstRestApp.models.Person;

public interface PeopleRepository extends JpaRepository<Person, Integer> {
}
