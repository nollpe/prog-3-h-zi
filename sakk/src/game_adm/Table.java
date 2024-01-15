package game_adm;
import pieces.*;

import javax.swing.*;
import java.io.*;
import java.util.*;
import java.awt.*;

public class Table implements Serializable
{
    private LinkedList<Piece> white=new LinkedList<Piece>();//all the white pieces
    private LinkedList<Piece> black=new LinkedList<Piece>();//all the black pieces
    private Piece[][] board;//the board represented as an 8 by 8 array
    private boolean turnOfWhite=true;//who s turn is it to move
    private Piece ChosenPiece;//the piece that is clicked on (and might move on the next click)
    public JFrame frame;//the frame of the game is shown on
    //the kings (its just easier to find them)
    private King WhiteKing;
    private King BlackKing;
    public BasicBot bb;//a bot if the user want one
    private Position enPassant;//the position of
    private boolean enPassantWasSet=false;//true in the turn following a double pawn push (after which it needs to be removed)
    private boolean vs_bot;//if the game is played vs a bot
    private int halfMovesSince;//stores how many half moves have been made since the last pawn push or piece capture
    private int moves;//the number of moves made in the game
    private boolean gameHasEnded=false;
    private String gameEndedDisplayText="nincs meg";

    public void pawnMoved()
    {
        halfMovesSince=-1;
    }

    public void pieceCaptured()
    {
        halfMovesSince=-1;
    }

    public boolean getTurnOfWhite()
    {
        return turnOfWhite;
    }

    public void setVsBot(boolean a){vs_bot=a;}

    public boolean getVsBot()
    {
        return vs_bot;
    }

    public LinkedList<Piece> getWhitePieces()
    {
        return white;
    }

    public LinkedList<Piece> getBlackPeces()
    {
        return black;
    }

    public void setEnPassant()
    {
        enPassantWasSet=true;
    }

    public void setEnPassant(Position n)
    {
        enPassant=n;
    }

    public Position getEnPassant() {
        return enPassant;
    }

    public void setPieceOn(int x, int y,Piece n)
    {
        board[x][y]=n;
    }

    public Piece getPieceOn(int x, int y)
    {
        return board[x][y];
    }

    public King getWhiteKing()
    {
        return WhiteKing;
    }

    public King getBlackKing()
    {
        return BlackKing;
    }

    //sets the state of the table from a string using Forsyth–Edwards Notation
    public void setState(String FEN)
    {
        //System.out.println(FEN);
        white=new LinkedList<Piece>();
        black=new LinkedList<Piece>();
        board=new Piece[8][8];
        for(int i=0;i<64;i++)
        {
            board[i/8][i%8]=new Empty(i/8,i%8,this);
        }
        int x=0,y=0;
        int i;
        for(i=0;i<FEN.length() && FEN.charAt(i)!=' ' ;i++)
        {
            Piece temp;King kt;
            switch (FEN.charAt(i))//megmikrózott kefír asztétika
            {
                case 'R':
                    temp=new Rook(x,y,true,this);
                    white.add(temp);
                    board[x][y]=temp;x++;
                    break;
                case 'N':
                    temp=new Knight(x,y,true,this);
                    white.add(temp);
                    board[x][y]=temp;x++;
                    break;
                case 'B':
                    temp=new Bishop(x,y,true,this);
                    white.add(temp);
                    board[x][y]=temp;x++;
                    break;
                case 'Q':
                    temp=new Queen(x,y,true,this);
                    white.add(temp);
                    board[x][y]=temp;x++;
                    break;
                case 'K':
                    kt=new King(x,y,true,this);
                    temp=kt;
                    white.add(temp);
                    board[x][y]=temp;x++;
                    WhiteKing=kt;
                    break;
                case 'P':
                    temp=new Pawn(x,y,true,this);
                    white.add(temp);
                    board[x][y]=temp;x++;
                    break;
                case 'r':
                    temp=new Rook(x,y,false,this);
                    black.add(temp);
                    board[x][y]=temp;x++;
                    break;
                case 'n':
                    temp=new Knight(x,y,false,this);
                    black.add(temp);
                    board[x][y]=temp;x++;
                    break;
                case 'b':
                    temp=new Bishop(x,y,false,this);
                    black.add(temp);
                    board[x][y]=temp;x++;
                    break;
                case 'q':
                    temp=new Queen(x,y,false,this);
                    black.add(temp);
                    board[x][y]=temp;x++;
                    break;
                case 'k':
                    kt=new King(x,y,false,this);
                    temp=kt;
                    black.add(temp);
                    board[x][y]=temp;x++;
                    BlackKing=kt;
                    break;
                case 'p':
                    temp=new Pawn(x,y,false,this);
                    black.add(temp);
                    board[x][y]=temp;x++;
                    break;
                case'/':
                    x=0;y++;
                    break;
                default:
                    x+=Character.getNumericValue(FEN.charAt(i));
                    break;
            }
        }

        turnOfWhite= FEN.charAt(i + 1) == 'w';
        i+=3;
        //import possible castleings
        if(FEN.charAt(i)=='-')i+=2;//if there is no way to castle we just move on
        else
        {
            while(FEN.charAt(i)!=' ')
            {
                switch(FEN.charAt(i))
                {
                    case'K':
                        board[0][7].setCanCastle(true);board[4][7].setCanCastle(true);
                        break;
                    case 'Q':
                        board[7][7].setCanCastle(true);board[4][7].setCanCastle(true);
                        break;
                    case'k':
                        board[7][0].setCanCastle(true);board[4][0].setCanCastle(true);
                        break;
                    case'q':
                        board[0][0].setCanCastle(true);board[4][0].setCanCastle(true);
                    default:
                        break;
                }
                i++;
            }
        }
        i++;
        if(FEN.charAt(i)=='-')i+=2;//if there is no enpassantable square we just move on
        else
        {
            enPassant=new Position(8-(int)FEN.charAt(i+1),(int)FEN.charAt(i)-'a');
        }
        Scanner s=new Scanner(FEN.substring(i));
        halfMovesSince=s.nextInt();
        moves=s.nextInt();
    }

