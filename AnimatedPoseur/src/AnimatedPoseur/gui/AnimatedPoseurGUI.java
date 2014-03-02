package AnimatedPoseur.gui;

import AnimatedPoseur.AnimatedPoseur;
import static AnimatedPoseur.AnimatedPoseurSetting.*;
import AnimatedPoseur.events.animationStates.*;
import AnimatedPoseur.events.canvas.PoseCanvasComponentHandler;
import AnimatedPoseur.events.canvas.PoseCanvasMouseHandler;
import AnimatedPoseur.events.colors.ColorPalletHandler;
import AnimatedPoseur.events.colors.CustomColorHandler;
import AnimatedPoseur.events.colors.FillColorHandler;
import AnimatedPoseur.events.colors.OutlineColorHandler;
import AnimatedPoseur.events.edit.AlphaHandler;
import AnimatedPoseur.events.edit.CopyHandler;
import AnimatedPoseur.events.edit.CutHandler;
import AnimatedPoseur.events.edit.ExitPoseEditorHandler;
import AnimatedPoseur.events.edit.MoveToBackHandler;
import AnimatedPoseur.events.edit.MoveToFrontHandler;
import AnimatedPoseur.events.edit.OutlineThicknessHandler;
import AnimatedPoseur.events.edit.PasteHandler;
import AnimatedPoseur.events.edit.SaveAndExportImageHandler;
import AnimatedPoseur.events.edit.StartSelectionHandler;
import AnimatedPoseur.events.pose.*;
import AnimatedPoseur.events.shapes.EllipseSelectionHandler;
import AnimatedPoseur.events.shapes.LineSelectionHandler;
import AnimatedPoseur.events.shapes.RectangleSelectionHandler;
import AnimatedPoseur.events.sprite.ExitHandler;
import AnimatedPoseur.events.sprite.NewAnimatedPoseurHandler;
import AnimatedPoseur.events.sprite.OpenAnimatedPoseurHandler;
import AnimatedPoseur.events.sprite.SaveAsHandler;
import AnimatedPoseur.events.sprite.SaveHandler;
import AnimatedPoseur.events.viewer.*;
import AnimatedPoseur.events.window.PoseurWindowHandler;
import AnimatedPoseur.events.zoom.ChangePoseDimensionsHandler;
import AnimatedPoseur.events.zoom.ZoomInHandler;
import AnimatedPoseur.events.zoom.ZoomOutHandler;
import AnimatedPoseur.files.ColorPalletLoader;
import AnimatedPoseur.files.PoseurFileManager;
import AnimatedPoseur.renderer.SceneRenderer;
import AnimatedPoseur.renderer.Sprite;
import AnimatedPoseur.shapes.PoseurShape;
import AnimatedPoseur.state.AnimatedPoseurState;
import AnimatedPoseur.state.ColorPalletState;
import AnimatedPoseur.state.PoseCanvasState;
import AnimatedPoseur.state.PoseurStateManager;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
/**
 * This class provides the full Graphical User Interface for the
 * Poseur application. It contains references to all GUI components,
 * including the rendering surfaces, and has service methods for
 * updating them.
 * 
 * @author  Richard McKenna
 *          Debugging Enterprises
 * @version 1.0
 */
public class AnimatedPoseurGUI extends JFrame
{
    // THE NAME OF THE APPLICATION, WE'LL PUT THIS IN THE 
    // WINDOW'S TITLE BAR, BUT MIGHT ALSO INCLUDE THE 
    // FILE NAME THERE
    protected String appName;
        
    // WE'LL HAVE TWO CANVASES IN THE CENTER, THE
    // ONE ON THE LEFT IS THE TRUE ONE, AND WILL NEVER
    // ZOOM THE VIEW. THE ONE ON THE RIGHT WILL ALLOW 
    // FOR ZOOMED IN AND OUT VIEWS. NOTE THAT WE'LL PUT
    // THEM INTO A SPLIT PANE
    private JSplitPane canvasSplitPane;

    private PoseCanvas trueCanvas;
    private PoseCanvas zoomableCanvas;
    
    // NORTH PANEL - EVERYTHING ELSE GOES IN HERE

    private JPanel northPanel;
    private JPanel northOfNorthPanel;
    private JPanel southOfNorthPanel;
    private JPanel westOfSouthOfNorthPanel;
    private JPanel centerOfSouthOfNorthPanel;
    private JPanel eastOfSouthOfNorthPanel;
    private SceneRenderer eastSceneRenderingPanel;
    private JPanel southPanel;
    private JPanel westOfSouthPanel;
    private JPanel eastOfSouthPanel;
    private JPanel northOfWestOfSouthPanel;
    private JPanel southOfWestOfSouthPanel;

    
    // FILE CONTROLS, WHICH IS LOCATED IN WEST OF NORTH PANEL
    private JToolBar fileToolbar;
    private JButton newAnimatedPoseurButton;
    private JButton openAnimatedPoseurButton;
    private JButton saveAnimatedPoseurButton;
    private JButton saveAsAnimatedPoseurButton;
    private JButton exitAnimatedPoseurButton;
    
    // ANIMATION STATES CONTROLS, WHICH IS LOCATED IN CENTER OF NORTH PANEL
    private JToolBar animationStateToolbar;
    private JButton newAnimationStateButton;
    private JButton renameAnimationStateButton;
    private JButton deleteAnimationStateButton;
    private JButton duplicateAnimationStateButton;
    private JList animationStateList;
    private JScrollPane paneForState;
    
    // POSE CONTROLS, WHICH IS LOCATED IN EAST OF NORTH PANEL
    private JToolBar poseToolbar;
    private JButton newPoseButton;
    private JButton addPoseButton;
    private JButton deletePoseButton;
    private JButton duplicatePoseButton;
    private JButton editPoseButton;
    private JButton setDurationOfPoseButton;
    private JButton moveUpPoseButton;
    private JButton moveDownPoseButton;
    private JList poseList;
    private JScrollPane paneForList;
    private JLabel imagesLabel;
    
    // SAVE AND EXPORT TOOLBAR
    private JToolBar saveToolbar;
    private JButton saveAndExportButton;
    private JButton exitButton;
    
    // EDIT CONTROLS
    private JToolBar editorToolbar;
    private JButton selectionButton;
    private JButton cutButton;
    private JButton copyButton;
    private JButton pasteButton;
    private JButton moveToBackButton;
    private JButton moveToFrontButton;
    
    // SHAPE SELECTION CONTROLS
    private JToolBar shapeToolbar;
    private JToggleButton rectToggleButton;
    private JToggleButton circleToggleButton;
    private JToggleButton lineToggleButton;
    private ButtonGroup shapeButtonGroup;
    private JComboBox lineStrokeSelectionComboBox;
    
    // ZOOM CONTROLS
    private JToolBar zoomToolbar;
    private JButton zoomOutButton;
    private JButton zoomInButton;
    private JButton dimensionsButton;
    private JLabel zoomLabel;
        
    // COLOR SELECTION CONTROLS
    private JToolBar colorSelectionToolbar;
    private ColorToggleButton outlineColorSelectionButton;
    private ColorToggleButton fillColorSelectionButton;
    private ButtonGroup colorButtonGroup;
    private ColorPallet colorPallet;    
    private JButton customColorSelectorButton;
    private JLabel alphaLabel;
    private JSlider transparencySlider;
    
    private JPanel animationToolbar;
    private JButton startAnimationButton;
    private JButton pauseAnimationButton;
    private JButton speedUpAnimationButton;
    private JButton slowDownAnimationButton;
    
    private ArrayList<Sprite> spriteList;
    private DefaultListModel animationStateListModel;
    private DefaultListModel<ImageIcon> poseListModel;

    /**
     * Default constructor for initializing the GUI. Note that the Poseur
     * application's state manager must have already been constructed and
     * setup when this method is called because we'll use some of the values
     * found there to initializer the GUI. The color pallet, for example, must
     * have already been loaded into the state manager before this constructor
     * is called.
     */
    public AnimatedPoseurGUI()
    {
        // IN CASE THE PARENT DOES ANYTHING, I USUALLY LIKE TO EXPLICITY INCLUDE THIS
        super();
        
        // CONSTRUCT AND LAYOUT THE COMPONENTS
        initGUI();

        // AND SETUP THE HANDLERS
        initHandlers();
        
        // ENABLE AND DISABLE ALL CONTROLS AS NEEDED
        updateMode();
    }
    
