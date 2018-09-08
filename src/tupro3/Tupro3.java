/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tupro3;

/**
 *
 * @author FarhanDzaky
 */
public class Tupro3 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        int k = 109;

        Knn a = new Knn();

        a.connection();

        a.view(k);

        a.crossValidation(k);

    }

}
