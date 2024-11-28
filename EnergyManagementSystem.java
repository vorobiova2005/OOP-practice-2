import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

// Інтерфейс для тарифікації
interface EnergyPricing {
    double DEFAULT_RATE = 1.5; // грн за кВт/год
    
    void updateEnergyRate(double newRate);
    double getCurrentEnergyRate();
}

// Абстрактний клас для користувача енергосистеми
abstract class EnergyUser {
    private String name;
    private String address;

    public EnergyUser(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public abstract void displayUserInfo();
}

// Клас споживача електроенергії
class EnergyConsumer extends EnergyUser {
    private double monthlyConsumption;
    private List<EnergyMeter> meters;

    public EnergyConsumer(String name, String address) {
        super(name, address);
        this.meters = new ArrayList<>();
    }

    public void addMeter(EnergyMeter meter) {
        meters.add(meter);
    }

    public double calculateMonthlyConsumption() {
        return meters.stream()
                     .mapToDouble(EnergyMeter::getCurrentReading)
                     .sum();
    }

    @Override
    public void displayUserInfo() {
        System.out.println("Споживач: " + getName());
        System.out.println("Адреса: " + getAddress());
        System.out.println("Місячне споживання: " + calculateMonthlyConsumption() + " кВт/год");
    }
}

// Клас постачальника електроенергії
class EnergySupplier extends EnergyUser implements EnergyPricing {
    private double currentEnergyRate;

    public EnergySupplier(String name, String address) {
        super(name, address);
        this.currentEnergyRate = DEFAULT_RATE;
    }

    @Override
    public void updateEnergyRate(double newRate) {
        this.currentEnergyRate = newRate;
        System.out.println("Оновлено тариф на електроенергію: " + newRate + " грн/кВт·год");
    }

    @Override
    public double getCurrentEnergyRate() {
        return currentEnergyRate;
    }

    @Override
    public void displayUserInfo() {
        System.out.println("Постачальник: " + getName());
        System.out.println("Поточний тариф: " + getCurrentEnergyRate() + " грн/кВт·год");
    }
}

// Клас лічильника електроенергії
class EnergyMeter {
    private String meterId;
    private double currentReading;
    private LocalDate lastReadingDate;

    public EnergyMeter(String meterId) {
        this.meterId = meterId;
        this.currentReading = 0;
        this.lastReadingDate = LocalDate.now();
    }

    public void updateReading(double newReading) {
        this.currentReading = newReading;
        this.lastReadingDate = LocalDate.now();
    }

    public double getCurrentReading() {
        return currentReading;
    }

    public String getMeterId() {
        return meterId;
    }
}

// Клас рахунку за електроенергію
class EnergyBill {
    private EnergyConsumer consumer;
    private EnergySupplier supplier;
    private LocalDate billDate;
    private double totalConsumption;
    private double totalCost;

    public EnergyBill(EnergyConsumer consumer, EnergySupplier supplier) {
        this.consumer = consumer;
        this.supplier = supplier;
        this.billDate = LocalDate.now();
        calculateBill();
    }

    private void calculateBill() {
        totalConsumption = consumer.calculateMonthlyConsumption();
        totalCost = totalConsumption * supplier.getCurrentEnergyRate();
    }

    public void displayBillDetails() {
        System.out.println("Рахунок за електроенергію");
        System.out.println("Дата: " + billDate);
        System.out.println("Споживач: " + consumer.getName());
        System.out.println("Постачальник: " + supplier.getName());
        System.out.println("Спожито електроенергії: " + totalConsumption + " кВт/год");
        System.out.println("Тариф: " + supplier.getCurrentEnergyRate() + " грн/кВт·год");
        System.out.println("Загальна вартість: " + totalCost + " грн");
    }
}

public class EnergyManagementSystem {
    public static void main(String[] args) {
        // Створення постачальника електроенергії
        EnergySupplier supplier = new EnergySupplier("Київенерго", "вул. Хрещатик, 12");
        
        // Створення споживача
        EnergyConsumer consumer = new EnergyConsumer("Іван Петров", "вул.Ropolitan, 42");
        
        // Додавання лічильників споживачу
        EnergyMeter meter1 = new EnergyMeter("M001");
        meter1.updateReading(250.5);
        consumer.addMeter(meter1);

        EnergyMeter meter2 = new EnergyMeter("M002");
        meter2.updateReading(180.3);
        consumer.addMeter(meter2);

        // Виведення інформації про споживача
        consumer.displayUserInfo();

        // Виведення інформації про постачальника
        supplier.displayUserInfo();

        // Зміна тарифу
        supplier.updateEnergyRate(1.8);

        // Створення та виведення рахунку
        EnergyBill bill = new EnergyBill(consumer, supplier);
        bill.displayBillDetails();
    }
}