
package AnimatedPoseur.events.pose;

import AnimatedPoseur.AnimatedPoseur;
import AnimatedPoseur.files.PoseFileManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This handler responds to when the user wants to delete current selected pose in list. A confirm message dialog will
 * be display after clicked delete button.
 * 
 * @author Yunlong Zhang
 *          CSE 219 Stony Brook
 * @version 1.0
 */
public class DeletePoseHandler implements ActionListener{
    /**
     * Called when the user requests to delete the pose via the delete button.
     * 
     * @param ae The event Object
     * 
     */
    @Override
    public void actionPerformed(ActionEvent ae)
    {
        AnimatedPoseur singleton = AnimatedPoseur.getPoseur();
        PoseFileManager poseFileManager = singleton.getPoseFileManager();
        poseFileManager.requestToDeletePose();
    }
}