package com.starwars;

import java.util.ArrayList;
import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Collectors;

enum Color {
    RED, GREEN
}

abstract class Tree {

    protected int value;
    protected Color color;
    protected int depth;

    public Tree(int value, Color color, int depth) {
        this.value = value;
        this.color = color;
        this.depth = depth;
    }

    public int getValue() {
        return value;
    }

    public Color getColor() {
        return color;
    }

    public int getDepth() {
        return depth;
    }

    public abstract void accept(TreeVis visitor);
}

class TreeNode extends Tree {

    private ArrayList<Tree> children = new ArrayList<>();

    public TreeNode(int value, Color color, int depth) {
        super(value, color, depth);
    }

    public void accept(TreeVis visitor) {
        visitor.visitNode(this);

        for (Tree child : children) {
            child.accept(visitor);
        }
    }

    public void addChild(Tree child) {
        children.add(child);
    }

    @Override
    public String toString() {
        return "TreeNode{" +
                "value=" + value +
                ", color=" + color +
                ", depth=" + depth +
                ", children.length=" + children.size() +
                '}';
    }

    public String printRoot() {
        return "Root{" +
                "value=" + value +
                ", color=" + color +
                ", depth=" + depth +
                ", children=" + Arrays.toString(children.toArray()) +
                '}';
    }
}

class TreeLeaf extends Tree {

    public TreeLeaf(int value, Color color, int depth) {
        super(value, color, depth);
    }

    public void accept(TreeVis visitor) {
        visitor.visitLeaf(this);
    }

    @Override
    public String toString() {
        return "TreeLeaf{" +
                "value=" + value +
                ", color=" + color +
                ", depth=" + depth +
                '}';
    }
}

abstract class TreeVis
{
    public abstract int getResult();
    public abstract void visitNode(TreeNode node);
    public abstract void visitLeaf(TreeLeaf leaf);

}

class SumInLeavesVisitor extends TreeVis {

    private Integer result = 0;
    public int getResult() {
        return result;
    }

    public void visitNode(TreeNode node) {
        //do nothing
    }

    public void visitLeaf(TreeLeaf leaf) {
        result += leaf.getValue();
    }
}

class ProductOfRedNodesVisitor extends TreeVis {

    private Integer result = 1;
    public int getResult() {
        return result;
    }

    public void visitNode(TreeNode node) {
        if (node.getColor() == Color.RED) {
            result = result * node.getValue();
        }
    }

    public void visitLeaf(TreeLeaf leaf) {
        if (leaf.getColor() == Color.RED) {
            result = result * leaf.getValue();
        }
    }
}

class FancyVisitor extends TreeVis {

    private Integer evenDeepNodesSum = 0;
    private Integer greenLeavesSum = 0;

    public int getResult() {
        int result = evenDeepNodesSum - greenLeavesSum;
        return result > 0 ? result : result * -1;
    }

    public void visitNode(TreeNode node) {
        if(node.getDepth() % 2 == 0)
            evenDeepNodesSum += node.getValue();
    }

    public void visitLeaf(TreeLeaf leaf) {
        if(leaf.getColor() == Color.GREEN)
            greenLeavesSum += leaf.getValue();
    }
}

class Tuple {
    private Integer father;
    private Integer son;

    Tuple(Integer father, Integer son) {
        this.father = father;
        this.son = son;
    }

    Integer getFather() {
        return father;
    }

    Integer getSon() {
        return son;
    }

    @Override
    public String toString() {
        return "Tuple{" +
                "father=" + father +
                ", son=" + son +
                '}';
    }
}

public class VisitorSolution {

