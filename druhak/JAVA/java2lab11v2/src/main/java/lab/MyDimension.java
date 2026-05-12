package lab;


import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MyDimension implements Serializable {

    @Serial
    private static final long serialVersionUID = 339862291821958968L;

    private double width;
    private double height;

}
