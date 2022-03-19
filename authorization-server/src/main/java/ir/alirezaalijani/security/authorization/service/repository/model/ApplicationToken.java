package ir.alirezaalijani.security.authorization.service.repository.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@Entity
@Table(name = "application_token")
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationToken {
    @Id
    private String id;
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateAt;
    private Date useTime;
    @Lob
    private String token;
    private String type;
    private Boolean expired;
    private String username;
}