    public static Tree solve() {
        Scanner input = new Scanner(System.in);

        final int n = Integer.parseInt(input.nextLine());

        List<Integer> values = new ArrayList<>();
        String[] valuesString = input.nextLine().split(" ");
        for (String s : valuesString) {
            values.add(Integer.parseInt(s));
        }

        List<Color> colors = new ArrayList<>();
        String[] colorsString = input.nextLine().split(" ");
        for (String s : colorsString ) {
            colors.add(Color.values()[Integer.parseInt(s)]);
        }

        Set<Integer> nodes = new HashSet<>();
        Set<Integer> leaves = new HashSet<>();
        Map<Integer, Integer> fatherMap = new HashMap<>();
        List<Tuple> relationships = new ArrayList<>();
        for (int i = 0; i < n-1; i++) {
            String[] xy = input.nextLine().split(" ");
            Integer father = Integer.parseInt(xy[0]) - 1;
            Integer son = Integer.parseInt(xy[1]) - 1;
            relationships.add(new Tuple(father, son));
            nodes.add(father);
            leaves.add(son);
            fatherMap.put(son, father);
        }
        leaves.removeAll(nodes);

        Integer[] depths = new Integer[n];
        Arrays.fill(depths, 0);

//        for(int leaf : leaves) {
//            depths[leaf] = 0;
//        }
//        for(int node : nodes) {
//            depths[node] = 1;
//        }

        boolean notBalanced;
        do {
            notBalanced = false;
            for(Tuple tuple : relationships) {
                Integer newDeep = 1 + depths[tuple.getSon()];
                if(newDeep > depths[tuple.getFather()]) {
                    notBalanced = true;
                    depths[tuple.getFather()] = newDeep;
                    changeMyFatherLine(tuple.getFather(), newDeep, fatherMap, depths);
                }
            }
        } while (notBalanced);

        Tree root = null;
        Tree[] treeArray = new Tree[n];
        List<Tree> treeList = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if(leaves.contains(i)) {
                treeArray[i] = new TreeLeaf(values.get(i), colors.get(i), depths[i]);
            } else {
                TreeNode newNode = new TreeNode(values.get(i), colors.get(i), depths[i]);
                treeArray[i] = newNode;
                if(root == null || root.getDepth() < newNode.getDepth())
                    root = newNode;
            }
        }

        for(Tuple tuple : relationships) {
            TreeNode node = (TreeNode) treeArray[tuple.getFather()];
            node.addChild(treeArray[tuple.getSon()]);
        }

        System.out.println("leaves.length = " + leaves.size());
        System.out.println("nodes.length = " + nodes.size());
        System.out.println("values.length = " + values.size());
        System.out.println("colors.length = " + colors.size());
        System.out.println("depths.length = " + depths.length);
        System.out.println("relationships = " + Arrays.toString(relationships.toArray()));
        System.out.println("root = " + ((TreeNode)root).printRoot());
        System.out.println("leaves = " + Arrays.toString(leaves.toArray()));
        System.out.println("nodes = " + Arrays.toString(nodes.toArray()));
        System.out.println("values = " + Arrays.toString(values.toArray()));
        System.out.println("colors = " + Arrays.toString(colors.toArray()));
        System.out.println("Arrays.toString(depths) = " + Arrays.toString(depths));

        treeList.addAll(Arrays.asList(treeArray));
        System.out.println("results");
        System.out.println("sum leaves = " + treeList.stream().filter(node -> node instanceof  TreeLeaf).map(Tree::getValue).reduce(0, (value0, value) -> value0 + value));
        System.out.println("red product = " + treeList.stream().filter(node -> node.getColor() == Color.RED).map(Tree::getValue).reduce(1, (value0, value) -> value0 * value));
        System.out.println("the end = " +
                ( treeList.stream().filter(node -> node.getDepth() % 2 == 0).map(Tree::getValue).reduce( (acc, value) -> acc + value).orElse(0)
                  -
                        treeList.stream().filter(node -> node.getColor() ==Color.GREEN && node instanceof TreeLeaf).map(Tree::getValue).reduce( (acc, value) -> acc + value).orElse(0)
                )
        );


        System.out.println("node depths  = " + Arrays.toString(
                treeList.stream().filter(node -> node instanceof  TreeNode).map(Tree::getDepth).collect(Collectors.toList()).toArray()));

        return root;

    }

    private static void changeMyFatherLine(Integer myIndex, Integer myNewDeep,
                                Map<Integer, Integer> fatherMap, Integer[] deeps) {

        Integer fatherIndex = fatherMap.get(myIndex);

        if(fatherIndex == null) return;

        Integer fatherDeep = deeps[fatherIndex];
        if(fatherDeep < (myNewDeep + 1)) {
            deeps[fatherIndex] = myNewDeep + 1;
            changeMyFatherLine(fatherIndex, deeps[fatherIndex], fatherMap, deeps);
        }

    }


    public static void main(String[] args) {
        Tree root = solve();
        SumInLeavesVisitor vis1 = new SumInLeavesVisitor();
        ProductOfRedNodesVisitor vis2 = new ProductOfRedNodesVisitor();
        FancyVisitor vis3 = new FancyVisitor();

        root.accept(vis1);
        root.accept(vis2);
        root.accept(vis3);

        int res1 = vis1.getResult();
        int res2 = vis2.getResult();
        int res3 = vis3.getResult();

        System.out.println(res1);
        System.out.println(res2);
        System.out.println(res3);
    }
}