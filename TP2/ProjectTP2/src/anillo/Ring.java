package anillo;

public class Ring {

    private Link current = new EmptyLink();

    public Ring add( Object cargo ) {
        current = current.add( cargo );
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

    private static abstract class Link {
        abstract Link add( Object cargo );
        abstract Link next();
        abstract Object current();
        abstract Link remove();
        abstract void setNext( Link next );
        abstract void setPrev( Link prev );
        abstract Link prev();
    }

    private static class EmptyLink extends Link {

        public Link add( Object cargo ) {
            ElementLink newLink = new ElementLink( cargo );
            newLink.setNext( newLink );
            newLink.setPrev( newLink );
            return newLink;
        }

        public Link next() {
            throw new IllegalStateException( "Ring is empty" );
        }

        public Object current() {
            throw new IllegalStateException( "Ring is empty" );
        }

        public Link remove() {
            return this;
        }

        public void setNext( Link next ) { }
        public void setPrev( Link prev ) { }
        public Link prev() { return this; }
    }

    private static class ElementLink extends Link {
        private final Object value;
        private Link next;
        private Link prev;

        public ElementLink( Object value ) {
            this.value = value;
        }

        public Link add( Object cargo ) {
            ElementLink newLink = new ElementLink( cargo );

            newLink.setNext( this );
            newLink.setPrev( prev );

            prev.setNext( newLink );
            this.setPrev( newLink );
            return newLink;
        }

        public Link next() {
            return next;
        }

        public Object current() {
            return value;
        }

        public Link remove() {
            if ( next == this ) {
                return new EmptyLink();
            } else {
                prev.setNext( next );
                next.setPrev( prev );
                return next;
            }
        }

        public void setNext( Link next ) {
            this.next = next;
        }

        public void setPrev( Link prev ) {
            this.prev = prev;
        }

        public Link prev() {
            return prev;
        }
    }
}