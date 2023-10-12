package br.com.nataliadiotto.todolist.task;


import br.com.nataliadiotto.todolist.task.TaskRepository;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
//https://localhost:8080/tasks/
public class TaskController {

    @Autowired //ask spring to manage
    private TaskRepository taskRepository;

    @PostMapping("/")
    public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest request) {
        System.out.println("It has arrived at controller");
        var userId = request.getAttribute("userId");
        taskModel.setUserId((UUID) userId);

        //validating dates
        var currentDate = LocalDateTime.now();
        if (currentDate.isAfter(taskModel.getStartAt()) || currentDate.isAfter(taskModel.getEndAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Start and end date must be after current date.");
        }

        /*if (taskModel.getCreatedAt().isAfter(taskModel.getEndAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("End date must be after creation date.");
        }*/

        var task = this.taskRepository.save(taskModel);
        return ResponseEntity.status(HttpStatus.OK).body(task);
    }

    @GetMapping("/") //return everything related to that user
    public List<TaskModel> list(HttpServletRequest request) {
        var userId = request.getAttribute("userId");
        var tasks = this.taskRepository.findByUserId((UUID) userId);
        return tasks;
    }

}
