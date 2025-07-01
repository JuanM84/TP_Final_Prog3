package frp.utn.tp.taskmanagement.domain;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {

    // If you don't need a total row count, Slice is better than Page.
    Slice<Task> findAllBy(Pageable pageable);
    List<Task> findByPerson(Person person);
    void deleteByPerson(Person person);
}
