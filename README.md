# Marketplace Project 
### Authors: Hayden White, Vishal Kattoju, Ryan Hirsh, and Soham Seksaria.
---
## Overview

The Marketplace project is a Java implementation of an online marketplace system. It consists of several classes representing different entities such as buyers, sellers, stores, items, and utilities for handling JSON operations.

## How to run
1. Download CS5_PROJECT dir
2. CD into CS5_PROJECT root dir
3. Compilation:
   - While in root directory, run the following command `javac -d bin -cp "jar_files/*" src/*.java`

4. Execution:
   - First run the now compiled Server.java with the command `java -cp bin:"jar_files/*" Server`
   - Then run the compiled Main.java with the command `java -cp bin:"jar_files/*" Main`

## Submissions

Hayden White - Submitted Report on Brightspace.

Hayden White - Submitted Vocareum workspace.

Hayden White - Submitted Presentation on Brightspace

## Testing
Detailed testing is listed in the `Tests.md` file.
 
## Classes

### Main
- **Functionality:** The entry point to the main Marketplace application and user flow. Connects to the Server and then directs to MarketplaceGUI.

### Marketplace

- **Functionality:** Represents the main marketplace, internally handling interactions between buyers, sellers, and items. Provides methods for importing and exporting data, managing transactions, and updating stock.
- **Relationships:** Uses various other classes to implement the marketplace functionality.

### MarketplaceGUI

- **Functionality:** Represents the GUI part of marketplace, allowing users to interact with the marketplace functions. It is where all user flow lives.
- **Relationships:** Called by the main method, uses Server, Marketplace, and various other classes to implement functionality.

### Server

- **Functionality:** Can be concurrently accessed over network to fetch, update, and manage data. Handles all client actions and stores updates in data.json. 
- **Relationships:** Uses JSON and various other classes to implement the buyer and seller functionality.
  
### Person

- **Functionality:** Represents a generic person with basic attributes like username, password, first name, last name, and email.
- **Relationships:** Extended by the `Buyer` and `Seller` classes.

### Buyer

- **Functionality:** Represents a buyer in the marketplace, extending the `Person` class. Includes features like a shopping cart, purchase history, and methods for buying items.
- **Relationships:** Has a composition relationship with the `Item` class and extends the `Person` class.

### Seller

- **Functionality:** Represents a seller in the marketplace, extending the `Person` class. Manages stores, including creating and editing stores, viewing store items, and tracking buyers.
- **Relationships:** Has a composition relationship with the `Store` class and extends the `Person` class.

### Store

- **Functionality:** Represents a store in the marketplace. Manages stock items, sold items, and provides methods to add items to stock.
- **Relationships:** Has a composition relationship with the `Item` class.

### Item

- **Functionality:** Represents an item available in the marketplace, with attributes like name, description, price, stock, and buyers. Also provides methods for handling buyers and stock.
- **Relationships:** Used by the `Store` and `Buyer` classes.

### JsonUtils

- **Functionality:** Provides utility methods for handling JSON operations, such as reading from and writing to JSON files, adding and removing objects from JSON, and checking for the existence of keys.
- **Relationships:** Used by various classes to handle JSON operations.

### CsvUtils

- **Functionality:** Provides utility methods for writing purchase history and product information to CSV files.
- **Relationships:** Used by the `Marketplace` class to export data to CSV.
- **Importing CSV files:** When importing CSV files, make sure the file follows the correct format. A sample format is shown in stock.csv.

