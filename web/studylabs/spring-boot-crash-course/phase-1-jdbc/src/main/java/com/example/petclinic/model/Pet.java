ackage com.example.petclinic.model;

import org.springframework.data.annotation.Id;

/**
 * Pet representation
 */
public class Pet {
    @Id
    private Long id;
    private String name;
    private String type;

    public Pet() {}

    public Pet(Long id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