    // ACCESSOR METHODS
    
    /**
     * Accessor method for getting the application's name.
     * 
     * @return The name of the application this window is 
     * being used for.
     */
    public String getAppName() { return appName; }
    
    /**
     * Accessor method for getting the color pallet.
     * 
     * @return The color pallet component.
     */
    public ColorPallet getColorPallet() { return colorPallet; }
    
    /**
     * Accessor method for getting the color currently set
     * for filling shapes.
     * 
     * @return The fill color currently in use for making shapes.
     */
    public Color getFillColor() { return fillColorSelectionButton.getBackground(); }

    /**
     * Accessor method for getting the color currently set
     * four outlining shapes.
     * 
     * @return The outline color currently in use for making shapes.
     */
    public Color getOutlineColor() { return outlineColorSelectionButton.getBackground(); }

    
 
    /**
     * Accessor method for getting the line thickness currently
     * set for drawing shape outlines and lines.
     * 
     * @return The line thickness currently in use for making shapes.
     */
    public int getLineThickness() { return lineStrokeSelectionComboBox.getSelectedIndex() + 1; }
    
    /**
     * Accessor method for getting the current transparency value of the slider.
     * 
     * @return The alpha value, between 0 (fully transparent) and 255 (fully opaque)
     * as currently set by the transparency slider.
     */
    public int getAlphaTransparency() { return transparencySlider.getValue(); }

    /**
     * Accessor method for getting the canvas that will
     * not zoom and will render the pose as is.
     * 
     * @return The true canvas, which is on the left.
     */
    public PoseCanvas getTruePoseCanvas() { return trueCanvas; }
    
    /**
     * Accessor method for getting the index of animation state list
     * 
     * @return An integer that indicates the index of animation state list
     */
    public int getSelectedAnimationState() {return animationStateList.getSelectedIndex();  }
    /**
     * Accessor method for getting the index of animation pose list
     * 
     * @return An integer that indicates the index of animation pose list
     */
    public int getSelectedPose() 
    { 
        return poseList.getSelectedIndex();
    }
    /**
     * Accessor method for getting the canvas that will
     * zoom in and out, rendering the pose accordingly.
     * 
     * @return The zoomable canvas, which is on the right.
     */
    public PoseCanvas getZoomablePoseCanvas() { return zoomableCanvas; }
    
    /**
     * Accessor method for getting the animation state list.
     * 
     * @return The animation state list.
     */
    public JList getAnimationStateList(){return animationStateList;}
           
      /**
     * Accessor method for getting the pose list.
     * 
     * @return The pose list.
     */
    public JList getPoseList(){return poseList;}
    
    /**
     * Accessor method for getting the SceneRender pane
     * 
     * @return The scene renderer panel
     */
    public SceneRenderer getSceneRender(){return eastSceneRenderingPanel; }
    
    
    /**
     * @return the imagesLabel
     */
    public JLabel getImagesLabel() {
        return imagesLabel;
    }
 
    
    /**
     * Accessor method getting the Animation State List model
     * 
     * @return Animation State List Model
     */
    public DefaultListModel getAnimationStateListModel() {return animationStateListModel;}
    
    /**
     * Accessor method getting Pose List Model
     * 
     * @return Pose list model
     */
    public DefaultListModel getPoseListModel() {return poseListModel;}
 

    /**
     * Accessor method to test if the outline color toggle button
     * is selected. Note that either the outline or fill button
     * must be selected at all times.
     * 
     * @return true if the outline toggle button is selected, false if
     * the fill button is selected.
     */
    public boolean isOutlineColorSelectionButtonToggled() { return outlineColorSelectionButton.isSelected(); }
    
    // MUTATOR METHODS
    
    /**
     * Mutator method for setting the app name.
     * 
     * @param initAppName The name of the application,
     * which will be put in the window title bar.
     */
    public void setAppName(String initAppName)
    {
        appName = initAppName;
    }    
    
    
    /**
     * This mutator method sets the background color for the
     * outline toggle button, which can then be used for 
     * the outline of new shapes.
     * 
     * @param initColor The color to use for shape outlines.
     */
    public void setOutlineToggleButtonColor(Color initColor)
    {
        outlineColorSelectionButton.setBackground(initColor);
    }

    /**
     * This mutator method sets the background color for the fill
     * toggle button, which can then be used for the fill
     * color of new shapes.
     * 
     * @param initColor The color to use for shape filling.
     */
    public void setFillToggleButtonColor(Color initColor)
    {
        fillColorSelectionButton.setBackground(initColor);
    }
    
       
    /**
     * Mutator method that setting given model to the Animation State List Model
     * 
     * @param model List Model to be set
     */
    public void setAnimationStateListModel(DefaultListModel model)
    {
        animationStateListModel = model;
        animationStateList.setModel(animationStateListModel);
    }
    
    /**
     * Mutator method that setting given model to the post list model
     * 
     * @param model List Model to be set
     * 
     */
    public void setPoseListModel(DefaultListModel model)
    {
        poseListModel = model;
        poseList.setModel(poseListModel);
    }
    

    // PUBLIC METHODS OTHER CLASSES NEED TO CALL
    
    /**
     * This method updates the zoom label display with the current
     * zoom level.
     */
    public void updateZoomLabel()
    {
        // GET THE RIGHT CANVAS STATE, SINCE IT ZOOMS
        AnimatedPoseur singleton = AnimatedPoseur.getPoseur();
        PoseurStateManager AnimatedPoseurStateManager = singleton.getPoseStateManager();
        PoseCanvasState zoomableCanvasState = AnimatedPoseurStateManager.getZoomableCanvasState();

        // GET THE ZOOM LEVEL
        float zoomLevel = zoomableCanvasState.getZoomLevel();
        
        // MAKE IT LOOK NICE
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMinimumFractionDigits(1);
        nf.setMaximumFractionDigits(1);
        String zoomText = ZOOM_LABEL_TEXT_PREFIX
                + nf.format(zoomLevel)
                + ZOOM_LABEL_TEXT_POSTFIX;
        
        // AND PUT IT IN THE LABEL
        zoomLabel.setText(zoomText);
    }    
    
    /**
     * The fill and outline toggle buttons are connected,
     * only one can be toggled on a any time. This method
     * turns the fill toggle button on.
     */
    public void toggleFillColorButton()
    {
        fillColorSelectionButton.select();
        outlineColorSelectionButton.deselect();
    }
    
    /**
     * The fill and outline toggle buttons are connected,
     * only one can be toggled on a any time. This method
     * turns the outline toggle button on.
     */
    public void toggleOutlineColorButton()
    {
        fillColorSelectionButton.deselect();
        outlineColorSelectionButton.select();
    }
        
    /**
     * This method updates the appropriate toggle button,
     * either outline or fill, with the color argument, 
     * setting it to its background.
     * 
     * @param color Color to use to set the background
     * for the appropriate toggle button.
     */
    public void updateDrawingColor(Color color)
    {
        // IF THE OUTLINE TOGGLE IS THE ONE THAT'S
        // CURRENTLY SELECTED, THEN THAT'S THE ONE
        // THE USER WANTED TO CHANGE
        if (outlineColorSelectionButton.isSelected())
        {
            outlineColorSelectionButton.setBackground(color);
        }
        // OTHERWISE IT'S THE FILL TOGGLE BUTTON
        else if (fillColorSelectionButton.isSelected())
        {
            fillColorSelectionButton.setBackground(color);
        }
    }

