import java.util.*;


public class Main {

    public static void main(String[] args) 
    {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        int level = 1;
        boolean playing = true;

        System.out.println("Welcome to the Number Memory Test!");

        while (playing) {
            System.out.println("\nLevel " + level);
            List<Integer> numbers = generateRandomNumbers(level, random);
            System.out.println("Memorize these numbers:");
            displayNumbers(numbers);
            pause(1000 * (level + 1));
            clearConsole();

            System.out.println("Enter the numbers: ");
            String input = scanner.nextLine();
            List<Integer> userNumbers = parseInput(input);

            if (checkAnswer(numbers, userNumbers)) {
                System.out.println("Correct!");
                level++;
            } else {
                System.out.println("Incorrect. Game Over!");
                System.out.print("The correct sequence:");
                for(int i = 0; i < numbers.size(); i++) {
                    System.out.print(numbers.get(i) + " ");
                }
                System.out.println();
                System.out.println("You reached level " + level);
                playing = false;
            }
        }
        scanner.close();
    }

    private static List<Integer> generateRandomNumbers(int count, Random random) {
        List<Integer> numbers = new ArrayList<>();
        for(int i = 0; i < count; i++) 
        {
            numbers.add(random.nextInt(10));
        }
        return numbers;
    }

    private static void displayNumbers(List<Integer> numbers) {
        for(int i = 0; i < numbers.size(); i++) {
            System.out.print(numbers.get(i) + " ");
        }
        System.out.println();
    }

    private static void pause(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


    private static void clearConsole() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }

    private static List<Integer> parseInput(String input) {
        List<Integer> numbers = new ArrayList<>();
        for (String part : parts) {
            try {
                numbers.add(Integer.parseInt(part));
            } catch (NumberFormatException e) {

            }
        }
        return numbers;
    }

    private static boolean checkAnswer(List<Integer> original, List<Integer> user) {
        return original.equals(user);
    }
}
