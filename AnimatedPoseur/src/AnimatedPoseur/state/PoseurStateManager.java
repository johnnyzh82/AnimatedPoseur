package AnimatedPoseur.state;

import AnimatedPoseur.AnimatedPoseur;
import static AnimatedPoseur.AnimatedPoseurSetting.*;
import AnimatedPoseur.files.PoseFileManager;
import AnimatedPoseur.gui.AnimatedPoseurGUI;
import AnimatedPoseur.gui.PoseCanvas;
import AnimatedPoseur.shapes.PoseurEllipse;
import AnimatedPoseur.shapes.PoseurLine;
import AnimatedPoseur.shapes.PoseurRectangle;
import AnimatedPoseur.shapes.PoseurShape;
import AnimatedPoseur.shapes.PoseurShapeType;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.geom.Rectangle2D;
/**
 * This class stores all the state information about the application
 * regarding the poses and rendering settings. Note that whenever
 * the data in this class changes, the visual representations
 * will also change.
 * 
 * @author  Richard McKenna
 *          Debugging Enterprises
 * @version 1.0
 */
 public class PoseurStateManager 
{
    // THIS KEEPS TRACK OF WHAT MODE OUR APPLICATION
    // IS IN. IN DIFFERENT MODES IT BEHAVES DIFFERENTLY
    // AND DIFFERENT CONTROLS ARE ENABLED OR DISABLED
    private AnimatedPoseurState state;
    
    // THIS IS THE POSE WE ARE ACTUALLY EDITING
    private AnimatedPoseurPose pose;
    // THIS IS THE POSEUR WE ARE ACTUALLY OPENING
//    private Image image;
//    private AnimatedPoseurSprite poseur;
    
    // THESE STORE THE STATE INFORMATION
    // FOR THE TWO CANVASES
    private PoseCanvasState trueCanvasState;
    private PoseCanvasState zoomableCanvasState;
       
    // THIS WILL SERVE AS OUR CLIPBOARD. NOTE THAT WE DO NOT
    // HAVE AN UNDO FEATURE, SO OUR CLIPBOARD CAN ONLY STORE
    // A SINGLE SHAPE
    private PoseurShape clipboard;
    
    // THIS WILL SERVE AS OUR CURRENTLY SELECTED SHAPE,
    // WHICH CAN BE MOVED AND CHANGED
    private PoseurShape selectedShape;

    // THIS IS A SHAPE THAT IS IN THE PROGRESS OF BEING
    // CREATED BY THE USER
    private PoseurShape shapeInProgress;
    private PoseurShapeType shapeInProgressType;

    // WE'LL USE THESE VALUES TO CALCULATE HOW FAR THE
    // MOUSE HAS BEEN DRAGGED BETWEEN CALLS TO
    // OUR mouseDragged METHOD CALLS
    private int lastMouseDraggedX;
    private int lastMouseDraggedY;
    
    /**
     * This constructor sets up all the state information regarding
     * the shapes that are to be rendered for the pose. Note that 
     * at this point the user has not done anything, all our
     * pose data structures are essentially empty.
     */
    public PoseurStateManager()
    {
        // WE ALWAYS START IN SELECT_SHAPE_MODE
        state = AnimatedPoseurState.STARTUP_STATE;
        
//        poseur = new AnimatedPoseurSprite(DEFAULT_POSEUR_WIDTH,DEFAULT_POSEUR_HEIGHT);
        // CONSTRUCT THE POSE, WHICH WILL BE USED
        // TO RENDER OUR STUFF
        pose = new AnimatedPoseurPose(DEFAULT_POSE_WIDTH, DEFAULT_POSE_HEIGHT);
//        image = null;;
        // NOW INITIALIZE THE CANVAS STATES. NOTE, DON'T CONFUSE
        // THE WORD "true" WITH THE PARAMETER "true". THEY HAVE
        // TWO SEPARATE MEANINGS
        trueCanvasState = new PoseCanvasState(
                false, pose, INIT_ZOOM_LEVEL, INIT_ZOOM_LEVEL, INIT_ZOOM_LEVEL);
        zoomableCanvasState = new PoseCanvasState(
                true, pose, INIT_ZOOM_LEVEL, ZOOM_FACTOR, MIN_ZOOM_LEVEL);

        // NOTHING IS ON THE CLIPBOARD TO START
        clipboard = null;
        
        // THERE IS NO SELECTED SHAPE YET
        selectedShape = null;
    }
 
