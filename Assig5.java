/////////////////////////////////////////////
// Andrew Zundel  >> CS401 >> Assignment 3 //
/////////////////////////////////////////////

// Creates a GUI Pane that allows the user to create files, edit them by placing shapes on the page, //
// and then allowing them to save there work.                                                        //
// All And All It Is A Great Holiday Card Maker                                                      //

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.io.*;
import java.nio.file.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.awt.print.*;
import java.awt.Image;


public class Assig5 {
	
	public static void main(String [] args){
		
		new Assig5();
		
	}
	
	enum Figures {TREE, SNOWFLAKE, GREETING, CLOUD, CABIN, SNOWMAN}
	enum Mode {NONE, DRAW, SELECTED, MOVING}
	
		// forms the basic frame components of the GUI
		private JFrame Window;
		private Picture theCanvas;
		private JMenuBar theBar;
		private JMenu fileMenu, editMenu;
		private JPopupMenu pop;
		// items for file menu options //
		private JMenuItem newScene, open, save, saveAs, saveAsJPEG, print, exit;
		// items for edit menu options //
		private JMenuItem copy, cut, paste;
		// items for pop-up menu options //
		private JMenuItem delete, resize;
		private JPanel radioPanel;
		private JButton modeButton;
		private JLabel msg;
		private JRadioButton makeTree, makeFlake, makeGreet, makeCloud, makeCabin, makeSMan;
		private ButtonGroup shapeGroup;
		private Figures currentFig;
		
		// for shape storage and accessibility
		private ArrayList<MyShape> shapeList;
		private MyShape newShape;
		private int shapeIndex;
		
		// file manipulation
		private boolean activeScene = false;
		private String sceneName;
		private File picFile;
		private PrintWriter picWrite;
		private Scanner picScan;
		private Path source;
		
