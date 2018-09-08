/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this temp_dlate file, choose Tools | Templates
 * and open the temp_dlate in the editor.
 */
package tupro3;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author FarhanDzaky
 */
public class Knn {

    private ArrayList<Data> data = new ArrayList<>();
    private ArrayList<Data> data_C = new ArrayList<>();
    private ArrayList<Data> data_D = new ArrayList<>();
    private ArrayList<Data> data_train = new ArrayList<>();
    private ArrayList<Data> data_tmp = new ArrayList<>();

    public void connection() {
        java.sql.Connection conn = null;
        Statement stmt = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");

            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_tupro3", "root", "");

            stmt = conn.createStatement();
            String sql;
            sql = "SELECT nama_berita, like_berita, provokasi_berita, komentar_berita, emosi_berita, hoax_berita FROM hoax";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String nama_berita = rs.getString("nama_berita");
                int like_berita = rs.getInt("like_berita");
                int provokasi_berita = rs.getInt("provokasi_berita");
                int komentar_berita = rs.getInt("komentar_berita");
                int emosi_berita = rs.getInt("emosi_berita");
                int hoax_berita = rs.getInt("hoax_berita");

                data.add(new Data(nama_berita, new int[]{like_berita, provokasi_berita, komentar_berita,
                    emosi_berita}, hoax_berita));
                data_D.add(new Data(nama_berita, new int[]{like_berita, provokasi_berita, komentar_berita,
                    emosi_berita}, hoax_berita));

            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException s) {

            }
            try {
                if (conn != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
                se2.printStackTrace();
            }
        }

    }

    private static int a, b;
    private static String f;
    private static int p, q, r, s;
    private int k;
    private ArrayList<Data> dataSet;

    public Knn() {

    }

    public Knn(ArrayList<Data> dataSet, int k) {
        this.dataSet = dataSet;
        this.k = k;
    }

    private Data[] getNearDistance(Data x) {
        Data[] retur = new Data[this.k];
        double temp_d = Double.MIN_VALUE;
        int rank = 0;
        for (Data tse : this.dataSet) {
            double distance = distance(x, tse);

            if (retur[retur.length - 1] == null) { //Hvis ikke fyldt
                int j = 0;
                while (j < retur.length) {
                    if (retur[j] == null) {
                        retur[j] = tse;
                        break;
                    }
                    j++;
                }
                if (distance > temp_d) {
                    rank = j;
                    temp_d = distance;
                }
            } else {
                if (distance < temp_d) {
                    retur[rank] = tse;
                    double f = 0.0;
                    int ind = 0;
                    for (int j = 0; j < retur.length; j++) {
                        double dt = distance(retur[j], x);
                        if (dt > f) {
                            f = dt;
                            ind = j;
                        }
                    }
                    temp_d = f;
                    rank = ind;

                }
            }
        }
        return retur;
    }

    public Object getKlasifikasi(Data e) {
        HashMap<Object, Double> classcount = new HashMap<Object, Double>();
        Data[] de = this.getNearDistance(e);

        for (int i = 0; i < de.length; i++) {
            double distance = Knn.distance(de[i], e);

            if (!classcount.containsKey(de[i].getHoax())) {
                classcount.put(de[i].getHoax(), distance);
            } else {
                classcount.put(de[i].getHoax(), classcount.get(de[i].getHoax()) + distance);

            }
        }

        Object o = null;
        double max = 0;
        for (Object ob : classcount.keySet()) {
            if (classcount.get(ob) > max) {
                max = classcount.get(ob);
                o = ob;
            }
        }
        return o;
    }

    public static double distance(Data a, Data b) {
        double distance = 0.0;
        int length = a.getData().length;
        for (int i = 0; i < length; i++) {
            double t = a.getData()[i] - b.getData()[i];
            distance = distance + t * t;
        }
        double z = Math.sqrt(distance);
        return z;
    }

    public void view(int k) {

        for (int i = 0; i < 4000; i++) {
            data_C.add(i, data.get(i));
        }

        Knn nm = new Knn(data_C, k);

        System.out.println("Berita| L  | P  | K  | E  |  H ");
        for (int i = 4000; i < data.size(); i++) {
            f = data.get(i).getNama_berita();
            p = data.get(i).getData()[0];
            q = data.get(i).getData()[1];
            r = data.get(i).getData()[2];
            s = data.get(i).getData()[3];

            a = (int) nm.getKlasifikasi(new Data(f, new int[]{p, q, r, s}, 0));

            String ho;
            if (a == 0) {
                ho = "Tidak Hoax";
            } else {
                ho = "Hoax";
            }

            System.out.println(f + " | " + p + " | " + q + " | " + r + " | "
                    + s + " | " + "(" + a + ") " + ho);

        }
    }
    private ArrayList<Data> data_tmp1 = new ArrayList<>();
    private ArrayList<Data> data_tmp2 = new ArrayList<>();
    private ArrayList<Data> data_tmp3 = new ArrayList<>();
    private ArrayList<Data> data_tmp4 = new ArrayList<>();
    DecimalFormat df = new DecimalFormat("0.00##");

    public double viewC(int k, int test) {

        double c = 0;
        double d1, d2, d3, d4;

        data_tmp1 = (ArrayList<Data>) data_D.clone();
        data_tmp2 = (ArrayList<Data>) data_D.clone();
        data_tmp3 = (ArrayList<Data>) data_D.clone();
        data_tmp4 = (ArrayList<Data>) data_D.clone();

        switch (test) {
            case 1:
                for (int i = 0; i < 1000; i++) {
                    data_tmp1.remove(0);
                }
                for (int i = 0; i < 3000; i++) {
                    data_train.add(i, data_tmp1.get(i));
                }
                Knn nn = new Knn(data_train, k);

                //menghitung presentase akurasi data
                for (int i = 0; i < 1000; i++) {
                    f = data.get(i).getNama_berita();
                    p = data.get(i).getData()[0];
                    q = data.get(i).getData()[1];
                    r = data.get(i).getData()[2];
                    s = data.get(i).getData()[3];

                    a = (int) nn.getKlasifikasi(new Data(f, new int[]{p, q, r, s}, 0));
                    b = (int) data.get(i).getHoax();

                    if (a == b) {
                        c++;
                    }
                }
                d1 = (c / 1000.0) * 100.0;
                String result = df.format(d1);
                System.out.println("Akurasi 1000 data pertama : " + result + "%");
                return d1;
            case 2:
                for (int i = 0; i < 1000; i++) {
                    data_tmp2.remove(1000);
                }
                for (int i = 0; i < 3000; i++) {
                    data_train.add(i, data_tmp2.get(i));
                }
                Knn nn1 = new Knn(data_train, k);

                //menghitung presentase akurasi data
                for (int i = 1000; i < 2000; i++) {
                    f = data.get(i).getNama_berita();
                    p = data.get(i).getData()[0];
                    q = data.get(i).getData()[1];
                    r = data.get(i).getData()[2];
                    s = data.get(i).getData()[3];

                    a = (int) nn1.getKlasifikasi(new Data(f, new int[]{p, q, r, s}, 0));
                    b = (int) data.get(i).getHoax();

                    if (a == b) {
                        c++;
                    }
                }
                d2 = (c / 1000.0) * 100.0;
                String result1 = df.format(d2);
                System.out.println("Akurasi 1000 data kedua : " + result1 + "%");
                return d2;
            case 3:
                for (int i = 0; i < 1000; i++) {
                    data_tmp3.remove(2000);
                }
                for (int i = 0; i < 3000; i++) {
                    data_train.add(i, data_tmp3.get(i));
                }
                Knn nn2 = new Knn(data_train, k);

                //menghitung presentase akurasi data
                for (int i = 2000; i < 3000; i++) {
                    f = data.get(i).getNama_berita();
                    p = data.get(i).getData()[0];
                    q = data.get(i).getData()[1];
                    r = data.get(i).getData()[2];
                    s = data.get(i).getData()[3];

                    a = (int) nn2.getKlasifikasi(new Data(f, new int[]{p, q, r, s}, 0));
                    b = (int) data.get(i).getHoax();

                    if (a == b) {
                        c++;
                    }
                }
                d3 = (c / 1000.0) * 100.0;
                String result2 = df.format(d3);
                System.out.println("Akurasi 1000 data ketiga : " + result2 + "%");
                return d3;
            case 4:
                for (int i = 0; i < 1000; i++) {
                    data_tmp2.remove(3000);
                }
                for (int i = 0; i < 3000; i++) {
                    data_train.add(i, data_tmp2.get(i));
                }
                Knn nn3 = new Knn(data_train, k);

                //menghitung presentase akurasi data
                for (int i = 3000; i < 4000; i++) {
                    f = data.get(i).getNama_berita();
                    p = data.get(i).getData()[0];
                    q = data.get(i).getData()[1];
                    r = data.get(i).getData()[2];
                    s = data.get(i).getData()[3];

                    a = (int) nn3.getKlasifikasi(new Data(f, new int[]{p, q, r, s}, 0));
                    b = (int) data.get(i).getHoax();

                    if (a == b) {
                        c++;
                    }
                }
                d4 = (c / 1000.0) * 100.0;
                String result3 = df.format(d4);
                System.out.println("Akurasi 1000 data keempat : " + result3 + "%");
                return d4;

            default:
                System.out.println("NONE");
                break;
        }

        return 0;
    }

    public double crossValidation(int k) {

        System.out.println("=============================");

        double pre1 = viewC(k, 1);
        double pre2 = viewC(k, 2);
        double pre3 = viewC(k, 3);
        double pre4 = viewC(k, 4);

        System.out.println("=============================");

        double pre_total = (pre1 + pre2 + pre3 + pre4) / 4.0;
        DecimalFormat df = new DecimalFormat("0.00##");
        String result = df.format(pre_total);
        System.out.println("Akurasi Total : " + result + "%");

        return pre_total;
    }

}