    // ACCESSOR METHODS
    
    /**
     * Gets the current mode of the application.
     * 
     * @return The mode currently being used by the application. 
     */
    public AnimatedPoseurState getMode() { return state; }
;
    /**
     * Gets the canvas state for the canvas on the left,
     * which does not zoom.
     * 
     * @return The canvas state for the left canvas.
     */
    public PoseCanvasState getTrueCanvasState() { return trueCanvasState; }

    /**
     * Gets the canvas state for the canvas on the right,
     * which can be zoomed in and out.
     * 
     * @return The canvas state for the right canvas.
     */
    public PoseCanvasState getZoomableCanvasState() { return zoomableCanvasState; }
        
    /**
     * Accessor method for getting the shape currently
     * being created.
     * 
     * @return The shape being created by the user.
     */
    public PoseurShape getShapeInProgress()
    {
        return shapeInProgress;
    }    
    
    /**
     * Accessor method for getting the pose object that
     * is being edited.
     * 
     * @return The Pose object currently being edited by the app.
     */
    public AnimatedPoseurPose getPose() { return pose; }
    

    /**
     * Accessor method for getting the shape the user has
     * selected. Note that if no shape is currently selected,
     * null is returned.
     * 
     * @return The shape the use has selected, null if nothing
     * is currently selected.
     */

    public PoseurShape getSelectedShape() { return selectedShape; }
     
    /**
     * Accessor method that tests to see if a shape is currently
     * being created by the user. If so, true is returned, else
     * false.
     * 
     * @return true if the user is in the process of creating a
     * shape, false otherwise.0
     */
    public boolean isShapeInProgress()
    {
        return shapeInProgress != null;
    }

    /**
     * Accessor method to test and see if a shape is currently
     * on the clipboard.
     * 
     * @return true if a shape is currently on the clipboard,
     * false otherwise.
     */
    public boolean isShapeOnClipboard()
    {
        return clipboard != null;
    }

    /**
     * Accessor method to test and see if a shape is currently
     * selected.
     * 
     * @return true if a shape is currently selected, false otherwise.
     */
    public boolean isShapeSelected()
    {
        return selectedShape != null;
    }
    
    /**
     * Accessor method that tests if the testSelectedShape
     * argument is the currently selected shape. If it is,
     * true is returned, else false.
     * 
     * @param testSelectedShape The shape to test to see if it's
     * the same object as the currently selected shape.
     * 
     * @return true if testSelectedShape is the currently 
     * selected shape. false otherwise.
     */
    public boolean isSelectedShape(PoseurShape testSelectedShape)
    {
        return testSelectedShape == selectedShape;
    }
    
    // MUTATOR METHODS
    
    /**
     * This mutator method changes the mode of the application,
     * which may result in a cursor change and the enabling and
     * disabling of various controls.
     * 
     * @param newMode The mode to set as the current mode.
     */
    public void setState(AnimatedPoseurState newMode)
    {
        // KEEP THE MODE
        state = newMode;
        
        // AND UPDATE THE GUI
        AnimatedPoseur singleton = AnimatedPoseur.getPoseur();
        AnimatedPoseurGUI gui = singleton.getGUI();
        gui.updateMode();
    }
    
    // METHODS MADE AVAILBLE TO OTHER CLASSES

    /**
     * This method is called when the user selects a shape type
     * to render. 
     * 
     * @param shapeType The shape type the user wishes to render.
     */
    public void selectShapeToDraw(PoseurShapeType shapeType)
    {
        // CLEAR THE SELECTED SHAPE
        selectedShape = null;
        
        if(state == AnimatedPoseurState.NEW_POSE_COMPLETE_SHAPE_STATE ||
                state == AnimatedPoseurState.NEW_POSE_DRAG_SHAPE_STATE||
                state == AnimatedPoseurState.NEW_POSE_SELECT_SHAPE_STATE||
                state == AnimatedPoseurState.NEW_POSE_SHAPE_SELECTED_STATE)
        // CHANGE THE MODE
        {
            setState(AnimatedPoseurState.NEW_POSE_CREATE_SHAPE_STATE);
        }
        else if (state == AnimatedPoseurState.POSE_EDITOR_COMPLETE_SHAPE_STATE ||
                state == AnimatedPoseurState.POSE_EDITOR_DRAG_SHAPE_STATE||
                state == AnimatedPoseurState.POSE_EDITOR_SELECT_SHAPE_STATE||
                state == AnimatedPoseurState.POSE_EDITOR_SHAPE_SELECTED_STATE)
        {
            setState(AnimatedPoseurState.POSE_EDITOR_CREATE_SHAPE_STATE);
        }
        
        // THIS WILL UNDO ANY SHAPE IN PROGRESS ALREADY
        shapeInProgress = null;    
        shapeInProgressType = shapeType;
        
        // REPAINT THE CANVASES
        repaintCanvases();
    }
    
