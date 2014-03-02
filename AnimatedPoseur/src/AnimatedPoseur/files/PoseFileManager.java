package AnimatedPoseur.files;

import AnimatedPoseur.AnimatedPoseur;
import static AnimatedPoseur.AnimatedPoseurSetting.*;
import AnimatedPoseur.gui.AnimatedPoseurGUI;
import AnimatedPoseur.renderer.Pose;
import AnimatedPoseur.renderer.PoseList;
import AnimatedPoseur.state.AnimatedPoseurPose;
import AnimatedPoseur.state.AnimatedPoseurSprite;
import AnimatedPoseur.state.AnimatedPoseurState;
import AnimatedPoseur.state.PoseurStateManager;
import java.awt.Image;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 * This class provides all the file servicing for the Animated Poseur application.
 * This means it directs all operations regarding loading, creating, and opening
 * saving files, Note that it employs use of PoseurIO for the actual file work, 
 * this class manages when to actually read and write from/to files, prompting
 * the user when necessary for file names and validation on actions.
 * 
 * @author  Richard McKenna
 *          Debugging Enterprises
 * @version 1.0
 */
public class PoseFileManager 
{
    // WE'LL STORE THE FILE CURRENTLY BEING WORKED ON
    // AND THE NAME OF THE FILE
    // .pose file we choose
    private File currentFile;
    // .png image type stored 
    private Image currentImage;
    // pose name , ex, BOX_MAN_WALKING_UP_1
    private String currentPoseName;
    // file name, pose name + ".pose", EX, BOX_MAN_WALKING_UP_1.pose
    private String currentFileName;
    // the sprite we opened right now
    private AnimatedPoseurSprite sprite;
    // the pose we selected, this will be loaded with .pose file
    private AnimatedPoseurPose pose;
    public static int poseSelected;
    
    
    
//     WE WANT TO KEEP TRACK OF WHEN SOMETHING HAS NOT BEEN SAVED
//    private boolean saved;
    
    // THIS GUY KNOWS HOW TO READ, WRITE, AND EXPORT POESS
    private PoseIO poseIO;
    private boolean saved;
    
    /**
     * This default constructor starts the program without a
     * pose file being edited.
     */
    public PoseFileManager()
    {
        // NOTHING YET
        currentFile = null;
        currentImage = null;
        currentPoseName = null;
        poseSelected = -1;
        sprite = null;
        pose = null;
        poseIO = new PoseIO();
        saved = true;
    }
    
    /**
     * This method starts the process of editing a new pose. If
     * a pose is already being edited, it will prompt the user
     * to save it first.
     */
    public boolean requestNewPose()
    {
        // ASK THE USER TO MAKE SURE THEY WANT TO GO AHEAD WITH IT
        AnimatedPoseurGUI gui = AnimatedPoseur.getPoseur().getGUI();
        int selection = JOptionPane.showOptionDialog(   gui, 
                PROMPT_TO_NEW_POSE_TEXT,
                PROMPT_TO_NEW_POSE__TITLE_TEXT, 
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, null, null);
        
        // IF THE USER CLICKED OK, THEN EXPORT THE POSE
        if (selection == JOptionPane.OK_OPTION)
        {
                PoseurStateManager poseurStateManager = AnimatedPoseur.getPoseur().getPoseStateManager();
                poseurStateManager.setState(AnimatedPoseurState.NEW_POSE_SELECT_SHAPE_STATE);
                poseurStateManager.resetState();
                
                return true;
        }
        return false;
    }
    
