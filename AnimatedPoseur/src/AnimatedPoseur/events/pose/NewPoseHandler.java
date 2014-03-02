
package AnimatedPoseur.events.pose;

import AnimatedPoseur.AnimatedPoseur;
import AnimatedPoseur.files.PoseFileManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This handler responds to when the user wants to create a blank pose. User will give this pose a new name when
 * create it.
 * 
 * @author Yunlong Zhang
 *          CSE 219 Stony Brook
 * @version 1.0
 */
public class NewPoseHandler implements ActionListener{
    /**
     * Called when the user requests to create a new pose  via the create pose button.
     * 
     * @param ae The event Object
     * 
     */
    @Override
    public void actionPerformed(ActionEvent ae)
    {
        AnimatedPoseur singleton = AnimatedPoseur.getPoseur();
        PoseFileManager poseFileManager = singleton.getPoseFileManager();
        poseFileManager.requestNewPose();
    }
}