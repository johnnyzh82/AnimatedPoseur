package AnimatedPoseur.events.viewer;

import AnimatedPoseur.AnimatedPoseur;
import AnimatedPoseur.files.PoseurFileManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * The SpeedUpAnimationHandler class responds to when the user
 * requests to speed up animation.
 *
 * @author  Yunlong Zhang
 *          CSE 219
 * @version 1.0
 */
public class SpeedUpAnimationHandler implements ActionListener {

    /**
     * Here's the actual method called when the user clicks the
     * speed up animation method, which results in speeding up of the
     * renderer, and thus the animator as well.
     *
     * @param ae Contains information about the event.
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
        //user should be able to speed up animation by 0.1f, this is
        //an arbitary number choosen
        AnimatedPoseur singleton = AnimatedPoseur.getPoseur();
        PoseurFileManager poseurFileManager = singleton.getPoseurFileManager();
        poseurFileManager.speedUpAnimationRendering();

    }
}
