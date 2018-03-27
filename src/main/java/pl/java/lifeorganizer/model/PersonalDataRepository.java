package pl.java.lifeorganizer.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalDataRepository extends JpaRepository <PersonalData, Long> {

    PersonalData getPersonalDataByUser(User user);

}
