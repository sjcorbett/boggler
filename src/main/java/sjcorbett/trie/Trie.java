package sjcorbett.trie;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Locale;

import com.google.common.base.MoreObjects;

public class Trie {

    public static class Node {
        final char character;
        boolean isTerminal;
        ArrayList<Node> children;

        private static final Comparator<Node> NODE_COMPARATOR = new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                return o1.character - o2.character;
            }
        };

        private Node(char character) {
            this.character = character;
        }

        public Node getChild(char character) {
            if (children == null) return null;
//            int index = Collections.binarySearch(children, new Node(character), NODE_COMPARATOR);
//            if (index >= 0) {
//                return children.get(index);
//            } else {
//                return null;
//            }
            for (Node node : children) {
                if (node.character == character) {
                    return node;
                }
            }
            return null;
        }

        public boolean isTerminal() {
            return isTerminal;
        }

        void addChild(Node node) {
            if (children == null) {
                children = new ArrayList<>();
                children.add(node);
            } else {
                // Maintain sorted order.
//                int i = 0;
//                for (; i < children.size() && (children.get(i).character < node.character); i++) {}
//                children.add(i, node);
                children.add(node);
            }
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("char", character)
                    .add("terminal", isTerminal)
                    .toString();
        }
    }

    private final Node ROOT = new Node('#');

    public boolean isWord(String word) {
        word = word.toUpperCase(Locale.ENGLISH);
        Node node = ROOT;
        for (char character : word.toCharArray()) {
            node = node.getChild(character);
            if (node == null) {
                return false;
            }
        }
        return node.isTerminal;
    }

    public Node getRootNode() {
        return ROOT;
    }

    public Node getNodeFor(char character) {
        return ROOT.getChild(character);
    }

    public Trie add(String word) {
        word = word.toUpperCase(Locale.ENGLISH);
        Node current = ROOT;
        for (char character : word.toCharArray()) {
            Node next = current.getChild(character);
            if (next == null) {
                next = new Node(character);
                current.addChild(next);
            }
            current = next;
        }
        current.isTerminal = true;
        return this;
    }

    public Trie addAll(Iterable<String> words) {
        for (String word : words) {
            add(word);
        }
        return this;
    }

}
