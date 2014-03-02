
package AnimatedPoseur.events.pose;

import AnimatedPoseur.AnimatedPoseur;
import AnimatedPoseur.files.PoseFileManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * This handler responds to when the user wants to select the animation 
 * pose which are displayed in the list. If such a pose is selected, correspondingly
 * pose control will be enabled.
 * 
 * @author Yunlong Zhang
 *          CSE 219 Stony Brook
 * @version 1.0
 */
public class PoseSelectionHandler implements ListSelectionListener {
    /**
     * Called when the user request to select an pose via the pose list
     * 
     * @param e The Event Object.
     * 
     */
    @Override
    public void valueChanged(ListSelectionEvent e)
    {
        AnimatedPoseur singleton = AnimatedPoseur.getPoseur();
        PoseFileManager poseFileManager = singleton.getPoseFileManager();
        poseFileManager.selectPose();
    }
}