    /**
     * Called each time the application's state changes, this method
     * is responsible for enabling, disabling, and generally updating 
     * all the GUI control based on what the current application
     * state (i.e. the PoseurMode) is in.
     */
    public final void updateMode()
    {
        // WE'LL NEED THESE GUYS
        AnimatedPoseur singleton = AnimatedPoseur.getPoseur();
        PoseurStateManager state = singleton.getPoseStateManager();
        AnimatedPoseurState mode = state.getMode();
        PoseurFileManager poseurFileManager = singleton.getPoseurFileManager();

        // IN THIS MODE THE USER IS ABOUT TO START DRAGGING
        // THE MOUSE TO CREATE A SHAPE
        if(mode == AnimatedPoseurState.NEW_POSE_COMPLETE_SHAPE_STATE||
               mode == AnimatedPoseurState.POSE_EDITOR_COMPLETE_SHAPE_STATE)
        {
            // THIS USES THE CROSSHAIR
            selectCursor(Cursor.CROSSHAIR_CURSOR);
            setEnabledPoseControls(false);
            setEnabledAnimationStateControls(true);
            animationStateList.setEnabled(false);
            poseList.setEnabled(false); 
            markPoseurChanged();
        }
        
        else if (mode == AnimatedPoseurState.NEW_POSE_CREATE_SHAPE_STATE || 
                mode == AnimatedPoseurState.POSE_EDITOR_CREATE_SHAPE_STATE)
        {
            // THIS USES THE CROSSHAIR
            selectCursor(Cursor.CROSSHAIR_CURSOR);
            // TURN THE APPROPRIATE CONTROLS ON/OFF
            setEnabledEditControls(false);
            selectionButton.setEnabled(true);
            setEnabledColorControls(true);
            setEnabledShapeControls(true);
            setEnabledZoomControls(true);
            enableSaveAndExportControl(true);
            markPoseurChanged();
                       
        }
//         IN THIS STATE THE USER HAS SELECTED A SHAPE
//         ON THE CANVAS AND IS DRAGGING IT
        else if (mode == AnimatedPoseurState.NEW_POSE_DRAG_SHAPE_STATE || 
                mode == AnimatedPoseurState.POSE_EDITOR_DRAG_SHAPE_STATE)
        {
            // THIS USES THE MOVE 
            selectCursor(Cursor.MOVE_CURSOR);
            
            setEnabledPoseControls(false);
            setEnabledAnimationStateControls(false);
            animationStateList.setEnabled(false);
            poseList.setEnabled(false);
            markPoseurChanged();
        }
        
        else if(mode == AnimatedPoseurState.NEW_POSE_CREATE_SHAPE_STATE||
                mode == AnimatedPoseurState.POSE_EDITOR_CREATE_SHAPE_STATE)
        {
            selectCursor(Cursor.CROSSHAIR_CURSOR);
            
            // TURN THE APPROPRIATE CONTROLS ON/OFF
            setEnabledEditControls(false);
            selectionButton.setEnabled(true);
            setEnabledPoseControls(false);
            setEnabledAnimationStateControls(false);
            animationStateList.setEnabled(false);
            poseList.setEnabled(false);
            markPoseurChanged();
        }
        
        else if (mode == AnimatedPoseurState.NEW_POSE_SELECT_SHAPE_STATE||
                 mode == AnimatedPoseurState.POSE_EDITOR_SELECT_SHAPE_STATE)
        {
            // THIS USES THE ARROW CURSOR
            selectCursor(Cursor.DEFAULT_CURSOR);
            
            // THERE IS NO SHAPE SELECTED, SO WE CAN'T
            // USE THE EDIT CONTROLS
            setEnabledEditControls(false);
            selectionButton.setEnabled(true);
            setEnabledColorControls(true);
            setEnabledShapeControls(true);
            setEnabledZoomControls(true);
            enableSaveAndExportControl(true);
            setEnabledPoseControls(false);
            setEnabledAnimationStateControls(true);
            animationStateList.setEnabled(false);
            poseList.setEnabled(false);
           
        }
        else if ( mode == AnimatedPoseurState.NEW_POSE_SHAPE_SELECTED_STATE||
                mode == AnimatedPoseurState.POSE_EDITOR_SHAPE_SELECTED_STATE)
        {
            // THIS USES THE ARROW CURSOR
            selectCursor(Cursor.DEFAULT_CURSOR);
            
            // THERE IS NO SHAPE SELECTED, SO WE CAN'T
            // USE THE EDIT CONTROLS
            setEnabledEditControls(true);
            setEnabledPoseControls(false);
            setEnabledAnimationStateControls(true);
            animationStateList.setEnabled(false);
            poseList.setEnabled(false);
            markPoseurChanged();
        }

        // THIS IS THE STATE WHEN THE Poseur APP FIRST
        // STARTS. THERE IS NO Pose YET, SO MOST CONTROLS
        // ARE DISABLED
        else if (mode == AnimatedPoseurState.STARTUP_STATE)
        {
            // THIS USES THE ARROW CURSOR
            selectCursor(Cursor.DEFAULT_CURSOR);
            
            // NOTHING IS SELECTED SO WE CAN'T EDIT YET
            enableStartupFileControls();
            setEnabledPoseControls(false);
            setEnabledAnimationStateControls(false);
            setEnabledEditControls(false);
            selectionButton.setEnabled(false);
            setEnabledColorControls(false);
            toggleOutlineColorButton();
            setEnabledZoomControls(false);
            setEnabledShapeControls(false);;
            setEnabledViewer(false);
            animationStateList.setEnabled(true);
            poseList.setEnabled(true);
            enableSaveAndExportControl(false);
        }
        else if (mode == AnimatedPoseurState.SELECT_ANIMATION_STATE_STATE)
        {
            // THIS USES THE ARROW CURSOR
            selectCursor(Cursor.DEFAULT_CURSOR);
            
            enableSaveAsControl();

            setEnabledAnimationStateControls(false);
            setEnabledPoseControls(false);
            enableNewAnimationStateButton();
            setEnabledViewer(false);

            setEnabledEditControls(false);
            setEnabledColorControls(false);
            toggleOutlineColorButton();
            setEnabledZoomControls(false);
            setEnabledShapeControls(false);
            animationStateList.setEnabled(true);
            poseList.setEnabled(true);
            enableSaveAndExportControl(false);
            markPoseurChanged();
        }
        
        else if (mode == AnimatedPoseurState.SELECT_POSEUR_POSE_STATE)
        {
            // THIS USES THE ARROW CURSOR
            selectCursor(Cursor.DEFAULT_CURSOR);
            setEnabledViewer(true);
            setEnabledAnimationStateControls(true);
            setEnabledPoseControls(false);
            enableNewPoseButton();
            enableAddPoseButton();
            setEnabledEditControls(false);
            setEnabledColorControls(false);
            toggleOutlineColorButton();
            setEnabledZoomControls(false);
            setEnabledShapeControls(false);
            animationStateList.setEnabled(true);
            poseList.setEnabled(true);
            enableSaveAndExportControl(false);
            markPoseurChanged();
        }
        
        else if(mode == AnimatedPoseurState.POSEUR_POSE_SELECTED_STATE)
        {
             // THIS USES THE ARROW CURSOR
            selectCursor(Cursor.DEFAULT_CURSOR);
            setEnabledPoseControls(true);
            setEnabledAnimationStateControls(true);
            setEnabledViewer(true);
            setEnabledEditControls(false);
            selectionButton.setEnabled(false);
            setEnabledColorControls(false);
            toggleOutlineColorButton();
            setEnabledZoomControls(false);
            setEnabledShapeControls(false);
            animationStateList.setEnabled(true);
            poseList.setEnabled(true);
            enableSaveAndExportControl(false);
            markPoseurChanged();
        }
        saveAnimatedPoseurButton.setEnabled(!poseurFileManager.isSaved());
    }
    
    public void updataCanvas()
    {
        AnimatedPoseur singleton = AnimatedPoseur.getPoseur();
        PoseurStateManager state = singleton.getPoseStateManager();
        // AND UPDATE THE SLIDER
        PoseurShape selectedShape = state.getSelectedShape();
        if (selectedShape != null)
        {
            // UPDATE THE SLIDER ACCORDING TO THE SELECTED
            // SHAPE'S ALPHA (TRANSPARENCY) VALUE, IF THERE
            // EVEN IS A SELECTED SHAPE
            transparencySlider.setValue(selectedShape.getAlpha());
        }

        // REDRAW EVERYTHING
        trueCanvas.repaint();
        zoomableCanvas.repaint();
    }
    // PRIVATE HELPER METHODS

    /**
     * This helper method constructs and lays out all GUI components, initializing
     * them to their default startup state.
     */
    private void initGUI()
    {
        // MAKE THE COMPONENTS
        constructGUIControls();
        
        // AND ARRANGE THEM
        layoutGUIControls();
    }
    
