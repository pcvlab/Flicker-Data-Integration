/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MyProcess;

/**
 *
 * @author amungen
 */
public class TestCode {
    public static void main(String[] args) {
           boolean kontrol =true;
            while(kontrol ==true){
                kontrol =false;
        MyProcess.GetImagesT g1 = new MyProcess.GetImagesT();
        g1.start();
        MyProcess.GetImagesT g2 = new MyProcess.GetImagesT();
        g2.start();
       
       
        
    }
}
}
