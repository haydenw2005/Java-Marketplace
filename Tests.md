### Test 1: User Authentication

**Steps:**

1. Launch the application.
2. Select "Sign in" option.
3. Enter valid username and password.

**Expected Result:**

- Application should log in the user and display their homepage.

**Test Status:** Passed.

### Test 2: User Registration

**Steps:**

1. Launch the application.
2. Select "Sign up" option.
3. Enter valid registration details.

**Expected Result:**

- Application should create a new user account and welcome the user.

**Test Status:** Passed.

### Test 3: Create Item

**Preconditions:**

- User is logged in as a seller.

**Steps:**

1. Navigate to the seller's store.
2. Choose "Create Item" option.
3. Enter valid item details.

**Expected Result:**

- The item should be created and added to the seller's store.

**Test Status:** Passed.

### Test 4: Search Products

**Preconditions:**

- User is logged in as buyer.

**Steps:**

1. Navigate to the Marketplace by selecting ViewMarketplace.
2. Enter a valid search query in the search bar in the bottom.
3. Select Search

**Expected Result:**

- Application should display relevant search results.

**Test Status:** Passed.

### Test 5: Sort Items by Price

**Preconditions:**

- User is buyer and is viewing the markplace.

**Steps:**

1. Choose the "Sort by Price" option.

**Expected Result:**

- Items should be displayed in descending order of price.

**Test Status:** Passed.

### Test 6: Edit User Profile

**Preconditions:**

- User is logged in.

**Steps:**

1. Select option to edit account from home page.
2. Enter the required fields.

**Expected Result:**

- User details should be successfully updated.

**Test Status:** Passed.

### Test 7: Delete User Account

**Preconditions:**

- User is logged in.

**Steps:**

1. Choose the "Delete Account" option in the home page.

**Expected Result:**

- User account should be deleted successfully.

**Test Status:** Passed.

Feel free to adjust these test cases based on the specific functionalities and scenarios you want to cover.

### Test 8: View All Marketplace Items

**Preconditions:**

- User is logged in.

**Steps:**

1. Navigate to the "All Marketplace Items" section.

**Expected Result:**

- Application should display a list of all items available in the marketplace.

**Test Status:** Passed.

### Test 9: View All Stores

**Preconditions:**

- User is logged in as Seller.

**Steps:**

1. Choose the "View All Stores" option.
2. Select the store to view from the dropdown.

**Expected Result:**

- Application should display a list of all stores in the marketplace.

**Test Status:** Passed.

### Test 10: View Products Sold by Stores

**Preconditions:**

- User is logged in as Seller.

**Steps:**

1. Navigate to the "Products Sold by Stores" section.
2. Select the store to view from the dropdown.

**Expected Result:**

- Application should display a list of stores sorted by the number of products sold.

**Test Status:** Passed.

### Test 11: View Products Bought by Stores

**Preconditions:**

- User is logged in as Seller.

**Steps:**

1. Navigate to the "Products Bought by Stores" section.
2. Select the store to view from the dropdown.

**Expected Result:**

- Application should display a list of stores sorted by the number of products bought.

**Test Status:** Passed.

### Test 12: Sort Stores by Products Sold

**Preconditions:**

- Buyer is viewing a list of stores in marketplace.

**Steps:**

1. Choose the "Sort by Products Sold" option.

**Expected Result:**

- Stores should be displayed in descending order of products sold.

**Test Status:** Passed.

### Test 13: Sort Stores by Products Bought

**Preconditions:**

- Buyer is viewing a list of stores in marketplace.

**Steps:**

1. Choose the "Sort by Products Bought" option.

**Expected Result:**

- Stores should be displayed in descending order of products bought.

**Test Status:** Passed.

### Test 14: Purchase Item

**Preconditions:**

- User is logged in as a buyer.
- User has selected a specific item for purchase from the marketplace.

**Steps:**

1. Choose the "Purchase" option for a selected item.

**Expected Result:**

