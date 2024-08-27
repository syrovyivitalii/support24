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

    @Scheduled(cron = "0 0 18 * * *", zone = "America/New_York") /* Every day at 18:00 in New York (which is 1:00 AM the next day in Kyiv) */
    @Transactional
    public void changeStatus() {
        taskRepository.updateStatus(LocalDateTime.now());
    }
}