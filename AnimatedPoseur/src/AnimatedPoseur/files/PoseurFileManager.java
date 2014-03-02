package AnimatedPoseur.files;

import AnimatedPoseur.AnimatedPoseur;
import static AnimatedPoseur.AnimatedPoseurSetting.*;
import AnimatedPoseur.gui.AnimatedPoseurGUI;
import AnimatedPoseur.renderer.Pose;
import AnimatedPoseur.renderer.PoseList;
import AnimatedPoseur.renderer.SceneRenderer;
import AnimatedPoseur.renderer.Sprite;
import AnimatedPoseur.renderer.SpriteType;
import AnimatedPoseur.state.AnimatedPoseurPose;
import AnimatedPoseur.state.AnimatedPoseurSprite;
import AnimatedPoseur.state.AnimatedPoseurState;
import AnimatedPoseur.state.PoseurStateManager;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javax.imageio.ImageIO;
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
 *          Yunlong Zhang
 *          CSE 219 Stony Brook
 * @version 1.0
 */
public class PoseurFileManager 
{
    // WE'LL STOR THE FILE CURRENTLY BEING WORKED ON AND THE NAME OF THE FILE
    private File currentFile;
    private String currentPoseurName;
    private String currentFileName;
    public  AnimatedPoseurSprite sprite;
    private Sprite spriteToRender;
    public static int animationStateSelected;
    
    // WE WANT TO KEEP TRACK OF WHEN SOMTHING HAS NOT BEEN SAVED
    private boolean saved;
    
    // READ, WRITE XML FILE
    private PoseurIO poseurIO;
    private PoseIO poseIO;
    
    /**
     * This default constructor starts the program without a
     * sprite file being edited.
     */
    public PoseurFileManager()
    {
        currentFile = null;
        currentFileName = null;
        currentPoseurName = null;
        animationStateSelected = -1;
        sprite = null;
        spriteToRender = null;
        saved = true;
        poseurIO = new PoseurIO();
        poseIO = new PoseIO();
    }
    
    /**
     * This method starts the process of editing a new poseur. If
     * a pose is already being edited, it will prompt the user
     * to save it first.
     */
    public void requestNewPoseur()
    {
        // WE MAY HAVE TO SAVE CURRENT WORK
        boolean continueToMakeNew = true;
        if (!saved)
        {
            // THE USER CAN OPT OUT HERE WITH A CANCEL
            continueToMakeNew = promptToSave();
        }
        
        // IF THE USER REALLY WANTS TO MAKE A NEW POSEUR
        if (continueToMakeNew)
        {
            // GO AHEAD AND PROCEED MAKING A NEW POSEUR
            continueToMakeNew = promptForNew();

            if (continueToMakeNew)
            {
                // NOW THAT WE'VE SAVED, LET'S MAKE SURE WE'RE IN THE RIGHT MODE
                PoseurStateManager poseStateManager = AnimatedPoseur.getPoseur().getPoseStateManager();
                AnimatedPoseurGUI gui = AnimatedPoseur.getPoseur().getGUI();
                poseStateManager.setState(AnimatedPoseurState.SELECT_ANIMATION_STATE_STATE);
            }
        }
    }
    
    /**
     * This method lets the user open a pose saved
     * to a file. It will also make sure data for the
     * current pose is not lost.
     */
    public void requestOpenPoseur()
    {
        // WE MAY HAVE TO SAVE CURRENT WORK
        boolean continueToOpen = true;
        if (!saved)
        {
            // THE USER CAN OPT OUT HERE WITH A CANCEL
            continueToOpen = promptToSave();
        }
        
        // IF THE USER REALLY WANTS TO OPEN A POSE
        if (continueToOpen)
        {
            // GO AHEAD AND PROCEED MAKING A NEW POSE
            promptToOpen();
        }
    }
    
    /**
     * This method will save the current pose to a file. Note that 
     * we already know the name of the file, so we won't need to
     * prompt the user.
     */
    public void requestSavePoseur()
    {
        // DON'T ASK, JUST SAVE
        boolean savedSuccessfully = poseurIO.savePoseur(currentFile);
        if (savedSuccessfully)
        {
            // MARK IT AS SAVED
            saved = true;
            
            // AND REFRESH THE GUI
            AnimatedPoseur.getPoseur().getGUI().updateMode();
        }
    }

