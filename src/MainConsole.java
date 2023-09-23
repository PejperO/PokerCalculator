import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class MainConsole {

    static ArrayList<String> people = new ArrayList();      //lista graczy
    static ArrayList<Double> cost = new ArrayList();        //lista wygranych
    static ArrayList<String> Transfer = new ArrayList<>();  //lista wszystkich transferów

    public static boolean CheckValues(ArrayList<Double> list){    //sprawdza czy kwoty są poprawne
        double sum = 0;                                           //jezeli ich suma != 0 to jest źle policzona kasa
        for (Double aDouble : list)
            sum += aDouble;
        return sum == 0;
    }

    public static void ConsoleInput(){    //dane z konsoli idą do list
        Scanner input = new Scanner(System.in);
        System.out.print("Ile zawodników: ");
        int players = input.nextInt();
        input.nextLine();

        for(int i = 1; i <= players; ++i){
            System.out.print("podaj imię " + i + " zawodnika: ");
            String tmp = input.nextLine();
            people.add(tmp);
        }

        for(int i = 0; i < players; ++i){
            System.out.print("podaj wygraną " + people.get(i) + ": ");
            double tmp = input.nextDouble();
            cost.add(tmp);
        }
    }

    public static void FileInput(){    //dane z pliku, dla wygody testow
        try {
            Scanner scanner = new Scanner(new File("src/data.txt"));

            for(int i = 0; i < 4; ++i){
                String tmp = scanner.nextLine();
                people.add(tmp);
            }
            for(int i = 0; i < 4; ++i){
                double tmp = Double.parseDouble(scanner.nextLine());
                cost.add(tmp);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }

    public static void Sort(){    //Sortowanie babelkowe obydwu list wzgledem cost / Collections.sort nie posortuje 2 na raz
        for (int i = 0; i < cost.size() - 1; i++)
            for (int j = 0; j < cost.size() - i - 1; j++)
                if (cost.get(j) < cost.get(j+1)) {
                    Collections.swap(cost, j, j+1);
                    Collections.swap(people, j, j+1);
                }
    }

    public static void Transfers(){
        boolean loop = true;

        while(loop){
            Sort();
            System.out.println(cost);
            double tmp = cost.get(0) + cost.get(cost.size()-1);   //suma najwiekszej wygranej i przegranej
            System.out.println(tmp);

            if (tmp >= 0) {     //jezeli cala przegrana pokrywa kogos wygrana
                cost.set(0, tmp);
                Transfer.add((people.get(people.size() - 1)) + " daje " + -cost.get(cost.size() - 1) + " dla " + people.get(0));
                System.out.println(Transfer.get(Transfer.size()-1));
                cost.set((cost.size() - 1), 0.0);
            }else if (tmp < 0) {    //jezeli przegrana jest wieksza od wygranej
                Transfer.add((people.get(people.size() - 1)) + " daje " + cost.get(0) + " dla " + people.get(0));
                System.out.println(Transfer.get(Transfer.size()-1));
                cost.set(0, 0.0);
                cost.set((cost.size() - 1), tmp);
            }

            loop = false;
            for(int i =0; i < cost.size()-1; ++i)   //loop do momentu az wszystkie kwoty sie nie wyzeruja
                if (cost.get(i) != 0.0) {
                    loop = true;
                    break;
                }
        }
    }

    public static void main(String[] args) {

        FileInput();
        //ConsoleInput();
        System.out.println(CheckValues(cost));

        Transfers();
        System.out.println(Transfer);
    }
}
