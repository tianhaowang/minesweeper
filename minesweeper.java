//Tianhao Wang && Estelle Chung
//ICS3U
//Helen Strelkovska
//Summative
//MineSweeper Final Project

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class minesweeper{
	static{ //change the look and feel
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch(Exception e) {}
	}
	//initializing global variables
	private static JPanel menupanel, gamepanel; //creates menu panel and game panel
	private static JFrame f; //main frame
	private static JMenuBar mb; //main menubar
	private static JLabel wintext; //star for winning
	private static int [] bombx; //bomb x intercept
	private static int [] bomby; //bomb y intercept
	private static int rows = 10, columns = 10, bombcount=10, turnNum=1; // rows of bomb, column of bombs,amount of bombs and turnee
	private static int bombmap[][]; //bombmap with bombs and numbers
	private static JButton [][] buttons; //buttons
	private static boolean continuesright[][]; //right buttons clickability
	private static boolean continuesleft[][];  // left buttons clickability
	private static boolean visited [][]=new boolean [rows][columns]; // visited units
	private static Icon flag=new ImageIcon("flag.jpg"); //flag icon
	private static int winWin = 0; //win condition
	
	public static void menubar(){ //initializing menubar
		mb = new JMenuBar();
		
		mb.setLayout(new BorderLayout());

        JMenu file = new JMenu("File"); // adding menu element
		
        JMenuItem exit = new JMenuItem("Exit"); // adding menu items
        
        exit.addActionListener(new ActionListener(){ //exit system
			public void actionPerformed(ActionEvent e){
				System.exit(0);
			}
        });
		
		mb.add(file,BorderLayout.WEST); // add it west side of menu
		
		JMenuItem mainscreen = new JMenuItem("Mainmenu");
		
		mainscreen.addActionListener(new ActionListener(){ //adds action to restart game
			public void actionPerformed(ActionEvent e){
				resetgame();
				try {
					menu();
				} catch (Exception ex) {}
			}
		});
		
		file.add(mainscreen); // adds the elements onto the menu bar
		
		file.add(exit);
		
        f.setJMenuBar(mb);	//sets the menubar
	}
	
	public static void instruction(){ //displays instructions on mainmenu
		JFrame instruction = new JFrame();
		JPanel instructionpic = new JPanel();
		ImageIcon inimage = new ImageIcon("instructions.jpg");
        JLabel inlabel = new JLabel(inimage);
		instructionpic.add(inlabel);
		instruction.setLayout(new BorderLayout());
		instruction.add(instructionpic, BorderLayout.CENTER);
		instruction.pack();
		instruction.setTitle("INSTRUCTIONS");
		instruction.setVisible(true);
	}
	
	public static void map(){ //generates the bomb map
		bombmap = new int[rows][columns];
		bombx = new int[bombcount];
		bomby = new int[bombcount];
		
		for(int i=0; i<bombcount; i++){	// generates ten bombs in random locations
			bombx[i]= (int)(Math.random()*10);
			bomby[i]= (int)(Math.random()*10);
			if(bombmap[bombx[i]][bomby[i]] == -1){
				i--;
			}
			else bombmap[bombx[i]][bomby[i]] = -1; //places bombs as -1
		}
		
		for(int i=0; i<rows;i++){	// places the numbers around the bombs
			for(int j=0; j<columns; j++){
				if(bombmap[i][j]!=-1){
					int count = 0;
					for(int k = i-1; k<=i+1; k++){
						for(int l = j-1; l<=j+1; l++){
							try{
								if(bombmap[k][l]==-1){
									count++;
								}	
							}catch (ArrayIndexOutOfBoundsException e){}
						}
					}bombmap[i][j]=count;
				}
			}
		}
	}	
	
	public static void resetgame(){ //resets game stats
		gamepanel.setVisible(false);
		turnNum=1;
		winWin=0;
		for(int i=0; i<rows; i++){ //resets clickability
			for(int j=0; j<columns; j++){
				continuesleft[i][j]=true;
				continuesright[i][j]=true;
			}
		}
		for(int i=0;i<rows;i++){ //resets visited array
			for (int j=0;j<columns;j++) {
				visited[i][j]=false;
			}
		}		
	}
	
    public static void menu() throws Exception{ //the main menu
        
		menubar();
		
        menupanel = new JPanel(new GridBagLayout());

        JButton start = new JButton("Start (10)"); //if user decides to play with 10 bombs (easy mode)

        start.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
				bombcount=10;
				menupanel.setVisible(false);
                game();	
            }
        });
		
		JButton start2 = new JButton("Start(15)"); //if user decides to play with 15 bombs (medium mode)
		
		start2.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
				bombcount=15;
				menupanel.setVisible(false);
                game();	
            }
        });
		
		JButton start3 = new JButton("Start (20)	"); //if user decides to play with 20 bombs (hard mode)
		
		start3.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
				bombcount=20;
				menupanel.setVisible(false);
                game();	
            }
        });
		
		JButton instructions = new JButton("Instructions");
		
		instructions.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				instruction();
			}
		});
		
        GridBagConstraints con = new GridBagConstraints();

        ImageIcon image = new ImageIcon("logo.JPG");  //displays the image on the mainmenu
        JLabel label = new JLabel(image);
		JPanel m = new JPanel();
        con.gridx = 0;	//setting coord
        con.gridy = 0;
        menupanel.add(label,con);
        con.insets = new Insets(10,10,10,10); //seperates it by 10 pixels
        m.add(start);
		m.add(start2);
		m.add(start3);
		m.add(instructions);
		con.gridy = 5; con.gridx = 0; 
		con.gridwidth = 1; con.gridheight = 1; 
		menupanel.add(m,con);	
        f.add(menupanel);
        f.setVisible(true);

	}
	
    public static void game(){//main game panel

		JButton replay = new JButton(); // replay button
		Icon re =new ImageIcon("retry.png");
		replay.setIcon(re);
		
		replay.addActionListener(new ActionListener(){ //replay action
			public void actionPerformed(ActionEvent e){
				menubar();
				resetgame();
				try {
					game();
				} catch (Exception ex) {}
			}
		});

		mb.add(replay, BorderLayout.EAST); // add button
		
		continuesleft = new boolean[rows][columns]; // initializing button clickability
		continuesright = new boolean[rows][columns];
		for(int i=0; i<rows; i++){
			for(int j=0; j<columns; j++){
				continuesleft[i][j]=true;
				continuesright[i][j]=true;
			}
		}
        gamepanel = new JPanel();
		gamepanel.setBackground( new Color(204,255,255));	
		gamepanel.setLayout(new GridLayout(rows,columns));
		buttons = new JButton[rows][columns];
		map();
		for(int i=0; i<buttons.length;i++){ //initializing/ placing buttons
			for(int j=0; j<buttons[0].length;j++){
				buttons[i][j] = new JButton("");
				gamepanel.add(buttons[i][j]);
				clicked(buttons[i][j],i,j); //adding action
			}
		}
		f.add(gamepanel,BorderLayout.CENTER);
		gamepanel.setVisible(true); 
    }
	public static void clicked(JButton buttons, int x,int y){ 	//method that is used when player clicks a button
		buttons.setFocusPainted(false); //disables the blue focus on the top left button for the method
		buttons.addMouseListener(new MouseAdapter(){ 
			public void mouseClicked(MouseEvent e){ 
				if(continuesright[x][y]){ // if the button is clibable both right and left
					if (e.isMetaDown()) { //if it is a right click, displays flag
						 //if there is no icon
						if (buttons.getIcon()==(null)){
							buttons.setIcon(flag);
							continuesleft[x][y]=false; //left button clickability 
						}
						else { //if the icon is already a flag
							buttons.setIcon(null);
							continuesleft[x][y]=true; //left button clickability 
						}
					} 
					else if(continuesleft[x][y]){ //left button clickability 
						if (buttons.getIcon()==(flag)) //if a flag is clicked
							buttons.setEnabled(false);
						if (turnNum == 1 && bombmap[x][y]!=0){ //if bomb or number is clicked on first click
							while(bombmap[x][y]!=0){ // regenerates map
								map();
							}
							turnNum++;
							if(bombmap[x][y]==0){ // reveals
								search(x,y);
							}
						}
						else if (bombmap[x][y]==0){
							search(x,y);
							turnNum++;
						}
						else {
							setpic(buttons,x,y);
							turnNum++;
						}
						continuesright[x][y] = false; // disables button
					}
				}
				for(int a = 0; a < rows; a++){
					for(int b = 0; b < columns; b++){
						if(visited[a][b]){
							winWin++;
						}
					}
				}// win condition for every space that is not a mine checked add one
				
				if(winWin >= ((rows*columns) - bombcount)){// when win condition is met (in this case if everything but the bomb is clicked) the game is over
					wintext = new JLabel(new ImageIcon("win.png"));
					mb.add(wintext, BorderLayout.CENTER);
					for(int i=0; i< rows; i++){ //disables all buttons
						for(int j=0; j<columns; j++){
							continuesright[x][y]=false;
							continuesleft[x][y]= false;
						}
					}
				}
				else winWin = 0; // reset win counter if win criteria is not met
			}
		});
	}
	public static void setpic(JButton buttons, int x, int y){//method is used to change the picture when a button is clicked
		continuesright[x][y]=false;
		continuesleft[x][y]= false;
		visited[x][y]=true;
		if (bombmap[x][y]==1) { //if a 1 is clicked
			Icon one =new ImageIcon("1-01.png");
			buttons.setIcon(one);
		}
		else if (bombmap[x][y]==2) { //if a 2 is clicked
			Icon two=new ImageIcon("2-01.png");
			buttons.setIcon(two);
		}
		else if (bombmap[x][y]==3) { //if a 3 is clicked
			Icon three=new ImageIcon("3-01.png");
			buttons.setIcon(three);
		}
		else if (bombmap[x][y]==4) { //if a 4 is clicked
			Icon four=new ImageIcon("4-01.png");
			buttons.setIcon(four);
		}
		else if (bombmap[x][y]==5) { //if a 5 is clicked
			Icon five=new ImageIcon("5-01.png");
			buttons.setIcon(five);
		}
		else if (bombmap[x][y]==6) { //if a 6 is clicked
			Icon six=new ImageIcon("6-01.png");
			buttons.setIcon(six);
		}
		else if (bombmap[x][y]==7) { //if a 7 is clicked
			Icon seven=new ImageIcon("7.png");
			buttons.setIcon(seven);
		}
		else if (bombmap[x][y]==8) { //if a 8 is clicked
			Icon eight=new ImageIcon("8-01.png");
			buttons.setIcon(eight);
		}
		else if (bombmap[x][y]==0){ //if an empty space is clicked
			buttons.setVisible(false);
			search(x,y);
		}
		else { //if a bomb is clicked
			rev(); //reveals all the other bombs
			for(int i=0; i<rows; i++){
				for(int j=0; j<columns; j++){
					continuesleft[i][j]=false;
					continuesright[i][j]=false;
				}
			}
		}	
	}
	public static void search (int r, int c){//method is used when a 0 is clicked, revealing many squares
		continuesright[r][c]=false;
		continuesleft[r][c]= false;
			if (bombmap[r][c]==0){ //shows spaces
				buttons[r][c].setVisible(false);
			}
			
			else if (bombmap[r][c]==1) { //displays a 1
				Icon num =new ImageIcon("1-01.png");
				buttons[r][c].setIcon(num);
			}
			else if (bombmap[r][c]==2) {//displays a 2 
				Icon num=new ImageIcon("2-01.png");
				buttons[r][c].setIcon(num);
					}
			else if (bombmap[r][c]==3) {//displays a 3
				Icon num=new ImageIcon("3-01.png");
				buttons[r][c].setIcon(num);
			}
			else if (bombmap[r][c]==4) {//displays a 4
				Icon num=new ImageIcon("4-01.png");
				buttons[r][c].setIcon(num);
			}
			else if (bombmap[r][c]==5) {//displays a 5
				Icon num=new ImageIcon("5-01.png");
				buttons[r][c].setIcon(num);
			}
			else if (bombmap[r][c]==6) {//displays a 6
				Icon num=new ImageIcon("6-01.png");
				buttons[r][c].setIcon(num);
			}
			else if (bombmap[r][c]==7) { //displays a 7
				Icon num=new ImageIcon("7.png");
				buttons[r][c].setIcon(num);
			}
			else if (bombmap[r][c]==8) {//displays an 8
				Icon num=new ImageIcon("8-01.png");
				buttons[r][c].setIcon(num);
			}
			if(bombmap[r][c]>0){
				visited[r][c]=true;
				return; //stops revealing 
			}
			visited[r][c]=true;	//makes visited boolean array true in order to stop repetition
			
			//recursion 
			if (r-1>-1 &&visited[r-1][c]==false) { //checks square above
			search(r-1,c);
			}
			if (r+1<10 && visited[r+1][c]==false){ //checks square below
			search(r+1,c);
			}
			if (c-1>-1&& visited[r][c-1]==false){ //checks left square
			search(r,c-1);
			}
			if (c+1<10&& visited[r][c+1]==false) {//checks right square
			search(r,c+1);
			}
			if (r+1<10 && c+1<10 && visited[r+1][c+1]==false) { //checks bottom right diagonal
				search(r+1, c+1);
			}
			if (r+1<10 && c-1>-1 && visited[r+1][c-1]==false){ //checks bottom left diagonal
				search(r+1, c-1);
			}
			if (r-1>-1 && c+1<10 && visited[r-1][c+1]==false) { //checks top right diagonal
				search(r-1, c+1);
			}
			if (r-1>-1 && c-1>-1 && visited[r-1][c-1]==false) { //checks top left diagonal
				search(r-1, c-1);
			}
				return;

	}
	public static void rev(){ //reveals all bombs
	  for(int i=0; i<bombcount; i++){
		   Icon mine=new ImageIcon("mine.png");
		   Icon xBomb = new ImageIcon("minex.png");
		   if(buttons[bombx[i]][bomby[i]].getIcon() == null){ //if at the end of the game a bomb is not flagged
			buttons[bombx[i]][bomby[i]].setIcon(mine);//sets icon to bomb
		   }
		   if (buttons[bombx[i]][bomby[i]].getIcon().equals(flag)) { //if there is a flag over a bomb location at the end
			buttons[bombx[i]][bomby[i]].setIcon(xBomb);  //displays a crossed out bomb
		   }
		 }
	 }
    public static void main(String[] args) throws Exception { //opening window main class
		f = new JFrame();
        f.setSize(500,500);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setTitle("MineSweeper");
        menu();
    }
}