    public boolean requestSaveAsPose()
    {
        // SO NOW ASK THE USER FOR A POSEUR NAME
        AnimatedPoseurGUI gui = AnimatedPoseur.getPoseur().getGUI();
        PoseurStateManager poseurStateManager = AnimatedPoseur.getPoseur().getPoseStateManager();
        PoseurFileManager poseurFileManager = AnimatedPoseur.getPoseur().getPoseurFileManager();
        String fileName = JOptionPane.showInputDialog(
                gui,
                POSEUR_NAME_REQUEST_TEXT,
                POSEUR_NAME_REQUEST_TITLE_TEXT,
                JOptionPane.QUESTION_MESSAGE);
        
        // IF THE USER CANCELLED, THEN WE'LL GET A fileName
        // OF NULL, SO LET'S MAKE SURE THE USER REALLY
        // WANTS TO DO THIS ACTION BEFORE MOVING ON
      
        if ((fileName != null)
                &&
            (fileName.length() > 0))
        {
            // TRY TO CREATE A NEW FOLDER, RETURN IF EXISTED
            File dir = new File(APP_DATA_PATH + fileName);
            if (!dir.exists())
                {
                    dir.mkdir();
                }
                else
                {
                     JOptionPane.showMessageDialog(
                            gui,
                            SAME_NAME_EXISTED_TEXT,
                            SAME_NAME_EXISTED_TITLE_TEXT,
                            JOptionPane.ERROR_MESSAGE);
                    poseurStateManager.setState(AnimatedPoseurState.STARTUP_STATE);
                    return false;
                }
            // UPDATE THE FILE NAMES AND FILE
            if(sprite == null)
            {
                sprite = new AnimatedPoseurSprite(DEFAULT_POSEUR_WIDTH,DEFAULT_POSEUR_HEIGHT);
            }
            else
            {
                sprite = getPoseur();
            }
            currentPoseurName = fileName;
            currentFileName = fileName + POSEUR_FILE_EXTENSION;
            
            currentFile = new File(dir,currentFileName);
            File posesDir = new File(APP_DATA_PATH + fileName + POSE_FOLDER);
//            File exportImagesDir = new File(APP_DATA_PATH + fileName + EXPORTED_IMAGES_FOLDER);
            posesDir.mkdir();
//            exportImagesDir.mkdir();

            // SAVE OUR NEW FILE
            poseurIO.savePoseur(currentFile);
            saved = true;

            // AND PUT THE FILE NAME IN THE TITLE BAR
            String appName = gui.getAppName();
            gui.setTitle(appName + APP_NAME_FILE_NAME_SEPARATOR + currentFile); 
            // NOW WE NEED SAVE ALL POSES FROM OLD FILE AND ALL IMAGES FROM TARGET
            HashMap<Integer, Image> spriteImages = sprite.getSpriteType().getSpriteImages();
            HashMap<Integer,String> imagesNames = sprite.getSpriteType().getImagesName();
            HashMap<Integer, AnimatedPoseurPose> spritePoses = sprite.getSpriteType().getSpritePoses();
            
            int size = spriteImages.size();
            for(int i = 1; i<=size; i++)
            {
                
                // FIRST COPY ALL POSES INTO POSE FOLDER CHAGNE THE NAME
                try
                {
                String poseDir = (APP_DATA_PATH + fileName 
                        + POSE_FOLDER
                        + FILE_SEPARATOR
                        + imagesNames.get(i)
                        + POSE_FILE_EXTENSION);
                
                File poseFile = new File(poseDir);
                poseFile.createNewFile();
                AnimatedPoseurPose tempPose = spritePoses.get(i);
                poseIO.savePoseWhenSaveAs(poseFile, tempPose);
                
                
                // SECOND EXPORT ALL IMAGES TO IMAGES FOLDER CHANGE THE NAME
                String imageDir = (APP_DATA_PATH 
                        + fileName 
                        + FILE_SEPARATOR
                        + imagesNames.get(i)
                        + PNG_FILE_EXTENSION);
                File imageFile = new File(imageDir);
                imageFile.createNewFile();
                
                Image image = spriteImages.get(i);
                BufferedImage imageToExport =(BufferedImage)image;
                ImageIO.write(imageToExport, PNG_FORMAT_NAME, imageFile);
                }
                catch(IOException ioe)
                {
                    JOptionPane.showMessageDialog(
                        gui,
                        IMAGE_EXPORTING_ERROR_TEXT,
                        IMAGE_EXPORTING_ERROR_TITLE_TEXT,
                        JOptionPane.ERROR_MESSAGE);    
                } 
            }        
            // WE DID IT!
            return true;
         }
        // USER DECIDED AGAINST IT
        poseurStateManager.setState(AnimatedPoseurState.STARTUP_STATE);
        return false;
    }
    
