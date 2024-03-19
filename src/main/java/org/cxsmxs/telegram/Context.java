package org.cxsmxs.telegram;

import org.cxsmxs.telegram.entities.*;
import java.util.List;

public class Context {
    private final Update update;
    private final Message message;
    private final Chat chat;
    private final List<String> commandArgs;

    public Context(Update update, Message message, Chat chat, List<String> commandArgs) {
        this.update = update;
        this.message = message;
        this.chat = chat;
        this.commandArgs = commandArgs;
    }
}

