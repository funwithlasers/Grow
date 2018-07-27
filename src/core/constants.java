/**
 * @author Petr (http://www.sallyx.org/)
 */
package core;

import javax.swing.*;
import java.awt.*;

public class constants {
    //change these values at your peril!
    static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();


    static double width = screenSize.getWidth();
    static double height = screenSize.getHeight();

    final public static int constWindowWidth  = (int) width;
    final public static int constWindowHeight = (int) height;
    //final public static int constWindowWidth  = 500;
    //final public static int constWindowHeight = 500;
}