    /**
     * This method will export the current pose to an image file.
     
    
    /**
     * This method will exit the application, making sure the user
     * doesn't lose any data first.
     */
    public void requestExit()
    {
        // WE MAY HAVE TO SAVE CURRENT WORK
        boolean continueToExit = true;
        if (!saved)
        {
            // THE USER CAN OPT OUT HERE
            continueToExit = promptToSave();
        }
        
        // IF THE USER REALLY WANTS TO EXIT THE APP
        if (continueToExit)
        {
            // EXIT THE APPLICATION
            System.exit(0);
        }
    }

    /**
     * This helper method asks the user for a name for the pose about
     * to be created. Note that when the pose is created, a corresponding
     * .pose file is also created.
     * 
     * @return true if the user goes ahead and provides a good name
     * false if they cancel.
     */
    private boolean promptForNew()
    {
        // SO NOW ASK THE USER FOR A POSEUR NAME
        AnimatedPoseurGUI gui = AnimatedPoseur.getPoseur().getGUI();
        PoseurStateManager poseurStateManager = AnimatedPoseur.getPoseur().getPoseStateManager();
        PoseurFileManager poseurFileManager = AnimatedPoseur.getPoseur().getPoseurFileManager();
        String fileName = JOptionPane.showInputDialog(
                gui,
                POSEUR_NAME_REQUEST_TEXT,
                POSEUR_NAME_REQUEST_TITLE_TEXT,
                JOptionPane.QUESTION_MESSAGE);
        
        // IF THE USER CANCELLED, THEN WE'LL GET A fileName
        // OF NULL, SO LET'S MAKE SURE THE USER REALLY
        // WANTS TO DO THIS ACTION BEFORE MOVING ON
        
        if ((fileName != null)
                &&
            (fileName.length() > 0))
        {
            // TRY TO CREATE A NEW FOLDER, RETURN IF EXISTED
            File dir = new File(APP_DATA_PATH + fileName);
            if (!dir.exists())
                {
                    dir.mkdir();
                }
                else
                {
                     JOptionPane.showMessageDialog(
                            gui,
                            SAME_NAME_EXISTED_TEXT,
                            SAME_NAME_EXISTED_TITLE_TEXT,
                            JOptionPane.ERROR_MESSAGE);
                    poseurStateManager.setState(AnimatedPoseurState.STARTUP_STATE);
                    return false;
                }
            // UPDATE THE FILE NAMES AND FILE
            if(sprite == null)
            {
                sprite = new AnimatedPoseurSprite(DEFAULT_POSEUR_WIDTH,DEFAULT_POSEUR_HEIGHT);
            }
            currentPoseurName = fileName;
            currentFileName = fileName + POSEUR_FILE_EXTENSION;
            
            currentFile = new File(dir,currentFileName);
            File posesDir = new File(APP_DATA_PATH + fileName + POSE_FOLDER);
//            File exportImagesDir = new File(APP_DATA_PATH + fileName + EXPORTED_IMAGES_FOLDER);
            posesDir.mkdir();
//            exportImagesDir.mkdir();
            gui.clearAniamtionStateList();
            gui.clearPoseList();
            gui.clearSprite();
            gui.clearImagePanel();
            
            // SAVE OUR NEW FILE
            poseurIO.savePoseur(currentFile);
            saved = true;

            // AND PUT THE FILE NAME IN THE TITLE BAR
            String appName = gui.getAppName();
            gui.setTitle(appName + APP_NAME_FILE_NAME_SEPARATOR + currentFile); 
            poseurStateManager.setState(AnimatedPoseurState.SELECT_ANIMATION_STATE_STATE);
            // WE DID IT!
            return true;
        }
        // USER DECIDED AGAINST IT
        poseurStateManager.setState(AnimatedPoseurState.STARTUP_STATE);
        return false;
    }