    /**
     * This method manages the response to mouse button dragging
     * on the right canvas. 
     * 
     * @param x X-coordinate of the mouse drag.
     * 
     * @param y Y-coordinate of the mouse drag.
     */
    public void processMouseDragged(int x, int y)
    {
        if(state == AnimatedPoseurState.STARTUP_STATE||
                state == AnimatedPoseurState.POSEUR_POSE_SELECTED_STATE||
                state == AnimatedPoseurState.SELECT_ANIMATION_STATE_STATE||
                state == AnimatedPoseurState.SELECT_POSEUR_POSE_STATE)
        {
            return;
        }
        // WE MAY HAVE TO USE VALUES IN POSE SPACE
        Rectangle2D.Double poseArea = zoomableCanvasState.getPoseArea();
        int incX = x - lastMouseDraggedX;
        int incY = y - lastMouseDraggedY;
        lastMouseDraggedX = x;
        lastMouseDraggedY = y;

        // IF WE ARE IN SHAPE_DRAG_MODE, THEN WHEREVER
        // THE MOUSE GOES IN THE POSE AREA THE SHAPE
        // MUST FOLLOW. NOTE THAT ONCE THE MOUSE
        // LEAVES THE POSE AREA WE NEED TO SWITCH
        
        // TO SHAPE_SELECTED_MODE
        
        if (state == AnimatedPoseurState.NEW_POSE_DRAG_SHAPE_STATE)
        {
            // MAKE SURE WE'RE NOT WAY OFF THE POSE AREA
            if (poseArea.contains(x, y))
            {
                // DRAG THE SHAPE
                float zoomLevel = zoomableCanvasState.getZoomLevel();
                incX /= zoomLevel;
                incY /= zoomLevel;
                Rectangle2D.Double truePoseArea = trueCanvasState.getPoseArea();
                selectedShape.moveShape(incX, incY, truePoseArea); 
                repaintCanvases();
            }
            // WE WENT WAY OFF THE EDGE, LET'S STOP DRAGGING
            else
            {
                setState(AnimatedPoseurState.NEW_POSE_SHAPE_SELECTED_STATE);
            }
        }
        // THIS IS AFTER WE'VE STARTED RENDERING A SHAPE
        // AND ARE DRAGGING THE MOUSE TO COMPLETE IT
        else if (state == AnimatedPoseurState.NEW_POSE_COMPLETE_SHAPE_STATE)
        {
            // WE HAVE TO USE POSE SPACE COORDINATES
            float zoomLevel = zoomableCanvasState.getZoomLevel();
            int poseSpaceX = (int)((x - poseArea.getX()) / zoomLevel);
            int poseSpaceY = (int)((y - poseArea.getY()) / zoomLevel);
            
            // IF WE'RE NOT IN THE POSE AREA, WE'RE NOT MAKING A SHAPE
            if (    (poseSpaceX < 0) ||
                    (poseSpaceY < 0) ||
                    (poseSpaceX >= poseArea.getWidth()) ||
                    (poseSpaceY >= poseArea.getHeight()))
            {
                // IF WE GO OUTSIDE THE POSE AREA START OVER
                setState(AnimatedPoseurState.NEW_POSE_CREATE_SHAPE_STATE);
                shapeInProgress = null;
                return;
            }
            // IF WE GET HERE THE SHAPE IS BEING DRAWN
            shapeInProgress.updateShapeInProgress(poseSpaceX, poseSpaceY);
            // REPAINT OF COURSE
            repaintCanvases();           
        }
        
        
        
        else if ( state == AnimatedPoseurState.POSE_EDITOR_DRAG_SHAPE_STATE  )
        {
            // MAKE SURE WE'RE NOT WAY OFF THE POSE AREA
            if (poseArea.contains(x, y))
            {
                // DRAG THE SHAPE
                float zoomLevel = zoomableCanvasState.getZoomLevel();
                incX /= zoomLevel;
                incY /= zoomLevel;
                Rectangle2D.Double truePoseArea = trueCanvasState.getPoseArea();
//                if(selectedShape == null)
//                {
//                    return;
//                }
                selectedShape.moveShape(incX, incY, truePoseArea); 
                repaintCanvases();
            }
            // WE WENT WAY OFF THE EDGE, LET'S STOP DRAGGING
            else
            {
                setState(AnimatedPoseurState.POSE_EDITOR_SHAPE_SELECTED_STATE);
            }
        }
        // SAME THING WITH POSE EDITOR
        else if (state == AnimatedPoseurState.POSE_EDITOR_COMPLETE_SHAPE_STATE)
        {
            // WE HAVE TO USE POSE SPACE COORDINATES
            float zoomLevel = zoomableCanvasState.getZoomLevel();
            int poseSpaceX = (int)((x - poseArea.getX()) / zoomLevel);
            int poseSpaceY = (int)((y - poseArea.getY()) / zoomLevel);
            
            // IF WE'RE NOT IN THE POSE AREA, WE'RE NOT MAKING A SHAPE
            if (    (poseSpaceX < 0) ||
                    (poseSpaceY < 0) ||
                    (poseSpaceX >= poseArea.getWidth()) ||
                    (poseSpaceY >= poseArea.getHeight()))
            {
                // IF WE GO OUTSIDE THE POSE AREA START OVER
                setState(AnimatedPoseurState.POSE_EDITOR_CREATE_SHAPE_STATE);
                shapeInProgress = null;
                return;
            }

            // IF WE GET HERE THE SHAPE IS BEING DRAWN
            shapeInProgress.updateShapeInProgress(poseSpaceX, poseSpaceY);
            // REPAINT OF COURSE
            repaintCanvases();           
        }
    }   
    