	public Assig5(){
		
		// creates a panel for the main picture as well as the button panel
		theCanvas = new Picture(640, 480);
		radioPanel = new JPanel();
		
		// creates a button for controlling the mode selection of above pane
		modeButton = new JButton("click to draw");
			ModeListener modeListen = new ModeListener();
		
		modeButton.addActionListener(modeListen);
		radioPanel.add(modeButton);
		
		radioPanel.add(theCanvas);
		msg = new JLabel("");
		radioPanel.add(msg);
		
		// creates radio button selections
		makeTree = new JRadioButton("TREE", true);
		makeFlake = new JRadioButton("SNOWFLAKE", false);
		makeGreet = new JRadioButton("GREETINGS", false);
		makeCloud = new JRadioButton("CLOUD", false);
		makeCabin = new JRadioButton("CABIN", false);
		makeSMan = new JRadioButton("SNOWMAN", false);
			RadioListener radioListen = new RadioListener();
			
		// creates a listener for the radio buttons	
		makeTree.addItemListener(radioListen);
		makeFlake.addItemListener(radioListen);
		makeGreet.addItemListener(radioListen);
		makeCloud.addItemListener(radioListen);
		makeCabin.addItemListener(radioListen);
		makeSMan.addItemListener(radioListen);
		
		// adds the buttons to the panel
		radioPanel.setLayout(new GridLayout(4,3));
			radioPanel.add(makeTree);
			radioPanel.add(makeFlake);
			radioPanel.add(makeGreet);
			radioPanel.add(makeCloud);
			radioPanel.add(makeCabin);
			radioPanel.add(makeSMan);
		
		// adds the shapes to the button grouping
		shapeGroup = new ButtonGroup();
			shapeGroup.add(makeTree);
			shapeGroup.add(makeFlake);
			shapeGroup.add(makeGreet);
			shapeGroup.add(makeCloud);
			shapeGroup.add(makeCabin);
			shapeGroup.add(makeSMan);
			
		// creates an object to test for the selected shape and dictate what mode its in	
		currentFig = Figures.TREE;
		theCanvas.setMode(Mode.NONE);
		
		// adds the panes the a container of the window
		Window = new JFrame("HOLIDAY GREETINGS CARDS");
		Container C = Window.getContentPane();
		theCanvas.setBackground(Color.LIGHT_GRAY);
		C.add(theCanvas, BorderLayout.NORTH);
		C.add(radioPanel, BorderLayout.SOUTH);
		
		// Creates the items for the menu bars of the file menu
		theBar = new JMenuBar();
		Window.setJMenuBar(theBar);
		fileMenu = new JMenu("File");
		newScene = new JMenuItem("New");
		open = new JMenuItem("Open");
		save = new JMenuItem("Save");
		saveAs = new JMenuItem("SaveAs..");
		saveAsJPEG = new JMenuItem("SaveAs JPEG");
		print = new JMenuItem("Print");
		exit = new JMenuItem("Exit");
			fileMenu.add(newScene); 
			fileMenu.add(open);
			fileMenu.add(save);
			fileMenu.add(saveAs);
			fileMenu.add(saveAsJPEG);
			fileMenu.add(print);
			fileMenu.add(exit);
			
			newScene.addActionListener(modeListen);
			open.addActionListener(modeListen);
			save.addActionListener(modeListen);
			saveAs.addActionListener(modeListen);
			saveAsJPEG.addActionListener(modeListen);
			print.addActionListener(modeListen);
			exit.addActionListener(modeListen);
			
		//Creates the items for the menu bar of the edit menu	
		editMenu = new JMenu("Edit");
		copy = new JMenuItem("Copy");
		cut = new JMenuItem("Cut");
		paste = new JMenuItem("Paste");
				paste.setEnabled(false);
			editMenu.add(copy);
			editMenu.add(cut);
			editMenu.add(paste);
			
			copy.addActionListener(modeListen);
			cut.addActionListener(modeListen);
			paste.addActionListener(modeListen);
			
		
		theBar.add(fileMenu);
		theBar.add(editMenu);
		
		// creates the items for the pop up menu
		pop = new JPopupMenu();
			delete = new JMenuItem("Delete");
			resize = new JMenuItem("Resize");
			
			delete.addActionListener(modeListen);
			resize.addActionListener(modeListen);
			
		pop.add(delete);
		pop.add(resize);
		
		Window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		Window.pack();
		Window.setVisible(true);
		
		
	}// Assig5()
	private class ModeListener implements ActionListener{
			
