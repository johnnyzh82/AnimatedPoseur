package AnimatedPoseur.events.edit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import AnimatedPoseur.AnimatedPoseur;
import AnimatedPoseur.files.PoseFileManager;
import AnimatedPoseur.state.PoseurStateManager;

/**
 * This event handler responds to when the user wants to save current pose 
 * and export .png formatted image to images folder.
 * 
 * @author Yunlong Zhang
 *          CSE 219 Stony Brook
 * @version 1.0
 */
public class SaveAndExportImageHandler implements ActionListener
{
    /**
     * This method relays this event to button save and export button.
     * 
     * @param ae The event object for this button press.
     */
    @Override
    public void actionPerformed(ActionEvent ae) 
    {
        AnimatedPoseur singleton = AnimatedPoseur.getPoseur();
        PoseFileManager poseFileManager = singleton.getPoseFileManager();
        poseFileManager.requestSaveAndExportPose();
    }   
}
