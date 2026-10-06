package com.bot.studytaskbot.service;

import com.bot.studytaskbot.config.BotConfig;
import com.bot.studytaskbot.model.Task;
import com.bot.studytaskbot.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfig botConfig;
    private final TaskRepository taskRepository;

    @Autowired
    public TelegramBot(BotConfig botConfig, TaskRepository taskRepository) {
        this.botConfig = botConfig;
        this.taskRepository = taskRepository;
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getBotToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            if (messageText.equals("/start") || messageText.equals("/help")) {
                sendMessage(chatId, "🤖 *StudyTaskBot - Menu de Comandos*:\n\n" +
                        "➕ `/add <tarefa>` - Adiciona uma nova tarefa\n" +
                        "📋 `/list` - Lista todas as suas tarefas\n" +
                        "✅ `/done <número>` - Marca uma tarefa como concluída\n" +
                        "🗑️ `/delete <número>` - Remove uma tarefa\n" +
                        "ℹ️ `/help` - Mostra esta ajuda");
            } else if (messageText.startsWith("/add ")) {
                String taskDescription = messageText.substring(5);
                Task task = new Task();
                task.setChatId(chatId);
                task.setDescription(taskDescription);
                task.setCompleted(false);
                taskRepository.save(task);

                sendMessage(chatId, "✅ Tarefa salva com sucesso!");
            } else if (messageText.equals("/list")) {
                List<Task> tasks = taskRepository.findByChatId(chatId);
                if (tasks.isEmpty()) {
                    sendMessage(chatId, "📭 Você não tem tarefas cadastradas.");
                } else {
                    StringBuilder sb = new StringBuilder("📋 *Suas Tarefas:*\n\n");
                    for (int i = 0; i < tasks.size(); i++) {
                        Task t = tasks.get(i);
                        String status = t.isCompleted() ? "✅" : "⏳";
                        sb.append((i + 1)).append(". ").append(t.getDescription()).append(" [").append(status).append("]\n");
                    }
                    sendMessage(chatId, sb.toString());
                }
            } else if (messageText.startsWith("/done ")) {
                try {
                    int index = Integer.parseInt(messageText.substring(6).trim()) - 1;
                    List<Task> tasks = taskRepository.findByChatId(chatId);

                    if (index >= 0 && index < tasks.size()) {
                        Task taskToComplete = tasks.get(index);
                        taskToComplete.setCompleted(true);
                        taskRepository.save(taskToComplete);
                        sendMessage(chatId, "🎉 Tarefa " + (index + 1) + " marcada como concluída!");
                    } else {
                        sendMessage(chatId, "❌ Número de tarefa inválido. Use /list para ver os números corretos.");
                    }
                } catch (NumberFormatException e) {
                    sendMessage(chatId, "⚠️ Formato inválido. Use `/done <número>` (Ex: `/done 1`)");
                }
            } else if (messageText.startsWith("/delete ")) {
                try {
                    int index = Integer.parseInt(messageText.substring(8).trim()) - 1;
                    List<Task> tasks = taskRepository.findByChatId(chatId);

                    if (index >= 0 && index < tasks.size()) {
                        Task taskToDelete = tasks.get(index);
                        taskRepository.delete(taskToDelete);
                        sendMessage(chatId, "🗑️ Tarefa " + (index + 1) + " removida com sucesso!");
                    } else {
                        sendMessage(chatId, "❌ Número de tarefa inválido. Use /list para ver os números corretos.");
                    }
                } catch (NumberFormatException e) {
                    sendMessage(chatId, "⚠️ Formato inválido. Use `/delete <número>` (Ex: `/delete 1`)");
                }
            } else {
                sendMessage(chatId, "❓ Comando não reconhecido. Use /help para ver os comandos disponíveis.");
            }
        }
    }

    private void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}