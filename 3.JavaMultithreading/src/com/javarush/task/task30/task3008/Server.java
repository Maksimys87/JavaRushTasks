package com.javarush.task.task30.task3008;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    private static Map<String, Connection> connectionMap = new ConcurrentHashMap<>();
    public static void main(String[] args) {
        int port = ConsoleHelper.readInt();
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Сервер запущен.");
            while (true) {
                Socket socket = serverSocket.accept();
                new Handler(socket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendBroadcastMessage(Message message) {
        for (Map.Entry<String,Connection> pair:connectionMap.entrySet()) {
            try {
                pair.getValue().send(message);
            } catch (IOException e) {
                ConsoleHelper.writeMessage("The message has not been delivered to " + pair.getKey());
            }
        }
    }

    private static class Handler extends Thread {
        private Socket socket;
        public Handler(Socket socket) {
            this.socket = socket;
        }

        private String serverHandshake(Connection connection) throws IOException, ClassNotFoundException {
            while (true) {
                connection.send(new Message(MessageType.NAME_REQUEST));
                Message message = connection.receive();
                String name;
                if (message.getType() == MessageType.USER_NAME){
                    if (!message.getData().isEmpty()) {
                        if (!connectionMap.containsKey(message.getData())) {
                            name = message.getData();
                            connectionMap.put(name,connection);
                            connection.send(new Message(MessageType.NAME_ACCEPTED));
                            return name;
                        }
                    }
                }
            }
        }

        private void serverMainLoop(Connection connection, String userName) throws IOException, ClassNotFoundException {
            while (true) {
                Message message = connection.receive();
                if (message.getType() == MessageType.TEXT)
                    sendBroadcastMessage(new Message(MessageType.TEXT,String.format("%s: %s",userName,message.getData())));
                else ConsoleHelper.writeMessage(userName + " sent not TEXT");
            }
        }

        private void sendListOfUsers(Connection connection, String userName) throws IOException {
            for (Map.Entry<String,Connection> pair:connectionMap.entrySet()) {
                if (!pair.getKey().equals(userName)) {
                    connection.send(new Message(MessageType.USER_ADDED,pair.getKey()));
                }
            }
        }

        @Override
        public void run() {
            ConsoleHelper.writeMessage("A new connection was installed with " + String.valueOf(socket.getRemoteSocketAddress()));
            String userName = null;
            try(Connection connection = new Connection(socket)) {
                userName = serverHandshake(connection);
                sendBroadcastMessage(new Message(MessageType.USER_ADDED,userName));
                sendListOfUsers(connection,userName);
                serverMainLoop(connection,userName);
            } catch (IOException | ClassNotFoundException e) {
                ConsoleHelper.writeMessage("Error of connection with " + socket.getRemoteSocketAddress());
            }
                if (userName != null) {
                    connectionMap.remove(userName);
                    sendBroadcastMessage(new Message(MessageType.USER_REMOVED, userName));
                }
                ConsoleHelper.writeMessage("Connection with " + socket.getRemoteSocketAddress() + " closed");
        }
    }
}
