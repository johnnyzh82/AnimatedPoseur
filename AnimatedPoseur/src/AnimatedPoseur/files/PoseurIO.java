package AnimatedPoseur.files;

import AnimatedPoseur.AnimatedPoseur;
import static AnimatedPoseur.AnimatedPoseurSetting.*;
import AnimatedPoseur.gui.AnimatedPoseurGUI;
import AnimatedPoseur.renderer.Pose;
import AnimatedPoseur.renderer.PoseList;
import AnimatedPoseur.renderer.PoseNode;
import AnimatedPoseur.renderer.Sprite;
import AnimatedPoseur.state.AnimatedPoseurPose;
import AnimatedPoseur.state.AnimatedPoseurSprite;
import AnimatedPoseur.state.PoseurStateManager;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This class performs Animated Poseur, which is sprite object saving to an XML
 * formatted .poseur file, loading such files like animation state and pose, creating
 * new state and pose, rename animation state, delete animation state and pose, duplicate 
 * animation state and pose, change the order of animation poses of given state and 
 * modify duration in xml file. Also, the pose file could also be possibly modified via pose editor.
 * 
 * @author Yunlong Zhang
 *          CSE 219 Stony Brook
 * @version 1.0
 */
public class PoseurIO {
    
    /**
     * This method loads the animation states of the poseurFileName .poseur file into
     * the Pose for editing. Note that this file must validate against
     * the animated_poseur.xsd schema.
     * 
     * @param poseurFileName The Poseur to load for editing.
     */
    public AnimatedPoseurSprite loadPoseur(String poseurFileName)
    {
        XMLUtilities xmlUtil = new XMLUtilities();
        AnimatedPoseurSprite tempSprite = null;
        try
        {
            Document doc = xmlUtil.loadXMLDocument(poseurFileName, POSEUR_SCHEMA);
            // LOAD POSEUR DEMENSION
            int poseurWidth = xmlUtil.getIntData(doc,POSEUR_WIDTH_NODE);
            int poseurHeight = xmlUtil.getIntData(doc, POSEUR_HEIGHT_NODE);
            
            // USING TEMP POSEUR AND POSE TO STORE LOADED DATA
            tempSprite = new AnimatedPoseurSprite(poseurWidth,poseurHeight);
            PoseList poseList;
            ArrayList<Integer> poseImageList;
            
            
            // FIRST, WE NEED STORE ALL IMAGES DATA TO HASHMAP
            NodeList imagesNodes = doc.getElementsByTagName(POSEUR_IMAGE_FILE_NODE);
            for(int i = 0; i < imagesNodes.getLength(); i++)
            {
                // INITIALIZE ID AND PATH OF IMAGE
                int id;
                String imageFileName;
                // READ CURREND LOOP INDEX ITEM
                Node node = imagesNodes.item(i);
                // GET ALL ATTRIBUTES OF THIS NODE
                NamedNodeMap attributesOfImagesFile = node.getAttributes();
                // READ BOTH OF THEM
                id = Integer.parseInt(attributesOfImagesFile.getNamedItem(POSEUR_ID_ATTRIBUTE).getTextContent());
                imageFileName = attributesOfImagesFile.getNamedItem(POSEUR_FILE_NAME_ATTRIBUTE).getTextContent();
                // CALCULATE THE PATH AND POSEUR NAME
                int dot = poseurFileName.lastIndexOf(DOT_SEPARATOR);
                int sep = poseurFileName.lastIndexOf(POSEUR_FILE_SEPARATOR);
                String poseurName = poseurFileName.substring(sep+1, dot);
                // COPY THIS IMAGE WITH PATH GIVEN AND ADD TO HASHMAP
                File file = new File(APP_DATA_PATH + poseurName + FILE_SEPARATOR + imageFileName);
                Image image = ImageIO.read(file);
                tempSprite.getSpriteType().addImage(id, image, imageFileName);
                // NOW CONVERT STRING IMAGE NAME TO POSE NAME
                // FOR EXAMPLE 
                // WALKING_DOWN_1.png  ------->  WALKING DOWN_1.poseur
                String poseFileName = tempSprite.getSpriteType().convertNames(id);
                PoseFileManager psm = AnimatedPoseur.getPoseur().getPoseFileManager();
                AnimatedPoseurPose newPose = psm.getPoseIO().loadPoseForPoseur(APP_DATA_PATH + poseurName + POSE_FOLDER + FILE_SEPARATOR + poseFileName);       
                tempSprite.getSpriteType().addPoses(id, newPose);
                
            }
            
            WhitespaceFreeXMLDoc newDoc = xmlUtil.loadXMLWhitespaceFreeXMLNode(poseurFileName, POSEUR_SCHEMA);
            // IF THERE'S A PROBLEM LOADING THE XML FILE THEN
            // SKIP THIS SPRITE TYPE
            if (newDoc == null) 
            {
                throw new InvalidXMLFileFormatException(poseurFileName, POSEUR_SCHEMA);
            }
            // LOAD ANIMATION STATES BY ITERATORS, MEANWHILE WE ALSO LOAD THE NAMES OF STRING TYPE
            // IN A NEW ARRAYLIST WE CREATED FOR FURTHER USE IN CONVINENCE
            Iterator animationList = newDoc.getRoot().getChildOfType(POSEUR_ANIMATIONS_LIST_NODE).getChildren();
            // CREATE SEVERAL TEMP NODES, ANIMATION STATE AND STRINGS
            WhitespaceFreeXMLNode tempNode;
            WhitespaceFreeXMLNode tempStateNode;
            WhitespaceFreeXMLNode tempAnimationSquenceNode;
            String spriteStateName;
            int id;
            int duration;
            // CREATE INTERATION OF ANIMATION LIST AND RETURN STATE AND ANIMATION SEQUENCE,
            // WE PULL OUT ALL DATA AND STORE THEM
           while (animationList.hasNext()) 
           {
                // THIS NODE USED BY ITERATION
                tempNode = (WhitespaceFreeXMLNode)animationList.next();
                // LOAD NODES FROM TREE
                tempStateNode = tempNode.getChildOfType(POSEUR_STATE_NODE);
                // LOAD ANIMATION SEQUENCE NODE
                tempAnimationSquenceNode = tempNode.getChildOfType(POSEUR_ANIMATION_SEQUENCE_NODE);
                // READ ORIGINAL ANIMATION STRING
                spriteStateName = tempStateNode.getData();
                // CHANGE IT TO ANIMATION
                // TRANSLATE IT TO ANIMATION
                Iterator animationSquence = tempAnimationSquenceNode.getChildren();
                WhitespaceFreeXMLNode tempAttributeName;
                // A NESTED ITERATION USED BY ADDING POSELIST IN EACH SPRITE TYPE, IN EACH
                // POSELIST WE HAVE DIFFERENT POSES THAT HAVE SPECIFIC ID AND DURATION THAT
                // IS FROM THE DATA WE READ
                while (animationSquence.hasNext()) 
                {
                    tempAttributeName = (WhitespaceFreeXMLNode)animationSquence.next();
                    // EACH ITERATION WE CREATE A NEW INSTANCE OF POSELIST
                    poseList = tempSprite.addPoseList(spriteStateName);
                    poseImageList = tempSprite.addPoseImageList(spriteStateName);
                    
                    // INITIALIZE ID AND DURATION FROM TREE
                    id = Integer.parseInt(tempAttributeName.getAttributeValue(POSEUR_IMAGE_ID_ATTRIBUTE));
                    duration = Integer.parseInt(tempAttributeName.getAttributeValue(POSEUR_DURATION_ATTRIBUTE));
                    // ADD IT
                    poseList.addPose(id, duration);
                    poseImageList.add(id);
                }
                // ADD IT TO ARRAYLIST
                tempSprite.addAnimationState(spriteStateName);
           }   
        } catch (IOException ex) {
            Logger.getLogger(PoseurIO.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch(InvalidXMLFileFormatException | DOMException | HeadlessException ex)
        {
            // SOMETHING WENT WRONG LOADING THE .pose XML FILE
            AnimatedPoseur singleton = AnimatedPoseur.getPoseur();
            AnimatedPoseurGUI gui = singleton.getGUI();
            JOptionPane.showMessageDialog(
                gui,
                POSEUR_LOADING_ERROR_TEXT,
                POSE_LOADING_ERROR_TITLE_TEXT,
                JOptionPane.ERROR_MESSAGE);      
            return null;
        }  
        return tempSprite;
    }   
    
    /**
     * This method saves the poseur currently being edited to the poseurFile. Note
     * that it will be saved as a .pose file, which is an XML-format that will
     * conform to the animtaed_poseur.xsd schema.
     * 
     * @param poseurFile The file to write the pose to.
     * 
     * @return true if the file is successfully saved, false otherwise. It's
     * possible that another program could lock out ours from writing to it,
     * so we need to let the caller know when this happens.
     */
    public boolean savePoseur(File poseurFile)
    {
        // GET THE POSE AND ITS DATA THAT WE HAVE TO SAVE
        AnimatedPoseur singleton = AnimatedPoseur.getPoseur();
        PoseurFileManager poseurFileManager = singleton.getPoseurFileManager();
        AnimatedPoseurSprite poseurToSave = poseurFileManager.getPoseur();
        HashMap<Integer,Image> imagesList = poseurToSave.getSpriteType().getSpriteImages();
        HashMap<Integer,AnimatedPoseurPose> posesList = poseurToSave.getSpriteType().getSpritePoses();
        HashMap<String,PoseList> posesListForDuration = poseurToSave.getSpriteType().getAnimationsToRender();
        ArrayList<String> stateNames = poseurToSave.getAnimationStateNames();
        // THOSE TWO ARRAYLISTS ARE USED TO WRITE ATTRIBUTE ID AND DURATION WITH RESPECT TO EACH POSE
        ArrayList<Integer> poseSequence;
        ArrayList<Integer> durationSequence;
        
        try
        {
            // THESE WILL HELP US BUILD A DOC
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            
            // FIRST MAKE THE DOCUMENT
            Document doc = docBuilder.newDocument();
            
            Element rootElement = doc.createElement(SPRITE_TYPE_NODE);
            doc.appendChild(rootElement);
            
            // THEN MAKE AND ADD THE WIDTH, HEIGHT
            Element poseurWidthElement = makeElement(doc, rootElement, 
                    POSEUR_WIDTH_NODE, "" + poseurToSave.getSpriteType().getWidth());
            Element poseurHeightElement = makeElement(doc, rootElement, 
                    POSEUR_HEIGHT_NODE, "" + poseurToSave.getSpriteType() .getHeight());
            
            // NOW LET'S MAKE THE IMAGES LIST
            Element imagesListElement = makeElement(doc,rootElement,
                    POSEUR_IMAGES_LIST_NODE, "");
            // AND ADD ALL IMAGES TO IMAGES LIST
            for(int i=1; i<=imagesList.size();i++)
            {
                String tempIm = poseurToSave.getSpriteType().getImagesFileName(i) + IMAGE_FILE_EXTENSION;
                Element poseurImageFileElement = doc.createElement(POSEUR_IMAGE_FILE_NODE);
                addNodeData(poseurImageFileElement, POSEUR_FILE_NAME_ATTRIBUTE, i , tempIm);
                imagesListElement.appendChild(poseurImageFileElement);
            }

            // NOW LET'S MAKE ANIMATION LIST
            Element animationListElement = makeElement(doc,rootElement,
                    POSEUR_ANIMATIONS_LIST_NODE, "");
            
            // NOW WE ARE GONNA ADD A BUNCH OF ANIMATION STATE NODES
            for(int i =0; i<stateNames.size(); i++)
            {
                // EACH LOOP WE ADD ONE STATE NODE
                Element animationStateElement = makeElement(doc,animationListElement,
                    POSEUR_ANIMATION_STATE_NODE, "");
                // WE NEED GET ANIMATION STATE STRING NAME
                String currentStateName = stateNames.get(i);
                Element stateElement = makeElement(doc,animationStateElement,
                POSEUR_STATE_NODE, ""+ currentStateName);
                Element animationSequenceElement = makeElement(doc,animationStateElement,
                POSEUR_ANIMATION_SEQUENCE_NODE, "");
                
                // GET THE ARRAY LIST OF ID SEQUENCE OF CURRENT ANIMATION SEQUENCE
                poseSequence = poseurToSave.getSpriteType().getPoseImageList(currentStateName); 
                // NOW WE NEED EXTRACT DURATION TIME FROM POSELIST IN ORDER TO WRITE IT
                // INITIALIZE ARRAYLIST DURATION TO NEW ONE EVERTY TIME WE READ A STATE
                durationSequence = new ArrayList();
                PoseList posesDurationList = posesListForDuration.get(currentStateName);
                // GET THE LINEAR SINGULAR ITERATOR , NOTE THIS IS NOT RENDERING ONE!!!!!
                Iterator<Pose> durationIterator = posesDurationList.getPoseListIterator();
                for(int j =0; j<posesDurationList.size();j++)
                { 
                    Pose poseCursor = durationIterator.next();
                    int duration = poseCursor.getDurationInFrames();
                    durationSequence.add(duration);
                }
                
                for(int k = 0; k< poseSequence.size();k++)
                {
                    Element poseElement = doc.createElement(POSEUR_POSE_NODE);
                    poseElement.setAttribute(POSEUR_IMAGE_ID_ATTRIBUTE, "" + poseSequence.get(k));
                    poseElement.setAttribute(POSEUR_DURATION_ATTRIBUTE, "" + durationSequence.get(k));
                    animationSequenceElement.appendChild(poseElement);
                }
            }
            
            // THE TRANSFORMER KNOWS HOW TO WRITE A DOC TO
            // An XML FORMATTED FILE, SO LET'S MAKE ONE
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, YES_VALUE);
            transformer.setOutputProperty(XML_INDENT_PROPERTY, XML_INDENT_VALUE);
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(poseurFile);
            
            // SAVE THE POSE TO AN XML FILE
            transformer.transform(source, result);    
            
            // WE MADE IT THIS FAR WITH NO ERRORS
            AnimatedPoseurGUI gui = singleton.getGUI();
            JOptionPane.showMessageDialog(
                gui,
                POSEUR_SAVED_TEXT,
                POSEUR_SAVED_TITLE_TEXT,
                
                JOptionPane.INFORMATION_MESSAGE);
            return true;
        }
            catch(TransformerException | ParserConfigurationException | DOMException | HeadlessException ex)
            {
                // SOMETHING WENT WRONG WRITING THE XML FILE
                AnimatedPoseurGUI gui = singleton.getGUI();
                JOptionPane.showMessageDialog(
                    gui,
                    POSEUR_SAVING_ERROR_TEXT,
                    POSEUR_SAVING_ERROR_TITLE_TEXT,
                    JOptionPane.ERROR_MESSAGE);  
                return false;
            }  
      }
    
       /**
     * This helper method builds elements (nodes) for us to help with building
     * a Doc which we would then save to a file.
     * 
     * @param doc The document we're building.
     * 
     * @param parent The node we'll add our new node to.
     * 
     * @param elementName The name of the node we're making.
     * 
     * @param textContent The data associated with the node we're making.
     * 
     * @return A node of name elementName, with textComponent as data, in the doc
     * document, with parent as its parent node.
     */
    private static Element makeElement(Document doc, Element parent, String elementName, String textContent)
    {
        Element element = doc.createElement(elementName);
        element.setTextContent(textContent);
        parent.appendChild(element);
        return element;
    }
    
    /**
     * This help method builds attribute of the id and name of a image file
     *
     * @param geometryNode node we append attributes on
     *
     * @param fileName image file name
     * 
     * @param idToWrite id 
     *
     * @param fileToWrite file to write into
     */
    private void addNodeData(Element geometryNode, String fileName, int idToWrite, String fileToWrite)
    {
        geometryNode.setAttribute(POSEUR_ID_ATTRIBUTE, "" + idToWrite);
        geometryNode.setAttribute(fileName, "" + fileToWrite);
    }
}
