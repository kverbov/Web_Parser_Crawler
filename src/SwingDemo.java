import javafx.scene.layout.Border;

import javax.swing.*;
import javax.swing.plaf.basic.BasicOptionPaneUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.util.concurrent.TimeUnit;

/**
 * Created by Verbov on 22.06.2017.
 */
public class SwingDemo {
    JButton OKbtn;



    SwingDemo(){
        JFrame jfrm=new JFrame("A simple Swing Application");
        jfrm.setVisible(true        );
        jfrm.setLayout(new FlowLayout());

        jfrm.setSize(220,90);

        jfrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel jlab=new JLabel("Swing means powerfull GUI");
        jlab=new JLabel("Press button");
        jfrm.add(jlab);
        JButton jbtnAplha=new JButton("Alpha");
        JButton jbtnBeta=new JButton("Beta");

/*
        jbtnAplha.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jlab.setText("Apha was pressed");
            }
        });

        jbtnBeta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jlab.setText("Beta was pressed");
            }
        });
*/

        jfrm.add(jbtnAplha);
        jfrm.add(jbtnBeta);
        jfrm.setVisible(true);
    }

    class myMouseAdapter extends MouseAdapter{
        public myMouseAdapter()
        {


        }
    }

    public static void main(String args[])
    {



        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SwingDemo();
            }
        });
    }
}
