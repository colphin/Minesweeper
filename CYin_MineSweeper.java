/* Calvin Yin
 * Period 4 
 * Feb 12 2012
 * 123456789012345678901234567890123456789012345678901234567890123456789
 * This lab took me forever 10 hour give or take 1 maybe??
 * I used a 1-d array and JButtons to create the board. The hardest 
 * thing for me was to make it so that it would check correctly that
 * everything was okay and the person would have won, also the fill
 * number() one was annoying. I had to check the boundary, the mines 
 * to get it to work. I have literally no idea why the timer is broken.
 */

import java.awt.* ;
import java.awt.event.* ;
import javax.swing.* ;
import javax.swing.event.* ;
import java.io.IOException;

public class CYin_MineSweeper extends JFrame implements ActionListener, MouseListener{
    int rows = 10, cols = 10;
    int mineNum = 10;
    GridLayout layout = new GridLayout (rows, cols);
    boolean [] mines = new boolean [rows*cols];
    boolean [] possible = new boolean [rows * cols];
    boolean lost = false;
    boolean won = false;
    int [] numbers =  new int [rows*cols];
    JButton [] buttons = new JButton [rows*cols];
    boolean [] clickDone = new boolean [rows* cols];
    JMenuItem newGame = new JMenuItem ("new game");
    JMenuItem difficulty = new JMenuItem("options");
    
    JMenuItem about = new JMenuItem("about");
    JMenuItem howToPlay = new JMenuItem("how to play");
    Timer timer = new Timer (1000, new TimerListener());;
    int timeCount=0;
    JLabel mineLabel = new JLabel ( "mines: " + mineNum + " marked: 0 " + "timer:"+ timeCount);

    JPanel panel = new JPanel();

    public static void main (String []args){
        new CYin_MineSweeper();
    }

    public CYin_MineSweeper(){
        panel.setLayout(layout);
        setUp1();
        for (int i = 0; i < (rows*cols); i ++){
            panel.add(buttons[i]);
        }
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Game");
        newGame.addActionListener(this);
        menu.add(newGame);
        menuBar.add(menu);
        
        JMenu menu1 = new JMenu("Options");
        difficulty.addActionListener(this);
        menu1.add(difficulty);
        menuBar.add(menu1);

        JMenu menu2 = new JMenu("Help");
        about.addActionListener(this);
        menu2.add(about);
        howToPlay.addActionListener(this);
        menu2.add(howToPlay);
        menuBar.add(menu2);

        this.setJMenuBar(menuBar);
        this.add(panel);
        this.add(mineLabel , BorderLayout.SOUTH);
        this.pack();
        this.setVisible(true);
    }

    public void setUp1(){
        for (int x =0 ; x< rows; x++){
            for (int y = 0; y< cols; y++){
                mines [(rows*y)+x]=false;
                clickDone [(rows*y)+x] = false;
                possible [(rows*y)+x] = true;
                buttons [(rows*y)+x] = new JButton();
                buttons [(rows*y)+x].setPreferredSize(new Dimension (20,20));
                buttons[(rows*y)+x].addActionListener(this);
                buttons[(rows*y)+x].addMouseListener(this);

            }
        }

        fillMines();
        fillNumbers();
    }

    public void setUp2(){
        this.remove(panel);
        panel = new JPanel();
        layout = new GridLayout(rows, cols);
        panel.setLayout(layout);
        buttons = new JButton [rows*cols];
        mines = new boolean [rows*cols];
        clickDone = new boolean [rows*cols];
        possible = new boolean [rows*cols];
        numbers = new int [rows*cols];
        setUp1();
        for (int i =0; i < (rows*cols); i ++){
            panel.add(buttons[i]);

        }
        this.add(panel);
        this.pack();
        fillMines();
        fillNumbers();
    }

    public void setup(){
        for ( int x = 0 ; x < rows ; x ++ ) {
            for ( int y = 0 ; y < cols ; y ++ ) {
                mines [(rows*y)+x] = false ;
                clickDone [(rows*y)+x] = false ;
                possible [(rows*y)+x] = true ;
                buttons [(rows*y)+x].setEnabled ( true ) ;
                buttons [(rows*y)+x].setText ( "" ) ;
            }
        }
        fillMines ( ) ;
        fillNumbers ( ) ;
        lost = false ;
        mineLabel.setText ( "mines: " + mineNum + " marked: 0" +" timer:" + timeCount) ;
        mineLabel.repaint();
    }

    public void fillMines(){
        /* The first gets random coordinates with random limiing it with floor,
         * than tests to see if it repeats if it does the mine isn't added
         * goes up until needed is 0
         */
        int needed = mineNum;
        while (needed> 0 ){
            int x = (int)Math.floor(Math.random()*rows);
            int y = (int)Math.floor(Math.random()*cols);

            if (!mines[(rows*y)+x]){
                mines [(rows*y)+x] = true;
                needed--;
            }
        }

    }

