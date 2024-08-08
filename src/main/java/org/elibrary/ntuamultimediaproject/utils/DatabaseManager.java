package org.elibrary.ntuamultimediaproject.utils;

import javafx.scene.control.Alert;
import org.elibrary.ntuamultimediaproject.models.*;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Utility class for managing the eLibrary database including
 * users, books, borrowed books, ratings, and categories.
 */
public class DatabaseManager {

    private static final String USER_FILE_PATH = "medialab/users.ser";
    private static final String BOOK_FILE_PATH = "medialab/books.ser";
    private static final String BORROWED_BOOK_FILE_PATH = "medialab/borrowedBooks.ser";
    private static final String RATING_BOOK_FILE_PATH = "medialab/ratings.ser";
    private static final String CATEGORY_FILE_PATH = "medialab/categories.ser";

    private static List<User> users = new ArrayList<>();
    private static List<Book> books = new ArrayList<>();
    private static List<BorrowBook> borrowedBooks = new ArrayList<>();
    private static List<Rating> ratings = new ArrayList<>();
    private static List<Category> categories = new ArrayList<>();

    private static String loggedInUsername;

    public static void deserializeDataOnStart() {
        users = loadUsers();
        books = loadBooks();
        borrowedBooks = loadBorrowedBooks();
        ratings = loadRatings();
        categories = loadCategories();
        System.out.println("INFO: Data loaded.");
    }

    public static void serializeDataOnExit() {
//      addUser(new User("medialab", "medialab_2024"));
        saveUsers(users);
        saveBooks(books);
        saveBorrowedBooks(borrowedBooks);
        saveRatings(ratings);
        saveCategories(categories);
        System.out.println("INFO: Data saved.");
    }