    /**
     * This helper method verifies that the user really wants to save their
     * unsaved work, which they might not want to do. Note that it could be
     * used in multiple contexts before doing other actions, like creating a
     * new pose, or opening another pose, or exiting. Note that the user will 
     * be presented with 3 options: YES, NO, and CANCEL. YES means the user 
     * wants to save their work and continue the other action (we return true
     * to denote this), NO means don't save the work but continue with the
     * other action (true is returned), CANCEL means don't save the work and
     * don't continue with the other action (false is returned).
     * 
     * @return true if the user presses the YES option to save, true if the user
     * presses the NO option to not save, false if the user presses the CANCEL
     * option to not continue.
     */
    private boolean promptToSave()
    {
       // PROMPT THE USER TO SAVE UNSAVED WORK
        AnimatedPoseurGUI gui = AnimatedPoseur.getPoseur().getGUI();
        int selection =JOptionPane.showOptionDialog(gui, 
                    PROMPT_TO_SAVE_TEXT, PROMPT_TO_SAVE_TITLE_TEXT, 
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, 
                    null, null, null);
        
        // IF THE USER SAID YES, THEN SAVE BEFORE MOVING ON
        if (selection == JOptionPane.YES_OPTION)
        {
            poseurIO.savePoseur(currentFile);
            saved = true;
        }
        
        // IF THE USER SAID CANCEL, THEN WE'LL TELL WHOEVER
        // CALLED THIS THAT THE USER IS NOT INTERESTED ANYMORE
        else if (selection == JOptionPane.CANCEL_OPTION)
        {
            return false;
        }

        // IF THE USER SAID NO, WE JUST GO ON WITHOUT SAVING
        // BUT FOR BOTH YES AND NO WE DO WHATEVER THE USER
        // HAD IN MIND IN THE FIRST PLACE
        return true;
    }
    
    /**
     * This helper method asks the user for a file to open. The
     * user-selected file is then loaded and the GUI updated. Note
     * that if the user cancels the open process, nothing is done.
     * If an error occurs loading the file, a message is displayed,
     * but nothing changes.
     */
    private void promptToOpen()
    {
        // WE'LL NEED THE GUI
        AnimatedPoseur singleton = AnimatedPoseur.getPoseur();
        AnimatedPoseurGUI gui = singleton.getGUI();
        
        // AND NOW ASK THE USER FOR THE POSE TO OPEN
        JFileChooser poseurFileChooser = new JFileChooser(APP_DATA_PATH);
        int buttonPressed = poseurFileChooser.showOpenDialog(gui);
        
        // ONLY OPEN A NEW FILE IF THE USER SAYS OK
        if (buttonPressed == JFileChooser.APPROVE_OPTION)
        {
            // GET THE FILE THE USER ENTERED
            gui.clearAniamtionStateList();
            currentFile = poseurFileChooser.getSelectedFile();            
            currentFileName = currentFile.getName();
            currentPoseurName = currentFileName.substring(0, currentFileName.indexOf("."));
            saved = true;
            // AND PUT THE FILE NAME IN THE TITLE BAR
            String appName = gui.getAppName();
            gui.setTitle(appName + APP_NAME_FILE_NAME_SEPARATOR + currentFile);             
            // AND LOAD THE .poseur (XML FORMAT) FILE
            sprite = poseurIO.loadPoseur(currentFile.getAbsolutePath());
            ArrayList<String> stateNames = sprite.getAnimationStateNames();
            if(stateNames == null)
            {
                stateNames = new ArrayList();
            }
            DefaultListModel animationStateListModel = gui.getAnimationStateListModel();
            Iterator<String> animationStates = stateNames.iterator();
            while (animationStates.hasNext()) {
                String stateName = animationStates.next();
                animationStateListModel.addElement(stateName);
            }
            gui.setAnimationStateListModel(animationStateListModel);
            PoseurStateManager poseurStateManager = singleton.getPoseStateManager();
            poseurStateManager.setState(AnimatedPoseurState.SELECT_ANIMATION_STATE_STATE);
        }
    }
    
    
    /**
     * This helper method will respond to any selection in Animation State List
     * if user click the animation state, we have a lot of post conditions after 
     * clicked the option. All data will be dynamically loaded into the structure, and
     * the renderer panel is ready to use for the sprite current state for viewing
     * 
     * @return ture if successfully loaded
     *          false if any error occurs 
     * 
     */
    public boolean selectAnimationState()
    { 
        // WHEN ONE ANIMATION STATE IS SELECTTED, SOME PART OF GUI WILL BE CHANGED:
        // 1, POSE CONTROL WILL BE ENABLED, POSES ARE AUTOMATICALLY LOADED INTO POSE LIST
        //    THIS ONE ACTUALLY HELPS TO LOOK FOR THE POSES SEQUENCES AND STORE THEM IN ARRAYLIST
        //    AND WE WILL PUT IT INTO LIST, WE CAN MODIFY THEM AS DESCRIBED.
        // 2, ANIMATION VIEWER IS ENABLED, SO USER CAN DIRECTLY VIEW THE ANIMATION STATE, NOTE 
        //    THIS WILL USE THOSE FUNCTION OF CLASSES IN RENDERER PACKAGES
        // THEN ACCESS POSEURGUI AND ACCESS POSERURSTATEMANAGER
        AnimatedPoseur singleton = AnimatedPoseur.getPoseur();
        AnimatedPoseurGUI gui = singleton.getGUI();
        PoseurStateManager poseurStateManager = singleton.getPoseStateManager();
        PoseurFileManager poseurFileManager = singleton.getPoseurFileManager();
        
        // CLEAR POSES LIST
        gui.clearPoseList();
        // GET CURRENT OPENED POSEUR
        animationStateSelected = gui.getSelectedAnimationState();
        if(animationStateSelected == -1)
        {
            return false;
        }
        
        sprite = poseurFileManager.getPoseur();
        
        gui.clearSprite();
        // GET CURRENT SELECTED STATE STRING NAME
        String stateName = sprite.getAnimationStateNames().get(animationStateSelected);
        spriteToRender = new Sprite(sprite.getSpriteType(),stateName);
        ArrayList<Integer> imagesIDs = sprite.getSpriteType().getPoseImageList(stateName);
        HashMap<Integer, Image> imagesHash = sprite.getSpriteType().getSpriteImages();
        
        DefaultListModel poseListModel = gui.getPoseListModel();
        for (int i=0; i<imagesIDs.size();i++)
        {
            int eachID = imagesIDs.get(i);
            Image eachImage = imagesHash.get(eachID).getScaledInstance(100,100,Image.SCALE_DEFAULT);
            ImageIcon eachIcon = new ImageIcon(eachImage);
            poseListModel.addElement(eachIcon);
        }
        gui.setPoseListModel(poseListModel);
        // SET IT TO NEXT STATE
        poseurStateManager.setState(AnimatedPoseurState.SELECT_POSEUR_POSE_STATE);
        return true;
    }
    
