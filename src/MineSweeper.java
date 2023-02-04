import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

public class MineSweeper {
    private static int isaretSayisi;
    private static int dogruTahminSayisi;
    private static int mayinSayisi;
    public void run() throws InterruptedException {
        bilgiGiris();
    }

    public static void bilgiGiris() throws InterruptedException {
        System.out.println("*******************************");
        System.out.println("******** MAYIN TARLASI ********");
        System.out.println("*******************************");
        Scanner scanInt = new Scanner(System.in);
        System.out.print("\nSatır sayısını girin(1-20) : ");
        int satirSayisi = scanInt.nextInt();
        System.out.print("Sütun sayısını girin(1-20) : ");
        int sutunSayisi = scanInt.nextInt();
        System.out.print("1-Kolay\n2-Orta\n3-Zor\nZorluk seviyesi girin:");
        int zorlukSeviyesi = scanInt.nextInt();

        mayinSayisi = mayinSayisiHesapla(satirSayisi, sutunSayisi, zorlukSeviyesi);

        String[][] mayinliBolge = mayinDose(satirSayisi, sutunSayisi, mayinSayisi);
        int[][] SayiTablosu = mayinliBolgeSayiliOlustur(mayinliBolge, satirSayisi, sutunSayisi);
        int[][] Takip = new int[satirSayisi][sutunSayisi]; // elemanları: 0 = Kapalı(işaretsiz) / 1 = Açık / 2 = İşaretli
        ArrayList<int[][]> mayin = new ArrayList<>();
        mayin.add(SayiTablosu);
        mayin.add(Takip);

        // mayinliBolgeYazdir(mayinliBolge);
        // mayinliBolgeSayiliYazdir(SayiTablosu);
        sonHaliYazdir(mayin);
        oyunaBasla(mayin, mayinSayisi);


    }

    private static int mayinSayisiHesapla(int satirSayisi, int sutunSayisi, int zorlukSeviyesi) {
        return ((satirSayisi * sutunSayisi) / 8) * zorlukSeviyesi;
    }

    public static void oyunaBasla(ArrayList<int[][]> mayin, int mayinSayisi) throws InterruptedException {
        int sayac = 1;
        Scanner scanInt = new Scanner(System.in);
        System.out.println("\nMayınlı bölge oluşturuldu..");
        Thread.sleep(1000);
        System.out.println("Toplam " + mayinSayisi + " tane mayın işaretlemeniz gerekiyor...");
        Thread.sleep(1000);
        while (true) {
            System.out.print(sayac + ". tahmininiz için \nSatır numarası girin = ");
            int s = scanInt.nextInt();
            System.out.print("Sütun numarası girin = ");
            int t = scanInt.nextInt();
            if (s >= 0 && s < mayin.get(0).length && t >= 0 && t < mayin.get(0).length) {
                sayac++;

                System.out.println("Bu hücreyi açmak için 1, işaretlemek için 2, işareti kaldırmak için 0 giriniz.");
                int secim;
                secim = scanInt.nextInt();
                switch (secim) {
                    case 0:
                        isaretiKaldir(mayin, s, t);
                        break;
                    case 1:
                        hucreAc(mayin, s, t);
                        break;
                    case 2:
                        isaretle(mayin, s, t);
                        break;
                    default:
                        System.out.println("Hatalı giriş..");
                }
            } else System.out.println("hatalı giriş yaptınız");
        }
    }

    private static void hucreAc(ArrayList<int[][]> mayin, int s, int t) {
        if (mayin.get(1)[s][t] == 1) System.out.println("Bu hücre zaten açık");
        else {
            if (mayin.get(0)[s][t] == 9) {
                System.out.println("Mayına bastınız.. GAME OVER!");
                System.exit(1);
            } else if (mayin.get(0)[s][t] != 0) mayin.get(1)[s][t] = 1;
            else hucreleriAc(mayin, s, t);
        }
        sonHaliYazdir(mayin);
    }

    private static void isaretle(ArrayList<int[][]> mayin, int s, int t) {
        if (mayin.get(1)[s][t] == 2) System.out.println("Bu hücre zaten işaretlenmiş.");
        else if (mayin.get(1)[s][t] == 1) System.out.println("Bu hücre açık");
        else {mayin.get(1)[s][t] = 2; isaretSayisi++;}
        if (mayin.get(0)[s][t]==9) dogruTahminSayisi++;
        if (dogruTahminSayisi==isaretSayisi && mayinSayisi==isaretSayisi){sonHaliYazdir(mayin); System.out.println("Tebrikler...!  Tüm mayınları doğru işaretlediniz..");
            System.exit(2);}
        sonHaliYazdir(mayin);
    }