    //returns a string that represents everything on the board using Forsyth–Edwards Notation
    public String getState()
    {
        String ret= "";
        //saving where pieces aro on the board
        for(int i=0;i<8;i++)
        {
            int numOfEmptySquares=0;
            for(int j=0;j<8;j++)
            {
                switch (this.board[j][i].getchar())//megmikrózott kefír asztétika
                {
                    case ' ':
                        numOfEmptySquares++;
                        break;
                    default:
                        if(numOfEmptySquares>0)
                        {
                            ret+=(char)(numOfEmptySquares+'0');
                            numOfEmptySquares=0;
                        }
                        ret+=this.board[j][i].getchar();
                        break;
                }
            }
            if(numOfEmptySquares>0)
            {
                ret+=(char)(numOfEmptySquares+'0');
            }
            if(i!=7) ret+='/';
        }
        //saving whos turn it is
        ret+=turnOfWhite?" w ":" b ";

        //saving the castleings
        boolean castlingIsPossible=false;
        if(board[0][7].canCastle()&&board[4][7].canCastle())
            {ret+="K";castlingIsPossible=true;}
        if(board[7][7].canCastle()&&board[4][7].canCastle())
            {ret+="Q";castlingIsPossible=true;}
        if(board[7][0].canCastle()&&board[4][0].canCastle())
            {ret+="k";castlingIsPossible=true;}
        if(board[0][0].canCastle()&&board[4][0].canCastle())
            {ret+="q";castlingIsPossible=true;}
        if(!castlingIsPossible)ret+="-";

        //saving enPassantable position if there is one
        if(enPassant==null)ret+=" - ";
        else ret+=" "+(char)('a'+enPassant.positionX)+""+(8-enPassant.positionY)+" ";

        //saving half moves and moves
        ret+=halfMovesSince+" "+moves;
        return ret;
    }

    public Table(JFrame f)
    {
        frame=f;
        vs_bot=false;
    }

    //constructor loading the table's state from a txt file setting the frame and adding a bot if needed
    public Table(String fileName,JFrame jf,boolean bot)
    {
        //reading the inputfile
        String inputPosition="tessek ribanc most inicializalva van";
        try
        {
            FileReader fr=new FileReader(fileName);
            BufferedReader inp = new BufferedReader(fr);
            try
            {
                 inputPosition= inp.readLine();
            }catch(java.io.IOException anyad){System.out.println("actually szar vagy ioexp");}
        }
        catch (FileNotFoundException asd){System.out.println("actually szar vagy filenotfound");}

        this.setState(inputPosition);

        ChosenPiece=null;
        frame=jf;
        vs_bot=bot;
        bb=new BasicBot(this);
    }

    //a constructor that doesnt get the filename: used to set the tavle up with the starting position
    public Table(JFrame jf,boolean bot)
    {
        this("starting.txt",jf,bot);
    }

