package ui;

import model.EpisodeTitle;
import model.Mode;
import service.FilePicker;
import service.NewNameRenderer;
import service.RenameWorker;
import service.StateMachine;
import service.StringUtils;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class CommonForm {
    private JTextField folderTextField;
    private JButton pickfolder;
    private JButton Go;
    private JLabel status;
    private JTextArea statusField;
    private JPanel home;
    private JPanel selector;

    public CommonForm() {
        pickfolder.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if (StateMachine.mode == Mode.SELECT || StateMachine.mode == Mode.DONE) {
                    handleSelectFile();
                } else {
                    statusField.append(
                        "Currently Unable to Select a File. [Current Status=" + StateMachine.mode + "]\r\n");

                }
            }
        });
        Go.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if (StateMachine.mode == Mode.GET_TITLE) {
                    handleConfirmTitle();
                } else if (StateMachine.mode == Mode.SELECT) {
                    handleVerifyingShow();
                } else {
                    statusField.append("Please select a Folder again. [Current Status=" + StateMachine.mode + "]\r\n");

                }
            }
        });
    }

    private void handleSelectFile() {
        File file = FilePicker.selectFile(pickfolder);
        if (file != null && file.exists() && file.isDirectory()) {
            String absolutePath = file.getAbsolutePath();
            folderTextField.setText(absolutePath);
            StateMachine.rootFolder = file;
            StateMachine.mode = Mode.SELECT;
            statusField.append("Selected [" + absolutePath + "]\r\n");
        }
    }

    private void handleVerifyingShow() {

        //
        try {
            StateMachine.mode = Mode.RUNNING;

            if (StateMachine.rootFolder == null) {
                if (StringUtils.isBlank(folderTextField.getText())) {
                    throw new IllegalStateException("You have not selected a folder.");
                } else {

                }
                StateMachine.rootFolder = new File(folderTextField.getText());
            }

            StateMachine.show = RenameWorker.getShowTitleAndSeason();

            if (StateMachine.show == null) {
                throw new IllegalStateException("Should have found show title");
            }

            StateMachine.mode = Mode.GET_TITLE;
            EpisodeTitle episodeTitle = new EpisodeTitle(StateMachine.show, 4);
            String rendered = NewNameRenderer.render(episodeTitle);
            statusField.append("Series name: [" + StateMachine.show.getTitle() //
                + "]\r\nSeason Number: [" + StateMachine.show.getSeason()//
                + "]\r\nExample new file name:\r\n" + rendered +
                "\r\nPress the 'Start' button again to confirm, or select a different folder.\r\n");


        } catch (Exception ex) {
            StateMachine.mode = Mode.DONE;

            ex.printStackTrace();
            statusField.append("ERROR FATAL:" + ex.getMessage() + "\r\n");
        } finally {
        }
    }

    private void handleConfirmTitle() {
        if (StateMachine.show == null) {
            statusField.append("Please select a Folder again... [Current Show=" + StateMachine.show + "]\r\n");
        }

        //TODO ...

    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("CommonForm");
        frame.setContentPane(new CommonForm().home);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
