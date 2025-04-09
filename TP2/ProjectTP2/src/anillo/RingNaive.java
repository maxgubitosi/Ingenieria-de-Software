package anillo;

public class RingNaive {
    private Link current;
    private static class Link {
        Object value;
        Link next;
        Link(Object v) {
            value = v;
            next = this;
        }
    }

    public RingNaive() {
        current = null;
    }

    public RingNaive add(Object cargo) {
        Link newLink = new Link(cargo);
        if (current == null) {
            current = newLink;
        } else {
            Link prev = getPrev();
            prev.next = newLink;
            newLink.next = current;
        }
        current = newLink;
        return this;
    }

    public RingNaive next() {
        if (current == null) throw new IllegalStateException("RingNaive is empty");
        current = current.next;
        return this;
    }

    public Object current() {
        if (current == null) throw new IllegalStateException("RingNaive is empty");
        return current.value;
    }

    public RingNaive remove() {
        if (current == null) {
            return this;
        }
        if (current.next == current) {
            current = null;
        } else {
            Link prev= getPrev();
            prev.next = current.next;
            current = current.next;
        }
        return this;
    }

    private Link getPrev(){
        Link prev = current;
        while (prev.next != current) {
            prev = prev.next;
        }
        return prev;
    }
}