    /**
     * Helper method that constructs all the GUI controls and
     * loads them with their necessary art and data.
     */    
    private void constructGUIControls()
    {
        // SOME COMPONENTS MAY NEED THE STATE MANAGER
        // FOR INITIALIZATION, SO LET'S GET IT
        AnimatedPoseur singleton = AnimatedPoseur.getPoseur();
        PoseurStateManager AnimatedPoseStateManager = singleton.getPoseStateManager();

        // LET'S START BY INITIALIZING THE CENTER AREA,
        // WHERE WE'LL RENDER EVERYTHING. WE'LL HAVE TWO
        // CANVASES AND PUT THEM INTO DIFFERENT SIDES
        // OF A JSplitPane
        canvasSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

        // LET'S MAKE THE CANVAS ON THE LEFT SIDE, WHICH
        // WILL NEVER ZOOM
        PoseCanvasState trueCanvasState = AnimatedPoseStateManager.getTrueCanvasState();
        trueCanvas = new PoseCanvas(trueCanvasState);
        trueCanvasState.setPoseCanvas(trueCanvas);
        trueCanvas.setBackground(TRUE_CANVAS_COLOR);
        
        // AND NOW THE CANVAS ON THE RIGHT SIDE, WHICH
        // WILL BE ZOOMABLE
        PoseCanvasState zoomableCanvasState = AnimatedPoseStateManager.getZoomableCanvasState();
        zoomableCanvas = new PoseCanvas(zoomableCanvasState);
        zoomableCanvasState.setPoseCanvas(zoomableCanvas);
        zoomableCanvas.setBackground(ZOOMABLE_CANVAS_COLOR);
        
        // ULTIMATELY EVERYTHING IN THE NORTH GOES IN HERE, INCLUDING
        // THREE PANELS FULL OF JToolBars
        northPanel = new JPanel(new BorderLayout());
        
        // WEST PANEL OF NORTH INCLUDES FILE CONTROLS AND RENDERER BUTTONS
        northOfNorthPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        southOfNorthPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        westOfSouthOfNorthPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        centerOfSouthOfNorthPanel = new JPanel();
        eastOfSouthOfNorthPanel = new JPanel();
        
        // EAST PANEL IN FAME THAT IS OBLITGATE OF RENDERING
        spriteList = new ArrayList<>();
        eastSceneRenderingPanel = new SceneRenderer(spriteList);
        eastSceneRenderingPanel.setBackground(Color.white);
        Border etchedBorderForRender = BorderFactory.createEtchedBorder();
        Border titledBorderForRender = BorderFactory.createTitledBorder(etchedBorderForRender, ANIMATIED_SPRITE_VIEWER);
        eastSceneRenderingPanel.setBorder(titledBorderForRender);
        eastSceneRenderingPanel.setPreferredSize(new Dimension(450,370));
        eastSceneRenderingPanel.setBackground(new Color(247,247,171));
       
        
        southPanel = new JPanel(new BorderLayout());
        westOfSouthPanel = new JPanel(new BorderLayout());
        eastOfSouthPanel = new JPanel(new GridLayout(1,4));
        eastOfSouthPanel.setBackground(new Color(247,247,171));
        northOfWestOfSouthPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        southOfWestOfSouthPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        
        
        
        // WE'LL BATCH LOAD THE IMAGES
        MediaTracker tracker = new MediaTracker(this);
        int idCounter = 0;

        // FILE CONTROLS
        fileToolbar  = new JToolBar();
        newAnimatedPoseurButton    = (JButton)initButton(NEW_ANIMATED_POSEUR_IMAGE_FILE,      fileToolbar,  tracker, idCounter++, JButton.class, null, NEW_ANIMATED_POSEUR_TOOLTIP);
        openAnimatedPoseurButton   = (JButton)initButton(OPEN_ANIMATED_POSEUR_IMAGE_FILE,     fileToolbar,  tracker, idCounter++, JButton.class, null, OPEN_ANIMATED_POSEUR_TOOLTIP);
        saveAnimatedPoseurButton   = (JButton)initButton(SAVE_ANIMATED_POSEUR_IMAGE_FILE,     fileToolbar,  tracker, idCounter++, JButton.class, null, SAVE_ANIMATED_POSEUR_TOOLTIP);
        saveAsAnimatedPoseurButton   = (JButton)initButton(SAVE_AS_ANIMATED_POSEUR_IMAGE_FILE,     fileToolbar,  tracker, idCounter++, JButton.class, null, SAVE_AS_ANIMATED_POSEUR_TOOLTIP);
        exitAnimatedPoseurButton   = (JButton)initButton(EXIT_ANIMATED_POSEUR_IMAGE_FILE,     fileToolbar,  tracker, idCounter++, JButton.class, null, EXIT_ANIMATED_POSEUR_TOOLTIP);
        
        // ANIMATION STATE CONTROLS
        animationStateToolbar = new JToolBar();
        newAnimationStateButton = (JButton)initButton(NEW_ANIMATION_STATE_IMAGE_FILE,      animationStateToolbar,  tracker, idCounter++, JButton.class, null, NEW_ANIMATION_STATE_TOOLTIP);
        renameAnimationStateButton = (JButton)initButton(RENAME_ANIMATION_STATE_IMAGE_FILE,      animationStateToolbar,  tracker, idCounter++, JButton.class, null, RENAME_ANIMATION_STATE_TOOLTIP);
        deleteAnimationStateButton = (JButton)initButton(DELETE_ANIMATION_STATE_IMAGE_FILE,      animationStateToolbar,  tracker, idCounter++, JButton.class, null, DELETE_ANIMATION_STATE_TOOLTIP);
        duplicateAnimationStateButton = (JButton)initButton(DUPLICTATE_ANIMATION_STATE_IMAGE_FILE,      animationStateToolbar,  tracker, idCounter++, JButton.class, null, DUPLICTATE_ANIMATION_STATE_TOOLTIP);
        animationStateListModel = new DefaultListModel();
        animationStateList = new JList(animationStateListModel);
        animationStateList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        paneForState = new JScrollPane(animationStateList);
        paneForState.setPreferredSize(new Dimension(378,120));
        animationStateList.setBackground(new Color(153,255,255));
    
        // POSE CONTROLS
        poseToolbar = new JToolBar();
        newPoseButton = (JButton)initButton(NEW_POSE_IMAGE_FILE,      poseToolbar,  tracker, idCounter++, JButton.class, null, NEW_POSE_TOOLTIP);
        addPoseButton = (JButton)initButton(OPEN_POSE_IMAGE_FILE,      poseToolbar,  tracker, idCounter++, JButton.class, null, OPEN_POSE_TOOLTIP);
        deletePoseButton = (JButton)initButton(DELETE_POSE_IMAGE_FILE,      poseToolbar,  tracker, idCounter++, JButton.class, null, DELETE_POSE_TOOLTIP);
        duplicatePoseButton = (JButton)initButton(DUPLICATE_POSE_IMAGE_FILE,      poseToolbar,  tracker, idCounter++, JButton.class, null, DUPLICATE_POSE_TOOLTIP);
        editPoseButton = (JButton)initButton(EDIT_POSE_IMAGE_FILE,      poseToolbar,  tracker, idCounter++, JButton.class, null, EDIT_POSE_TOOLTIP);
        setDurationOfPoseButton = (JButton)initButton(SET_POSE_DURATION_IMAGE_FILE,      poseToolbar,  tracker, idCounter++, JButton.class, null, SET_POSE_DURATION_TOOLTIP);
        moveUpPoseButton = (JButton)initButton(MOVE_UP_POSE_IMAGE_FILE ,      poseToolbar,  tracker, idCounter++, JButton.class, null, MOVE_UP_POSE_TOOLTIP);
        moveDownPoseButton = (JButton)initButton(MOVE_DOWN_POSE_IMAGE_FILE,      poseToolbar,  tracker, idCounter++, JButton.class, null, MOVE_DOWN_POSE_TOOLTIP);
        poseListModel = new DefaultListModel();
        poseList = new JList(poseListModel);
        poseList.setVisibleRowCount(1);
        poseList.setLayoutOrientation(JList.HORIZONTAL_WRAP); 
        poseList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        paneForList = new JScrollPane(poseList);
        paneForList.setPreferredSize(new Dimension(650,120));
        poseList.setBackground(new Color(153,255,255));
        
        
        // EDITING CONTROLS
        editorToolbar = new JToolBar();    
        selectionButton = (JButton)initButton(SELECTION_IMAGE_FILE, editorToolbar, tracker, idCounter++, JButton.class, null, SELECT_TOOLTIP);
        cutButton = (JButton)initButton(CUT_IMAGE_FILE,editorToolbar,tracker,idCounter++,JButton.class,null,CUT_TOOLTIP);
        copyButton  = (JButton)initButton(COPY_IMAGE_FILE,   editorToolbar, tracker, idCounter++, JButton.class, null, COPY_TOOLTIP);
        pasteButton = (JButton)initButton(PASTE_IMAGE_FILE,  editorToolbar, tracker, idCounter++, JButton.class, null, PASTE_TOOLTIP);
        moveToBackButton = (JButton)initButton(MOVE_TO_BACK_IMAGE_FILE,editorToolbar,tracker,idCounter++,JButton.class,null,MOVE_TO_BACK_TOOLTIP);
        moveToFrontButton = (JButton)initButton(MOVE_TO_FRONT_IMAGE_FILE,editorToolbar,tracker,idCounter++,JButton.class,null,MOVE_TO_FRONT_TOOLTIP);
        
        // SAVING TOOLBAR
        saveToolbar= new JToolBar();
        saveAndExportButton = (JButton)initButton(SAVE_AND_EXPORT_IMAGE, saveToolbar, tracker, idCounter++, JButton.class, null, SAVE_AND_EXPORT_TOOLTIP);
        exitButton = (JButton)initButton(EXIT_POSE_EDITOR, saveToolbar, tracker, idCounter++, JButton.class, null, EXIT_POSE_EDITOR_TOOLTIP);
        
        // HERE ARE OUR SHAPE SELECTION CONTROLS
        shapeToolbar = new JToolBar();
        shapeButtonGroup = new ButtonGroup();
        lineToggleButton   = (JToggleButton)initButton( LINE_SELECTION_IMAGE_FILE,      shapeToolbar, tracker, idCounter++, JToggleButton.class, shapeButtonGroup, LINE_TOOLTIP);
        rectToggleButton   = (JToggleButton)initButton( RECT_SELECTION_IMAGE_FILE,      shapeToolbar, tracker, idCounter++, JToggleButton.class, shapeButtonGroup, RECT_TOOLTIP);
        circleToggleButton   = (JToggleButton)initButton( CIRCLE_SELECTION_IMAGE_FILE,      shapeToolbar, tracker, idCounter++, JToggleButton.class, shapeButtonGroup, CIRCLE_TOOLTIP);

        // THE LINE THICKNESS SELECTION COMBO BOX WILL GO WITH THE SHAPE CONTROLS
        DefaultComboBoxModel lineThicknessModel = new DefaultComboBoxModel();
        for (int i = 0; i < NUM_STROKES_TO_CHOOSE_FROM; i++)
        {
            String imageFileName =  STROKE_SELECTION_FILE_PREFIX
                                  + (i+1)
                                  + PNG_FILE_EXTENSION;
            Image img = batchLoadImage(imageFileName, tracker, idCounter++);
            ImageIcon ii = new ImageIcon(img);
            lineThicknessModel.addElement(ii);
        }
        lineStrokeSelectionComboBox = new JComboBox(lineThicknessModel);

        // NOW THE ZOOM TOOLBAR
        zoomToolbar = new JToolBar();
        zoomOutButton = (JButton)initButton(ZOOM_OUT_IMAGE_FILE, zoomToolbar, tracker, idCounter++, JButton.class, null, ZOOM_OUT_TOOLTIP);
        zoomInButton = (JButton)initButton(ZOOM_IN_IMAGE_FILE, zoomToolbar, tracker, idCounter++, JButton.class, null, ZOOM_IN_TOOLTIP);
        zoomLabel = new JLabel();
        zoomLabel.setFont(ZOOM_LABEL_FONT);
        updateZoomLabel();
        dimensionsButton = (JButton)initButton(POSE_DIMENSIONS_IMAGE_FILE, zoomToolbar, tracker, idCounter++, JButton.class, null, CHANGE_POSE_DIMENSIONS_TOOLTIP);
        
        // COLOR SELECTION CONTROLS
        colorSelectionToolbar = new JToolBar();
        colorButtonGroup = new ButtonGroup();
        outlineColorSelectionButton = (ColorToggleButton)initButton(OUTLINE_COLOR_IMAGE_FILE, colorSelectionToolbar, tracker, idCounter++, ColorToggleButton.class, colorButtonGroup, OUTLINE_TOOLTIP);
        outlineColorSelectionButton.setBackground(Color.BLACK);
        fillColorSelectionButton = (ColorToggleButton)initButton(FILL_COLOR_IMAGE_FILE, colorSelectionToolbar, tracker, idCounter++, ColorToggleButton.class, colorButtonGroup, FILL_TOOLTIP);
        fillColorSelectionButton.setBackground(Color.WHITE);
        outlineColorSelectionButton.setSelected(true);
        
        // AND LET'S LOAD THE COLOR PALLET FROM AN XML FILE
        ColorPalletLoader cpl = new ColorPalletLoader();
        ColorPalletState cps = new ColorPalletState();
        cpl.initColorPallet(COLOR_PALLET_SETTINGS_XML, COLOR_PALLET_SETTINGS_SCHEMA,cps);

        // NOW LET'S SETUP THE COLOR PALLET. USING THE
        // STATE WE JUST LOADED. NOW MAKE OUR COLOR PALLET
        // AND MAKE SURE THEY KNOW ABOUT ONE ANOTHER
        colorPallet = new ColorPallet(cps);
        cps.setView(colorPallet);

        // THIS CONTROL WILL LET US CHANGE THE COLORS IN THE COLOR PALLET
        customColorSelectorButton = (JButton)initButton(CUSTOM_COLOR_SELECTOR_IMAGE_FILE, colorSelectionToolbar, tracker, idCounter++, JButton.class, null, CUSTOM_COLOR_TOOLTIP);
        
        // AND THE TRANSPARENCY SLIDER AND LABEL
        alphaLabel = new JLabel(ALPHA_LABEL_TEXT);
        alphaLabel.setFont(ALPHA_LABEL_FONT);
        alphaLabel.setBackground(ALPHA_BACKGROUND_COLOR);
        transparencySlider = new JSlider(JSlider.HORIZONTAL, TRANSPARENT, OPAQUE, OPAQUE);
        transparencySlider.setBackground(ALPHA_BACKGROUND_COLOR);
        transparencySlider.setMajorTickSpacing(ALPHA_MAJOR_TICK_SPACING);// MAXIMUM
        transparencySlider.setMinorTickSpacing(ALPHA_MINOR_TICK_SPACING);// MINIMUM
        transparencySlider.setPaintLabels(true);
        transparencySlider.setPaintTicks(true);
        transparencySlider.setPaintTrack(true);
        transparencySlider.setToolTipText(ALPHA_TOOLTIP);
        transparencySlider.setSnapToTicks(false);
        
        animationToolbar = new JPanel();
        animationToolbar.setLayout(new FlowLayout());
        animationToolbar.setBackground(new Color(247,247,171));
                
        startAnimationButton = initAnimationButton(START_ANIMATION_IMAGE_FILE, START_ANIMATION_TOOLTIP, tracker, idCounter++, animationToolbar);
        pauseAnimationButton = initAnimationButton(PAUSE_ANIMATION_IMAGE_FILE, PAUSE_ANIMATION_TOOLTIP, tracker, idCounter++, animationToolbar);
        slowDownAnimationButton = initAnimationButton(SLOW_DOWN_ANIMATION_IMAGE_FILE, SLOW_DOWN_ANIMATION_TOOLTIP,tracker, idCounter++, animationToolbar);
        speedUpAnimationButton = initAnimationButton(SPEED_UP_ANIMATION_IMAGE_FILE,SPEED_UP_ANIMATION_TOOLTIP, tracker, idCounter++, animationToolbar);
        
        
        // NOW WE NEED TO WAIT FOR ALL THE IMAGES THE
        // MEDIA TRACKER HAS BEEN GIVEN TO FULLY LOAD
        try
        {
            tracker.waitForAll();
        }
        catch(InterruptedException ie)
        {
            // LOG THE ERROR
             ie.printStackTrace();       
        }
    }
        
