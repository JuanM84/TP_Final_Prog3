package frp.utn.tp.taskmanagement.service;

import frp.utn.tp.taskmanagement.domain.Person;
import frp.utn.tp.taskmanagement.domain.Task;
import frp.utn.tp.taskmanagement.domain.TaskRepository;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

@Service
@PreAuthorize("isAuthenticated()")
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class TaskService {

    private final TaskRepository taskRepository;

    private final Clock clock;

    TaskService(TaskRepository taskRepository, Clock clock) {
        this.taskRepository = taskRepository;
        this.clock = clock;
    }

    public void createTask(String description, Person person, @Nullable LocalDate dueDate) {
        if ("fail".equals(description)) {
            throw new RuntimeException("This is for testing the error handler");
        }
        var task = new Task();
        task.setDescription(description);
        task.setPerson(person);
        task.setCreationDate(clock.instant());
        task.setDueDate(dueDate);
        taskRepository.saveAndFlush(task);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public List<Task> list(Pageable pageable) {
        return taskRepository.findAllBy(pageable).toList();
    }

    @Transactional(readOnly = true)
    public List<Task> findByPerson(Person person) {
        return taskRepository.findByPerson(person);
    }

    public void updateTask(Task task) {
        taskRepository.saveAndFlush(task);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    // Borrar todas las tareas de un usuario
    public void deleteByPerson(Person person){
        taskRepository.deleteByPerson(person);
    }

}