    /**
     * This method manages the response to a mouse button press
     * on the right canvas. 
     * 
     * @param x X-coordinate of the mouse press.
     * 
     * @param y Y-coordinate of the mouse press.
     */    
    public void processMousePress(int x, int y)
    {
        if(state == AnimatedPoseurState.STARTUP_STATE||
                state == AnimatedPoseurState.SELECT_POSEUR_POSE_STATE||
                state == AnimatedPoseurState.SELECT_ANIMATION_STATE_STATE||
                state == AnimatedPoseurState.POSEUR_POSE_SELECTED_STATE)
        {
            return;
        }
        // WE MAY HAVE TO USE VALUES IN POSE SPACE
        Rectangle2D poseArea = zoomableCanvasState.getPoseArea();
        float zoomLevel = zoomableCanvasState.getZoomLevel();
        int poseSpaceX = (int)((x - poseArea.getX()) / zoomLevel);
        int poseSpaceY = (int)((y - poseArea.getY()) / zoomLevel);
        lastMouseDraggedX = x;
        lastMouseDraggedY = y;
        
        // IF WE'RE NOT IN THE POSE AREA WE WON'T DO ANYTHING
        if (    (poseSpaceX < 0) ||
                (poseSpaceY < 0) ||
                (poseSpaceX > poseArea.getWidth()) ||
                (poseSpaceY > poseArea.getHeight()))
        {
            return;
        }
                        
        // IF WE ARE IN SELECT_SHAPE_MODE, FIND THE FIRST SHAPE,
        // TOP TO BOTTOM, THAT CONTAINS THE POINT.
            // IF NO SHAPE IS FOUND, DO NOTHING
            // IF A SHAPE IS FOUND, SWITCH TO DRAG_SHAPE_MODE
            //   AND MAKE THE FOUND SHAPE THE SELECTED SHAPE
        if ((state == AnimatedPoseurState.NEW_POSE_SELECT_SHAPE_STATE) ||
            (state == AnimatedPoseurState.NEW_POSE_SHAPE_SELECTED_STATE))
        {
            // ASK THE Pose TO FIND A SHAPE
            PoseurShape foundShape = pose.findShapeWithPoint(poseSpaceX, poseSpaceY);
            if (foundShape != null)
            {
                selectedShape = foundShape;
                setState(AnimatedPoseurState.NEW_POSE_DRAG_SHAPE_STATE);
            }
            // IF WE DON'T FIND ONE, RESET IT SO THERE
            // IS NO SELECTED SHAPE
            else
            {
                selectedShape = null;
                setState(AnimatedPoseurState.NEW_POSE_SELECT_SHAPE_STATE);
            }
        }
        
        else if (state == AnimatedPoseurState.NEW_POSE_CREATE_SHAPE_STATE)
        {
            // CREATE  A RECTANGLE
            if (shapeInProgressType == PoseurShapeType.RECTANGLE)
            {
                shapeInProgress = PoseurRectangle.factoryBuildRectangle(poseSpaceX, poseSpaceY);
            }
            // CREATE AN ELLIPSE
            else  if (shapeInProgressType == PoseurShapeType.ELLIPSE)
            {
                shapeInProgress = PoseurEllipse.factoryBuildEllipse(poseSpaceX, poseSpaceY);
            }
            // CREATE A LINE
            else if(shapeInProgressType == PoseurShapeType.LINE)
            {
                 shapeInProgress = PoseurLine.factoryBuildLine(poseSpaceX, poseSpaceY);
            }
            // WE NEED TO SWITCH MODES
            setState(AnimatedPoseurState.NEW_POSE_COMPLETE_SHAPE_STATE);
        }
        
        // SAME THING HAPPENS IN POSE EDITOR
        else if ((state == AnimatedPoseurState.POSE_EDITOR_SELECT_SHAPE_STATE) ||
            state == AnimatedPoseurState.POSE_EDITOR_SHAPE_SELECTED_STATE)
        {
            // ASK THE Pose TO FIND A SHAPE
            PoseurShape foundShape = pose.findShapeWithPoint(poseSpaceX, poseSpaceY);
            if (foundShape != null)
            {
                selectedShape = foundShape;
                setState(AnimatedPoseurState.POSE_EDITOR_DRAG_SHAPE_STATE);
            }
            // IF WE DON'T FIND ONE, RESET IT SO THERE
            // IS NO SELECTED SHAPE
            else
            {
                selectedShape = null;
                setState(AnimatedPoseurState.POSE_EDITOR_SELECT_SHAPE_STATE);
            }
        }
        else if (state == AnimatedPoseurState.POSE_EDITOR_CREATE_SHAPE_STATE ||
                state == AnimatedPoseurState.POSE_EDITOR_SHAPE_SELECTED_STATE)
        {
            // CREATE  A RECTANGLE
            if (shapeInProgressType == PoseurShapeType.RECTANGLE)
            {
                shapeInProgress = PoseurRectangle.factoryBuildRectangle(poseSpaceX, poseSpaceY);
            }
            // CREATE AN ELLIPSE
            else  if (shapeInProgressType == PoseurShapeType.ELLIPSE)
            {
                shapeInProgress = PoseurEllipse.factoryBuildEllipse(poseSpaceX, poseSpaceY);
            }
            // CREATE A LINE
            else if(shapeInProgressType == PoseurShapeType.LINE)
            {
                 shapeInProgress = PoseurLine.factoryBuildLine(poseSpaceX, poseSpaceY);
            }
            // WE NEED TO SWITCH MODES
            setState(AnimatedPoseurState.POSE_EDITOR_COMPLETE_SHAPE_STATE);
        }
    }