		public void actionPerformed(ActionEvent e){
			
			// event triggered from the panel button press
			if(e.getSource() == modeButton){
				
				// puts frame into draw mode and creates an active scene //
				if(modeButton.getText().equals("click to draw")){
								
					// sets the variables the are caused by button press
					theCanvas.setMode(Mode.DRAW);
					msg.setText("Position new shapes with mouse");
					modeButton.setText("click to edit");
					activeScene = true;
							
				// puts the frame into the standard editing mode //	
				}else if(modeButton.getText().equals("click to edit")){
								
					// sets the variables that are caused by the button press
					theCanvas.setMode(Mode.NONE);
					msg.setText("Edit shapes with the mouse");
					modeButton.setText("click to draw");
					activeScene = true;
								
				}
			
				
// the file menu buttons ------------------------------------------------------//
			// handles if new scene is chosen	
			}else if(e.getSource() == newScene){
				
				int option1 = -6;
				String input, oldScene = null;
				
				if(activeScene){
					
						// the option selection for saving a previous file
						option1 = JOptionPane.showConfirmDialog(Window, "Would You Like To Save The Old Scene? ", "PROMPT", JOptionPane.YES_NO_CANCEL_OPTION);
						
						// saves the old file
						if(option1 == JOptionPane.YES_OPTION){
								
								// saves the old file if the old file has a name
						    	if(sceneName != null){
						    		
						    		try{
						    			
						    			// creates the path to the file and creates the file
										source = Paths.get("directory/" + sceneName);
										picFile = new File(source.toString());
										
						    			picWrite = new PrintWriter(picFile);
						    			
						    			// gains size for looping items to write
						    			picWrite.println(shapeList.size());
						    			for(int i = 0; i < shapeList.size(); i++){
						    				
						    				picWrite.println(shapeList.get(i).saveData());
						    				
						    			}
						    			
						    			picWrite.close();
						    			
						    			sceneName = (String)JOptionPane.showInputDialog(Window, "Please Name Your New Scene:");
						    			msg.setText("The File " + sceneName + " Has Been Created!");
						    			
						    			// wipes the pane for new shapes to be added
						    			shapeList.clear();
						    			theCanvas.repaint();
						    			
						    		}catch(FileNotFoundException fnf){
						    			
						    			JOptionPane.showMessageDialog(Window, "The File " + sceneName + " Doesn't Exist"); 
						    			System.exit(0);
						    			
						    		}
						    	
						    	// saving the previously unnamed file
						    	}else{
						    		
						    		boolean illegalName = true;
						    		
						    		// loops through until a legal name is entered
						    		while(illegalName){
						    	
						    		try{
						    			
						    			oldScene = (String)JOptionPane.showInputDialog(Window, "Please name the file?");
						    			// creates the path to the file and creates the file
										source = Paths.get("directory/" + oldScene);
										
										// creates the file inside the directory
										Files.createFile(source);
										
										picFile = new File(source.toString());
										
						    			picWrite = new PrintWriter(picFile);
						    			
						    			// gains the size and loops around the data to write to file
						    			picWrite.print(shapeList.size());
						    			for(int i = 0; i < shapeList.size(); i++){
						    				
						    				picWrite.print(shapeList.get(i).saveData());
						    				
						    			}
						    			
						    			picWrite.close();
						    			
						    			// prepares the pane for new data to be written
						    			shapeList.clear();
						    			theCanvas.repaint();
						    			illegalName = false;
						    			
						    			
						    		}catch(FileNotFoundException fnf){
						    			
						    			JOptionPane.showMessageDialog(Window, "The file " + oldScene + " Was Not Found"); 
						    			System.exit(0);
						    			
						    		}catch(IOException ioe){
						    			
						    			JOptionPane.showMessageDialog(Window, "The file" + oldScene +  "is invalid!");
						    			illegalName = true;
						    		}
						    		
						    	}
						    
						    }
							   
						}else if(option1 == JOptionPane.NO_OPTION){
							
							// immediately asks for the input of the new file to create
							input = (String)JOptionPane.showInputDialog(Window, "What is the name of your new scene?");
							if ((input != null) && (input.length() > 0)) {
								
								// sets the new name of the file to create and then clears the pane for adding new variables
								sceneName = input;
								activeScene = true;
								shapeList.clear();
								theCanvas.repaint();
								msg.setText("New Filename " + sceneName + " Created! Save when ready!");
								
							}else{
								
								//handles a cancel option for new file 
								//still doesn't save previous files
								sceneName = input;
								activeScene = true;
								shapeList.clear();
								theCanvas.repaint();
							}
							
						}else if(option1 == JOptionPane.CANCEL_OPTION){
							
							// nothing is done //
							msg.setText("Job Canceled");
							
						}
					
				}else{
					
					// asks for input of a new scene
					input = (String)JOptionPane.showInputDialog(Window, "What is the name of your new scene?");
					if ((input != null) && (input.length() > 0)) {
						
						// names the new file and clears the pane for new file name
						sceneName = input;
						shapeList.clear();
						theCanvas.repaint();
						activeScene = true;
						msg.setText("New Filename " + sceneName + " Created! Save when ready!");
						
					}else{
						
						// handles the cancel option and does nothing when clicked
						
					}
				}	
			
			// handles opening an existing file	data
			}else if(e.getSource() == open){
				
				boolean illegalName = true;
				String openScene = null, shape, text = "Edit Text";
				int x, y, size;
				int fileNum = 0;
				
				// itterates for a legal name
				while(illegalName){
				
					try{
						
						// gets the scene name
						openScene = (String)JOptionPane.showInputDialog(Window, "What is the scene name?");
				
					if ((openScene != null) && (openScene.length() > 0)) {
						
					//paths to the file and creates a scanner to read in data	
					source = Paths.get("directory/" + openScene);
					picFile = new File(source.toString());
					picScan = new Scanner(picFile);
					
					// make a new array list to store the shapes to be inputed
					shapeList = new ArrayList<MyShape>();
						
					// takes in the first variable size of the file 
					shape = picScan.nextLine();
					
					// converts it to a integer
					fileNum = Integer.parseInt(shape);
					illegalName = false;
					
					msg.setText("Scene " + openScene + " Now Open!");
					
					
					}else{
						
						msg.setText("Job Canceled");
						illegalName = false;
						//no job is done in this case
						
					}
					
					}catch(FileNotFoundException fnf){
						
						JOptionPane.showMessageDialog(Window, "The File " + openScene + " Was Not Found!");
						illegalName = true;
						
					}
				}
				
					// sets the scene name to current newly open scene
					sceneName = openScene;
					for(int i = 0; i < fileNum; i++){
					
						// makes array of string to store the data
						String [] shaping = picScan.nextLine().split(":");
						
						// sets the current figure, the x, the y, and the size from split data
						currentFig = Figures.valueOf(shaping[0].toUpperCase());
						x = Integer.parseInt(shaping[1]);
						y = Integer.parseInt(shaping[2]);
						size = Integer.parseInt(shaping[3]);
						// if the shape has a size of 5 it assumes it has the text component
						if(shaping.length == 5){
							text = shaping[4];
						} // this is to avoid throwing a error for array out of bounds
						
							// Based on the figure select it creates the appropriate shape
							if(currentFig == Figures.TREE){
								
								newShape = new Tree(x, y, size);
								
							}else if(currentFig == Figures.SNOWFLAKE){
								
								newShape = new Snowflake(x, y, size);
								
							}else if(currentFig == Figures.GREETING){
								
								newShape = new Greeting(x, y ,size, text);
								
							}else if(currentFig == Figures.CLOUD){
								
								newShape = new Cloud(x, y, size);
								
							}else if(currentFig == Figures.CABIN){
								
								newShape = new Cabin(x, y, size);
								
							}else if(currentFig == Figures.SNOWMAN){
								
								newShape = new Snowman(x, y, size);
								
							}
						
						// adds shape and refreshes the canvas
						theCanvas.addShape(newShape);
						theCanvas.repaint();
						activeScene = true;
						
					}
				
					
				
			// handles saving the current file data	
			}else if(e.getSource() == save){
				
				// acts on whether the scene has a name or not yet
				if(sceneName == null){
					
					String input;
					boolean illegalName = true;
					
					// loops until finding an appropriate name
					while(illegalName){
						
						input = (String)JOptionPane.showInputDialog(Window, "What is the name of your new scene?");
						sceneName = input;
							
						if ((input != null) && (input.length() > 0)) {
						msg.setText("File " + sceneName + " Saved!");
						
						try{
							
							// creates the path to the file and creates the file
							source = Paths.get("directory/" + sceneName);
							Files.createFile(source);
							picFile = new File(source.toString());
							
							picWrite = new PrintWriter(picFile);
							
							// writes the size of the items and loops based on how many items
							picWrite.println(shapeList.size());
							
			    				for(int i = 0; i < shapeList.size(); i++){
			    				
			    					picWrite.println(shapeList.get(i).saveData());
			    				
			    				}
			    			
			    			picWrite.close();
			    			illegalName = false;
							
							
						}catch(FileNotFoundException fnf){
							
							JOptionPane.showMessageDialog(Window, "Something Went Wrong Exiting");
							
						}catch(IOException ioe){
							
							JOptionPane.showMessageDialog(Window, "That file name already exists(or is invalid) please enter another");
							illegalName = true;
						}
					
						}else{
							
							// does nothing when cancel is selected
							
						}
					}
					
				}else{
					
			
						try{
							
							// creates the path to the file and rewrites the file
							source = Paths.get("directory/" + sceneName);
							picFile = new File(source.toString());
							
							picWrite = new PrintWriter(picFile);
							
							// finds size to loop the shapes input around
							picWrite.println(shapeList.size());
			    			for(int i = 0; i < shapeList.size(); i++){
			    				
			    				picWrite.println(shapeList.get(i).saveData());
			    				
			    			}
			    			
			    			picWrite.close();
							msg.setText("File " + sceneName + " Saved!");
							
						}catch(FileNotFoundException fnf){
							
							JOptionPane.showMessageDialog(Window, "The File " + sceneName + " Doesn't Exist");
							
						}
		
			}
				
			// handles saving the current file data to a new file	
			}else if(e.getSource() == saveAs){
				
				String input;
				boolean overwrite = true;
				boolean illegalName = true;
				
				// loops until legal name found
				while(illegalName){
				
					input = (String)JOptionPane.showInputDialog(Window, "What is the name of your new scene?");
					
					if ((input != null) && (input.length() > 0)) {
						if(sceneName != null){
					
							// tells the rest of saveAs function whether it must overwrite or not
							if(input.equals(sceneName)){
						
								overwrite = true;
						
							}else{  overwrite = false;   }
						
						}	
					
					sceneName = input;
					
					try{
						
						// creates the path to the file and creates the file
						source = Paths.get("directory/" + sceneName);
						
						// if the file must be created
						if(!overwrite){
						Files.createFile(source);
						}
						
						picFile = new File(source.toString());
						
						picWrite = new PrintWriter(picFile);
						
						// gets the size of the list to loop around
						picWrite.println(shapeList.size());
		    			for(int i = 0; i < shapeList.size(); i++){
		    				
		    				picWrite.println(shapeList.get(i).saveData());
		    				
		    			}
		    			
		    			picWrite.close();
		    			illegalName = false;
						msg.setText("File " + sceneName + " Saved!");
						
					}catch(FileNotFoundException fnf){
						
						JOptionPane.showMessageDialog(Window, "The File " + input + " Doesn't Exist!");
						System.exit(0);
						
					}catch(IOException ioe){
						
						JOptionPane.showMessageDialog(Window, "That file " + input + " already exists(or is invalid) please enter another");
						illegalName = true;
						
					}
					
				}else{
					
					// handles the cancel option
					
				}
					
				}	
			// handles saving the current file data as a JPEG
			// WARNING IT WORKS BUT HAS A SLIGHT HUE OVER THE JPEG, NOT SURE WHY!!	
			}else if(e.getSource() == saveAsJPEG){
				
				// creates a buffered image to store the JPEG
				BufferedImage BI = new BufferedImage(640, 480, BufferedImage.TYPE_INT_ARGB);
				// creates graphics for the picture
				Graphics g = BI.createGraphics();
				// paints to the file from the canvas
				theCanvas.paint(g);
				g.dispose();
				
				// asks user to name JPEG
				String jpegName = (String)JOptionPane.showInputDialog(Window, "Please name your JPEG file(ex. card.jpg) ");
				
				// paths the file into the directory
				source = Paths.get("directory/" + jpegName);
				
				try{
					
					// writes the image to the file type specified below
					ImageIO.write(BI, "jpg", new File(source.toString()));	
					msg.setText(jpegName + " was saved as JPEG!");
					
				}catch(Exception ex){
					
					JOptionPane.showMessageDialog(Window, "An Exception Was Thrown, Invalid File!");
					
				}
				
				
				
				
				
			// handles printing the current file data out of a printer	
			}else if(e.getSource() == print){
				
				// prepares the canvas to be printed and grabs the printing job
				Printable thePPanel = new thePrintPanel(theCanvas);
				PrinterJob job = PrinterJob.getPrinterJob();
				job.setPrintable(thePPanel);
				
				// if ok is true the job will print out otherways will not
				boolean ok = job.printDialog();
				if(ok){
					
					try{
						
						job.print();
						
					}catch(PrinterException pe){
						
						JOptionPane.showMessageDialog(Window, "Printing Was Not Possible");
						
					}
					
				}else{
					
					//Nothing is printed
					
				}
				
				
			// handles exiting the program	
			}else if(e.getSource() == exit){
				
				JOptionPane.showMessageDialog(Window, "HAPPY HOLIDAYS!!!");
				System.exit(0);
				
//the edit file menu items----------------------------------------------------//			
			// handles copying the current object selected	
			}else if(e.getSource() == copy){
				
				if(theCanvas.mode == Mode.SELECTED){
					
					String shapes;
					String [] shaping;
					int x, y, size;
					
					// sets the next state conditions of the edit menu
					paste.setEnabled(true);
					cut.setEnabled(false);
					copy.setEnabled(false);
			
					msg.setText("Shape copied into clipboard(ready to paste)");
					
					// grabs the current selected items index and stores the shape in newShape
					shapeIndex = theCanvas.getSelected(theCanvas.x1, theCanvas.y1);
					newShape = shapeList.get(shapeIndex);
					
					// the shape is outputed into string format
					shapes = newShape.saveData();
					
					// splits the shapes into it variables
					shaping = shapes.split(":");
					currentFig = Figures.valueOf(shaping[0].toUpperCase());
					
						x = Integer.parseInt(shaping[1]);
						y = Integer.parseInt(shaping[2]);
						size = Integer.parseInt(shaping[3]);
					
					// copies data of correct variable into the new variable
					if(currentFig == Figures.TREE){
						
						newShape = new Tree(x,y,size);
						
					}else if(currentFig == Figures.SNOWFLAKE){
						
						newShape = new Snowflake(x, y, size);
						
					}else if(currentFig == Figures.GREETING){
						
						// if text is present it will be held within shaping[4]
						newShape = new Greeting(x, y ,size, shaping[4]);
						
					}else if(currentFig == Figures.CLOUD){
						
						newShape = new Cloud(x, y, size);
						
					}else if(currentFig == Figures.CABIN){
						
						newShape = new Cabin(x, y, size);
						
					}else if(currentFig == Figures.SNOWMAN){
						
						newShape = new Snowman(x, y, size);
						
					}

					
				}else if(theCanvas.mode == Mode.NONE || theCanvas.mode == Mode.MOVING){
					
					paste.setEnabled(false);
					cut.setEnabled(true);
					copy.setEnabled(true);
					
					// nothing else is done given the copy command in this case
					
				}
				
			// handles cutting out the current object selected	
			}else if(e.getSource() == cut){
				
				if(theCanvas.mode == Mode.SELECTED){
					
					// sets initial conditions of cuts initiation
					paste.setEnabled(true);
					cut.setEnabled(false);
					copy.setEnabled(false);
					
					msg.setText("Shape cut into clipboard(ready to paste)");
					
					// gets the current shape, stores it as new shape, and deletes the selected shape
					shapeIndex = theCanvas.getSelected(theCanvas.x1, theCanvas.y1);
					newShape = shapeList.get(shapeIndex);
					theCanvas.deleteSelected();
					theCanvas.repaint();
					
				}else if(theCanvas.mode == Mode.NONE || theCanvas.mode == Mode.MOVING){
					
					paste.setEnabled(false);
					cut.setEnabled(true);
					copy.setEnabled(true);
					
					// nothing else is done in response to the cut command
					
				}
				
			// handles pasting the current object copied or cut	
			}else if(e.getSource() == paste){
				
				// uses move to tract the last clicked location to cut or copy to
				newShape.move(theCanvas.x1, theCanvas.y1);
				
				// adds the new shape whether cut or copied to the new location
				theCanvas.addShape(newShape);
				theCanvas.repaint();
				
				// finalizes with new state variables
				msg.setText("Shape has been pasted");
				paste.setEnabled(false);
				copy.setEnabled(true);
				cut.setEnabled(true);
				
// the popup file menu items-------------------------------------------------//
			// handles deleting an item right clicked on	
			}else if(e.getSource() == delete){
				
				// checks if the shape was deleted
				boolean ans = theCanvas.deleteSelected();
				if(ans){
					
					// if yes notifies the user
					msg.setText("Shape Deleted");
					theCanvas.repaint();
					
				}
				
			// handles resizing an item right clicked on	
			}else if(e.getSource() == resize){
				
				boolean nono = true;
				
				// loops for a valid resizing 
				while(nono){
					
					try{
						// asks for the size 
						String input = JOptionPane.showInputDialog(Window, "Please Enter New Size:");
					
						if ((input != null) && (input.length() > 0)) {
							
							// parses to the int variable
							int size = Integer.parseInt(input);
						
							// recreates the shape with resized parameters
							shapeIndex = theCanvas.getSelected(theCanvas.x1, theCanvas.y1);
							shapeList.get(shapeIndex).resize(size);
							theCanvas.repaint();
							nono = false;
							
						}else{
							
							// handles cancel option
							
						}
						
					}catch(Exception ex){ JOptionPane.showMessageDialog(Window, "The size was not an int! Please Try Again");  }
				
				}
			}
		}
	  }
			
