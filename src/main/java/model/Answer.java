package model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Anton Mikhaylov on 10.02.2018.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Answer {
    private String text;
//    private String picPath;
    private boolean correct;
}