    public static List<User> loadUsers() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USER_FILE_PATH))) {
            return (List<User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }

    public static void saveUsers(List<User> users) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USER_FILE_PATH))) {
            oos.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Book> loadBooks() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(BOOK_FILE_PATH))) {
            return (List<Book>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }

    public static void saveBooks(List<Book> books) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(BOOK_FILE_PATH))) {
            oos.writeObject(books);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<BorrowBook> loadBorrowedBooks() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(BORROWED_BOOK_FILE_PATH))) {
            return (List<BorrowBook>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }

    private static void saveBorrowedBooks(List<BorrowBook> borrowedBooks) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(BORROWED_BOOK_FILE_PATH))) {
            oos.writeObject(borrowedBooks);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Rating> loadRatings() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(RATING_BOOK_FILE_PATH))) {
            return (List<Rating>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }

    private static void saveRatings(List<Rating> ratings) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(RATING_BOOK_FILE_PATH))) {
            oos.writeObject(ratings);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Category> loadCategories() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(CATEGORY_FILE_PATH))) {
            return (List<Category>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }

    private static void saveCategories(List<Category> categories) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(CATEGORY_FILE_PATH))) {
            oos.writeObject(categories);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getLoggedInUsername() {
        return loggedInUsername;
    }

    public static void setLoggedInUsername(String username) {
        loggedInUsername = username;
    }

    /**
     *
     * Basic CRUD operations and Methods for User
     *
     */

    // Add user
    public static void addUser(User user) {
        users.add(user);
    }

    // Update user
    public static void updateUser(User updatedUser) {
        int index = findUserIndexByUsername(updatedUser.getUsername());
        if (index != -1) {
            users.set(index, updatedUser);
        } else {
            System.out.println("INFO: User not found.");
        }
    }

    // Delete user by ID
    public static void deleteUser(User user) {
        int index = findUserIndexByUsername(user.getUsername());
        if (index != -1) {
            removeBorrowsByUsername(user.getUsername());
            users.remove(index);
        } else {
            System.out.println("INFO: User not found.");
        }
    }

    // Helper method to find user index by ID
    private static int findUserIndexByUsername(String username) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(username)) {
                return i;
            }
        }
        return -1; // User not found
    }

    // Function to check if a user exists in the database
    public static boolean checkUserExists(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    // Function to check if the password matches for the existing user
    public static boolean checkPasswordMatches(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    // Helper method to find user by username
    private static User findUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    // Method to find if a user is an Admin user
    public static boolean isUserAdmin(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getAdmin()) {
                return true;
            }
        }
        return false;
    }

    // Method to return all registered users
    public static List<User> getUsers() {
        return users;
    }

    // Method to find if a username is already in use
    public static boolean isUsernameUnique(String username) {
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return false; // Username already exists
            }
        }
        return true; // Username is unique
    }

    // Method to find if an email address is already in use
    public static boolean isEmailUnique(String email) {
        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(email)) {
                return false; // Email already exists
            }
        }
        return true; // Email is unique
    }

    // Method to find if an ADT number is already in use
    public static boolean isAdtUnique(String adt) {
        for (User user : users) {
            if (user.getAdt().equalsIgnoreCase(adt)) {
                return false; // ADT already exists
            }
        }
        return true; // ADT is unique
    }

    /**
     *
     * Basic CRUD operations and Methods for Book
     *
     */

    // Add Book
    public static void addBook(Book book) {
        books.add(book);
    }

    // Update Book by ISBN
    public static void updateBook(Book updatedBook) {
        int index = findBookIndexByISBN(updatedBook.getIsbn());
        if (index != -1) {
            books.set(index, updatedBook);
        } else {
            System.out.println("INFO: Book not found.");
        }
    }

    // Delete Book by ISBN
    public static void deleteBook(Book book) {
        int index = findBookIndexByISBN(book.getIsbn());
        if (index != -1) {
            removeBorrowsByBookISBN(book.getIsbn());
            books.remove(index);
        } else {
            System.out.println("INFO: Book not found.");
        }
    }

    // Helper method to find book index by ISBN
    private static int findBookIndexByISBN(String isbn) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getIsbn().equals(isbn)) {
                return i;
            }
        }
        return -1; // Book not found
    }

    // Method to return all books
    public static List<Book> getBooks() {
        return books;
    }

    // Method to return the total number of books
    public static int getNumberOfBooks() {
        return books.size();
    }

    // Get the top-rated books
    public static List<Book> getTopRatedBooks(int count) {
        books.sort(Comparator.comparing(Book::getRating).reversed());

        List<Book> topRatedBooks = new ArrayList<>();
        for (int i = 0; i < Math.min(count, books.size()); i++) {
            topRatedBooks.add(books.get(i));
        }
        return topRatedBooks;
    }

    // Search books based on title, writer, and year of publish (any combination)
    public static List<Book> searchBooks(String title, String writer, String yearOfPublish) {
        List<Book> searchResults = new ArrayList<>();

        for (Book book : books) {
            boolean matchesTitle = title.isEmpty() || book.getTitle().toLowerCase().contains(title.toLowerCase());
            boolean matchesWriter = writer.isEmpty() || book.getWriter().toLowerCase().contains(writer.toLowerCase());
            boolean matchesYearOfPublish = yearOfPublish.isEmpty() || String.valueOf(book.getYear()).equals(yearOfPublish);

            boolean atLeastOneFieldNonEmpty = !title.isEmpty() || !writer.isEmpty() || !yearOfPublish.isEmpty();

            if (atLeastOneFieldNonEmpty && matchesTitle && matchesWriter && matchesYearOfPublish) {
                searchResults.add(book);
            }
        }

        return searchResults;
    }

    // Find book by ISBN
    public static Book findBookByISBN(String isbn) {
        for (Book book : books) {
            if (book.getIsbn().equals(isbn)) {
                return book;
            }
        }
        return null; // Book not found
    }

    // Method to check if ISBN is already in use
    public static boolean isISBNUnique(String isbn) {
        for (Book book : books) {
            if (book.getIsbn().equalsIgnoreCase(isbn)) {
                return false; // ISBN already exists
            }
        }
        return true; // ISBN is unique
    }

    /**
     *
     * Basic CRUD operations and Methods for Category
     *
     */

    // Add Category
    public static void addCategory(Category category) {
        categories.add(category);
    }

    // Delete Category by its Name
    public static void deleteCategory(String categoryName) {
        int index = findCategoryIndexByName(categoryName);
        if (index != -1) {
            removeBooksByCategory(categoryName);
            categories.remove(index);
        } else {
            System.out.println("INFO: Category not found.");
        }
    }

    // Helper method to find Category index by Name
    private static int findCategoryIndexByName(String categoryName) {
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).getCategoryName().equals(categoryName)) {
                return i;
            }
        }
        return -1;
    }

    // Method to return all categories
    public static List<String> getCategories() {
        List<String> categoriesInString = new ArrayList<>();
        for (Category category : categories) {
            categoriesInString.add(category.getCategoryName());
        }
        return categoriesInString;
    }

    // Method to check if category name already exists
    public static Category categoryExists(String categoryName) {
        for (Category category : categories) {
            if(category.getCategoryName().equalsIgnoreCase(categoryName)) {
                return category;
            }
        }
        return null;
    }

    // Add category after checking uniqueness
    public static void addNewCategory(String category) {
        if(categoryExists(category) == null) {
            addCategory(new Category(category));
        }
    }

    // Update category name
    public static void updateCategory(String oldCategory, String newCategory) {
        if (books != null) {
            for (Book book : books) {
                if (oldCategory.equals(book.getCategoryName())) {
                    book.setCategoryName(newCategory);
                }
            }
        }
        deleteCategory(oldCategory);
        addNewCategory(newCategory);
    }

    // Method to return all books inside a category
    public static List<Book> getBooksByCategory(String category) {
        List<Book> categorizedBooks = new ArrayList<>();
        for (Book book : books) {
            if (category.equals(book.getCategoryName())) {
                categorizedBooks.add(book);
            }
        }
        return categorizedBooks;
    }

    // Helper method to remove books by category
    private static void removeBooksByCategory(String categoryName) {
        List<Book> booksToRemove = new ArrayList<>();
        for (Book book : books) {
            if (book.getCategoryName().equals(categoryName)) {
                booksToRemove.add(book);
            }
        }
        books.removeAll(booksToRemove);

        for (Book removedBook : booksToRemove) {
            removeBorrowsByBookISBN(removedBook.getIsbn());
        }
    }

    // Method to check if category name is already in use
    public static boolean isCategoryNameUnique(String categoryName) {
        for (Category category : categories) {
            if (category.getCategoryName().equalsIgnoreCase(categoryName)) {
                return false; // Category name already exists
            }
        }
        return true; // Category name is unique
    }

    /**
     *
     * Basic CRUD operations and Methods for BorrowBook
     *
     */

    // Delete a borrow
    public static void deleteBorrow(String username, String bookISBN) {
        int index = findBorrowIndexByUserAndISBN(username, bookISBN);
        if (index != -1) {
            Book book = findBookByISBN(bookISBN);
            if (book != null) {
                book.setCopies(book.getCopies() + 1);
                updateBook(book);
            }
            borrowedBooks.remove(index);
        } else {
            System.out.println("INFO: Borrowed Book not found.");
        }
    }

    // Helper method to find borrow index by username and isbn
    private static int findBorrowIndexByUserAndISBN(String username, String bookISBN) {
        for (int i = 0; i < borrowedBooks.size(); i++) {
            if (borrowedBooks.get(i).getBookISBN().equals(bookISBN)
                    && borrowedBooks.get(i).getUsername().equals(username)) {
                return i;
            }
        }
        return -1;
    }

    // Delete all borrows by book isbn
    private static void removeBorrowsByBookISBN(String bookISBN) {
        List<BorrowBook> borrowsToRemove = new ArrayList<>();
        for (BorrowBook borrowBook : borrowedBooks) {
            if (borrowBook.getBookISBN().equals(bookISBN)) {
                borrowsToRemove.add(borrowBook);
            }
        }
        borrowedBooks.removeAll(borrowsToRemove);
    }

    // Delete all borrows by username
    private static void removeBorrowsByUsername(String username) {
        List<BorrowBook> borrowsToRemove = new ArrayList<>();
        for (BorrowBook borrow : borrowedBooks) {
            if (borrow.getUsername().equals(username)) {
                borrowsToRemove.add(borrow);
            }
        }
        for (BorrowBook borrowToRemove : borrowsToRemove) {
            Book book = findBookByISBN(borrowToRemove.getBookISBN());
            if (book != null) {
                book.setCopies(book.getCopies() + 1);
                updateBook(book);
            }
        }
        borrowedBooks.removeAll(borrowsToRemove);
    }

    // Borrow book
    public static void borrowBook(String username, String bookISBN) {
        User user = findUserByUsername(username);
        if (user == null) {
            System.out.println("INFO: User not found.");
            return;
        }

        if (getBorrowedBooksCountForUser(username) >= 2) {
            AlertManager.showAlert(Alert.AlertType.WARNING, "Borrow Limit Exceeded", "You have reached the maximum limit for borrowing books.");
            return;
        }

        Book book = findBookByISBN(bookISBN);
        if (book == null) {
            System.out.println("INFO: Book not found.");
            return;
        }

        if (book.getCopies() <= 0) {
            AlertManager.showAlert(Alert.AlertType.WARNING, "No Available Copies", "Sorry, the book is currently unavailable for borrowing.");
            return;
        }

        book.setCopies(book.getCopies() - 1);

        LocalDate borrowDate = LocalDate.now();
        LocalDate returnDate = borrowDate.plusDays(5);

        BorrowBook borrowBook = new BorrowBook(username, bookISBN, book.getTitle(), borrowDate, returnDate);
        borrowedBooks.add(borrowBook);
        updateBook(book);
        AlertManager.showAlert(Alert.AlertType.INFORMATION, "Borrow Confirmed", "Book borrowed successfully. Please return by " + returnDate.format(DateTimeFormatter.ofPattern("dd MMMM yyyy")));
    }

    // Get the number of borrowed books per user
    public static int getBorrowedBooksCountForUser(String username) {
        int count = 0;
        for (BorrowBook borrowBook : borrowedBooks) {
            if (borrowBook.getUsername().equals(username)) {
                count++;
            }
        }
        return count;
    }

    // Find all active borrowed books by username
    public static List<BorrowBook> findAllActiveBorrowedBooksByUsername(String username) {
        List<BorrowBook> activeBorrowedBooks = new ArrayList<>();
        LocalDate currentDate = LocalDate.now();

        for (BorrowBook borrowBook : borrowedBooks) {
            if (borrowBook.getUsername().equals(username) && currentDate.isBefore(borrowBook.getReturnDate())) {
                activeBorrowedBooks.add(borrowBook);
            }
        }

        return activeBorrowedBooks;
    }

    // Get all active borrows
    public static List<BorrowBook> getBorrowedBooks() {
        return borrowedBooks;
    }

    /**
     *
     * Basic CRUD operations and Methods for Rating
     *
     */

    // Add rating
    public static void rateBook(String username, String bookISBN, Integer rating, String comment) {
        Rating existingRating = findUserBookRating(username, bookISBN);

        if (existingRating != null) {
            existingRating.setRating(rating);
            existingRating.setComment(comment);
            AlertManager.showAlert(Alert.AlertType.INFORMATION, "Rating Updated", "Your rating has been updated successfully.");
        } else {
            Rating newRating = new Rating(username, bookISBN, rating, comment);
            ratings.add(newRating);
            AlertManager.showAlert(Alert.AlertType.INFORMATION, "Rating Submitted", "Your rating submitted successfully. Thank you!");
        }
        updateBookRating(findBookByISBN(bookISBN));
    }

    // Method to get a rating by username and isbn
    public static Rating findUserBookRating(String username, String bookISBN) {
        for (Rating rating : ratings) {
            if (rating.getUsername().equals(username) && rating.getBookISBN().equals(bookISBN)) {
                return rating;
            }
        }
        return null;
    }

    // Method to get all book's ratings by isbn
    public static List<Rating> getBookRatings(Book book) {
        List<Rating> bookRatings = new ArrayList<>();
        for(Rating rating : ratings) {
            if(rating.getBookISBN().equals(book.getIsbn())) {
                bookRatings.add(rating);
            }
        }
        return bookRatings;
    }

    // Update rating
    public static void updateBookRating(Book book) {
        int numOfRatings = 0;
        int sumRating = 0;
        for(Rating rating : ratings) {
            if(rating.getBookISBN().equals(book.getIsbn())) {
                numOfRatings++;
                sumRating = sumRating + rating.getRating();
            }
        }
        book.setRating((double) sumRating / numOfRatings);
        book.setNumRatings(numOfRatings);
    }

}
