
package AnimatedPoseur.events.pose;

import AnimatedPoseur.AnimatedPoseur;
import AnimatedPoseur.files.PoseFileManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This handler responds to when the user wants to edit current selected pose in list. The pose editor with and 
 * shapes, collor pallet, zoomable canvas and cut, copy paste will be available to use after.
 * initialize to 
 * 
 * @author Yunlong Zhang
 *          CSE 219 Stony Brook
 * @version 1.0
 */
public class EditPoseHandler implements ActionListener{
    /**
     * Called when the user requests to edit a pose via the edit button.
     * 
     * @param ae The event Object
     * 
     */
    @Override
    public void actionPerformed(ActionEvent ae)
    {
        AnimatedPoseur singleton = AnimatedPoseur.getPoseur();
        PoseFileManager poseFileManager = singleton.getPoseFileManager();
        poseFileManager.requestEditPose();
    }
}