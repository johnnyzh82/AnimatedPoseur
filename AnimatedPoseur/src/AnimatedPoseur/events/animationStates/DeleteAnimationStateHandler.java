
package AnimatedPoseur.events.animationStates;

import AnimatedPoseur.AnimatedPoseur;
import AnimatedPoseur.files.PoseurFileManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 * This handler responds to when the user wants to delete the animation state
 * in the list.
 * 
 * @author Yunlong Zhang
 *          CSE 219 Stony Brook
 * @version 1.0
 */
public class DeleteAnimationStateHandler implements ActionListener {
    /**
     * Called when the user request to delete an animation state via the delete 
     * button.
     * 
     * @param e The Event Object.
     * 
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        AnimatedPoseur singleton = AnimatedPoseur.getPoseur();
        PoseurFileManager poseurFileManager = singleton.getPoseurFileManager();
        poseurFileManager.promptToDeleteFile();
    }
}

