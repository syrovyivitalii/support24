package lv.dsns.support24.common.config.job;

import lombok.RequiredArgsConstructor;
import lv.dsns.support24.task.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class Scheduling {

    @Autowired
    private final TaskRepository taskRepository;

    @Scheduled(cron = "10 1 0 * * *", zone = "Europe/Kyiv") /* every day at 01.00 in Europe/Kyiv */
    @Transactional
    public void changeStatus() {
        taskRepository.updateStatus(LocalDateTime.now());
    }
}