    /**
     * This method responds to a mouse button released on 
     * the right canvas.
     * 
     * @param x X-coordinate of where the mouse button release happened.
     * 
     * @param y Y-coordinate of where the mouse button release happened.
     */
    public void processMouseReleased(int x, int y)
    {
        // WE HAVE TO USE POSE SPACE COORDINATES
            if(state == AnimatedPoseurState.NEW_POSE_DRAG_SHAPE_STATE)
            {
                setState(AnimatedPoseurState.NEW_POSE_SHAPE_SELECTED_STATE);
            }
            else if(state == AnimatedPoseurState.NEW_POSE_COMPLETE_SHAPE_STATE)
            {
                    Rectangle2D poseArea = zoomableCanvasState.getPoseArea();
                    float zoomLevel = zoomableCanvasState.getZoomLevel();
                    int poseSpaceX = (int)((x - poseArea.getX()) / zoomLevel);
                    int poseSpaceY = (int)((y - poseArea.getY()) / zoomLevel);
                    // WE COMPLETE THE SHAPE BY DRAGGING THE MOUSE
                    lastMouseDraggedX = x;
                    lastMouseDraggedY = y;
                    // IF WE'RE NOT IN THE POSE AREA, WE'RE NOT MAKING A SHAPE
                    if ((poseSpaceX < 0) ||
                            (poseSpaceY < 0) ||
                            (poseSpaceX > poseArea.getWidth()) ||
                            (poseSpaceY > poseArea.getHeight()))
                    {
                        setState(AnimatedPoseurState.NEW_POSE_CREATE_SHAPE_STATE);
                        shapeInProgress = null;
                        return;
                    } 

                    // ASK THE SHAPE TO UPDATE ITSELF BASE ON WHERE ON
                    // THE POSE THE MOUSE BUTTON WAS RELEASED
                    if (!shapeInProgress.completesValidShape(poseSpaceX, poseSpaceY))
                    {
                        shapeInProgress = null;
                        setState(AnimatedPoseurState.NEW_POSE_CREATE_SHAPE_STATE);
                    }
                    else
                    {
                        // OUR LITTLE SHAPE HAS GROWN UP
                        pose.addShape(shapeInProgress);
                        selectedShape = shapeInProgress;
                        shapeInProgress = null;
                         // WE CAN DRAW ANOTHER ONE NOW
                        setState(AnimatedPoseurState.NEW_POSE_CREATE_SHAPE_STATE);
                        // REPAINT OF COURSE
                        repaintCanvases();
                    }
               }
            
                
            else if(state == AnimatedPoseurState.POSE_EDITOR_DRAG_SHAPE_STATE)
            {
                setState(AnimatedPoseurState.POSE_EDITOR_SHAPE_SELECTED_STATE);
            }
            else if(state == AnimatedPoseurState.POSE_EDITOR_COMPLETE_SHAPE_STATE)
            {
                    Rectangle2D poseArea = zoomableCanvasState.getPoseArea();
                    float zoomLevel = zoomableCanvasState.getZoomLevel();
                    int poseSpaceX = (int)((x - poseArea.getX()) / zoomLevel);
                    int poseSpaceY = (int)((y - poseArea.getY()) / zoomLevel);
                    // WE COMPLETE THE SHAPE BY DRAGGING THE MOUSE
                    lastMouseDraggedX = x;
                    lastMouseDraggedY = y;
                    // IF WE'RE NOT IN THE POSE AREA, WE'RE NOT MAKING A SHAPE
                    if ((poseSpaceX < 0) ||
                            (poseSpaceY < 0) ||
                            (poseSpaceX > poseArea.getWidth()) ||
                            (poseSpaceY > poseArea.getHeight()))
                    {
                        setState(AnimatedPoseurState.POSE_EDITOR_CREATE_SHAPE_STATE);
                        shapeInProgress = null;
                        return;
                    } 

                    // ASK THE SHAPE TO UPDATE ITSELF BASE ON WHERE ON
                    // THE POSE THE MOUSE BUTTON WAS RELEASED
                    if (!shapeInProgress.completesValidShape(poseSpaceX, poseSpaceY))
                    {
                        shapeInProgress = null;
                        setState(AnimatedPoseurState.POSE_EDITOR_CREATE_SHAPE_STATE);
                    }
                    else
                    {
                        // OUR LITTLE SHAPE HAS GROWN UP
                        pose.addShape(shapeInProgress);
                        selectedShape = shapeInProgress;
                        shapeInProgress = null;
                         // WE CAN DRAW ANOTHER ONE NOW
                        setState(AnimatedPoseurState.POSE_EDITOR_CREATE_SHAPE_STATE);
                        // REPAINT OF COURSE
                        repaintCanvases();
                    }
               }
    }