    public void fillNumbers(){
        /*checks the each side of clicked box seeing if it goes out of bounds
        * than places the mine as it goes.
        */
        for (int x =0; x< rows; x++){
            for (int y =0 ;y<rows;y++){
                int cur = (rows*y+x);
                if (mines[cur]){
                    numbers [cur] =0;
                    continue;
                }

                int temp =0;
                //keeps it inbounds
                boolean l = (x-1) >=0;
                boolean r = (x+1) <rows;
                boolean u = (y-1) >= 0;
                boolean d = (y+1) < cols;

                //specify the directions
                int left = ( rows * ( y ) ) + ( x - 1 ) ;
                int right = ( rows * ( y ) ) + ( x + 1 ) ;
                int up = ( rows * ( y - 1 ) ) + ( x ) ;
                int upleft = ( rows * ( y - 1 ) ) + ( x - 1 ) ;
                int upright = ( rows * ( y - 1 ) ) + ( x + 1 ) ;
                int down = ( rows * ( y + 1 ) ) + ( x ) ;
                int downleft = ( rows * ( y + 1 ) ) + ( x - 1 ) ;
                int downright = ( rows * ( y + 1 ) ) + ( x + 1 ) ;
                if ( u ) {
                    if ( mines [ up ] ) {
                        temp ++ ;
                    }
                    if ( l ) {
                        if ( mines [ upleft ] ) {
                            temp ++ ;
                        }
                    }
                    if ( r ) {
                        if ( mines [ upright ] ) {
                            temp ++ ;
                        }
                    }
                }
                if ( d ) {
                    if ( mines [ down ] ) {
                        temp ++ ;
                    }
                    if ( l ) {
                        if ( mines [ downleft ] ) {
                            temp ++ ;
                        }
                    }
                    if ( r ) {
                        if ( mines [ downright ] ) {
                            temp ++ ;
                        }
                    }
                }
                if ( l ) {
                    if ( mines [ left ] ) {
                        temp ++ ;
                    }
                }
                if ( r ) {
                    if ( mines [ right ] ) {
                        temp ++ ;
                    }
                }
                numbers[cur] = temp;
            }
        }
    }

    public void actionPerformed(ActionEvent e){
        
        if (e.getSource()==difficulty){
            rows = Integer.parseInt ( ( String ) JOptionPane.showInputDialog (
                    this , "Rows" , "Rows" , JOptionPane.PLAIN_MESSAGE , null ,
                    null , 10 ) ) ;
            cols = Integer.parseInt ( ( String ) JOptionPane.showInputDialog (
                    this , "Columns" , "Columns" , JOptionPane.PLAIN_MESSAGE ,
                    null , null , 10 ) ) ;
            mineNum = Integer.parseInt ( ( String ) JOptionPane
                .showInputDialog ( this , "Mines" , "Mines" ,
                    JOptionPane.PLAIN_MESSAGE , null , null , 10 ) ) ;
            setUp2();
        } if (! won){
            for ( int x = 0 ; x < rows ; x ++ ) {
                for ( int y = 0 ; y < cols ; y ++ ) {
                    if ( e.getSource ( ) == buttons [(rows*y)+x]
                    && ! won && possible [(rows*y)+x] ) {
                        doCheck ( x , y ) ;
                        break ;
                    }
                }
            }
        }if ( e.getSource ( ) == newGame ) {
            setup ( ) ;
            won = false ;
            return ;

        }if (e.getSource() == about){
            try{
                JEditorPane gameinfo = new JEditorPane("file:MineSweepAbout.htm");
                JOptionPane.showMessageDialog(null, gameinfo, "About", JOptionPane.PLAIN_MESSAGE, null);
            }catch(IOException w){
                System.out.println("About Error: " + w.getMessage());
            }
        }if (e.getSource() == howToPlay){
            try{
                JEditorPane instructions = new JEditorPane("file:MineSweep.htm");
                JScrollPane helpPane = new JScrollPane(instructions);
                JOptionPane.showMessageDialog(null, helpPane, "How To Play", JOptionPane.PLAIN_MESSAGE, null);
            }catch(IOException i ){
                System.out.println("How To Error: " + i.getMessage());
            }
        }
        checkWin();
    }

    public void mouseEntered (MouseEvent e){
    }

    public void mouseExited (MouseEvent e){
    }

    public void mousePressed(MouseEvent e){
        /* Small problem with the thing, it does work but i don't can't add the picture
         * to the button. other than that its fine.
         */
        int n = 0 ;
        if ( e.getButton()==3 ){
            for ( int x = 0 ; x < rows ; x ++ ) {
                for ( int y = 0 ; y < cols ; y ++ ) {
                    if ( e.getSource ( ) == buttons [(rows*y)+x]){
                        possible [ (rows*y)+x] = ! possible [(rows*y)+ x ] ;
                    }
                    if ( ! clickDone [(rows*y)+x] ) {
                        if ( ! possible [(rows*y)+x] && buttons[(rows*y)+x].getText().compareTo("")==0 ) {
                            buttons [(rows*y)+x].setText ( "X" ) ;
                            n ++ ;
                        }else if (possible [(rows*y)+x] && buttons[(rows*y)+x].getText().compareTo("X")==0){
                            buttons [(rows*y)+x].setText ( "?" ) ;
                            n++;
                        } else if (possible [(rows*y)+x] && buttons[(rows*y)+x].getText().compareTo("?")==0){
                            buttons [(rows*y)+x].setText ( "" ) ;
                        }
                        mineLabel.setText ( "mines: " + mineNum + " marked: "+ n + "timer" + timeCount) ;
                        mineLabel.repaint();
                    }
                }
            }
        }
    }

