public class Employee {
    private int id;
    private String name;
    private String position;
    private double hourlyRate;

    public Employee(int id, String name, String position, double hourlyRate) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.hourlyRate = hourlyRate;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPosition() {
        return position;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(double hourlyRate) {
        if (hourlyRate <= 0) {
            throw new IllegalArgumentException("Hourly rate must be greater than zero.");
        }
        this.hourlyRate = hourlyRate;
    }

    @Override
    public String toString() {
        return String.format("ID: %d | Name: %-20s | Position: %-15s | Hourly Rate: PHP %.2f",
                id, name, position, hourlyRate);
    }
}
