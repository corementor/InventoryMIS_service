package io.corementor.finexp.base;


import io.corementor.finexp.inventory.common.util.ESequenceType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import psychemesh.framework.domain.AbstractBaseEntity;


import java.time.LocalDateTime;

/**
 * The Class SequenceNumber.
 *
 * @author Blaise Mugisha.
 * @version 1.0
 */

@Getter
@Setter
@Entity
@Table(name = "sequence_number" , schema = "public")
public class SequenceNumber extends AbstractBaseEntity {

    /** The cert id. */
    @Column(nullable = false)
    private Long number;
    /** The type. */
    @Enumerated(EnumType.STRING)
    private ESequenceType type;

    /** The created by . */
    @CreatedBy
    private String createdBy;

    /** The created at. */
    @CreatedDate
    private LocalDateTime createdAt = LocalDateTime.now();

    /** The modified by. */
    @LastModifiedBy
    private String modifiedBy;

    /** The modified at. */
    @LastModifiedDate
    private LocalDateTime modifiedAt;

}
