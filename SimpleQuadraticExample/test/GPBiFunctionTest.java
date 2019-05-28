import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GPBiFunctionTest {

    @Test
    void testAddCalculateAddWith2TerminalNodes(){
        Node addNode = new FunctionNode(new GPBiFunction(GPUtils.addBiFunction,"+"), Arrays.asList(TestUtils.oneNode,TestUtils.oneNode));
        assertFunctionNode(Optional.of(2.0),Optional.of(3),Optional.of(1),Optional.of("(+ 1.0 1.0)"),Optional.empty(), addNode);
    }

    @Test
    void testAddCalculateWith1TerminalNodeAnd1VariableNode(){
        Node addNode = new FunctionNode(new GPBiFunction(GPUtils.addBiFunction,"+"), Arrays.asList(TestUtils.oneNode,TestUtils.xNode));
        assertFunctionNode(Optional.of(2.0),Optional.of(3),Optional.of(1),Optional.of("(+ 1.0 x0)"),Optional.of(new Double[]{1.0}), addNode);
    }

    @Test
    void testMultipleCalculateWith2TerminalNodes(){
        Node multipleNode = new FunctionNode(new GPBiFunction(GPUtils.multipleBiFunction,"+"), Arrays.asList(TestUtils.oneNode,TestUtils.twoNode));
        assertFunctionNode(Optional.of(2.0),Optional.of(3),Optional.of(1),Optional.of("(+ 1.0 2.0)"),Optional.empty(), multipleNode);
    }

    @Test
    void testMultipleCalculateWith1TerminalNodeAnd1VariableNode(){
        Node multipleNode = new FunctionNode(new GPBiFunction(GPUtils.multipleBiFunction,"*"), Arrays.asList(TestUtils.oneNode,TestUtils.xNode));
        assertFunctionNode(Optional.of(2.0),Optional.of(3),Optional.of(1),Optional.of("(* 1.0 x0)"),Optional.of(new Double[]{2.0}), multipleNode);
    }

    @Test
    void testCalculateWith2VariableNodes(){
        Node addNode = new FunctionNode(new GPBiFunction(GPUtils.multipleBiFunction,"*"), Arrays.asList(TestUtils.xNode,TestUtils.xNode));
        assertFunctionNode(Optional.of(4.0),Optional.of(3),Optional.of(1),Optional.of("(* x0 x0)"),Optional.of(new Double[]{2.0}), addNode);
    }


    @Test
    void testAdd(){
        testCalculate(GPUtils.subtractBiFunction,"+");
    }

    private void testCalculate(BiFunction<Double[], List<Node>, Double> function, String clojureString){
        Node testNode;

        // with 2 TerminalNodes
        testNode = new FunctionNode(new GPBiFunction(function,clojureString),Arrays.asList(TestUtils.oneNode,TestUtils.twoNode));
        assertFunctionNode(Optional.of(-1.0),Optional.of(3),Optional.of(1),Optional.of(String.format("(%s 1.0 2.0)",clojureString)),Optional.empty(), testNode);
        // with 1 VariableNode, 1 TerminalNode
        testNode = new FunctionNode(new GPBiFunction(function,clojureString),Arrays.asList(TestUtils.xNode,TestUtils.twoNode));
        assertFunctionNode(Optional.of(-1.0),Optional.of(3),Optional.of(1),Optional.of(String.format("(%s x0 2.0)",clojureString)),Optional.empty(), testNode);

        testNode = new FunctionNode(new GPBiFunction(function,clojureString),Arrays.asList(TestUtils.twoNode,TestUtils.xNode));

        // with 2 VariableNode
        testNode = new FunctionNode(new GPBiFunction(function,clojureString),Arrays.asList(TestUtils.xNode,TestUtils.xNode));
    }

    // helper method

    private void assertFunctionNode(Optional<Double> expValue, Optional<Integer> expSize, Optional<Integer>  expDepth, Optional<String>  expClojureString, Optional<Double[]> inputs, Node actNode){
        assertEquals(FunctionNode.class,actNode.getClass());
        assertEquals(expSize.get(), actNode.size());
        assertEquals(expDepth.get(),actNode.getDepth());
        assertEquals(expClojureString.get(),actNode.print());
        assertEquals(expValue.get(),actNode.calculate(inputs.orElse(null)));
    }

}