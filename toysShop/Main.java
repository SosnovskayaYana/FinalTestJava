package toysShop;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {
    static Scanner iScanner = new Scanner(System.in);
    public static void main(String[] args) {

        LinkedList<Toy> toysList = new LinkedList<>();
        LinkedList<String> winnerList = new LinkedList<>();
        controller(toysList, winnerList);

    }

    private static void controller(LinkedList<Toy> list, LinkedList<String> winnerList) {
        if (list.isEmpty()) {
//            System.out.println("Автомат пуст. Необходимо добавить игрушки!");
//            addToy(list, 1);
            list.add(new Toy("кукла", 3, 50));
            list.add(new Toy("машина", 2, 10));
            list.add(new Toy("мяч", 2, 70));
            list.add(new Toy("робот", 1, 20));
        }
        int num = 1;
        int step = 5;
        while (num == 1 || num == 2 || num == 3) {
            System.out.println("Что Вы хотите сделать?\n1 - добавить игрушку;\n2 - играть;\n3 - забрать выигрыш\n0 - выйти.");
            num = iScanner.nextInt();
            if(num==1) {
                addToy(list, num);
            } else if (num == 2) {
                if (step <= 0) {
                    System.out.println("Попыток больше нет.");

                } else {
                    System.out.println("Список игрушек:");
                    list.forEach(n -> System.out.println(n.getInfo()));
                    winnerList = chooseToy(list, winnerList);
                    step--;
                    System.out.println("У Вас есть еще "+step+" попыток");
                }
            } else if (num == 3) {
                if (winnerList.size() != 0) {
                    System.out.println("Ваш выигрыш: ");
                    winnerList.forEach(n -> System.out.println(n));
                    System.out.println("Вытащить игрушку?\n1 - да;\n2 - нет");
                    int ans = iScanner.nextInt();
                    if (ans == 1) {
                        winnerList = pullOutToys(winnerList);
                        System.out.println("Осталось вытащить: ");
                        winnerList.forEach(n -> System.out.println(n));
                    }

                } else {
                    System.out.println("Вы еще ничего не выиграли.");
                }
            }
        }

    }


    private static LinkedList<Toy> addToy(LinkedList<Toy> list, int num) {
        while(num == 1) {
            Scanner iScanner = new Scanner(System.in);
            System.out.print("Введите название игрушки: ");
            String name = iScanner.nextLine();
            int count = 1;
            int odd = 0;
            if (list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getName().equals(name)) {
                        count = list.get(i).getCount();
                        count++;
                        list.get(i).inputCount(count);
                        System.out.println("Такой вид игрушки уже есть. Хотите изменить частоту выпадения данного типа игрушек?\n1 - да; \n0 - нет.");
                        int ans = iScanner.nextInt();

                        if (ans == 0) {
                            odd = list.get(i).getOdd();
                        } else if (ans == 1) {
                            System.out.print("Введите частоту выпадения игрушки (%): ");
                            odd = iScanner.nextInt();
                        }
                        list.get(i).inputOdd(odd);
                        break;

                    } else if (!list.get(i).getName().equals(name) && i == list.size() - 1) {
                        System.out.print("Введите частоту выпадения игрушки (%): ");
                        odd = iScanner.nextInt();
                        list.add(new Toy(name, count, odd));
                        break;
                    }
                }
            }  else {
                System.out.print("Введите частоту выпадения игрушки (%): ");
                odd = iScanner.nextInt();
                list.add(new Toy(name, count, odd));
            }

            System.out.println("Хотите добавить еще игрушку?\n1 - да;\n0 - нет.");
            num = iScanner.nextInt();
        }
        return list;
    }

    private static LinkedList<String> chooseToy (LinkedList<Toy> list, LinkedList<String> winnerList) {
        ArrayList<Toy> variationList = new ArrayList<>();
        int total = totalToys(list);
        for (Toy n: list ) {
            float odd = n.getOdd()/100f;
            int maxCountOdd = Math.round(total*odd);
            for (int i = 0 ; i < maxCountOdd; i++) {
                variationList.add(n);
            }
        }
        int randomToy = new Random().nextInt(variationList.size());
        Toy winnerToy = variationList.get(randomToy);
        if (list.contains(winnerToy)) {
            int winnerIndex = list.indexOf(winnerToy);
            if (list.get(winnerIndex).getCount() != 0) {
                System.out.println("Ура! Ваш приз - " + list.get(winnerIndex).getName());
                int count = list.get(winnerIndex).getCount();
                count--;
                list.get(winnerIndex).inputCount(count);
                System.out.println(list.get(winnerIndex).getInfo());
                winnerList = listingWinnerToys(list.get(winnerIndex), winnerList);

            } else {
                System.out.println("Увы! Вы ничего не выиграли. Попробуйте еще раз");
            }
        }
        return winnerList;

    }

    private static int totalToys (LinkedList<Toy> list) {
        int total = 0;
        for (Toy n: list ) {
            total += n.getCount();
        }
        return total;
    }

    private static LinkedList<String> listingWinnerToys (Toy elem, LinkedList<String> winnerList) {
        int count = 1;
        winnerList.addLast("Название: "+ elem.getName() + " | Количество: " + count);
        return winnerList;
    }

    private static LinkedList<String> pullOutToys(LinkedList<String> winnerList) {
        try (FileWriter fw = new FileWriter("Toys.txt", true)) {
            fw.write(winnerList.getLast());
            fw.append("\n");
            fw.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage()); // обработка ошибок
        }
        winnerList.removeLast();
        return winnerList;
    }

}