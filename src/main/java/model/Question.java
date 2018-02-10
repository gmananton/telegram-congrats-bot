package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by Anton Mikhaylov on 10.02.2018.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Question {
    private String text;
    private List<Answer> answers;
}