	private class RadioListener implements ItemListener{
		
		public void itemStateChanged(ItemEvent e){
			
			// control from the radio buttons of each figure to draw
			if(e.getSource() == makeTree){
				
				currentFig = Figures.TREE;
				
			}else if(e.getSource() == makeFlake){
				
				currentFig = Figures.SNOWFLAKE;
				
			}else if(e.getSource() == makeGreet){
				
				currentFig = Figures.GREETING;
				
			}else if(e.getSource() == makeCloud){
				
				currentFig = Figures.CLOUD;
				
			}else if(e.getSource() == makeCabin){
				
				currentFig = Figures.CABIN;
				
			}else if(e.getSource() == makeSMan){
				
				currentFig = Figures.SNOWMAN;
				
			}
			
			
		}
	}
	private class Picture extends JPanel{
		
		// initializes variables for the picture panel
		private int prefwid, prefht;
		private int selindex;
		private int x1, y1, x2, y2;
		private boolean popped;
		private Mode mode;
		
		public Picture(int pw, int ph){
			
			// creates a new arraylist for shapes stored in the picture 
			shapeList = new ArrayList<MyShape>();
			selindex = -1;
			
			// sets the prefered height and width and makes it visible
			prefwid = pw;
			prefht = ph;
			setOpaque(true);
			setBackground(Color.LIGHT_GRAY);
			
			addMouseListener(new MyMouse());
			addMouseMotionListener(new MouseMotion());
			popped = false;
			
		}
		private class MyMouse extends MouseAdapter{
		
