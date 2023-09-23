import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {

    static ArrayList<String> people = new ArrayList();      //lista graczy
    static ArrayList<Double> cost = new ArrayList();        //lista wygranych
    static ArrayList<String> Transfer = new ArrayList<>();  //lista wszystkich transferów

    private static JTextField playersField = new JTextField(5);
    private static JButton submitButton = new JButton("Submit");

    public static void main(String[] args) {
        JFrame frame = new JFrame("Transfer Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 500);
        frame.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());

        JLabel playersLabel = new JLabel("Ile zawodników:");
        inputPanel.add(playersLabel);
        inputPanel.add(playersField);
        inputPanel.add(submitButton);

        frame.add(inputPanel, BorderLayout.NORTH);

        JTextArea resultTextArea = new JTextArea(10, 40);
        resultTextArea.setEditable(false);
        frame.add(new JScrollPane(resultTextArea), BorderLayout.CENTER);

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String playersText = playersField.getText();
                int players = Integer.parseInt(playersText);
                people.clear();
                cost.clear();
                Transfer.clear();
                for (int i = 1; i <= players; ++i) {
                    String playerName = JOptionPane.showInputDialog("Podaj imię " + i + " zawodnika:");
                    people.add(playerName);
                }
                for (int i = 0; i < players; ++i) {
                    String costText = JOptionPane.showInputDialog("Podaj wygraną dla " + people.get(i) + ":");
                    double playerCost = Double.parseDouble(costText);
                    cost.add(playerCost);
                }

                if (CheckValues(cost)) {
                    Transfers();
                    for (String s : Transfer)
                        resultTextArea.append(s + "\n");
                } else {
                    resultTextArea.setText("Suma wygranych nie wynosi 0, proszę wprowadzić poprawne dane.");
                }
            }
        });

        frame.setVisible(true);
    }

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

            for(int i = 0; i < 6; ++i){
                String tmp = scanner.nextLine();
                people.add(tmp);
            }
            for(int i = 0; i < 6; ++i){
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
            double tmp = cost.get(0) + cost.get(cost.size() - 1);   //suma najwiekszej wygranej i przegranej

            if (tmp >= 0) {     //jezeli cala przegrana pokrywa kogos wygrana
                cost.set(0, tmp);
                Transfer.add((people.get(people.size() - 1)) + " daje " + -cost.get(cost.size() - 1) + " dla " + people.get(0));
                cost.set((cost.size() - 1), 0.0);
            }else if (tmp < 0) {    //jezeli przegrana jest wieksza od wygranej
                Transfer.add((people.get(people.size() - 1)) + " daje " + -tmp + " dla " + people.get(0));
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
}
