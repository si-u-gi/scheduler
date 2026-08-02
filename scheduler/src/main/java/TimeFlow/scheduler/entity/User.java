@Entity
@Table(name = "users")
public class User {

    @Id
    @
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String name;
    private String username;
    private String email;
    private String phone;
    private String password;
    private Character gender; // M, F
    private LocalDate birthDate;
}