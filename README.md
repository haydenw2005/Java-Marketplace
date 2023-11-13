# Marketplace Project README

## Overview

The Marketplace project is a Java implementation of an online marketplace system. It consists of several classes representing different entities such as buyers, sellers, stores, items, and utilities for handling JSON operations.

## How to run
1. Compilation:
   - Open a terminal.
   - Navigate to the project directory.
   - Run the compilation command (e.g., `javac MainClass.java`).

2. Execution:
   - Run the compiled program (e.g., `java MainClass`).

## Submissions
Hayden White - Submitted Report on Brightspace. 
Hayden White - Submitted Vocareum workspace.

## Classes

### Person

- **Functionality:** Represents a generic person with basic attributes like username, password, first name, last name, and email.
- **Testing:** Tested using JUnit to ensure that the getters and setters work as expected.
- **Relationships:** Extended by the `Buyer` and `Seller` classes.

### Buyer

- **Functionality:** Represents a buyer in the marketplace, extending the `Person` class. Includes features like a shopping cart, purchase history, and methods for buying items.
- **Testing:** JUnit tests cover methods related to adding items to the cart, buying items, and managing purchase history.
- **Relationships:** Has a composition relationship with the `Item` class and extends the `Person` class.

### Seller

- **Functionality:** Represents a seller in the marketplace, extending the `Person` class. Manages stores, including creating and editing stores, viewing store items, and tracking buyers.
- **Testing:** JUnit tests cover methods related to creating and editing stores, managing store items, and tracking buyers.
- **Relationships:** Has a composition relationship with the `Store` class and extends the `Person` class.

### Store

- **Functionality:** Represents a store in the marketplace. Manages stock items, sold items, and provides methods to add items to stock.
- **Testing:** JUnit tests cover methods related to adding items to stock and managing sold items.
- **Relationships:** Has a composition relationship with the `Item` class.

### Item

- **Functionality:** Represents an item available in the marketplace, with attributes like name, description, price, stock, and buyers. Also provides methods for handling buyers and stock.
- **Testing:** JUnit tests cover methods related to adding buyers, updating stock, and creating items.
- **Relationships:** Used by the `Store` and `Buyer` classes.

### JsonUtils

- **Functionality:** Provides utility methods for handling JSON operations, such as reading from and writing to JSON files, adding and removing objects from JSON, and checking for the existence of keys.
- **Testing:** JUnit tests cover methods related to adding and removing objects from JSON files.
- **Relationships:** Used by various classes to handle JSON operations.

### CsvUtils

- **Functionality:** Provides utility methods for writing purchase history and product information to CSV files.
- **Testing:** JUnit tests cover methods related to writing purchase history and product information to CSV files.
- **Relationships:** Used by the `Marketplace` class to export data to CSV.

### Marketplace

- **Functionality:** Represents the main marketplace, handling interactions between buyers, sellers, and items. Provides methods for importing and exporting data, managing transactions, and updating stock.
- **Testing:** JUnit tests cover methods related to managing transactions, importing/exporting data, and updating stock.
- **Relationships:** Uses various other classes to implement the marketplace functionality.