    /**
     * This help method respond to user delete an Animation State, post condition
     * is that the Animation State is successfully deleted.
     * 
     * @return true if success
     *          false if not
     */
    public boolean promptToDeleteFile()
    {
        AnimatedPoseur singleton = AnimatedPoseur.getPoseur();
        AnimatedPoseurGUI gui = singleton.getGUI();
        PoseurStateManager poseurStateManager = singleton.getPoseStateManager();
        PoseurFileManager poseurFileManager = singleton.getPoseurFileManager();
        int selection =JOptionPane.showOptionDialog(gui, 
                    PROMPT_TO_DELETE_STATE_TEXT, PROMPT_TO_DELETE_STATE_TITLE_TEXT, 
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, 
                    null, null, null);
             // IF THE USER SAID YES, THEN SAVE BEFORE MOVING ON
        if (selection == JOptionPane.YES_OPTION)
        {
            // GET CURRENT OPENED POSEUR
            animationStateSelected = gui.getSelectedAnimationState();
            sprite = poseurFileManager.getPoseur();
            // GET CURRENT SELECTED STATE STRING NAME
            String stateName = sprite.getAnimationStateNames().get(animationStateSelected);
            // WE NEED DELETE IN RENDERING LIST!!!
            HashMap animationToRender = sprite.getSpriteType().getAnimationsToRender();
            animationToRender.remove(stateName);
            // THEN DELETE POSES IMAGES LIST
            HashMap poseImagesList = sprite.getSpriteType().getPosesImageList();
            poseImagesList.remove(stateName);
            // DELETE THE NAME IN STRING ARRAYLIST 
            // AND REFRESH!!
            ArrayList<String> animationStateList = sprite.getAnimationStateNames();
            animationStateList.remove(animationStateSelected);
            gui.getAnimationStateListModel().removeElement(stateName);
            gui.refreshAnimationStateList();
            
            // REFRESH THE POSE LIST ALSO WITH NO ANIMATION STATE SELECTTED, POSE LIST 
            // IS COMPLETELY BLANK
            DefaultListModel poseListModel = gui.getPoseListModel();
            poseListModel.clear();
            gui.setPoseListModel(poseListModel);
            poseurStateManager.setState(AnimatedPoseurState.SELECT_ANIMATION_STATE_STATE); 
        }
        else if (selection == JOptionPane.CANCEL_OPTION)
        {
            return false;
        }
        return true;
    }
    
