package com.jwetherell.algorithms.data_structures;


/**
 * An trie used to store key->values pairs, this is an implementation of an
 * associative array.
 * 
 * http://en.wikipedia.org/wiki/Trie
 * http://en.wikipedia.org/wiki/Associative_array
 * 
 * @author Justin Wetherell <phishman3579@gmail.com>
 */
public class TrieMap<C extends CharSequence, V> extends Trie<C> {

    public TrieMap() {
        root = new MapNode<C, V>(null, null);
    }

    @SuppressWarnings("unchecked")
    public boolean put(C key, V value) {
        int length = (key.length() - 1);
        Node<C> prev = root;
        for (int i = 0; i < length; i++) {
            Node<C> n = null;
            Character c = key.charAt(i);
            int index = prev.childIndex(c);
            if (index >= 0) {
                n = prev.getChild(index);
            } else {
                n = new MapNode<C, V>(prev, c);
                prev.addChild(n);
            }
            prev = n;
        }

        MapNode<C, V> n = null;
        Character c = key.charAt(length);
        int index = prev.childIndex(c);
        if (index >= 0) {
            n = (MapNode<C, V>) prev.getChild(index);
            if (n.value == null) {
                n.character = c;
                n.isWord = true;
                n.value = value;
                size++;
                return true;
            } else {
                return false;
            }
        } else {
            n = new MapNode<C, V>(prev, c, true, value);
            prev.addChild(n);
            size++;
            return true;
        }
    }

    @SuppressWarnings("unchecked")
    public V get(C key) {
        if (root == null) return null;

        MapNode<C, V> n = (MapNode<C, V>) root;
        int length = (key.length() - 1);
        for (int i = 0; i <= length; i++) {
            char c = key.charAt(i);
            int index = n.childIndex(c);
            if (index >= 0) {
                n = (MapNode<C, V>) n.getChild(index);
            } else {
                return null;
            }
        }
        if (n != null) return n.value;
        return null;
    }

    @Override
    public boolean add(C String) {
        // This should not be used
        throw new RuntimeException("This method is not supported");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return TrieMapPrinter.getString(this);
    }

    protected static class MapNode<C extends CharSequence, V> extends Node<C> {

        protected V value = null;

        protected MapNode(Node<C> parent, Character character) {
            super(parent, character, false);
        }

        protected MapNode(Node<C> parent, Character character, boolean isWord, V value) {
            super(parent, character, isWord);
            this.value = value;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            if (value != null) builder.append("key=").append(isWord).append(" value=").append(value).append("\n");
            for (int i=0; i<getChildrenSize(); i++) {
                Node<C> c = getChild(i);
                builder.append(c.toString());
            }
            return builder.toString();
        }
    }

    protected static class TrieMapPrinter {

        public static <C extends CharSequence, V> String getString(TrieMap<C, V> map) {
            return getString(map.root, "", true);
        }

        @SuppressWarnings("unchecked")
        protected static <C extends CharSequence, V> String getString(Node<C> node, String prefix, boolean isTail) {
            StringBuilder builder = new StringBuilder();

            if (node instanceof MapNode) {
                MapNode<C, V> hashNode = (MapNode<C, V>) node;
                builder.append(prefix + (isTail ? "└── " : "├── ") + 
                        ((node.isWord == true) ? 
                            ("(" + node.character + ") = " + hashNode.value) 
                        : 
                            node.character) + "\n");
            } else {
                builder.append(prefix + (isTail ? "└── " : "├── ") + 
                        ((node.isWord == true) ? 
                            ("(" + node.character + ") " + node.isWord) 
                        : 
                            node.character) + "\n");
            }
            if (node.getChildrenSize()>0) {
                for (int i = 0; i < node.getChildrenSize() - 1; i++) {
                    builder.append(getString(node.getChild(i), prefix + (isTail ? "    " : "│   "), false));
                }
                if (node.getChildrenSize() >= 1) {
                    builder.append(getString(node.getChild(node.getChildrenSize() - 1), prefix + (isTail ? "    " : "│   "), true));
                }
            }

            return builder.toString();
        }
    }
}
