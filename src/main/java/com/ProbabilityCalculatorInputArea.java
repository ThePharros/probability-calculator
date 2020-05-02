package com;

import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.components.FlatTextField;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ProbabilityCalculatorInputArea extends JPanel {

    private final JTextField uiDropRate;
    private final JTextField uiKillCount;
    private final JTextField uiDropsReceived;

    ProbabilityCalculatorInputArea() {
        setLayout(new GridLayout(3,1,0,4));

        uiDropRate = addComponent("Drop Rate:");
        uiKillCount = addComponent("Kill Count:");
        uiDropsReceived = addComponent("Drops Received:");
    }

    private JTextField addComponent(String label) {
        final JPanel container = new JPanel();
        container.setLayout(new GridLayout(1,2,0,4));

        final JLabel uiLabel = new JLabel(label);
        final FlatTextField uiInput = new FlatTextField();

        uiLabel.setFont(FontManager.getRunescapeSmallFont());
        uiLabel.setBorder(new EmptyBorder(0, 0, 0, 0));
        uiLabel.setForeground(Color.WHITE);

        uiInput.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        uiInput.setHoverBackgroundColor(ColorScheme.DARK_GRAY_HOVER_COLOR);
        uiInput.setBorder(new EmptyBorder(5, 7, 5, 7));

        container.add(uiLabel, BorderLayout.WEST);
        container.add(uiInput, BorderLayout.EAST);

        add(container);

        return uiInput.getTextField();
    }
}