    //redraws the entire frame
    public void kirajz(JFrame frame)
    {
        frame.getContentPane().removeAll();
        JPanel panel;
        panel=new JPanel(){@Override
        protected void paintComponent(Graphics g) {

            super.paintComponent(g);
            for(int i=0;i<8;i++)
            {
                for(int j=0;j<8;j++)
                {
                    if((i+j)%2==1)
                    {
                        g.setColor(Color.LIGHT_GRAY);
                        g.fillRect(i*80,j*80,80,80);
                        //g.drawRect(i*80,j*80,80,80);
                    }
                }
            }
            float q= (float) 0.7;
            Color colour=new Color(q,q,q);// *sips tea
            g.setColor(colour);
            if(ChosenPiece!=null)
            {
                LinkedList<Position> lm=ChosenPiece.getLegalMoves();
                for (Position temp : lm) {
                    g.fillOval(temp.positionX * 80 + 25, temp.positionY * 80 + 25, 30, 30);
                    if (!board[temp.positionX][temp.positionY].isEmpty()) {
                        g.fillOval(temp.positionX * 80, temp.positionY * 80, 80, 80);
                        if ((temp.positionY + temp.positionX) % 2 == 1) {
                            g.setColor(Color.LIGHT_GRAY);
                        } else {
                            g.setColor(Color.WHITE);
                        }
                        g.fillOval(temp.positionX * 80 + 5, temp.positionY * 80 + 5, 70, 70);
                        g.setColor(colour);
                    }
                }
            }
        }};
        panel.setLayout(null);

        for(int i=0;i<64;i++)
        {
            panel.add(this.board[i/8][i%8]);
        }
        if(gameHasEnded)
        {
            JLabel label = new JLabel(gameEndedDisplayText);
            label.setFont(label.getFont().deriveFont(Font.BOLD, 64));
            label.setForeground(Color.BLUE);
            label.setSize(400,200);
            label.setLocation(127,250);
            panel.add(label,2);
        }
        frame.add(panel);
        frame.setSize(654,700);

        frame.setVisible(true);
    }

    //when a piece was clicked it signals it to the table
    public void wasclicked(Piece temp)
    {
        if(ChosenPiece==null)
        {
            ChosenPiece=temp;
        }
        else if(ILikeToMoveItMoveIt(ChosenPiece.getPos(),temp.getPos()))
        {
            ChosenPiece=null;
            if(vs_bot)
            {
                bb.makeBasicMove();
                //bb.attack();
                //bb.defend();
            }
        }
        else
        {
            ChosenPiece=temp;
        }
        kirajz(frame);
    }

    //only called if no moves can be made by one side, and see if their king is in check to determine whether someone won or it's a draw
    int didBlackWin()
    {
        return WhiteKing.isInCheck()?-1:0;
    }
    int didWhiteWin()
    {
        return BlackKing.isInCheck()?1:0;
    }

    //checks if the game has ended and if yes returns the result
    int gameHasEnded()
    {
        if(halfMovesSince==50)
            return 0;//draw (50 move rule)
        LinkedList<Piece> myPieces=this.turnOfWhite?this.white:this.black;
        for(Piece p:myPieces)
        {
            if(p.getLegalMoves().size()!=0)
            {
                return 69;//basically nothing happens just funny number
            }
        }
        gameHasEnded=true;
        return this.turnOfWhite? didBlackWin(): didWhiteWin();
    }

    //actually move the piece
    public boolean ILikeToMoveItMoveIt(Position from, Position to)
    {
        try
        {
            if(this.board[from.positionX][from.positionY].isWhite() !=this.turnOfWhite)
            {//dont move a piece in their opponents turn
                return false;
            }
            //the piece makes the move, this throws an exeption if it isn't legal
            this.board[from.positionX][from.positionY].moveTo(to.positionX,to.positionY);
            //handling en passant
            if(!enPassantWasSet)
            {
                enPassant=null;
            }enPassantWasSet=false;

            //switches the turn
            this.turnOfWhite^=true;
            this.kirajz(frame);
            //check if the game has ended and if yes what is the result
            switch (this.gameHasEnded())
            {
                case(1):
                    gameEndedDisplayText="white won";
                    break;
                case(-1):
                    gameEndedDisplayText="black won";
                    break;
                case(0):
                    gameEndedDisplayText="draw";
                    break;
            }
            halfMovesSince++;
            if(turnOfWhite)moves++;//it adds in whites turn bc it already changed it from black
            kirajz(frame);
            return true;
        }catch(cannotMoveThere asd){return false;}

    }

}