    /**
     * This method lets the user open a pose saved
     * to a file. It will also make sure data for the
     * current pose is not lost.
     */
    public boolean requestAddPose()
    {
            // WE'LL NEED THE GUI
            AnimatedPoseur singleton = AnimatedPoseur.getPoseur();
            PoseurFileManager poseurFileManager = singleton.getPoseurFileManager();
            AnimatedPoseurGUI gui = singleton.getGUI();
            PoseurStateManager poseurStateManager = singleton.getPoseStateManager();
            // AND NOW ASK THE USER FOR THE POSE TO OPEN
            String poseurName = poseurFileManager.getCurrentPoseurName();
            JFileChooser poseFileChooser = new JFileChooser(APP_DATA_PATH + poseurName + POSE_FOLDER);
            int buttonPressed = poseFileChooser.showOpenDialog(gui);

            // ONLY OPEN A NEW FILE IF THE USER SAYS OK
            if (buttonPressed == JFileChooser.APPROVE_OPTION)
            {
                currentFile = poseFileChooser.getSelectedFile();
                currentFileName = currentFile.getName();
                currentPoseName = currentFileName.substring(0, currentFileName.indexOf("."));
                sprite = poseurFileManager.getPoseur();
                int stateIndex = poseurFileManager.animationStateSelected;
                String stateName = sprite.getAnimationStateNames().get(stateIndex);
                // WE NEED TO ADD TO BOTH POSELIST AND ARRAYLIST
                // GET THREE HASHTABLE
                HashMap<Integer, Image> spriteImages = sprite.getSpriteType().getSpriteImages();
                HashMap<Integer, String> imagesName = sprite.getSpriteType().getImagesName();
                
                HashMap<String, PoseList> poseListHash = sprite.getSpriteType().getAnimationsToRender();
                HashMap<String, ArrayList<Integer>> posesImageListHash = sprite.getSpriteType().getPosesImageList();
                PoseList poseList = poseListHash.get(stateName);
                ArrayList<Integer> poseImagesIDs = posesImageListHash.get(stateName);
                
                pose = poseIO.loadPoseForPoseur(currentFile.getAbsolutePath());
                int i;
                for(i=1;i<=imagesName.size();i++)
                {
                    if(imagesName.get(i).equals(currentPoseName))
                            {
                                break;
                            }
                }
                currentImage = spriteImages.get(i);
                poseImagesIDs.add(i);
                if(poseList == null)
                {
                    poseList = new PoseList();
                }
                poseList.addPose(i, 5);
                DefaultListModel poseListModel = gui.getPoseListModel();
                Image image = currentImage.getScaledInstance(100,100,Image.SCALE_DEFAULT);
                ImageIcon icon = new ImageIcon(image);
                poseListModel.addElement(icon);
                gui.getPoseList().setSelectedIndex(poseListModel.getSize()-1);
                poseurStateManager.setState(AnimatedPoseurState.POSEUR_POSE_SELECTED_STATE);
                return true;
            }
        poseurStateManager.setState(AnimatedPoseurState.SELECT_POSEUR_POSE_STATE);
        return false;      
    }
    