    /**
     * 
     * will cut the selected item and put it on the clipboard.
     */
    public void cutSelectedItem()
    {
        // CUT OF THE SELECTED ITEM AND        
        // PLACE THE SELECTED ITEM ON THE CLIPBOARD
        clipboard = selectedShape.clone();
        pose.removeShape(selectedShape);
        selectedShape = null;
        // THIS JUST REFRESHES CONTROLS
        setState(state);
        
        repaintCanvases();
    }
    
    /**
     * This will process a request to copy the selected item, which
     * will copy the selected item and put it on the clipboard.
     */
    public void copySelectedItem()
    {
        // MAKE A COPY OF THE SELECTED ITEM AND        
        // PLACE THE SELECTED ITEM ON THE CLIPBOARD
        clipboard = selectedShape.clone();
        
        // THIS JUST REFRESHES CONTROLS
        setState(state);
    }

    /**
     * This method puts the item in the clipboard onto the 
     * rendering surface.
     */
    public void pasteSelectedItem()
    {
        // GET THE SHAPE NO THE CLIPBOARD AND ADD IT
        // TO THE RENDERING SURFACE
        PoseurShape shapeToPaste = clipboard.clone();
        shapeToPaste.move(0, 0);
        pose.addShape(shapeToPaste);

        // REPAINT THE CANVASES
        repaintCanvases();
    } 
    
    
    /**
     * This method will assign the selected color to the selected
     * shape, if there is one, based on which toggle button is
     * selected. Note that it will also change the color in the
     * toggle button.
     * 
     * @param selectedColor The color used to change the selected
     * shape and/or the outline/fill toggle button background.
     */
    public void selectPalletColor(Color selectedColor)
    {
        AnimatedPoseur singleton = AnimatedPoseur.getPoseur();
        AnimatedPoseurGUI gui = singleton.getGUI();
        
        // IF A SHAPE IS SELECTED, THEN WE'RE GOING TO CHANGE IT
        if (isShapeSelected())
        {
            if (gui.isOutlineColorSelectionButtonToggled())
            {
                selectedShape.setOutlineColor(selectedColor);
            }
            else
            {
                selectedShape.setFillColor(selectedColor);
            }
            
            // REPAINT THE SHAPES
            repaintCanvases();
        }
        
        // EITHER WAY WE CHANGE THE BACKGROUND OF THE TOGGLE BUTTON
        if (gui.isOutlineColorSelectionButtonToggled())
        {
            gui.setOutlineToggleButtonColor(selectedColor);
        }
        else
        {
            gui.setFillToggleButtonColor(selectedColor);
        }
    }   
    
