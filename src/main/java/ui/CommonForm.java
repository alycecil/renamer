package ui;


import javafx.util.Pair;
import model.EpisodeTitle;
import model.Mode;
import service.FilePicker;
import service.NewNameRenderer;
import service.RenameWorker;
import service.StateMachine;
import service.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.PrintWriter;
import java.util.List;

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

                if (StateMachine.mode == Mode.CONFIRM_IT) {
                    handleDoRename();
                } else if (StateMachine.mode == Mode.GET_TITLE) {
                    handleConfirmTitle();
                } else if (StateMachine.mode == Mode.SELECT) {
                    handleVerifyingShow();
                } else {
                    statusField.append("Please select a Folder again. [Current Status=" + StateMachine.mode + "]\r\n");

                }
            }
        });
    }

    private void handleDoRename() {
        try {
            for (Pair<File, File> r : StateMachine.renames) {

                File from = r.getKey(), to = r.getValue();

                boolean success = from.renameTo(to);

                if (!success) {
                    statusField.append(new StringBuilder()
                        .append("!!!!! !!!! !!!!\r\nFailed to rename \r\nFrom:")
                        .append(from.getAbsolutePath())
                        .append("\r\nTo:")
                        .append(to.getAbsolutePath())
                        .append("\r\n")
                        .toString());
                } else {
                    statusField.append(new StringBuilder()
                        .append("Renamed From:")
                        .append(from)
                        .append(" To:")
                        .append(to)
                        .append("\r\n")
                        .toString());

                }
            }

            StateMachine.mode = Mode.DONE;
        } catch (Exception ex) {
            StateMachine.mode = Mode.DONE;

            ex.printStackTrace();
            statusField.append("ERROR FATAL:" + ex.getMessage() + "\r\n");
        }

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
                File file = new File(folderTextField.getText());
                if (file.isDirectory()) {
                    StateMachine.rootFolder = file;
                } else {
                    StateMachine.rootFolder = file.getParentFile();
                }
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
        try {
            List<Pair<File, File>> res = RenameWorker.prepRenames(statusField);
            if (res == null || res.isEmpty()) {
                statusField.append("This didnt work for some reason, we didnt get any files to do.\r\n");
                StateMachine.mode = Mode.DONE;

            } else {

                StringBuilder sb = new StringBuilder();
                sb.append("Renames Prepared:\r\n");

                for (Pair<File, File> r : res) {
                    File from = r.getKey(), to = r.getValue();

                    sb
                        .append("From:")
                        .append(from.getAbsolutePath())
                        .append("\r\n")
                        .append("To:")
                        .append(to.getAbsolutePath())
                        .append("\r\n");

                }

                String str = sb.toString();

                File renameLog = getRenameLog();
                statusField.append(str + "We wrote a log of the files we will rename to \r\n" + renameLog +
                    "\r\nPress the 'Start' button again\r\n");

                try (PrintWriter out = new PrintWriter(renameLog)) {
                    out.println(str);
                }
                StateMachine.mode = Mode.CONFIRM_IT;

            }
        } catch (Exception ex) {
            StateMachine.mode = Mode.DONE;

            ex.printStackTrace();
            statusField.append("ERROR FATAL:" + ex.getMessage() + "\r\n");
        }
    }

    private File getRenameLog() {
        return new File(StateMachine.rootFolder, "renamelog.txt");
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("CommonForm");
        frame.setContentPane(new CommonForm().home);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    {
        // GUI initializer generated by IntelliJ IDEA GUI Designer
        // >>> IMPORTANT!! <<<
        // DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        home = new JPanel();
        home.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(4, 1, new Insets(0, 0, 0, 0), -1, -1));
        selector = new JPanel();
        selector.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        home.add(selector, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER,
            com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK |
                com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK |
                com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false
        ));
        final JLabel label1 = new JLabel();
        label1.setText("Folder");
        selector.add(label1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST,
            com.intellij.uiDesigner.core.GridConstraints.FILL_NONE,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false
        ));
        folderTextField = new JTextField();
        selector.add(folderTextField, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST,
            com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false
        ));
        pickfolder = new JButton();
        pickfolder.setText("...");
        selector.add(pickfolder, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER,
            com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK |
                com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false
        ));
        Go = new JButton();
        Go.setText("Start");
        home.add(Go, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER,
            com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK |
                com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false
        ));
        status = new JLabel();
        status.setText("status");
        home.add(status, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST,
            com.intellij.uiDesigner.core.GridConstraints.FILL_NONE,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false
        ));
        statusField = new JTextArea();
        statusField.setEditable(false);
        statusField.setEnabled(true);
        statusField.setLineWrap(true);
        statusField.setText("");
        statusField.setWrapStyleWord(true);
        home.add(statusField, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1,
            com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER,
            com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW,
            com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0,
            false
        ));
        label1.setLabelFor(folderTextField);
    }

    /** @noinspection ALL */
    public JComponent $$$getRootComponent$$$() { return home; }

}
