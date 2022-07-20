package tm.salam.hazarLogistika.railway.models;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;

@lombok.Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "excel_files")
public class ExcelFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name")
    @NotEmpty(message = "name excel file is empty")
    @NotBlank(message = "name excel file is mandatory")
    private String name;
    @Column(name = "path")
    @NotEmpty(message = "path excel file is empty")
    @NotBlank(message = "path excel file is mandatory")
    private String path;
    @CreationTimestamp
    private Date created;
    @Column(name = "updated")
    @UpdateTimestamp
    private Date updated;
    @OneToMany(mappedBy = "excelFile",cascade = CascadeType.ALL)
    private List<Data>dataList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "data_fixing_id")
    private DataFixing dataFixing;
    @OneToMany(mappedBy = "excelFile", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Van>vans;
}
