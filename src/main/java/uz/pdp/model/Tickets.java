package uz.pdp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tickets {
    private int id;
    private int userId;
    private int showtimeId;
    private Timestamp time;
    private double price;
    private int seatNumber;
}
