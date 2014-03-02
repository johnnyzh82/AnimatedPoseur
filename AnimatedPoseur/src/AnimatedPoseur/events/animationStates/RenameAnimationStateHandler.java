
package AnimatedPoseur.events.animationStates;

import AnimatedPoseur.AnimatedPoseur;
import AnimatedPoseur.files.PoseurFileManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 * This handler responds to when the user wants to rename the animation state
 * in the list.Note that a new dialog will be displayed to notify input of new 
 * name.
 * 
 * @author Yunlong Zhang
 *          CSE 219 Stony Brook
 * @version 1.0
 */
public class RenameAnimationStateHandler implements ActionListener {
    /**
     * Called when the user request to rename an animation state via the rename
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
        poseurFileManager.promptForRenameAnimationState();
    }
}
