package AnimatedPoseur.events.edit;

import AnimatedPoseur.AnimatedPoseur;
import AnimatedPoseur.files.PoseFileManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This event handler responds to when the user want to exit pose 
 * editor at anytime, a message dialog will notice user if they 
 * want to save their work or not.
 * 
 * @author Yunlong Zhang
 *          CSE 219 Stony Brook
 * @version 1.0
 */
public class ExitPoseEditorHandler implements ActionListener
{
    /**
     * This method relays this event to the data manager, which
     * will update its state and the gui.
     * 
     * @param ae The event object for this button press.
     */
    @Override
    public void actionPerformed(ActionEvent ae) 
    {
        AnimatedPoseur singleton = AnimatedPoseur.getPoseur();
        PoseFileManager poseFileManager = singleton.getPoseFileManager();
        poseFileManager.requestExit();
    }   
}