import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

public class MineSweeper {
    private static int isaretSayisi;
    private static int dogruTahminSayisi;
    private static int mayinSayisi;
    private static int[][] mayinliBolge;
    private static int[][] Takip;

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
        Takip = new int[satirSayisi][sutunSayisi]; // elemanları: 0 = Kapalı(işaretsiz) / 1 = Açık / 2 = İşaretli

        // mayinliBolgeYazdir(mayinliBolge);
        // mayinliBolgeSayiliYazdir(SayiTablosu);
        mayinDose(satirSayisi, sutunSayisi, mayinSayisi);
        sonHaliYazdir();
        oyunaBasla();
    }

    private static int mayinSayisiHesapla(int satirSayisi, int sutunSayisi, int zorlukSeviyesi) {
        return ((satirSayisi * sutunSayisi) / 8) * zorlukSeviyesi;
    }

    public static void mayinDose(int satirSayisi, int sutunSayisi, int mayinSayisi) {
        mayinliBolge = new int[satirSayisi][sutunSayisi];
        for (int i = 0; i < mayinSayisi; i++) {
            int mayinSatir = (int) (Math.random() * satirSayisi);
            int mayinSutun = (int) (Math.random() * sutunSayisi);
            if (mayinliBolge[mayinSatir][mayinSutun] != 9) {
                mayinliBolge[mayinSatir][mayinSutun] = 9;
            } else i--;
        }

        for (int i = 0; i < satirSayisi; i++) {
            for (int j = 0; j < sutunSayisi; j++) {
                if (mayinliBolge[i][j] == 0) {
                    for (int k = i - 1; k <= i + 1; k++) {
                        for (int l = j - 1; l <= j + 1; l++) {
                            if ((k == i && l == j) || k < 0 || l < 0 || k >= satirSayisi || l >= sutunSayisi) continue;
                            if (mayinliBolge[k][l] == 9) {
                                mayinliBolge[i][j]++;
                            }
                        }
                    }
                }
            }
        }
    }

    public static void oyunaBasla() throws InterruptedException {
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
            if (s >= 0 && s < mayinliBolge.length && t >= 0 && t < mayinliBolge[0].length) {
                sayac++;

                System.out.println("Bu hücreyi açmak için 1, işaretlemek için 2, işareti kaldırmak için 0 giriniz.");
                int secim;
                secim = scanInt.nextInt();
                switch (secim) {
                    case 0:
                        isaretiKaldir(s, t);
                        break;
                    case 1:
                        hucreAc(s, t);
                        break;
                    case 2:
                        isaretle(s, t);
                        break;
                    default:
                        System.out.println("Hatalı giriş..");
                }
            } else System.out.println("hatalı giriş yaptınız");
        }
    }

    private static void hucreAc(int s, int t) {
        if (Takip[s][t] == 1) System.out.println("Bu hücre zaten açık");
        else {
            if (mayinliBolge[s][t] == 9) {
                System.out.println("Mayına bastınız.. GAME OVER!");
                System.exit(1);
            } else if (mayinliBolge[s][t] != 0) Takip[s][t] = 1;
            else hucreleriAc(s, t);
        }
        sonHaliYazdir();
    }

    private static void isaretle(int s, int t) {
        if (Takip[s][t] == 2) System.out.println("Bu hücre zaten işaretlenmiş.");
        else if (Takip[s][t] == 1) System.out.println("Bu hücre açık");
        else {
            Takip[s][t] = 2;
            isaretSayisi++;
        }
        if (mayinliBolge[s][t] == 9) dogruTahminSayisi++;
        if (dogruTahminSayisi == isaretSayisi && mayinSayisi == isaretSayisi) {
            sonHaliYazdir();
            System.out.println("Tebrikler...!  Tüm mayınları doğru işaretlediniz..");
            System.exit(2);
        }
        sonHaliYazdir();
    }

    private static void isaretiKaldir(int s, int t) {
        if (Takip[s][t] == 0) System.out.println("Bu hücre işaretlenmemiş.");
        else if (Takip[s][t] == 1) System.out.println("Bu hücre açık");
        else {
            Takip[s][t] = 0;
            isaretSayisi--;
        }
        sonHaliYazdir();
    }

    public static void mayinliBolgeYazdir() { // Yeni oluşturulan mayin tarlası yazdiriliyor
        for (int[] ints : mayinliBolge) {
            for (int i : ints) {
                System.out.print(i + "\t");
            }
            System.out.println();
        }
        System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
    }

    public static void sonHaliYazdir() {
        for (int i = 0; i < mayinliBolge.length; i++) {
            for (int j = 0; j < mayinliBolge[0].length; j++) {
                if (Takip[i][j] == 0) {
                    System.out.print("-" + "\t");
                }//Hücre kapalıysa - yazacak
                if (Takip[i][j] == 1) {
                    if (mayinliBolge[i][j] == 0) System.out.print(" " + "\t");
                    else
                        System.out.print(mayinliBolge[i][j] + "\t");// Hücre açıksa Hücrenin değdiği mayın sayısını yazacak
                }
                if (Takip[i][j] == 2) System.out.print("F" + "\t");// Hücre işaretliyse F yazacak
            }
            System.out.println();
        }
    }

    private static void hucreleriAc(int s, int t) {
        if (s < 0 || s > mayinliBolge.length - 1 || t < 0 || t > mayinliBolge[0].length - 1 || mayinliBolge[s][t] != 0 || Takip[s][t] == 1)
            return;
        else {
            Takip[s][t] = 1;
            hucreleriAc(s - 1, t);
            hucreleriAc(s + 1, t);
            hucreleriAc(s, t - 1);
            hucreleriAc(s, t + 1);
        }
    }
}