    /**
     * Resets the state of the system such that the pose has
     * no shapes and there are no shapes in progress or
     * selected.
     */
    public void resetState()
    {
        pose.reset();
        selectedShape = null;
        shapeInProgress = null;
        if(state == AnimatedPoseurState.NEW_POSE_COMPLETE_SHAPE_STATE ||
                state == AnimatedPoseurState.NEW_POSE_DRAG_SHAPE_STATE||
                state == AnimatedPoseurState.NEW_POSE_SELECT_SHAPE_STATE||
                state == AnimatedPoseurState.NEW_POSE_SHAPE_SELECTED_STATE||
                state == AnimatedPoseurState.NEW_POSE_CREATE_SHAPE_STATE)
        {
            setState(AnimatedPoseurState.NEW_POSE_SELECT_SHAPE_STATE);
        }
        else if(state == AnimatedPoseurState.POSE_EDITOR_COMPLETE_SHAPE_STATE ||
                state == AnimatedPoseurState.POSE_EDITOR_CREATE_SHAPE_STATE||
                state == AnimatedPoseurState.POSE_EDITOR_DRAG_SHAPE_STATE||
                state == AnimatedPoseurState.POSE_EDITOR_SELECT_SHAPE_STATE||
                state == AnimatedPoseurState.POSE_EDITOR_SHAPE_SELECTED_STATE)
        {
            setState(AnimatedPoseurState.POSE_EDITOR_SHAPE_SELECTED_STATE);
        }
    }
    