    /**
     * Respond to duplicate handler
     * 
     * @return true if deleted
     *          false if not deleted
     */
    public boolean promptToDuplicate()
    {
         
        AnimatedPoseur singleton = AnimatedPoseur.getPoseur();
        AnimatedPoseurGUI gui = singleton.getGUI();
        PoseurStateManager poseurStateManager = singleton.getPoseStateManager();
        PoseurFileManager poseurFileManager = singleton.getPoseurFileManager();
        int selection =JOptionPane.showOptionDialog(gui, 
                    PROMPT_TO_DUPLICATE_STATE_TEXT, PROMPT_TO_DUPLICATE_STATE_TITLE_TEXT, 
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, 
                    null, null, null);
        // IF THE USER SAID YES, THEN MOVING ON
        if (selection == JOptionPane.YES_OPTION)
        {
            // GET CURRENT OPENED POSEUR
            animationStateSelected = gui.getSelectedAnimationState();
            sprite = poseurFileManager.getPoseur();
            // GET CURRENT SELECTED STATE STRING NAME
            String stateName = sprite.getAnimationStateNames().get(animationStateSelected);
            ArrayList<Integer> imagesIDs = sprite.getSpriteType().getPoseImageList(stateName);
            // COPY SAME IMAGES ID LIST TO NEW STATE
            // FOR EXAMPLE: WALKING_UP_1    ------->  WALKING_UP_1-COPY
            String newStateName = stateName + APP_NAME_FILE_NAME_SEPARATOR + COPY_EXTENSION;
            // DUPLICATE THIS IN RENDER HASH MAP
            HashMap<String,PoseList> animationToRender = sprite.getSpriteType().getAnimationsToRender();
            if(animationToRender.containsKey(newStateName))
            {
                 JOptionPane.showMessageDialog(
                            gui,
                            SAME_NAME_EXISTED_TEXT,
                            SAME_NAME_EXISTED_TITLE_TEXT,
                            JOptionPane.ERROR_MESSAGE);
                            poseurStateManager.setState(AnimatedPoseurState.SELECT_ANIMATION_STATE_STATE);
                            return false;
            }
            PoseList oldPoseList = animationToRender.get(stateName);
            PoseList newPoseList = new PoseList();
            // WE MUST CREATE A NEW VARIABLE DOESN'T RELATE TO OLD POSE LIST
            if(oldPoseList!=null){
                Iterator<Pose> poseListIterator = oldPoseList.getPoseListIterator();
                while(poseListIterator.hasNext())
                {
                    Pose node = poseListIterator.next();
                    newPoseList.addPose(node.getImageID(), node.getDurationInFrames());
                }
            }
            animationToRender.put(newStateName,newPoseList);
            // THEN DUPLICATE IN POSES HASH MAP
            HashMap posesImageList = sprite.getSpriteType().getPosesImageList();
            // MAKE A COPY OF LIST
            ArrayList<Integer> newImagesIDs = new ArrayList();
            for(int i=0; i<imagesIDs.size();i++)
            {
                newImagesIDs.add(imagesIDs.get(i));
            }
            posesImageList.put(newStateName, newImagesIDs);
            // DONE!
            // ADD THE NAME IN STRING ARRAYLIST 
            // AND REFRESH!!
            ArrayList<String> animationStateList = sprite.getAnimationStateNames();
            animationStateList.add(newStateName);
            gui.getAnimationStateListModel().addElement(newStateName);
            gui.setAnimationStateListModel(gui.getAnimationStateListModel());
            gui.clearPoseList();
            poseurStateManager.setState(AnimatedPoseurState.SELECT_ANIMATION_STATE_STATE);
        }
        else if (selection == JOptionPane.CANCEL_OPTION)
        {
            return false;
        }
        return true;
    }
    