    /**
     * When user request to delete pose from pose list, this method response
     * to the delete button clicked, if clicked that we need to change corresponding 
     * data in structure, if not we need to notice user that any error happens
     * 
     * @return true if deleted
     */
    public boolean requestToDeletePose()
    {
        // WE'LL NEED THE GUI
        AnimatedPoseur singleton = AnimatedPoseur.getPoseur();
        PoseurFileManager poseurFileManager = singleton.getPoseurFileManager();
        PoseFileManager poseFileManager = singleton.getPoseFileManager();
        AnimatedPoseurGUI gui = singleton.getGUI();
        PoseurStateManager poseurStateManager = singleton.getPoseStateManager();
        int selection =JOptionPane.showOptionDialog(gui, 
                    PROMPT_TO_DELETE_POSE_TEXT, PROMPT_TO_DELETE_POSE_TITLE_TEXT, 
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, 
                    null, null, null);
             // IF THE USER SAID YES, THEN SAVE BEFORE MOVING ON
        if (selection == JOptionPane.YES_OPTION)
        {
            sprite = poseurFileManager.getPoseur();
            // NOW WE NEED EXTRACT LIST SELECTION INDEX AND TRY TO GET WHAT WE NEED
            // IN ALL DATAT STRUCTURES
            int indexOfState = poseurFileManager.animationStateSelected;
            poseSelected = gui.getSelectedPose();
            if (indexOfState == -1 || poseSelected == -1) 
            {return false;}
            // WE NEED EXTRACT ANIMATEDPOSEURPOSE AND IMAGE PATH
            // GET THE ANIMATION STATE NAME WE SELECTED
            String stateName = sprite.getAnimationStateNames().get(indexOfState);
            // ALL DELETIONS WILL CHAGNE CONTENT OF INNER TWO HASH TABLES
            HashMap<String,PoseList> animationToRenderHash = sprite.getSpriteType().getAnimationsToRender();
            HashMap<String, ArrayList<Integer>> poseImagesListHash = sprite.getSpriteType().getPosesImageList();
            // GET INNER POSELIST AND ARRAYLIST OF HASHMAPS
            PoseList animationRenderList = animationToRenderHash.get(stateName);
            ArrayList<Integer> poseImagesList = poseImagesListHash.get(stateName);
            // REMOVE 
            animationRenderList.removePose(poseSelected);
            poseImagesList.remove(poseSelected);
            // SET IMAGE PANEL AND CANVAS TO NULL
            poseFileManager = null;
            // DONE!
            // NOW WE NEED CHANGE THE OUTLOOK OF THE POSE LIST AND REFRESH IT TO MAKE A NEW LIST WITHOUT
            // THE POSE WE REMOVVE, FIRST WE GET HASH MAP OF ALL IMAGES/POSES NAMES
            HashMap<Integer,Image> imagesHash = sprite.getSpriteType().getSpriteImages();
            // THE ARRAYLIST WE NEED STORE ALL NAMES
            ArrayList<ImageIcon> displayedPoses = new ArrayList();
            
            for(int i =0;i<poseImagesList.size();i++)
            {
                Image currentImage = imagesHash.get(poseImagesList.get(i)).getScaledInstance(100,100,Image.SCALE_DEFAULT);
                ImageIcon icon = new ImageIcon(currentImage);
                displayedPoses.add(icon);
            }
            
            DefaultListModel poseListModel = new DefaultListModel();
            for(int i=0; i<displayedPoses.size();i++)
            {
                poseListModel.addElement(displayedPoses.get(i));
            }
            gui.setPoseListModel(poseListModel);
            poseurStateManager.setState(AnimatedPoseurState.SELECT_POSEUR_POSE_STATE);
        }
        else if(selection == JOptionPane.CANCEL_OPTION)
        {
            return false;
        }
        return true;
    }
    
    
    /**
     * This method responds to selection of pose list, after selection. the pose
     * data will be loaded into structure for future use.Like deletion, add, move position,etc
     * 
     * @return true is selected 
     * 
     */
    public boolean selectPose()
    {
        // WHEN WE CLICKED ON THE POSE THAT WE SELECTED, SOMETHING WILL CHANGE IN OUR POSE FILE MANAGER 
        // 1, FIRST, THE IMAGE WILL BE DYNAMICALLY FETCHED INTO IMAGE PANEL WITH CERTAIN SIZE 128*128
        // 2, THE POSE FILE IS DYNAMICALLY LOADED INTO OUR INSTANCE VARIABLE BUT WE DIDN'T CHANGE ANY STATE 
        //    THE POSE IS READY TO USE BUT WE WON'T CHANGE OUTLOOK OF GUI, WHICH MEANS CANVAS DOESN'T HAVE
        //    ANY CHANGE UNLESS WE CLICK THE EDIT BUTTON.
        
        // ACCESS ALL NECESSARY DATA
        AnimatedPoseur singleton = AnimatedPoseur.getPoseur();
        AnimatedPoseurGUI gui = singleton.getGUI();
        PoseurStateManager poseurStateManager = singleton.getPoseStateManager();
        PoseurFileManager poseurFileManager = singleton.getPoseurFileManager();
        // EVERY TIME WE CLEAR OUR POSE LIST 
        
        // GET TWO INDEXES THAT WE CHOOSE
        int stateSelected = poseurFileManager.animationStateSelected;
        poseSelected = gui.getSelectedPose();
        
        if(poseSelected == -1) 
        {
            return false;
        }
        // GET THE SPRITE, WE MIGHT USE IT
        sprite = poseurFileManager.getPoseur();
        // ALSO GET THE NAME OF ANIMATION STATE, WE MIGHT NEED IT!
        String stateName = sprite.getAnimationStateNames().get(stateSelected);
        // FIRST WE NEED TO TRACK THE ID AND THE POSE LIST OF SELECTED POSE
        // GET TWO HASH FROM SPRITE
        HashMap<String, ArrayList<Integer>> posesImageList = sprite.getSpriteType().getPosesImageList();
        // THEN WE EXTRACT THE SPECIFIC ID FOR CURRENT SELECT POSE
        ArrayList<Integer> allIDs = posesImageList.get(stateName);
        int id = allIDs.get(poseSelected);
        // IF WE KNOW THIS ID, THEN WE CAN EXTRACT CORRESPONDING INFORMATION 
        // LIKE IMAGE, IMAGENAME AND ANIMATEDPOSEURPOSE
        // GET THREE HASH MAP
        HashMap<Integer, Image> spriteImages = sprite.getSpriteType().getSpriteImages();
        HashMap<Integer,String> imagesName = sprite.getSpriteType().getImagesName();
        HashMap<Integer, AnimatedPoseurPose> spritePoses = sprite.getSpriteType().getSpritePoses();
        String spriteName = poseurFileManager.getCurrentPoseurName();
        currentImage = spriteImages.get(id).getScaledInstance(DEFAULT_IMAGE_WIDTH,DEFAULT_IMAGE_HEIGHT,Image.SCALE_DEFAULT);
        
        currentPoseName = imagesName.get(id);
        currentFile = new File(APP_DATA_PATH + spriteName + FILE_SEPARATOR + POSE_FOLDER + FILE_SEPARATOR + currentPoseName + POSE_FILE_EXTENSION);

        pose = spritePoses.get(id);
        ImageIcon icon = new ImageIcon(currentImage);
        gui.getImagesLabel().setIcon(icon);
        gui.addLabelOnPanel(gui.getImagesLabel());
        poseurStateManager.setState(AnimatedPoseurState.POSEUR_POSE_SELECTED_STATE);
        return true;
    }
    