			public void mousePressed(MouseEvent e){
				
				// gets location of the mouse press
				x1 = e.getX();
				y1 = e.getY();
				
				if(!e.isPopupTrigger() && (mode == Mode.NONE || mode == Mode.SELECTED)){
					
					if(selindex >= 0){
						
						unSelect();
						mode = Mode.NONE;
					
					}
					
					selindex = getSelected(x1, y1);
					
					if(selindex >= 0){
						
						mode = Mode.SELECTED;
						
						MyShape curr = shapeList.get(selindex);
							if(curr instanceof MyText && e.getClickCount() == 2){
					
								String newText = JOptionPane.showInputDialog(Window, "Enter new text (cancel for no change):");
								
								if ((newText != null) && (newText.length() > 0)) {
									
									// sets text if there is text present within the shape
									if(newText != null){
									
										((MyText)curr).setText(newText);
									
									}
									
								}else{
									
									// handles the cancel option
									
								}
							}
						repaint();
						
					}else if(e.isPopupTrigger() && selindex >= 0){
						
						// trigers the popup menu at current mouse location
						pop.show(Picture.this, x1, y1);
						popped = true;
						
					}
					
				}
				
			} // mouseDrug
			public void mouseReleased(MouseEvent e){
					
				// if the mode of the Picture is in draw mode
				if(mode == mode.DRAW){
					
					// creates the figure present on the release of a mouse
					if(currentFig == Figures.TREE){
						
						newShape = new Tree(x1, y1, 50);
						
					}else if(currentFig == Figures.SNOWFLAKE){
						
						newShape = new Snowflake(x1, y1, 10);
						
					}else if(currentFig == Figures.GREETING){
						
						newShape = new Greeting(x1, y1 ,30);
						
					}else if(currentFig == Figures.CLOUD){
						
						newShape = new Cloud(x1, y1, 50);
						
					}else if(currentFig == Figures.CABIN){
						
						newShape = new Cabin(x1, y1, 100);
						
					}else if(currentFig == Figures.SNOWMAN){
						
						newShape = new Snowman(x1, y1, 50);
						
					}
					
					addShape(newShape);
					
				// deals with moving the object	
				}else if(mode == Mode.MOVING){
					
					mode = Mode.NONE;
					unSelect();
					modeButton.setEnabled(true);
					repaint();
					
				// shows popup menu at current mouse location	
				}else if(e.isPopupTrigger() && selindex >= 0){
					
					pop.show(Picture.this, x1, y1);
					
				}
					
				popped = false;
					
			}
		
		} // MyMouse
		private class MouseMotion extends MouseMotionAdapter{
		
