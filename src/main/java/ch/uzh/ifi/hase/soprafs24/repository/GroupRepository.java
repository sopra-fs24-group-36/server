package ch.uzh.ifi.hase.soprafs24.repository;

import ch.uzh.ifi.hase.soprafs24.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;


@Repository("groupRepository")
public interface GroupRepository extends JpaRepository<Group, Long> {

  Optional<Group> findById(Long id);

}
