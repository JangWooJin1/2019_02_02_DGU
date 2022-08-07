/*
 Rable.java
 2019 12 03
 2018213030 �ֵ�ȣ
 ��ü�������α׷��� 
 ���ĺ� ��ġ GUI ���� ������Ʈ ���� 
*/
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;


public class Rable extends JFrame { 
  JLabel Label[]; 
  boolean chack;
  JLabel chackLocation = null;
  
  public static void main(String[] args) { 
    new Rable();
 } 
  
 public Rable() { 
   setTitle("Alphabet ��ġ Ŭ��"); // Ÿ��Ʋ
   char Alphabet = 65; // char Ÿ���� �����ؼ� �ƽ�Ű�ڵ带 �̿�
   Label = new JLabel[26]; 
   myListener listener = new myListener();
  
   for (;Alphabet < 91; Alphabet++) { // �ƽ�Ű�ڵ��� 65�� A�̹Ƿ� Z�� 90���� Alphabet++
     JLabel label = new JLabel(""+Alphabet); 
     int x = (int) (Math.random() * 245)+5; // x�� 5~250������ ���ڸ� �������� ���� 
     int y = (int) (Math.random() * 245)+5; // y�� 5~250������ ���ڸ� �������� ���� 
     label.setLocation(x, y); // label�� x, y ��ġ�� ��ġ 
     label.setSize(15, 15); // label�� ũ�⸦ 10 x 10 ���� ���� 
     label.addMouseListener(listener);
     add(label); // label�� ����Ʈ�ҿ� ���� 
     Label[(int)(Alphabet-65)] = label; 
  }  
  setLayout(null); // ����Ʈ���� ��ġ������ ���� 
  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
  setSize(320, 320); // �������� ũ�⸦ 320, 320�� ���� 
  setVisible(true); 
} 

 
 class myListener extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			JLabel la = (JLabel) e.getSource();
			
			if (la == Label[0]) { // Ŭ���� ����� A�� �´��� �˻��ϴ� ���ǹ�
				la.setEnabled(false);
				la.setVisible(false);
			} else {
				int index = 0;
				
				for (int i = 0; i < Label.length; i++) { 
					if (la == Label[i])
						index = i;  // // ���� ������(���°��) Ŭ���ߴ��� �˻��ؼ� index�� ��� ����
				}
				
				for (int j = 0; j < index; j++) {
					if (Label[j].isEnabled() == true) {	 // Ŭ���� ���� ���� �ϳ��� �Ⱦ����� ���� ������ ���� ���ؿ� "�����ǥ" ǥ���ϰ� ���� 	
						if(chack == true) { // ��ǥ�� �̹� ��� �Ǿ� �ִ��� �˻� (�ߺ�����)
							chackLocation.setEnabled(false); // ���� �ִ� �����ǥ ����
							chackLocation.setVisible(false);
							chack = false; 
						}
						
						//�����ǥ ���
						chackLocation = new JLabel("("+(Label[j].getX()-Label[index].getX())+","+(Label[j].getY()-Label[index].getY())+")");
						chackLocation.setLocation(Label[index].getX()-10,  Label[index].getY()-30);
						chackLocation.setSize(100, 100);
						add(chackLocation);
						repaint();
						chack = true;
						return;
					}
				}
				Label[index].setEnabled(false); // ������ �°� ���ȴٸ� �˹ٺ� ����
				Label[index].setVisible(false);
			}
			
		}
	}
 

}