			public void mouseDragged(MouseEvent e){
			
				// tracks the mouse when the mouse is pressed and dragged
				x2 = e.getX();
				y2 = e.getY();
			
				if((mode == Mode.SELECTED || mode == Mode.MOVING) && !popped){
					
					try{
						
						// temporarily makes a shape moving it to the dragged location
						MyShape s = shapeList.get(selindex);
						mode = Mode.MOVING;
						s.move(x2,y2);
						
					}catch(ArrayIndexOutOfBoundsException aio){
						
						// When Cutting An Object An Exception Is Caught Here Because
						// The Index Was Changed Momentarily To Cut The Object
						
					}
					
				}
				repaint();
			
			}
		
		}// MouseMotion
		private int getSelected(double x, double y){
		
			// loops to find the current selected object and return the index
			for(int i = 0; i < shapeList.size(); i++){
			
				if(shapeList.get(i).contains(x, y)){
				
					shapeList.get(i).highlight(true);
					return i;
				
				}
			
			}
			
		return -1;
		}
		public void unSelect(){
		
			// unselects the figure
			if(selindex >= 0){
			
				shapeList.get(selindex).highlight(false);
				selindex = -1;
				
			}
		
		}
		public boolean deleteSelected(){
			
			if(selindex >= 0){
				
				// removes the shape at the current given index location
				shapeList.remove(selindex);
				selindex = -1;
				return true;
				
			}else{ return false; }
			
		}
		public void setMode(Mode newMode){
			
			mode = newMode;
			
		}
		public void addShape(MyShape newshape){
			
			shapeList.add(newshape);
			repaint();
			
		}
		public Dimension getPreferredSize(){
			
			return new Dimension(prefwid, prefht);
			
		}
		public void paintComponent(Graphics g){
			
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
			for(int i = 0; i < shapeList.size(); i++){
				
				shapeList.get(i).draw(g2d);
			}
			
		}
} // Picture
class thePrintPanel implements Printable{
		
		JPanel panelToPrint;
		
		public int print(Graphics g, PageFormat pf, int page) throws PrinterException{
			
	        if (page > 0) { /* We have only one page, and 'page' is zero-based */
	            return NO_SUCH_PAGE;
	        }

	        /* User (0,0) is typically outside the imageable area, so we must
	         * translate by the X and Y values in the PageFormat to avoid clipping
	         */
	        Graphics2D g2d = (Graphics2D)g;
	        AffineTransform t = new AffineTransform();
	        t.scale(0.9, 0.9);
	        g2d.transform(t);
	        g2d.translate(pf.getImageableX(), pf.getImageableY());

	        /* Now print the window and its visible contents */
	        panelToPrint.printAll(g);

	        /* tell the caller that this page is part of the printed document */
	        return PAGE_EXISTS;
	    }
	    
	    public thePrintPanel(JPanel p)
	    {
	    	panelToPrint = p;
	    }
	}
}