- The user should be able to successfully purchase the item.

**Test Status:** Passed.

### Test 15: Add Item To cart

**Preconditions:**

- User is logged in as a buyer.
- User has selected a specific item for purchase from the marketplace.

**Steps:**

1. Choose the "Add to Cart" option for a selected item.
2. Navigate back to the homepage
3. Select view cart.

**Expected Result:**

- The user should be able to successfully add the item to cart, and view it.

**Test Status:** Passed.

### Test 16: Buy cart

**Preconditions:**

- User is logged in as a buyer.
- User has selected items in their cart from the marketplace.

**Steps:**

1. Select the "View Cart" option from the homepage.
2. Select "Buy cart"

**Expected Result:**

- The user should be able to successfully buy all items in their cart.

**Test Status:** Passed.

### Test 17: Export Purchase History

**Preconditions:**

- User is logged in as a buyer.
- User has purchased items from the marketplace

**Steps:**

1. Select the "Export Purchase HIstory to CSV" option from the homepage.
2. Enter the filename to write to, and select ok.

**Expected Result:**

- The user should be able to see a csv file with their purchase history.

**Test Status:** Passed.


### Test 18: Export Store Items 

**Preconditions:**

- User is logged in as a seller.
- User has created items in the marketplace.

**Steps:**

1. Select the "Export Store Items to CSV" option from the homepage.
2. Enter the filename to write to, and select ok.

**Expected Result:**

- The user should be able to see a csv file with their Store Items.

**Test Status:** Passed.

### Test 19: Import Store Items 

**Preconditions:**

- User is logged in as a seller.
- User has a csv file with their items in the correct format to import.

**Steps:**

1. Select the "Import Store Items from CSV" option from the homepage.
2. Enter the name of the csv file to import from and select ok.

**Expected Result:**

- The user should now have their items in the marketplace, and updated in the network.

**Test Status:** Passed.

### Test 20: View Store Sales

**Preconditions:**

- User is logged in as a seller.

**Steps:**

1. Navigate to the seller's dashboard.
2. Click on the "View Store Sales" button.
3. Select the store from the dropdown.

**Expected Result:**

- Application should display a list of sales for the selected store.

**Test Status:** Passed.

### Test 21: View Listed Products

**Preconditions:**

- User is logged in as a seller.

**Steps:**

1. Navigate to the seller's dashboard.
2. Click on the "View Listed Products" button.


**Expected Result:**

- Application should display a list of all products listed in the seller's stores.

**Test Status:** Passed.

### Test 22: View Sold Products

**Preconditions:**

- User is logged in as a seller.

**Steps:**

1. Navigate to the seller's dashboard.
2. Click on the "View Sold Products" button.

**Expected Result:**

- Application should display a list of all products sold in the seller's stores.

**Test Status:** Passed.

### Test 23: View Product Buyers

**Preconditions:**

- User is logged in as a seller.

**Steps:**

1. Navigate to the seller's dashboard.
2. Click on the "View Product Buyers" button for a specific product.

**Expected Result:**

- Application should display a list of buyers who purchased the selected product.

**Test Status:** Passed.

### Test 24: Restock Item

**Preconditions:**

- User is logged in as a seller.
- User has a specific item that needs restocking.

**Steps:**

1. Navigate to the seller's dashboard.
2. Choose a specific store and select the "View Listed Products" button.
3. Identify an item with low stock.
4. Click on the "Restock" button for the identified item.
5. Enter a valid quantity for restocking.

**Expected Result:**

- The item's stock should be successfully replenished with the specified quantity.

**Test Status:** Passed.

### Test 25: Edit Store

**Preconditions:**

- User is logged in as a seller.
- User has one or more stores.

**Steps:**

1. Navigate to the seller's dashboard.
2. Choose the "View All Stores" option.
3. Select the "Edit" button for a specific store.
4. Modify the store details (e.g., name, description).

**Expected Result:**

- The store details should be successfully updated with the new information.

**Test Status:** Passed.