    /**
     *  Refresh the all editor control
     *
     */
    public void refreshEditorControl()
    {
        pose.reset();
        selectedShape = null;
        shapeInProgress = null;
        clipboard = null;
    }
    /**
     * Changes the state (i.e. mode) of the application
     * such that the user may select shapes.
     */
    public void startShapeSelection()
    {
          if(state == AnimatedPoseurState.NEW_POSE_COMPLETE_SHAPE_STATE ||
                state == AnimatedPoseurState.NEW_POSE_DRAG_SHAPE_STATE||
                state == AnimatedPoseurState.NEW_POSE_SELECT_SHAPE_STATE||
                state == AnimatedPoseurState.NEW_POSE_SHAPE_SELECTED_STATE||
                state == AnimatedPoseurState.NEW_POSE_CREATE_SHAPE_STATE)
        {
            setState(AnimatedPoseurState.NEW_POSE_SELECT_SHAPE_STATE);
        }
        else if(state == AnimatedPoseurState.POSE_EDITOR_COMPLETE_SHAPE_STATE ||
                state == AnimatedPoseurState.POSE_EDITOR_CREATE_SHAPE_STATE||
                state == AnimatedPoseurState.POSE_EDITOR_DRAG_SHAPE_STATE||
                state == AnimatedPoseurState.POSE_EDITOR_SELECT_SHAPE_STATE||
                state == AnimatedPoseurState.POSE_EDITOR_SHAPE_SELECTED_STATE)
        {
            setState(AnimatedPoseurState.POSE_EDITOR_SELECT_SHAPE_STATE);
        }
    }
        
    /**
     * Called any time the contents of this state change such that it
     * affects the view, this method forces both canvases to repaint.
     */
    private void repaintCanvases()
    {
        // THE POSE HAS CHANGED
        markPoseChanged();       
        PoseCanvas trueCanvas = trueCanvasState.getCanvas();
        trueCanvas.repaint();
        PoseCanvas zoomableCanvas = zoomableCanvasState.getCanvas();
        zoomableCanvas.repaint();
    }
    
    /**
     * Changes the dimensions of the pose we're editing.
     * 
     * @param initWidth The new pose width to use.
     * 
     * @param initHeight The new pose height to use.
     */
    public void setPoseDimensions(int initWidth, int initHeight)
    {
        // CHANGE THE DATA0
        pose.setPoseWidth(initWidth);
        pose.setPoseHeight(initHeight);
        trueCanvasState.updatePoseArea();
        zoomableCanvasState.updatePoseArea();
        
        // AND RENDER EVERYTHING WITH THE NEW DATA
        repaintCanvases();
    }

    /**
     * This method sets the state to its current state, which
     * cascades a reloading of gui control enabling and disabling.
     */
    public void refreshState()
    {
        setState(state);
        repaintCanvases();
        
    }
    
    /**
     * We call this method every time something in the pose has changed
     * such that we are aware that the most recent Pose does not match
     * the file.
     */
    public void markPoseChanged()
    {
        AnimatedPoseur singleton = AnimatedPoseur.getPoseur();
        PoseFileManager fileManager = singleton.getPoseFileManager();
        fileManager.markPoseFileAsNotSaved();
    }  
  
    /**
     * This is accessor helper method to help us get current outline thickness of the selected shape
     * Precondition is that this selected shape can not be null
     * @return 
     *   return the thickness of current selected outline
     */
    public BasicStroke getSelectedShapeOutlineThickness()
    {
        if(selectedShape!=null)
        {
             return selectedShape.getOutlineThickness();
        }
        else
        {
            return null;
        }
    }
    
    /**
     * This is mutator method that helps us to set a new outline thickness to the current selected
     * outline
     * Precondition is that selected shape can not be null
     * Postcondition is that both zoomble and real canvases are both repainted
     * @param bs 
     *  The new outline thickness needs to be set to the selected outline
     * 
     */
    public void setSelectedShapeOutlineThickness(BasicStroke bs)
    {
        if(selectedShape!=null)
        {
            selectedShape.setOutlineThickness(bs);
            repaintCanvases();
        }
    }
    
    /**
     * This mutator method helps us to set the new alpha value into current selected shape
     * Postcondition is that the invisibility of current selected shape changed
     * @param initAlpha
     *  initAlpha is new integer value represents the alpha value we will set in current selected shape
     */
    public void setAlphaValue(int initAlpha)
    {
        if(selectedShape!=null)
        {
            selectedShape.setAlpha(initAlpha);
            repaintCanvases();
        }
    }


    /**
     * @param pose the pose to set
     */
    public void setPose(AnimatedPoseurPose pose) {
        this.pose = pose;
    }

}