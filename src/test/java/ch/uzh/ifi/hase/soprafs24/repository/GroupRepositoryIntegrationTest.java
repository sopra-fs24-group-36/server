package ch.uzh.ifi.hase.soprafs24.repository;

import ch.uzh.ifi.hase.soprafs24.entity.Group;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class GroupRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private GroupRepository groupRepository;

    @Test
    public void findById_success() {
        // given
        List<Long> members = Arrays.asList(10L, 20L, 30L);

        Group group = new Group();
        group.setName("name");
        group.setMembers(members);

        entityManager.persist(group);
        entityManager.flush();

        // when
        Optional<Group> found = groupRepository.findById(group.getId());

        // then
        assertNotNull(found.get());
    }

}
