import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

//node class with constructor to create the key and height for each node
// key = value in node
// height = node height
class Node {
    int key;
    Node left;
    Node right;
    int height;

    public Node(int placeholder) {
        key = placeholder;
        height = 0; 
    }
}

//queue to store each node 
class Queue {
    Node node;
    Queue next;
    Queue(Node placeholder) {
        this.node = placeholder;
        this.next = null;
    }
}

public class AVLTreeAssignment {
    Node rootNode;

    //this method gets the node height
    // if not null then return height
    int height(Node placeholder) {
        if (placeholder == null){
            return -1;
        } else {
            return placeholder.height;
        }
    }

    //this method gets balance factor 
    // if not null then return height 
    // BF = height(left) - height(right)
    int getBalance(Node placeholder) {
        if (placeholder == null) {
            return 0;
        } else {
            return height(placeholder.left) - height(placeholder.right);
        }
    }

    // this method sets right rotate 
    Node rightRotate(Node x){
        Node newParent = x.left;
        Node otherTree = newParent.right;
        newParent.right = x;
        x.left = otherTree;
        int leftHeight = height(x.left);
        int rightHeight = height(x.right);
        if (leftHeight > rightHeight) {
            x.height = leftHeight + 1;
        } else {
            x.height = rightHeight + 1;
        }
        int newParentLeftHeight = height(newParent.left);
        int newParentRightHeight = height(newParent.right); 
        if (newParentLeftHeight > newParentRightHeight) {
            newParent.height = newParentLeftHeight + 1;
        } else {
            newParent.height = newParentRightHeight + 1;
        }
        return newParent;
    }

    //this method sets left rotate
    Node leftRotate(Node x) {
        Node newParent = x.right;
        Node otherTree = newParent.left;
        newParent.left = x;
        x.right = otherTree;
        int leftHeight = height(x.left);
        int rightHeight = height(x.right);
        if (leftHeight > rightHeight) {
            x.height = leftHeight + 1;
        } else {
            x.height = rightHeight + 1;
        }
        int newParentLeftHeight = height(newParent.left);
        int newParentRightHeight = height(newParent.right);
        if (newParentLeftHeight > newParentRightHeight) {
            newParent.height = newParentLeftHeight + 1;
        } else {
            newParent.height = newParentRightHeight + 1;
        }
        return newParent;
    }

    //this method rebalances the tree after each insertion
    //if balance > 1 then left heavy
    //if balance < -1 then right heavy
    //if balance > 1 and left child balance >= 0 then right rotate
    //if balance < -1 and right child balance <= 0 then left rotate
    Node rebalance(Node placeholderNode) {
        placeholderNode.height = Math.max(height(placeholderNode.left), height(placeholderNode.right)) + 1;
        int balance = getBalance(placeholderNode);

        if (balance > 1 && getBalance(placeholderNode.left) >= 0) {
            return rightRotate(placeholderNode);
        }
        if (balance < -1 && getBalance(placeholderNode.right) <= 0) {
            return leftRotate(placeholderNode);
        }
        if (balance > 1 && getBalance(placeholderNode.left) < 0) {
            placeholderNode.left = leftRotate(placeholderNode.left);
            return rightRotate(placeholderNode);
        }
        if (balance < -1 && getBalance(placeholderNode.right) > 0) {
            placeholderNode.right = rightRotate(placeholderNode.right);
            return leftRotate(placeholderNode);
        }
        return placeholderNode;
    }

    //this method puts key into tree and rebalances 
    //if key < then go left
    //if key > then go right
    Node insert(Node placeholderNode, int key) {
        if (placeholderNode == null)
            return new Node(key);

        if (key < placeholderNode.key)
            placeholderNode.left = insert(placeholderNode.left, key);
        else if (key > placeholderNode.key)
            placeholderNode.right = insert(placeholderNode.right, key);
        else
            return placeholderNode;

        placeholderNode = rebalance(placeholderNode);
        return placeholderNode;
    }

    //print tree using levels
    public void printLevelOrder(PrintWriter writer) {
        if (rootNode == null) {
            return;
        }

        Queue head = new Queue(rootNode);
        Queue tail = head;
        
        int nodesInCurrentLevel = 1; 
        int nodesInNextLevel = 0;

        while (nodesInCurrentLevel != 0) {
            Node curr = head.node;
            head = head.next; 
            
            writer.print(curr.key + "(Height-" + curr.height + " Balance-" + getBalance(curr) + ") ");
            nodesInCurrentLevel--;

            if (curr.left != null) {
                Queue newNode = new Queue(curr.left);
                if (head == null) { 
                    head = newNode; tail = newNode; 
                }
                else { 
                    tail.next = newNode; tail = tail.next; 
                }
                nodesInNextLevel++;
            }

            if (curr.right != null) {
                Queue newNode = new Queue(curr.right);
                if (head == null) { 
                    head = newNode; tail = newNode; 
                }
                else { 
                    tail.next = newNode; tail = tail.next; 
                }
                nodesInNextLevel++;
            }

            if (nodesInCurrentLevel == 0) {
                nodesInCurrentLevel = nodesInNextLevel;
                nodesInNextLevel = 0;
                writer.println(); 
            }
        }
    }

    public static void main(String[] args) {
        AVLTreeAssignment avlTree = new AVLTreeAssignment();
        try {
            Scanner getScanner = new Scanner(new File("input.txt"));
            while (getScanner.hasNextInt()) {
                avlTree.rootNode = avlTree.insert(avlTree.rootNode, getScanner.nextInt());
            }
            getScanner.close();
            PrintWriter object = new PrintWriter("output.txt");
            avlTree.printLevelOrder(object);
            object.close();
            System.out.println("Done");
        } catch (FileNotFoundException e) {
            System.out.println("Cannot get file");
        }
    }
}