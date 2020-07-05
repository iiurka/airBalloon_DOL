package com.dol.ui.util;

import com.dol.ui.elements.ImagePanel;

public class ImagePanelObserver {

    private final ImagePanel panel;

    public ImagePanelObserver(ImagePanel panel) {
        this.panel = panel;
    }

    public GeoCoordinates getPointA() {
        return panel.getPointA();
    }

    public GeoCoordinates getPointB() {
        return panel.getPointB();
    }
}
