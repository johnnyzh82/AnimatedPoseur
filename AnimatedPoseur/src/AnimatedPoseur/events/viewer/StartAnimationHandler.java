package AnimatedPoseur.events.viewer;

import AnimatedPoseur.AnimatedPoseur;
import AnimatedPoseur.files.PoseurFileManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The StartAnimationHandler class responds to when the user
 * requests to start animation.
 *
 * @author  Richard McKenna
 *          Yunlong Zhang
 *          CSE 219 Stony Brook
 * @version 1.0
 */
public class StartAnimationHandler implements ActionListener {
    // THIS IS REALLY THE ONLY ONE WHO CAN PAUSE OR UNPAUSE ANIMATION

    /**
     * Here's the actual method called when the user clicks the
     * start animation method, which results in unpausing of the
     * renderer, and thus the animator as well.
     *
     * @param ae Contains information about the event.
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
        AnimatedPoseur singleton = AnimatedPoseur.getPoseur();
        PoseurFileManager poseurFileManager = singleton.getPoseurFileManager();
        poseurFileManager.startAnimationRendering();
    }
}
