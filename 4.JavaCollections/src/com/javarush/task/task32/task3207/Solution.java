package com.javarush.task.task32.task3207;

import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/* 
К серверу по RMI
*/
public class Solution {
    public static final String UNIC_BINDING_NAME = "double.string";
    public static Registry registry;

    //pretend we start rmi client as CLIENT_THREAD thread
    public static Thread CLIENT_THREAD = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                DoubleString doubleStr = (DoubleString) registry.lookup(UNIC_BINDING_NAME);
                System.out.println(doubleStr.doubleString("Yo-ho-ho!"));
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (NotBoundException e) {
                e.printStackTrace();
            }
        }
    });

    public static void main(String[] args) {
        //pretend we start rmi server as main thread
        Remote stub = null;
        final DoubleStringImpl service = new DoubleStringImpl();
        try {
            registry = LocateRegistry.createRegistry(2099);
            stub = UnicastRemoteObject.exportObject(service, 0);
            registry.bind(UNIC_BINDING_NAME, stub);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (AlreadyBoundException e) {
            e.printStackTrace();
        }
        //start client
        CLIENT_THREAD.start();
        try {
            Thread.sleep(300);
            UnicastRemoteObject.unexportObject(service, false);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (NoSuchObjectException e) {
            e.printStackTrace();
        }
    }
}