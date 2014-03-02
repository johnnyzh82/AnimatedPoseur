package AnimatedPoseur.events.zoom;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import AnimatedPoseur.AnimatedPoseur;
import AnimatedPoseur.gui.AnimatedPoseurGUI;
import AnimatedPoseur.state.PoseCanvasState;
import AnimatedPoseur.state.PoseurStateManager;

/**
 * This handler responds to when the user wishes to zoom in. Note
 * that we'll only be zooming in and out on the right canvas. The
 * left canvas will show the true size of the pose.
 * 
 * @author  Richard McKenna
 *           Yunlong Zhang
 *          CSE 219 Stony Brook
 * @version 1.0
 */
public class ZoomInHandler implements ActionListener
{
    /**
     * This method responds by updating the zoom level 
     * accordingly and repainting the view.
     * 
     * @param ae The Event Object.
     */
    @Override
    public void actionPerformed(ActionEvent ae)
    {
        // RELAY THE REQUEST TO THE STATE MANAGER
        AnimatedPoseur singleton = AnimatedPoseur.getPoseur();
        PoseurStateManager poseurStateManager = singleton.getPoseStateManager();
        PoseCanvasState poseCanvasState = poseurStateManager.getZoomableCanvasState();
        poseCanvasState.zoomIn();
        
        // AND MAKE SURE THE ZOOM LABEL REFLECTS THE CHANGE
        AnimatedPoseurGUI gui = singleton.getGUI();
        gui.updateZoomLabel();
    }
}