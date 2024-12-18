package org.example;

public interface MessageFactory {
    MessageBehaviour createMessageBehaviour(MessageConfiguration config);
}
