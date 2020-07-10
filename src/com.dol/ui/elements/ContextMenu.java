package com.dol.ui.elements;

import com.dol.ui.util.ImagePanelObserver;
import com.dol.ui.util.Region;

import javax.swing.*;


//TODO: to delete this class
public class ContextMenu extends JPopupMenu {

    Region region;

    public ContextMenu(JFrame frame, ImagePanelObserver imageObserver) {

        JMenuItem itemChooseEurope = new JMenuItem("Choose Europe region");

        itemChooseEurope.addActionListener(e -> {});
        add(itemChooseEurope);
    }
}
