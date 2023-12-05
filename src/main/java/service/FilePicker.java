package service;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class FilePicker {

    public static File selectFile(
        Component parent
    ) {

        JFileChooser jFileChooser = new JFileChooser();

        int result = jFileChooser.showOpenDialog(parent);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jFileChooser.getSelectedFile();
            File parentFile = selectedFile.getParentFile();
            JOptionPane.showMessageDialog(parent, "Opening...\r\n" + parentFile.getAbsolutePath());
            return parentFile;
        }
        return null;
    }
}
