package com.dol.ui.elements;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImagePanel extends JPanel {

    private final int width;
    private final int height;

    public ImagePanel(int width, int height) {
        this.width = width;
        this.height = height;
    }

    //TODO: написать нормальный вывод ошибки пользователю
    //--------------------------------------------
    public void paintComponent (Graphics g) {
        Image image = null;

        try {
            image = ImageIO.read(new File("resources" + File.separator + "map1.jpg"));
        } catch (IOException e) {
            System.out.println("Файл map1.jpg не найден");
            System.exit(1);
        }

        g.drawImage(image, 0, 0, width-17, height, null);
    }
}