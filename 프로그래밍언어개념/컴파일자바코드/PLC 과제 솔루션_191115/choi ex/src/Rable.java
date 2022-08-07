/*
 Rable.java
 2019 12 03
 2018213030 최두호
 객체지향프로그래밍 
 알파벳 배치 GUI 구현 프로젝트 과제 
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
   setTitle("Alphabet 배치 클릭"); // 타이틀
   char Alphabet = 65; // char 타입을 선언해서 아스키코드를 이용
   Label = new JLabel[26]; 
   myListener listener = new myListener();
  
   for (;Alphabet < 91; Alphabet++) { // 아스키코드의 65가 A이므로 Z인 90까지 Alphabet++
     JLabel label = new JLabel(""+Alphabet); 
     int x = (int) (Math.random() * 245)+5; // x에 5~250사이의 숫자를 랜덤으로 대입 
     int y = (int) (Math.random() * 245)+5; // y에 5~250사이의 숫자를 랜덤으로 대입 
     label.setLocation(x, y); // label을 x, y 위치에 배치 
     label.setSize(15, 15); // label의 크기를 10 x 10 으로 설정 
     label.addMouseListener(listener);
     add(label); // label을 컨텐트팬에 부착 
     Label[(int)(Alphabet-65)] = label; 
  }  
  setLayout(null); // 컨텐트팬의 배치관리자 제거 
  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
  setSize(320, 320); // 프레임의 크기를 320, 320로 설정 
  setVisible(true); 
} 

 
 class myListener extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			JLabel la = (JLabel) e.getSource();
			
			if (la == Label[0]) { // 클릭한 대상이 A가 맞는지 검사하는 조건문
				la.setEnabled(false);
				la.setVisible(false);
			} else {
				int index = 0;
				
				for (int i = 0; i < Label.length; i++) { 
					if (la == Label[i])
						index = i;  // // 현재 무엇을(몇번째를) 클릭했는지 검사해서 index에 결과 삽입
				}
				
				for (int j = 0; j < index; j++) {
					if (Label[j].isEnabled() == true) {	 // 클릭한 값의 전에 하나라도 안없어진 것이 있으면 누른 값밑에 "상대좌표" 표시하고 리턴 	
						if(chack == true) { // 좌표가 이미 출력 되어 있는지 검사 (중복방지)
							chackLocation.setEnabled(false); // 전에 있던 상대좌표 삭제
							chackLocation.setVisible(false);
							chack = false; 
						}
						
						//상대좌표 출력
						chackLocation = new JLabel("("+(Label[j].getX()-Label[index].getX())+","+(Label[j].getY()-Label[index].getY())+")");
						chackLocation.setLocation(Label[index].getX()-10,  Label[index].getY()-30);
						chackLocation.setSize(100, 100);
						add(chackLocation);
						repaint();
						chack = true;
						return;
					}
				}
				Label[index].setEnabled(false); // 순서에 맞게 눌렸다면 알바벳 삭제
				Label[index].setVisible(false);
			}
			
		}
	}
 

}

