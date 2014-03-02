
package AnimatedPoseur.events.viewer;


import AnimatedPoseur.AnimatedPoseur;
import AnimatedPoseur.files.PoseurFileManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The StopAnimationHandler class responds to when the user
 * requests to stop animation.
 *
 * @author  Yunlong Zhang
 *          CSE 219 Stony Brook
 * @version 1.0
 */
public class StopAnimationHandler implements ActionListener {
    /**
     * This is the method actually called when users,clicks the pause
     * button method, which results stop the renderer and animator.
     *
     * @param ae Contain information about the event
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
        AnimatedPoseur singleton = AnimatedPoseur.getPoseur();
        PoseurFileManager poseurFileManager = singleton.getPoseurFileManager();
        poseurFileManager.stopAnimationRendering();
    }
}