    /**
     *  Duplicate the current pose in pose list, we add an identical copy in structure.
     * 
     * @return true if duplicated
     */
    public boolean requestToDuplicate()
    {
        // GET A BUNCH OF THINGS WE NEED TO USE
        AnimatedPoseur singleton = AnimatedPoseur.getPoseur();
        AnimatedPoseurGUI gui = singleton.getGUI();
        PoseurStateManager poseurStateManager = singleton.getPoseStateManager();
        PoseurFileManager poseurFileManager = singleton.getPoseurFileManager();
        int selection =JOptionPane.showOptionDialog(gui, 
                    PROMPT_TO_DUPLICATE_POSE_TEXT, PROMPT_TO_DUPLICATE_POSE_TITLE_TEXT, 
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, 
                    null, null, null);
        // IF THE USER SAID YES, THEN MOVING ON
        if (selection == JOptionPane.YES_OPTION)
        {
            // AT VERY BEGINING WE ALWAYS GET TWO INDEXES FOR FURTURAL USE
            int stateSelected = poseurFileManager.animationStateSelected;
            poseSelected = gui.getSelectedPose();
            // GET POSE
            pose = poseurStateManager.getPose();
            // GET POSE NAME
            sprite = poseurFileManager.getPoseur();
            String stateName = sprite.getAnimationStateNames().get(stateSelected);
            HashMap<String,PoseList> animationToRender = sprite.getSpriteType().getAnimationsToRender();
            HashMap<String,ArrayList<Integer>> posesImageList = sprite.getSpriteType().getPosesImageList();
            // GET THE ARRAYLIST, THEN GET THE ID OF THE SELECTED POSE
            // USE ID TO TRACK THE NAME OF POSE
            int poseID = posesImageList.get(stateName).get(poseSelected);
            // WE MUST NOT GENERATE THE SAME NAME, OTHERWISE WE WILL MEET NO ARCURACY OF THE DATA WE EXTRACT
            // FROM HASH MAP
            // WE NEED TO COPY THIS INTO 
            // 1, ID ARRAYLIST
            // 2, POSE LIST
            ArrayList<Integer> imagesIDs = posesImageList.get(stateName);
            int copyNumber = imagesIDs.get(poseSelected);
            imagesIDs.add(copyNumber);
            // DONE OF ARRAYLIST, THEN PSOE LIST, WE HAVE TO CONSIDER DURATION IN THIS, AND ALSO ID
            PoseList poseList = animationToRender.get(stateName);
            poseList.duplicate(poseSelected);
            // DONE WITH ADDING ALL NECESSARY DATA
            // NOW WE NEED CHANGE THE LOOKING OF THE POSE LIST
            HashMap<Integer, Image> spriteImages = sprite.getSpriteType().getSpriteImages();
            Image image = spriteImages.get(poseID).getScaledInstance(100,100,Image.SCALE_DEFAULT);
            ImageIcon icon = new ImageIcon(image);
            gui.getPoseListModel().addElement(icon);
            gui.setPoseListModel(gui.getPoseListModel());
            poseurStateManager.setState(AnimatedPoseurState.SELECT_POSEUR_POSE_STATE);
        }
        else if(selection == JOptionPane.CANCEL_OPTION)
        {
            return false;
        }
        return true;
    }
    
    /**
     * Reset the duration to the current selected pose, the duration will be changed
     * if successful input an integer.
     * 
     * @return true if set
     */
    public boolean requestSetDuration()
    {
        // GET A BUNCH OF THINGS WE NEED TO USE
        AnimatedPoseur singleton = AnimatedPoseur.getPoseur();
        AnimatedPoseurGUI gui = singleton.getGUI();
        PoseurStateManager poseurStateManager = singleton.getPoseStateManager();
        PoseurFileManager poseurFileManager = singleton.getPoseurFileManager();
        String stateName = poseurFileManager.getPoseur().getAnimationStateNames().get(poseurFileManager.animationStateSelected);
        int preDur = poseurFileManager.getPoseur().getSpriteType().getAnimationsToRender().get(stateName).getPose(poseSelected).getDurationInFrames();
        int selection =JOptionPane.showOptionDialog(gui, 
                    PROMPT_TO_SET_DURATION  + preDur , PROMPT_TO_SET_DURATION_TITLE, 
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, 
                    null, null, null);
        // IF THE USER SAID YES, THEN MOVING ON
        if (selection == JOptionPane.YES_OPTION)
        {
                String duration = JOptionPane.showInputDialog(
                        gui,
                        DURATION_TIME_REQUEST_TEXT,
                        DURATION_TIME_REQUEST_TITLE_TEXT,
                        JOptionPane.QUESTION_MESSAGE);
                int dur;
                // IF THE USER CANCELLED, THEN WE'LL GET A duration
                // OF NULL, SO LET'S MAKE SURE THE USER REALLY
                // WANTS TO DO THIS ACTION BEFORE MOVING ON
                try {
                    dur = Integer.parseInt(duration);
                }
                catch(NumberFormatException nFE){
                     JOptionPane.showMessageDialog(
                                    gui,
                                    INVALID_DURATION_ENTRY_TEXT,
                                    INVALID_DURATION_TITLE_TEXT,
                                    JOptionPane.ERROR_MESSAGE);
                                    poseurStateManager.setState(AnimatedPoseurState.POSEUR_POSE_SELECTED_STATE);
                     return false;
                }
                // AT VERY BEGINING WE ALWAYS GET TWO INDEXES FOR FURTURAL USE
                poseSelected = gui.getSelectedPose();
                // GET POSE
                pose = poseurStateManager.getPose();
                // GET POSE NAME
                sprite = poseurFileManager.getPoseur();
                HashMap<String,PoseList> animationToRender = sprite.getSpriteType().getAnimationsToRender();
                // GET THE ID WE SELECTED
                // WE NEED FIRST GET POSE NAME WE SELECTED
                PoseList poseList = animationToRender.get(stateName);
                Iterator<Pose> allPoses = poseList.getPoseListIterator();
                // WE NEED TO LOOK FOR THE ONE WE NEED
                Pose poseNode = null;
                for(int i =0; i<poseList.size(); i++)
                {
                    poseNode = allPoses.next();
                    if(i==poseSelected)
                    {break;}
                }
                poseNode.setDurationInFrames(dur);
                 JOptionPane.showMessageDialog(
                                    gui,
                                    SAVE_POSE_DURATION_TEXT + " " + dur,
                                    SAVE_POSE_DURATION_TITLE_TEXT,
                                    JOptionPane.QUESTION_MESSAGE);
                                    poseurStateManager.setState(AnimatedPoseurState.POSEUR_POSE_SELECTED_STATE);
        }
        else if(selection == JOptionPane.CANCEL_OPTION)
        {
            return false;
        }
        return true;
    }
    
