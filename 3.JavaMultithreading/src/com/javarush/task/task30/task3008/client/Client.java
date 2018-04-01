package com.javarush.task.task30.task3008.client;

import com.javarush.task.task30.task3008.Connection;
import com.javarush.task.task30.task3008.ConsoleHelper;
import com.javarush.task.task30.task3008.Message;
import com.javarush.task.task30.task3008.MessageType;
import java.io.IOException;
import java.net.Socket;

public class Client {
    protected Connection connection;
    private volatile boolean clientConnected = false;

    public static void main(String[] args) {
        Client client = new Client();
        client.run();
    }

    protected String getServerAddress() {
        ConsoleHelper.writeMessage("Enter ServerAddress");
        return ConsoleHelper.readString();
    }

    protected int getServerPort() {
        ConsoleHelper.writeMessage("Enter ServerPort");
        return ConsoleHelper.readInt();
    }

    protected String getUserName() {
        ConsoleHelper.writeMessage("Enter UserName");
        return ConsoleHelper.readString();
    }

    protected boolean shouldSendTextFromConsole() {
        return true;
    }

    protected SocketThread getSocketThread() {
        return new SocketThread();
    }

    protected void sendTextMessage(String text) {
        ConsoleHelper.writeMessage("Enter TextMessage");
        try {
            connection.send(new Message(MessageType.TEXT,text));
        } catch (IOException e) {
            ConsoleHelper.writeMessage("Error sendTextMessage to Server");
            clientConnected = false;
        }
    }

    public void run() {
        SocketThread socketThread = getSocketThread();
        socketThread.setDaemon(true);
        socketThread.start();
        synchronized (this) {
          //  while (!clientConnected) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    ConsoleHelper.writeMessage("Error: Waiting was interrupted");
                }
          //  }
           if (clientConnected) {
               ConsoleHelper.writeMessage("Соединение установлено. Для выхода наберите команду 'exit'.");
               String text;
               while (clientConnected && (!(text = ConsoleHelper.readString()).equals("exit")))
                   if (shouldSendTextFromConsole()) sendTextMessage(text);
           }
           else ConsoleHelper.writeMessage("Произошла ошибка во время работы клиента.");
        }
    }

    public class SocketThread extends Thread {
        protected void processIncomingMessage(String message) {
            ConsoleHelper.writeMessage(message);
        }

        protected void informAboutAddingNewUser(String userName) {
            ConsoleHelper.writeMessage(userName + " joined chat");
        }

        protected void informAboutDeletingNewUser(String userName) {
            ConsoleHelper.writeMessage(userName + " left chat");
        }

        protected void notifyConnectionStatusChanged(boolean clientConnected) {
            Client.this.clientConnected = clientConnected;
            synchronized (Client.this) {
                Client.this.notify();
            }
        }

        protected void clientHandshake() throws IOException, ClassNotFoundException {
            while (true) {
                MessageType messageType = Client.this.connection.receive().getType();
                if ((messageType == MessageType.NAME_REQUEST) || (messageType == MessageType.NAME_ACCEPTED)) {
                    if (messageType == MessageType.NAME_REQUEST) Client.this.connection.send(new Message(MessageType.USER_NAME,getUserName()));
                    if (messageType == MessageType.NAME_ACCEPTED) {
                        notifyConnectionStatusChanged(true);
                        break;
                    }
                }
                else throw new IOException("Unexpected MessageType");
            }
        }

        protected void clientMainLoop() throws IOException, ClassNotFoundException {
            while (true) {
                Message message = Client.this.connection.receive();
                MessageType messageType = message.getType();
                if ((messageType == MessageType.TEXT) || (messageType == MessageType.USER_ADDED) || (messageType == MessageType.USER_REMOVED)) {
                    if (messageType == MessageType.TEXT) processIncomingMessage(message.getData());
                    if (messageType == MessageType.USER_ADDED) informAboutAddingNewUser(message.getData());
                    if (messageType == MessageType.USER_REMOVED) informAboutDeletingNewUser(message.getData());
                }
                else throw new IOException("Unexpected MessageType");
            }
        }

        public void run() {
            String serverAddress = getServerAddress();
            int serverPort = getServerPort();
            try {
                Socket socket = new Socket(serverAddress,serverPort);
                connection = new Connection(socket);
                clientHandshake();
                clientMainLoop();
            } catch (IOException | ClassNotFoundException e) {
                notifyConnectionStatusChanged(false);
            }
        }
    }
}