    /**
     * This helper method locates all the components inside the frame. Note
     * that it does not put most buttons into their proper toolbars because 
     * that was already done for most when they were initialized by initButton.
     */
    private void layoutGUIControls()
    {
        // LET'S PUT THE TWO CANVASES INSIDE 
        // THE SPLIT PANE. WE'LL PUT THE DIVIDER
        // RIGHT IN THE MIDDLE AND WON'T LET
        // THE USER MOVE IT - FOOLPROOF DESIGN!
        canvasSplitPane.setLeftComponent(trueCanvas);
        canvasSplitPane.setRightComponent(zoomableCanvas);
        canvasSplitPane.setResizeWeight(0);
        canvasSplitPane.setDividerLocation(0);
        canvasSplitPane.setDividerSize(15);
        canvasSplitPane.setEnabled(false);
        
        // PUT THE COMBO BOX IN THE SHAPE TOOLBAR
        shapeToolbar.add(lineStrokeSelectionComboBox);
        
        // ARRANGE THE COLOR SELECTION TOOLBAR
        colorSelectionToolbar.add(colorPallet);        
        colorSelectionToolbar.add(customColorSelectorButton);
        colorSelectionToolbar.add(alphaLabel);
        colorSelectionToolbar.add(transparencySlider);

        
        // PUT CONTROLS ON NORTH OF NORTH PANEL
        northOfNorthPanel.add(fileToolbar);
        northOfNorthPanel.add(animationStateToolbar);
        northOfNorthPanel.add(poseToolbar);
        
        eastOfSouthOfNorthPanel.setPreferredSize(new Dimension(150,150));
        imagesLabel = new JLabel();
        eastOfSouthOfNorthPanel.add(getImagesLabel());
        centerOfSouthOfNorthPanel.add(paneForList);
        
        Border etchedBorderForEastOfSouthOfNorthPanel = BorderFactory.createEtchedBorder();
        Border titledBorderForEastOfSouthOfNorthPanel = BorderFactory.createTitledBorder(etchedBorderForEastOfSouthOfNorthPanel, POSE);
        eastOfSouthOfNorthPanel.setBorder(titledBorderForEastOfSouthOfNorthPanel);
//        eastOfSouthOfNorthPanel.setBackground(new Color(153,255,255));
        
        Border etchedBorderForCenter = BorderFactory.createEtchedBorder();
        Border titledBorderForCenter = BorderFactory.createTitledBorder(etchedBorderForCenter, SELECT_ANIMATED_POSEUR_POSE);
        centerOfSouthOfNorthPanel.setBorder(titledBorderForCenter);
        
        westOfSouthOfNorthPanel.add(paneForState);
        Border etchedBorderForWest = BorderFactory.createEtchedBorder();
        Border titledBorderForWest = BorderFactory.createTitledBorder(etchedBorderForWest, SELECT_ANIMATED_POSEUR_ANIMATION_STATE);
        westOfSouthOfNorthPanel.setBorder(titledBorderForWest);
        
        southOfNorthPanel.add(westOfSouthOfNorthPanel);
        southOfNorthPanel.add(centerOfSouthOfNorthPanel);
        southOfNorthPanel.add(eastOfSouthOfNorthPanel);
        
        
        
        
        // NOW ARRANGE THE TOOLBARS
        northOfWestOfSouthPanel.add(saveToolbar);
        northOfWestOfSouthPanel.add(editorToolbar);
        northOfWestOfSouthPanel.add(shapeToolbar);
        northOfWestOfSouthPanel.add(zoomToolbar);
        southOfWestOfSouthPanel.add(colorSelectionToolbar);
        
        // NOW PUT ALL THE CONTROLS IN THE SOUTH 
        westOfSouthPanel.add(northOfWestOfSouthPanel,BorderLayout.NORTH);
        westOfSouthPanel.add(southOfWestOfSouthPanel,BorderLayout.SOUTH);
        eastOfSouthPanel.add(animationToolbar);
        Border etchedBorder = BorderFactory.createEtchedBorder();
        Border titledBorder = BorderFactory.createTitledBorder(etchedBorder, ANIMATIED_SPRITE_VIEWER_CONTROLS);
        eastOfSouthPanel.setBorder(titledBorder);
        eastOfSouthPanel.setPreferredSize(new Dimension(450,100));
        southPanel.add(westOfSouthPanel,BorderLayout.WEST);
        southPanel.add(eastOfSouthPanel,BorderLayout.EAST);

        // NOW PUT ALL THE CONTROLS IN THE NORTH
        northPanel.add(northOfNorthPanel,BorderLayout.NORTH);
        northPanel.add(southOfNorthPanel,BorderLayout.SOUTH);

        // AND NOW PUT EVERYTHING INSIDE THE FRAME
        add(northPanel, BorderLayout.NORTH);
        add(canvasSplitPane, BorderLayout.CENTER);
        add(southPanel,BorderLayout.SOUTH);
        add(eastSceneRenderingPanel, BorderLayout.EAST);
        
    }
    
