import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.*;

class NodeTest {

    private static final double TOL = 0.000001;
    private BiFunction<Double,Double,Double> add = Double::sum;
    private BiFunction<Double,Double,Double> subtract = (a, b)-> a-b;
    private BiFunction<Double,Double,Double> multiply = (a, b)-> a*b;
    private BiFunction<Double,Double,Double> protectedDivision = (a, b)-> (b==0)?1.0:a/b;
    private double v0;
    private double v1;

    private Node t0;
    private Node t1;

    private VariableNode x0;
    private VariableNode x1;


    @BeforeEach
    void SetUp(){
        v0 = 1.2;
        v1 = -2.8;

        t0 = new TerminalNode(v0);
        t1 = new TerminalNode(v1);

        x0 = new VariableNode(0);
        x1 = new VariableNode(1);

    }

    @Test
    void getValueIfTerminal() {
        assertEquals(v0, t0.get(null),TOL);
        assertEquals(v1, t1.get(null),TOL);
    }

    @Test
    void printTerminal() {
        assertEquals(String.valueOf(v0), t0.print());
        assertEquals(String.valueOf(v1), t1.print());
    }


    @Test
    void simpleAddLambdaTest(){
        assertEquals(v0+v1, add.apply(v0,v1),TOL);
    }

    @Test
    void simpleSubtractLambdaTest(){
        assertEquals(v0-v1, subtract.apply(v0,v1),TOL);
    }

    @Test
    void simpleMultiplayLambdaTest(){
        assertEquals(v0*v1, multiply.apply(v0,v1),TOL);
    }

    @Test
    void simpleProtectedDivisionLambdaTest(){
        assertEquals(1.0, protectedDivision.apply(3.3,0.0),TOL);
        v1 = 0.0;
        assertEquals(1.2, protectedDivision.apply(3.6,3.0),TOL);
    }


    @Test
    void testNodeWithLambdaAndTwoTerminalNode(){
        var f0 = new FunctionNode(add,"+",t0,t1);
        assertEquals(v0+v1,f0.get(null),TOL);
    }

    @Test
    void testPrintNodeWithLambdaAndTwoTerminalNode(){
        var f0 = new FunctionNode(add,"+",t0,t1);
        var expStr = "( " + String.valueOf(v0) + " "+ f0.getFunctionString() + " " +String.valueOf(v1) + " )";
        assertEquals(expStr,f0.print());
        assertTrue(expStr.equals(f0.print()));
    }

    @Test
    void testNodeThatIsVariableNode(){
        var variables = new double[]{v0,v1};
        assertEquals(v0,x0.get(variables),TOL);
        assertEquals(v1,x1.get(variables),TOL);
    }

    @Test
    void testPrintVariableNode(){
        assertEquals("x0",x0.print());
        assertEquals("x1",x1.print());
    }

    @Test
    void testPrintAddFunctionNode(){
        var f0 = new FunctionNode(add,"+");
        assertEquals("( null + null )",f0.print());
    }

}