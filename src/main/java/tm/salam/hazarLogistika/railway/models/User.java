package tm.salam.hazarLogistika.railway.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name")
    @NotEmpty(message = "name user's is empty")
    @NotBlank(message = "name user's is mandatory")
    private String name;
    @Column(name = "surname")
    @NotEmpty(message = "surname user's is empty")
    @NotBlank(message = "surname user's is mandatory")
    private String surname;
    @Column(name = "email")
    @Email(message = "email user's is invalid")
    @NotBlank(message = "email user's is mandatory")
    @NotEmpty(message = "email user's is empty")
    private String email;
    @Column(name = "password")
    @NotBlank(message = "password user's is mandatory")
    @NotEmpty(message = "password user's is empty")
    private String password;
    @Column(name = "image_path")
    private String imagePath;
    @Column(name = "status")
    private Boolean status;
    @Column(name = "verification_code")
    private String verificationCode;
    @CreationTimestamp
    @Column(name = "created")
    private Date created;
    @UpdateTimestamp
    @Column(name = "updated")
    private Date updated;
    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.REFRESH)
    @JoinTable(
            name = "roles_users",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
    )
    private List<Role> roles;
}