    /**
     * This method listen to the move up button clicked if pose is selected, and this button
     * is clicked after, the selected pose will be moved up
     * 
     * @return true if moved
     */
    public boolean requestToMoveUp()
    {
         // GET A BUNCH OF THINGS WE NEED TO USE
        AnimatedPoseur singleton = AnimatedPoseur.getPoseur();
        AnimatedPoseurGUI gui = singleton.getGUI();
        PoseurStateManager poseurStateManager = singleton.getPoseStateManager();
        PoseurFileManager poseurFileManager = singleton.getPoseurFileManager();
        // AT VERY BEGINING WE ALWAYS GET TWO 6INDEXES FOR FURTURAL USE
        int stateSelected = gui.getSelectedAnimationState();
        poseSelected = gui.getSelectedPose();
        // GET POSE
        pose = poseurStateManager.getPose();
        // GET POSE NAME
        sprite = poseurFileManager.getPoseur();
        String stateName = sprite.getAnimationStateNames().get(stateSelected);
            
        HashMap<String,PoseList> animationToRender = sprite.getSpriteType().getAnimationsToRender();
        HashMap<String,ArrayList<Integer>> posesImageList = sprite.getSpriteType().getPosesImageList();
        // GET THE ID WE SELECTED
        ArrayList<Integer> posesSequence = posesImageList.get(stateName);
        if(poseSelected == 0)
        {
            JOptionPane.showMessageDialog(
                            gui,
                            REACH_THE_TOP_LIST_TEXT,
                            REACH_THE_TOP_LIST_TITLE_TEXT,
                            JOptionPane.ERROR_MESSAGE);
                            poseurStateManager.setState(AnimatedPoseurState.POSEUR_POSE_SELECTED_STATE);
             return false;
        }
        // SWAP IN ARRAYLIST
        int swappedIndex = poseSelected -1;
        swapInArrayList(posesSequence,swappedIndex,poseSelected);

        PoseList list = animationToRender.get(stateName);
        // SWAP TWO POSE FOR RENDERING
        list.swapTwoPoses(swappedIndex,poseSelected);
        DefaultListModel poseListModel = gui.getPoseListModel();
        // SWAP TWO STRING IN POSE LIST
        swapTwoImageIconInListModel(poseListModel,swappedIndex,poseSelected);
        gui.setPoseListModel(poseListModel);
        gui.getPoseList().setSelectedIndex(swappedIndex);
        poseurStateManager.setState(AnimatedPoseurState.POSEUR_POSE_SELECTED_STATE);
        return true;
    }
    