    /**
     * Add a new Animation State, a new animation state will be named by user
     * if user want to add one or more animation state.
     *
     * @return true if success
     *          false if not
     */
    public boolean promptForNewAnimationState()
    {
            AnimatedPoseur singleton = AnimatedPoseur.getPoseur();
            AnimatedPoseurGUI gui = singleton.getGUI();
            PoseurStateManager poseurStateManager = singleton.getPoseStateManager();
            PoseurFileManager poseurFileManager = singleton.getPoseurFileManager();
                String fileName = JOptionPane.showInputDialog(
                gui,
                STATE_NAME_REQUEST_TEXT,
                STATE_NAME_REQUEST_TITLE_TEXT,
                JOptionPane.QUESTION_MESSAGE);

            // GET CURRENT OPENED POSEUR
            if ((fileName != null)
                &&
            (fileName.length() > 0))
            {
                    sprite = poseurFileManager.getPoseur();
                    // GET CURRENT SELECTED STATE STRING NAME
                    ArrayList<String> stateNames = sprite.getAnimationStateNames();
                    // THIS LOOP CHECKS IF THERE IS SAME NAME EXISTED
                    for(int i=0;i<stateNames.size();i++)
                    {
                        if(stateNames.get(i).equals(fileName))
                        {
                            JOptionPane.showMessageDialog(
                            gui,
                            SAME_NAME_EXISTED_TEXT,
                            SAME_NAME_EXISTED_TITLE_TEXT,
                            JOptionPane.ERROR_MESSAGE);
                            poseurStateManager.setState(AnimatedPoseurState.SELECT_ANIMATION_STATE_STATE);
                            return false;
                        }
                    }
                    // FIRST ADD IT TO ARRAYLIST NAMES STRING
                    stateNames.add(fileName);
                    // THEN WE NEED TO ADD TO POSE LIST HASH MAP
                    PoseList newPoseList = new PoseList();
                    HashMap animationToRender = sprite.getSpriteType().getAnimationsToRender();
                    animationToRender.put(fileName,newPoseList);
                    // THEN WE NEED TO ADD TO POSES IMAGES LIST
                    HashMap posesImageList = sprite.getSpriteType().getPosesImageList();
                    ArrayList<Integer> newIDlist = new ArrayList();
                    posesImageList.put(fileName, newIDlist);
                    
                    // DONE!
                    // AND REFRESH TWO LISTS!!
                    gui.getAnimationStateListModel().addElement(fileName);
                    gui.getAnimationStateList().setSelectedIndex(stateNames.size()-1);
                    // REFRESH THE NEW POSELIST
                    DefaultListModel poseListModel = gui.getPoseListModel();
                    poseListModel.clear();
                    gui.setPoseListModel(poseListModel);
                    poseurStateManager.setState(AnimatedPoseurState.SELECT_POSEUR_POSE_STATE);
                    return true;
            }
            poseurStateManager.setState(AnimatedPoseurState.SELECT_ANIMATION_STATE_STATE);
            return false;
     }
    
    /**
     * Response the rename button clicked. 
     * 
     * @return true if Renamed
     *          false if not
     */
    public boolean promptForRenameAnimationState()
    {
            AnimatedPoseur singleton = AnimatedPoseur.getPoseur();
            AnimatedPoseurGUI gui = singleton.getGUI();
            PoseurStateManager poseurStateManager = singleton.getPoseStateManager();
            PoseurFileManager poseurFileManager = singleton.getPoseurFileManager();
            String fileName = JOptionPane.showInputDialog(
                gui,
                STATE_NAME_REQUEST_TEXT,
                STATE_NAME_REQUEST_TITLE_TEXT,
                JOptionPane.QUESTION_MESSAGE);
            // CLEAR TWO LISTS FIRST
//            gui.clearAniamtionStateList(); 
            // GET CURRENT OPENED POSEUR
            if ((fileName != null)
                &&
            (fileName.length() > 0))
            {
                sprite = poseurFileManager.getPoseur();
                // GET CURRENT SELECTED STATE STRING NAME
                ArrayList<String> stateNames = sprite.getAnimationStateNames();
                // THIS LOOP CHECKS IF THERE IS SAME NAME EXISTED
                for(int i=0;i<stateNames.size();i++)
                {
                    if(stateNames.get(i).equals(fileName))
                    {
                        JOptionPane.showMessageDialog(
                        gui,
                        SAME_NAME_EXISTED_TEXT,
                        SAME_NAME_EXISTED_TITLE_TEXT,
                        JOptionPane.ERROR_MESSAGE);
                        poseurStateManager.setState(AnimatedPoseurState.SELECT_ANIMATION_STATE_STATE);
                        return false;
                    }
                }
                // FIRST WE NEED TO ACCESS THE STATE WE SELECTED
                animationStateSelected = gui.getSelectedAnimationState();
                String oldName = stateNames.get(animationStateSelected);
                // MODEFY THE STRING ARRAYLIST
                // DELETE IT
                stateNames.remove(animationStateSelected);
                stateNames.add(animationStateSelected, fileName);
                // REPLACE THIS WITH NEW ONE
                
                // THEN WE NEED TO ADD TO POSE LIST HASH MAP
                HashMap<String,PoseList> animationToRender = sprite.getSpriteType().getAnimationsToRender();
                PoseList poseList = animationToRender.get(oldName);
                // DELETE IT
                animationToRender.remove(oldName);
                // ADD NEW ONE
                animationToRender.put(fileName, poseList);

                // DO IT SAME IN IMAGES LIST
                HashMap<String,ArrayList<Integer>> posesImageList = sprite.getSpriteType().getPosesImageList();
                ArrayList<Integer> IDlist = posesImageList.get(oldName);
                // DELETE OLD ONE
                posesImageList.remove(oldName);
                // ADD NEW ONE
                posesImageList.put(fileName, IDlist);
                // DONE
                // CLEAR AND ADD
                // ONLY REFRESH THE STATE LIST
                DefaultListModel animationStateModel = gui.getAnimationStateListModel();
                int tempAnimationStateSelected = animationStateSelected;
                animationStateModel.removeElementAt(tempAnimationStateSelected);
                animationStateModel.add(tempAnimationStateSelected, fileName);
                gui.getAnimationStateList().setSelectedIndex(stateNames.indexOf(fileName));
                gui.setAnimationStateListModel(animationStateModel);
                poseurStateManager.setState(AnimatedPoseurState.SELECT_POSEUR_POSE_STATE);
                return true;
            }
            poseurStateManager.setState(AnimatedPoseurState.SELECT_ANIMATION_STATE_STATE);
            return false;
    }
    
