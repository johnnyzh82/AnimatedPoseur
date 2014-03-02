
package AnimatedPoseur.events.sprite;

import AnimatedPoseur.AnimatedPoseur;
import AnimatedPoseur.files.PoseurFileManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This handler responds to when the user wants to exit the application.
 * We will have to be careful not to accidentally lose any of the user's work.
 * 
 * @author Yunlong Zhang
 *          CSE 219 Stony Brook
 * @version 1.0
 */
public class ExitHandler implements ActionListener{
    /**
     * Called when the user requests to exit via the exit button, this will make
     * sure no work is lost and then close the application
     * 
     * @param ae The event Object
     * 
     */
    @Override
    public void actionPerformed(ActionEvent ae)
    {
        // FORWARD THE REQUEST TO THE FILE MANAGER
        AnimatedPoseur singleton = AnimatedPoseur.getPoseur();
        PoseurFileManager poseurFileManager = singleton.getPoseurFileManager();
        poseurFileManager.requestExit();
    }
}
