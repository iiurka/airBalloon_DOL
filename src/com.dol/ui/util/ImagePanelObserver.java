package com.dol.ui.util;

import com.dol.ui.elements.ImagePanel;

public class ImagePanelObserver {

    private final ImagePanel panel;

    public ImagePanelObserver(ImagePanel panel) {
        this.panel = panel;
    }

    public Point getPointA() {
        return panel.getPointA();
    }

    public Point getPointB() {
        return panel.getPointB();
    }
}
