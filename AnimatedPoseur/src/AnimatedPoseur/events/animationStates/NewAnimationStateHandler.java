
package AnimatedPoseur.events.animationStates;

import AnimatedPoseur.AnimatedPoseur;
import AnimatedPoseur.files.PoseurFileManager;
import AnimatedPoseur.gui.AnimatedPoseurGUI;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 * This handler responds to when the user wants to create an new animation state
 * in the list.
 * 
 * @author Yunlong Zhang
 *          CSE 219 Stony Brook
 * @version 1.0
 */
public class NewAnimationStateHandler implements ActionListener {
    /**
     * Called when the user request to create an animation state via the new 
     * animation state button.
     * 
     * @param e The Event Object.
     * 
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        AnimatedPoseur singleton = AnimatedPoseur.getPoseur();
        PoseurFileManager poseurFileManager = singleton.getPoseurFileManager();
        poseurFileManager.promptForNewAnimationState();
    }
}
