# book_catalog

Here’s a **detailed README** for your **SmartBookCatalog** project. It covers **project structure, configuration, and implementation details** for Spring Boot, MyBatis, and Web Services. 🚀  

---

# 📚 SmartBookCatalog  
An online book catalog system built with **Spring Boot**, **MyBatis**, **Spring Security**, and **Thymeleaf**.

## 🏗️ Project Structure  

```
SmartBookCatalog/
│── src/
│   ├── main/
│   │   ├── java/com/example/smartbookcatalog/
│   │   │   ├── controller/         # Controllers (Admin, User, Authentication, Home)
│   │   │   ├── service/            # Business logic layer
│   │   │   ├── repository/         # MyBatis mappers (data access layer)
│   │   │   ├── model/              # Entity classes (Book, User, FavoriteBook)
│   │   │   ├── config/             # Security and database configuration
│   │   ├── resources/
│   │   │   ├── templates/          # Thymeleaf HTML files
│   │   │   ├── static/             # CSS, JS, Images
│   │   │   ├── mappers/            # MyBatis mapper XMLs
│   │   │   ├── application.properties  # Spring Boot configuration
│── pom.xml
│── README.md
```

---

## 🔧 **Configuration Choices**
### **1️⃣ Spring Boot**
- **Used Spring Boot’s Auto-Configuration**: No need for manual `Bean` definitions.
- **Spring Security**: Configured **role-based access** for **Admin** & **Users**.
- **Spring MVC**: Controllers use `@RestController` for API endpoints and `@Controller` for Thymeleaf views.

### **2️⃣ MyBatis Configuration**
- **MyBatis Mapper XMLs** are stored in `src/main/resources/mappers/` for SQL queries.
- Uses **Mapper Interfaces** instead of JPA `@Repository`.

### **3️⃣ Database Connection**
Configured in `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/smartbookcatalog
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.datasource.hikari.maximum-pool-size=10
```

---

## 🌍 **Features**
### **✅ Admin Panel**
- **Add, Edit, Delete Books** 📚
- **Search Books** 🔎
- **Manage Users** 👤

### **✅ User Dashboard**
- **View Books** 📖
- **Search Books** 🔎
- **Add to Favorites** ⭐
- **Personalized Book List** 📚

### **✅ Authentication & Security**
- **Role-based Access** (`Admin`, `User`)
- **Custom Login & Registration Pages**
- **BCrypt Password Hashing**

---

## 🚀 **Implementation Details**
### **1️⃣ Controllers**
#### **📌 `HomeController.java`**
Handles the **homepage** displaying all books.

```java
@Controller
public class HomeController {
    private final BookService bookService;

    public HomeController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/")
    public String homePage(Model model) {
        List<Book> books = bookService.getAllBooks();
        model.addAttribute("books", books);
        return "index"; // Loads index.html
    }
}
```

#### **📌 `AdminController.java`**
Handles **admin operations**: adding, editing, deleting books.

```java
@Controller
@RequestMapping("/admin")
public class AdminController {
    private final BookService bookService;

    public AdminController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public String adminDashboard(Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        return "admin-dashboard";
    }

    @PostMapping("/add")
    public String addBook(@RequestParam String title, @RequestParam String author,
                          @RequestParam String isbn, @RequestParam int publishedYear) {
        bookService.addBook(new Book(null, title, author, isbn, publishedYear));
        return "redirect:/admin";
    }
}
```

---

### **2️⃣ Services**
#### **📌 `BookService.java`**
Handles **business logic** for books.

```java
@Service
public class BookService {
    private final BookMapper bookMapper;

    public BookService(BookMapper bookMapper) {
        this.bookMapper = bookMapper;
    }

    public List<Book> getAllBooks() {
        return bookMapper.getAllBooks();
    }

    public void addBook(Book book) {
        bookMapper.insertBook(book);
    }
}
```

---

### **3️⃣ Repositories (MyBatis)**
#### **📌 `BookMapper.java`**
```java
@Mapper
public interface BookMapper {
    @Select("SELECT * FROM books")
    List<Book> getAllBooks();

    @Insert("INSERT INTO books (title, author, isbn, published_year) VALUES (#{title}, #{author}, #{isbn}, #{publishedYear})")
    void insertBook(Book book);
}
```

---

### **4️⃣ Security**
#### **📌 `SecurityConfig.java`**
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/user/**").hasRole("USER")
                .requestMatchers("/").permitAll()
            )
            .formLogin(login -> login
                .loginPage("/auth/login")
                .defaultSuccessUrl("/", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .permitAll()
            );
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return username -> userRepository.findByUsername(username)
            .map(user -> User.withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole())
                .build())
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
```

---

### **5️⃣ Web Services (External API Calls)**
#### **📌 `GoogleBooksService.java`**
Fetches books **from Google Books API** using ISBN.

```java
@Service
public class GoogleBooksService {
    private final RestTemplate restTemplate;

    public GoogleBooksService(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public String fetchBookDetails(String isbn) {
        String url = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn;
        return restTemplate.getForObject(url, String.class);
    }
}
```

---

## 🛠️ **Running the Project**
### **1️⃣ Install Dependencies**
```sh
mvn clean install
```

### **2️⃣ Run Application**
```sh
mvn spring-boot:run
```

### **3️⃣ Open in Browser**
- **Home Page:** `http://localhost:8080/`
- **Admin Panel:** `http://localhost:8080/admin`
- **User Dashboard:** `http://localhost:8080/user/books`
- **Login Page:** `http://localhost:8080/auth/login`

---

## 💡 **Future Enhancements**
- ✅ Add **REST API** (`/api/books`, `/api/users`)
- ✅ Integrate **Google Books API** for fetching book details
- ✅ Implement **event-driven notifications**
- ✅ Add **pagination & sorting** for books
- ✅ Implement **unit & integration testing**

---

## 🤝 **Contributing**
Pull requests are welcome! 🎉 Please ensure your code follows best practices and is well-documented.

---

🚀 **Congratulations!** Now your **README** is **detailed, structured, and easy to understand!** 🎯
