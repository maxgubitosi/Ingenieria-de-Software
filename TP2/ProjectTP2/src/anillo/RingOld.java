package anillo;

public class RingOld {

    private Link currentLink = new EmptyLink();

    public RingOld add(Object cargo ) {
        currentLink = currentLink.addPrevLink( cargo );
        return this;
    }

    public RingOld next() {
        currentLink = currentLink.next();
        return this;
    }

    public Object current() {
        return currentLink.getCargo();
    }

    public RingOld remove() {
        currentLink = currentLink.removeMyself();
        return this;
    }

    private static abstract class Link {
        abstract Link addPrevLink( Object cargo );
        abstract Link next();
        abstract Object getCargo();
        abstract Link removeMyself();
        abstract void setNext( Link next );
        abstract void setPrev( Link prev );
    }

    private static class EmptyLink extends Link {

        public Link addPrevLink( Object cargo ) {
            ElementLink newLink = new ElementLink( cargo );
            newLink.setNext( newLink );
            newLink.setPrev( newLink );
            return newLink;
        }

        public Link next() {
            throw new IllegalStateException( "Ring is empty" );
        }

        public Object getCargo() {
            throw new IllegalStateException( "Ring is empty" );
        }

        public Link removeMyself() {
            return this;
        }

        public void setNext( Link next ) { }
        public void setPrev( Link prev ) { }
    }

    private static class ElementLink extends Link {
        private final Object value;
        private Link next;
        private Link prev;

        public ElementLink( Object value ) {
            this.value = value;
        }

        public Link addPrevLink( Object cargo ) {
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

        public Object getCargo() {
            return value;
        }

        public Link removeMyself() {
            // Reemplazar con 'dameElProxCurr()'
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

    }
}