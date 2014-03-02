package AnimatedPoseur.events.viewer;

import AnimatedPoseur.AnimatedPoseur;
import AnimatedPoseur.files.PoseurFileManager;
import AnimatedPoseur.gui.AnimatedPoseurGUI;
import AnimatedPoseur.renderer.SceneRenderer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The SlowDownAnimationHandler class responds to when the user
 * requests to slow down animation.
 *
 * @author  Yunlong Zhang
 *          CSE 219
 * @version 1.0
 */
public class SlowDownAnimationHandler implements ActionListener {

    /**
     * Here's the actual method called when the user clicks the
     * slow down animation method, which results in slow down of the
     * renderer, and thus the animator as well.
     *
     * @param ae Contains information about the event.
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
        //user should be able to slow down animation by 0.1f, this is
        //an arbitary number choosen
        AnimatedPoseur singleton = AnimatedPoseur.getPoseur();
        PoseurFileManager poseurFileManager = singleton.getPoseurFileManager();
        poseurFileManager.slowDownAnimationRendering();

    }
}
