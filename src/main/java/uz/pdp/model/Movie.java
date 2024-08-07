package uz.pdp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Movie {
    private int id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int categoryId;
    private String posterImage;
    private LocalDate createDate;
}
