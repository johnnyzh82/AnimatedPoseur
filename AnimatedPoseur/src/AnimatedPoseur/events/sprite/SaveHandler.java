package AnimatedPoseur.events.sprite;

import AnimatedPoseur.AnimatedPoseur;
import AnimatedPoseur.files.PoseurFileManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This handler responds to when the user wants to save the the 
 * Animated Poseur currently being edited.
 * 
 * @author  Yunlong Zhang
 * @version 1.0
 */
public class SaveHandler implements ActionListener
{
    /**
     * Called when the user requests to save as  via the
     * save button..
     * 
     * @param ae The Event Object.
     */
    @Override
    public void actionPerformed(ActionEvent ae)
    {
        // FORWARD THE REQUEST TO THE FILE MANAGER
        AnimatedPoseur singleton = AnimatedPoseur.getPoseur();
        PoseurFileManager poseurFileManager = singleton.getPoseurFileManager();
        poseurFileManager.requestSavePoseur();
    }
}