    /**
     *  This method helps user to start render animation sprite
     */
    public void startAnimationRendering()
    {
        AnimatedPoseur singleton = AnimatedPoseur.getPoseur();
        AnimatedPoseurGUI gui = singleton.getGUI();
        gui.clearSprite();
        if(animationStateSelected == -1)
        {
            JOptionPane.showMessageDialog(
                            gui,
                            NOTHING_SELECTED_ERROR_TEXT,
                            NOTHING_SELECTED_ERROR_TITLE_TEXT,
                            JOptionPane.ERROR_MESSAGE);
                            return;
        }
        String stateName = sprite.getAnimationStateNames().get(animationStateSelected);
         // THIS IS FOR FUTURAL RENDERING ON RENDERING SCENE PANEL
        SpriteType newType = sprite.getSpriteType();
        selectAnimationState();
        if(spriteToRender == null)
        {
            spriteToRender = new Sprite(newType,stateName);
        }
        // PUT THE SPRITE IN THE MIDDLE OF THE PANEL
        SceneRenderer eastSceneRenderingPanel = gui.getSceneRender();
        int rendererWidth = eastSceneRenderingPanel.getWidth();
        int rendererHeight = eastSceneRenderingPanel.getHeight();
        int x = (rendererWidth/2) - (newType.getWidth()/2);
        int y = (rendererHeight/2) - (newType.getHeight()/2);
        spriteToRender.setPositionX(x);
        spriteToRender.setPositionY(y);
        gui.getSceneRender().startScene();
        gui.addSprite(spriteToRender);
        gui.getSceneRender().unpauseScene();
    }
    
    /**
     * This method helps us to pause the render sprite
     */
     public void stopAnimationRendering()
    {
        AnimatedPoseur singleton = AnimatedPoseur.getPoseur();
        AnimatedPoseurGUI gui = singleton.getGUI();
        gui.pauseTheRender();
    }
    
     /**
      * This method serves the speeding up for the render sprite.
      */
    public void speedUpAnimationRendering()
    {
        AnimatedPoseur singleton = AnimatedPoseur.getPoseur();
        AnimatedPoseurGUI gui = singleton.getGUI();
        float newSpeed = gui.getSceneRender().getTimeScaler() - 0.2f;
        System.out.println(newSpeed);
        gui.getSceneRender().setTimeScaler(newSpeed);
    }
      
     /**
      * The mothod that slows down the render sprite speed.
      */
    public void slowDownAnimationRendering()
    {
        AnimatedPoseur singleton = AnimatedPoseur.getPoseur();
        AnimatedPoseurGUI gui = singleton.getGUI();
        float newSpeed = gui.getSceneRender().getTimeScaler() + 0.2f;
        System.out.println(newSpeed);
        gui.getSceneRender().setTimeScaler(newSpeed);
    }
       
    /**
     * This mutator method marks the file as not saved, which means that
     * when the user wants to do a file-type operation, we should prompt
     * the user to save current work first. Note that this method should
     * be called any time the pose is changed in some way.
     */
    public void markPoseurFileAsNotSaved()
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
     * Access method for getting current sprite
     * 
     * @return the sprite
     */
    public AnimatedPoseurSprite getPoseur() {
        return sprite;
    }

    /**
     * Access method for getting renderer sprite
     * 
     * @return the spriteToRender
     */
    public Sprite getSpriteToRender() {
        return spriteToRender;
    }

    /**
     * Access method for getting current pose name
     * 
     * @return the currentPoseurName
     */
    public String getCurrentPoseurName() {
        return currentPoseurName;
    }
    
}
