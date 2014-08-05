import javax.swing.*;

public class frame extends JFrame
{
    JPanel mypanel1;
    JPanel mypanel2;
    JPanel mypanel3;

    JList mybutton1;
    JButton mybutton2;
    JButton mybutton3;
    
    JLabel mylabel;

    public frame(){
	mypanel1 = new JPanel();
	mypanel2 = new JPanel();
	mypanel3 = new JPanel();

	mybutton1 = new JList();
	
	mybutton2 = new JButton("WELP");
	mybutton3 = new JButton("HERP");
	mylabel = new JLabel();

	mypanel1.add(mybutton1);
	mypanel1.add(mylabel);
	this.add(mypanel1);
//	this.add(mypanel2);
//	this.add(mypanel3);

    }

    public static void main(String[] args){
	frame derp = new frame();

	derp.setTitle("derp");
	derp.setSize(900,700);
	derp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	derp.setVisible(true);
    }
    
}