    /**
     *  This method is similar to move up, instead, it will move down one position
     * 
     * @return true if moved
     */
    public boolean requestToMoveDown()
    {
         // GET A BUNCH OF THINGS WE NEED TO USE
        AnimatedPoseur singleton = AnimatedPoseur.getPoseur();
        AnimatedPoseurGUI gui = singleton.getGUI();
        PoseurStateManager poseurStateManager = singleton.getPoseStateManager();
        PoseurFileManager poseurFileManager = singleton.getPoseurFileManager();
        // AT VERY BEGINING WE ALWAYS GET TWO INDEXES FOR FURTURAL USE
        int stateSelected = gui.getSelectedAnimationState();
        poseSelected = gui.getSelectedPose();
        // GET POSE
        pose = poseurStateManager.getPose();
        // GET POSE NAME
        sprite = poseurFileManager.getPoseur();
        String stateName = sprite.getAnimationStateNames().get(stateSelected);
        HashMap<String,PoseList> animationToRender = sprite.getSpriteType().getAnimationsToRender();
        HashMap<String,ArrayList<Integer>> posesImageList = sprite.getSpriteType().getPosesImageList();
        // GET THE ID WE SELECTED
        ArrayList<Integer> posesSequence = posesImageList.get(stateName);
        
        if(poseSelected == posesSequence.size() - 1)
        {
            JOptionPane.showMessageDialog(
                            gui,
                            REACH_THE_BOTTEM_LIST_TEXT,
                            REACH_THE_BOTTEM_LIST_TITLE_TEXT,
                            JOptionPane.ERROR_MESSAGE);
                            poseurStateManager.setState(AnimatedPoseurState.POSEUR_POSE_SELECTED_STATE);
             return false;
        }
        // SWAP IN INTEGER SEQUENCE
        int swappedIndex = poseSelected + 1;
        swapInArrayList(posesSequence,poseSelected,swappedIndex);
        PoseList list = animationToRender.get(stateName);
        // SWAP POSE FOR RENDERING
        list.swapTwoPoses(poseSelected,swappedIndex);
        DefaultListModel poseListModel = gui.getPoseListModel();
        // SWAP IN OUTLOOK OF POSE LIST
        swapTwoImageIconInListModel(poseListModel,poseSelected,swappedIndex);
        gui.setPoseListModel(poseListModel);
        gui.getPoseList().setSelectedIndex(swappedIndex);
        poseurStateManager.setState(AnimatedPoseurState.POSEUR_POSE_SELECTED_STATE);
        return true;
    }
    
    /**
     * This pose will help us to edit current view of the pose
     * 
     * @return true if enter the pose editor
     */
    public boolean requestEditPose()
    {
        // GET A BUNCH OF THINGS WE NEED TO USE
        AnimatedPoseur singleton = AnimatedPoseur.getPoseur();
        AnimatedPoseurGUI gui = singleton.getGUI();
        PoseurStateManager poseurStateManager = singleton.getPoseStateManager();
        PoseurFileManager poseurFileManager = singleton.getPoseurFileManager();
        // AT VERY BEGINING WE ALWAYS GET TWO INDEXES FOR FURTURAL USE
        int stateSelected = gui.getSelectedAnimationState();
        poseSelected = gui.getSelectedPose();
        if(stateSelected == -1||poseSelected == -1) 
        {
            return false;
        }
        // GET POSE
        pose = poseurStateManager.getPose();
        // GET POSE NAME
        sprite = poseurFileManager.getPoseur();
        String poseurName = poseurFileManager.getCurrentPoseurName();
        String stateName = sprite.getAnimationStateNames().get(stateSelected);;
        HashMap<Integer, String> imagesName = sprite.getSpriteType().getImagesName();
        HashMap<String,ArrayList<Integer>> posesImageList = sprite.getSpriteType().getPosesImageList();
        // GET THE ID WE SELECTED
        ArrayList<Integer> posesSequence = posesImageList.get(stateName);
        int poseID = posesSequence.get(poseSelected);
        String absolutePath = APP_DATA_PATH + poseurName + POSE_FOLDER + FILE_SEPARATOR + imagesName.get(poseID) + POSE_FILE_EXTENSION;
        
        poseIO.loadPose(absolutePath);
        poseurStateManager.setState(AnimatedPoseurState.POSE_EDITOR_SELECT_SHAPE_STATE);
        return true;
    }

