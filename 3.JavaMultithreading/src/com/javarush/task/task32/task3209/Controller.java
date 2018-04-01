package com.javarush.task.task32.task3209;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.io.*;

public class Controller {
    private View view;
    private HTMLDocument document = new HTMLDocument();
    private File currentFile;

    public Controller(View view) {
        this.view = view;
    }

    public static void main(String[] args) {
        View view = new View();
        Controller controller = new Controller(view);
        view.setController(controller);
        view.init();
        controller.init();

    }

    public void init() {
        createNewDocument();
    }

    public void createNewDocument() {
        view.selectHtmlTab();
        resetDocument();
        view.setTitle("HTML редактор");
        view.resetUndo();
        currentFile = null;
    }

    public void openDocument() {
        try {
            view.selectHtmlTab();
            JFileChooser jFileChooser = new JFileChooser();
            jFileChooser.setFileFilter(new HTMLFileFilter());
            if (jFileChooser.showOpenDialog(view) == JFileChooser.APPROVE_OPTION) {
                currentFile = jFileChooser.getSelectedFile();
                resetDocument();
                view.setTitle(currentFile.getName());
                try(FileReader fileReader = new FileReader(currentFile)) {
                    new HTMLEditorKit().read(fileReader, document, 0);
                }
                view.resetUndo();
            }
        } catch (IOException e) {
            ExceptionHandler.log(e);
        } catch (BadLocationException e) {
            ExceptionHandler.log(e);
        }
    }

    public void saveDocument() {
        try {
            view.selectHtmlTab();
            if (currentFile == null) saveDocumentAs();
            else {
                view.setTitle(currentFile.getName());
                try(FileWriter fileWriter = new FileWriter(currentFile)) {
                    new HTMLEditorKit().write(fileWriter, document, 0, document.getLength());
                }
            }
        } catch (IOException e) {
            ExceptionHandler.log(e);
        } catch (BadLocationException e) {
            ExceptionHandler.log(e);
        }
    }

    public void saveDocumentAs() {
        try {
            view.selectHtmlTab();
            JFileChooser jFileChooser = new JFileChooser();
            jFileChooser.setFileFilter(new HTMLFileFilter());
            if (jFileChooser.showSaveDialog(view) == JFileChooser.APPROVE_OPTION) {
                System.out.println(document.getLength());
                currentFile = jFileChooser.getSelectedFile();
                view.setTitle(currentFile.getName());
                try(FileWriter fileWriter = new FileWriter(currentFile)) {
                    new HTMLEditorKit().write(fileWriter, document, 0, document.getLength());
                }
            }
        } catch (IOException e) {
            ExceptionHandler.log(e);
        } catch (BadLocationException e) {
            ExceptionHandler.log(e);
        }
    }

    public void exit() {
        System.exit(0);
    }

    public void resetDocument() {
        if (document != null) {
            document.removeUndoableEditListener(view.getUndoListener());
            document = (HTMLDocument) new HTMLEditorKit().createDefaultDocument();
            document.addUndoableEditListener(view.getUndoListener());
            view.update();
        }
    }

    public void setPlainText(String text) {
        resetDocument();
        StringReader stringReader = new StringReader(text);
        try {
            new HTMLEditorKit().read(stringReader, document, 0);
        } catch (IOException e) {
            ExceptionHandler.log(e);
        } catch (BadLocationException e) {
            ExceptionHandler.log(e);
        }
    }

    public String getPlainText() {
        try {
            StringWriter stringWriter = new StringWriter();
            new HTMLEditorKit().write(stringWriter,document,0,document.getLength());
            return stringWriter.toString();
        } catch (IOException e) {
            ExceptionHandler.log(e);
        } catch (BadLocationException e) {
            ExceptionHandler.log(e);
        }
        return null;
    }

    public HTMLDocument getDocument() {
        return document;
    }
}
