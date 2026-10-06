# 🤖 StudyTaskBot

Um bot de gerenciamento de tarefas para o Telegram desenvolvido em **Java** utilizando **Spring Boot**, banco de dados em memória **H2** e a biblioteca oficial do Telegram Bots.

## 🚀 Tecnologias Utilizadas
* **Java 17+**
* **Spring Boot** (Spring Data JPA)
* **Banco H2** (Persistência em memória)
* **TelegramBots Long Polling API**
* **Lombok**

## 📋 Comandos Disponíveis
O bot responde aos seguintes comandos no Telegram:
* `/start` ou `/help` - Exibe o menu de comandos disponíveis.
* `/add <sua tarefa>` - Adiciona uma nova tarefa à sua lista.
* `/list` - Lista todas as suas tarefas com seus respectivos status (`⏳` Pendente / `✅` Concluída).
* `/done <número>` - Marca uma tarefa específica como concluída.
* `/delete <número>` - Remove uma tarefa da lista.

## ⚙️ Como Executar Localmente
1. Clone este repositório:
   ```bash
   git clone [https://github.com/MateusStecca/study-task-bot.git](https://github.com/MateusStecca/study-task-bot.git)
