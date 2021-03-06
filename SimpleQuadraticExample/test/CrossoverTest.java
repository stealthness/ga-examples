import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CrossoverTest {

    private static final String TESTCASE_FILENAME =  "testcases//crossoverTestCases.txt";
    List<String> expChildrenList;
    List<String> parentList;

    @Test
    void testCrossoverOnSimpleTrees(){
        Node parentNode0 = TestUtils.onePlusX;
        Node parentNode1 = TestUtils.xNode;

        Node parentNode2= TestUtils.xPlusTwo;
        Node parentNode3 = TestUtils.oneNode;

        Node child0 = GPUtils.mutateIndex1.apply(Arrays.asList(parentNode0,parentNode1),1.0).clone();
        Node child1 = GPUtils.mutateIndex1.apply(Arrays.asList(parentNode2,parentNode3),1.0).clone();

        TestUtils.assertNode(TestUtils.xPlusX,child0);
        TestUtils.assertNode(TestUtils.onePlusTwo,child1);
    }

    // crossoverTestCases.txt

    @Test
    void testSimpleCrossoverATIndex1(){
        // on (+ 1.0 2.0),(- x0 1.0)
        testTestCase("testCase001"); // 1,1
        testTestCase("testCase002"); // 2,2
        testTestCase("testCase003"); // 1,2
        testTestCase("testCase004"); // 2,1
    }
    @Test
    void testSimpleCrossoverATIndex2(){

        // on (* 1.0 (* x0 2.0)),(+ x0 3.0)
        testTestCase("testCase005"); // 1,1
        testTestCase("testCase006"); // 2,2
        testTestCase("testCase007"); // 3,1
        testTestCase("testCase008"); // 3,2
    }

    @Test
    void testSimpleCrossoverATIndex3(){
        // on (+ 1.0 (* x0 2.0),(- (+ 2.0 (+ x0 1.0)) 1.0)
        testTestCase("testCase030"); // 3,3
        testTestCase("testCase031"); // 1,4
        testTestCase("testCase032"); // 4,1
    }

    @Test
    void testGenerateCrossover(){
        IntStream.range(0,5).forEach(i ->{

            List<Node> terminalList = Arrays.asList(new TerminalNode(1.0),new TerminalNode(0.0),new TerminalNode(2.0));
            List<GPFunction> functionList = Arrays.asList(new GPBiFunction(GPUtils.add,"+"),
                    new GPBiFunction(GPUtils.multiply,"*"),//new GPBiFunction(GPUtils.abs,"abs"),
                    new GPBiFunction(GPUtils.subtract,"-"),new GPBiFunction(GPUtils.divide,"/"));

            Node parent1 = TestUtils.generateNodeAtDepth(3,terminalList,functionList);//new Random().nextInt(3)
            Node parent2 = TestUtils.generateNodeAtDepth(new Random().nextInt(3),terminalList,functionList);

            List<Node> parents = Arrays.asList(parent1,parent2);
            System.out.println("parents");
            parents.forEach(p -> System.out.println(p.toClojureString()));
            System.out.println("children");
            List<Node> children = NodeUtils.crossoverNodes(parents,1.0);
            children.forEach(c -> System.out.println(c.toClojureString()));
        });

    }


    private void testCrossOverAt(List<String> expChildrenList, List<String> parentList, Integer[] indexes, BiFunction<List<String>,Integer[], List<Node>> function){
        for (int i = 0; i<parentList.size(); i =+2){
            List<Node> children = function.apply(parentList, indexes);
            TestUtils.assertNode(NodeUtils.createNodeFromString(expChildrenList.get(i)),children.get(0));
            TestUtils.assertNode(NodeUtils.createNodeFromString(expChildrenList.get(i+1)),children.get(1));
        }
    }

    void testTestCase(String testCase){
        List<String> testCaseStrings = TestUtils.getTestCase(testCase,TESTCASE_FILENAME, Optional.of(3));
        assertEquals(3,testCaseStrings.size());
        var strings = Arrays.asList(testCaseStrings.get(0).split(","));
        parentList = Arrays.asList(testCaseStrings.get(1).split(","));
        expChildrenList = Arrays.asList(testCaseStrings.get(2).split(","));
        testCrossOverAt(expChildrenList,parentList,new Integer[]{Integer.valueOf(strings.get(2)),Integer.valueOf(strings.get(3))},GPUtils.crossoverAt);
    }

}