    /**
     * This method will export the current pose to an image file.
     */
    public boolean requestSaveAndExportPose()
    {
         // GET A BUNCH OF THINGS WE NEED TO USE
        AnimatedPoseur singleton = AnimatedPoseur.getPoseur();
        AnimatedPoseurGUI gui = singleton.getGUI();
        PoseurStateManager poseurStateManager = singleton.getPoseStateManager();
        PoseurFileManager poseurFileManager = singleton.getPoseurFileManager();
        if (poseurStateManager.getMode() == AnimatedPoseurState.NEW_POSE_SELECT_SHAPE_STATE||
               poseurStateManager.getMode() == AnimatedPoseurState.NEW_POSE_CREATE_SHAPE_STATE||
                poseurStateManager.getMode() == AnimatedPoseurState.NEW_POSE_DRAG_SHAPE_STATE||
                poseurStateManager.getMode() == AnimatedPoseurState.NEW_POSE_COMPLETE_SHAPE_STATE||
                poseurStateManager.getMode() == AnimatedPoseurState.NEW_POSE_SHAPE_SELECTED_STATE
        )
        {         
            Boolean success = true;
            do{
            String fileName = JOptionPane.showInputDialog(
                gui,
                POSE_NAME_REQUEST_TEXT,
                POSE_NAME_REQUEST_TITLE_TEXT,
                JOptionPane.QUESTION_MESSAGE);
                if ((fileName != null)
                     &&
                    (fileName.length() > 0))
                {
                    int selection = JOptionPane.showOptionDialog(   gui, 
                    EXPORT_POSE_TEXT + fileName + POSE_FILE_EXTENSION,
                    EXPORT_POSE_TITLE_TEXT, 
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
                    null, null, null);
                     
                     if (selection == JOptionPane.OK_OPTION)
                     {
                            String folderName = poseurFileManager.getCurrentPoseurName();
                            File dir = new File(APP_DATA_PATH + folderName + FILE_SEPARATOR + POSE_FOLDER);
                            currentFileName = fileName + POSE_FILE_EXTENSION;
                            currentPoseName = fileName;
                            currentFile = new File(dir,currentFileName);
                            if(!currentFile.exists())
                            {
                                AnimatedPoseurPose poseTemp = getPoseIO().savePose(currentFile);
                                Image image = getPoseIO().exportPose(fileName);
                                HashMap<Integer, AnimatedPoseurPose> poseHash = poseurFileManager.getPoseur().getSpriteType().getSpritePoses();
                                HashMap<Integer, Image> imageHash = poseurFileManager.getPoseur().getSpriteType().getSpriteImages();
                                HashMap<Integer, String> imageNameHash = poseurFileManager.getPoseur().getSpriteType().getImagesName();
                                int incSize = poseHash.size() + 1;
                                // Store them 
                                poseHash.put(incSize, poseTemp);
                                imageHash.put(incSize, image);
                                imageNameHash.put(incSize, currentPoseName);
                                 // Track the name and file name
                                saved = true;
                            }
                            else
                            {
                                JOptionPane.showMessageDialog(
                                gui,
                                SAME_NAME_EXISTED_TEXT,
                                SAME_NAME_EXISTED_TITLE_TEXT,
                                JOptionPane.ERROR_MESSAGE);
                                success = false;
                            }
                      }
                     else if(selection == JOptionPane.CANCEL_OPTION){
                         return false;
                     }
                }
                }while(!success);
        }
        
        
        //IF WE EXPORT FROM POSE EDITOR
        else if(poseurStateManager.getMode() == AnimatedPoseurState.POSE_EDITOR_SHAPE_SELECTED_STATE||
                poseurStateManager.getMode() == AnimatedPoseurState.POSE_EDITOR_COMPLETE_SHAPE_STATE||
                poseurStateManager.getMode() == AnimatedPoseurState.POSE_EDITOR_DRAG_SHAPE_STATE||
                poseurStateManager.getMode() == AnimatedPoseurState.POSE_EDITOR_SELECT_SHAPE_STATE||
                poseurStateManager.getMode() == AnimatedPoseurState.POSE_EDITOR_CREATE_SHAPE_STATE)
        {
            // AT VERY BEGINING WE ALWAYS GET TWO INDEXES FOR FURTURAL USE
            int stateSelected = gui.getSelectedAnimationState();
            poseSelected = gui.getSelectedPose();
                    // GET POSE
            pose = poseurStateManager.getPose();
             // GET POSE NAME
            sprite = poseurFileManager.getPoseur();
            String stateName = sprite.getAnimationStateNames().get(stateSelected);
            
            HashMap<Integer, Image> spriteImages = sprite.getSpriteType().getSpriteImages();
            HashMap<Integer, AnimatedPoseurPose> spritePoses = sprite.getSpriteType().getSpritePoses();
            HashMap<Integer, String> imagesName = sprite.getSpriteType().getImagesName();
            HashMap<String,PoseList> animationToRender = sprite.getSpriteType().getAnimationsToRender();
            HashMap<String,ArrayList<Integer>> posesImageList = sprite.getSpriteType().getPosesImageList();
            
            ArrayList<Integer> IDs = posesImageList.get(stateName);
            
            int selection = JOptionPane.showOptionDialog(   gui, 
                    EXIT_POSE_EDITOR_REQUEST_TEXT,
                    EXIT_POSE_EDITOR_REQUEST_TITLE_TEXT, 
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
                    null, null, null);
            // IF USER SAYS YES
            if (selection == JOptionPane.OK_OPTION)
            {
                boolean success = true;
                do{
                        String newPoseName = JOptionPane.showInputDialog(
                                        gui,
                                        POSE_NAME_REQUEST_TEXT,
                                        POSE_NAME_REQUEST_TITLE_TEXT,
                                        JOptionPane.QUESTION_MESSAGE);
                        if ((newPoseName != null)
                             &&
                           (newPoseName.length() > 0))
                        {
                            String folderName = poseurFileManager.getCurrentPoseurName();
                            File dir = new File(APP_DATA_PATH + folderName + FILE_SEPARATOR + POSE_FOLDER);
                            currentFileName = newPoseName + POSE_FILE_EXTENSION;
                            currentPoseName = newPoseName;
                            currentFile = new File(dir,currentFileName);
                            if(!currentFile.exists())
                            {
                                pose = getPoseIO().savePose(currentFile);
                                poseurStateManager.setPose(pose);
                                currentImage = getPoseIO().exportPose(newPoseName);
                                // WRITE IT INTO 5 HASHMAP
                                int newID = spriteImages.size() + 1;
                                spriteImages.put(newID, currentImage);
                                spritePoses.put(newID, pose);
                                imagesName.put(newID, newPoseName);
                                PoseList poseList = animationToRender.get(stateName);
                                Iterator<Pose> poseListIterator = poseList.getPoseListIterator();
                                Pose poseNode = null;
                                for(int i=0 ; i<poseList.size(); i++)
                                {
                                    poseNode = poseListIterator.next();
                                    if (i == poseSelected)
                                    {
                                        break;
                                    }
                                }
                                poseNode.setImageID(newID - 1);
                                // DONE OF SET ID
                                IDs.add(poseSelected, newID);
                                IDs.remove(poseSelected + 1);
                                
                                saved = true;
                                DefaultListModel poseListModel = gui.getPoseListModel();
                                Image image = currentImage.getScaledInstance(100,100,Image.SCALE_DEFAULT);
                                ImageIcon icon = new ImageIcon(image);
                                poseListModel.add(poseSelected, icon);
                                success = true;
                                poseurStateManager.resetState();
                            }
                            else
                            {
                                JOptionPane.showMessageDialog(
                                gui,
                                SAME_NAME_EXISTED_TEXT,
                                SAME_NAME_EXISTED_TITLE_TEXT,
                                JOptionPane.ERROR_MESSAGE);
                                success = false;
                            }
                        }
                }while(!success);
            }
            else if (selection == JOptionPane.CANCEL_OPTION)
            {
                return false;
            }
        }
        return true;
    }
    
