package hello.mhipster.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

import hello.mhipster.domain.enumeration.WaterType;

/**
 * A Fish.
 */
@Entity
@Table(name = "fish")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Fish implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 3)
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Min(value = 0)
    @Column(name = "age", nullable = false)
    private Integer age;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "water_type", nullable = false)
    private WaterType waterType;

    @ManyToOne
    @JsonIgnoreProperties("fish")
    private School school;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Fish name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public Fish age(Integer age) {
        this.age = age;
        return this;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public WaterType getWaterType() {
        return waterType;
    }

    public Fish waterType(WaterType waterType) {
        this.waterType = waterType;
        return this;
    }

    public void setWaterType(WaterType waterType) {
        this.waterType = waterType;
    }

    public School getSchool() {
        return school;
    }

    public Fish school(School school) {
        this.school = school;
        return this;
    }

    public void setSchool(School school) {
        this.school = school;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Fish)) {
            return false;
        }
        return id != null && id.equals(((Fish) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Fish{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", age=" + getAge() +
            ", waterType='" + getWaterType() + "'" +
            "}";
    }
}