    /**
     * GUI setup method can be quite lengthy and repetitive so
     * it helps to create helper methods that can do a bunch of
     * things at once. This method creates a button with a bunch
     * of premade values. Note that we are using Java reflection
     * here, to make an object based on what class type it has.
     * 
     * @param imageFile The image to use for the button.
     * 
     * @param parent The container inside which to put the button.
     * 
     * @param tracker This makes sure our button fully loads.
     * 
     * @param id A unique id for the button so the tracker knows it's there.
     * 
     * @param buttonType The type of button, we'll use reflection for making it.
     * 
     * @param bg Some buttons will go into groups where only one may be selected
     * at a time.
     * 
     * @param tooltip The mouse-over text for the button.
     * 
     * @return A fully constructed and initialized button with all the data
     * provided to it as arguments.
     */
    
    private AbstractButton initButton(   String imageFile, 
                                        Container parent, 
                                        MediaTracker tracker, 
                                        int id, 
                                        Class buttonType,
                                        ButtonGroup bg,
                                        String tooltip)
    {
        try 
        {
            // LOAD THE IMAGE AND MAKE AN ICON
            Image img = batchLoadImage(imageFile, tracker, id);
            ImageIcon ii = new ImageIcon(img);
            
            // HERE'S REFLECTION MAKING OUR OBJECT USING IT'S CLASS
            // NOTE THAT DOING IT LIKE THIS CALLS THE buttonType
            // CLASS' DEFAULT CONSTRUCTOR, SO WE MUST MAKE SURE IT HAS ONE
            AbstractButton createdButton;
            createdButton = (AbstractButton)buttonType.newInstance();
            
            // NOW SETUP OUR BUTTON FOR USE
            createdButton.setIcon(ii);
            createdButton.setToolTipText(tooltip);
            parent.add(createdButton);
            
            // INSETS ARE SPACING INSIDE THE BUTTON,
            // TOP LEFT RIGHT BOTTOM
            Insets buttonMargin = new Insets(   
                    BUTTON_INSET, BUTTON_INSET, BUTTON_INSET, BUTTON_INSET);
            createdButton.setMargin(buttonMargin);
            
            // ADD IT TO ITS BUTTON GROUP IF IT'S IN ONE
            if (bg != null)
            {
                bg.add(createdButton);
            }
            
            // AND RETURN THE SETUP BUTTON
            return createdButton;
        } 
        catch (InstantiationException | IllegalAccessException ex) 
        {
            // WE SHOULD NEVER GET THIS ERROR, BUT WE HAVE TO PUT
            // A TRY CATCH BECAUSE WE'RE USING REFLECTION TO DYNAMICALLY
            // CONSTRUCT OUR BUTTONS BY CLASS NAME
            Logger.getLogger(AnimatedPoseurGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        // THIS WOULD MEAN A FAILURE OF SOME SORT OCCURED
        return null;
    }
    
       /**
     * This is a helper method for making a button. It loads the image and sets
     * it as the button image. It then puts it in the panel.
     *
     * @param iconFilename Image for button
     * @param tooltip Tooltip for button
     * @param mt Used for batch loading of images
     * @param id Numeric id of image to help with batch loading
     * @param panel The container to place the button into
     *
     * @return The fully constructed button, ready for use.
     */
    private JButton initAnimationButton(String iconFilename, String tooltip, MediaTracker mt, int id, JPanel panel) {
        // LOAD THE IMAGE
        Toolkit tk = Toolkit.getDefaultToolkit();
        Image img = tk.getImage(BUTTON_IMAGES_PATH  + iconFilename);
        mt.addImage(img, id);

        // AND USE IT TO BUILD THE BUTTON
        ImageIcon ii = new ImageIcon(img);
        JButton button = new JButton(ii);
        button.setToolTipText(tooltip);

        // LET'S PUT A LITTLE BUFFER AROUND THE IMAGE AND THE EDGE OF THE BUTTON
        Insets insets = new Insets(2, 2, 2, 2);
        button.setMargin(insets);

        // PUT THE BUTTON IN THE CONTAINER
        panel.add(button);

        // AND SEND THE CONSTRUCTED BUTTON BACK
        return button;
    }

    /**
     * This method helps us load a bunch of images and ensure they are 
     * fully loaded when we want to use them.
     * 
     * @param imageFile The path and name of the image file to load.
     * 
     * @param tracker This will help ensure all the images are loaded.
     * 
     * @param id A unique identifier for each image in the tracker. It
     * will only wait for ids it knows about.
     * 
     * @return A constructed image that has been registered with the tracker.
     * Note that the image's data has not necessarily been fully loaded when 
     * this method ends.
     */
    private Image batchLoadImage(String imageFile, MediaTracker tracker, int id)
    {
        Toolkit tk = Toolkit.getDefaultToolkit();
        Image img = tk.getImage(imageFile);
        tracker.addImage(img, id);        
        return img;
    }

    /**
     * This method constructs and registers all the event handlers
     * for all the GUI controls.
     */
    private void initHandlers()
    {
        // THIS WILL HANDLE THE SCENARIO WHEN THE USER CLICKS ON
        // THE X BUTTON, WE'LL WANT A CUSTOM RESPONSE
        PoseurWindowHandler pwh = new PoseurWindowHandler();
        this.addWindowListener(pwh);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        // FILE TOOLBAR HANDLER
        NewAnimatedPoseurHandler naph = new NewAnimatedPoseurHandler();
        newAnimatedPoseurButton.addActionListener(naph);
        OpenAnimatedPoseurHandler oph = new OpenAnimatedPoseurHandler();
        openAnimatedPoseurButton.addActionListener(oph);
        SaveHandler sph = new SaveHandler();
        saveAnimatedPoseurButton.addActionListener(sph);
        SaveAsHandler saph = new SaveAsHandler();
        saveAsAnimatedPoseurButton.addActionListener(saph);
        ExitHandler eh = new ExitHandler();
        exitAnimatedPoseurButton.addActionListener(eh);
        
        // EDIT TOOLBAR HANDLER
        StartSelectionHandler startSH = new StartSelectionHandler();
        selectionButton.addActionListener(startSH);
        CutHandler cutEh = new CutHandler();
        cutButton.addActionListener(cutEh);
        CopyHandler copyEh = new CopyHandler();
        copyButton.addActionListener(copyEh);
        PasteHandler pasteEh = new PasteHandler();
        pasteButton.addActionListener(pasteEh);
        OutlineThicknessHandler oth = new OutlineThicknessHandler();
        lineStrokeSelectionComboBox.addItemListener(oth);
        AlphaHandler ah = new AlphaHandler();
        transparencySlider.addChangeListener(ah);
        MoveToBackHandler mtbh = new MoveToBackHandler();
        moveToBackButton.addActionListener(mtbh);
        MoveToFrontHandler mtfh = new MoveToFrontHandler();
        moveToFrontButton.addActionListener(mtfh);
        
        // ANIMATION STATE CONTROL HANDLER
        AnimationStateSelectionHandler ash = new AnimationStateSelectionHandler();
        animationStateList.addListSelectionListener(ash);
        DeleteAnimationStateHandler dah = new DeleteAnimationStateHandler();
        deleteAnimationStateButton.addActionListener(dah);
        DuplicateAnimationStateHandler duah = new DuplicateAnimationStateHandler();
        duplicateAnimationStateButton.addActionListener(duah);
        NewAnimationStateHandler nah = new NewAnimationStateHandler();
        newAnimationStateButton.addActionListener(nah);
        RenameAnimationStateHandler rah = new RenameAnimationStateHandler();
        renameAnimationStateButton.addActionListener(rah);
        
        // POSE CONTROL HANDLER
        PoseSelectionHandler psh = new PoseSelectionHandler();
        poseList.addListSelectionListener(psh);
        AddPoseHandler aph = new AddPoseHandler();
        addPoseButton.addActionListener(aph);
        DeletePoseHandler dph = new DeletePoseHandler();
        deletePoseButton.addActionListener(dph);
        DuplicatePoseHandler duph = new DuplicatePoseHandler();
        duplicatePoseButton.addActionListener(duph);
        DurationHandler dh = new DurationHandler();
        setDurationOfPoseButton.addActionListener(dh);
        EditPoseHandler eph = new EditPoseHandler();
        editPoseButton.addActionListener(eph);
        MoveDownHandler mdh = new MoveDownHandler();
        moveDownPoseButton.addActionListener(mdh);
        MoveUpHandler mup = new MoveUpHandler();
        moveUpPoseButton.addActionListener(mup);
        NewPoseHandler nph = new NewPoseHandler();
        newPoseButton.addActionListener(nph);
        
        // POSE CONTROL ADDTION HANDLER
        SaveAndExportImageHandler saeih = new SaveAndExportImageHandler();
        saveAndExportButton.addActionListener(saeih);
        ExitPoseEditorHandler epeh = new ExitPoseEditorHandler();
        exitButton.addActionListener(epeh);
        // SHAPE SELECTION HANDLERS
        RectangleSelectionHandler rsh = new RectangleSelectionHandler();
        rectToggleButton.addActionListener(rsh);
        EllipseSelectionHandler esh = new EllipseSelectionHandler();
        circleToggleButton.addActionListener(esh);
        LineSelectionHandler lsh = new LineSelectionHandler();
        lineToggleButton.addActionListener(lsh);
                
        // ZOOM HANDLERS
        ZoomInHandler zih = new ZoomInHandler();
        zoomInButton.addActionListener(zih);
        ZoomOutHandler zoh = new ZoomOutHandler();
        zoomOutButton.addActionListener(zoh);
        ChangePoseDimensionsHandler cpdh = new ChangePoseDimensionsHandler();
        dimensionsButton.addActionListener(cpdh);
        
        // ANIMATION VIEWER
        StartAnimationHandler sah = new StartAnimationHandler();
        startAnimationButton.addActionListener(sah);
        StopAnimationHandler stah = new StopAnimationHandler();
        pauseAnimationButton.addActionListener(stah);
        SpeedUpAnimationHandler suah = new SpeedUpAnimationHandler();
        speedUpAnimationButton.addActionListener(suah);
        SlowDownAnimationHandler sdah = new SlowDownAnimationHandler();
        slowDownAnimationButton.addActionListener(sdah);
        // COLOR CONTROL HANDLERS
        OutlineColorHandler acal = new OutlineColorHandler();
        outlineColorSelectionButton.addActionListener(acal);
        FillColorHandler fcal = new FillColorHandler();
        fillColorSelectionButton.addActionListener(fcal);
        ColorPalletHandler cph = new ColorPalletHandler();
        colorPallet.registerColorPalletHandler(cph);
        CustomColorHandler cch = new CustomColorHandler();
        customColorSelectorButton.addActionListener(cch);
        
        // CANVAS MOUSE HANDLERS
        PoseCanvasMouseHandler rsmh = new PoseCanvasMouseHandler();
        zoomableCanvas.addMouseListener(rsmh);
        zoomableCanvas.addMouseMotionListener(rsmh);
        
        // THIS HANDLER IS CALLED WHEN THE COMPONENT IS 
        // FIRST SIZED TO BE DISPLAYED. WE WISH TO CALCULATE
        // THE POSE AREA AT THAT TIME, SO WE'LL DO IT FOR
        // BOTH CANVASES
        PoseCanvasComponentHandler pcch = new PoseCanvasComponentHandler();
        trueCanvas.addComponentListener(pcch);
        zoomableCanvas.addComponentListener(pcch);
    }
       
    // METHODS FOR ENABLING AND DISABLING GROUPS OF CONTROLS.
    // THESE METHODS ALL SUPPOR THE updateMode METHOD. I'LL
    // SPARE YOU DESCRIPTIONS OF EACH ONE
    
    private void enableStartupFileControls()
    {
        // THESE BUTTONS ARE ALWAYS ENABLED
        newAnimatedPoseurButton.setEnabled(true);
        openAnimatedPoseurButton.setEnabled(true);
        exitAnimatedPoseurButton.setEnabled(true);
        
        // THESE BUTTONS START OFF AS DISABLED
        saveAnimatedPoseurButton.setEnabled(false);
        saveAsAnimatedPoseurButton.setEnabled(false);
    }
    
    private void enableSaveAsControl()
    {
        // THESE ARE ENABLED AS SOON AS WE START EDITING
        saveAsAnimatedPoseurButton.setEnabled(true);
    }    
 
    private void setEnabledEditControls(boolean isEnabled)
    {
        
        AnimatedPoseur singleton = AnimatedPoseur.getPoseur();
        PoseurStateManager state = singleton.getPoseStateManager();
        // THE SELECTION BUTTON NEEDS TO BE CHECKED SEPARATELY
        selectionButton.setEnabled(isEnabled);
        // THESE ARE EASY, JUST DO AS THEY'RE TOLD
        cutButton.setEnabled(state.isShapeSelected());
        copyButton.setEnabled(state.isShapeSelected());
                
        // WE ONLY WANT PASTE ENABLED IF THERE IS
        // SOMETHING ON THE CLIPBOARD
        
        pasteButton.setEnabled(state.isShapeOnClipboard());
        // WE ONLY ENABLE MOVE TO FRONT AND MOVE TO BACK WHEN USER SELECTED THE SHAPE
        // IT WILL BE DISABLED IF USER DESELECTED ANY SHAPES
        moveToBackButton.setEnabled(state.isShapeSelected());
        moveToFrontButton.setEnabled(state.isShapeSelected());
    }
    
    private void setEnabledColorControls(boolean isEnabled)
    {
        outlineColorSelectionButton.setEnabled(isEnabled);
        fillColorSelectionButton.setEnabled(isEnabled);
        customColorSelectorButton.setEnabled(isEnabled);
        colorPallet.setEnabled(isEnabled);
        outlineColorSelectionButton.setSelected(isEnabled);
        alphaLabel.setEnabled(isEnabled);
        transparencySlider.setEnabled(isEnabled);
    }

    private void setEnabledZoomControls(boolean isEnabled)
    {
        zoomInButton.setEnabled(isEnabled);
        zoomOutButton.setEnabled(isEnabled);
        zoomLabel.setEnabled(isEnabled);
        dimensionsButton.setEnabled(isEnabled);
    }
    
    private void setEnabledShapeControls(boolean isEnabled)
    {
        // INIT THEM AS USABLE OR NOT
        rectToggleButton.setEnabled(isEnabled);
        circleToggleButton.setEnabled(isEnabled);
        lineToggleButton.setEnabled(isEnabled);
        lineStrokeSelectionComboBox.setEnabled(isEnabled);
        
        // IF THEY'RE USABLE, MAKE THE TOGGLES UNSELECTED
        if (isEnabled)
        {
            shapeButtonGroup.clearSelection();
        }
    }

    private void setEnabledAnimationStateControls(boolean isEnabled)
    {
        //THOSE BUTTONS CAN ONLY BE LISTENED WHEN USER SELECT A ANIMATION STATE
        newAnimationStateButton.setEnabled(isEnabled);
        deleteAnimationStateButton.setEnabled(isEnabled);
        duplicateAnimationStateButton.setEnabled(isEnabled);
        renameAnimationStateButton.setEnabled(isEnabled);
    }
    
    private void enableNewAnimationStateButton()
    {
        newAnimationStateButton.setEnabled(true);
    }
    
    private void enableAddPoseButton()
    {
        addPoseButton.setEnabled(true);
    }
    
    private void enableNewPoseButton()
    {
        newPoseButton.setEnabled(true);
    }
    
    private void enableSaveAndExportControl(boolean isEnabled)
    {
        saveAndExportButton.setEnabled(isEnabled);
        exitButton.setEnabled(isEnabled);
    }
    
    private void setEnabledPoseControls(boolean isEnabled)
    {
        // THOSE BUTTONS CAN ONLY BE LISTENED WHEN USER SELECT A POSE
        newPoseButton.setEnabled(isEnabled);
        addPoseButton.setEnabled(isEnabled);
        deletePoseButton.setEnabled(isEnabled);
        duplicatePoseButton.setEnabled(isEnabled);
        editPoseButton.setEnabled(isEnabled);
        moveDownPoseButton.setEnabled(isEnabled);
        moveUpPoseButton.setEnabled(isEnabled);
        setDurationOfPoseButton.setEnabled(isEnabled);
    }
    
    private void setEnabledViewer(boolean isEnabled)
    {
        startAnimationButton.setEnabled(isEnabled);
        pauseAnimationButton.setEnabled(isEnabled);
        speedUpAnimationButton.setEnabled(isEnabled);
        slowDownAnimationButton.setEnabled(isEnabled);
    }
    private void selectCursor(int cursorToUse)
    {
        // AND NOW SWITCH TO A CROSSHAIRS CURSOR
        Cursor arrowCursor = Cursor.getPredefinedCursor(cursorToUse);
        setCursor(arrowCursor);    
    }

    /**
     * @param imagesPanel the imagesPanel to set
     */
    public void addLabelOnPanel(JLabel imagesLabel) {
        this.imagesLabel = imagesLabel;
        eastOfSouthOfNorthPanel.add(imagesLabel);
        eastOfSouthOfNorthPanel.repaint();
        updateMode();
    }
    
    
    /**
     * Helper method that clear the panel up
     */
    public void clearImagePanel()
    {
        eastOfSouthOfNorthPanel.repaint();
    }
    
       /**
     * We call this method every time something in the pose has changed
     * such that we are aware that the most recent Pose does not match
     * the file.
     */
    public void markPoseurChanged()
    {
        AnimatedPoseur singleton = AnimatedPoseur.getPoseur();
        PoseurFileManager fileManager = singleton.getPoseurFileManager();
        fileManager.markPoseurFileAsNotSaved();
    } 
    
    
    /*
     * This method simply helps us to refresh the animation list, no other effect.
     * 
     */
    public void refreshAnimationStateList(){animationStateList.setModel(animationStateListModel);}
    
    /**
     * This method simply helps use to refresh the pose list, no other effects.
     */
    public void refreshPoseList(){poseList.setModel(poseListModel);}
    
    /**
     * This method clear all Animation State up by one time, there will be blank after cleared.
     */
    public void clearAniamtionStateList(){
        animationStateListModel.clear();
        animationStateList.setModel(animationStateListModel);
    }
    
    /**
     * This method clear all pose list at one time, no other effect.
     */
    public void clearPoseList(){
        poseListModel.clear();
        poseList.setModel(poseListModel);
    }
    /**
     * This method helps use to clear the sprite arraylist and this serves for the addSprite method
     * becasue we only have one sprite at the renderer panel. So this is a list with one and only one 
     * sprite in it. Any time we change to render another sprite and we clear the list before
     */
    public void clearSprite()
    {
        spriteList.clear();
        eastSceneRenderingPanel.pauseScene();
        eastSceneRenderingPanel.repaint();
    }
    
    /**
     * Method that add a sprite into the list and this sprite will be rendered in the renderer panel
     * 
     * @param sprite The sprite that will be added
     */
    public void addSprite(Sprite sprite)
    {
        clearSprite();
        spriteList.add(sprite);
        eastSceneRenderingPanel.startScene();
        if (eastSceneRenderingPanel.getAnimateAndRenderTask().isScenePaused()) {
            eastSceneRenderingPanel.unpauseScene();
        }
        eastSceneRenderingPanel.setTimeScaler(1f);
        startAnimationButton.setEnabled(false);
        pauseAnimationButton.setEnabled(true);
        speedUpAnimationButton.setEnabled(true);
        slowDownAnimationButton.setEnabled(true);
    }
    
       /**
     * This method helps use to pause the Scene renderer is user click pause button
     */
    public void pauseTheRender()
    {
        eastSceneRenderingPanel.pauseScene();
        startAnimationButton.setEnabled(true);
        pauseAnimationButton.setEnabled(false);
        speedUpAnimationButton.setEnabled(false);
        slowDownAnimationButton.setEnabled(false);
    }
}