    /**
     * This method will exit the application, making sure the user
     * doesn't lose any data first.
     */
    public void requestExit()
    {
        AnimatedPoseur singleton = AnimatedPoseur.getPoseur();
        AnimatedPoseurGUI gui = singleton.getGUI();
        PoseurStateManager poseurStateManager = singleton.getPoseStateManager();
         // WE MAY HAVE TO SAVE CURRENT WORK
        boolean continueToExit = true;
        if (!saved)
        {
           // THE USER CAN OPT OUT HERE
           continueToExit = requestSaveAndExportPose();
        }

        // IF THE USER REALLY WANTS TO EXIT THE APP

        // EXIT THE APPLICATION
        // GET A BUNCH OF THINGS WE NEED TO USE
        // GET A BUNCH OF THINGS WE NEED TO USE
        gui.updateMode();
        gui.updataCanvas();
        poseurStateManager.refreshEditorControl();
        poseurStateManager.setState(AnimatedPoseurState.POSEUR_POSE_SELECTED_STATE);
    }
    /**
     * This mutator method marks the file as not saved, which means that
     * when the user wants to do a file-type operation, we should prompt
     * the user to save current work first. Note that this method should
     * be called any time the pose is changed in some way.
     */
    public void markPoseFileAsNotSaved()
    {
        saved = false;
    }

    /**
     * Accessor method for checking to see if the current pose has been
     * saved since it was last editing. If the current file matches
     * the pose data, we'll return true, otherwise false.
     * 
     * @return true if the current pose is saved to the file, false otherwise.
     */
    public boolean isSaved()
    {
        return saved;
    }

    /**
     * Access method for getting the PoseIO
     * @return the poseIO
     */
    public PoseIO getPoseIO() {
        return poseIO;
    }

    /**
     * This method help swap two integer values in an array list
     * 
     * @param target target ArrayList
     * @param smallIndex smaller index of this array list
     * @param bigIndex bigger index of this array list
     */
    private void swapInArrayList(ArrayList<Integer> target, int smallIndex, int bigIndex )
    {
        int smallIndexValue = target.get(smallIndex);
        int bigIndexValue = target.get(bigIndex);
        target.remove(bigIndex);
        target.remove(smallIndex);
        target.add(smallIndex, bigIndexValue);
        target.add(bigIndex, smallIndexValue);
    }
    
    /**
     * This method helps us swap two images icon in a list model
     * 
     * @param mode list model
     * @param in smaller index
     * @param dex bigger index
     */
    private void swapTwoImageIconInListModel(DefaultListModel mode, int in, int dex)
    {
        ImageIcon first = (ImageIcon)mode.getElementAt(in);
        ImageIcon second = (ImageIcon)mode.getElementAt(dex);
        mode.removeElementAt(dex);
        mode.removeElementAt(in);
        mode.add(in, second);
        mode.add(dex, first);
    }
}