package bearmaps.hw4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Zhiwei_Zhou
 * @create 2021-02-05 11:36
 */
public class MyTrieSet implements TrieSet61B{

    private node root;

    private class node{
        boolean isKEy;
        HashMap<Character, node> children;

        public node() {
            this.isKEy = false;
            children = new HashMap<>();
        }

        public void insertchild(Character c, node child) {
            children.put(c, child);
        }

        public boolean ischild(Character c) {
            return children.containsKey(c);
        }

        public node getchild(Character c) {
            return children.get(c);
        }

        public void setKEY() {
            isKEy = true;
        }

        public boolean isKEy() {
            return isKEy;
        }

        public HashMap<Character, node> getChildren() {
            return children;
        }
    }

    public MyTrieSet() {
        this.root = new node();
    }

    @Override
    public void clear() {
        root = new node();
    }

    @Override
    public boolean contains(String key) {
        if(key == null) {
            throw new IllegalArgumentException("the string cannot be null");
        }
        node point = root;
        while(!key.isEmpty()) {
            char c = key.charAt(0);
            if(!point.ischild(c)) return false;
            point = point.getchild(c);
            key = key.substring(1);
        }
        return point.isKEy();
    }

    @Override
    public void add(String key) {
        addhelper(root, key);
    }

    private node addhelper(node next, String word) {
        if(word.isEmpty()) {
            next.setKEY();
            return next;
        }
        char c = word.charAt(0);
        if(next.ischild(c)) {
            node a = next.getchild(c);
            next.insertchild(c, addhelper(a, word.substring(1)));
        } else {
            node a = new node();
            next.insertchild(c, addhelper(a, word.substring(1)));
        }
        return next;
    }

    @Override
    public List<String> keysWithPrefix(String prefix) {
        ArrayList<String> list = new ArrayList<>();
        node point = root;
        String pre = prefix;
        while(!prefix.isEmpty()) {
            char c = prefix.charAt(0);
            if(!point.ischild(c)) return null;
            point = point.getchild(c);
            prefix = prefix.substring(1);
        }

        for (char c:
             point.getChildren().keySet()) {
            colhelp(pre + c, list, point.getchild(c));
        }

        return list;
    }

    private void colhelp(String s, List<String> list, node n) {
        if(n.isKEy()) list.add(s);
        for (char c:
             n.getChildren().keySet()) {
            colhelp(s + c, list, n.getchild(c));
        }
    }

    @Override
    public String longestPrefixOf(String key) {
        throw new UnsupportedOperationException("has no code here");
    }

}
