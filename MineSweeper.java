import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

public class MineSweeper extends JFrame implements ActionListener, MouseListener
{
    JToggleButton[][] board;
    JPanel boardPanel;
    boolean firstClick;
    int numMines;

    ImageIcon mineIcon,flag;
    ImageIcon[]numbers;
    GraphicsEnvironment ge;
    Font mineFont;
    public MineSweeper()
    {
        numMines = 10;
        try {
            ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            mineFont = Font.createFont(Font.TRUETYPE_FONT, new File("C:\\Users\\richard\\IdeaProjects\\MineSweeper\\src\\mine-sweeper.ttf"));
            ge.registerFont(mineFont);
        }catch(IOException|FontFormatException e)
        {}
        mineIcon = new ImageIcon("C:\\Users\\richard\\IdeaProjects\\MineSweeper\\src\\mine.png");
        mineIcon = new ImageIcon(mineIcon.getImage().getScaledInstance(40,40, Image.SCALE_SMOOTH));

        flag = new ImageIcon("C:\\Users\\richard\\IdeaProjects\\MineSweeper\\src\\flag.png");
        flag = new ImageIcon(flag.getImage().getScaledInstance(40,40, Image.SCALE_SMOOTH));
        numbers=new ImageIcon[8];
        for(int x = 0; x<8;x++){
            numbers[x] = new ImageIcon("C:\\Users\\richard\\IdeaProjects\\MineSweeper\\src\\"+(x+1)+".png");
            numbers[x] = new ImageIcon(numbers[x].getImage().getScaledInstance(40,40, Image.SCALE_SMOOTH));
        }
        firstClick = true;
        createBoard(10,10);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
    public void createBoard(int row, int col)
    {
        if(boardPanel != null)
            this.remove(boardPanel);
        boardPanel = new JPanel();
        board = new JToggleButton[row][col];
        boardPanel.setLayout(new GridLayout(row,col));

        for(int r = 0; r < row; r++)
        {
            for(int c = 0; c < col; c++)
            {
                board[r][c]=new JToggleButton();
                board[r][c].putClientProperty("row",r);
                board[r][c].putClientProperty("column",c);
                board[r][c].putClientProperty("state",0);
                board[r][c].setFont(mineFont);
                board[r][c].setBorder(BorderFactory.createBevelBorder(0));
                board[r][c].setFocusPainted(false);
                board[r][c].addMouseListener(this);
                boardPanel.add(board[r][c]);
            }
        }

        this.setSize(col*40,row*40);
        this.add(boardPanel);
        this.revalidate();

    }
    public void setMinesAndCounts(int currRow, int currCol)
    {
        int count = numMines;
        int dimR = board.length;
        int dimC = board[0].length;
        while(count>0)
        {
            int randR = (int)(Math.random()*dimR);
            int randC = (int)(Math.random()*dimC);
            int state = (int)((JToggleButton)board[randR][randC]).getClientProperty("state");
            if(state==0 && (Math.abs(currRow-randR)>1 && Math.abs(currCol-randC)>1))
            {
                board[randR][randC].putClientProperty("state",-1);
                count--;
            }
        }

        for(int r = 0; r < dimR; r++)
        {
            for(int c = 0; c < dimC; c++)
            {
                try{
                    int num = 0;
                    try{
                        if((int)((JToggleButton)board[r][c-1]).getClientProperty("state") == -1)
                            num++;
                    }catch(ArrayIndexOutOfBoundsException e){
                    }
                    try{
                        if((int)((JToggleButton)board[r][c+1]).getClientProperty("state") == -1)
                            num++;
                    }catch(ArrayIndexOutOfBoundsException e){
                    }
                    try{
                        if((int)((JToggleButton)board[r+1][c-1]).getClientProperty("state") == -1)
                            num++;
                    }catch(ArrayIndexOutOfBoundsException e){
                    }
                    try{
                        if((int)((JToggleButton)board[r+1][c+1]).getClientProperty("state") == -1)
                            num++;
                    }catch(ArrayIndexOutOfBoundsException e){
                    }
                    try{
                        if((int)((JToggleButton)board[r-1][c]).getClientProperty("state") == -1)
                            num++;
                    }catch(ArrayIndexOutOfBoundsException e){
                    }
                    try{
                        if((int)((JToggleButton)board[r-1][c+1]).getClientProperty("state") == -1)
                            num++;
                    }catch(ArrayIndexOutOfBoundsException e){
                    }
                    try{
                        if((int)((JToggleButton)board[r+1][c]).getClientProperty("state") == -1)
                            num++;
                    }catch(ArrayIndexOutOfBoundsException e){
                    }
                    try{
                        if((int)((JToggleButton)board[r-1][c-1]).getClientProperty("state") == -1)
                            num++;
                    }catch(ArrayIndexOutOfBoundsException e){
                    }
                    if((int)((JToggleButton)board[r][c]).getClientProperty("state")!=-1)
                        board[r][c].putClientProperty("state",num);

                }catch(ArrayIndexOutOfBoundsException e){
                }

            }
        }

		for(int r = 0; r < dimR; r++)
		{
			for(int c = 0; c < dimC; c++)
			{
				int state = (int)((JToggleButton)board[r][c]).getClientProperty("state");
				board[r][c].setText(""+state);
			}
		}
    }
    public void checkWin()
    {
        int dimR=board.length;
        int dimC=board[0].length;
        int totalSpaces = dimR*dimC;
        int count = 0;
        for(int r = 0; r < dimR; r++)
        {
            for(int c = 0; c < dimC; c++)
            {
                int state = (int)board[r][c].getClientProperty("state");
                if(board[r][c].isSelected() && state != -1)
                    count++;
            }
        }
        if(numMines==totalSpaces-count)
            JOptionPane.showMessageDialog(null,"You are a winner!");
    }
    public void actionPerformed(ActionEvent e)
    {

    }

    public void mouseReleased(MouseEvent e)
    {
        int row	= (int)((JToggleButton)e.getComponent()).getClientProperty("row");
        int col	= (int)((JToggleButton)e.getComponent()).getClientProperty("column");

        if(e.getButton() == MouseEvent.BUTTON1 && board[row][col].isEnabled())
        {
            if(firstClick)
            {
                setMinesAndCounts(row,col);
                firstClick = false;
            }

            int state = (int)board[row][col].getClientProperty("state");
            if(state==-1)
            {
                board[row][col].setIcon(mineIcon);
                board[row][col].setContentAreaFilled(false);
                board[row][col].setOpaque(true);
                board[row][col].setBackground(Color.RED);

                revealMines();

                JOptionPane.showMessageDialog(null,"You are a loser!");
                //board[row][col].setBackground(Color.RED);
            }
            else
            {
                expand(row, col);
                checkWin();
            }
            if(e.getButton()==MouseEvent.BUTTON3){
                if(!board[row][col].isSelected()){
                    if(board[row][col].getIcon()==null){
                        board[row][col].setIcon(flag);
                        board[row][col].setDisabledIcon(flag);
                        board[row][col].setEnabled(false);
                    } else {
                        board[row][col].setIcon(null);
                        board[row][col].setDisabledIcon(null);
                        board[row][col].setEnabled(true);
                    }
                }
            }
        }
    }
    public void revealMines()
    {
        for(int r = 0; r < board.length;r++)
        {
            for(int c = 0; c < board[0].length;c++)
            {
                int state = (int)board[r][c].getClientProperty("state");
                if(state==-1)
                {
                    board[r][c].setIcon(mineIcon);
                    board[r][c].setDisabledIcon(mineIcon);
                }
                board[r][c].setEnabled(false);
            }
        }
    }
    public void expand(int row, int col)
    {
        if(!board[row][col].isSelected())
            board[row][col].setSelected(true);

        int state = (int)board[row][col].getClientProperty("state");
        if(state>0)
        {
            //board[row][col].setText(""+state);
            write(row,col,state);
        }
        else
        {
            for(int r33 = row-1; r33 <= row+1; r33++)
            {
                for(int c33 = col-1; c33 <= col+1; c33++)
                {
                    try
                    {
                        if(!board[r33][c33].isSelected())
                            expand(r33,c33);
                    }
                    catch(ArrayIndexOutOfBoundsException e){
                    }
                }
            }
        }

    }
    public void write(int row, int col, int state)
    {
        Color c = Color.BLUE;
        switch(state)
        {
            case 1: c = Color.BLUE; break;
            case 2: c = Color.GREEN; break;
            case 3: c = Color.RED; break;
            case 4: c = new Color(128,0,128); break;
            case 5: c = new Color(128, 0, 0); break;
            case 6: c = Color.CYAN; break;
            case 7: c = Color.MAGENTA; break;
            case 8: c = Color.GRAY; break;
        }
        if(state>0)
        {
            //board[row][col].setForeground(c);
           // board[row][col].setText(""+state);
            board[row][col].setIcon(numbers[state-1]);
            board[row][col].setDisabledIcon(numbers[state-1]);
        }
        //board[row][col].setText(""+state);
    }
    public void mouseClicked(MouseEvent e)	{}
    public void mousePressed(MouseEvent e)	{}
    public void mouseEntered(MouseEvent e)	{}
    public void mouseExited(MouseEvent e)	{}
    public static void main(String[] args)
    {
        MineSweeper app = new MineSweeper();
    }
}

