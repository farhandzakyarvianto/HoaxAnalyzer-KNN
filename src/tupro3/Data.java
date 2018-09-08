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
public class Data {
    
    private String nama_berita;
    private int[] data;
    private int hoax;

    public Data(String nama_berita, int[] data, int hoax) {
        this.nama_berita = nama_berita;
        this.data = data;
        this.hoax = hoax;
    }

    /**
     * @return the nama_berita
     */
    public String getNama_berita() {
        return nama_berita;
    }

    /**
     * @return the data
     */
    public int[] getData() {
        return data;
    }

    /**
     * @return the hoax
     */
    public int getHoax() {
        return hoax;
    }
    
    
    
}
