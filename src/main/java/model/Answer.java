package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Anton Mikhaylov on 10.02.2018.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Answer {
    private String text;
    private boolean correct;
}
