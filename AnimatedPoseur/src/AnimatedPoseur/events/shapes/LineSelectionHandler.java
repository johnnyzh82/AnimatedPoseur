package AnimatedPoseur.events.shapes;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import AnimatedPoseur.AnimatedPoseur;
import AnimatedPoseur.shapes.PoseurShapeType;
import AnimatedPoseur.state.PoseurStateManager;

/**
 * This handler responds to when the user requests to
 * start drawing a line.
 * 
 * @author  Richard McKenna 
 *          Yunlong Zhang
 *         CSE 219 Stony Brook
 * @version 1.0
 */
public class LineSelectionHandler implements ActionListener
{
    /**
     * When the user requests to draw a line, we'll need
     * to notify the data manager, since it managers the 
     * shape in progress. It will update the gui as needed
     * as well.
     * 
     * @param ae The Event Object.
     */
    @Override
    public void actionPerformed(ActionEvent ae)
    {
        // RELAY THE REQUEST TO THE DATA MANAGER
        AnimatedPoseur singleton = AnimatedPoseur.getPoseur();
        PoseurStateManager poseurStateManager = singleton.getPoseStateManager();
        poseurStateManager.selectShapeToDraw(PoseurShapeType.LINE);
    }
}