    public void mouseReleased(MouseEvent e){
    }

    public void mouseClicked (MouseEvent e){
    }

    public void doCheck(int x, int y){
        
        int cur = (rows*y)+x;
        boolean l = ( x - 1 ) >= 0 ;
        boolean r = ( x + 1 ) < rows ;
        boolean u = ( y - 1 ) >= 0 ;
        boolean d = ( y + 1 ) < cols ;
        int left = ( rows * ( y ) ) + ( x - 1 ) ;
        int right = ( rows * ( y ) ) + ( x + 1 ) ;
        int up = ( rows * ( y - 1 ) ) + ( x ) ;
        int upleft = ( rows * ( y - 1 ) ) + ( x - 1 ) ;
        int upright = ( rows * ( y - 1 ) ) + ( x + 1 ) ;
        int down = ( rows * ( y + 1 ) ) + ( x ) ;
        int downleft = ( rows * ( y + 1 ) ) + ( x - 1 ) ;
        int downright = ( rows * ( y + 1 ) ) + ( x + 1 ) ;

        clickDone [cur] = true;
        buttons[cur].setEnabled(false);
        if ( numbers [ cur ] == 0 && ! mines [ cur ] && ! lost && ! won ) {
            if ( u && ! won ) {
                if ( ! clickDone [ up ] && ! mines [ up ] ) {
                    clickDone [ up ] = true ;
                    buttons [ up ].doClick ( ) ;
                }
                if ( l && ! won ) {
                    if ( !clickDone[upleft] && numbers[upleft] != 0
                    && ! mines [ upleft ] ) {
                        clickDone [ upleft ] = true ;
                        buttons [ upleft ].doClick ( ) ;
                    }
                }
                if ( r && ! won ) {
                    if ( ! clickDone[upright]&& numbers[upright] != 0
                    && ! mines [upright]) {
                        clickDone [upright] = true ;
                        buttons [upright].doClick ( ) ;
                    }
                }
            }
            if ( d && ! won ) {
                if ( ! clickDone [down] && !mines[down] ) {
                    clickDone [ down] = true ;
                    buttons [ down].doClick() ;
                }
                if ( l && ! won ) {
                    if ( ! clickDone [ downleft] && numbers[ downleft ] != 0
                    && ! mines [ downleft ] ) {
                        clickDone [ downleft ] = true ;
                        buttons [ downleft ].doClick ( ) ;
                    }
                }
                if ( r && ! won ) {
                    if ( ! clickDone [ downright ]
                    && numbers [ downright ] != 0
                    && ! mines [ downright ] ) {
                        clickDone [ downright ] = true ;
                        buttons [ downright ].doClick ( ) ;
                    }
                }
            }
            if ( l && ! won ) {
                if ( !clickDone[ left ] && ! mines[left]){
                    clickDone[left ] = true ;
                    buttons[left ].doClick () ;
                }
            }
            if ( r && ! won ) {
                if ( ! clickDone [ right ] && ! mines [ right ] ) {
                    clickDone [ right ] = true ;
                    buttons [ right ].doClick ( ) ;
                }
            }
        } else {
            buttons [ cur ].setText ( "" + numbers [ cur ] ) ;
            if ( ! mines [ cur ] && numbers [ cur ] == 0 ) {
                buttons [ cur ].setText ( "" ) ;
            }
        }
        if ( mines [ cur ] && ! won ) {
            buttons [ cur ].setText ( "0" ) ;
            doLose ( ) ;
        }
    }

    public void checkWin(){
        for (int x =0; x < rows; x++){
            for(int y =0; y <cols; y++){
                int cur = (rows*y)+x;
                if (! clickDone [cur]){
                    if (mines[cur]){
                        continue;
                    }else{
                        return;
                    }
                }
            }
        }
        doWin();
    }

    public void doWin (){
        if(! lost && ! won){
            won= true;
            JOptionPane.showMessageDialog ( null ,
                "you win!\nstarting a new game" , "you lose" ,
                JOptionPane.INFORMATION_MESSAGE ) ;
            newGame.doClick ( ) ;
        }
    }

    public void doLose(){
        if(!lost && !won){
            lost = true;
            for(int i =0; i < rows*cols; i ++){
                if (!clickDone [i]){
                    buttons [i].doClick(0);
                }
            }
            JOptionPane.showMessageDialog(null, "you lose! \nStarting a new game", "you Lose!", JOptionPane.ERROR_MESSAGE);
            setup();

        }
    }
    class TimerListener implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			timeCount++;
			mineLabel.setText ( "mines: " + mineNum + " marked: 0" +" timer:" + timeCount) ;
			mineLabel.repaint();
		}
	}
}

