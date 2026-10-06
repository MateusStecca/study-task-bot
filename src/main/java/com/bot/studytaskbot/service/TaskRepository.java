package com.bot.studytaskbot.repository;

import com.bot.studytaskbot.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByChatId(Long chatId);
    Optional<Task> findByIdAndChatId(Long id, Long chatId);
}