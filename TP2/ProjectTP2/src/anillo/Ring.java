//package anillo;
//
//public class Ring {
//    private Link current;
//    private static class Link {
//        Object value;
//        Link next;
//        Link(Object v) {
//            value = v;
//            next = this;
//        }
//    }
//
//    public Ring() {
//        current = null;
//    }
//
//    public Ring add(Object cargo) {
//        Link newLink = new Link(cargo);
//        if (current == null) {
//            current = newLink;
//        } else {
//            Link prev = getPrev();
//            prev.next = newLink;
//            newLink.next = current;
//        }
//        current = newLink;
//        return this;
//    }
//
//    public Ring next() {
//        if (current == null) throw new IllegalStateException("Ring is empty");
//        current = current.next;
//        return this;
//    }
//
//    public Object current() {
//        if (current == null) throw new IllegalStateException("Ring is empty");
//        return current.value;
//    }
//
//    public Ring remove() {
//        if (current == null) {
//            return this;
//        }
//        if (current.next == current) {
//            current = null;
//        } else {
//            Link prev= getPrev();
//            prev.next = current.next;
//            current = current.next;
//        }
//        return this;
//    }
//
//    private Link getPrev(){
//        Link prev = current;
//        while (prev.next != current) {
//            prev = prev.next;
//        }
//        return prev;
//    }
//}

package anillo;

public class Ring {

    private Link current = new EmptyLink();

    public Ring add(Object cargo) {
        current = current.add(cargo);
        return this;
    }

    public Ring next() {
        current = current.next();
        return this;
    }

    public Object current() {
        return current.current();
    }

    public Ring remove() {
        current = current.remove();
        return this;
    }

    // ---------- Interfaz Link ----------
    private interface Link {
        Link add(Object cargo);
        Link next();
        Object current();
        Link remove();
        Link getPrev(Link from);
        void setNext(Link next);
    }

    // ---------- Clase para anillo vacío ----------
    private static class EmptyLink implements Link {

        public Link add(Object cargo) {
            // crea el primer eslabón que apunta a sí mismo
            ElementLink newLink = new ElementLink(cargo);
            newLink.setNext(newLink);
            return newLink;
        }

        public Link next() {
            throw new IllegalStateException("Ring is empty");
        }

        public Object current() {
            throw new IllegalStateException("Ring is empty");
        }

        public Link remove() {
            return this;
        }

        public Link getPrev(Link from) {
            return this;
        }

        public void setNext(Link next) {
            // no hace nada
        }
    }

    // ---------- Clase para un eslabón con valor ----------
    private static class ElementLink implements Link {
        private final Object value;
        private Link next;

        public ElementLink(Object value) {
            this.value = value;
        }

        public Link add(Object cargo) {
            ElementLink newLink = new ElementLink(cargo);
            Link prev = getPrev(this);
            prev.setNext(newLink);
            newLink.setNext(this);
            return newLink;
        }

        public Link next() {
            return next;
        }

        public Object current() {
            return value;
        }

        public Link remove() {
            if (next == this) {
                return new EmptyLink(); // era el único
            } else {
                Link prev = getPrev(this);
                prev.setNext(next);
                return next;
            }
        }

        public Link getPrev(Link from) {
            Link link = this;
            while (link.next() != from) {
                link = link.next();
            }
            return link;
        }

        public void setNext(Link next) {
            this.next = next;
        }
    }
}