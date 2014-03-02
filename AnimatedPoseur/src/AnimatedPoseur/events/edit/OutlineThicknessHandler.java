package AnimatedPoseur.events.edit;


import java.awt.BasicStroke;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import AnimatedPoseur.AnimatedPoseur;
import AnimatedPoseur.gui.AnimatedPoseurGUI;
import AnimatedPoseur.state.PoseurStateManager;

/**
 * This event handler responds to when the user wants to change the outline thickness of the current selected shape onto 
 * renderer surface
 * @author Yunlong Zhang
 *          CSE 219 Stony Brook
 * @version 1.0
 */
public class OutlineThicknessHandler implements ItemListener
{
    /**
     * This method relays this event to the data manager, which
     * will set new current line thickness
     * 
     * @param ie The event object for any item clicked in combo box
     */
    @Override
    public void itemStateChanged(ItemEvent ie) 
    {
        // ACCESS ALL NECESSARY DATA
        AnimatedPoseur singleton = AnimatedPoseur.getPoseur();
        AnimatedPoseurGUI gui = singleton.getGUI();
        PoseurStateManager poseurStateManager = singleton.getPoseStateManager();
        // THEN CHECK IF ANY SELECTION HAPPENED
        if(ie.getStateChange() == ItemEvent.SELECTED)
        {
            // GET THE SELECT THICKNESS IN COMBO BOX
            int i = gui.getLineThickness();
            // CREATE A NEW STRUCTURE OF BASICSTROKE CLASS
            BasicStroke bs = new BasicStroke(i);
            // SET IT INTO CURRENT SELECTED SHAPE
            poseurStateManager.setSelectedShapeOutlineThickness(bs);
        }
    }   
}

