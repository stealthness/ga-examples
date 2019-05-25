
import lombok.Data;

import java.util.List;
import java.util.function.BiFunction;

@Data
public class GPSingleFunction implements GPFunction{


    private static final int MAX_NUMBER_SUB_NODES = 10;
    private BiFunction<Double[], List<Node>, Double> function;


    GPSingleFunction(BiFunction<Double[], List<Node>, Double> function){
        this.function = function;
    }


    @Override
    public Double apply(Double[] inputs, List<Node> nodes) {
        return function.apply(inputs,nodes);
    }

    @Override
    public int getMaxSubNodes() {
        return MAX_NUMBER_SUB_NODES;
    }
}
