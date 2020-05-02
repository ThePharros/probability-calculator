package com;

import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.BorderFactory;
import java.awt.*;
import java.text.DecimalFormat;

public class ProbabilityCalculatorOutputArea extends JPanel {

    private String outputMsg = "Awaiting input...";
    private DecimalFormat df = new DecimalFormat("#.####");
    private final JTextArea textArea;
    private double atLeastChance;
    private double zeroChance;
    private double exactChance;
    private String strAtLeastChance;
    private String strExactChance;
    private String strZeroChance;

    ProbabilityCalculatorOutputArea(double dropRate, int killCount, int exactDrops) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(ColorScheme.DARKER_GRAY_COLOR, 5));
        setBackground(ColorScheme.DARK_GRAY_HOVER_COLOR);

        //calculateProbabilities(dropRate, killCount, exactDrops);

        textArea = new JTextArea(outputMsg);
        textArea.setEditable(false);
        textArea.setFont(FontManager.getRunescapeBoldFont());
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        add(textArea);

    }

    private double nCx(double n, double x) {

        if (x > n / 2) {
            x = n - x;
        }

        double ret = 1.0;
        for (int i = 1; i <= x; i++) {
            ret *= (n - x + i);
            ret /= i;
        }

        return ret;
         /*
         //alternative method of computing nCx
        if (x < 0 || x > n) {
            return 0;
        }
        if (x == 0 || x == n) {
            return 1;
        }
        x = Math.min(x, n-x);
        double c = 1.0;
        for (int i = 0; i <= x-1; i++) {
            c = c * (n - i)/(i + 1);
        }
        return c;
        */
    }

    private double binomialProb(double n, double x, double p) {
        return nCx(n, x) * Math.pow(p, x) * (Math.pow(1.0-p, n-x));
    }

    private void calculateProbabilities(double dropRate, double killCount, double exactDrops) {
        if (killCount < exactDrops) {
            outputMsg = "You've somehow cheated the RNG gods and managed to get more drops than you got kills. What is this sorcery?!";
        } else if (dropRate > 1.0 || dropRate < 0.0) {
            outputMsg = "Please use a drop rate value between 0.0 and 1.0.";
        } else {
            exactChance = binomialProb(killCount, exactDrops, dropRate);
            zeroChance = Math.pow(1.0-dropRate, killCount);
            if (exactDrops == 1.0) {
                atLeastChance = 1.0 - zeroChance;
            } else {
                atLeastChance = 0.0;
                for (int i = 0; i < exactDrops; i++) {
                    atLeastChance += binomialProb(killCount, i, dropRate);
                }
                atLeastChance = 1.0 - atLeastChance;
            }


            strAtLeastChance = df.format(atLeastChance*100.0);
            if (strAtLeastChance.equals("0") || strAtLeastChance.equals("100")) {
                strAtLeastChance = "~" + strAtLeastChance;
            }
            strExactChance = df.format(exactChance*100.0);
            if (strExactChance.equals("0") || strExactChance.equals("100")) {
                strExactChance = "~" + strExactChance;
            }
            strZeroChance = df.format(zeroChance*100.0);
            if (strZeroChance.equals("0") || strZeroChance.equals("100")) {
                strZeroChance = "~" + strZeroChance;
            }
            outputMsg = "At " + (int)killCount + " kills, " + (int)exactDrops + " drop(s), and a drop rate of " + dropRate + ", your chances are:\n\n" +
                    "Chance to get at least " + (int)exactDrops + " drop(s):\n" + strAtLeastChance + "%\n\n" +
                    "Chance to get exactly " + (int)exactDrops + " drop(s):\n" + strExactChance + "%\n\n" +
                    "Chance to get zero drops:\n" + strZeroChance + "%";
        }
    }
}
