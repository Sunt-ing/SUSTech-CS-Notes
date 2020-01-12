package observer3;

/*
 * Copyright (c) 2001, 2005. Steven J. Metsker.
 * 
 * Steve Metsker makes no representations or warranties about
 * the fitness of this software for any particular purpose, 
 * including the implied warranty of merchantability.
 *
 * Please use this software as you wish with the sole
 * restriction that you may not claim that you wrote it.
 */

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * This utility class provides an interface that makes the Swing subsystem easy
 * to use.
 * 
 * @author Steven J. Metsker
 */
public class SwingFacade {
    /**
     * @param title
     *            the words to show in the title border tab
     * @return a (beveled) titled border with the given title
     */
    public static TitledBorder createTitledBorder(String title) {
        TitledBorder tb = BorderFactory.createTitledBorder(BorderFactory
                .createBevelBorder(BevelBorder.RAISED), title,
                TitledBorder.LEFT, TitledBorder.TOP);
        tb.setTitleColor(Color.black);
        tb.setTitleFont(getStandardFont());
        return tb;
    }

    /**
     * @param title
     *            the words to show in the title border tab
     * @param in
     *            the panel that the border goes around
     * @return a new panel that wraps a titled border around the given panel
     */
    public static JPanel createTitledPanel(String title, JPanel in) {
        JPanel out = new JPanel();
        out.add(in);
        out.setBorder(createTitledBorder(title));
        return out;
    }

    /**
     * @return a standard font for GUI use
     */
    public static Font getStandardFont() {
        return new Font("Dialog", Font.PLAIN, 18);
    }

    /**
     * Display the given component within a frame.
     * 
     * @param component
     *            the component to display
     * @param title
     *            the window title for the frame
     * @return the frame
     */
    public static JFrame launch(Component c, String title) {
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
    
        frame.getContentPane().add(c);

        frame.pack();
        frame.setVisible(true);
        return frame;
    }

}