    private static void isaretiKaldir(ArrayList<int[][]> mayin, int s, int t) {
        if (mayin.get(1)[s][t] == 0) System.out.println("Bu hücre işaretlenmemiş.");
        else if (mayin.get(1)[s][t] == 1) System.out.println("Bu hücre açık");
        else {mayin.get(1)[s][t] = 0; isaretSayisi--;}
        sonHaliYazdir(mayin);

    }

    public static int[][] mayinliBolgeSayiliOlustur(String[][] mayinliBolge, int satirSayisi, int sutunSayisi) {
        int[][] mayinliBolgeSayili = new int[satirSayisi][sutunSayisi];
        for (int i = 0; i < satirSayisi; i++) {
            for (int j = 0; j < sutunSayisi; j++) {
                if (Objects.equals(mayinliBolge[i][j], "-")) {
                    for (int k = i - 1; k <= i + 1; k++) {
                        for (int l = j - 1; l <= j + 1; l++) {
                            if ((k == i && l == j) || k < 0 || l < 0 || k >= satirSayisi || l >= sutunSayisi) continue;
                            if (mayinliBolge[k][l].equals("*")) {
                                mayinliBolgeSayili[i][j]++;
                            }
                        }
                    }
                } else mayinliBolgeSayili[i][j] = 9;
            }
        }
        return mayinliBolgeSayili;
    }

    public static void mayinliBolgeSayiliYazdir(int[][] mayinliBolgeSayili) { // Mayin tarlasında mayınlara temas eden hücreler yazdiriliyor
        for (int[] i : mayinliBolgeSayili) {
            for (int s : i) {
                System.out.print(s + "\t");
            }
            System.out.println();
        }
    }

    public static void mayinliBolgeYazdir(String[][] mayinliBolge) { // Yeni oluşturulan mayin tarlası yazdiriliyor
        for (String[] strings : mayinliBolge) {
            for (String string : strings) {
                System.out.print(string + "\t");
            }
            System.out.println();
        }
        System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
    }

    public static String[][] mayinDose(int satirSayisi, int sutunSayisi, int mayinSayisi) {
        String[][] mayinliBolge = new String[satirSayisi][sutunSayisi];
        for (String[] strings : mayinliBolge) {
            Arrays.fill(strings, "-");
        }

        for (int i = 0; i < mayinSayisi; i++) {
            int mayinSatir = (int) (Math.random() * satirSayisi);
            int mayinSutun = (int) (Math.random() * sutunSayisi);
            if (!Objects.equals(mayinliBolge[mayinSatir][mayinSutun], "*")) {
                mayinliBolge[mayinSatir][mayinSutun] = "*";
            } else i--;
        }
        return mayinliBolge;
    }

    public static void sonHaliYazdir(ArrayList<int[][]> mayin) {
        for (int i = 0; i < mayin.get(0).length; i++) {
            for (int j = 0; j < mayin.get(0)[0].length; j++) {
                if (mayin.get(1)[i][j] == 0) {
                    System.out.print("-" + "\t");
                }//Hücre kapalıysa - yazacak
                if (mayin.get(1)[i][j] == 1) {
                    if (mayin.get(0)[i][j] == 0) System.out.print(" " + "\t");
                    else
                        System.out.print(mayin.get(0)[i][j] + "\t");// Hücre açıksa Hücrenin değdiği mayın sayısını yazacak
                }
                if (mayin.get(1)[i][j] == 2) System.out.print("F" + "\t");// Hücre işaretliyse F yazacak
            }
            System.out.println();
        }
    }


    private static void hucreleriAc(ArrayList<int[][]> mayin, int s, int t) {
        if (s < 0 || s > mayin.get(0).length - 1 || t < 0 || t > mayin.get(0)[0].length - 1 || mayin.get(0)[s][t] != 0 || mayin.get(1)[s][t] == 1)
            return;
        else {
            mayin.get(1)[s][t] = 1;
            hucreleriAc(mayin, s - 1, t);
            hucreleriAc(mayin, s + 1, t);
            hucreleriAc(mayin, s, t - 1);
            hucreleriAc(mayin, s, t + 1);
        }
    }
}
