package game_adm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class Game
{
    Table t;//the table on which the game is played
    JFrame frame;//

    //the save game option: opens a window where the user can save the game with any name
    void saveTheGame()
    {

        JFrame saveFrame=new JFrame("save game");
        saveFrame.setSize(400,100);
        saveFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel panel=new JPanel(new BorderLayout());
        JPanel top=new JPanel();
        JPanel bottom=new JPanel();
        final String[] saveAs = {""};
        final JTextField[] outp = {new JTextField("save game as:")};
        JTextField inp=new JTextField(20);
        JButton saveButton=new JButton("save");
        saveButton.addActionListener(e -> {

            try {
                saveAs[0] =inp.getText();
                FileOutputStream f = new FileOutputStream(saveAs[0]+".txt");
                ObjectOutputStream out = new ObjectOutputStream(f);
                out.writeObject(Game.this.t);
                out.close();
                saveFrame.dispose();
            }
            catch(IOException ex) {System.out.println(ex+" "+ex.getCause()); }

        });
        top.add(outp[0]);
        bottom.add(inp);
        bottom.add(saveButton);
        inp.setEditable(true);
        outp[0].setEditable(false);
        panel.add(top,BorderLayout.PAGE_START);
        panel.add(bottom,BorderLayout.PAGE_END);
        saveFrame.add(panel);
        saveFrame.setVisible(true);

    }

    //opens a window where the user can load any game from the saves
    void loadGame()
    {
        File wd=new File(System.getProperty("user.dir"));
        File[] files=wd.listFiles();
        int numberOfSaves=0;
        Object[] savesArr=new Object[20];
        for(File i:files)
        {
            String path=i.getAbsolutePath();
            int index=path.lastIndexOf('.');
            if(index>0)
            {
                if(path.substring(index).equals(".txt"))
                {
                    //System.out.println(i.getName());
                    savesArr[numberOfSaves]=i.getName().substring(0,i.getName().lastIndexOf('.'));
                    numberOfSaves++;
                }
            }
        }
        JFrame f=new JFrame("load");
        JComboBox saves=new JComboBox(savesArr);
        JButton loadButton=new JButton("load");
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    FileInputStream f = new FileInputStream((String)saves.getSelectedItem()+".txt");
                    ObjectInputStream in = new ObjectInputStream(f);
                    t = (Table)in.readObject();
                    in.close();
                    frame.dispose();
                    frame=t.frame;
                    menu();
                    t.kirajz(frame);

                } catch(IOException | ClassNotFoundException ex)
                {
                    System.out.println(ex);
                }


                f.dispose();
            }
        });

        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        f.setSize(400,100);
        JPanel p=new JPanel();
        p.add(saves);
        p.add(loadButton);
        f.add(p);
        f.setVisible(true);
    }

    //starts a new game with the users settings
    void newGame()
    {
        JFrame f=new JFrame("new game");

        Object[] vsArr=new Object[2];
        vsArr[0]="human vs bot";
        vsArr[1]="human vs human";
        JComboBox vs=new JComboBox(vsArr);

        Object[] iwArr=new Object[2];
        iwArr[0]="play as white";
        iwArr[1]="play as black";
        JComboBox iw=new JComboBox(iwArr);

        JButton startButton=new JButton("start game");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    FileInputStream f = new FileInputStream("starting_cls.txt");
                    ObjectInputStream in = new ObjectInputStream(f);
                    t = (Table)in.readObject();
                    in.close();
                    if(vs.getSelectedItem().equals("human vs bot"))
                    {
                        t.bb=new BasicBot(t);
                        t.setVsBot(true);
                        if(iw.getSelectedItem().equals("play as black"))
                        {
                            t.bb.makeBasicMove();
                        }

                    }


                } catch(IOException | ClassNotFoundException ex)
                {
                    System.out.println(ex);
                }
                frame.dispose();
                frame=t.frame;
                menu();
                t.kirajz(frame);
                f.dispose();
                t.kirajz(frame);
                /*t=new game_adm.Table(frame, Objects.equals(vs.getSelectedItem(), "human vs bot"));
                if(vs.getSelectedItem().equals("human vs bot") && iw.getSelectedItem().equals("play as black"))
                {
                    t.bb.makeBasicMove();
                }
                f.dispose();
                t.kirajz(frame);*/
            }
        });


        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        f.setSize(400,100);
        JPanel p=new JPanel();
        p.add(vs);
        p.add(iw);
        p.add(startButton);
        f.add(p);
        f.setVisible(true);
    }

    //creates the menuon the top of the window
    public void menu()
    {
        JMenuBar a=new JMenuBar();

        JMenuItem loadGame=new JMenuItem("load game");
        loadGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Game.this.loadGame();
            }
        });

        JMenuItem saveGame=new JMenuItem("save game");
        saveGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Game.this.saveTheGame();
            }
        });

        JMenuItem newGame=new JMenuItem("new game");
        newGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Game.this.newGame();
            }
        });

        a.add(newGame);
        a.add(loadGame);
        a.add(saveGame);

        frame.setJMenuBar(a);
    }

    //constructor: nothing else needs to be done with it, since the users interactions determine everything else
    public Game()
    {
        frame=new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menu();
        t=new Table(this.frame,false);
        t.kirajz(frame);
        frame.setVisible(true);
    }


    public static void main(String[] args)
    {
        Game nami =new Game();


        //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA

    }
}
