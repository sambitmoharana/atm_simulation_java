import java.util.*;

// Base class representing a Bank Account
class Account {
    protected double balance;
    protected int pin;

    public Account(double initialBalance, int pin) {
        this.balance = initialBalance;
        this.pin = pin;
    }

    public boolean verifyPIN(int enteredPIN) {
        return this.pin == enteredPIN;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive.");
        }
        balance += amount;
    }

    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive.");
        }
        if (amount > balance) {
            throw new IllegalArgumentException("Insufficient funds.");
        }
        balance -= amount;
    }
}

// Derived class representing a specific type of Account (Savings)
class SavingsAccount extends Account {
    public SavingsAccount(double initialBalance, int pin) {
        super(initialBalance, pin);
    }

    // Example of inheritance: adding specific behavior
    public void addInterest(double rate) {
        if (rate < 0) throw new IllegalArgumentException("Interest rate cannot be negative.");
        balance += balance * rate / 100;
    }
}

// ATM class handles multiple accounts using a Collection
class ATMSimulation {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Map<Integer, SavingsAccount> accounts = new HashMap<>(); // Stores accounts by account number
    private static SavingsAccount currentAccount;

    public static void main(String[] args) {
        // Sample accounts
        accounts.put(1001, new SavingsAccount(1000.00, 1234));
        accounts.put(1002, new SavingsAccount(500.00, 5678));
        accounts.put(1003, new SavingsAccount(1500.00, 4321));

        System.out.println("=== Welcome to the ATM Simulation ===");

        try {
            login();
            showMenu();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            System.out.println("Thank you for using the ATM!");
        }
    }

    private static void login() {
        int attempts = 0;
        while (attempts < 3) {
            System.out.print("\nEnter Account Number: ");
            int accNum = scanner.nextInt();
            System.out.print("Enter PIN: ");
            int enteredPIN = scanner.nextInt();

            SavingsAccount account = accounts.get(accNum);
            if (account != null && account.verifyPIN(enteredPIN)) {
                currentAccount = account;
                System.out.println("Login successful!\n");
                return;
            } else {
                attempts++;
                System.out.println("Invalid account number or PIN. Attempts left: " + (3 - attempts));
            }
        }
        throw new SecurityException("Too many failed attempts. Access denied.");
    }

    private static void showMenu() {
        while (true) {
            System.out.println("\n--- ATM Menu ---");
            System.out.println("1. Check Balance");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Add Interest");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            try {
                int choice = scanner.nextInt();
                switch (choice) {
                    case 1 -> checkBalance();
                    case 2 -> deposit();
                    case 3 -> withdraw();
                    case 4 -> addInterest();
                    case 5 -> {
                        System.out.println("Session ended. Goodbye!");
                        return;
                    }
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // clear input buffer
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void checkBalance() {
        System.out.printf("Your current balance is: Rs.%.2f%n", currentAccount.getBalance());
    }

    private static void deposit() {
        System.out.print("Enter deposit amount: ");
        double amount = scanner.nextDouble();
        currentAccount.deposit(amount);
        System.out.printf("Rs.%.2f deposited successfully.%n", amount);
    }

    private static void withdraw() {
        System.out.print("Enter withdrawal amount: ");
        double amount = scanner.nextDouble();
        
        currentAccount.withdraw(amount);
        System.out.printf("Rs.%.2f withdrawn successfully.%n", amount);
    }

    private static void addInterest() {
        System.out.print("Enter interest rate (%): ");
        double rate = scanner.nextDouble();
        currentAccount.addInterest(rate);
        System.out.println("Interest added successfully!");
    }
}
