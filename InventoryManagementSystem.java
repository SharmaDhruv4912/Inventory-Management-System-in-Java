import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

class InventoryItem {
    private String name;
    private double price;
    private int quantity;

    public InventoryItem(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

class InventoryManager {
    private List<InventoryItem> inventory;

    public InventoryManager() {
        inventory = new ArrayList<>();
    }

    // Add an item to inventory
    public synchronized void addItem(String name, double price, int quantity) {
        for (InventoryItem item : inventory) {
            if (item.getName().equals(name)) {
                // Item already exists, update quantity
                item.setQuantity(item.getQuantity() + quantity);
                System.out.println("Item quantity updated in inventory.");
                return;
            }
        }
        // Add new item to inventory
        InventoryItem newItem = new InventoryItem(name, price, quantity);
        inventory.add(newItem);
        System.out.println("Item added to inventory.");
    }

    // Remove an item from inventory
    public synchronized void removeItem(String name) {
        InventoryItem itemToRemove = null;
        for (InventoryItem item : inventory) {
            if (item.getName().equals(name)) {
                itemToRemove = item;
                break;
            }
        }
        if (itemToRemove != null) {
            inventory.remove(itemToRemove);
            System.out.println("Item removed from inventory.");
        } else {
            System.out.println("Item not found in inventory.");
        }
    }

    // Display the current inventory
    public synchronized List<InventoryItem> getInventory() {
        return new ArrayList<>(inventory);
    }
}

public class InventoryManagementSystem extends Frame {
    private InventoryManager inventoryManager = new InventoryManager();
    private TextField itemNameField, itemPriceField, itemQuantityField;
    private TextArea inventoryDisplayArea;

    public InventoryManagementSystem() {
        super("Inventory Management System");
        setLayout(new FlowLayout());

        Label nameLabel = new Label("Name:");
        itemNameField = new TextField(20);
        add(nameLabel);
        add(itemNameField);

        Label priceLabel = new Label("Price:");

        itemPriceField = new TextField(10);
        add(priceLabel);
        add(itemPriceField);

        Label quantityLabel = new Label("Quantity:");
        itemQuantityField = new TextField(5);
        add(quantityLabel);
        add(itemQuantityField);

        Button addButton = new Button("Add Item");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = itemNameField.getText();
                double price = Double.parseDouble(itemPriceField.getText());
                int quantity = Integer.parseInt(itemQuantityField.getText());
                inventoryManager.addItem(name, price, quantity);
                updateDisplay();
            }
        });
        add(addButton);

        Button removeButton = new Button("Remove Item");
        removeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = itemNameField.getText();
                inventoryManager.removeItem(name);
                updateDisplay();
            }
        });
        add(removeButton);

        inventoryDisplayArea = new TextArea(10, 40);
        inventoryDisplayArea.setEditable(false);
        add(inventoryDisplayArea);

        updateDisplay();

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
                System.exit(0);
            }
        });

        setSize(400, 300);
        setVisible(true);
    }

    private void updateDisplay() {
        List<InventoryItem> inventory = inventoryManager.getInventory();
        inventoryDisplayArea.setText("");
        for (InventoryItem item : inventory) {
            inventoryDisplayArea.append(item.getName() + " - $" + item.getPrice() + " - Quantity: " + item.getQuantity() + "\n");
        }
    }

    public static void main(String[] args) {
        new InventoryManagementSystem();
    }
}
